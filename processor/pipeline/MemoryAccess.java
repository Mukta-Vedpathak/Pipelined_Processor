package processor.pipeline;
import generic.Statistics;
import processor.*;
import generic.*;
import generic.Instruction.OperationType;
import generic.Operand.OperandType;

public class MemoryAccess {
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;
	
	public MemoryAccess(Processor containingProcessor,
	EX_MA_LatchType eX_MA_Latch, 
	MA_RW_LatchType mA_RW_Latch,
	IF_EnableLatchType iF_EnableLatch)
	{
		this.containingProcessor=containingProcessor;
		this.EX_MA_Latch=eX_MA_Latch;
		this.MA_RW_Latch=mA_RW_Latch;
		this.IF_EnableLatch=iF_EnableLatch;
	}
	
	public void performMA()
	{
		if( EX_MA_Latch.getIsNop() ){
			EX_MA_Latch.setNop(false);
			MA_RW_Latch.setNop(true);
			MA_RW_Latch.setInstruction(null);
		}
		else if(EX_MA_Latch.isMA_enable()){
			Instruction ci=EX_MA_Latch.getInstruction();
			int aluResult=EX_MA_Latch.getAluResult();
			MA_RW_Latch.setAluResult(aluResult);

		
			OperationType cOP=ci.getOperationType();
			if(cOP==OperationType.store){
			int w=containingProcessor.getRegisterFile().getValue(ci.getSourceOperand1().getValue());
			containingProcessor.getMainMemory().setWord(aluResult, w);
			}else if(cOP==OperationType.load){
			int ldResult=containingProcessor.getMainMemory().getWord(aluResult);
			MA_RW_Latch.setLdResult(ldResult);
			}

			if(ci.getOperationType()==OperationType.end){
				IF_EnableLatch.setIF_enable(false);
			}
			MA_RW_Latch.setInstruction(ci);
			MA_RW_Latch.setRW_enable(true);
			// EX_MA_Latch.setMA_enable(false); //removed to simulate pipelining
		}
	}
}
