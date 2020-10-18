package chatsystemIHM;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import chatsystemTCP.Client;
import chatsystemTCP.Message;

public class Window extends JFrame implements ActionListener {

    // Variables utiles pour le positionnement et la taille des éléments
    private int posX;
    private int posY;
    private int width;
    private int height;
    private int insetX;
    private int insetY;

    // Client du chat
    private Client client;

    // Elements graphiques
    private JPanel mainContainer;
    private JPanel chatContainer;
    private JPanel connectionContainer;
    private JLabel labelHost;
    private JLabel labelPort;
    private JLabel labelPseudo;
    private JTextField textfieldHost;
    private JTextField textfieldPort;
    private JTextField textfieldPseudo;
    private JTextArea chatTextArea;
    private JTextField msgTextArea;
    private JScrollPane scrollChat;
    private JButton sendButton;
    private JButton connectButton;

    Window() {
        // Récupération de la taille de l'écran
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // Paramètres de la fenêtre
        width = (int) (screenSize.getWidth() * 0.6);
        height = (int) (screenSize.getHeight() * 0.8);
        this.setSize(width, height);
        this.setLayout(null);
        this.setTitle("IHM Chatsystem - TCP");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true); // On rend la fenêtre visible (permet de récupérer les insets)
        insetX = this.getInsets().left + this.getInsets().right;
        insetY = this.getInsets().top + this.getInsets().bottom;

        // Paramètres du panel conteneur principal
        this.mainContainer = new JPanel();
        this.mainContainer.setLayout(null);
        width = this.getWidth() - insetX;
        height = this.getHeight() - insetY;
        this.mainContainer.setBounds(0, 0, width, height);
        this.mainContainer.setBackground(Color.GRAY);

        // Autres éléments

        // Conteneur du chat
        this.chatContainer = new JPanel();
        this.chatContainer.setLayout(null);
        width = this.mainContainer.getWidth();
        height = (int) (this.mainContainer.getHeight() * 0.8);
        posX = 0;
        posY = (int) (this.mainContainer.getHeight() * 0.2);
        this.chatContainer.setBounds(posX, posY, width, height);
        this.chatContainer.setBackground(Color.GRAY);

        this.chatTextArea = new JTextArea();
        this.chatTextArea.setEditable(false);
        this.chatTextArea.setLineWrap(true);

        this.scrollChat = new JScrollPane(this.chatTextArea);
        width = (int) (this.chatContainer.getWidth() * 0.9);
        height = (int) (this.chatContainer.getHeight() * 0.7);
        posX = (this.chatContainer.getWidth()/2) - (width/2);
        posY = (int) (this.chatContainer.getHeight() * 0.02);
        this.scrollChat.setBounds(posX, posY, width, height);

        this.msgTextArea = new JTextField();
        width = (int) (this.chatContainer.getWidth() * 0.7);
        height = (int) (this.chatContainer.getHeight() * 0.23);
        posX = (int) (this.chatContainer.getWidth()/2) -  (int) (this.chatContainer.getWidth() * 0.9 / 2);
        posY = (int) (this.chatContainer.getHeight() * 0.75);
        this.msgTextArea.setBounds(posX, posY, width, height);
        this.msgTextArea.setEnabled(false);

        this.sendButton = new JButton("Envoyer le message");
        int buttonOffsetX = 10;
        width = (int) (this.chatContainer.getWidth() * 0.2) - buttonOffsetX;
        height = (int) (this.chatContainer.getHeight() * 0.23);
        posX = (this.chatContainer.getWidth()/2) + (int) (this.chatContainer.getWidth() * 0.7 - this.chatContainer.getWidth() * 0.9 / 2) + buttonOffsetX;
        posY = (int) (this.chatContainer.getHeight() * 0.75);
        this.sendButton.setBounds(posX, posY, width, height);
        this.sendButton.addActionListener(this);
        this.sendButton.setFocusable(false);
        this.sendButton.setEnabled(false);

        // Conteneur des infos de connexion
        
        this.connectionContainer = new JPanel();
        this.connectionContainer.setLayout(null);
        width = this.mainContainer.getWidth();
        height = (int) (this.mainContainer.getHeight() * 0.2);
        posX = 0;
        posY = 0;
        this.connectionContainer.setBounds(posX, posY, width, height);
        this.connectionContainer.setBackground(Color.GRAY);

        int offsetXLabel = 10; // offset nécessaire pour que le texte des JLabel s'affiche en entier
        
        // Label + TextField pour l'Host
        this.labelHost = new JLabel("Host : ");
        width = (int) Math.ceil(this.labelHost.getPreferredSize().getWidth()) + offsetXLabel;
        height = (int) Math.ceil(this.labelHost.getPreferredSize().getHeight());
        posX = (int) (this.connectionContainer.getWidth()/2) - 150;
        posY = (int) (this.connectionContainer.getHeight() * 0.2);
        this.labelHost.setBounds(posX, posY, width, height);
        this.textfieldHost = new JTextField();
        width = 150;
        posX = (int) (this.connectionContainer.getWidth()/2) - 90;
        this.textfieldHost.setBounds(posX, posY, width, height);

