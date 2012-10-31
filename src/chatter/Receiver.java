package chatter;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

public class Receiver implements Runnable {
	
	private MulticastSocket s;

	public Receiver(MulticastSocket s) {
		this.s = s;
	}
	
	public void run() {
		byte[] buf = new byte[1000];
		DatagramPacket recv = new DatagramPacket(buf, buf.length);
		while(true) {
			try {
				s.receive(recv);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(recv.getAddress() + ": " + new String(recv.getData(), 0, recv.getLength()));
		}
	}
}
