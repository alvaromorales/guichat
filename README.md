GUICHAT
=============

A simple chat client and server.

About
-----

This is a simple chat client similar to IRC (Internet Relay Chat). Users have the ability to join and create rooms.

Installation
-----------

To install and run do the following:
    
    * Clone the repository
    * Run main.Server.main() with no command-line arguments to start and instance of the server. Note: As currently configured, the program will run a server on localhost:4444. So make sure nothing else is running on that port to avoid errors.
    * Running main.Client.main() with no command-line arguments to start the GUI
    * Repeat the previous step until you have the desired number of clients
    * Enjoy playing around with this simple chat interface

To run this code for people on separate machines you will need to modify the server name from localhost to what is appropriate.

Usage
-----

This project is intended to demonstrate a few of the basics of software design.

Concurrency
----------

Server Side

We will use a LinkedBlockingQueue implementation for the request blocking queue. This implementation is thread­safe, and allows multiple Users to put Requests onto it without running into race conditions, messing up the order of requests or overwriting requests. These methods will be atomic.
The request handler class will contain an overloaded handleRequest method (one for each type of requests, e.g. Login, SimpleMessage,etc). Each of these methods will be synchronized. The running RequestHandler thread will take from the queue, and run the corresponding handleRequest method. Because we are using a data structure that is thread­safe, and building a thread­safe RequestHandler class that contains synchronized, atomic methods, our server design is thread­safe.

Client Side

The client concurrency strategy is similar to that of the server. When the client connects to the server two things are created: a ChatSession, responsible for keeping track of the ChatWindows the client has open and maintaining a response blocking queue, and a ResponseHandler, responsible for handling the Response objects. 

As with the RequestHandler on the server side, the ResponseHandler will contain overloaded handleResponse methods. Each of these methods will be synchronized. The ResponseHandler thread takes Responses from the blocking queue in the ChatSession and runs the corresponding handleResponse method.

Most of the Response objects will require that we mutate the ArrayList of messages in a specific ChatWindow and write to the corresponding JTextArea in the GUI. This is all handled by synchronized methods in specific ChatWindow class. With the exception of System Messages and help message pre connection, all writing to the main chat window will be done by active ChatWindow or the ChatSession (in the case of adding new ChatRooms to the Chat Rooms list).

When the user elects to terminate a ChatWindow, we will join all of the threads in the ChatSession blocking queue to ensure that none of those threads can modify the specified ChatWindow after it is terminated. The same is done when the entire ChatSession is terminated.

Testing
-------

* Connect to Chat Server
* Create a username
* Create a new room
* Type message into room
* Leave room

This test should show the message displayed next to the specified username, the new chat room should be in the list
of available chat rooms. The user should then be able to leave the room.

* Connect to Chat Server
* Create a username
* Create a new room
* Type message into room
* Leaves room
* Other user connects to chat server
* Other user creates a username
* Other user creates a new room
* Other user types message into new room

This test should show the message displayed next to the first specified username, the new chat room should be in the list
of available chat rooms, the other user should be able to create a new username and login to the chat server and not see the 
other user's chat room in the list, the new chat room that the second user creates should join the list of available chat
rooms, the message the second user types should be only displayed in the second user's chat room next to the second user's
name.

* Connect to Chat Server
* Create a username
* Create a new room
* Type message into room
* Other user connects to chat server
* Other user creates a username
* Other user joins existing room
* Other user types message into new room
* First user types message into room
* First user leaves room
* Second user leaves room

This test should show the message displayed next to the first specified username, the new chat room should be in the list
of available chat rooms, the other user should be able to create a new username and login to the chat server and see the 
other user's chat room in the list. The second user should be able to join the first user's room. The first user should be
notified that the second user joined. The second user should not be able to see the first user's previous message. The 
second user should be able to type in a message, which will be displayed next to their username. The first user should
see this new message. The first user should be able to type in a message, which will be displayed next to their username.
The second user should see this new message. The second user should be notified that the first user left. The second user
should be able to leave.

* Connect to Chat Server
* Create a username
* Create a new room
* Type message into room
* Other user connects to chat server
* Other user creates a username
* Other user joins existing room
* Other user types delayed message into new room
* First user types message into room
* First user leaves room
* Second user leaves room

This test should show the message displayed next to the first specified username, the new chat room should be in the list
of available chat rooms, the other user should be able to create a new username and login to the chat server and see the 
other user's chat room in the list. The second user should be able to join the first user's room. The first user should be
notified that the second user joined. The second user should not be able to see the first user's previous message. The 
second user should be able to type in a delayed message, which will be displayed next to their username immediately. The first 
user should be able to type in a message before the second user's message shows up for them, which will be displayed next to the 
first username. The second user's delayed message should show up before the first user's message. Both users should be able to 
see both messages eventually.The second user should be notified that the first user left. The second user should be able to 
leave.

Other tests:
* One user, multiple chat rooms
* Multiple users, one chat room
* Multiple users, multiple chat rooms



Contributing
------------

1. Fork it.
2. Create a branch (`git checkout -b my_branch`)
3. Commit your changes (`git commit -am "Added some shit"`)
4. Push to the branch (`git push origin my_branch`)
5. Open a [Pull Request][1]
6. Enjoy a refreshing Diet Coke and wait