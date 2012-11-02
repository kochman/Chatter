package chatter;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

public class Receiver implements Runnable {

	private MulticastSocket socket;
	private Text text_log;

	public Receiver(MulticastSocket socket, Text text_log) {
		this.socket = socket;
		this.text_log = text_log;
	}

	public void run() {
		byte[] buf = new byte[1000];
		final DatagramPacket recv = new DatagramPacket(buf, buf.length);
		while(true) {
			try {
				socket.receive(recv);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					text_log.append(recv.getAddress().toString().substring(1) + ": " + new String(recv.getData(), 0, recv.getLength()) + System.getProperty("line.separator"));
				}
			});
		}
	}
}
