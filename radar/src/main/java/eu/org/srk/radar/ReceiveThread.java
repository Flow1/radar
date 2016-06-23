/**
 * @author Theo den Exter, ARS
 * Date: May 21th 2016
 * Version 1.0
 *
 * Receiver
 *
 * History
 *
 */
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
		logs = LoggerObject.getInstance();

		if (command != "") {
			displayIncoming(is);
		}

		error = false;

		while (!error) {
			//System.out.println("loop");
			is = socket.getDataInputStream();
			error = is == null;

			if (!error) {
				logs.logInfo("Start listening to IVS");
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
				if (command == 6)
					processTrackIdentificatieRIS(is);
				if (command == 7)
					processWijzigingReisGegevensRIS(is);				
			}
		}
		//System.out.println("Stopped");
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
				logs.logError("Error reading RW Stream: " + e);
				disconnect();
			}
		}
	}

	int waitNextSerie(DataInputStream is) {
		Binary t = new Binary();
		boolean next = false;
		byte b = 0;
		try {
			while (!next) {
				boolean ready = false;
				while (!ready) {
					long starttime = System.currentTimeMillis();
					b = is.readByte();
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
									|| (b == 5) || (b == 6) || (b == 7)) {
								next = true;
							} else {
								// System.out.print("Invalid command received: ");
								// System.out.format("%02X ", b);
								// System.out.println();
								logs.logError("Invalid command received: "
										+ t.byteToInt(b));

							}
						}
					}
				}
			}
		} catch (Exception e) {
			logs.logError("Error reading RW Stream: " + e);
			disconnect();
		}

		int i = b;
		return i;
	}

	// Process Incoming Track Identification
	void processTrackIdentificatie(DataInputStream is) {
		try {
			Binary t = new Binary();

			logs.logDebug("Track Identification");
			byte[] r = new byte[4];

			r = getWord(is);
			int reisid = t.fromByteArray1(r);
			logs.logDebug("Journey ID: " + reisid);

			r = getWord(is);
			String scheepslabel = new String(r);
			logs.logDebug("Ships Label: " + scheepslabel);

			r = getWord(is);
			int scheepslengte = t.fromByteArray1(r);
			logs.logDebug("Ships Length: " + scheepslengte);

			r = getWord(is);
			int scheepsbreedte = t.fromByteArray1(r);
			logs.logDebug("Ships Width: " + scheepsbreedte);

			r = getWord(is);
			int diepgang = t.fromByteArray1(r);
			logs.logDebug("Ships Depth: " + diepgang);

			r = getWord(is);
			logs.logDebug("DP Id: " + t.byteToInt(r[2]));
			logs.logDebug("Ships Attributes: " + r[3]);

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

	// Process Incoming Track Identification Extended
	void processTrackIdentificatieUitgebreid(DataInputStream is) {
		try {
			Binary t = new Binary();

			logs.logDebug("Track Identification Extended");

			byte[] r = new byte[4];

			r = getWord(is);
			int reisid = t.fromByteArray1(r);
			logs.logDebug("JourneyID: " + reisid);

			r = getWord(is);
			String scheepslabel = new String(r);
			logs.logDebug("Ships label: " + scheepslabel);

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
			logs.logDebug("Ships Name: " + scheepsnaam);

			r = getWord(is);
			int scheepslengte = t.fromByteArray1(r);
			logs.logDebug("Ships Length: " + scheepslengte);

			r = getWord(is);
			int scheepsbreedte = t.fromByteArray1(r);
			logs.logDebug("Ships Width: " + scheepsbreedte);

			r = getWord(is);
			int diepgang = t.fromByteArray1(r);
			logs.logDebug("Ships Depth: " + diepgang);

			r = getWord(is);
			logs.logDebug("DP Id: " + t.byteToInt(r[2]));
			logs.logDebug("Ships Attributes: " + r[3]);

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

	// Process Incoming Track Identification RIS
	void processTrackIdentificatieRIS(DataInputStream is) {
		try {
			Binary t = new Binary();

			logs.logDebug("Track Identification RIS");

			byte[] r = new byte[4];

			r = getWord(is);
			int reisid = t.fromByteArray1(r);
			logs.logDebug("JourneyID: " + reisid);

			r = getWord(is);
			String scheepslabel = new String(r);
			logs.logDebug("Ships label: " + scheepslabel);

			r = getWord(is);
			int nchar = t.fromByteArray1(r);
			
			String scheepsnaam ="";
			for (int i=0;i<nchar;i++) {
				r = getWord(is);
				if (r[0]==0) r[0]=' ';
				if (r[1]==0) r[1]=' ';
				if (r[2]==0) r[2]=' ';
				if (r[3]==0) r[3]=' ';
				String s1 = "" + (char) r[0] + (char) r[1] + (char) r[2]
						+ (char) r[3];
				scheepsnaam=scheepsnaam+s1;
			}
			scheepsnaam=scheepsnaam.trim();

			r = getWord(is);
			int scheepslengte = t.fromByteArray1(r);
			logs.logDebug("Ships Length: " + scheepslengte);

			r = getWord(is);
			int scheepsbreedte = t.fromByteArray1(r);
			logs.logDebug("Ships Width: " + scheepsbreedte);

			r = getWord(is);
			long mmmi = t.fromByteArray1Long(r);
			logs.logDebug("MMMI: " + mmmi);
			
			r = getWord(is);
			long imo = t.fromByteArray1Long(r);
			logs.logDebug("IMO: " + imo);				
			
			r = getWord(is);
			long euro = t.fromByteArray1Long(r);
			logs.logDebug("Euro: " + euro);
			
			r = getWord(is);
			int diepgang = t.fromByteArray1(r);
			logs.logDebug("Ships Depth: " + diepgang);

			r = getWord(is);
			logs.logDebug("DP Id: " + t.byteToInt(r[2]));
			logs.logDebug("Ships Attributes: " + r[3]);

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
	
	// Process Incoming Change Travel Data
	void processWijzigingReisGegevens(DataInputStream is) {
		try {
			Binary t = new Binary();

			logs.logDebug("Change of Journey Data");

			byte[] r = new byte[4];

			r = getWord(is);
			int nelem = t.fromByteArray1(r);

			for (int k = 0; k < nelem; k++) {

				r = getWord(is);
				int reisid = t.fromByteArray1(r);
				logs.logDebug("Journey ID: " + reisid);

				r = getWord(is);
				String scheepslabel = new String(r);
				logs.logDebug("Ships Label: " + scheepslabel);

				r = getWord(is);
				int scheepslengte = t.fromByteArray1(r);
				logs.logDebug("Ships Length: " + scheepslengte);

				r = getWord(is);
				int scheepsbreedte = t.fromByteArray1(r);
				logs.logDebug("Ships width: " + scheepsbreedte);

				r = getWord(is);
				int diepgang = t.fromByteArray1(r);
				logs.logDebug("Ships Depth: " + diepgang);

				r = getWord(is);
				logs.logDebug("DP Id: " + t.byteToInt(r[2]));
				logs.logDebug("Ships attributes: " + r[3]);

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

	// Process Incoming Traveldata Extended
	void processWijzigingReisGegevensUitgebreid(DataInputStream is) {
		try {
			Binary t = new Binary();

			logs.logDebug("Change Journey Data Extended");

			byte[] r = new byte[4];

			r = getWord(is);
			int nelem = t.fromByteArray1(r);

			for (int k = 0; k < nelem; k++) {

				r = getWord(is);
				int reisid = t.fromByteArray1(r);
				logs.logDebug("JourneyID: " + reisid);

				r = getWord(is);
				String scheepslabel = new String(r);
				logs.logDebug("Ships label: " + scheepslabel);
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
				logs.logDebug("Ships Name: " + scheepsnaam);

				r = getWord(is);
				int scheepslengte = t.fromByteArray1(r);
				logs.logDebug("Ships Length: " + scheepslengte);

				r = getWord(is);
				int scheepsbreedte = t.fromByteArray1(r);
				logs.logDebug("Ships Width: " + scheepsbreedte);

				r = getWord(is);
				int diepgang = t.fromByteArray1(r);
				logs.logDebug("Ships Depth: " + diepgang);

				r = getWord(is);
				logs.logDebug("DP Id: " + t.byteToInt(r[2]));
				logs.logDebug("Ships Attributes: " + r[3]);

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

	// Process Incoming Traveldata ris
	void processWijzigingReisGegevensRIS(DataInputStream is) {
		try {
			Binary t = new Binary();

			logs.logDebug("Change Journey Data RIS");

			byte[] r = new byte[4];

			r = getWord(is);
			int nelem = t.fromByteArray1(r);

			for (int k = 0; k < nelem; k++) {

				r = getWord(is);
				int reisid = t.fromByteArray1(r);
				logs.logDebug("JourneyID: " + reisid);

				r = getWord(is);
				String scheepslabel = new String(r);
				logs.logDebug("Ships label: " + scheepslabel);


				r = getWord(is);
				int nchar = t.fromByteArray1(r);
				
				String scheepsnaam ="";
				for (int i=0;i<nchar;i++) {
					r = getWord(is);
					if (r[0]==0) r[0]=' ';
					if (r[1]==0) r[1]=' ';
					if (r[2]==0) r[2]=' ';
					if (r[3]==0) r[3]=' ';
					String s1 = "" + (char) r[0] + (char) r[1] + (char) r[2]
							+ (char) r[3];
					scheepsnaam=scheepsnaam+s1;
				}
				scheepsnaam=scheepsnaam.trim();
				
				logs.logDebug("Ships Name: " + scheepsnaam);

				r = getWord(is);
				int scheepslengte = t.fromByteArray1(r);
				logs.logDebug("Ships Length: " + scheepslengte);

				r = getWord(is);
				int scheepsbreedte = t.fromByteArray1(r);
				logs.logDebug("Ships Width: " + scheepsbreedte);

				r = getWord(is);
				int diepgang = t.fromByteArray1(r);
				logs.logDebug("Ships Depth: " + diepgang);

				r = getWord(is);
				long mmmi = t.fromByteArray1Long(r);
				logs.logDebug("MMMI: " + mmmi);
				
				r = getWord(is);
				long imo = t.fromByteArray1Long(r);
				logs.logDebug("IMO: " + imo);				
				
				r = getWord(is);
				long euro = t.fromByteArray1Long(r);
				logs.logDebug("Euro: " + euro);				
				
				r = getWord(is);
				logs.logDebug("DP Id: " + t.byteToInt(r[2]));
				logs.logDebug("Ships Attributes: " + r[3]);

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
			logs.logDebug("JourneyID: " + reisid);

			r = getWord(is);
			logs.logDebug("DP Id: " + r[2]);
			logs.logDebug("Video Displaymode: " + r[3]);
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

}
