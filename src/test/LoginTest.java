package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.DelayQueue;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import protocol.InterfaceAdapter;
import protocol.LoginRequest;
import protocol.LoginResponse;
import protocol.Request;
import protocol.Response;

import main.Server;

/**
 * Tests the login process
 * Testing strategy:
 *  -
 */
public class LoginTest {
    //Server server = new Server(4444);
        
    @Test
    public void serializeTest() {
        Gson gson = new GsonBuilder()
                            .registerTypeAdapter(Request.class, new InterfaceAdapter<Request>())
                            .registerTypeAdapter(Response.class, new InterfaceAdapter<Response>())
                            .create();
        Request request = new LoginRequest("benbitdiddle");
        String json = gson.toJson(request); 
        System.out.println(json);
    }
    
    
    /**
     * Tests that a single user can login
     * @throws InterruptedException 
     */
    //@Test
    public void singleUserLoginTest() throws InterruptedException {
        RequestTester test = new RequestTester("benbitdiddle", new DelayQueue<DelayedRequest>(), 1);
        Thread t = new Thread(test);
        t.start();
        t.join();

        List<Response> expected = new ArrayList<Response>();
        expected.add(new LoginResponse("benbitdiddle"));
        assertEquals(expected,test.getResponseList());
    }
    
}
