package org.dkchallenge;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;



public class SwingDataProcessor {
	private List<Double> time;
	private List<Double> ax;
	private List<Double> ay;
	private List<Double> az;
	private List<Double> wx;
	private List<Double> wy;
	private List<Double> wz;
	//private List<IndexPair> indices; //stores index of first and last elements that meet threshold requirements for multi-continuity function
	String filename;

	public SwingDataProcessor() {
		
	}
	
	public SwingDataProcessor(List<Double> t,List<Double> ax, List<Double> ay, List<Double> az, List<Double> wx, List<Double> wy, List<Double> wz, List<IndexPair> indices, String filename ) {
		this.time = t;
		this.ax = ax;
		this.ay = ay;
		this.az = az;
		this.wx = wx;
		this.wy = wy;
		this.wz = wz;
		//this.indices = indices;
		this.filename = filename;
		try (
	            Reader br = Files.newBufferedReader(Paths.get(filename));
	            CSVParser parser = new CSVParser(br, CSVFormat.DEFAULT);
	        ) {
	            for (CSVRecord record : parser) {
	            	time.add(Double.parseDouble(record.get(0)));
	            	ax.add(Double.parseDouble(record.get(1)));
					ay.add(Double.parseDouble(record.get(2)));
					az.add(Double.parseDouble(record.get(3)));
					wx.add(Double.parseDouble(record.get(4)));
					wy.add(Double.parseDouble(record.get(5)));
					wz.add(Double.parseDouble(record.get(6)));
	        }
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public List<Double> getTime() {
		return time;
	}

	public void setTime(List<Double> time) {
		this.time = time;
	}

	public List<Double> getAx() {
		return ax;
	}

	public void setAx(List<Double> ax) {
		this.ax = ax;
	}

	public List<Double> getAy() {
		return ay;
	}

	public void setAy(List<Double> ay) {
		this.ay = ay;
	}

	public List<Double> getAz() {
		return az;
	}

	public void setAz(List<Double> az) {
		this.az = az;
	}

	public List<Double> getWx() {
		return wx;
	}

	public void setWx(List<Double> wx) {
		this.wx = wx;
	}

	public List<Double> getWy() {
		return wy;
	}

	public void setWy(List<Double> wy) {
		this.wy = wy;
	}

	public List<Double> getWz() {
		return wz;
	}

	public void setWz(List<Double> wz) {
		this.wz = wz;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	/*
	public List<IndexPair> getIndices() {
		return indices;
	}

	public void setIndices(List<IndexPair> indices) {
		this.indices = indices;
	}
	*/
	/**
	* Returns first index where data has values that meet threshold for at least winLength samples in a row
	*
	* @param  data 		 	input column
	* @param  indexBegin 	index to begin search
	* @param  indexEnd 		index where search ends
	* @param  threshold 	value that data entry must be above
	* @param  winLength		entries must be above threshold for winLength in a row
	* @return firstIndex 	first index of series of entries that meet threshold
	*/
	public int searchContinuityAboveValue(List<Double> data, int indexBegin, int indexEnd, double threshold,int winLength) {
		return helper(data, data, data, indexBegin, indexEnd, threshold, threshold, Double.POSITIVE_INFINITY, winLength, false);
	}
	/**
	* Returns first index where data has values that meet threshold for at least winLength samples in a row, starting from higher index and searching down
	*
	* @param  data 		 	input column
	* @param  indexBegin 	index to begin search. Greater than indexEnd.
	* @param  indexEnd 		index where search ends
	* @param  thresholdHi 	value that data entry must be above
	* @param  thresholdLo 	value that data entry must be below
	* @param  winLength		entries must be above threshold for winLength in a row
	* @return firstIndex 	first index of series of entries that meet threshold
	*/
	public int backSearchContinuityWithinRange(List<Double> data, int indexBegin, int indexEnd, double thresholdLo, double thresholdHi,int winLength) {
		return helper(data, data, data, indexEnd, indexBegin, thresholdLo, thresholdLo, thresholdHi,winLength, true);
		
	}
	
	
	/**
	* Returns first index where data1 and data2 have values that meet threshold for at least winLength samples in a row
	*
	* @param  data1		 	input column1
	* @param  data2	 		input column2
	* @param  indexBegin 	index to begin search
	* @param  indexEnd 		index where search ends
	* @param  threshold1 	value that data entry must be above
	* @param  threshold2 	value that data entry must be below
	* @param  winLength		entries must be above threshold for winLength in a row
	* @return firstIndex 	first index of series of entries that meet threshold
	*/
	public int searchContinuityAboveValueTwoSignals(List<Double> data1, List<Double> data2, int indexBegin, int indexEnd, double threshold1, double threshold2,int winLength) {
		return helper(data1, data2, data1, indexBegin, indexEnd, threshold1, threshold2, Double.POSITIVE_INFINITY,winLength, false);
		
	}
	/**
	* Returns all sets of entries that meet threshold
	*
	* @param  data	 		input column
	* @param  indexBegin 	index to begin search
	* @param  indexEnd 		index where search ends
	* @param  thresholdHi 	value that data entry must be above
	* @param  thresholdLo 	value that data entry must be below
	* @param  winLength		entries must be above threshold for winLength in a row
	* @return indices		returns List of entry series that meet threshold as Pair. Each pair contains first and last element in each series.
	*/
	public List<IndexPair> searchMultiContinuityWithinRange(List<Double> data, int indexBegin, int indexEnd, double thresholdLo, double thresholdHi,int winLength) {
		int numSamples = 0;//number of data entries that have met threshold in a row
		int firstIndex = -1;
		int lastIndex = -1; //index marking end of series that meet threshold
		boolean reachedLength = false; //true if encountered 'winlength' number of entries meeting threshold in a row
		List<IndexPair> indices = new ArrayList<IndexPair>();
		for (int i = indexBegin; (i <= indexEnd); i++) {
			if((data.get(i) > thresholdLo) && (data.get(i) < thresholdHi)) {
				if (numSamples == 0) {
					firstIndex = i;
				}
				numSamples++;
				if(numSamples >= winLength) {
					reachedLength = true;
					lastIndex = i;			
				}
			}
			else {
				if (reachedLength) {
					indices.add(new IndexPair(firstIndex, lastIndex));
					reachedLength = false;
					lastIndex = -1;
					firstIndex = -1;
				}
			numSamples = 0;
			firstIndex = -1;
			}
		}
		if (firstIndex != -1 && lastIndex != -1) {
			indices.add(new IndexPair(firstIndex, lastIndex));
		}
		return indices;
	}
	public int helper(List<Double> data1, List<Double> data2, List<Double> data3, int indexBegin, int indexEnd, double thresholdLo1, double thresholdLo2, double thresholdHi,int winLength, boolean backSearch) {
		int numSamples = 0; //number of data entries that have met threshold in a row
		int firstIndex = -1;
		int m;
		if (backSearch) {
			m = indexEnd;
		}
		else { 
			m = 0;
			}
		for (int i = indexBegin; (i <= indexEnd); i++) {
			int k = Math.abs(m-i);
			if((data1.get(k) > thresholdLo1) && (data2.get(k) > thresholdLo2) && (data3.get(k) < thresholdHi)) {
				if (numSamples == 0) {
					firstIndex = k;
				}
				numSamples++;
				if(numSamples >= winLength) {
					return firstIndex;
				}
			}
			else {
			numSamples = 0;
			firstIndex = -1;
			}
		}
		return -1;
	}
}
