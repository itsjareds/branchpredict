package edu.clemson.cs.cpsc330.branchpredict.common;

/**
 * BranchPredictorInterface is an interface for any BranchPredictor
 * implementation. It includes methods for predictions occurring iteratively
 * within a loop.
 * 
 * @author Jared Klingenberber <klinge2@clemson.edu>
 * @author Shi Zheng <shiz@clemson.edu>
 */
public interface BranchPredictorInterface {
	public void readInput();

	public boolean getPrediction(Long address, boolean didBranch);

	public int getIndex(Long address);

	public int incrementState(int state);

	public int decrementState(int state);

	public boolean predictBranch(int index);
}
