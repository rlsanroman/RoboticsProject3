package project3;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class SocketServer {
	
	public static void start(String port) {
		try {
			startServer(Integer.parseInt(port));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void startServer(int PORT) throws Exception {
		//int PORT=0;
		int BACKLOG=1;
		int ClientNumber=1;
		//Scanner input=new Scanner(System.in);
		System.out.println("Server IP address: " + InetAddress.getLocalHost().getHostAddress());
		//System.out.println("Please enter the PORT number.");
		//PORT=input.nextInt();
		//input.nextLine();
		//System.out.println("Please enter the max number of clients that can connect to the server.");
		//BACKLOG=input.nextInt();
		
		ServerSocket listen = new ServerSocket(PORT, BACKLOG);
		System.out.println("Server is now listening.");
		try {
			while(true) {
				new ClientListenThread(listen.accept(), ClientNumber).start();
				//ClientNumber++;
			}
		} finally {
			//input.close();
			listen.close();
		}
	}
	
	private static class ClientListenThread extends Thread {
		private Socket socket;
		private PrintWriter out;
		private int ClientNumber;
		
		public ClientListenThread(Socket socket, int ClientNumber){
			this.socket=socket;
			this.ClientNumber=ClientNumber;
		}
		
		public void run() {
			try{
				String ClientInput="";
				out=new PrintWriter(socket.getOutputStream(), true);
				out.println("Connection between the server and the client" +
						"is successfully established!"+
						"You are client "+ClientNumber+"!");
				new ServerSendThread(socket).start();
				Scanner socketInput = new Scanner(socket.getInputStream());
				while(true) {
					ClientInput=socketInput.nextLine();
					if(ClientInput.equals("\\disconnect"))
					{
						out.println("\\disconnect");
						break;
					}
					System.out.println(socket.getInetAddress()+":"+socket.getLocalPort()+": "+ClientInput);	
				}
				System.out.println("Client"+ClientNumber+"("+socket.getInetAddress()+") is diconnected.");
				socketInput.close();
			} catch (IOException e) {
				System.out.println(e);
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
					System.out.println(e);
				}
			}
		}
	}
	
	private static class ServerSendThread extends Thread {
		private Socket socket;
		private PrintWriter out;
		String storedServerInput="";
		
		public ServerSendThread(Socket socket) {
			this.socket=socket;
		}
		
		public void run() {
			try{
				Scanner ServerInput=new Scanner(System.in);
				out=new PrintWriter(socket.getOutputStream(), true);
				while(true)
				{
					storedServerInput=ServerInput.nextLine();
					out=new PrintWriter(socket.getOutputStream(), true);
					out.println(storedServerInput);
					if(socket.isClosed()) break;
				}
				ServerInput.close();
			} catch (IOException e) {
				return;
			} finally {	
				try {
					socket.close();
				} catch (IOException e) {
					System.out.println(e);
				}
			}
		}
	}
}
