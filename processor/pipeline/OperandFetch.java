package processor.pipeline;

import generic.*;
import processor.*;


import generic.Instruction.OperationType;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;
	
	public OperandFetch(Processor containingProcessor,
	IF_OF_LatchType iF_OF_Latch,
	OF_EX_LatchType oF_EX_Latch,	  
	EX_MA_LatchType eX_Ma_Latch,
	MA_RW_LatchType mA_Rw_Latch,
	IF_EnableLatchType iF_EnableLatch)
	{	this.containingProcessor=containingProcessor;
		this.IF_OF_Latch=iF_OF_Latch;
		this.OF_EX_Latch=oF_EX_Latch;
		this.EX_MA_Latch=eX_Ma_Latch;
		this.MA_RW_Latch=mA_Rw_Latch;
		this.IF_EnableLatch=iF_EnableLatch;
	}

	public static String twoscomplement(StringBuffer str)
	{	int n=str.length();
		int i;
		for(i=n-1;i>=0;i--){if(str.charAt(i)=='1'){break;}}
		if(i ==-1){return "1" + str;}
		for(int k=i-1;k>=0;k--){
		if(str.charAt(k) =='1')
		{str.replace(k, k+1, "0");}
		else{str.replace(k, k+1, "1");}
		}
		return str.toString();
	}

	public static String toBinary(int x, int len){
		if(len>0){return String.format("%" + len + "s",	Integer.toBinaryString(x)).replace(" ", "0");}
		return null;
	}

	public static int toInteger(String binary){
		if(binary.charAt(0) =='1'){StringBuffer bufferBinary=new StringBuffer();
			bufferBinary.append(binary);
			binary="-" + twoscomplement(bufferBinary);}
		else{binary="+"+binary;}
		return Integer.parseInt(binary, 2);
	}

	public static boolean problem(Instruction i, int r1, int r2){
		int index=-100;
		if(i!=null && i.getOperationType()!=null){
			index=i.getOperationType().ordinal();
		}else{index=99;}
		int dest_index=-100;
		if( index<=23 ){
			if(i!=null){
				dest_index=i.getDestinationOperand().getValue();
			}else{dest_index=-1;}
			if( r1 == dest_index || r2 == dest_index ){
				return true;
			}else{return false;}
		}else{return false;}
	}

	public boolean problemDivision(int r1,int r2){
		Instruction ex=OF_EX_Latch.getInstruction();
		Instruction ma=EX_MA_Latch.getInstruction();
		Instruction rw=MA_RW_Latch.getInstruction();
		if( r1==31 || r2==31 ){
			int ex_index=-100;
			int ma_index=-100;
			int rw_index=-100;
			if( ex!=null && ex.getOperationType()!=null ){
				ex_index=ex.getOperationType().ordinal();
			}else{ex_index=99;}
			if( ma!=null && ma.getOperationType()!=null ){
				ma_index=ma.getOperationType().ordinal();
			}else{ma_index=99;}
			if( rw!=null && rw.getOperationType()!=null ){
				rw_index=rw.getOperationType().ordinal();
			}else{rw_index=99;}
			if(
				 ex_index==6 || ex_index==7 ||
				 ma_index==6 || ma_index==7 ||
				 rw_index==6 || rw_index==7 
			  ){
				return true;
			}else{return false;}
		}else{return false;}
	}

	public void problemBubble(){
		IF_EnableLatch.setIF_enable(false);
		OF_EX_Latch.setNop(true);
	}

	public void performOF()
	{
		if(IF_OF_Latch.isOF_enable())
		{
			Statistics.setOFStages(Statistics.getStages()+1);
			int Instruction=IF_OF_Latch.getInstruction();
			int cpc=containingProcessor.getRegisterFile().getProgramCounter();
			String binaryInstruction=toBinary(Instruction, 32);

			OperationType[] operationTypes=OperationType.values();//getting all operation types from OperationType enum
			int opCodeInt=Integer.parseInt(binaryInstruction.substring(0, 5), 2);
			OperationType cOP=operationTypes[opCodeInt];

			if(cOP.ordinal() == 24 || cOP.ordinal() == 25 || cOP.ordinal() == 26 || cOP.ordinal() == 27 || cOP.ordinal() == 28){
				IF_EnableLatch.setIF_enable(false);
			}
			boolean prob=false;
			Instruction ex=OF_EX_Latch.getInstruction();
			Instruction ma=EX_MA_Latch.getInstruction();
			Instruction rw=MA_RW_Latch.getInstruction();

			Instruction currentInstruction=new Instruction();
			Operand rs1=new Operand();
			Operand rs2=new Operand();
			Operand rd=new Operand();
			Operand jump=new Operand();
			Operand imm=new Operand();
			int r1=-1;
			int r2=-1;
			int rdd=-1;
			int immediate=-1;
			// currentInstruction.setProgramCounter(cpc);
			// currentInstruction.setOperationType(cOP);
			switch(cOP){
				case add:
				case sub:
				case mul:
				case div:
				case and:
				case or:
				case xor:
				case slt:
				case sll:
				case srl:
				case sra:
					rs1.setOperandType(Operand.OperandType.Register);
					rs2.setOperandType(Operand.OperandType.Register);
					rd.setOperandType(Operand.OperandType.Register);
					r1=Integer.parseInt((binaryInstruction.substring(5, 10)), 2);
					r2=Integer.parseInt((binaryInstruction.substring(10, 15)), 2);
					rdd=Integer.parseInt((binaryInstruction.substring(15, 20)), 2);
					rs1.setValue(r1);
					rs2.setValue(r2);
					rd.setValue(rdd);
					if(problem(ex,r1,r2)){prob=true;}
					if(problem(ma,r1,r2)){prob=true;}
					if(problem(rw,r1,r2)){prob=true;}
					if(problemDivision(r1,r2)){prob=true;}
					if(prob){this.problemBubble(); break;}

					currentInstruction.setOperationType(operationTypes[opCodeInt]);
					currentInstruction.setSourceOperand1(rs1);
					currentInstruction.setSourceOperand2(rs2);
					currentInstruction.setDestinationOperand(rd);
					break;
				case jmp:
					rdd=Integer.parseInt((binaryInstruction.substring(5, 10)), 2);
					immediate=toInteger(binaryInstruction.substring(10, 32));
					if(immediate!=0){jump.setOperandType(Operand.OperandType.Immediate);
							 jump.setValue(immediate);
					}
					else{jump.setOperandType(Operand.OperandType.Register);
					     jump.setValue(rdd);}
					currentInstruction.setOperationType(operationTypes[opCodeInt]);
					currentInstruction.setDestinationOperand(jump);
					break;
				case end:
					currentInstruction.setOperationType(operationTypes[opCodeInt]);
					IF_EnableLatch.setIF_enable(false);
					break;
				case beq:
				case bne:
				case blt:
				case bgt:
					rs1.setOperandType(Operand.OperandType.Register);
					rs2.setOperandType(Operand.OperandType.Register);
					imm.setOperandType(Operand.OperandType.Immediate);
					r1=Integer.parseInt((binaryInstruction.substring(5, 10)), 2);
					r2=Integer.parseInt((binaryInstruction.substring(10, 15)), 2);
					immediate=toInteger(binaryInstruction.substring(15, 32));
					rs1.setValue(r1);
					rs2.setValue(r2);
					imm.setValue(immediate);
					if(problem(ex,r1,r2)){prob=true;}
					if(problem(ma,r1,r2)){prob=true;}
					if(problem(rw,r1,r2)){prob=true;}
					if(problemDivision(r1,r2)){prob=true;}
					if(prob){this.problemBubble(); break;}

					currentInstruction.setOperationType(operationTypes[opCodeInt]);
					currentInstruction.setSourceOperand1(rs1);
					currentInstruction.setSourceOperand2(rs2);
					currentInstruction.setDestinationOperand(imm);
					break;
				default:
					rs1.setOperandType(Operand.OperandType.Register);
					rd.setOperandType(Operand.OperandType.Register);
					rs2.setOperandType(Operand.OperandType.Immediate);
					r1=Integer.parseInt((binaryInstruction.substring(5, 10)), 2);
					if(problem(ex,r1,r1)){prob=true;}
					if(problem(ma,r1,r1)){prob=true;}
					if(problem(rw,r1,r1)){prob=true;}
					if(problemDivision(r1,r1)){prob=true;}
					if(prob){this.problemBubble(); break;}

					rdd=Integer.parseInt((binaryInstruction.substring(10, 15)), 2);
					immediate=toInteger(binaryInstruction.substring(15, 32));
					rs1.setValue(r1);
					rs2.setValue(immediate);
					rd.setValue(rdd);
					currentInstruction.setOperationType(operationTypes[opCodeInt]);
					currentInstruction.setSourceOperand1(rs1);
					currentInstruction.setSourceOperand2(rs2);
					currentInstruction.setDestinationOperand(rd);
					break;
			}

			OF_EX_Latch.setInstruction(currentInstruction);
			// IF_OF_Latch.setOF_enable(false);  //removed to simulate pipelining
			OF_EX_Latch.setEX_enable(true);

		}
	}

}
