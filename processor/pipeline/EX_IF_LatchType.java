package processor.pipeline;

public class EX_IF_LatchType {

	
	boolean EX_IF_enable;
	int PC;
	
	public EX_IF_LatchType(boolean x, int pc) {
		EX_IF_enable = x;
		PC = pc;
	}
	public EX_IF_LatchType(boolean x) {
		EX_IF_enable = x;
	}
	public int getPC() {
		return PC;
	}
	public boolean isEX_IF_enable() {
		return EX_IF_enable;
	}
	public void setEX_IF_enable(boolean x) {
		EX_IF_enable = x;
	}
	public void setPC(int pc) {
		PC = pc;
	}
	public void setEX_IF_enable(boolean x, int pc) {
		EX_IF_enable = x;
		PC = pc;
	}
	public EX_IF_LatchType() {
		EX_IF_enable = false;
	}

}
