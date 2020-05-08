## README
Read this file in its entirety before beginning to code.

### Chat Room Requirements and How it Works
The server and client programs to let users exchange messages. Each user runs the ChatClient program and connects to a shared ChatServer.  The server forwards incoming messages to all clients that are connected.

Typing a message and hitting send transmits the desired message to the server, which will in turn forward the message to any other client that is connected. The server print something to its screen such as: "Joe: Hi I just joined" indicating that a message was sent by the user Joe. Additionally, the server prints a message such as "Joe has joined the chat" when a new user joins the chat.