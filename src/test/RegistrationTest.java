package test;

import static org.junit.Assert.*;
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
import protocol.Registration.LoginRequest;
import protocol.ServerErrorResponse;
import main.Server;

/**
 * Tests the login process
 * Testing strategy:
 *  - test the serialization of a login request object
 *  - test a user logging into the server
 *  - test a user logging into the server with a username that is taken
 * 
 *  @category no_didit
 */
public class RegistrationTest extends ServerTest {
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
    
    /**
     * Tests that a user cannot login with a username that is already taken
     * After getting a username already taken, the user will attempt to login with a different username
     * @throws InterruptedException 
     */
    @Test
    public void takenUsernameLoginTest() throws InterruptedException {
        Server server = new Server(SERVER_PORT);
        startServer(server);
        
        RequestTester test1 = new RequestTester("benbitdiddle", new DelayQueue<DelayedRequest>(), 1);
        Thread t1 = new Thread(test1);
        t1.start();
        
        RequestTester test2 = new RequestTester("benbitdiddle", new DelayQueue<DelayedRequest>(), 1);
        Thread t2 = new Thread(test2);
        t2.start();

        t1.join();
        t2.join();
        
        List<Response> expected = new ArrayList<Response>();
        expected.add(new ServerErrorResponse(ServerErrorResponse.Type.LOGIN_TAKEN, "Username taken"));
        assertEquals(expected,test2.getResponseList());
        
        stopServer(server);
    }
    
}
