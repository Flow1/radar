/**
 * @author Theo den Exter, ARS
 * Date: May 21th 2016
 * Version 1.0
 *
 * Radar simulator main class
 * Needs implementation of triggering a message to send "Journey Selected"
 *
 * History
 *
 */
package eu.org.srk.radar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import java.awt.geom.Ellipse2D;

import javax.swing.*;

import java.awt.*;

import javax.swing.JPanel;

import eu.org.srk.radar.Binary;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;

public class Radar {

	int xw = 600;
	int yw = 700;

	int xwradar = 600;
	int ywradar = 700;

	JFrame frame;
	JPanel panel;
	JPanel panel1;
	JPanel panel2;
	JPanel panel3;
	JPanel panel4;
	JPanel panel70;
	JPanel currentPanel;
	Timer timer;

	JTextField travelId;
	JTextField travelId2;
	JTextField travelId3;
	JTextField travelId70;
	JTextField DPId;
	JTextField DPId70;
	JTextField shipLabel;
	JTextField shipName;
	JTextField shipDepth;
	JTextField shipWidth;
	JTextField shipLength;
	JTextField XPos;
	JTextField YPos;
	JTextField vaarHoek;
	JTextField shipSpeed;
	JCheckBox VideoMode;
	JCheckBox VBInfo;
	JCheckBox zasilZ;
	JCheckBox zasilA;
	JCheckBox zasilS;
	JCheckBox zasilI;
	JCheckBox zasilL;

	JTextField zichtmeterId;
	JTextField CommStatus;
	JTextField zichtMeter;
	JTextField zelfTest;
	JTextField Status;
	JTextField MistIndicator;
	JTextField MistWaarden;

	ActionListener actionListener;

	Polygon poly;
	CoordTrans CT;

	boolean create = false;

	String filename;
	String filenamezm;
	Services service = new Services();
	static StopButton ButtonB = new StopButton();

