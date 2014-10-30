/**
 * 
 */
package edu.clemson.cs.cpsc330.branchpredict.drivers;

/**
 * @author jared
 *
 */
public class GShare extends BranchPredictor {

	private static final int N = 2;
	private static final int INDEX_LAST_N_BITS = 16;
	private static final int SIZE = 1 << INDEX_LAST_N_BITS;

	private static int[] patternHistoryTable = new int[SIZE];
	private static int globalBhsr = 0;

	{
		if (N < 1) {
			System.out.println("Value for N not sane. N must be 1 or larger.");
			System.exit(1);
		}
	}

	public GShare() {
		super();
	}

	public GShare(String filename) {
		super(filename);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BranchPredictor predictor;

		if (args.length > 0)
			predictor = new GShare(args[0]);
		else
			predictor = new GShare();

		predictor.readInput();
	}

	@Override
	public boolean getPrediction(Long address, boolean didBranch) {
		boolean prediction = false;

		int index = getIndex(address) ^ globalBhsr;

		globalBhsr <<= 1;
		globalBhsr %= SIZE;
		if (didBranch) {
			globalBhsr++;
			patternHistoryTable[index] = incrementState(patternHistoryTable[index]);
		} else
			patternHistoryTable[index] = decrementState(patternHistoryTable[index]);

		prediction = predictBranch(index);

		if (prediction == didBranch)
			bl.incrementSuccesses();
		else
			bl.incrementFailures();

		return prediction;
	}

	@Override
	public int getIndex(Long address) {
		return new Long(address % SIZE).intValue();
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