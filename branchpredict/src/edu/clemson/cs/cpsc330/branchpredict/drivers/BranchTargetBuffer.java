package edu.clemson.cs.cpsc330.branchpredict.drivers;

import edu.clemson.cs.cpsc330.branchpredict.common.BranchPredictor;

/**
 * BranchTargetBuffer is an implementation of a P6-like branch predictor. It
 * maintains a Branch History Table and a Pattern History Table to implement
 * 2-level adaptive branch prediction.
 * 
 * @author Jared Klingenberger <klinge2@clemson.edu>
 * @author Shi Zheng <shiz@clemson.edu>
 */
public class BranchTargetBuffer extends BranchPredictor {

	private final int N = 2;
	private int BHT_INDEX_N_BITS;
	private int BHSR_N_BITS;

	private int[] branchHistoryTable;
	private int[] patternHistoryTable;

	public BranchTargetBuffer(int index_n, int bhsr_n) {
		super(index_n);
		BHT_INDEX_N_BITS = index_n;
		BHSR_N_BITS = bhsr_n;
		branchHistoryTable = new int[getBhtSize()];
		patternHistoryTable = new int[getBhtSize() * getPhtSize()];
	}

	public BranchTargetBuffer(int index_n, int bhsr_n, String filename) {
		super(index_n, filename);
		BHT_INDEX_N_BITS = index_n;
		BHSR_N_BITS = bhsr_n;
		branchHistoryTable = new int[getBhtSize()];
		patternHistoryTable = new int[getBhtSize() * getPhtSize()];
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BranchPredictor predictor;

		int index_n = 9;
		int bhsr_n = 4;

		if (args.length > 0)
			predictor = new BranchTargetBuffer(index_n, bhsr_n, args[0]);
		else
			predictor = new BranchTargetBuffer(index_n, bhsr_n);

		predictor.readInput();
	}

	@Override
	public boolean getPrediction(Long address, boolean didBranch) {
		boolean prediction = false;

		int bhtIndex = getIndex(address);
		int phtIndex = ((bhtIndex << BHSR_N_BITS) | branchHistoryTable[bhtIndex])
				% (getBhtSize() * getPhtSize());

		branchHistoryTable[bhtIndex] <<= 1;
		branchHistoryTable[bhtIndex] %= getPhtSize();

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
		return new Long(address % getBhtSize()).intValue();
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

	private int getBhtSize() {
		return 1 << BHT_INDEX_N_BITS;
	}

	private int getPhtSize() {
		return 1 << BHSR_N_BITS;
	}

}
