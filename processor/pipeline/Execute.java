package processor.pipeline;
import generic.Instruction;
import generic.Operand;
import processor.Processor;
import generic.Instruction.OperationType;
import generic.Operand.OperandType;
import java.util.*;
import generic.Statistics;

public class Execute {
	Processor containingProcessor;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	IF_OF_LatchType IF_OF_Latch;
	IF_EnableLatchType IF_EnableLatch;
	
	public Execute(Processor containingProcessor,
	OF_EX_LatchType oF_EX_Latch,
	EX_MA_LatchType eX_MA_Latch,
	EX_IF_LatchType eX_IF_Latch,
	IF_OF_LatchType iF_OF_Latch,
	IF_EnableLatchType iF_EnableLatch)
	{
		this.containingProcessor=containingProcessor;
		this.OF_EX_Latch=oF_EX_Latch;
		this.EX_MA_Latch=eX_MA_Latch;
		this.EX_IF_Latch=eX_IF_Latch;
		this.IF_OF_Latch=iF_OF_Latch;
		this.IF_EnableLatch=iF_EnableLatch;
	}
	
	public void performEX()
	{
		if(OF_EX_Latch.getIsNop()){
			EX_MA_Latch.setNop(true);
			OF_EX_Latch.setNop(false);
			EX_MA_Latch.setInstruction(null);
		}
		else if(OF_EX_Latch.isEX_enable()){
			Instruction ci=OF_EX_Latch.getInstruction();
			EX_MA_Latch.setInstruction(ci);

			int cpc=containingProcessor.getRegisterFile().getProgramCounter() - 1;
			OperationType cOP=ci.getOperationType();
			int sO1=-1, sO2=-1, imm1, remainder;
			int aluResult=-1;
			int opCode=cOP.ordinal();

			if (opCode>23 && opCode<30){
				Statistics.setBranches(Statistics.getBranches()+2);
				IF_EnableLatch.setIF_enable(false);
				IF_OF_Latch.setOF_enable(false);
				OF_EX_Latch.setEX_enable(false);
			}



			switch(cOP){

				case add:
					sO1=containingProcessor.getRegisterFile().getValue(ci.getSourceOperand1().getValue());
					sO2=containingProcessor.getRegisterFile().getValue(ci.getSourceOperand2().getValue());
					aluResult=sO1+sO2;
					break;
				case addi:
					sO1=containingProcessor.getRegisterFile().getValue(ci.getSourceOperand1().getValue());
					imm1=ci.getSourceOperand2().getValue();
					aluResult=sO1+imm1;
					break;
				case sub:
					sO1=containingProcessor.getRegisterFile().getValue(ci.getSourceOperand1().getValue());
					sO2=containingProcessor.getRegisterFile().getValue(ci.getSourceOperand2().getValue());
					aluResult=sO1-sO2;
					break;
				case subi:
					sO1=containingProcessor.getRegisterFile().getValue(ci.getSourceOperand1().getValue());
					imm1=ci.getSourceOperand2().getValue();
					aluResult=sO1-imm1;
					break;
				case mul:
					sO1=containingProcessor.getRegisterFile().getValue(ci.getSourceOperand1().getValue());
					sO2=containingProcessor.getRegisterFile().getValue(ci.getSourceOperand2().getValue());
					aluResult=sO1*sO2;
					break;
				case muli:
					sO1=containingProcessor.getRegisterFile().getValue(ci.getSourceOperand1().getValue());
					imm1=ci.getSourceOperand2().getValue();
					aluResult=sO1*imm1;
					break;
				case div:
					sO1=containingProcessor.getRegisterFile().getValue(ci.getSourceOperand1().getValue());
					sO2=containingProcessor.getRegisterFile().getValue(ci.getSourceOperand2().getValue());
					aluResult=sO1/sO2;
					remainder=(sO1%sO2);
					containingProcessor.getRegisterFile().setValue(31, remainder);
					break;	
				case divi:
					sO1=containingProcessor.getRegisterFile().getValue(ci.getSourceOperand1().getValue());
					imm1=ci.getSourceOperand2().getValue();
					aluResult=sO1/ imm1;
					remainder=(sO1%imm1);
					containingProcessor.getRegisterFile().setValue(31, remainder);
					break;
				case and:
					sO1=containingProcessor.getRegisterFile().getValue(ci.getSourceOperand1().getValue());
					sO2=containingProcessor.getRegisterFile().getValue(ci.getSourceOperand2().getValue());
					aluResult=sO1&sO2;
					break;			
				case andi:
					sO1=containingProcessor.getRegisterFile().getValue(ci.getSourceOperand1().getValue());
					imm1=ci.getSourceOperand2().getValue();
					aluResult=sO1&imm1;
					break;
				case or:
					sO1=containingProcessor.getRegisterFile().getValue(ci.getSourceOperand1().getValue());
					sO2=containingProcessor.getRegisterFile().getValue(ci.getSourceOperand2().getValue());
					aluResult=sO1|sO2;
					break;
				case ori:
					sO1=containingProcessor.getRegisterFile().getValue(ci.getSourceOperand1().getValue());
					imm1=ci.getSourceOperand2().getValue();
					aluResult=sO1|imm1;
					break;
				case xor:
					sO1=containingProcessor.getRegisterFile().getValue(ci.getSourceOperand1().getValue());
					sO2=containingProcessor.getRegisterFile().getValue(ci.getSourceOperand2().getValue());
					aluResult=sO1^sO2;
					break;
				case xori:
					sO1=containingProcessor.getRegisterFile().getValue(ci.getSourceOperand1().getValue());
					imm1=ci.getSourceOperand2().getValue();
					aluResult=sO1^imm1;
					break;
				case slt:
					sO1=containingProcessor.getRegisterFile().getValue(ci.getSourceOperand1().getValue());
					sO2=containingProcessor.getRegisterFile().getValue(ci.getSourceOperand2().getValue());
					aluResult=(sO1<sO2)?1:0;
				case slti:
					sO1=containingProcessor.getRegisterFile().getValue(ci.getSourceOperand1().getValue());
					imm1=ci.getSourceOperand2().getValue();
					aluResult=(sO1<imm1)?1:0;
				case sll:
					sO1=containingProcessor.getRegisterFile().getValue(ci.getSourceOperand1().getValue());
					sO2=containingProcessor.getRegisterFile().getValue(ci.getSourceOperand2().getValue());
					aluResult=sO1<<sO2;
					break;
				case slli:
					sO1=containingProcessor.getRegisterFile().getValue(ci.getSourceOperand1().getValue());
					imm1=ci.getSourceOperand2().getValue();
					aluResult=sO1<<imm1;
					break;
				case srl:
					sO1=containingProcessor.getRegisterFile().getValue(ci.getSourceOperand1().getValue());
					sO2=containingProcessor.getRegisterFile().getValue(ci.getSourceOperand2().getValue());
					aluResult=sO1>>>sO2;
					break;
				case srli:
					sO1=containingProcessor.getRegisterFile().getValue(ci.getSourceOperand1().getValue());
					imm1=ci.getSourceOperand2().getValue();
					aluResult=sO1>>>imm1;
					break;
				case sra:
					sO1=containingProcessor.getRegisterFile().getValue(ci.getSourceOperand1().getValue());
					sO2=containingProcessor.getRegisterFile().getValue(ci.getSourceOperand2().getValue());
					aluResult=sO1>>sO2;
					break;
				case srai:
					sO1=containingProcessor.getRegisterFile().getValue(ci.getSourceOperand1().getValue());
					imm1=ci.getSourceOperand2().getValue();
					aluResult=sO1>>imm1;
					break;
				case store:
					sO1=containingProcessor.getRegisterFile().getValue(ci.getDestinationOperand().getValue());
					imm1=ci.getSourceOperand2().getValue();
					aluResult=sO1+imm1;
					break;
				case load:
					sO1=containingProcessor.getRegisterFile().getValue(ci.getSourceOperand1().getValue());
					imm1=ci.getSourceOperand2().getValue();
					aluResult=sO1+imm1;
					break;
				case jmp:
					OperandType jump=ci.getDestinationOperand().getOperandType();
					if(jump==OperandType.Register){
						imm1=containingProcessor.getRegisterFile().getValue(ci.getDestinationOperand().getValue());
					}
					else{
						imm1=ci.getDestinationOperand().getValue();
					}
					aluResult=cpc+imm1;
					EX_IF_Latch.setEX_IF_enable(true, aluResult);
					break;
				case beq:
					sO1=containingProcessor.getRegisterFile().getValue(ci.getSourceOperand1().getValue());
					sO2=containingProcessor.getRegisterFile().getValue(ci.getSourceOperand2().getValue());
					imm1=ci.getDestinationOperand().getValue();
					if(sO1==sO2){
						aluResult=cpc+imm1;
						EX_IF_Latch.setEX_IF_enable(true, aluResult);
					}
					break;
				case bgt:
					sO1=containingProcessor.getRegisterFile().getValue(ci.getSourceOperand1().getValue());
					sO2=containingProcessor.getRegisterFile().getValue(ci.getSourceOperand2().getValue());
					imm1=ci.getDestinationOperand().getValue();
					if(sO1> sO2){
						aluResult=cpc+imm1;
						EX_IF_Latch.setEX_IF_enable(true, aluResult);
					}
					break;
				case bne:
					sO1=containingProcessor.getRegisterFile().getValue(ci.getSourceOperand1().getValue());
					sO2=containingProcessor.getRegisterFile().getValue(ci.getSourceOperand2().getValue());
					imm1=ci.getDestinationOperand().getValue();
					if(sO1!=sO2){
						aluResult=cpc+imm1;
						EX_IF_Latch.setEX_IF_enable(true, aluResult);
					}
					break;
				case blt:
					sO1=containingProcessor.getRegisterFile().getValue(ci.getSourceOperand1().getValue());
					sO2=containingProcessor.getRegisterFile().getValue(ci.getSourceOperand2().getValue());
					imm1=ci.getDestinationOperand().getValue();
					if(sO1<sO2){
						aluResult=cpc+imm1;
						EX_IF_Latch.setEX_IF_enable(true, aluResult);
					}
					break;				
				case end:
					break;
				default:
					break;
			}
			EX_MA_Latch.setAluResult(aluResult);
			

			// OF_EX_Latch.setEX_enable(false); //removed to simulate piplining
			EX_MA_Latch.setMA_enable(true);

		}
	}
}
