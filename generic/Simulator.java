package generic;

import processor.*;
import java.io.*;
import java.nio.*;

public class Simulator {
		
	static Processor processor;
	static boolean simulationComplete;
	
	public static void setupSimulation(String assemblyProgramFile, Processor p)
	{
		Simulator.processor = p;
		loadProgram(assemblyProgramFile);
		
		simulationComplete = false;
	}
	
	static void loadProgram(String assemblyProgramFile)
	{
		/*
		 * TODO
		 * 1. load the program into memory according to the program layout described
		 *    in the ISA specification
		 * 2. set PC to the address of the first instruction in the main
		 * 3. set the following registers:
		 *     x0 = 0
		 *     x1 = 65535
		 *     x2 = 65535
		 */
		try{
			InputStream inp= new FileInputStream(assemblyProgramFile);
			DataInputStream di = new DataInputStream(inp);
			int next=0;
			if(di.available()>0){
				next = di.readInt();
				processor.getRegisterFile().setProgramCounter(next);
			} 
			for(int address=0;di.available()>0;address++){
				next=di.readInt();
				processor.getMainMemory().setWord(address, next);
			}
			processor.getRegisterFile().setValue(0, 0);
			processor.getRegisterFile().setValue(1,65535);
			processor.getRegisterFile().setValue(2, 65535);
			di.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static void simulate()
	{
		// Statistics.setnOI(0);
		// Statistics.setnOC(0);

		while(simulationComplete == false)
		{
			processor.getRWUnit().performRW();
			Clock.incrementClock();
			processor.getMAUnit().performMA();
			Clock.incrementClock();
			processor.getEXUnit().performEX();
			Clock.incrementClock();
			processor.getOFUnit().performOF();
			Clock.incrementClock();
			processor.getIFUnit().performIF();
			Clock.incrementClock();
			
			Statistics.setnOI(Statistics.getNOI() + 1);
			Statistics.setnOC(Statistics.getNOC() + 1);
		}
		System.out.println(Statistics.getNOI()+" "+Statistics.getNOC()+" "+Statistics.getBranches()+" "+Statistics.getStages()+" "+Statistics.getRegisterWrites());
	}
	
	public static void setSimulationComplete(boolean value)
	{
		simulationComplete = value;
	}
}
