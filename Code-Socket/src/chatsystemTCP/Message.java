package chatsystemTCP;

import java.io.Serializable;

/**
 * Message est la classe qui représente un message échangé entre le 
 * serveur et un client.
 * 
 * @author Killian OECHSLIN
 * @author Thomas MIGNOT
 */
public class Message implements Serializable {

    // ----- Attributs de Message -----

    /**
     * Pseudo de l'envoyeur.
     */
    private String pseudo;

    /**
     * Contenu du message.
     */
    private String content;

    /**
     * Type du message : 1 = Message d'information ; 2 = Message normal
     */
    private int type;

    // ----- Méthodes -----
    
    /**
     * Constructeur.
     * @param content Le contenu du message.
     * @param pseudo Le pseudo de l'envoyeur.
     * @param type Le type du message.
     */
    public Message(String content, String pseudo, int type) {
        this.pseudo = pseudo;
        this.content = content;
        this.type = type;
    }

    /**
	* Getter.
	* @return Le contenu du message.
	**/
    public String getContent() {
        return this.content;
    }

    /**
	* Getter.
	* @return Le pseudo de l'envoyeur.
	**/
    public String getPseudo() {
        return this.pseudo;
    }

    /**
	* Getter.
	* @return Le type du message.
	**/
    public int getType() {
        return this.type;
    }
}