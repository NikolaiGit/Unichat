package Client;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class StartClient {
	private static ClientIF client = null;
	private static ClientGUI gui = null;


	public static void main(String[] args) {
		client = new Client();
		gui = new ClientGUI(client);
		client.setGUI(gui);
	}

}

/*
 * while (true) { String name = JOptionPane.showInputDialog(null,
 * "Geben Sie Ihren Namen ein: ", "Installation UniChat",
 * JOptionPane.PLAIN_MESSAGE);
 * 
 * String adresse = JOptionPane.showInputDialog(null,
 * "Geben Sie die Adresse eines Servers ein: \n Format: \"xxx.xxx.xxx.xxx\"",
 * "Installation UniChat", JOptionPane.PLAIN_MESSAGE);
 * 
 * try {
 * 
 * break; } catch (IOException e2) { JOptionPane.showMessageDialog(null,
 * "Es ist kein Server unter der angegebenen Adresse erreichbar ...",
 * "Installation UniChat", JOptionPane.WARNING_MESSAGE);
 * 
 * } }
 */