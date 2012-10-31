package chatter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Chatter {
	public static void main(String[] args) throws IOException {
		InetAddress group = InetAddress.getByName("235.1.7.2");
		MulticastSocket s = new MulticastSocket(5172);
		s.joinGroup(group);
		
		Runnable receiver = new Receiver(s);
		new Thread(receiver).start();
		
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		while(true) {
		    String msg = stdin.readLine();
			DatagramPacket hi = new DatagramPacket(msg.getBytes(), msg.length(),
					group, 5172);
			s.send(hi);
		}
	}

}
