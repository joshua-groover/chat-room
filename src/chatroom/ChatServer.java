package chatroom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * Joshua Groover
 */

 public class ChatServer extends ChatWindow {

	private ArrayList<ClientHandler> handlers = new ArrayList<ClientHandler>();

	public ChatServer(){
		super();
		this.setTitle("Chat Server");
		this.setLocation(80,80);

		try {
			// Create a listening service for connections
			// at the designated port number.
			ServerSocket srv = new ServerSocket(2113);

			while (true) {
				// The method accept() blocks until a client connects.
				// Every client that connects:
				// - has a seperate thread created to handle comms with that client
				// - has their client handler stored in the handlers array list 
				printMsg("Waiting for a connection");
				Socket socket = srv.accept();
				ClientHandler handler = new ClientHandler(socket);
				handlers.add(handler);
				Thread t = new Thread(handler);
				t.start();
			}

		} catch (IOException e) {
			System.out.println(e);
		}
	}

	/* Send a string message to every client that is connected to the chat server */
	public void SendAllClientsMessage(String s){
		for (ClientHandler client : handlers){
			client.sendMsg(s);
		}
	}

	/** This innter class handles communication to/from one client. */
	class ClientHandler implements Runnable{
		private PrintWriter writer;
		private BufferedReader reader;
		private String name = new String();

		public ClientHandler(Socket socket) {
			try {
				InetAddress serverIP = socket.getInetAddress();
				printMsg("Connection made to " + serverIP);
				writer = new PrintWriter(socket.getOutputStream(), true);
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			}
			catch (IOException e){
					printMsg("\nERROR:" + e.getLocalizedMessage() + "\n");
				}
		}
		public void run(){
			handleConnection();
		}
		public void handleConnection() {
			try {
				while(true) {
					// read a message from the client
					readMsg();
				}
			}
			catch (IOException e){
				printMsg("\nERROR:" + e.getLocalizedMessage() + "\n");
			}
		}

		private boolean checkIfName(String s){
			if(s.length()>6){
				if(s.substring(0,5).equals("/name")){
					return true;
				}
			}
			return false;
		}
		/** Receive and display a message */
		public void readMsg() throws IOException {
			//reads the message, checks if it is a name change
			// if name change: 
			// - if first name change: set the name to the input name and tell the server x joined the chat
			// - else: change the clients name and tell the rest of the clients about it
			// if not name change: send the message out to all other clients
			String s = reader.readLine();
			boolean isName = checkIfName(s);
			if(isName){
				String newName = s.substring(6);
				if(name.equals("")){
					name = newName;
					String msg = name + " has joined the chat.";
					printMsg(msg);
					SendAllClientsMessage(msg);
				}
				else{
					String msg = name + " has changed their name to " + newName + ".";
					name = newName;
					printMsg(msg);
					SendAllClientsMessage(msg);
				}
			}
			else{
				printMsg(name + ": " + s);
				SendAllClientsMessage(name + ": " + s);
			}
		}
		/** Send a string */
		public void sendMsg(String s){
			writer.println(s);
		}

	}

	public static void main(String args[]){
		new ChatServer();
	}
}
