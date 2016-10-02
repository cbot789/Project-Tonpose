package tonpose;

import java.io.IOException;
import java.util.Scanner;

public class ClientTest {
	public static void main(String[] args) throws IOException{
		String ip = "10.25.70.122"; //DK-02 Server IP
		int port = 8080;
		String name;
		String type;
		String data;
		Scanner in = new Scanner(System.in);
		System.out.print("Enter client username: ");
		name = in.nextLine();
		Client client = new Client(ip, name, port);
		Message msg;
		while(true){
			System.out.print("Enter message type: ");
			type = in.nextLine();
			System.out.print("Enter message: ");
			data = in.nextLine();
			msg = new Message(type);
			msg.setData(data);
			client.sendMsg(msg);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(client.serverMsg);
		}
	}
}
