package processor.pipeline;
import generic.*;

public class MA_RW_LatchType {
	
	boolean RW_enable;
	boolean nop;
	Instruction instruction;
	int aluResult;
	int ldResult;

	public void setInstruction(Instruction i){
		this.instruction = i;
	}
	public Instruction getInstruction() {
		return instruction;
	}
	public void setRW_enable(boolean var) {
		RW_enable = var;
	}
	public int getAluResult(){
		return this.aluResult;
	}
	public boolean isRW_enable() {
		return RW_enable;
	}
	public int getLdResult(){
		return this.ldResult;
	}
	public MA_RW_LatchType()
	{
		this.RW_enable = false;
		this.nop=false;
	}
	public void setAluResult(int x){
		this.aluResult = x;
	}
	public void setLdResult(int y){
		this.ldResult = y;
	}
	public void setNop(boolean x){
		this.nop=x;
	}
	public boolean getIsNop(){
		return this.nop;
	}

}
