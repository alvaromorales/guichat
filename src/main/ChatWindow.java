package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Creates a chat window inside of chat session
 */
class ChatWindow {
    private final String name;
    private List<Message> messages;
    private List<Thread> chatWindowThreads;
    
    public ChatWindow(String name) {
        this.name = name;
        this.messages = Collections.synchronizedList(new ArrayList<Message>());
        this.chatWindowThreads = Collections.synchronizedList(new ArrayList<Thread>());
    }

    private void getListOfClients() {
        //TODO
        //needs to create a Request object added to chatWindowThreads
    }

    private void sendMessage(Message m) {
        //TODO
        //needs to create a Request object added to chatWindowThreads
    }

    private void addMessage(Message m) {
        this.messages.add(m);
    }

    private List<Message> getMessages() {
        return this.messages;
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