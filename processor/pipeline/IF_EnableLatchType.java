package processor.pipeline;

public class IF_EnableLatchType {
	
	boolean IF_enable;
	
	public IF_EnableLatchType()
	{
		IF_enable = true;
	}

	public IF_EnableLatchType(boolean IF_Latch_Value)
	{
		IF_enable = IF_Latch_Value;
	}

	public boolean isIF_enable() {
		return this.IF_enable;
	}

	public void setIF_enable(boolean iF_enable) {
		this.IF_enable = iF_enable;
	}

}