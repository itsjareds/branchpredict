/**
 * 
 */
package edu.clemson.cs.cpsc330.branchpredict.drivers;

import edu.clemson.cs.cpsc330.branchpredict.common.BranchPredictor;

/**
 * @author Jared Klingenberger <klinge2@clemson.edu>
 * @author Shi Zheng <shiz@clemson.edu>
 *
 */
public class BranchTargetBuffer extends BranchPredictor {

	private static final int N = 2;
	private static final int BHT_INDEX_N_BITS = 16;
	private static final int BHT_SIZE = 1 << BHT_INDEX_N_BITS;
	private static final int BHSR_N_BITS = 4;
	private static final int PHT_SIZE = 1 << BHSR_N_BITS;

	private static int[] branchHistoryTable = new int[BHT_SIZE];
	private static int[] patternHistoryTable = new int[BHT_SIZE * PHT_SIZE];

	public BranchTargetBuffer() {
		super(BHT_INDEX_N_BITS);
	}

	public BranchTargetBuffer(String filename) {
		super(BHT_INDEX_N_BITS, filename);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BranchPredictor predictor;

		if (args.length > 0)
			predictor = new BranchTargetBuffer(args[0]);
		else
			predictor = new BranchTargetBuffer();

		predictor.readInput();
	}

	@Override
	public boolean getPrediction(Long address, boolean didBranch) {
		boolean prediction = false;

		int bhtIndex = getIndex(address);
		int phtIndex = branchHistoryTable[bhtIndex] % PHT_SIZE;

		branchHistoryTable[bhtIndex] <<= 1;
		branchHistoryTable[bhtIndex] %= PHT_SIZE;

		prediction = predictBranch(phtIndex);

		if (didBranch) {
			branchHistoryTable[bhtIndex]++;
			patternHistoryTable[phtIndex] = incrementState(patternHistoryTable[phtIndex]);
		} else
			patternHistoryTable[phtIndex] = decrementState(patternHistoryTable[phtIndex]);

		if (prediction == didBranch)
			bl.incrementSuccesses();
		else
			bl.incrementFailures();

		return prediction;
	}

	@Override
	public int getIndex(Long address) {
		return new Long(address % BHT_SIZE).intValue();
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

}
