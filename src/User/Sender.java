package User;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import Node.Node;
import message.Message;
import message.MessageTypes;

public class Sender extends Thread implements MessageTypes {
	
	Scanner input;
	Boolean joined; 
    Socket sendSocket;	
    ObjectOutputStream writeTo;
	Node connectTo;
	Node nodeInfo; 
	
	public Sender(Node meshNode) {
		// declare scanner and joined boolean
        input = new Scanner(System.in);
        joined = false;
        nodeInfo = meshNode;
	}
	


	public void run() {
		// loop while true
        while (true) {
            // display console prompt
		    System.out.println("To JOIN Chat : type in JOIN <node's ip address> <node's port number> ____" );
		    System.out.println("TO send a NOTE in the CHAT: type in a message ____");
		    System.out.println("To LEAVE a Chat: type in LEAVE ____");
		    System.out.println("To SHUTDOWN your node: type in SHUTDOWN ____");
		    System.out.println("To SHUTDOWN the network: type in SHUTDOWN_ALL ____");
		    
		    // get input from console
            String inputScan = input.nextLine();

            // check for invalid action based on message type
            if (inputScan.startsWith("JOIN")) {
                if (joined) {
                    System.out.println("Error: Already joined chat");
                    continue;
                }
            }
            else {
                if (!joined)
                {
                    System.out.println("Error: Not in chat");
                    continue;
                }
            }

			try {
				// test for join
				if (inputScan.startsWith("JOIN")) {
					// create node with server info
					String[] messageInfo = inputScan.split(" ");
					connectTo = new Node(messageInfo[1], Integer.parseInt(messageInfo[2]));

					// create socket with input string info
					sendSocket = new Socket(connectTo.getAddress(), connectTo.getPort());
					writeTo = new ObjectOutputStream(sendSocket.getOutputStream());

					// write to the node JOIN, nodeInfo
					writeTo.writeObject(new Message(JOIN, nodeInfo));
					
					
					writeTo.close();
					// close socket 
					sendSocket.close();

					// send joined message
					//socketConnection(JOINED, nodeInfo);
					
					joined = true;
				}
		
				// test for leave
				else if (inputScan.startsWith("LEAVE")) {
					socketConnection(LEAVE, nodeInfo);
				}
		        // test for shutdown_all
				else if (inputScan.startsWith("SHUTDOWN_ALL")) {
					socketConnection(SHUTDOWN_ALL, nodeInfo);
					System.exit(0);
				}
				// test for shutdown
				else if (inputScan.startsWith("SHUTDOWN")) {
					socketConnection(SHUTDOWN, nodeInfo);
					System.exit(0);
				}
		
				// assume note
				else {
					System.out.print("Note is sent:");
					socketConnection( NOTE, inputScan);
				}
			}catch( IOException ex)
			{
				System.err.println("Eroor reading message");
			}
		}
	}
	
	/*
	 * Parameter : JOIN / SHUTDOWN/ SHUTDOWN_ALL / LEAVE / NOTE, nodeInfo / inputScan
	 * Function takes in a message type with an object and loop through the list of nodes
	 * creates a socket to send a message to everyone in the peer to peer network 
	 */
	public void socketConnection(int typeOfMessage, Object content) throws UnknownHostException, IOException {
		// loop through list
		for (int index = 0; index < MeshNode.clientNodes.size(); index++) {
			
			System.out.println(MeshNode.clientNodes.get(index).getAddress()+ " " +MeshNode.clientNodes.get(index).getPort());
	 		// create socket
			sendSocket = new Socket(MeshNode.clientNodes.get(index).getAddress(), MeshNode.clientNodes.get(index).getPort());
			writeTo = new ObjectOutputStream(sendSocket.getOutputStream());
	        
			// write the node typOfMessage, content
			writeTo.writeObject(new Message(typeOfMessage, content));
			
			writeTo.close();
	        // close socket
			sendSocket.close();
	    }
	}
}
