package processor.pipeline;

import processor.*;
import generic.*;

public class InstructionFetch {
	
	Processor containingProcessor;
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	EX_IF_LatchType EX_IF_Latch;
	
	public InstructionFetch(Processor containingProcessor, IF_EnableLatchType iF_EnableLatch, IF_OF_LatchType iF_OF_Latch, EX_IF_LatchType eX_IF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_EnableLatch = iF_EnableLatch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}
	
	public void performIF()
	{
		if(IF_EnableLatch.isIF_enable())
		{
			if(EX_IF_Latch.isEX_IF_enable()) {
				int branchPC = EX_IF_Latch.getPC();
				containingProcessor.getRegisterFile().setProgramCounter(branchPC);
				EX_IF_Latch.setEX_IF_enable(false);
			}
			int cpc = containingProcessor.getRegisterFile().getProgramCounter();
			int newi = containingProcessor.getMainMemory().getWord(cpc);
			IF_OF_Latch.setInstruction(newi);
			containingProcessor.getRegisterFile().setProgramCounter(cpc + 1);
			// IF_EnableLatch.setIF_enable(false); removed to simulate pipelining
			IF_OF_Latch.setOF_enable(true);
		}
	}

}
