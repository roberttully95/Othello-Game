package Othello;

import static java.lang.Math.pow;
import static java.lang.Math.round;

public class Math {

	/**
	 * Returns the average of an array of doubles.
	 * 
	 * @param array
	 *            provides array of integers from which the method can calculate the
	 *            average.
	 * 
	 * @return average of input array to 2 decimal places.
	 */
	protected double averageOfArray(int[] array) {

		// initialize local variable
		double sum = 0;

		// calculate sum of digits
		for (int i = 0; i < array.length; i++) {
			sum = sum + array[i];
		}

		// calculate average
		double average = sum / array.length;

		// rounds to 2 decimal places
		average = average*100;
		average = round(average);
		average = average/100;
		
		return average;
	}

	/**
	 * Returns the uncorrected standard deviation of an array of doubles.
	 * 
	 * @param array
	 *            provides array of integers from which the method can calculate the
	 *            standard deviations.
	 * 
	 * @return uncorrected standard deviation of input array to 2 decimal places.
	 */
	protected double standardDeviationOfArray(int[] array) {

		// initialize local variables
		double deviationFromMean = 0;
		double sum = 0;

		// calculates the average of the array.
		double average = averageOfArray(array);

		// calculates sum of deviations from mean.
		for (int i = 0; i < array.length; i++) {
			deviationFromMean = array[i] - average;
			sum = sum + pow(deviationFromMean, 2);
		}

		double x = sum/(array.length);
		
		// calculates uncorrected standard deviation
		double standardDeviation = pow(x, 0.5);
		
		// rounds to 2 decimal places
		standardDeviation = standardDeviation*100;
		standardDeviation = round(standardDeviation);
		standardDeviation = standardDeviation/100;
		
		return standardDeviation;
	}
	
}
