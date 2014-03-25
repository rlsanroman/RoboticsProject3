package project3;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SocketClient {
	private static PrintWriter out;
	
	public static void start(String port, String ip_address) {
		try {
			startClient(Integer.parseInt(port), ip_address);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			while(!storedClientInput.equals("\\disconnect"))
			{
				storedClientInput=ClientInput.nextLine();
				out=new PrintWriter(socket.getOutputStream(), true);
				out.println(storedClientInput);
			}
			while(!socket.isClosed())
			{				
			}
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
