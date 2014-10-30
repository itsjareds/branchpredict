/**
 * N-bit Saturating Counter Branch Predictor
 */
package edu.clemson.cs.cpsc330.branchpredict.drivers;

import edu.clemson.cs.cpsc330.branchpredict.common.BranchPredictor;

/**
 * @author Jared Klingenberger
 * @author Shi Zheng
 *
 */
@SuppressWarnings("unused")
public class SaturatingCounter extends BranchPredictor {

	private static final int N = 2;
	private static final int INDEX_N_BITS = 16;
	private static final int SIZE = 1 << INDEX_N_BITS;

	private static int[] branchHistoryTable = new int[SIZE];

	{
		if (N < 1) {
			System.out.println("Value for N not sane. N must be 1 or larger.");
			System.exit(1);
		}
	}

	public SaturatingCounter() {
		super(INDEX_N_BITS);
	}

	public SaturatingCounter(String filename) {
		super(INDEX_N_BITS, filename);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BranchPredictor predictor;

		if (args.length > 0)
			predictor = new SaturatingCounter(args[0]);
		else
			predictor = new SaturatingCounter();

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
		return new Long(address % SIZE).intValue();
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

}
