package Comunication.generator;

import uniandes.gload.core.LoadGenerator;
import uniandes.gload.core.Task;
import Comunication.generator.*;
import Comunication.test.StressTest;;
/**
 * GLoad Core Class - Task
 * @author Victor Guana at University of Los Andes (vm.guana26@uniandes.edu.co)
 * Systems and Computing Engineering Department - Engineering Faculty
 * Licensed with Academic Free License version 2.1
 * 
 * ------------------------------------------------------------
 * Example Class Client Server:
 * This is Generator Controller - Setup and Launch the Load 
 * Over the Server Using a Task
 * ------------------------------------------------------------
 * 
 */
public class Generator
{
	/**
	 * Load Generator Service (From GLoad 1.0)
	 */
	private LoadGenerator generator;
	
	/**
	 * Constructs a new Generator
	 */
	public Generator (int number, int gap, StressTest test)
	{
		test.setFails(0);
		Task work = createTask(test);
		int numberOfTasks = number;
		int gapBetweenTasks = gap;
		generator = new LoadGenerator("Client - Server Load Test", numberOfTasks, work, gapBetweenTasks);
		generator.generate();
	}
	
	/**
	 * Helper that Constructs a Task
	 */
	private Task createTask(StressTest test)
	{
		return new ClientServerTask(test);
	}
	
	/**
	 * Starts the Application
	 * @param args
	 */
	public static void main (String ... args)
	{
		@SuppressWarnings("unused")
		Generator gen = new Generator(100,100, new StressTest());
	}

}

