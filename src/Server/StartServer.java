package Server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class StartServer {

	public static void main(String[] args) {
		System.out.println(
				"Willkommen zum UniChat - Server Interface\n" + "================================================\n");
		Server server = new Server(55555);
		InetAddress inet = null;
		try {
			inet = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		System.out.println("Server auf Port " + server.getServerPort() + " erreichbar unter " + inet + " gestartet.");
		Thread t = new Thread(server);
		t.start();
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.print("\nZum Beenden des Server bestätigen Sie bitte mit \"end\": ");
			if (scanner.nextLine().equals("end")) {
				System.out.println("Server beendet.");
				scanner.close();
				t.interrupt();
				System.exit(0);
			} else {
				System.out.println("Falsche Eingabe");
			}
		}
	}

}
