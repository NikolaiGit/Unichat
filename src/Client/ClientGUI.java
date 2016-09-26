package Client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import message.dataChat;

public class ClientGUI extends JFrame implements ComponentListener {
	private static final long serialVersionUID = 1L;
	private JPanel isRegisteredLeftSidePanel, isNotRegisteredLeftSidePanel, inChatLeftSidePanel, registerRightSidePanel,
			enterChatRightSidePanel, chatRightSidePanel, chatRightSideSendMessagePanel, createChatRightSidePanel,
			chatRightSideShowMessagePanel, chatRightSideInfoChatPanel;
	private ClientIF client;
	private String nameUser, adresseServer, neuerChatName, neuerChatPasswort;
	private JLabel falscherNameOderAdresse, fehlerBeiCreateChat, falscherNameOderPasswort;
	private JTextField registerRightSidePanelTextFieldName, registerRightSidePanelTextFieldAdresse,
			createChatRightSidePanelName, createChatRightSidePanelPasswort, enterChatRightSidePanelPasswort, newMessage;
	private JComboBox<String> enterChatRightSidePanelName;
	private LinkedList<dataChat> verfuegbareChats;
	private String chatname;
	private JLabel chatnameLabel;
	LinkedList<JLabel> showedMessages;


	/*
	 * TODO schöner lösen mit keine 4 Buttons einzeln schreiben zum Beispiel nur
	 * anonyme Buttons dem panel zuweisen indem einfach ne Methode aufgerufen
	 * wird, der das Panel, der ActionListener und die Dimension übergeben wird
	 */

