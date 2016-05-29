package eu.org.srk.radar;

public class StopButton {

	private static StopButton instance = null;
	boolean button;

	protected StopButton() {
	}

	public static StopButton getInstance() {
		if (instance == null) {
			instance = new StopButton();
		}
		return instance;
	}

	public boolean getButton() {
		return button;
	}

	public void setButton(boolean b) {
		button = b;
	}

}
