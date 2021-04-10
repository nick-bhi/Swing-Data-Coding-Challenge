package org.dkchallenge;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.dkchallenge.SwingDataProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class JavaConfig {

	@Bean
	public SwingDataProcessor swingDataProcessor() {
		
		String filename = "latestSwing.csv";
		// constructor injection 
		//return new SwingDataProcessor(time(), ax(), ay(), az(), wx(), wy(), wz(), indices(), filename);
		
		List<Double> time = new ArrayList<Double>();
		List<Double> ax = new ArrayList<Double>();
		List<Double> ay = new ArrayList<Double>();
		List<Double> az = new ArrayList<Double>();
		List<Double> wx = new ArrayList<Double>();
		List<Double> wy = new ArrayList<Double>();
		List<Double> wz = new ArrayList<Double>();
		List<IndexPair> ind = new ArrayList<IndexPair>();
		SwingDataProcessor sdp = new SwingDataProcessor();
		sdp.setAx(ax);
		sdp.setAy(ay);
		sdp.setAz(az);
		sdp.setWx(wx);
		sdp.setWy(wy);
		sdp.setWz(wz);
		sdp.setFilename(filename);
		sdp.setTime(time);
		sdp.setIndices(ind);
		return sdp;
		
		
	}
	/* For constructor injection instead of setter injection
	@Bean
	public List<Double> ax(){
		return new ArrayList<Double>();
	}
	@Bean
	public List<Double> ay(){
		return new ArrayList<Double>();
	}
	@Bean
	public List<Double> az(){
		return new ArrayList<Double>();
	}
	@Bean
	public List<Double> wx(){
		return new ArrayList<Double>();
	}
	@Bean
	public List<Double> wy(){
		return new ArrayList<Double>();
	}
	@Bean
	public List<Double> wz(){
		return new ArrayList<Double>();
	}
	@Bean
	public List<Double> time(){
		return new ArrayList<Double>();
	}
	@Bean
	public List<IndexPair> indices(){
		return new ArrayList<IndexPair>();
	}
	*/
}
