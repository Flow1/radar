/**
 * @author Theo den Exter, ARS
 * Date: May 21th 2016
 * Version 1.0
 *
 * Semaphore: not used currently
 *
 * History
 *
 */
package eu.org.srk.radar;

public abstract class Semaphore {
	protected int value = 0;

	protected Semaphore() {
		value = 0;
	}

	protected Semaphore(int initial) {
		value = initial;
	}

	public synchronized void p() {
		value--;
		if (value < 0)
			try {
				wait();
			} catch (InterruptedException e) {
			}
	}

	public synchronized void v() {
		value++;
		if (value >= 0)
			notify();
	}
}