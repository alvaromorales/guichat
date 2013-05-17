package test;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.DelayQueue;
import main.Server;
import org.junit.Test;
import protocol.AvailableRoomsResponse;
import protocol.Request;
import protocol.Response;
import protocol.SendMessageRequest;
import protocol.SimpleMessage;
import protocol.Registration.LoginResponse;
import protocol.RoomRequest.JoinOrCreateRoomRequest;
import protocol.RoomResponse.JoinedRoomResponse;

/**
 * Tests joining and leaving rooms
 * Testing strategy:
 *  - test a user sending a message
 *  - test a user receiving a message from another user
 * 
 *  @category no_didit
 */
public class MessageTest extends ServerTest {

    /**
     * Tests that a user can send a message
     * @throws InterruptedException 
     */
    @Test
    public void sendMessageTest() throws InterruptedException {
        Server server = new Server(SERVER_PORT);
        startServer(server);

        DelayQueue<DelayedRequest> requestQueue = new DelayQueue<DelayedRequest>();
        requestQueue.add(new DelayedRequest(new JoinOrCreateRoomRequest("benbitdiddle", "foo"), 1));
        Timestamp time = new Timestamp((new Date()).getTime());
        Request message = new SendMessageRequest("benbitdiddle","foo",new SimpleMessage("hello", time, "benbitdiddle", "foo"));
        requestQueue.add(new DelayedRequest(message,5));

        RequestTester test = new RequestTester("benbitdiddle", requestQueue, 4);
        Thread t = new Thread(test);
        t.start();
        t.join();

        List<Response> expected = new ArrayList<Response>();
        expected.add(new LoginResponse("benbitdiddle"));
        ArrayList<String> rooms = new ArrayList<String>();
        rooms.add("foo");
        List<String> usersExpected = new ArrayList<String>();
        usersExpected.add("benbitdiddle");
        expected.add(new AvailableRoomsResponse(rooms));
        expected.add(new JoinedRoomResponse("foo", usersExpected));
        expected.add(new SendMessageRequest("benbitdiddle","foo",new SimpleMessage("hello", time, "benbitdiddle", "foo")));
        assertEquals(expected,test.getResponseList());

        stopServer(server);
    }

}
