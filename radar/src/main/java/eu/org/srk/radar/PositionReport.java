/**
 * @author Theo den Exter, ARS
 * Date: May 21th 2016
 * Version 1.0
 *
 * Position Report
 *
 * History
 *
 */
package eu.org.srk.radar;

import java.io.*;

class PositionReport implements Serializable {

	Integer reisID;
	Integer xPos;
	Integer yPos;
	boolean vraagBijkomendeInfo;
	byte dpID; // Desktopplek
	byte videoDisplayMode; // 0 = normale video mode, 1 = speciale video mode
	String scheepsLabel; // 4 char, ASCII karakters 20 (hex) - 7E (hex)
	String scheepsNaam; // 16 char, ASCII karakters 20 (hex) - 7E (hex)
	int scheepsLengte; // 0 en 500 m
	Integer scheepsBreedte; // 0 en 500 m
	Integer scheepsDiepgang; // 0-30 meter in 0.1 eenheid
	Byte scheepsKenmerk; // Zasil
	Long mmsi; // 0 <= MMSI <= 4294967295
	Long imo; // 0 <= MMSI <= 4294967295
	Long europaNummer; // 0 <= MMSI <= 4294967295
	String centrale; // Zeebrugge Vlissingen Terneuzen Zandvliet Hansweert
	Integer direction;
	Integer speed;

	PositionReport() {
		this.reisID = 0;
		this.xPos = 0;
		this.yPos = 0;
		this.centrale = "Vlissingen";

		this.dpID = 0;

		this.videoDisplayMode = 0;

		this.scheepsLabel = "onbe";
		this.scheepsNaam = "onbekend";

		this.scheepsDiepgang = 0;
		this.scheepsKenmerk = 0;
		this.mmsi = 0L;
		this.imo = 0L;

		this.speed = 3;
		this.direction = 45;

	}

	public void setReisID(Integer val) {
		this.reisID = val;

	}

	Integer getReisID() {
		return this.reisID;

	}

	public void setXPos(int val) {
		this.xPos = val;

	}

	public Integer getXPos() {
		return this.xPos;

	}

	public void setYPos(int val) {
		this.yPos = val;

	}

	public Integer getYPos() {
		return this.yPos;

	}

	public void setVraagBijkomendeInfo(boolean val) {
		this.vraagBijkomendeInfo = val;
	}

	public Integer getVraagBijkomendeInfo() {
		if (this.vraagBijkomendeInfo) {
			return 1;

		} else {
			return 0;
		}

	}

	public void setDpID(byte val) {
		this.dpID = val;
	}

	public byte getDpID() {
		return this.dpID;
	}

	public void setVideoDisplayMode(byte val) {
		this.videoDisplayMode = val;
	}

	public byte getVideoDisplayMode() {
		return this.videoDisplayMode;
	}

	public void setScheepsLabel(String val) {
		this.scheepsLabel = val;
	}

	public String getScheepsLabel() {
		return this.scheepsLabel;
	}

	public void setScheepsNaam(String val) {
		this.scheepsNaam = val;
	}

	public String getScheepsNaam() {
		return this.scheepsNaam;
	}

	public void setScheepsLengte(int val) {
		this.scheepsLengte = val;
	}

	public void setScheepsBreedte(Integer val) {
		this.scheepsBreedte = val;
	}

	public Integer getScheepsBreedte() {
		return this.scheepsBreedte;
	}

	public Integer getScheepsLengte() {
		return this.scheepsLengte;
	}

	public void setScheepsDiepgang(Integer val) {
		this.scheepsDiepgang = val;
	}

	public Integer getScheepsDiepgang() {
		return this.scheepsDiepgang;
	}

	public void setScheepsKenmerk(byte val) {
		this.scheepsKenmerk = val;
	}

	public byte getScheepsKenmerk() {
		return this.scheepsKenmerk;
	}

	public void setScheepsMMSI(Long val) {
		this.mmsi = val;
	}

	public void setScheepsIMO(Long val) {
		this.imo = val;
	}

	public void setEuropaNummer(Long val) {
		this.europaNummer = val;
	}

	public void setCentrale(String val) {
		this.centrale = val;
	}

	public Integer getDirection() {
		return this.direction;
	}

	public void setDirection(Integer val) {
		this.direction = val;
	}

	public void setSpeed(Integer val) {
		this.speed = val;
	}

	public Integer getSpeed() {
		return this.speed;
	}

	public void calculateMove(int seconds) {
		Double s = this.speed * 1.85200 * 1000 / 60.0 / 60.0; // Meters per

		CoordTrans ct;
		ct = CoordTrans.getInstance();

		// seconds
		boolean again = true;
		int count = 0;
		while (again && (count < 9)) {
			count++;
			Double p = this.direction / 180.0 * Math.PI;
			Integer x = this.xPos + (int) Math.round(Math.sin(p) * s * seconds);
			Integer y = this.yPos + (int) Math.round(Math.cos(p) * s * seconds);

			if (!ct.isInWesterscheldt(x, y)) {
				this.direction = (this.direction + 45);
				if (this.direction > 360)
					this.direction = this.direction - 360;
			} else {
				again = false;
				this.xPos = x;
				this.yPos = y;
			}
		}

	}

}