	private void createAndShowGUI() {
		// Create and set up the window.
		frame = new JFrame("RW Radar Simulator");

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create the menu bar.
		// Make it have a green background.
		JMenuBar greenMenuBar = new JMenuBar();
		greenMenuBar.setOpaque(true);
		greenMenuBar.setBackground(new Color(154, 165, 127));
		greenMenuBar.setPreferredSize(new Dimension(200, 20));

		// #####################################################################################################################
		// Edit/Show Journey
		// #####################################################################################################################

		panel = new JPanel();
		panel.setPreferredSize(new Dimension(xw, yw));
		panel.setOpaque(true);
		panel.setBackground(new Color(248, 213, 131));
		panel.setLayout(null);

		JLabel jLabel0 = new JLabel("Travel ID");
		jLabel0.setBackground(new Color(248, 213, 131));
		jLabel0.setLocation(5, 5);
		jLabel0.setSize(100, 20);
		travelId = new JTextField(3);
		travelId.setLocation(300, 5);
		travelId.setSize(100, 20);
		travelId.setBackground(Color.lightGray);
		travelId.addActionListener(menuActionListener);
		travelId.setEnabled(true);
		travelId.setText("2");
		travelId.setActionCommand("Travel ID");

		JLabel jLabel1 = new JLabel("DP ID (0-255)", JLabel.LEFT);
		jLabel1.setLocation(5, 30);
		jLabel1.setSize(100, 20);
		jLabel1.setBackground(new Color(248, 213, 131));
		DPId = new JTextField(3);
		DPId.setBackground(new Color(248, 213, 131));
		DPId.setEnabled(true);
		DPId.setText("2");
		DPId.setDocument(new JTextFieldLimit(3));
		DPId.setLocation(300, 30);
		DPId.setSize(30, 20);

		JLabel jLabel2 = new JLabel("Ship label (16 chars)", JLabel.LEFT);
		jLabel2.setBackground(new Color(248, 213, 131));
		shipLabel = new JTextField("BEER");
		shipLabel.setBackground(new Color(248, 213, 131));
		shipLabel.setLocation(300, 60);
		shipLabel.setSize(200, 20);
		shipLabel.setDocument(new JTextFieldLimit(16));
		jLabel2.setLocation(5, 60);
		jLabel2.setSize(200, 20);

		JLabel jLabel3 = new JLabel("Ship name (48 chars)", JLabel.LEFT);
		jLabel3.setBackground(new Color(248, 213, 131));
		shipName = new JTextField("Beerenboot");
		shipName.setBackground(new Color(248, 213, 131));
		shipName.setLocation(300, 90);
		shipName.setSize(300, 20);
		shipName.setDocument(new JTextFieldLimit(48));
		jLabel3.setLocation(5, 90);
		jLabel3.setSize(200, 20);

		JLabel jLabel4 = new JLabel("Ship depth (0-30 m)", JLabel.LEFT);
		jLabel4.setBackground(new Color(248, 213, 131));
		shipDepth = new JTextField("20");
		shipDepth.setBackground(new Color(248, 213, 131));
		shipDepth.setVisible(true);
		shipDepth.setLocation(300, 120);
		shipDepth.setSize(100, 20);
		jLabel4.setLocation(5, 120);
		jLabel4.setSize(200, 20);

		JLabel jLabel16 = new JLabel("Ship width (0-30 m)", JLabel.LEFT);
		jLabel16.setBackground(new Color(248, 213, 131));
		shipWidth = new JTextField("20");
		shipWidth.setBackground(new Color(248, 213, 131));
		shipWidth.setVisible(true);
		shipWidth.setLocation(300, 150);
		shipWidth.setSize(100, 20);
		jLabel16.setLocation(5, 150);
		jLabel16.setSize(200, 20);

		JLabel jLabel40 = new JLabel("Ship length (0-500m)", JLabel.LEFT);
		jLabel40.setBackground(new Color(248, 213, 131));
		shipLength = new JTextField("20");
		shipLength.setBackground(new Color(248, 213, 131));
		shipLength.setVisible(true);
		shipLength.setLocation(300, 180);
		shipLength.setSize(100, 20);
		jLabel40.setLocation(5, 180);
		jLabel40.setSize(200, 20);

		JLabel jLabel5 = new JLabel("XPos (m relative to Ellertswoud)",
				JLabel.LEFT);
		jLabel5.setBackground(new Color(248, 213, 131));
		XPos = new JTextField("20");
		XPos.setBackground(new Color(248, 213, 131));
		XPos.setVisible(true);
		XPos.setLocation(300, 210);
		XPos.setSize(100, 20);
		jLabel5.setLocation(5, 210);
		jLabel5.setSize(200, 20);

		JLabel jLabel6 = new JLabel("YPos (m relative to Ellertswoud)",
				JLabel.LEFT);
		jLabel6.setBackground(new Color(248, 213, 131));
		YPos = new JTextField("20");
		YPos.setBackground(new Color(248, 213, 131));
		YPos.setVisible(true);
		YPos.setLocation(300, 240);
		YPos.setSize(100, 20);
		jLabel6.setLocation(5, 240);
		jLabel6.setSize(200, 20);

		JLabel jLabel7 = new JLabel("Direction (0-360)", JLabel.LEFT);
		jLabel7.setBackground(new Color(248, 213, 131));
		vaarHoek = new JTextField("20");
		vaarHoek.setBackground(new Color(248, 213, 131));
		vaarHoek.setVisible(true);
		vaarHoek.setEnabled(true);
		vaarHoek.setLocation(300, 270);
		vaarHoek.setSize(100, 20);
		jLabel7.setLocation(5, 270);
		jLabel7.setSize(200, 20);

		JLabel jLabel8 = new JLabel("Speed (knots)", JLabel.LEFT);
		jLabel8.setBackground(new Color(248, 213, 131));
		shipSpeed = new JTextField("20");
		shipSpeed.setBackground(new Color(248, 213, 131));
		shipSpeed.setVisible(true);
		shipSpeed.setEnabled(true);
		shipSpeed.setLocation(300, 300);
		shipSpeed.setSize(100, 20);
		jLabel8.setLocation(5, 300);
		jLabel8.setSize(100, 20);

		JLabel jLabel9 = new JLabel("Video mode", JLabel.LEFT);
		jLabel9.setBackground(new Color(248, 213, 131));
		VideoMode = new JCheckBox("Video mode");
		VideoMode.setSelected(true);
		VideoMode.setBackground(new Color(248, 213, 131));
		VideoMode.setActionCommand("Video mode");
		VideoMode.setLocation(300, 330);
		VideoMode.setSize(100, 20);
		jLabel9.setLocation(5, 330);
		jLabel9.setSize(100, 20);

		JLabel jLabel10 = new JLabel("Request for additional information",
				JLabel.LEFT);
		jLabel10.setBackground(new Color(248, 213, 131));
		VBInfo = new JCheckBox("Vraag bijkomende informatie");
		VBInfo.setSelected(true);
		VBInfo.setBackground(new Color(248, 213, 131));
		VBInfo.setActionCommand("Vraag bijkomende informatie");
		VBInfo.setLocation(300, 360);
		VBInfo.setSize(200, 20);
		jLabel10.setLocation(5, 360);
		jLabel10.setSize(200, 20);

		zasilZ = new JCheckBox("Z");
		JLabel jLabel11 = new JLabel("Sea Vessel/No Sea Vessel", JLabel.LEFT);
		jLabel11.setBackground(new Color(248, 213, 131));
		zasilZ.setSelected(true);
		zasilZ.setBackground(new Color(248, 213, 131));
		zasilZ.setActionCommand("ZaSIL Z");
		zasilZ.setLocation(300, 390);
		zasilZ.setSize(100, 20);
		jLabel11.setLocation(5, 390);
		jLabel11.setSize(200, 20);

		zasilA = new JCheckBox("A");
		JLabel jLabel12 = new JLabel("Anchored/Not Anchored", JLabel.LEFT);
		jLabel11.setBackground(new Color(248, 213, 131));
		zasilA.setSelected(true);
		zasilA.setBackground(new Color(248, 213, 131));
		zasilA.setActionCommand("ZaSIL A");
		zasilA.setLocation(300, 420);
		zasilA.setSize(200, 20);
		jLabel12.setLocation(5, 420);
		jLabel12.setSize(200, 20);

		zasilS = new JCheckBox("S");
		JLabel jLabel13 = new JLabel("Special/No Special Transport",
				JLabel.LEFT);
		jLabel13.setBackground(new Color(248, 213, 131));
		zasilS.setSelected(true);
		zasilS.setBackground(new Color(248, 213, 131));
		zasilS.setActionCommand("ZaSIL S");
		zasilS.setLocation(300, 450);
		zasilS.setSize(200, 20);
		jLabel13.setLocation(5, 450);
		jLabel13.setSize(200, 20);

		zasilI = new JCheckBox("I");
		JLabel jLabel14 = new JLabel("IMO/No IMO", JLabel.LEFT);
		jLabel14.setBackground(new Color(248, 213, 131));
		zasilI.setSelected(true);
		zasilI.setBackground(new Color(248, 213, 131));
		zasilI.setActionCommand("ZaSIL I");
		zasilI.setLocation(300, 480);
		zasilI.setSize(100, 20);
		jLabel14.setLocation(5, 480);
		jLabel14.setSize(200, 20);

		zasilL = new JCheckBox("L");
		JLabel jLabel15 = new JLabel("Pilot/No Pilot on board", JLabel.LEFT);
		jLabel15.setBackground(new Color(248, 213, 131));
		zasilL.setSelected(true);
		zasilL.setBackground(new Color(248, 213, 131));
		zasilL.setActionCommand("ZaSIL L");
		zasilL.setLocation(300, 510);
		zasilL.setSize(100, 20);
		jLabel15.setLocation(5, 510);
		jLabel15.setSize(200, 20);

		panel.add(jLabel0);
		panel.add(travelId);

		panel.add(jLabel1);
		panel.add(DPId);

		panel.add(jLabel2);
		panel.add(shipLabel);

		panel.add(jLabel3);
		panel.add(shipName);

		panel.add(jLabel4);
		panel.add(shipDepth);

		panel.add(jLabel16);
		panel.add(shipWidth);

		panel.add(jLabel40);
		panel.add(shipLength);

		panel.add(jLabel5);
		panel.add(XPos);

		panel.add(jLabel6);
		panel.add(YPos);

		panel.add(jLabel7);
		panel.add(vaarHoek);

		panel.add(jLabel8);
		panel.add(shipSpeed);

		panel.add(jLabel9);
		panel.add(VideoMode);

		panel.add(jLabel10);
		panel.add(VBInfo);

		panel.add(jLabel11);
		panel.add(zasilZ);

		panel.add(jLabel12);
		panel.add(zasilA);

		panel.add(jLabel13);
		panel.add(zasilS);

		panel.add(jLabel14);
		panel.add(zasilI);

		panel.add(jLabel15);
		panel.add(zasilL);

		JButton jbnLeft = new JButton("Save/Create");
		jbnLeft.setActionCommand("Save Journey");
		JButton jbnRight = new JButton("Cancel");
		jbnRight.setActionCommand("Cancel Entry");

		jbnLeft.setSize(120, 30);
		jbnRight.setSize(120, 30);

		jbnLeft.addActionListener(menuActionListener);
		jbnRight.addActionListener(menuActionListener);
		jbnRight.setVisible(true);
		jbnLeft.setVisible(true);

		jbnLeft.setLocation(100, 550);
		jbnRight.setLocation(300, 550);

		panel.add(jbnLeft);
		panel.add(jbnRight);

		// #####################################################################################################################
		// Show list of Journeys
		// #####################################################################################################################

		panel1 = new JPanel();
		panel1.setPreferredSize(new Dimension(600, 700));
		panel1.setOpaque(true);
		panel1.setBackground(new Color(248, 213, 131));
		panel1.setLayout(null);

		JLabel jLabel30 = new JLabel("List of Travel ID", JLabel.LEFT);
		jLabel30.setBackground(new Color(248, 213, 131));
		travelId2 = new JTextField(300);
		travelId2.setBackground(Color.lightGray);
		travelId2.setEnabled(true);

		jLabel30.setLocation(5, 30);
		jLabel30.setSize(100, 20);
		travelId2.setLocation(150, 30);
		travelId2.setSize(500, 90);

		panel1.add(jLabel30);
		panel1.add(travelId2);

		// #####################################################################################################################
		// Remove Journey
		// #####################################################################################################################

		panel3 = new JPanel();
		panel3.setPreferredSize(new Dimension(600, 700));
		panel3.setOpaque(true);
		panel3.setBackground(new Color(248, 213, 131));
		panel3.setLayout(null);

		JLabel jLabel50 = new JLabel("Travel ID");
		jLabel50.setBackground(new Color(248, 213, 131));
		jLabel50.setLocation(5, 5);
		jLabel50.setSize(100, 20);
		travelId3 = new JTextField(3);
		travelId3.setLocation(300, 5);
		travelId3.setSize(100, 20);
		travelId3.setBackground(Color.lightGray);
		travelId3.addActionListener(menuActionListener);
		travelId3.setEnabled(true);
		travelId3.setText("2");
		travelId3.setActionCommand("Travel IDR");

		panel3.add(jLabel50);
		panel3.add(travelId3);

		JButton jbnLeft3 = new JButton("Remove");
		jbnLeft3.setActionCommand("Remove");
		JButton jbnRight3 = new JButton("Cancel");

		jbnLeft3.setSize(120, 30);
		jbnRight3.setSize(120, 30);

		jbnLeft3.addActionListener(menuActionListener);
		jbnRight3.addActionListener(menuActionListener);
		jbnRight3.setVisible(true);
		jbnLeft3.setVisible(true);

		jbnLeft3.setLocation(100, 550);
		jbnRight3.setLocation(300, 550);

		panel3.add(jbnLeft3);
		panel3.add(jbnRight3);

		// #####################################################################################################################
		// Send Journey Selected
		// #####################################################################################################################

		panel70 = new JPanel();
		panel70.setPreferredSize(new Dimension(600, 700));
		panel70.setOpaque(true);
		panel70.setBackground(new Color(248, 213, 131));
		panel70.setLayout(null);

		JLabel jLabel70 = new JLabel("Travel ID");
		jLabel70.setBackground(new Color(248, 213, 131));
		jLabel70.setLocation(5, 5);
		jLabel70.setSize(100, 20);
		travelId70 = new JTextField(3);
		travelId70.setLocation(300, 5);
		travelId70.setSize(100, 20);
		travelId70.setBackground(Color.lightGray);
		travelId70.setEnabled(true);
		travelId70.setText("2");

		JLabel jLabel71 = new JLabel("DP ID (0-255)", JLabel.LEFT);
		jLabel71.setLocation(5, 30);
		jLabel71.setSize(100, 20);
		jLabel71.setBackground(new Color(248, 213, 131));
		DPId70 = new JTextField(3);
		DPId70.setBackground(new Color(248, 213, 131));
		DPId70.setEnabled(true);
		DPId70.setText("2");
		DPId70.setDocument(new JTextFieldLimit(3));
		DPId70.setLocation(300, 30);
		DPId70.setSize(30, 20);

		panel70.add(jLabel70);
		panel70.add(travelId70);
		panel70.add(jLabel71);
		panel70.add(DPId70);

		JButton jbnLeft70 = new JButton("Send");
		jbnLeft70.setActionCommand("SendJourneySelect");

		jbnLeft70.setSize(120, 30);

		jbnLeft70.addActionListener(menuActionListener);
		jbnLeft70.setVisible(true);

		jbnLeft70.setLocation(100, 550);

		panel70.add(jbnLeft70);

		// #####################################################################################################################
		// Edit/Show Sightmeter
		// #####################################################################################################################

		panel2 = new JPanel();
		panel2.setPreferredSize(new Dimension(600, 700));
		panel2.setOpaque(true);
		panel2.setBackground(new Color(248, 213, 131));
		panel2.setLayout(null);

		JLabel jLabel20 = new JLabel("Sightmeter ID (1-2)", JLabel.LEFT);
		jLabel20.setBackground(new Color(248, 213, 131));
		zichtmeterId = new JTextField(3);
		zichtmeterId.addActionListener(menuActionListener);
		zichtmeterId.setEnabled(true);
		zichtmeterId.setText("2");
		zichtmeterId.setBackground(Color.lightGray);
		zichtmeterId.setActionCommand("Zichtmeter ID");
		zichtmeterId.setDocument(new JTextFieldLimit(1));

		zichtmeterId.setLocation(300, 30);
		zichtmeterId.setSize(100, 20);
		jLabel20.setLocation(5, 30);
		jLabel20.setSize(100, 20);

		JLabel jLabel21 = new JLabel("Comm status (0, <>0)", JLabel.LEFT);
		jLabel2.setBackground(new Color(248, 213, 131));
		CommStatus = new JTextField(3);
		CommStatus.setEnabled(true);
		CommStatus.setText("2");
		CommStatus.setBackground(new Color(248, 213, 131));
		CommStatus.setDocument(new JTextFieldLimit(1));

		CommStatus.setLocation(300, 60);
		CommStatus.setSize(100, 20);
		jLabel21.setLocation(5, 60);
		jLabel21.setSize(100, 20);

		JLabel jLabel22 = new JLabel("Sightmeter (name)", JLabel.LEFT);
		jLabel22.setBackground(new Color(248, 213, 131));
		zichtMeter = new JTextField("BEER");
		zichtMeter.setBackground(new Color(248, 213, 131));
		zichtMeter.setActionCommand("Zichtmeter");

		zichtMeter.setLocation(300, 90);
		zichtMeter.setSize(100, 20);
		jLabel22.setLocation(5, 90);
		jLabel22.setSize(100, 20);

		JLabel jLabel23 = new JLabel("Selftest (G,V)", JLabel.LEFT);
		jLabel23.setBackground(new Color(248, 213, 131));
		zelfTest = new JTextField("Beerenboot");
		zelfTest.setBackground(new Color(248, 213, 131));
		zelfTest.setActionCommand("Zelf test");

		zelfTest.setDocument(new JTextFieldLimit(1));
		zelfTest.setLocation(300, 120);
		zelfTest.setSize(100, 20);
		jLabel23.setLocation(5, 120);
		jLabel23.setSize(100, 20);

		JLabel jLabel24 = new JLabel("Status (A,B)", JLabel.LEFT);
		jLabel24.setBackground(new Color(248, 213, 131));
		Status = new JTextField("20");
		Status.setBackground(new Color(248, 213, 131));
		Status.setActionCommand("Status");
		Status.setDocument(new JTextFieldLimit(1));
		Status.setVisible(true);

		Status.setLocation(300, 150);
		Status.setSize(100, 20);
		jLabel24.setLocation(5, 150);
		jLabel24.setSize(100, 20);

		JLabel jLabel25 = new JLabel("Fog Indicator", JLabel.LEFT);
		jLabel25.setBackground(new Color(248, 213, 131));
		MistIndicator = new JTextField("20");
		MistIndicator.setBackground(new Color(248, 213, 131));
		MistIndicator.setDocument(new JTextFieldLimit(1));
		MistIndicator.setVisible(true);

		MistIndicator.setLocation(300, 180);
		MistIndicator.setSize(100, 20);
		jLabel25.setLocation(5, 180);
		jLabel25.setSize(100, 20);

		JLabel jLabel26 = new JLabel("Sightmeter value (km)", JLabel.LEFT);
		jLabel26.setBackground(new Color(248, 213, 131));
		MistWaarden = new JTextField("20");
		MistWaarden.setBackground(new Color(248, 213, 131));
		MistWaarden.setDocument(new JTextFieldLimit(3));
		MistWaarden.setVisible(true);

		MistWaarden.setLocation(300, 210);
		MistWaarden.setSize(200, 30);
		jLabel26.setLocation(5, 210);
		jLabel26.setSize(100, 20);

		panel2.add(jLabel20);
		panel2.add(zichtmeterId);

		panel2.add(jLabel21);
		panel2.add(CommStatus);

		panel2.add(jLabel22);
		panel2.add(zichtMeter);

		panel2.add(jLabel23);
		panel2.add(zelfTest);

		panel2.add(jLabel24);
		panel2.add(Status);

		panel2.add(jLabel25);
		panel2.add(MistIndicator);

		panel2.add(jLabel26);
		panel2.add(MistWaarden);

		JButton jbnLeft2 = new JButton("Save");
		jbnLeft2.setActionCommand("Save2");
		JButton jbnRight2 = new JButton("Cancel");

		jbnLeft2.setSize(120, 30);
		jbnRight2.setSize(120, 30);

		jbnLeft2.addActionListener(menuActionListener);
		jbnRight2.addActionListener(menuActionListener);
		jbnRight2.setVisible(true);
		jbnLeft2.setVisible(true);

		jbnLeft2.setLocation(100, 550);
		jbnRight2.setLocation(300, 550);

		panel2.add(jbnLeft2);
		panel2.add(jbnRight2);

		// #####################################################################################################################
		// Show Radar
		// #####################################################################################################################

		int dimx = 1000;
		int dimy = (int) ((dimx * 1.0) / 1.6);
		xwradar = dimx;
		ywradar = dimy;

		int offsetx = 100;
		int offsety = 100;
		int xposm = 0;
		int yposm = 0;
		int xposm2 = 1000000;
		int yposm2 = 1000000;
		int count = 0;

		CT = CoordTrans.getInstance();

		try {
			BufferedReader br = null;
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					"the shape.csv")));
			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				String xpos = sCurrentLine.substring(0,
						sCurrentLine.indexOf(';'));
				String ypos = sCurrentLine
						.substring(sCurrentLine.indexOf(';') + 1);

				int xposn = Integer.parseInt(xpos);
				int yposn = Integer.parseInt(ypos);
				count++;

				if (xposn > xposm)
					xposm = xposn;
				if (yposn > yposm)
					yposm = yposn;
				if (xposn < xposm2)
					xposm2 = xposn;
				if (yposn < yposm2)
					yposm2 = yposn;
			}
			br.close();
		} catch (Exception e) {
			;
		}

		int xPoly[] = new int[count];
		int yPoly[] = new int[count];

		try {
			BufferedReader br = null;
			String sCurrentLine;
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					"the shape.csv")));
			count = 0;
			while ((sCurrentLine = br.readLine()) != null) {
				String xpos = sCurrentLine.substring(0,
						sCurrentLine.indexOf(';'));
				String ypos = sCurrentLine
						.substring(sCurrentLine.indexOf(';') + 1);

				int xposn = Integer.parseInt(xpos);
				int yposn = Integer.parseInt(ypos);

				xPoly[count] = xposn;
				yPoly[count] = yposn;
				count++;
			}
			br.close();
		} catch (Exception e) {
			;
		}

		for (int i = 0; i < xPoly.length; i++) {
			xPoly[i] = (int) (1.0 * (xPoly[i] - xposm2) / (xposm - xposm2) * (dimx + offsetx))
					- offsetx;
			yPoly[i] = (int) (1.0 * (yposm - yPoly[i]) / (yposm - yposm2) * (dimy + offsety))
					- offsety;
		}

		poly = new Polygon(xPoly, yPoly, xPoly.length);

		CT.setData(dimx, dimy, offsetx, offsety, xposm, yposm, xposm2, yposm2,
				poly);

		// Actual display of Radar Screen
		panel4 = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.setColor(Color.BLACK);
				g.drawPolygon(poly);
				g.setColor(Color.BLUE);
				g.fillPolygon(poly);

				for (int k = 0; k < service.list.size(); k++) {
					PositionReport w = (PositionReport) service.list.get(k);
					Integer x = w.getXPos();
					Integer y = w.getYPos();

					Integer x1 = CT.calculateXPos(x, y);
					Integer y1 = CT.calculateYPos(x, y);
					Graphics2D g2 = (Graphics2D) g;
					Ellipse2D e = new Ellipse2D.Double(x1, y1, 4, 4);
					g2.setColor(Color.red);
					g2.fill(e);

					g2.setColor(Color.yellow);
					String n = Integer.toString(w.getReisID()) + " "
							+ w.getScheepsLabel();
					g2.drawString(n, x1, y1 + 20);
					// JLabel ship = new JLabel(w.getScheepsLabel());
					// ship.setLocation(x1,y1+20);
					// ship.setSize(70,20);

				}
			}
		};

		panel4.setSize(new Dimension(dimx, dimy));

		panel4.setBackground(Color.lightGray);
		panel4.setLayout(null);

		JLabel jLabel60 = new JLabel("Scheldt Radar Chain");
		jLabel60.setBackground(new Color(248, 213, 131));
		jLabel60.setLocation(5, dimy - 30);
		jLabel60.setSize(200, 20);

		JLabel jLabel62 = new JLabel(".");
		jLabel62.setBackground(new Color(248, 213, 131));
		jLabel62.setLocation(422, 322);
		jLabel62.setForeground(Color.RED);
		jLabel62.setSize(200, 20);

		JLabel jLabel61 = new JLabel("Ellertshuizen Virtual Radar");
		jLabel61.setBackground(new Color(248, 213, 131));
		jLabel61.setLocation(390, 293);
		jLabel61.setForeground(Color.BLACK);
		jLabel61.setSize(200, 20);

		JLabel jLabel63 = new JLabel("SRK Vlissingen");
		jLabel63.setBackground(new Color(248, 213, 131));
		jLabel63.setLocation(190, 220);
		jLabel63.setForeground(Color.RED);
		jLabel63.setSize(200, 20);

		panel4.add(jLabel60);
		panel4.add(jLabel61);
		panel4.add(jLabel62);
		panel4.add(jLabel63);

		// Frequent update of radar display
		actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				// System.out.println("click");
				panel4.repaint();
			}
		};

		// Setup update frequency: 5 seconds
		timer = new Timer(5000, actionListener);

		// #####################################################################################################################
		// Activate first screen
		// #####################################################################################################################

		frame.setContentPane(panel);
		currentPanel = panel;

		// #####################################################################################################################
		// Setup menu
		// #####################################################################################################################

		// #####################################################################################################################
		// Menu column
		// #####################################################################################################################

		// Set the menu bar and add the label to the content pane.
		frame.setJMenuBar(greenMenuBar);

		JMenuBar menuBar;
		JMenu menu, submenu;
		JMenuItem menuItem;
		JRadioButtonMenuItem rbMenuItem;
		JCheckBoxMenuItem cbMenuItem;

		// Create the menu bar.
		menuBar = new JMenuBar();

		// #####################################################################################################################
		// First menu column
		// #####################################################################################################################

		// Build the first menu.
		menu = new JMenu("SIM Setup");
		menuBar.add(menu);

		// Journey submenu
		menu.addSeparator();
		submenu = new JMenu("New Journey");

		menuItem = new JMenuItem("New journey: Track ID");
		menuItem.addActionListener(menuActionListener);
		submenu.add(menuItem);

		menuItem = new JMenuItem("New journey: Track ID Extended");
		submenu.add(menuItem);
		menuItem.addActionListener(menuActionListener);
		menu.add(submenu);

		menuItem = new JMenuItem("Send Journey Selected");
		submenu.add(menuItem);
		menuItem.addActionListener(menuActionListener);
		menu.add(submenu);

		menuItem = new JMenuItem("Random create Journeys");
		submenu.add(menuItem);
		menuItem.addActionListener(menuActionListener);
		menu.add(submenu);

		menuItem = new JMenuItem("Random create Sightmeters");
		submenu.add(menuItem);
		menuItem.addActionListener(menuActionListener);
		menu.add(submenu);

		menuItem = new JMenuItem("Clear all Journeys");
		submenu.add(menuItem);
		menuItem.addActionListener(menuActionListener);
		menu.add(submenu);

		menu.add(menuItem);

		// #####################################################################################################################
		// Second menu column
		// #####################################################################################################################

		// Build second menu in the menu bar.
		menu = new JMenu("SIM Ops");
		menuItem = new JMenuItem("Close Journey");
		menuItem.addActionListener(menuActionListener);
		menu.add(menuItem);
		menuItem = new JMenuItem("Load Journeys");
		menuItem.addActionListener(menuActionListener);
		menu.add(menuItem);
		menuItem = new JMenuItem("Store Journeys");
		menuItem.addActionListener(menuActionListener);
		menu.add(menuItem);
		menuItem = new JMenuItem("Load Sightmeters");
		menuItem.addActionListener(menuActionListener);
		menu.add(menuItem);
		menuItem = new JMenuItem("Store Sightmeters");
		menuItem.addActionListener(menuActionListener);
		menu.add(menuItem);
		menuItem = new JMenuItem("Show Radar");
		menuItem.addActionListener(menuActionListener);
		menu.add(menuItem);
		menuBar.add(menu);

		// #####################################################################################################################
		// Third menu column
		// #####################################################################################################################

		// Build second menu in the menu bar.
		menu = new JMenu("SIM Manipulation");
		menuItem = new JMenuItem("View/Adapt Journey");
		menuItem.addActionListener(menuActionListener);
		menu.add(menuItem);
		menuItem = new JMenuItem("View/Adapt Journey Extended");
		menuItem.addActionListener(menuActionListener);
		menu.add(menuItem);
		menuItem = new JMenuItem("View/Adapt Sightmeter");
		menuItem.addActionListener(menuActionListener);
		menu.add(menuItem);
		menuItem = new JMenuItem("Show list of Journeys");
		menuItem.addActionListener(menuActionListener);
		menu.add(menuItem);

		menuItem = new JMenuItem("Stop publishing");
		menuItem.addActionListener(menuActionListener);
		menu.add(menuItem);

		menuItem = new JMenuItem("Start publishing");
		menuItem.addActionListener(menuActionListener);
		menu.add(menuItem);

		menuBar.add(menu);

		// #####################################################################################################################
		// Fourth menu column
		// #####################################################################################################################

		// Build third menu in the menu bar.
		menu = new JMenu("Exit");
		menuItem = new JMenuItem("Exit");
		menuItem.addActionListener(menuActionListener);
		menu.add(menuItem);
		menuBar.add(menu);

		// #####################################################################################################################
		// Activate menu
		// #####################################################################################################################

		frame.setJMenuBar(menuBar);

		// #####################################################################################################################
		// Display the window
		// #####################################################################################################################

		frame.setVisible(true);
		frame.setSize(700, 700);
		service = Services.getInstance();
		service.createListOfZichtMeters();
		Simulator sim = new Simulator();
		sim.start();

	}

	// #####################################################################################################################
	// Action listener
	// #####################################################################################################################

	ActionListener menuActionListener = new ActionListener() {

		public void actionPerformed(ActionEvent e) {

			// Stop radar display update
			timer.stop();

			// Setup/modify size of windows
			if (e.getActionCommand() == "Show Radar") {
				frame.setSize(xwradar, ywradar);
			} else {
				frame.setSize(xw, yw);
			}

			// Actions menu
			if (e.getActionCommand() == "New journey: Track ID") {
				create = true;
				SelectExtended(false);
				SelectPanel(panel);
			}

			if (e.getActionCommand() == "New journey: Track ID Extended") {
				create = true;
				SelectExtended(true);
				SelectPanel(panel);
			}

			if (e.getActionCommand() == "Send Journey Selected") {
				SelectPanel(panel70);
			}

			if (e.getActionCommand() == "View/Adapt Journey") {
				create = false;
				SelectExtended(false);
				SelectPanel(panel);
			}

			if (e.getActionCommand() == "View/Adapt Journey Extended") {
				create = false;
				SelectExtended(true);
				SelectPanel(panel);
			}

			if (e.getActionCommand() == "Random create Journeys") {
				int n = service.createListOfSchips();
			}

			if (e.getActionCommand() == "Random create Sightmeters") {
				service.createListOfZichtMeters();
			}

			if (e.getActionCommand() == "Clear all Journeys") {
				DeleteAllJourney();
			}

			if (e.getActionCommand() == "Close Journey") {
				SelectPanel(panel3);
			}

			if (e.getActionCommand() == "Load Vessels") {
				service.loadPositionReports(filename);
			}

			if (e.getActionCommand() == "SendJourneySelect") {
				Integer rid = Integer.valueOf(travelId70.getText());
				Binary t = new Binary();
				Byte dpid = t.intToByte(Integer.valueOf(DPId70.getText()));
				SaveJourneySelect(rid, dpid);
			}

			if (e.getActionCommand() == "Store Vessels") {
				// service.storePositionReports(filename);
			}

			if (e.getActionCommand() == "Load Sightmeters") {
				// service.loadZichtMeters(filenamezm);
			}

			if (e.getActionCommand() == "Show Radar") {
				SelectPanel(panel4);
				frame.setSize(xwradar, ywradar);
				panel4.repaint();
				timer.start();
			}

			if (e.getActionCommand() == "Store Sightmeters") {
				service.storeZichtMeters(filenamezm);
			}

			if (e.getActionCommand() == "View/Adapt Sightmeter") {
				SelectPanel(panel2);
			}

			if (e.getActionCommand() == "Show list of Journeys") {
				travelId2.setText(ShowListOfJourney());
				SelectPanel(panel1);
			}

			if (e.getActionCommand() == "Start publishing") {
				ButtonB.setButton(true);
			}
			if (e.getActionCommand() == "Stop publishing") {
				ButtonB.setButton(false);
			}

			if (e.getActionCommand() == "Exit") {
				ExitApp();
			}

			// Actions first screen
			if (e.getActionCommand() == "Travel ID") {
				// System.out.println(travelId.getText());
				LoadJourney(new Integer(travelId.getText()));
				SelectPanel(panel);
			}

			if (e.getActionCommand() == "Save Journey") {
				SaveJourney(new Integer(travelId.getText()), create);
			}

			if (e.getActionCommand() == "Zichtmeter ID") {
				LoadZichtmeter(new Integer(zichtmeterId.getText()));
				SelectPanel(panel2);
			}

			if (e.getActionCommand() == "Save2") {
				SaveZichtmeter(new Integer(zichtmeterId.getText()));
			}

			if (e.getActionCommand() == "Travel IDR") {
			}

			if (e.getActionCommand() == "Remove") {
				System.out.println("Remove " + travelId3.getText());
				DeleteJourney(new Integer(travelId3.getText()));
			}
		}

	};

	// #####################################################################################################################
	// Miscelaneous
	// #####################################################################################################################

	void LockJourney() {
		travelId.setEnabled(false);
	};

	void UnlockJourney() {
		travelId.setEnabled(true);
	};

	void ExitApp() {
		System.exit(0);
	};

	void SelectExtended(boolean b) {
		shipName.setEnabled(b);
		frame.invalidate();
		frame.validate();
	};

	void SelectPanel(JPanel panel) {
		frame.setContentPane(panel);
		panel.revalidate();
		currentPanel = panel;
		frame.invalidate();
		// frame.validate();
		frame.repaint();
	};

	void LoadJourney(Integer ReisID) {
		PositionReport r = service.getPositionReport(ReisID);
		if (r == null) {
			travelId.setText(Integer.toString(ReisID));
			DPId.setText("");
			shipLabel.setText("");
			shipName.setText("Travel not found");
			shipDepth.setText("");
			shipWidth.setText("");
			shipLength.setText("");
			XPos.setText("");
			YPos.setText("");
			vaarHoek.setText("");
			shipSpeed.setText("");
			VideoMode.setSelected(false);
			VBInfo.setSelected(false);
			zasilZ.setSelected(false);
			zasilA.setSelected(false);
			zasilS.setSelected(false);
			zasilI.setSelected(false);
			zasilL.setSelected(false);
		} else {
			travelId.setText(Integer.toString(ReisID));
			DPId.setText(Integer.toString(r.getDpID()));
			shipLabel.setText(r.getScheepsLabel());
			shipName.setText(r.getScheepsNaam());
			shipDepth.setText(Integer.toString(r.getScheepsDiepgang()));
			shipWidth.setText(Integer.toString(r.getScheepsBreedte()));
			shipLength.setText(Integer.toString(r.getScheepsLengte()));
			XPos.setText(Integer.toString(r.getXPos()));
			YPos.setText(Integer.toString(r.getYPos()));
			vaarHoek.setText(Integer.toString(r.getDirection()));
			shipSpeed.setText(Integer.toString(r.getSpeed()));
			if (r.getVideoDisplayMode() == 0) {
				VideoMode.setSelected(false);
			} else {
				VideoMode.setSelected(true);
			}

			if (r.getVraagBijkomendeInfo() == 0) {
				VBInfo.setSelected(false);
			} else {
				VBInfo.setSelected(true);
			}

			byte s = r.getScheepsKenmerk();

			if ((s & 0x10) == 0) {
				zasilZ.setSelected(false);
			} else {
				zasilZ.setSelected(true);
			}

			if ((s & 0x08) == 0) {
				zasilA.setSelected(false);
			} else {
				zasilA.setSelected(true);
			}

			if ((s & 0x04) == 0) {
				zasilS.setSelected(false);
			} else {
				zasilS.setSelected(true);
			}

			if ((s & 0x02) == 0) {
				zasilI.setSelected(false);
			} else {
				zasilI.setSelected(true);
			}

			if ((s & 0x01) == 0) {
				zasilL.setSelected(false);
			} else {
				zasilL.setSelected(true);
			}

		}
	};

	String ShowListOfJourney() {
		String s = service.showListOfJourneys();
		return s;
	};

	void SaveJourney(Integer ReisID, boolean create) {
		PositionReport r = service.getPositionReport(ReisID);

		Binary t = new Binary();

		if (r == null && create) {
			r = new PositionReport();
			r.setReisID(ReisID);
			Services service = new Services();
			service.list.add(r);
		}

		if (r != null) {
			r.setReisID(new Integer(travelId.getText()));
			r.setDpID(t.intToByte(Integer.valueOf(DPId.getText())));
			r.setScheepsLabel(shipLabel.getText());
			r.setScheepsNaam(shipName.getText());
			r.setScheepsDiepgang(new Integer(shipDepth.getText()));
			r.setScheepsBreedte(new Integer(shipWidth.getText()));
			r.setScheepsLengte(new Integer(shipLength.getText()));
			r.setXPos(new Integer(XPos.getText()));
			r.setYPos(new Integer(YPos.getText()));
			if (vaarHoek.getText().trim() == "") {
				r.setDirection(45);
			} else {
				r.setDirection(new Integer(vaarHoek.getText()));
			}

			if (vaarHoek.getText().trim() == "") {
				r.setSpeed(5);
			} else {
				r.setSpeed(new Integer(shipSpeed.getText()));
			}

			if (VideoMode.isSelected()) {
				r.setVideoDisplayMode((byte) 1);
			} else {
				r.setVideoDisplayMode((byte) 0);
			}

			if (VBInfo.isSelected()) {
				r.setVraagBijkomendeInfo(true);
			} else {
				r.setVraagBijkomendeInfo(false);
			}

			byte b = 0;
			if (zasilZ.isSelected())
				b = (byte) (b | 0x10);
			if (zasilA.isSelected())
				b = (byte) (b | 0x08);
			if (zasilS.isSelected())
				b = (byte) (b | 0x04);
			if (zasilI.isSelected())
				b = (byte) (b | 0x02);
			if (zasilL.isSelected())
				b = (byte) (b | 0x01);
			r.setScheepsKenmerk(b);
		}

	};

	void SaveJourneySelect(Integer ReisID, Byte DpID) {
		JourneySelect p = JourneySelect.getInstance();
		p.setButton(ReisID, DpID);
	};

	void SaveZichtmeter(Integer meter) {
		ZichtmeterInfo r = service.getZichtmeter(meter);
		if (r != null) {
			if (CommStatus.getText() == "1") {
				r.comstat = 1;
			} else {
				r.comstat = 0;
			}
			r.zelftest = (byte) zelfTest.getText().charAt(0);
			r.status = (byte) Status.getText().charAt(0);
			r.mistind = (byte) MistIndicator.getText().charAt(0);
		}
	};

	void LoadZichtmeter(Integer meter) {
		ZichtmeterInfo r = service.getZichtmeter(meter);
		if (r != null) {
			zichtmeterId.setText(Integer.toString(meter));

			if (r.getComstat() == 0) {
				CommStatus.setText("0");
			} else {
				CommStatus.setText("1");
			}
			zichtMeter.setText(r.getIdName());
			zelfTest.setText(String.valueOf((char) r.getZelftest()));
			Status.setText(String.valueOf((char) r.getStatusByte()));
			MistIndicator.setText(String.valueOf((char) r.getMistIndByte()));
			MistWaarden.setText(r.getZicht());
		}
	};

	void DeleteJourney(Integer reisID) {
		PositionReport r = service.getPositionReport(reisID);
		if (r != null) {
			service.deletePositionReport(reisID);
		}
	};

	void DeleteAllJourney() {
		service.list = null;
	};

	public Radar(String f1, String f2) {
		filename = f1;
		filenamezm = f2;
	}

	// #####################################################################################################################
	// Main
	// #####################################################################################################################

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				PropertiesObject props = PropertiesObject.getInstance();
				props.loadProperties("config.properties");

				LoggerObject logs = LoggerObject.getInstance();

				String server = props.getProperty("server");
				String portListen = props.getProperty("portlisten");
				String portWrite = props.getProperty("portwrite");

				String filename = props.getProperty("filename");
				String filenamezm = props.getProperty("filenamezm");

				logs.logInfo("Start application: " + server + ":" + portListen
						+ " " + portWrite);
				logs.logDebug("Start application: " + server + ":" + portListen
						+ " " + portWrite);

				if (server == null) {
					logs.logError("Servername in properties not found");
					System.exit(0);
				}

				if (portListen == null) {
					logs.logError("Listen port in properties not found");
					System.exit(0);
				}

				if (portWrite == null) {
					logs.logError("Write port in properties not found");
					System.exit(0);
				}

				Radar t = new Radar(filename, filenamezm);
				ButtonB = StopButton.getInstance();
				ButtonB.setButton(true);

				t.createAndShowGUI();
			}
		});
	}

}