	public ClientGUI(ClientIF c) {
		client = c;
		verfuegbareChats = new LinkedList<>();
		showedMessages = new LinkedList<>();
		this.setTitle("UniChat");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(null);
		this.setSize(800, 600);
		this.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width / 2) - (this.getWidth() / 2),
				Toolkit.getDefaultToolkit().getScreenSize().height / 2 - (this.getHeight() / 2));
		this.addComponentListener(this);
		buildAll();
		this.setVisible(true);
		isNotRegisteredLeftSidePanel.setVisible(true);
	}


	private void buildAll() {

		//
		// isNotRegisteredLeftSidePanel
		//
		isNotRegisteredLeftSidePanel = new JPanel();
		isNotRegisteredLeftSidePanel.setLayout(new GridLayout(2, 1, 50, 50));
		addButton(isNotRegisteredLeftSidePanel, new JButton(), new RegisterClient1ActionListener(),
				new Dimension(200, 30), "Am Server registrieren ...");
		addButton(isNotRegisteredLeftSidePanel, new JButton(), new EndClientActionListener(), new Dimension(200, 30),
				"Client beenden... ");
		isNotRegisteredLeftSidePanel.setVisible(false);
		this.add(isNotRegisteredLeftSidePanel);

		//
		// isRegisteredLeftSidePanel
		//
		isRegisteredLeftSidePanel = new JPanel();
		isRegisteredLeftSidePanel.setLayout(new GridLayout(4, 1, 50, 50));
		addButton(isRegisteredLeftSidePanel, new JButton(), new CreateChat1ActionListener(), new Dimension(300, 30),
				"Neuen Chat erstellen ...");
		addButton(isRegisteredLeftSidePanel, new JButton(), new EnterChat1ActionListener(), new Dimension(300, 30),
				"Trete einem Chat bei ...");
		addButton(isRegisteredLeftSidePanel, new JButton(), new DeregisterClientActionListener(),
				new Dimension(400, 30), "Melde diesen Client am Server ab ...");
		addButton(isRegisteredLeftSidePanel, new JButton(), new EndClientActionListener(), new Dimension(200, 30),
				"Client beenden ...");
		isRegisteredLeftSidePanel.setVisible(false);
		this.add(isRegisteredLeftSidePanel);

		//
		// inChatLeftSidePanel
		//
		inChatLeftSidePanel = new JPanel();
		inChatLeftSidePanel.setVisible(false);
		inChatLeftSidePanel.setLayout(new GridLayout(2, 1, 50, 50));
		addButton(inChatLeftSidePanel, new JButton(), new LeaveChatActionListener(), new Dimension(200, 30),
				"Chat verlassen ...");
		addButton(inChatLeftSidePanel, new JButton(), new EndClientActionListener(), new Dimension(200, 30),
				"Client beenden ...");
		inChatLeftSidePanel.setVisible(false);
		this.add(inChatLeftSidePanel);

		//
		// registerRightSidePanel
		//
		registerRightSidePanel = new JPanel();
		registerRightSidePanel.setLayout(new GridLayout(3, 2));
		registerRightSidePanel.add(new JLabel("Ihr Name bitte: "));
		registerRightSidePanelTextFieldName = new JTextField();
		registerRightSidePanel.add(registerRightSidePanelTextFieldName);
		registerRightSidePanel.add(new JLabel("Adresse des Servers: "));
		registerRightSidePanelTextFieldAdresse = new JTextField();
		registerRightSidePanel.add(registerRightSidePanelTextFieldAdresse);
		addButton(registerRightSidePanel, new JButton(), new RegisterClient2ActionListener(), new Dimension(50, 30),
				"OK");
		falscherNameOderAdresse = new JLabel("Leider waren die angegeben Daten fehlerhaft");
		falscherNameOderAdresse.setVisible(false);
		registerRightSidePanel.add(falscherNameOderAdresse);
		registerRightSidePanel.setVisible(false);
		this.add(registerRightSidePanel);

		//
		// createChatRightSidePanel
		//
		createChatRightSidePanel = new JPanel();
		createChatRightSidePanel.setLayout(new GridLayout(3, 2));
		createChatRightSidePanel.add(new JLabel("Name des Chats: "));
		createChatRightSidePanelName = new JTextField();
		createChatRightSidePanel.add(createChatRightSidePanelName);
		createChatRightSidePanel.add(new JLabel("Passwort für diesen Chat: "));
		createChatRightSidePanelPasswort = new JTextField();
		createChatRightSidePanel.add(createChatRightSidePanelPasswort);
		addButton(createChatRightSidePanel, new JButton(), new CreateChat2ActionListener(), new Dimension(50, 30),
				"OK");
		fehlerBeiCreateChat = new JLabel("Die maximale Anzahl an Chats ist leider schon erreicht...");
		fehlerBeiCreateChat.setVisible(false);
		createChatRightSidePanel.add(fehlerBeiCreateChat);
		createChatRightSidePanel.setVisible(false);
		this.add(createChatRightSidePanel);

		//
		// enterChatRightSidePanel
		//
		enterChatRightSidePanel = new JPanel();
		enterChatRightSidePanel.setLayout(new GridLayout(3, 2));
		enterChatRightSidePanel.add(new JLabel("Verfügbare Chats: "));
		enterChatRightSidePanelName = new JComboBox<>();
		enterChatRightSidePanelName.setEditable(false);
		enterChatRightSidePanelName.setMaximumRowCount(10);
		enterChatRightSidePanel.add(enterChatRightSidePanelName);
		enterChatRightSidePanel.add(new JLabel("Passwort des Chats: "));
		enterChatRightSidePanelPasswort = new JTextField();
		enterChatRightSidePanel.add(enterChatRightSidePanelPasswort);
		falscherNameOderPasswort = new JLabel("Passwort oder Name sind falsch");
		falscherNameOderPasswort.setVisible(false);
		enterChatRightSidePanel.add(falscherNameOderPasswort);
		addButton(enterChatRightSidePanel, new JButton(), new EnterChat2ActionListener(), new Dimension(50, 30), "OK");
		enterChatRightSidePanel.setVisible(false);
		this.add(enterChatRightSidePanel);

		//
		// chatRightSidePanel
		//
		chatRightSidePanel = new JPanel();
		chatRightSidePanel.setLayout(null);
		chatRightSidePanel.setSize(this.getWidth() * 75 / 100, this.getHeight());
		chatRightSidePanel.setLocation(this.getWidth() * 25 / 100, 0);
		chatRightSidePanel.setBackground(Color.RED);

		chatRightSideInfoChatPanel = new JPanel();
		chatRightSideInfoChatPanel.setSize(chatRightSidePanel.getWidth(), chatRightSidePanel.getHeight() * 10 / 100);
		chatRightSideInfoChatPanel.setLocation(0, 0);
		chatnameLabel = new JLabel(" ");
		chatnameLabel.setSize(chatRightSideInfoChatPanel.getWidth(), 100);
		chatnameLabel.setBackground(Color.RED);
		chatnameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		chatRightSideInfoChatPanel.add(chatnameLabel);
		chatRightSidePanel.add(chatRightSideInfoChatPanel);

		chatRightSideShowMessagePanel = new JPanel();
		chatRightSideShowMessagePanel.setSize(chatRightSidePanel.getWidth(), chatRightSidePanel.getHeight() * 70 / 100);
		chatRightSideShowMessagePanel.setLocation(0, chatRightSideInfoChatPanel.getHeight());
		chatRightSideShowMessagePanel.setLayout(new BoxLayout(chatRightSideShowMessagePanel, BoxLayout.Y_AXIS));
		chatRightSideShowMessagePanel.setVisible(true);
		chatRightSidePanel.add(chatRightSideShowMessagePanel);

		chatRightSideSendMessagePanel = new JPanel();
		chatRightSideSendMessagePanel.setLayout(null);
		chatRightSideSendMessagePanel.setSize(chatRightSidePanel.getWidth(), chatRightSidePanel.getHeight() * 20 / 100);
		newMessage = new JTextField();
		newMessage.setSize(chatRightSideSendMessagePanel.getWidth() * 85 / 100,
				chatRightSideSendMessagePanel.getHeight());
		newMessage.setLocation(0, 0);
		chatRightSideSendMessagePanel.add(newMessage);
		addButton(chatRightSideSendMessagePanel, new JButton(), new SendMessageActionListener(),
				new Dimension(chatRightSideSendMessagePanel.getWidth() * 15 / 100,
						chatRightSideSendMessagePanel.getHeight()),
				">", new Point(chatRightSideSendMessagePanel.getWidth() * 85 / 100, 0));

		chatRightSideSendMessagePanel.setLocation(0,
				chatRightSideShowMessagePanel.getHeight() + chatRightSideInfoChatPanel.getHeight());
		chatRightSideSendMessagePanel.setVisible(true);
		chatRightSidePanel.add(chatRightSideSendMessagePanel);

		this.add(chatRightSidePanel);
		chatRightSidePanel.setVisible(false);

		//
		//
		//
		//
		//
		//
		resizeAllComponents();
	}


	private void allRightSidePanelsVisibilityfalse() {
		registerRightSidePanel.setVisible(false);
		enterChatRightSidePanel.setVisible(false);
		chatRightSidePanel.setVisible(false);
		createChatRightSidePanel.setVisible(false);
	}


	private void resizeAllComponents() {
		isNotRegisteredLeftSidePanel.setSize(this.getWidth() * 25 / 100, this.getHeight());
		isNotRegisteredLeftSidePanel.setLocation(0, 0);
		isRegisteredLeftSidePanel.setSize(this.getWidth() * 25 / 100, this.getHeight());
		isRegisteredLeftSidePanel.setLocation(0, 0);
		inChatLeftSidePanel.setSize(this.getWidth() * 25 / 100, this.getHeight());
		inChatLeftSidePanel.setLocation(0, 0);
		enterChatRightSidePanel.setSize(this.getWidth() * 75 / 100, this.getHeight());
		enterChatRightSidePanel.setLocation(this.getWidth() * 25 / 100, 0);
		chatRightSidePanel.setSize(this.getWidth() * 75 / 100, this.getHeight());
		chatRightSidePanel.setLocation(this.getWidth() * 25 / 100, 0);
		chatRightSideInfoChatPanel.setSize(chatRightSidePanel.getWidth(), chatRightSidePanel.getHeight() * 10 / 100);
		chatRightSideInfoChatPanel.setLocation(0, 0);
		chatRightSideShowMessagePanel.setSize(chatRightSidePanel.getWidth(), chatRightSidePanel.getHeight() * 70 / 100);
		chatRightSideShowMessagePanel.setLocation(0, chatRightSideInfoChatPanel.getHeight());
		chatRightSideSendMessagePanel.setSize(chatRightSidePanel.getWidth(), chatRightSidePanel.getHeight() * 20 / 100);
		chatRightSideSendMessagePanel.setLocation(0,
				chatRightSideShowMessagePanel.getHeight() + chatRightSideInfoChatPanel.getHeight());

		registerRightSidePanel.setSize(this.getWidth() * 75 / 100, this.getHeight());
		registerRightSidePanel.setLocation(this.getWidth() * 25 / 100, 0);
		createChatRightSidePanel.setSize(this.getWidth() * 75 / 100, this.getHeight());
		createChatRightSidePanel.setLocation(this.getWidth() * 25 / 100, 0);

		newMessage.setLocation(0, 0);
		newMessage.setSize(chatRightSideSendMessagePanel.getWidth() * 90 / 100,
				chatRightSideSendMessagePanel.getHeight());
		this.repaint();
	}


	public void addButton(JPanel container, JButton button, ActionListener l, Dimension d, String text) {
		button.setText(text);
		button.setSize(d);
		button.addActionListener(l);
		container.add(button);
	}


	public void addButton(JPanel container, JButton button, ActionListener l, Dimension d, String text,
			Point location) {
		button.setText(text);
		button.setSize(d);
		button.addActionListener(l);
		button.setLocation(location);
		container.add(button);
	}


	@Override
	public void repaint() {
		super.repaint();
	}


	public void repaintMessagePanel() {
		System.out.println("Repaint-Befehl von Client");

		// hole dir die neuen Nachrichten
		List<String> list = client.getNewMessages();
		// stecke die neuen nachrichten in zwei JLabels getrennt nach
		// Verfasser und Inhalt
		for (int i = 0; i < list.size(); i++) {
			String[] s = list.get(i).split("\n");
			showedMessages.add(new JLabel(s[0] + ": "));
			String text = "";
			for (int k = 1; k < s.length; k++) {
				text = text + s[k];
			}
			System.out.println(text);
			showedMessages.add(new JLabel(text));
			showedMessages.add(new JLabel(""));
		}

		int i = (chatRightSideShowMessagePanel.getHeight()) / 25;
		// TODO das ist nicht so schön hier, aber Porblem ist, dass
		// getHeight 0 liefert und nicht 600
		if (i < 1)
			i = (420) / 25;
		while (true) {
			if ((showedMessages.size()) >= i) {
				showedMessages.pollFirst();
			} else {
				break;
			}
		}
		chatRightSideShowMessagePanel.removeAll();
		chatRightSideShowMessagePanel.revalidate();
		System.out.println(chatRightSideShowMessagePanel.getLayout());

		for (int k = 0; k < showedMessages.size(); k++) {
			JLabel l = showedMessages.get(k);
			l.setSize(this.getWidth(), 25);
			chatRightSideShowMessagePanel.add(l);

		}
		chatRightSideShowMessagePanel.revalidate();
		chatRightSideShowMessagePanel.repaint();
	}


	@Override
	public void componentHidden(ComponentEvent arg0) {
	}


	@Override
	public void componentMoved(ComponentEvent arg0) {
	}


	@Override
	public void componentResized(ComponentEvent arg0) {
		resizeAllComponents();
	}


	@Override
	public void componentShown(ComponentEvent arg0) {
		resizeAllComponents();
	}

	class EndClientActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent a) {
			client.leaveChat();
			client.deregisterClient();
			System.exit(0);
		}
	}

	class LeaveChatActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent a) {
			chatname = "";
			inChatLeftSidePanel.setVisible(false);
			chatRightSidePanel.setVisible(false);
			isRegisteredLeftSidePanel.setVisible(true);
			client.leaveChat();
		}
	}

	class RegisterClient2ActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent a) {
			boolean b = false;
			try {
				b = client.registerClient(nameUser = registerRightSidePanelTextFieldName.getText(),
						adresseServer = registerRightSidePanelTextFieldAdresse.getText());
			} catch (IOException e) {
				b = false;
			}
			if (!b) {
				falscherNameOderAdresse.setVisible(true);
				registerRightSidePanel.repaint();
				return;
			}
			isNotRegisteredLeftSidePanel.setVisible(false);
			registerRightSidePanel.setVisible(false);
			isRegisteredLeftSidePanel.setVisible(true);
		}
	}

	class RegisterClient1ActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent a) {
			registerRightSidePanel.setVisible(true);
			repaint();
		}
	}

	class CreateChat1ActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent a) {
			allRightSidePanelsVisibilityfalse();
			createChatRightSidePanel.setVisible(true);
		}
	}

	class CreateChat2ActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent a) {
			if (!client.createChat(neuerChatName = createChatRightSidePanelName.getText(),
					neuerChatPasswort = createChatRightSidePanelPasswort.getText())) {
				fehlerBeiCreateChat.setVisible(true);
				createChatRightSidePanel.repaint();
				return;
			}
			createChatRightSidePanel.setVisible(false);
		}
	}

	class EnterChat1ActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent a) {
			client.requestChats();
			// TODO auf Rückgabewert boolean prüfen
			verfuegbareChats.addAll(client.getChats().values());
			for (dataChat dc : verfuegbareChats) {
				enterChatRightSidePanelName.addItem(dc.getChatname() + " erstellt durch " + dc.getNameCreater());
			}
			allRightSidePanelsVisibilityfalse();
			enterChatRightSidePanel.setVisible(true);
		}
	}

	class EnterChat2ActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (enterChatRightSidePanelName.getSelectedItem() == null || enterChatRightSidePanelPasswort.equals("")) {
				falscherNameOderPasswort.setVisible(true);
				enterChatRightSidePanel.repaint();
				return;
			}
			int i = enterChatRightSidePanelName.getSelectedIndex();
			if (!client.enterChat(verfuegbareChats.get(i), enterChatRightSidePanelPasswort.getText())) {
				falscherNameOderPasswort.setVisible(true);
				enterChatRightSidePanel.repaint();
				return;
			}
			chatname = verfuegbareChats.get(i).getChatname();
			chatnameLabel.setText(chatname);
			enterChatRightSidePanel.setVisible(false);
			isRegisteredLeftSidePanel.setVisible(false);
			inChatLeftSidePanel.setVisible(true);
			chatRightSidePanel.setVisible(true);
		}
	}

	class SendMessageActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			client.sendMessageInChat(newMessage.getText());
			newMessage.setText("");
		}
	}

	class DeregisterClientActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent a) {
			allRightSidePanelsVisibilityfalse();
			isRegisteredLeftSidePanel.setVisible(false);
			isNotRegisteredLeftSidePanel.setVisible(true);
			client.deregisterClient();
		}
	}
}