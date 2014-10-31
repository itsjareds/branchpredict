package edu.clemson.cs.cpsc330.branchpredict.common;

import java.text.DecimalFormat;

/**
 * BranchLog is a utility class that logs statistics for Branch Predictors. It
 * includes a convenience method for printing out the collected statistics.
 * 
 * @author Jared Klingenberger <klinge2@clemson.edu>
 * @author Shi Zheng <shiz@clemson.edu>
 */
public class BranchLog {
	private final int INDEX_LAST_N_BITS;
	private int successes = 0;
	private int failures = 0;
	private int entries = 0;

	public BranchLog(final int N) {
		INDEX_LAST_N_BITS = N;
	}

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

	public int getIndexSize() {
		return INDEX_LAST_N_BITS;
	}

	public String getPercentString() {
		Float percent = 0.0f;

		if (this.getEntries() != 0)
			percent = (this.getFailures() * 100.0f) / this.getEntries();

		return new DecimalFormat("#.##").format(percent) + "%";
	}

	public void printStatistics() {
		System.out.println((1 << this.getIndexSize()) + " entries ("
				+ this.getIndexSize() + "-bit index): mispredicts = "
				+ this.getFailures() + ", rate = " + this.getPercentString());
	}
}
