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
 * Concurrency: all synchronized methods are used
 * to avert race conditions
 */
public class ChatWindow {
    private final String name;
    private List<Message> messages;
    private List<String> users;
    private int unreadCount = 0;
    
    /**
     * Constructs a ChatWindow
     *
     * @param name: name of chat window
     */
    public ChatWindow(String name) {
        this.name = name;
        this.messages = Collections.synchronizedList(new ArrayList<Message>());
        this.users = Collections.synchronizedList(new ArrayList<String>());
    }

    /**
     * Sets the unread message count to 0
     */
    public void setMessageCountToZero() {
        unreadCount = 0;
    }

    /**
     * Adds a message to messages. Modifies the gui
     * as appropriate. If the chatwindow is current,
     * it will be printed to the screen. Otherwise,
     * the unread message count will be incremented. 
     *
     * @param message: message to add
     * @param gui: gui to modify
     */
    public synchronized void addMessage(Message m, ChatGUI gui) {
        this.messages.add(m);
        if (this.equals(gui.getCurrentChatWindow())) { //ChatWindow is current
            gui.writeToWindow(m.getUsername() + ": " + m.getMessage() + "\n");
        } else { //ChatWindow is not open. Modify sidebar
            unreadCount += 1;
            //adjust the table to reflect the unread message count
            synchronized(gui.getTableModelLock()) {
                //locate the correct row
                for (int row = 0; row < gui.chatWindowsTableModel.getRowCount(); row++) {
                    if (((String) gui.chatWindowsTableModel.getValueAt(row, 1)).equals(name)) {
                        gui.chatWindowsTableModel.setValueAt(unreadCount,row,3);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Adds a user to users. Modifies the gui
     * as appropriate.
     *
     * @param username: name of user to add
     * @param gui: gui to modify
     */
    public synchronized void addUser(String username, ChatGUI gui) {
        this.users.add(username);
        if (this.equals(gui.getCurrentChatWindow())) { //ChatWindow is current
            gui.writeToWindow("System Message: " + username + " has joined the chat room." + "\n");
        } else { //ChatWindow is not open. 
            
        }
        //adjust the table to reflect the number of users
        synchronized(gui.getTableModelLock()) {
            //locate the correct row
            for (int row = 0; row < gui.chatWindowsTableModel.getRowCount(); row++) {
                if (((String) gui.chatWindowsTableModel.getValueAt(row, 1)).equals(name)) {
                    gui.chatWindowsTableModel.setValueAt(users.size(),row,2);
                    break;
                }
            }
        }
    }

    /**
     * Removes a user from users. Modifies the gui
     * as appropriate.
     *
     * @param username: name of user to remove
     * @param gui: gui to modify
     */
    public synchronized void removeUser(String username, ChatGUI gui) {
        this.users.remove(username);
        if (this.equals(gui.getCurrentChatWindow())) { //ChatWindow is current
            gui.writeToWindow("System Message: " + username + " has left the chat room." + "\n");
        } else { //ChatWindow is not open. 
            
        }
        //adjust the table to reflect the number of users
        synchronized(gui.getTableModelLock()) {
            //locate the correct row
            for (int row = 0; row < gui.chatWindowsTableModel.getRowCount(); row++) {
                if (((String) gui.chatWindowsTableModel.getValueAt(row, 1)).equals(name)) {
                    gui.chatWindowsTableModel.setValueAt(users.size(),row,2);
                    break;
                }
            }
        }
    }

    /**
     * Getter method for unreadCount
     *
     * @@return unreadCount: the number of unread messages
     */
    public synchronized List<Message> getMessages() {
        return this.messages;
    }

    /**
     * Getter method for users
     *
     * @return users: the users in the room
     */
    public synchronized List<String> getUsers() {
        return this.users;
    }

    /**
     * Setter method for users
     *
     * @param users: list of users (Strings)
     */
    public synchronized void setUsers(List<String> users) {
        this.users = users;
    }

    /**
     * Getter method for name
     *
     * @return name: the name of the chat room
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter method for unreadCount
     *
     * @return unreadCount: the number of unread messages
     */
    public synchronized int getUnreadCount() {
        return unreadCount;
    }

    /**
     * Attempts to write the contents of this ChatWindow
     * to a file saved in the root directory of this project.
     */
    public synchronized void saveConversation() {
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

    /**
     * Checks whether or not the param is equal to this
     * object.
     *
     * @param obj: Object to compare for equality
     * @return boolean: true or false depending on equality
     */
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