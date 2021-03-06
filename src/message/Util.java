/**
 * 
 */
package message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author Nikolai Seip, Sebastian Mattheis, Fabian Hinz
 *
 */
public class Util {

	/**
	 * wraps the message to a DatagramPacket
	 * 
	 * @param iadr
	 *            the InetAddress of the recipient
	 * @param port
	 *            port of the recipient
	 * @return a DatagramPacket
	 */
	public static DatagramPacket getMessageAsDatagrammPacket(Message message, InetAddress serverAdresse,
			int serverPort) {
		ByteArrayOutputStream byteArrOutStream = new ByteArrayOutputStream();
		try (ObjectOutputStream objOut = new ObjectOutputStream(byteArrOutStream);) {
			objOut.writeObject(message);
		} catch (IOException e) {
			throw new RuntimeException();
		}
		byte[] buffer = byteArrOutStream.toByteArray();
		DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, serverAdresse, serverPort);
		datagramPacket.setData(buffer);
		return datagramPacket;
	}


	/**
	 * wraps the data out of the DatagramPacket to a Message-Object
	 * 
	 * @param datagramPacket
	 *            the DatagramPacket
	 * @return message-object
	 */
	public static Message getMessageOutOfDatagramPacket(DatagramPacket datagramPacket) {
		byte[] buf = datagramPacket.getData();
		ByteArrayInputStream byteArrInStream = new ByteArrayInputStream(buf); // von
																				// Datagram

		Message message = null;
		try (ObjectInputStream objInStream = new ObjectInputStream(byteArrInStream);) {
			message = (Message) objInStream.readObject();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException();
		} catch (IOException e) {
			throw new RuntimeException();
		}
		return message;
	}


	/**
	 * A method that opens a TCP connection with a server, sends a message,
	 * waits for an answer and closes the connection
	 * 
	 * @param message
	 *            The message that is supposed to be sent
	 * @param address
	 *            The address of the target server
	 * @param serverPort
	 *            The open port of the target server
	 * @return The Message response of the server
	 * @throws IOException
	 */
	public static Message sendAndGetMessage(Message message, InetAddress serverAdresse, int serverPort)
			throws IOException {
		// TODO Auf Port binden
		try (Socket server = new Socket(serverAdresse, serverPort);
				ObjectOutputStream out = new ObjectOutputStream(server.getOutputStream());
				ObjectInputStream in = new ObjectInputStream(server.getInputStream());) {

			out.writeObject(message);
			out.flush();
			return (Message) in.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException();
		}

	}


	/**
	 * Checks if it is possible to establish a TCP connection to a server with
	 * specified address and port
	 * 
	 * @param address
	 *            The address of the target server
	 * @param serverPort
	 *            The port to be checked
	 * @param timeout
	 *            Timeout of the connection
	 * @return A boolean indicating success or failure
	 */
	public static boolean testConnection(InetAddress adress, int serverPort, int timeout) {

		try (Socket server = new Socket();) {
			server.connect(new InetSocketAddress(adress, serverPort), timeout);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
			return true;
		} catch (IOException e) {
			return false;
		}
	}
}
