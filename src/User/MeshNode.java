package User;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import Node.Node;
import message.Message;

public class MeshNode
	{
	// array of Nodes within the network // change to private
	public static ArrayList<Node> clientNodes = new ArrayList<Node>();
	
	public static Node receiverNode; 
	
	
	public MeshNode( Node meshNode ) 
	   {
		// Initialize peer node
		receiverNode = meshNode;
		
		// add the peer node to clientNode list
		clientNodes.add(meshNode);
		
		// Display mesh node name and port number
		System.out.println(meshNode.getName()+
						" is listening for connections on port: " + 
						meshNode.port);
		
		// sender thread 
		Thread senderThread = new Thread( new Sender( meshNode ));
		senderThread.start();
		
		try
		   {
			// create a server sockets to list and accept connection
			ServerSocket userSocket = new ServerSocket( meshNode.port );
			
			// loop 
			while(true)
			{
				
				// new socket for client 
				Socket clientSocket = userSocket.accept();
				
				// create a thread for clientSocket
					// thread is for reading and writing to clients 
				Thread thread = new Thread( new Receiver(clientSocket) );
			
				// run the thread 
				thread.start();
				
				
				
				// print if connection was made
				System.out.println("Connected");
			
			}
			// 
			
		   } catch( Exception ex )
		    {
			   // display errors with connections 
			   ex.printStackTrace();
		    }
	    }
	
	
	 public static void main(String[] args) throws UnknownHostException, IOException
        {
		 // create a MeshNode
		Node node1 = new Node("192.168.0.4", 8081, "Node 1");
	
		MeshNode user1 = new MeshNode(node1);
		
       
        }
   
	 
	
	

	}
