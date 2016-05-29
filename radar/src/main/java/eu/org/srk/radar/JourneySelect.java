/**
 * @author Theo den Exter, ARS
 * Date: May 21th 2016
 * Version 1.0
 *
 * Temp stop messaging to IVS
 *
 * History
 *
 */
package eu.org.srk.radar;

public class JourneySelect {

	private static JourneySelect instance = null;
	static boolean loaded;
	static Integer travelID;
	static Byte dpID;

	protected JourneySelect() {
	}

	public static JourneySelect getInstance() {
		if (instance == null) {
			instance = new JourneySelect();
			travelID = 0;
			dpID = 0;
			loaded = false;
		}
		return instance;
	}

	public Integer getTravelID() {
		return travelID;
	}

	public byte getDpID() {
		return dpID;
	}

	public boolean isLoaded() {
		return loaded;
	}

	public void clearLoaded() {
		loaded = false;
	}

	public void setButton(Integer i, byte b) {
		travelID = i;
		dpID = b;
		loaded = true;
	}

}
