package cs309.tonpose;

import java.io.IOException;
import java.util.Scanner;

public class ClientTest {
	public static void main(String[] args) throws IOException{
		String ip = "localhost";//"10.25.70.122"; //DK-02 Server IP
		int port = 8080;
		String name;
		String type;
		String data1;
		String data2;
		int data3;
		int data4;
		Scanner in = new Scanner(System.in);
		System.out.print("Enter client username: ");
		name = in.nextLine();
		Client client = new Client(ip, name, port);
		Message msg;
		while(true){
			System.out.print("Enter message type: ");
			type = in.nextLine();
			System.out.print("Enter data 1: ");
			data1 = in.nextLine();
			System.out.print("Enter data 2: ");
			data2 = in.nextLine();
			System.out.print("Enter data 3: ");
			data3 = in.nextInt();
			System.out.print("Enter data 4: ");
			data4 = in.nextInt();
			msg = new Message(type);
			msg.setData1(data1);
			msg.setData2(data2);
			msg.setData3(data3);
			msg.setData4(data4);
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
