package test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.DelayQueue;

import main.Server;

import org.junit.Test;
import protocol.RoomRequest.*;
import protocol.RoomResponse.*;
import protocol.Registration.*;
import protocol.Response;

/**
 * Tests joining and leaving rooms
 * Testing strategy:
 *  - test a user joining a room
 *  - test a delayed request to join a room
 *  - tests the user joining an existing room
 *  - test a user joining a room, and then leaving the room
 * 
 *  @category no_didit
 */
public class RoomTest extends ServerTest {

    /**
     * Tests that the user "benbitdiddle" can join a room named "foo"
     */
    @Test
    public void joinRoomTest() throws InterruptedException {
        Server server = new Server(SERVER_PORT);
        startServer(server);

        DelayQueue<DelayedRequest> requestQueue = new DelayQueue<DelayedRequest>();
        requestQueue.add(new DelayedRequest(new JoinOrCreateRoomRequest("benbitdiddle", "foo"), 0));

        RequestTester test = new RequestTester("benbitdiddle", requestQueue, 2);
        Thread t = new Thread(test);
        t.start();
        t.join();

        List<Response> expected = new ArrayList<Response>();
        expected.add(new LoginResponse("benbitdiddle"));
        List<String> usersExpected = new ArrayList<String>();
        usersExpected.add("benbitdiddle");
        expected.add(new JoinedRoomResponse("foo", usersExpected));
        assertEquals(expected,test.getResponseList());

        stopServer(server);
    }

    /**
     * Tests that the user "benbitdiddle" can join a room named "foo", where the request is delayed
     * @throws InterruptedException 
     */
    @Test
    public void delayedJoinRoomTest() throws InterruptedException {
        Server server = new Server(SERVER_PORT);
        startServer(server);

        DelayQueue<DelayedRequest> requestQueue = new DelayQueue<DelayedRequest>();
        requestQueue.add(new DelayedRequest(new JoinOrCreateRoomRequest("benbitdiddle", "foo"), 5));

        RequestTester test = new RequestTester("benbitdiddle", requestQueue, 2);
        Thread t = new Thread(test);
        t.start();
        t.join();
        
        List<Response> expected = new ArrayList<Response>();
        expected.add(new LoginResponse("benbitdiddle"));
        List<String> usersExpected = new ArrayList<String>();
        usersExpected.add("benbitdiddle");
        expected.add(new JoinedRoomResponse("foo",usersExpected));
        assertEquals(expected,test.getResponseList());

        stopServer(server);
    }

    /**
     * Tests that the user "alyssaphacker" can join an existing room named "foo"
     * @throws InterruptedException 
     */
    @Test
    public void joinExistingRoomTest() throws InterruptedException {
        Server server = new Server(SERVER_PORT);
        startServer(server);

        DelayQueue<DelayedRequest> requestQueue1 = new DelayQueue<DelayedRequest>();
        requestQueue1.add(new DelayedRequest(new JoinOrCreateRoomRequest("benbitdiddle", "foo"), 50));

        RequestTester test1 = new RequestTester("benbitdiddle", requestQueue1, 2);
        Thread t1 = new Thread(test1);
        t1.start();

        DelayQueue<DelayedRequest> requestQueue2 = new DelayQueue<DelayedRequest>();
        requestQueue2.add(new DelayedRequest(new JoinOrCreateRoomRequest("alyssaphacker", "foo"), 0));

        RequestTester test2 = new RequestTester("alyssaphacker", requestQueue2, 2);
        Thread t2 = new Thread(test2);
        t2.start();

        t1.join();
        t2.join();

        List<Response> expected = new ArrayList<Response>();
        expected.add(new LoginResponse("alyssaphacker"));
        List<String> usersExpected = new ArrayList<String>();
        usersExpected.add("alyssaphacker");
        expected.add(new JoinedRoomResponse("foo",usersExpected));
        assertEquals(expected,test2.getResponseList());

        stopServer(server);
    }

    /**
     * Tests that the user "benbitdiddle" can join a room named "foo", and then leave it
     */
    //@Test
    public void joinAndLeaveRoomTest() throws InterruptedException {
        Server server = new Server(SERVER_PORT);
        startServer(server);

        DelayQueue<DelayedRequest> requestQueue = new DelayQueue<DelayedRequest>();
        requestQueue.add(new DelayedRequest(new JoinOrCreateRoomRequest("benbitdiddle", "foo"), 0));
        requestQueue.add(new DelayedRequest(new LeaveRoomRequest("benbitdiddle", "foo"), 10));

        RequestTester test = new RequestTester("benbitdiddle", requestQueue, 3);
        Thread t = new Thread(test);
        t.start();
        t.join();

        List<Response> expected = new ArrayList<Response>();
        expected.add(new LoginResponse("benbitdiddle"));
        List<String> usersExpected = new ArrayList<String>();
        usersExpected.add("benbitdiddle");
        expected.add(new JoinedRoomResponse("foo",usersExpected));
        expected.add(new LeftRoomResponse("foo"));
        assertEquals(expected,test.getResponseList());

        stopServer(server);
    }

}
