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
    List chatWindows;

    public ChatSession(PrintWriter output) {
        this.output = output;
        this.chatWindows = Collections.synchronizedList(new ArrayList<ChatWindow>());
    }

    private void routeMessage(ChatWindow c, Message m) {
        //TODO
    }

    private boolean openChatWindow(ChatWindow c) {
        //TODO
    }

    private void closeChatWindow(ChatWindow c) {
        //TODO
    }

    private boolean logout() {
        //TODO
    }
}