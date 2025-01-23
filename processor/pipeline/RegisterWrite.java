package processor.pipeline;

import generic.*;
import processor.*;
import generic.Instruction.OperationType;
import generic.Statistics;

public class RegisterWrite {
	Processor containingProcessor;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;
	
	public RegisterWrite(Processor containingProcessor, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch)
	{
		this.containingProcessor=containingProcessor;
		this.MA_RW_Latch=mA_RW_Latch;
		this.IF_EnableLatch=iF_EnableLatch;
	}
	
	public void performRW()
	{
		if(MA_RW_Latch.getIsNop()){
			MA_RW_Latch.setNop(false);
		}else if(MA_RW_Latch.isRW_enable()) {
			Statistics.setRegisterWrites(Statistics.getRegisterWrites()+1);
			Instruction ci=MA_RW_Latch.getInstruction();
			OperationType cOP=ci.getOperationType();
			int rd=-1,l=-1,a=-1;
			switch (cOP){
			case store:
			case jmp:
			case beq:
			case blt:
			case bgt:
				break;
			case load:
				l=MA_RW_Latch.getLdResult();
				rd=ci.getDestinationOperand().getValue();
				containingProcessor.getRegisterFile().setValue(rd, l);
				break;
			case end:
				Simulator.setSimulationComplete(true);
				break;
			default:
				rd=ci.getDestinationOperand().getValue();
				a=MA_RW_Latch.getAluResult();
				containingProcessor.getRegisterFile().setValue(rd, a);
				break;
			}			
			// MA_RW_Latch.setRW_enable(false); //removed to simulate pipelining
			if(cOP!=OperationType.end){
			IF_EnableLatch.setIF_enable(true);
			}
			}
		}

}
