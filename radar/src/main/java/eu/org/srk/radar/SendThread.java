/**
 * @author Theo den Exter, ARS
 * Date: May 21th 2016
 * Version 1.0
 *
 * Sender
 *
 * History
 *
 */
package eu.org.srk.radar;

import java.io.*;
import java.util.List;
import java.lang.Thread;
import java.util.Random;

//Positie rapport	RW	=> IVS, 1x per minuut, max 300 schepen, bericht id: 101
//Zichtmeter informatie	RW	=> IVS, per 10 seconden, bericht id: 102
//Reis geselecteerd	RW	=> IVS, op verzoek operator, bericht id: 105

class SendThread extends Thread {
	List<Object> list;
	List<Object> listZichtMeter;

	LoggerObject logs;
	boolean error = false;
	Services service;
	StopButton buttonB;

	DataOutputStream os;
	ServerSocketManagement socket;

	public SendThread(DataOutputStream p1) {
		service = Services.getInstance();
		this.list = service.list;
		this.listZichtMeter = service.listZichtMeter;
		this.os = p1;
		buttonB = StopButton.getInstance();

		os = null;
	}

	public void run() {

		socket = ServerSocketManagement.getInstance();

		// Test sending
		// sendTest();

		// Communicate every 10 seconds
		int sec = 4;
		int count = 1;
		int countsec = 0;
		Random randomGenerator = new Random();

		int randomInt = randomGenerator.nextInt(30) + 5;

		logs = LoggerObject.getInstance();
		JourneySelect p = JourneySelect.getInstance();
		error = false;

		while (!error) {
			if (error) {
				os = null;
			} else {
				os = socket.getDataOutputStream();
			}
			error = os == null;

			try {
				Thread.sleep(1000);
			} catch (InterruptedException ie) {
				// Handle exception
			}

			countsec++;
			if (countsec >= 10) {
				countsec = 0;

				logs.logInfo("Wake up");

				if (buttonB.getButton()) {
//					logs.logInfo("Send zicht meter");
//					sendZichtMeter(0);
//					try {
//						Thread.sleep(600);
//					} catch (InterruptedException ie) {
//						// Handle exception
//					}
//					sendZichtMeter(1);

					if (count == 6) {
						try {
							Thread.sleep(600);
						} catch (InterruptedException ie) {
							// Handle exception
						}
						sendPositionReport(sec);
						count = 0;
					}
					count++;
				}
			} else {
				if (p.isLoaded()) {
					byte b = p.getDpID();
					Long i = new Long(p.getTravelID());
					p.clearLoaded();
					try {
						Thread.sleep(600);
					} catch (InterruptedException ie) {
						// Handle exception
					}
					sendTraveLSelected(i, b);
				}

			}
		}

	}

	private void disconnect() {
		error = true;
		os = null;
		socket.disconnect();
	}

	// Send test
	public void sendTest() {
		try {
			while (true) {
				for (int k = 0; k <= 255; k++) {
					byte r = (byte) k;
					System.out.println(r);
					os.write(r);
				}
			}
		} catch (IOException e) {
			disconnect();
		}
	}

	// Send position report
	public void sendPositionReport(int sec) {
		try {
			Binary t = new Binary();

			int i = list.size();

			if (i != 0) {

				int b = 101;
				byte[] c = t.toByteArray1(b);
				os.write(c, 0, 4);

				c = t.toByteArray1(i);
				os.write(c, 0, 4);

				for (int k = 0; k < i; k++) {
					PositionReport w = (PositionReport) list.get(k);

					// Calculate new position
					w.calculateMove(sec);

					c = t.toByteArray1(w.getReisID());
					os.write(c, 0, 4);

					c = t.toByteArray1(w.getXPos());
					os.write(c, 0, 4);

					c = t.toByteArray1(w.getYPos());
					os.write(c, 0, 4);

					c[0] = 0;
					c[1] = 0;
					c[2] = 0;
					c[3] = 0;
					if (w.getVraagBijkomendeInfo() == 1) {
						c[3] = 1;
					}
					os.write(c, 0, 4);
				}

			}
		} catch (IOException e) {
			disconnect();
		}

	}

	// Send Travel Selected
	public void sendTraveLSelected(long reisID, byte dpid) {
		try {
			Binary t = new Binary();
			int b = 105;
			byte[] c = t.toByteArray1(b);
			os.write(c);

			c = t.toByteArray1Long(reisID);
			os.write(c);

			int d = dpid;
			c = t.toByteArray1(d);
			os.write(c);

		} catch (IOException e) {
			disconnect();
		}
	}

	// Send Sight Meter
	public void sendZichtMeter(int meter) {
		try {
			Binary t = new Binary();
			int b = 102;
			byte[] c = t.toByteArray1(b);
			os.write(c);

			ZichtmeterInfo w = (ZichtmeterInfo) listZichtMeter.get(meter);

			c[0] = w.zicht[0];
			c[1] = w.zicht[1];
			c[2] = w.comstat;
			c[3] = w.id;
			os.write(c);

			c[0] = w.zelftest;
			c[1] = w.status;
			c[2] = w.mistind;
			c[3] = w.zicht[2];
			os.write(c);

		} catch (IOException e) {
			disconnect();
		}
	}

}
