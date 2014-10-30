/**
 * N-bit Saturating Counter Branch Predictor
 */
package edu.clemson.cs.cpsc330.branchpredict.drivers;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

import edu.clemson.cs.cpsc330.branchpredict.common.BranchLog;

/**
 * @author Jared Klingenberger
 * @author Shi Zheng
 *
 */
public class SaturatingCounter {
	private final static int N = 2;
	private final static int INDEX_LAST_N_BITS = 16;
	private final static int SIZE = (int) Math.pow(2, INDEX_LAST_N_BITS);

	private static int[] branchHistory = new int[SIZE];
	private static BranchLog bl = new BranchLog();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length > 0) {
			// Input coming from plaintext file
			try {
				System.setIn(new FileInputStream(args[0]));
			} catch (FileNotFoundException e) {
				System.out.println("Error opening input file " + args[0]);
				e.printStackTrace();
				System.exit(1);
			}
		}

		if (N < 1) {
			System.out.println("Value for N not sane. N must be 1 or larger.");
			System.exit(1);
		}

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

						int index = getIndex(address);

						boolean didBranch = (Integer.parseInt(tokens[1]) == 1);
						if (didBranch)
							branchHistory[index] = incrementState(branchHistory[index]);
						else
							branchHistory[index] = decrementState(branchHistory[index]);

						boolean prediction = predictBranch(index);

						if (prediction == didBranch)
							bl.incrementSuccesses();
						else
							bl.incrementFailures();
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

	private static int decrementState(int state) {
		return (state - 1 >= 0) ? state - 1 : state;
	}

	private static int incrementState(int state) {
		return (state + 1 < Math.pow(2, N)) ? state + 1 : state;
	}

	private static boolean predictBranch(int index) {
		return (branchHistory[index] >= Math.pow(2, N - 1));
	}

	private static int getIndex(Long address) {
		return new Long(address % SIZE).intValue();
	}

}
