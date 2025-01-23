package processor.pipeline;

import generic.*;

public class EX_MA_LatchType {
	
	boolean MA_enable;
	boolean nop;
	Instruction instruction;
	int aluResult;

	public void setInstruction(Instruction i){
		this.instruction = i;
	}

	public Instruction getInstruction() {
		return this.instruction;
	}

	public void setAluResult(int x){
		this.aluResult = x;
	}

	public int getAluResult(){
		return this.aluResult;
	}

	public EX_MA_LatchType()
	{
		MA_enable = false;
		nop=false;
	}

	public boolean isMA_enable() {
		return MA_enable;
	}

	public void setMA_enable(boolean var) {
		MA_enable = var;
	}

	public void setNop(boolean x){
		this.nop=x;
	}

	public boolean getIsNop(){
		return this.nop;
	}
}
