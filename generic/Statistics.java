package generic;

import processor.*;
import java.io.*;
import java.nio.*;

public class Statistics {
	
	// TODO add your statistics here
	static int nOI;
	static int nOC;
	static int OF;
	static int branches;
	static int registerWrites;
	

	public static void printStatistics(String statFile)
	{
		try
		{
			PrintWriter writer = new PrintWriter(statFile);

			//Printing statistics in the statFile
			writer.println("Number of instructions carried out = " + Statistics.nOI);
			writer.println("Number of cycles taken = " + Statistics.nOC);
			writer.println("Number of cycles per instruction = " + (Statistics.nOC/Statistics.nOI));
			writer.println("Number of instructions per cycle = " + (Statistics.nOI/Statistics.nOC));
			writer.println("Number of OF Stalls = " + (Statistics.nOI-Statistics.registerWrites));
			writer.println("Number of Wrong Branches = " + Statistics.branches);
			
			writer.close();
		}
		catch(Exception e)
		{
			Misc.printErrorAndExit(e.getMessage());
		}
	}
	
	// TODO write functions to update statistics
	public static void setnOI(int nOI) {
		Statistics.nOI = nOI;
	}
	
	public static void setnOC(int nOC) {
		Statistics.nOC = nOC;
	}

	public static void setRegisterWrites(int rW) {
		Statistics.registerWrites = rW;
	}

	public static void setOFStages(int OF) {
		Statistics.OF = OF;
	}
	
	public static void setBranches(int branches) {
		Statistics.branches = branches;
	}

	public static int getNOI() { return Statistics.nOI; }
	public static int getNOC() { return Statistics.nOC; }
	public static int getRegisterWrites() { return Statistics.registerWrites; }
	public static int getStages() { return Statistics.OF; }
	public static int getBranches() { return Statistics.branches; }
}
