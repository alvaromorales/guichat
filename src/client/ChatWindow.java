package client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import protocol.Message;

/**
 * Creates a chat window inside of chat session
 */
public class ChatWindow {
    private final String name;
    private List<Message> messages;
    private List<String> users;
    
    public ChatWindow(String name) {
        this.name = name;
        this.messages = Collections.synchronizedList(new ArrayList<Message>());
    }

    public void getListOfClients() {
        //TODO
        //needs to create a Request object added to chatWindowThreads
    }

    public void sendMessage(Message m) {
        //TODO
        //needs to create a Request object added to chatWindowThreads
    }

    public void addMessage(Message m) {
        this.messages.add(m);
    }

    public void addUser(String username) {
        this.users.add(username);
    }

    public List<Message> getMessages() {
        return this.messages;
    }

    public List<String> getUsers() {
        return this.users;
    }

    @Override
    public boolean equals(Object obj) {
        //check edges
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof ChatWindow)) return false;

        ChatWindow that = (ChatWindow) obj;
        //just check that the name's are equal. name's unique for a ChatWindow.
        if (this.name.equals(that.name)) return true;
        //else return false
        return false;
    }
}