package org.dkchallenge;

import org.dkchallenge.JavaConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainDriver {

	public static void main (String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(JavaConfig.class);
		
		SwingDataProcessor testData = (SwingDataProcessor) context.getBean("swingDataProcessor");
		//SwingDataProcessor testData2 = (SwingDataProcessor) context.getBean("swingDataProcessor");
		// example values for testing
		int begin = 35;
		int end = 80;
		double treshold = -1.0;
		double threshL = 0.5;
		double threshH = 1.1;
		double thresh1 = -1.0;
		double thresh2 = 0.1;
		int window = 3;
		System.out.println(testData.searchContinuityAboveValue(testData.getAx(),begin, end, treshold, window));
		System.out.println(testData.searchContinuityAboveValueTwoSignals(testData.getAx(),testData.getAy(), begin, end, thresh1, thresh2, window));
		System.out.println(testData.searchMultiContinuityWithinRange(testData.getAx(), begin, end, threshL, threshH, window));
		//System.out.println(testData.searchMultiContinuityWithinRange(testData2.getAx(), begin, end, threshL, threshH, window));
		System.out.println(testData.backSearchContinuityWithinRange(testData.getAx(),end-1, begin, threshL, threshH, window));
		context.close();
	}
}