        // Label + TextField pour le Port
        this.labelPort = new JLabel("Port : ");
        width = (int) Math.ceil(this.labelPort.getPreferredSize().getWidth()) + offsetXLabel;
        height = (int) Math.ceil(this.labelPort.getPreferredSize().getHeight());
        posX = (int) (this.connectionContainer.getWidth()/2) - 150;
        posY = (int) (this.connectionContainer.getHeight() * 0.45);
        this.labelPort.setBounds(posX, posY, width, height);
        this.textfieldPort = new JTextField();
        width = 150;
        posX = (int) (this.connectionContainer.getWidth()/2) - 90;
        this.textfieldPort.setBounds(posX, posY, width, height);

        // Label + TextField pour le Pseudo
        this.labelPseudo = new JLabel("Pseudo : ");
        width = (int) Math.ceil(this.labelPseudo.getPreferredSize().getWidth()) + offsetXLabel;
        height = (int) Math.ceil(this.labelPseudo.getPreferredSize().getHeight());
        posX = (int) (this.connectionContainer.getWidth()/2) - 150;
        posY = (int) (this.connectionContainer.getHeight() * 0.7);
        this.labelPseudo.setBounds(posX, posY, width, height);
        this.textfieldPseudo = new JTextField();
        width = 150;
        posX = (int) (this.connectionContainer.getWidth()/2) - 90;
        this.textfieldPseudo.setBounds(posX, posY, width, height);

        this.connectButton = new JButton("Se connecter");
        width = 150;
        height = (int) (this.connectionContainer.getHeight() * 0.3);
        posX = (int) (this.connectionContainer.getWidth()/2) + 100;
        posY = (int) (this.connectionContainer.getHeight()/2) - (int) (height/2);
        this.connectButton.setBounds(posX, posY, width, height);
        this.connectButton.addActionListener(this);
        this.connectButton.setFocusable(false);

        // Ajout des éléments
        this.chatContainer.add(this.scrollChat);
        this.chatContainer.add(this.msgTextArea);
        this.chatContainer.add(this.sendButton);

        this.connectionContainer.add(this.labelHost);
        this.connectionContainer.add(this.textfieldHost);
        this.connectionContainer.add(this.labelPort);
        this.connectionContainer.add(this.textfieldPort);
        this.connectionContainer.add(this.labelPseudo);
        this.connectionContainer.add(this.textfieldPseudo);
        this.connectionContainer.add(this.connectButton);

        this.mainContainer.add(this.chatContainer);
        this.mainContainer.add(this.connectionContainer);

        this.add(this.mainContainer);
        this.repaint(); // Repaint la fenêtre pour afficher les éléments

        // Création du client
        this.client = new Client();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.sendButton) {
            String content = this.msgTextArea.getText();
            if (!(content.isBlank() || content.isEmpty())) {
                try {
                    this.client.sendMsg(content);
                    this.msgTextArea.setText("");
                } catch (Exception ex) {
                    System.out.println("Error when sending : " + ex);
                }
            }
        }
        else if (e.getSource() == this.connectButton) {
            if (!this.client.getIsConnected()) {
                String host;
                int port;
                String pseudo;
                host = this.textfieldHost.getText();
                if (host.isEmpty() || host.isBlank()) {
                    JOptionPane.showMessageDialog(null, "Veuillez rentrer un host valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    port = Integer.parseInt(this.textfieldPort.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Veuillez rentrer un entier pour le numero de port.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                pseudo = this.textfieldPseudo.getText();
                if (pseudo.isEmpty() || pseudo.isBlank()) {
                    JOptionPane.showMessageDialog(null, "Veuillez rentrer un pseudo.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    this.client.connect(host, port, pseudo, this);
                    this.connectButton.setText("Se deconnecter");
                    this.sendButton.setEnabled(true);
                    this.msgTextArea.setEnabled(true);
                } catch (Exception ex) {
                    System.out.println("Error when connecting : " + ex);
                    JOptionPane.showMessageDialog(null, "La connexion a echoue !", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
            else {
                try {
                    this.client.disconnect();
                    this.connectButton.setText("Se connecter");
                    this.sendButton.setEnabled(false);
                    this.msgTextArea.setEnabled(false);
                    this.chatTextArea.setText("");
                    this.msgTextArea.setText("");
                } catch (Exception ex) {
                    System.out.println("Error when disconnecting : " + ex);
                }
            }
        }
    }

    public void receiveMsg(Message msg) {
        if (msg.getType() == 1) {
            // Message de type information
            this.chatTextArea.append(msg.getContent() + "\n");
        } else {
            // Message normal
            this.chatTextArea.append(msg.getPseudo() + " dit : " + msg.getContent() + "\n");
        }
    }
}
