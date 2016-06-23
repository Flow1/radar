/**
 * @author Theo den Exter, ARS
 * Date: May 21th 2016
 * Version 1.0
 *
 * Start communication engine
 *
 * Note: contains a teststub for providing messages from IVS to RW
 *       This will also be the future interface between JMS and Engine
 *
 * History
 *
 */

package eu.org.srk.radar;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Simulator extends Thread {

	String server; 
	String portListen;
	String portWrite;
	DataInputStream is;
	DataOutputStream os;

	ClientSocketManagement socket;

	Simulator(String server, String portListen, String portWrite) {
		this.server = server;
		this.portListen = portListen;
		this.portWrite = portWrite;
	}

	public void run() {

		socket = ClientSocketManagement.getInstance();
		socket.adminSocket(this.server, this.portListen, this.portWrite);
		is = socket.getDataInputStream();
		os = socket.getDataOutputStream();

		try {
			while (true) {

				is = socket.getDataInputStream();
				os = socket.getDataOutputStream();
				if ((is == null) || (os == null)) {
					socket.getConnectionSingle();
				}

				if ((is != null) && (os != null)) {
					SendThread p1 = new SendThread(os);
					p1.start();

					ReceiveThread p2 = new ReceiveThread(is, "");
					p2.start();

					// Wait for ending threads, btw: will never happen
					p1.join();
					p2.join();
				}
			}

		} catch (InterruptedException e) {
			;
		}
	}
}