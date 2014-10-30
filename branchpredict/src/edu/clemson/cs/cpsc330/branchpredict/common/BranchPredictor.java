/**
 * 
 */
package edu.clemson.cs.cpsc330.branchpredict.common;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

/**
 * @author Jared Klingenberger <klinge2@clemson.edu>
 * @author Shi Zheng <shiz@clemson.edu>
 *
 */
public abstract class BranchPredictor implements BranchPredictorInterface {

	protected BranchLog bl;

	public BranchPredictor(int indexSize) {
		bl = new BranchLog(indexSize);
	}

	public BranchPredictor(int indexSize, String filename) {
		bl = new BranchLog(indexSize);

		// Input coming from plaintext file
		try {
			System.setIn(new FileInputStream(filename));
		} catch (FileNotFoundException e) {
			System.out.println("Error opening input file " + filename);
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void readInput() {
		InputStreamReader isReader = new InputStreamReader(System.in);
		BufferedReader bufReader = new BufferedReader(isReader);

		while (true) {
			try {
				String inputStr = null;
				if ((inputStr = bufReader.readLine()) != null) {
					String[] tokens = inputStr.split("[ ]+");

					if (tokens.length >= 2) {
						String addressStr = tokens[0];
						Long address = Long.parseLong(addressStr, 16);
						boolean didBranch = (Integer.parseInt(tokens[1]) == 1);

						boolean prediction = getPrediction(address, didBranch);

						// if (bl.getEntries() >= 0
						// && bl.getEntries() <= 20) {
						// System.out.println(addressStr + " "
						// + (didBranch ? 1 : 0) + " "
						// + (prediction ? 1 : 0));
						// }
					} else {
						System.out.println("Malformed input line: " + inputStr);
						System.exit(1);
					}
				} else
					break;
			} catch (Exception e) {
				System.out.println("Exception while reading line!");
				e.printStackTrace();
				System.exit(1);
			}
			bl.incrementEntries();
		}
		bl.printStatistics();
	}

}
