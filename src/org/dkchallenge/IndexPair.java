package org.dkchallenge;

import org.springframework.stereotype.Component;

public class IndexPair {
	private int start;
	private int end;
	public IndexPair(int val1, int val2) {
		start = val1;
		end = val2;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	
	public String toString() {
		return "[start:" + start + "," + "end:" + end + "]";
	}
	
}
