/**
 * 
 */
package edu.clemson.cs.cpsc330.branchpredict.common;

import java.text.DecimalFormat;

/**
 * @author Jared Klingenberger <klinge2@clemson.edu>
 * @author Shi Zheng <shiz@clemson.edu>
 *
 */
public class BranchLog {
	private int successes = 0;
	private int failures = 0;
	private int entries = 0;

	public int getSuccesses() {
		return successes;
	}

	public void incrementSuccesses() {
		this.successes++;
	}

	public int getFailures() {
		return failures;
	}

	public void incrementFailures() {
		this.failures++;
	}

	public int getEntries() {
		return entries;
	}

	public void incrementEntries() {
		this.entries++;
	}

	public String getPercentString() {
		Float percent = 0.0f;

		if (this.getEntries() != 0)
			percent = (this.getFailures() * 100.0f) / this.getEntries();

		return new DecimalFormat("#.##").format(percent) + "%";
	}

	public void printStatistics() {
		System.out.println(this.getEntries() + " entries: mispredicts = "
				+ this.getFailures() + ", rate = " + this.getPercentString());
	}
}
