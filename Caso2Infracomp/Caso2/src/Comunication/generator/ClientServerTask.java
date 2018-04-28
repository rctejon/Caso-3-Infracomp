package Comunication.generator;

import uniandes.gload.core.Task;
import Comunication.disp.*;

/**
 * GLoad Core Class - Task
 * @author Victor Guana at University of Los Andes (vm.guana26@uniandes.edu.co)
 * Systems and Computing Engineering Department - Engineering Faculty
 * Licensed with Academic Free License version 2.1
 * 
 * ------------------------------------------------------------
 * Example Class Client Server:
 * This Class Represents the task that we want to generate in a concurrent way
 * ------------------------------------------------------------
 * 
 */
public class ClientServerTask extends Task
{

	@Override
	public void execute() 
	{
		// TODO Auto-generated method stub
		try {
			Client client = new Client();
			client.sendState("41 24.2028, 2 10.4418");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void fail() 
	{
		// TODO Auto-generated method stub
		System.out.println(Task.MENSAJE_FAIL);
		
	}

	@Override
	public void success() 
	{
		// TODO Auto-generated method stub
		System.out.println(Task.OK_MESSAGE);
		
	}

}
