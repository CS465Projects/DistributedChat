package User;

import java.net.Socket;
import java.util.ArrayList;

import Node.Node;

public class Main {
	public static void main(String[] args)
    {
		try
		{
		// create a socket with nodes ip and port
		Socket clientSocket = new Socket( "192.168.0.4", 8082);
		
	
		// create client handler thread 
		System.out.println("connected");
		
		
		} catch( Exception ex ) {
			ex.printStackTrace();
		}
	// 
	}
	
	
           
   
    }

