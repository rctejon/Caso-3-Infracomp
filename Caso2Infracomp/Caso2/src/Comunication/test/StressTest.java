package Comunication.test;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import Comunication.generator.Generator;

public class StressTest {
	

	private int fails;
	
	private long keyTime;
	
	private long actualizationTime;
	
	public StressTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main (String ... args)
	{
		StressTest test = new StressTest();
		PrintWriter writer;
		try {
			writer = new PrintWriter("stressTest.txt", "UTF-8");
			writer.println("Carga ala,");
			Generator gen1 = new Generator(80, 100, test);
			writer.println("2,");
//			writer.println("Carga media,");
//			Generator gen2 = new Generator(200, 40, test);
//			writer.println("Carga baja,");
//			Generator gen3 = new Generator(80, 100, test);
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getFails() {
		return fails;
	}

	public void setFails(int fails) {
		this.fails = fails;
	}

	public long getKeyTime() {
		return keyTime;
	}

	public void setKeyTime(long keyTime) {
		this.keyTime = keyTime;
	}

	public long getActualizationTime() {
		return actualizationTime;
	}

	public void setActualizationTime(long actualizationTime) {
		this.actualizationTime = actualizationTime;
	}

	
	
}
