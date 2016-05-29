package eu.org.srk.radar;

import java.io.*;
import java.util.List;
import java.lang.Thread;

//Track identificatie 1
//Wijziging van reis gegevens 2
//Video display mode 3
//Track identificatie uitgebreid 4
//Wijziging van reisgegevens uitgebreid 5

class ReceiveThread extends Thread {
	DataInputStream is;
	List<Object> list;
	String command;

	LoggerObject logs;
	boolean error = false;
	Services service;
	ServerSocketManagement socket;

	public ReceiveThread(DataInputStream p1, String command1) {
		service = Services.getInstance();
		this.list = service.list;
		this.command = command1;
		is = null;
	}

	public void run() {

		socket = ServerSocketManagement.getInstance();
		is = socket.getDataInputStream();

		if (command != "") {
			System.out.println("Listen");
			displayIncoming(is);
		}

		error = false;

		logs.logInfo("Start listening to IVS");

		while (!error) {

			is = socket.getDataInputStream();
			error = is == null;

			int command = waitNextSerie(is);
			if (command == 1)
				processTrackIdentificatie(is);
			if (command == 2)
				processWijzigingReisGegevens(is);
			if (command == 3)
				processVideoDisplayMode(is);
			if (command == 4)
				processTrackIdentificatieUitgebreid(is);
			if (command == 5)
				processWijzigingReisGegevensUitgebreid(is);
		}
	}

	private void disconnect() {
		error = true;
		is = null;
		socket.disconnect();
	}

	// Read a 32 bit word
	byte[] getWord(DataInputStream is) throws IOException {

		byte[] r = new byte[4];
		r[0] = ' ';
		r[1] = ' ';
		r[2] = ' ';
		r[3] = ' ';
		r[0] = is.readByte();
		r[1] = is.readByte();
		r[2] = is.readByte();
		r[3] = is.readByte();
		return r;
	}

	void displayIncoming(DataInputStream is) {
		while (true) {
			try {
				long starttime = System.currentTimeMillis();

				byte b = is.readByte();

				long endtime = System.currentTimeMillis();
				long difference = endtime - starttime;
				System.out.print(difference + " msec, byte=");
				System.out.format("%02X ", b);
				System.out.println();
			} catch (Exception e) {
				System.out.println("theo: " + e);
				disconnect();
			}
		}
	}

