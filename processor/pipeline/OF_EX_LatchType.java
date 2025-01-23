package processor.pipeline;

import generic.*;

public class OF_EX_LatchType {
	boolean EX_enable;
	boolean nop;
	Instruction instruction;


	public void setEX_enable(boolean x) {
		EX_enable = x;
	}
	public Instruction getInstruction() {
		return this.instruction;
	}
	public void setInstruction(Instruction i){
		this.instruction = i;
	}
	public boolean isEX_enable() {
		return EX_enable;
	}
	public OF_EX_LatchType()
	{
		this.EX_enable = false;
		this.nop = false;
	}
	public void setNop(boolean x){
		this.nop=x;
	}
	public boolean getIsNop(){
		return nop;
	}
}
