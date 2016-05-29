/**
 * @author Theo den Exter, ARS
 * Date: May 21th 2016
 * Version 1.0
 *
 * Simulator engine
 *
 * History
 *
 */
package eu.org.srk.radar;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Simulator extends Thread {

	// List of journeys
	List<Object> list = new ArrayList<Object>();
	List<Object> listZichtMeter = new ArrayList<Object>();
	Services service;

	ServerSocketManagement serverSocket;

	Simulator() {
		service = Services.getInstance();
		this.list = service.list;
		this.listZichtMeter = service.listZichtMeter;
	}

	public void run() {

		PropertiesObject props = PropertiesObject.getInstance();

		String server = props.getProperty("server");
		String portListen = props.getProperty("portlisten");
		String portWrite = props.getProperty("portwrite");

		// ServerSocket echoServer = null;
		// String line;
		DataInputStream is;
		DataOutputStream os;

		serverSocket = ServerSocketManagement.getInstance();
		serverSocket.createServerSocket(server, portListen);

		is = null;
		os = null;

		while (true) {
			try {
				serverSocket.waitForConnection();
				is = serverSocket.getDataInputStream();
				os = serverSocket.getDataOutputStream();

				SendThread p1 = new SendThread(os);
				p1.start();

				ReceiveThread p2 = new ReceiveThread(is, "");
				p2.start();

				// Wait for ending threads
				p1.join();
				p2.join();

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}