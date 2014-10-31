package edu.clemson.cs.cpsc330.branchpredict.drivers;

import edu.clemson.cs.cpsc330.branchpredict.common.BranchPredictor;

/**
 * SaturatingCounter is an implementation of an n-bit Saturating Counter branch
 * predictor. It maintains a Branch History Table that contains an n-bit state.
 * If the state is greater than or equal to 2^(n-1), then the branch is
 * predicted taken.
 * 
 * @author Jared Klingenberger
 * @author Shi Zheng
 */
@SuppressWarnings("unused")
public class SaturatingCounter extends BranchPredictor {

	private final int N = 2;
	private int INDEX_N_BITS;

	private int[] branchHistoryTable;

	{
		if (N < 1) {
			System.out.println("Value for N not sane. N must be 1 or larger.");
			System.exit(1);
		}
	}

	public SaturatingCounter(int n) {
		super(n);
		INDEX_N_BITS = n;
		branchHistoryTable = new int[getSize()];
	}

	public SaturatingCounter(int n, String filename) {
		super(n, filename);
		INDEX_N_BITS = n;
		branchHistoryTable = new int[getSize()];
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BranchPredictor predictor = null;

		try {
			if (args.length >= 2)
				predictor = new SaturatingCounter(Integer.parseInt(args[0]),
						args[1]);
			else if (args.length >= 1)
				predictor = new SaturatingCounter(Integer.parseInt(args[0]));
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

	public boolean getPrediction(Long address, boolean didBranch) {
		boolean prediction = false;

		int index = getIndex(address);

		prediction = predictBranch(index);

		if (didBranch)
			branchHistoryTable[index] = incrementState(branchHistoryTable[index]);
		else
			branchHistoryTable[index] = decrementState(branchHistoryTable[index]);

		if (prediction == didBranch)
			bl.incrementSuccesses();
		else
			bl.incrementFailures();

		return prediction;
	}

	public int getIndex(Long address) {
		return new Long(address % getSize()).intValue();
	}

	public int incrementState(int state) {
		return (state + 1 < (1 << N)) ? state + 1 : state;
	}

	public int decrementState(int state) {
		return (state - 1 >= 0) ? state - 1 : state;
	}

	public boolean predictBranch(int index) {
		return (branchHistoryTable[index] >= (1 << (N - 1)));
	}

	private int getSize() {
		return 1 << INDEX_N_BITS;
	}

}
