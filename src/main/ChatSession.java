package main;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Creates a chat session with the server
 */
class ChatSession {
    PrintWriter output;
    List<ChatWindow> chatWindows;

    public ChatSession(PrintWriter output) {
        this.output = output;
        this.chatWindows = Collections.synchronizedList(new ArrayList<ChatWindow>());
    }

    private void routeMessage(ChatWindow c, Message m) {
        //find the correct window
        synchronized(chatWindows) {
            for (ChatWindow cw: chatWindows) {
                if (cw.equals(c)) {
                    cw.addMessage(m);
                    //also display message in cooresponding GUI window
                    //TODO
                }
            }
        }
        //window was not found
        //need to handle this somehow...
    }

    private void sendMessage(ChatWindow c, Message m) {
        //add the message
        c.addMessage(m);
        //also display window in corresponding GUI window
        //send message to others in chat room
        c.sendMessage(m);
    }

    private  boolean openChatWindow(ChatWindow c) {
        //check that the window were attempting to add doesn't already exist
        synchronized(chatWindows) {
            for (ChatWindow cw: chatWindows) {
                if (cw.equals(c)) {
                    //handle duplicate window somehow
                    return false;
                }
            }
        }
        
        //add the chat window and create corresponding GUI pieces
        chatWindows.add(c);
        return true;
    }

    private void closeChatWindow(ChatWindow c) throws InterruptedException {
        //wait for the threads in the ChatWindow to finish
        for (Thread t: c.getThreads()) {
            t.join();
        }
        //send Request object to server
        //TODO
        //remove the ChatWindow from chatWindows
        this.chatWindows.remove(c);
        //and kill the corresponding GUI pieces
        //TODO
    }

    private boolean logout() throws InterruptedException {
        //close all of the chat windows and corresponding GUI pieces
        synchronized(chatWindows) {
            for (ChatWindow cw: chatWindows) {
                for (Thread t: cw.getThreads()) t.join();
            }
        }
        return false;
        
        //eliminate GUI pieces
        //TODO
        //terminate ChatSession
        //TODO
    }
}