package User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import Node.Node;
import message.Message;
import message.MessageTypes;

public class Receiver implements Runnable {

	private Socket clientSocket;
	
	public static ObjectInputStream readFromNet;
    ObjectOutputStream writeToNet;
    
    Message message;
        
    Node messageNode = null;
    
	public Receiver(Socket clientSocket) 
		{
		// TODO Auto-generated constructor stub
		this.clientSocket = clientSocket;
		
		// initialze socket to be used in run
		}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		// handle input 
		// get input and output streams
		try 
		   {
			readFromNet = new ObjectInputStream(clientSocket.getInputStream());
			//writeToNet = new ObjectOutputStream(clientSocket.getOutputStream());
			
			// convert inputStream to object
			message = (Message)readFromNet.readObject();
			
		   }catch(IOException | ClassNotFoundException ex)
		{
			   System.err.println("Error reading message");
		}
		
			
		
		
		// switch cases
		switch( message.getType())
		{
			// message join
		case MessageTypes.JOIN:
			
			System.out.println(((Node) message.getContent()).getName() + " JOINED the Chat");
			// add receiver node to list 
			MeshNode.clientNodes.add((Node) message.getContent());
			
			try {
				Socket socketBack = new Socket(((Node) message.getContent()).getAddress(), 
						                          ((Node) message.getContent()).getPort());
				ObjectOutputStream streamOut = new ObjectOutputStream(socketBack.getOutputStream());
				
				streamOut.writeObject(new Message( MessageTypes.LIST, MeshNode.clientNodes));
				
				// close the stream 
				streamOut.close();
				//socketBack.close();
				// writeToNet.writeObject(New Message( MessageTypes.JOINED, MeshNode ));
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			break;
			
		case MessageTypes.LIST:
			//update client nodes list
			MeshNode.clientNodes = (ArrayList<Node>) message.getContent();
			
			// display where list was received from
			messageNode = locateNode(clientSocket);
			System.out.println("Mesh list received from: " +messageNode.name);
			
			break;
		
		    // message leave
		case MessageTypes.LEAVE:
			System.out.println(((Node) message.getContent()).getName() + " left the chat.");
			MeshNode.clientNodes.remove((Node) message.getContent());
			break;
			
		    // Message note 
		case MessageTypes.NOTE:
			
			messageNode = locateNode(clientSocket);
			// get clients name 
			System.out.println( messageNode.name+
								" : " +
								(String)message.getContent() +
								".");
			break;
			
		case MessageTypes.SHUTDOWN:
			System.out.println(((Node) message.getContent()).getName() + " shutdown.");
			
			// remove node from list
			MeshNode.clientNodes.remove((Node) message.getContent());
			break;
			
		case MessageTypes.SHUTDOWN_ALL:
			System.out.println(((Node) message.getContent()).getName()+" shutdown the network");
			
			System.exit(0);
			break;
		
		
		}
		// check the message type and print

	}
	
	/*
	 * Parameters: Socket clientSocket
	 * Return: Node found in clientNodes list
	 * This function locates the current node with the corresponding socket in the receiver thread
	 * it is used for the note and list case where the object received is not a node. 
	 */
	public static Node locateNode( Socket clientSocket )
	   {
		String clientAddress = clientSocket.getInetAddress().toString();
		String[] split = clientAddress.split("/");
		
		Node nodeFound = null;
		
		for (int index = 0; index < MeshNode.clientNodes.size(); index++) {
			if( MeshNode.clientNodes.get(index).getAddress().equals(split[1]))
			{
				nodeFound = MeshNode.clientNodes.get(index);
			}
		}
		return nodeFound;
		
	   }

}
