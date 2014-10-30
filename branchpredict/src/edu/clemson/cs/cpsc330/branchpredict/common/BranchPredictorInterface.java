package edu.clemson.cs.cpsc330.branchpredict.common;

public interface BranchPredictorInterface {
	public void readInput();

	public boolean getPrediction(Long address, boolean didBranch);

	public int getIndex(Long address);

	public int incrementState(int state);

	public int decrementState(int state);

	public boolean predictBranch(int index);
}
