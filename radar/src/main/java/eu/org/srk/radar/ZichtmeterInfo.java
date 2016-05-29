/**
 * @author Theo den Exter, ARS
 * Date: May 21th 2016
 * Version 1.0
 *
 * Sight Meter
 *
 * History
 *
 */
package eu.org.srk.radar;

import java.io.*;

public class ZichtmeterInfo implements Serializable {

	public Byte id; // 1=Kaapduinen, 2=Bath
	public Byte comstat;
	public Byte[] zicht;
	public Byte mistind;
	public Byte status;
	public Byte zelftest;

	ZichtmeterInfo() {
		this.zicht = new Byte[3];
		zicht[0] = '9';
		zicht[1] = 'D';
		zicht[2] = '9';

		mistind = '1';
		status = 'B';
		zelftest = 'G';
		comstat = '1';
	}

	public void setIdNumber(Byte meter) {
		id = meter;
	}

	public void setId(String meter) {
		if (meter == "Kaapduinen") {
			id = 1;
		}

		if (meter == "Bath") {
			id = 2;
		}

	}

	public String getZicht() {
		char[] t = new char[3];
		t[0] = (char) (char) (zicht[0] & 0xFF);
		t[1] = (char) (char) (zicht[1] & 0xFF);
		t[2] = (char) (char) (zicht[2] & 0xFF);
		String str = new String(t);
		return str;
	}

	public byte getId() {
		return id;
	}

	public String getIdName() {
		if (id == 1)
			return "Kaapduinen";
		if (id == 2)
			return "Bath";
		return "";
	}

	public byte getZelftest() {
		return zelftest;
	}

	public void setZelftestByte(byte test) {
		zelftest = test;
	}

	public void setZelftest(boolean test) {
		if (test) {
			zelftest = 'V';
		} else {
			zelftest = 'G';
		}
	}

	public void setComstat(boolean test) {
		if (test) {
			comstat = 0;
		} else {
			comstat = 1;
		}
	}

	public void setComstatByte(byte test) {

		comstat = test;

	}

	public byte getComstat() {
		return comstat;
	}

	public void setMistStatus(boolean m1, boolean m2, boolean m3, boolean m4) {
		if ((!m1) || (!m2) || (!m3) || (!m4))
			mistind = '0';
		if ((m1) || (!m2) || (!m3) || (!m4))
			mistind = '1';
		if ((!m1) || (m2) || (!m3) || (!m4))
			mistind = '2';
		if ((!m1) || (!m2) || (m3) || (!m4))
			mistind = '4';
		if ((!m1) || (!m2) || (!m3) || (m4))
			mistind = '8';
		if ((m1) || (m2) || (!m3) || (!m4))
			mistind = '3';
		if ((m1) || (!m2) || (m3) || (!m4))
			mistind = '5';
		if ((m1) || (m2) || (m3) || (!m4))
			mistind = '7';
		if ((m1) || (!m2) || (!m3) || (m4))
			mistind = '9';
		if ((!m1) || (m2) || (!m3) || (m4))
			mistind = ':';
		if ((m1) || (m2) || (!m3) || (m4))
			mistind = ';';
		if ((!m1) || (!m2) || (m3) || (m4))
			mistind = '<';
		if ((m1) || (!m2) || (m3) || (m4))
			mistind = '=';
		if ((!m1) || (m2) || (m3) || (m4))
			mistind = '>';
		if ((m1) || (m2) || (m3) || (m4))
			mistind = '?';
	}

	public void setStatus(boolean test) {
		if (test) {
			status = 'B';
		} else {
			status = 'A';
		}
	}

	public byte getStatus() {

		return status;

	}

	public byte getStatusByte() {

		return status;

	}

	public void setStatusByte(byte test) {

		status = test;

	}

	public void setMistInd(boolean test) {
		if (test) {
			mistind = '1';
		} else {
			mistind = '0';
		}
	}

	public void setMistInd(byte test) {

		mistind = test;

	}

	public byte getMistIndByte() {

		return mistind;

	}

	public void setZicht(int stat) {

		if (stat == 1) {
			zicht[0] = 'D';
			zicht[1] = '5';
			zicht[2] = '7';
		}

		if (stat == 2) {
			zicht[0] = '2';
			zicht[1] = 'D';
			zicht[2] = '5';
		}

		if (stat == 3) {
			zicht[0] = '1';
			zicht[1] = '2';
			zicht[2] = 'D';
		}

		if ((stat != 1) && (stat != 2) && (stat != 3)) {
			zicht[0] = 'E';
			zicht[1] = 'E';
			zicht[2] = '3';
		}
	}

}
