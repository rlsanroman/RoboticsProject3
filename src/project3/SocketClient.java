package project3;

import java.awt.Graphics;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.SwingWorker;

public class SocketClient extends SwingWorker<Integer, Integer> {
	private static PrintWriter out;
	public static int PORT;
	public static String ServerIP, message="";
	private static Boolean messageSet=false; //
	
	public static void setMessage(String m) {
		SocketClient.message = m;
		messageSet = true;
	}

	public static void setPORT(int p) {
		PORT = p;
	}
	
	public static void setIP(String ip) {
		ServerIP = ip;
	}
	
	protected Integer doInBackground() throws Exception
    {
        // Do a time-consuming task.
		startClient(PORT, ServerIP);
        Thread.sleep(1000);
        return 42;
    }
	
	public static void startClient(int PORT, String ServerIP) throws Exception {
		//int PORT=0;
		//String ServerIP;
		//Scanner input=new Scanner(System.in);
		System.out.println("This is the Client.");
		//System.out.println("Please enter the Server IP.");
		//ServerIP=input.nextLine();
		//System.out.println("Please enter the PORT number.");
		//PORT=input.nextInt();
		//input.nextLine();
		
		Socket socket = new Socket(ServerIP, PORT);
		Scanner ClientInput=new Scanner(System.in);
		//Scanner socketInput = new Scanner(socket.getInputStream());
		String storedClientInput="";
		
		try {
			//Scanner socketInput = new Scanner(socket.getInputStream());
			//System.out.println(socketInput.nextLine())
			new ClientListenThread(socket).start();
			while(true) {
				if(!message.equals("disconnect") && messageSet) {
					out=new PrintWriter(socket.getOutputStream(), true);
					//SocketServer.setReady(true);
					out.println(message);					
					messageSet = false;
				}
			}
//			while(!storedClientInput.equals("\\disconnect"))
//			{
//				storedClientInput=ClientInput.nextLine();
//				String s = "This is a test";
//				System.console().writer().println(s);
//				out=new PrintWriter(socket.getOutputStream(), true);
//				out.print("This is a test");
//				out.println(storedClientInput);
//			}
//			while(!socket.isClosed())
//			{				
//			}
			//socketInput.close();
		} finally {
			socket.close();
			System.out.println("Client(sending end) is closed.");
			//input.close();
			ClientInput.close();
		}
	}
	
	private static class ClientListenThread extends Thread {
		private Socket socket;
		
		public ClientListenThread(Socket socket) {
			this.socket=socket;
		}
		public void run() {
			try {
				String StoredServerInput="";
				Scanner socketInput = new Scanner(socket.getInputStream());
				while(true) {
					StoredServerInput=socketInput.nextLine();
					if(StoredServerInput.equals("\\disconnect"))
					{
						out.println("\\disconnect");
						break;
					}
					System.out.println(StoredServerInput);
				}
				socketInput.close();
			} catch (IOException e) {
				System.out.println(e);
			} finally {
				try {
					socket.close();
					System.out.println("Client(receiving end) is closed.");
				} catch (IOException e) {
					System.out.println(e);
				}
			}
		}
	}
}