	int waitNextSerie(DataInputStream is) {
		System.out.println("Wait next");
		boolean next = false;
		byte b = 0;
		try {
			while (!next) {
				boolean ready = false;
				while (!ready) {
					long starttime = System.currentTimeMillis();

					b = is.readByte();

					System.out.println(b);

					long endtime = System.currentTimeMillis();
					long difference = endtime - starttime;
					// System.out.print(difference+" msec, byte=");
					// System.out.format("%02X ",b);
					// System.out.println();
					if (difference > 200L)
						ready = true;
				}
				if (b == 0) {
					b = is.readByte();
					if (b == 0) {

						b = is.readByte();
						if (b == 0) {
							b = is.readByte();

							if ((b == 1) || (b == 2) || (b == 3) || (b == 4)
									|| (b == 5)) {
								next = true;
							} else {
								System.out.print("Invalid command received: ");
								System.out.format("%02X ", b);
								System.out.println();

							}
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Error reading RW Stream: " + e);
			disconnect();
		}

		int i = b;
		return i;
	}

	// Process Incoming Track Identification
	void processTrackIdentificatie(DataInputStream is) {
		try {
			Binary t = new Binary();

			System.out.println("Track Identification");
			byte[] r = new byte[4];

			r = getWord(is);
			int reisid = t.fromByteArray1(r);
			System.out.println("ReisID: " + reisid);

			r = getWord(is);
			int nelem = t.fromByteArray1(r);

			String scheepslabel = new String(r);
			System.out.println("Scheepslabel: " + scheepslabel);

			r = getWord(is);
			int scheepslengte = t.fromByteArray1(r);
			System.out.println("Scheepslengte: " + scheepslengte);

			r = getWord(is);
			int scheepsbreedte = t.fromByteArray1(r);
			System.out.println("Scheepsbreedte: " + scheepsbreedte);

			r = getWord(is);
			int diepgang = t.fromByteArray1(r);
			System.out.println("Diepgang: " + diepgang);

			r = getWord(is);
			System.out.println("DP Id: " + r[2]);
			System.out.println("Scheepskenmerken: " + r[3]);

			boolean f = false;

			if (!error) {
				for (int i = 0; i < list.size(); i++) {
					PositionReport w = (PositionReport) list.get(i);
					if (w.getReisID() == reisid) {
						w.setDpID(r[2]);
						w.setScheepsLabel(scheepslabel);
						w.setScheepsLengte(scheepslengte);
						w.setScheepsBreedte(scheepsbreedte);
						w.setScheepsDiepgang(diepgang);
						byte j = r[3];
						w.setScheepsKenmerk(j);
						f = true;
					}
				}
				if (!f) {
					System.out.println("Nieuwe journey");
					PositionReport w = new PositionReport();

					w.setReisID(reisid);
					w.setDpID(r[2]);
					w.setScheepsLabel(scheepslabel);
					w.setScheepsLengte(scheepslengte);
					w.setScheepsBreedte(scheepsbreedte);
					w.setScheepsDiepgang(diepgang);
					byte j = r[3];
					w.setScheepsKenmerk(j);
					list.add(w);
				}
			}
		} catch (Exception e) {
			disconnect();
		}
	}

	// Process Incoming Change Travel Data
	void processWijzigingReisGegevens(DataInputStream is) {
		try {
			Binary t = new Binary();

			System.out.println("Wijziging Reis Gegevens");

			byte[] r = new byte[4];

			r = getWord(is);
			int reisid = t.fromByteArray1(r);
			System.out.println("ReisID: " + reisid);

			r = getWord(is);
			int nelem = t.fromByteArray1(r);

			for (int k = 0; k < nelem; k++) {

				String scheepslabel = new String(r);
				System.out.println("Scheepslabel: " + scheepslabel);

				r = getWord(is);
				int scheepslengte = t.fromByteArray1(r);
				System.out.println("Scheepslengte: " + nelem);
				r = getWord(is);
				int scheepsbreedte = t.fromByteArray1(r);
				System.out.println("Scheepsbreedte: " + nelem);
				r = getWord(is);
				int diepgang = t.fromByteArray1(r);
				System.out.println("Diepgang: " + nelem);
				r = getWord(is);

				System.out.println("Scheepskenmerken: " + r[3]);

				boolean done = false;
				if (!error) {
					for (int i = 0; i < list.size(); i++) {
						PositionReport w = (PositionReport) list.get(i);
						if (w.getReisID() == reisid) {
							w.setDpID(r[2]);
							w.setScheepsLabel(scheepslabel);
							w.setScheepsLengte(scheepslengte);
							w.setScheepsBreedte(scheepsbreedte);
							w.setScheepsDiepgang(diepgang);
							byte j = r[3];
							w.setScheepsKenmerk(j);
							done = true;
						}
					}
					if (!done) {
						PositionReport w = new PositionReport();
						w.setReisID(reisid);
						w.setDpID(r[2]);
						w.setScheepsLabel(scheepslabel);
						w.setScheepsLengte(scheepslengte);
						w.setScheepsBreedte(scheepsbreedte);
						w.setScheepsDiepgang(diepgang);
						byte j = r[3];
						w.setScheepsKenmerk(j);
						list.add(w);
					}
				}
			}
		} catch (Exception e) {
			disconnect();
		}
	}

	// Process Incoming Video Display Mode
	void processVideoDisplayMode(DataInputStream is) {
		try {
			Binary t = new Binary();

			logs.logDebug("Video Display Mode");

			byte[] r = new byte[4];

			r = getWord(is);
			int reisid = t.fromByteArray1(r);
			System.out.println("ReisID: " + reisid);

			r = getWord(is);
			System.out.println("DP Id: " + r[2]);
			System.out.println("Video Displaymode: " + r[3]);
			for (int i = 0; i < list.size(); i++) {
				if (!error) {
					PositionReport w = (PositionReport) list.get(i);
					if (w.getReisID() == reisid) {
						w.setVideoDisplayMode(r[3]);
						w.setDpID(r[2]);
					}
				}
			}

		} catch (Exception e) {
			disconnect();
		}
	}

	// Process Incoming Track Identification Extended
	void processTrackIdentificatieUitgebreid(DataInputStream is) {
		try {
			Binary t = new Binary();

			System.out.println("Track Identificatie Uitgebreid");

			byte[] r = new byte[4];

			r = getWord(is);
			int reisid = t.fromByteArray1(r);
			System.out.println("ReisID: " + reisid);

			r = getWord(is);
			int nelem = t.fromByteArray1(r);

			String scheepslabel = new String(r);
			System.out.println("Scheepslabel: " + scheepslabel);

			byte[] r1 = getWord(is);
			byte[] r2 = getWord(is);
			byte[] r3 = getWord(is);
			byte[] r4 = getWord(is);

			String s1 = "" + (char) r1[0] + (char) r1[1] + (char) r1[2]
					+ (char) r1[3];
			String s2 = "" + (char) r2[0] + (char) r2[1] + (char) r2[2]
					+ (char) r2[3];
			String s3 = "" + (char) r3[0] + (char) r3[1] + (char) r3[2]
					+ (char) r3[3];
			String s4 = "" + (char) r4[0] + (char) r4[1] + (char) r4[2]
					+ (char) r4[3];

			String scheepsnaam = s1 + s2 + s3 + s4;
			System.out.println("Scheepsnaam: " + scheepsnaam);

			r = getWord(is);
			int scheepslengte = t.fromByteArray1(r);
			System.out.println("Scheepslengte: " + scheepslengte);

			r = getWord(is);
			int scheepsbreedte = t.fromByteArray1(r);
			System.out.println("Scheepsbreedte: " + scheepsbreedte);

			r = getWord(is);
			int diepgang = t.fromByteArray1(r);
			System.out.println("Diepgang: " + diepgang);

			r = getWord(is);
			System.out.println("DP Id: " + r[2]);

			System.out.println("Scheepskenmerken: " + r[3]);

			boolean f = false;
			if (!error) {
				for (int i = 0; i < list.size(); i++) {

					PositionReport w = (PositionReport) list.get(i);
					if (w.getReisID() == reisid) {
						w.setDpID(r[2]);
						w.setScheepsLabel(scheepslabel);
						w.setScheepsLengte(scheepslengte);
						w.setScheepsBreedte(scheepsbreedte);
						w.setScheepsNaam(scheepsnaam);
						w.setScheepsDiepgang(diepgang);
						byte j = r[3];
						w.setScheepsKenmerk(j);
						f = true;
					}

				}
				if (!f) {
					System.out.println("Nieuwe journey");
					PositionReport w1 = new PositionReport();

					w1.setReisID(reisid);
					w1.setDpID(r[2]);
					w1.setScheepsLabel(scheepslabel);
					w1.setScheepsLengte(scheepslengte);
					w1.setScheepsBreedte(scheepsbreedte);
					w1.setScheepsNaam(scheepsnaam);
					w1.setScheepsDiepgang(diepgang);
					byte j = r[3];
					w1.setScheepsKenmerk(j);
					list.add(w1);
				}
			}

		} catch (Exception e) {
			disconnect();
		}
	}

	// Process Incoming Traveldata Extended
	void processWijzigingReisGegevensUitgebreid(DataInputStream is) {
		try {
			Binary t = new Binary();

			System.out.println("Wijziging Reis Gegevens");

			byte[] r = new byte[4];

			r = getWord(is);
			int reisid = t.fromByteArray1(r);
			System.out.println("ReisID: " + reisid);

			r = getWord(is);
			int nelem = t.fromByteArray1(r);

			for (int k = 0; k < nelem; k++) {

				String scheepslabel = new String(r);
				System.out.println("Scheepslabel: " + scheepslabel);
				byte[] r1 = getWord(is);
				byte[] r2 = getWord(is);
				byte[] r3 = getWord(is);
				byte[] r4 = getWord(is);

				String s1 = "" + (char) r1[0] + (char) r1[1] + (char) r1[2]
						+ (char) r1[3];
				String s2 = "" + (char) r2[0] + (char) r2[1] + (char) r2[2]
						+ (char) r2[3];
				String s3 = "" + (char) r3[0] + (char) r3[1] + (char) r3[2]
						+ (char) r3[3];
				String s4 = "" + (char) r4[0] + (char) r4[1] + (char) r4[2]
						+ (char) r4[3];

				String scheepsnaam = s1 + s2 + s3 + s4;
				System.out.println("Scheepsnaam: " + scheepsnaam);
				r = getWord(is);
				int scheepslengte = t.fromByteArray1(r);
				System.out.println("Scheepslengte: " + nelem);
				r = getWord(is);
				int scheepsbreedte = t.fromByteArray1(r);
				System.out.println("Scheepsbreedte: " + nelem);
				r = getWord(is);
				int diepgang = t.fromByteArray1(r);
				System.out.println("Diepgang: " + nelem);
				r = getWord(is);

				System.out.println("Scheepskenmerken: " + r[3]);

				boolean done = false;
				if (!error) {
					for (int i = 0; i < list.size(); i++) {
						PositionReport w = (PositionReport) list.get(i);
						if (w.getReisID() == reisid) {
							w.setDpID(r[2]);
							w.setScheepsLabel(scheepslabel);
							w.setScheepsLengte(scheepslengte);
							w.setScheepsBreedte(scheepsbreedte);
							w.setScheepsDiepgang(diepgang);
							byte j = r[3];
							w.setScheepsKenmerk(j);
							done = true;
						}
					}
					if (!done) {
						PositionReport w = new PositionReport();
						w.setReisID(reisid);
						w.setDpID(r[2]);
						w.setScheepsLabel(scheepslabel);
						w.setScheepsLengte(scheepslengte);
						w.setScheepsBreedte(scheepsbreedte);
						w.setScheepsDiepgang(diepgang);
						byte j = r[3];
						w.setScheepsKenmerk(j);
						list.add(w);
					}
				}
			}
		} catch (Exception e) {
			disconnect();
		}
	}
}
