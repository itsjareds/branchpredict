package edu.clemson.cs.cpsc330.branchpredict.drivers;

import edu.clemson.cs.cpsc330.branchpredict.common.BranchPredictor;

/**
 * GShare is an implementation of a gshare branch predictor. It uses a Pattern
 * History Table indexed with an instruction address xor'ed with a global Branch
 * History Shift Register (as an Integer).
 * 
 * @author Jared Klingenberger <klinge2@clemson.edu>
 * @author Shi Zheng <shiz@clemson.edu>
 */
@SuppressWarnings("unused")
public class GShare extends BranchPredictor {

	private final int N = 2;
	private int INDEX_N_BITS;

	private int[] patternHistoryTable;
	private int globalBhsr = 0;

	{
		if (N < 1) {
			System.out.println("Value for N not sane. N must be 1 or larger.");
			System.exit(1);
		}
	}

	public GShare(int n) {
		super(n);
		INDEX_N_BITS = n;
		patternHistoryTable = new int[getSize()];
	}

	public GShare(int n, String filename) {
		super(n, filename);
		INDEX_N_BITS = n;
		patternHistoryTable = new int[getSize()];
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BranchPredictor predictor = null;

		try {
			if (args.length >= 2)
				predictor = new GShare(Integer.parseInt(args[0]), args[1]);
			else if (args.length >= 1)
				predictor = new GShare(Integer.parseInt(args[0]));
			else
				predictor = new GShare(16);
		} catch (NumberFormatException e) {
			// will be handled with null check
		}

		if (predictor == null) {
			System.out.println("Syntax: <program> n_bit_index [input file]");
			System.exit(1);
		}

		predictor.readInput();
	}

	@Override
	public boolean getPrediction(Long address, boolean didBranch) {
		boolean prediction = false;

		int index = getIndex(address) ^ globalBhsr;

		globalBhsr <<= 1;
		globalBhsr %= getSize();

		prediction = predictBranch(index);

		if (didBranch) {
			globalBhsr++;
			patternHistoryTable[index] = incrementState(patternHistoryTable[index]);
		} else
			patternHistoryTable[index] = decrementState(patternHistoryTable[index]);

		if (prediction == didBranch)
			bl.incrementSuccesses();
		else
			bl.incrementFailures();

		return prediction;
	}

	@Override
	public int getIndex(Long address) {
		return new Long(address % getSize()).intValue();
	}

	@Override
	public int incrementState(int state) {
		return (state + 1 < (1 << N)) ? state + 1 : state;
	}

	@Override
	public int decrementState(int state) {
		return (state - 1 >= 0) ? state - 1 : state;
	}

	@Override
	public boolean predictBranch(int index) {
		return (patternHistoryTable[index] >= (1 << (N - 1)));
	}

	private int getSize() {
		return 1 << INDEX_N_BITS;
	}

}
