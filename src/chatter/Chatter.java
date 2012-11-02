package chatter;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class Chatter {

	public static void main(String[] args) throws IOException {
		final MulticastSocket socket = new MulticastSocket(5172);
		final InetAddress group = InetAddress.getByName("235.1.7.2");
		
		// open multicast socket
		socket.joinGroup(group);

		// create gui
		Display display = new Display();
		Shell shell = new Shell(display);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		shell.setLayout(layout);
		shell.setMinimumSize(300, 150);
		shell.setSize(450, 300);
		shell.setText("Chatter");

		final Text text_log = new Text(shell, SWT.BORDER | SWT.MULTI | SWT.WRAP);
		text_log.setEditable(false);
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 2;
		text_log.setLayoutData(gridData);

		final Text text_entry = new Text(shell, SWT.BORDER | SWT.SINGLE);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		text_entry.setLayoutData(gridData);
		text_entry.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				Chatter.sendMessage(text_entry.getText(), socket, group);
				text_entry.setText("");
			}
			public void widgetSelected(SelectionEvent e) {
				Chatter.sendMessage(text_entry.getText(), socket, group);
				text_entry.setText("");
			}
		});

		Button button =  new Button(shell, SWT.PUSH);
		button.setText("Send");
		button.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				Chatter.sendMessage(text_entry.getText(), socket, group);
				text_entry.setText("");
			}
			public void widgetSelected(SelectionEvent e) {
				Chatter.sendMessage(text_entry.getText(), socket, group);
				text_entry.setText("");
			}
		});
		
		// receiver thread
		Receiver receiver = new Receiver(socket, text_log);
		new Thread(receiver).start();

		// moar gui stuff
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	public static void sendMessage(String message, MulticastSocket s, InetAddress group) {
		DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(),
				group, 5172);
		try {
			s.send(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
