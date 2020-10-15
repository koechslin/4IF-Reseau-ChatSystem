package chatsystemTCP;

import java.io.Serializable;

public class Message implements Serializable {

    // Attributs de Message
    private String pseudo;
    private String content;
    private int type; // type = 1 => message d'information ; type = 2 => message normal

    /**
	* constructor
    * @param content the content of the message
    * @param pseudo the pseudo of the user
    * @param type the type of the message
	**/
    public Message(String content, String pseudo, int type) {
        this.pseudo = pseudo;
        this.content = content;
        this.type = type;
    }

    /**
	* getter
	* @return the variable 'content'
	**/
    public String getContent() {
        return this.content;
    }

    /**
	* getter
	* @return the variable 'pseudo'
	**/
    public String getPseudo() {
        return this.pseudo;
    }

    /**
	* getter
	* @return the variable 'type'
	**/
    public int getType() {
        return this.type;
    }
}
