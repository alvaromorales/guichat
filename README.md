GUICHAT
=============

A simple chat client and server.

About
-----

Talk about chat client.

Installation
-----------

Talk about installation.


Usage
-----

Talk about usage.

Concurrency
----------

Talk about concurrency.


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