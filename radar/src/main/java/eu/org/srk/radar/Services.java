package eu.org.srk.radar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Iterator;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ClassNotFoundException;

public class Services {

	private static Services instance = null;

	// List of vessels
	static List<Object> list = new ArrayList<Object>();
	static List<Object> listZichtMeter = new ArrayList<Object>();

	protected Services() {
	}

	public static Services getInstance() {
		if (instance == null) {
			instance = new Services();
		}
		return instance;
	}

	private void writeObject(List<Object> listAccount, String filename)
			throws IOException {
		FileOutputStream fos = new FileOutputStream(filename);
		ObjectOutputStream objOutputStream = new ObjectOutputStream(fos);
		for (Object obj : listAccount) {

			objOutputStream.writeObject(obj);
			objOutputStream.reset();
		}
		objOutputStream.close();
	}

	private List<Object> readObject(String filename)
			throws ClassNotFoundException, IOException {
		List<Object> list1 = new ArrayList();
		FileInputStream fis = new FileInputStream(filename);
		ObjectInputStream obj = new ObjectInputStream(fis);
		try {
			while (fis.available() != -1) {
				// Read object from file
				PositionReport acc = (PositionReport) obj.readObject();
				list1.add(acc);
			}
		} catch (EOFException ex) {
			// System.out.println("Error reading file " + filename + " " + ex);
		}
		return list1;
	}

	private List<Object> readObjectZM(String filename)
			throws ClassNotFoundException, IOException {
		List<Object> list1 = new ArrayList();
		FileInputStream fis = new FileInputStream(filename);
		ObjectInputStream obj = new ObjectInputStream(fis);
		try {
			while (fis.available() != -1) {
				// Read object from file
				ZichtmeterInfo acc = (ZichtmeterInfo) obj.readObject();
				list1.add(acc);
			}
		} catch (EOFException ex) {
			// System.out.println("Error reading file " + filename + " " + ex);
		}
		return list1;
	}

	public PositionReport getPositionReport(Integer reisID) {

		if (list != null) {
			for (int k = 0; k < list.size(); k++) {
				PositionReport w = (PositionReport) list.get(k);

				if (w.getReisID().intValue() == reisID.intValue()) {
					return w;
				}
			}
		}
		return null;
	}

	public ZichtmeterInfo getZichtmeter(Integer id) {
		if (list != null) {
			for (int k = 0; k < listZichtMeter.size(); k++) {
				ZichtmeterInfo w = (ZichtmeterInfo) listZichtMeter.get(k);
				if (w.getId() == id) {
					return w;
				}
			}
		}
		return null;
	}

	public String showListOfJourneys() {

		String a = "";
		for (int k = 0; k < list.size(); k++) {
			PositionReport w = (PositionReport) list.get(k);
			a = a + " " + w.getReisID();
		}
		return a.trim();
	}

	public void deletePositionReport(Integer reisID) {
		if (list.size() > 0) {
			Iterator<Object> i = list.iterator();
			while (i.hasNext()) {
				PositionReport o = (PositionReport) i.next();
				if (o.getReisID().intValue() == reisID.intValue()) {
					i.remove();
					System.out.println("Removed " + reisID);
				}
			}
		}
	}

	public boolean storePositionReports(String filename) {

		try {
			writeObject(list, filename);
		} catch (Exception e) {
			return false;
		}
		return true;

	}

	public boolean loadPositionReports(String filename) {

		try {
			list = readObject(filename);
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
		return true;

	}

	public boolean storeZichtMeters(String filename) {

		try {
			writeObject(listZichtMeter, filename);
		} catch (Exception e) {
			return false;
		}
		return true;

	}

	public boolean loadZichtMeters(String filename) {

		try {
			listZichtMeter = readObjectZM(filename);
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
		return true;

	}

	public int createListOfSchips() {
		Random randomGenerator = new Random();
		CoordTrans ct;
		ct = CoordTrans.getInstance();
		int randomInt = randomGenerator.nextInt(30) + 5;

		for (int idx = 1; idx <= randomInt; ++idx) {

			PositionReport t = new PositionReport();

			t.setScheepsNaam("Vessel " + idx);
			t.setScheepsLabel("v " + idx);

			boolean valid = false;

			int r = randomGenerator.nextInt(63998 * 2 - 63998);
			while (!valid) {

				int x = randomGenerator.nextInt(63998) - 32000;
				t.setXPos(x);
				int y = randomGenerator.nextInt(63998) - 32000;
				t.setYPos(y);

				if (ct.isInWesterscheldt(x, y))
					valid = true;
			}

			r = randomGenerator.nextInt(200);
			t.setScheepsLengte(r);

			r = randomGenerator.nextInt(20);
			t.setScheepsDiepgang(r);

			r = randomGenerator.nextInt(40);
			t.setScheepsBreedte(r);

			r = randomGenerator.nextInt(360);
			t.setDirection(r);

			r = randomGenerator.nextInt(90);
			t.setSpeed(r + 1);
			t.setReisID(idx);
			r = randomGenerator.nextInt(10);
			if (r > 5) {
				t.setVraagBijkomendeInfo(false);
			} else {
				t.setVraagBijkomendeInfo(true);
			}
			list.add(t);

		}
		return randomInt;
	}

	public void createListOfZichtMeters() {
		Random randomGenerator = new Random();

		int randomInt = randomGenerator.nextInt(30) + 5;

		for (int idx = 1; idx <= 2; idx++) {

			ZichtmeterInfo t = new ZichtmeterInfo();

			if (idx == 1)
				t.setId("Kaapduinen");
			if (idx == 2)
				t.setId("Bath");

			int r = randomGenerator.nextInt(10);
			if (r > 5) {
				t.setZelftest(true);
			} else {
				t.setZelftest(false);
			}

			r = randomGenerator.nextInt(10);
			if (r > 5) {
				t.setComstat(true);
			} else {
				t.setComstat(false);
			}

			r = randomGenerator.nextInt(10);
			boolean m1 = (r > 5);

			r = randomGenerator.nextInt(10);
			boolean m2 = (r < 5);

			r = randomGenerator.nextInt(10);
			boolean m3 = (r > 5);

			r = randomGenerator.nextInt(10);
			boolean m4 = (r < 5);
			t.setMistStatus(m1, m2, m3, m4);

			r = randomGenerator.nextInt(10);
			m4 = (r > 5);
			t.setStatus(m4);

			r = randomGenerator.nextInt(10);
			m4 = (r > 5);
			t.setComstat(m4);

			r = randomGenerator.nextInt(4);
			t.setZicht(r);

			listZichtMeter.add(t);
		}

	}

	public static void main(String[] args) {
		Services s = new Services();
		s.createListOfSchips();
		s.createListOfZichtMeters();
		String filename = "c://theo//list.ser";
		if (s.storePositionReports(filename)) {
			System.out.println("Save succesful");
		} else {
			System.out.println("Save failed");
		}

		System.out.println(list.size());
		s.deletePositionReport(2);
		System.out.println(list.size());

		s.loadPositionReports(filename);
		System.out.println(list.size());

	}

}
