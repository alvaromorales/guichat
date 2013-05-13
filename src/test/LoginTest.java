package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.DelayQueue;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import protocol.InterfaceAdapter;
import protocol.Registration;
import protocol.Request;
import protocol.Response;

import main.Server;

/**
 * Tests the login process
 * Testing strategy:
 *  -
 */
public class LoginTest extends ServerTest {
    @Test
    /**
     * Tests the serialization and deserialization of a LoginRequest
     */
    public void serializeTest() {
        Gson gson = new GsonBuilder()
                            .registerTypeAdapter(Request.class, new InterfaceAdapter<Request>())
                            .registerTypeAdapter(Response.class, new InterfaceAdapter<Response>())
                            .create();
        
        Request request = new Registration.LoginRequest("benbitdiddle");
        String json = gson.toJson(request,Request.class); 
        
        Request deserialized = gson.fromJson(json, Request.class);
        assertEquals(request, deserialized);
    }
    
    
    /**
     * Tests that a single user can login
     * @throws InterruptedException 
     */
    @Test
    public void singleUserLoginTest() throws InterruptedException {
        Server server = new Server(SERVER_PORT);
        startServer(server);
        
        RequestTester test = new RequestTester("benbitdiddle", new DelayQueue<DelayedRequest>(), 1);
        Thread t = new Thread(test);
        t.start();
        t.join();

        List<Response> expected = new ArrayList<Response>();
        expected.add(new Registration.LoginResponse("benbitdiddle"));
        assertEquals(expected,test.getResponseList());
        
        stopServer(server);
    }
    
}
