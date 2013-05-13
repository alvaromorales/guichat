package client;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

    public synchronized void addMessage(Message m, ChatGUI gui) {
        this.messages.add(m);
        gui.writeToWindow(m.getUsername() + ":" + m.getMessage());
    }

    public void addUser(String username) {
        this.users.add(username);
    }

    public void removeUser(String username) {
        //TODO
    }

    public List<Message> getMessages() {
        return this.messages;
    }

    public List<String> getUsers() {
        return this.users;
    }

    public String getName() {
        return this.name;
    }

    public void saveConversation() {
        try {
             File file = new File(name + "_History.txt");
            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for (Message m: messages) {
                bw.write(m.getUsername() + ": " + m.getMessage() + "\n");
            }
            bw.close(); 
        } catch (IOException e) {
            e.printStackTrace();
        }
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