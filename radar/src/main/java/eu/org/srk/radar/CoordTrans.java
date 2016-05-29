/**
 * @author Theo den Exter, ARS
 * Date: May 21th 2016
 * Version 1.0
 *
 * Coordinate transformation for polygon
 *
 * History
 *
 */
package eu.org.srk.radar;

import java.awt.Polygon;

public class CoordTrans {

	int dimx;
	int dimy;
	int offsetx;
	int offsety;

	int xposm = 0;
	int yposm = 0;
	int xposm2 = 0;
	int yposm2 = 0;

	Polygon poly;

	// Virtal radar position at Ellewoutsdijk
	// N 51 23.635 E003 47.907
	// RD: 44431 379511
	private static CoordTrans instance = null;

	protected CoordTrans() {
	}

	public static CoordTrans getInstance() {
		if (instance == null) {
			instance = new CoordTrans();
		}
		return instance;
	}

	public void setData(int x1, int x2, int x3, int x4, int x5, int x6, int x7,
			int x8, Polygon poly1) {
		dimx = x1;
		dimy = x2;
		offsetx = x3;
		offsety = x4;

		xposm = x5;
		yposm = x6;
		xposm2 = x7;
		yposm2 = x8;
		poly = poly1;
	}

	public boolean isInWesterscheldtRD(Integer xpos, Integer ypos) {
		int xp = (int) (1.0 * (xpos - xposm2) / (xposm - xposm2) * (dimx + offsetx))
				- offsetx;
		int yp = (int) (1.0 * (yposm - ypos) / (yposm - yposm2) * (dimy + offsety))
				- offsety;
		return poly.inside(xp, yp);
	}

	public boolean isInWesterscheldt(Integer xpos, Integer ypos) {
		int xpos1 = xpos + 44431;
		int ypos1 = ypos + 379511;
		int xp = (int) (1.0 * (xpos1 - xposm2) / (xposm - xposm2) * (dimx + offsetx))
				- offsetx;
		int yp = (int) (1.0 * (yposm - ypos1) / (yposm - yposm2) * (dimy + offsety))
				- offsety;
		return poly.inside(xp, yp);
	}

	public Integer calculateXPos(Integer xpos, Integer ypos) {
		int xpos1 = xpos + 44431;
		int ypos1 = ypos + 379511;
		int xp = (int) (1.0 * (xpos1 - xposm2) / (xposm - xposm2) * (dimx + offsetx))
				- offsetx;
		// int yp=(int)
		// (1.0*(yposm-ypos1)/(yposm-yposm2)*(dimy+offsety))-offsety;
		return xp;
	}

	public Integer calculateYPos(Integer xpos, Integer ypos) {
		int xpos1 = xpos + 44431;
		int ypos1 = ypos + 379511;
		// int xp=(int)
		// (1.0*(xpos1-xposm2)/(xposm-xposm2)*(dimx+offsetx))-offsetx;
		int yp = (int) (1.0 * (yposm - ypos1) / (yposm - yposm2) * (dimy + offsety))
				- offsety;
		return yp;
	}
}
