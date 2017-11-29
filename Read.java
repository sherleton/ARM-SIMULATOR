import java.util.*;
import java.io.*;
import java.lang.Math.*;

class Instruction
{
	static HashMap<String,Instruction> map=new HashMap<String,Instruction>();
	public static int[] registers = new int[15];

	String name;
	String cond;
	String condition;
	String dp;
	String immediate;
	String opcode;
	String s;
	String op1;
	String dest;
	String op2;
	String address;

	Instruction(String addr,String bin)
	{
		condition = bin.substring(0,4);
		dp = bin.substring(4,6);
		immediate = bin.substring(6,7);
		opcode = bin.substring(7,11);
		s = bin.substring(11,12);
		op1 = bin.substring(12,16);
		dest = bin.substring(16,20);
		op2 = bin.substring(20,32);
		address=addr;
		this.setname();
		this.setcond();
		map.put(address+opcode, this);	
	}

	private void setcond() {

		if(this.condition.equals("0000")){
			cond="EQ";
		}
		else if(this.condition.equals("0001")){
			cond="NE";
		}
		else if(this.condition.equals("0010")){
			cond="CS";
		}
		else if(this.condition.equals("0011")){
			cond="CC";
		}
		else if(this.condition.equals("0100")){
			cond="MI";
		}
		else if(this.condition.equals("0101")){
			cond="PL";
		}
		else if(this.condition.equals("0110")){
			cond="VS";
		}
		else if(this.condition.equals("0111")){
			cond="VC";
		}
		else if(this.condition.equals("1000")){
			cond="HI";
		}
		else if(this.condition.equals("1001")){
			cond="LS";
		}
		else if(this.condition.equals("1010")){
			cond="GE";
		}
		else if(this.condition.equals("1011")){
			cond="LT";
		}
		else if(this.condition.equals("1100")){
			cond="GT";
		}
		else if(this.condition.equals("1101")){
			cond="LE";
		}		
		else if(this.condition.equals("1110")){
			cond="AL";
		}
	}

	private void setname() {

		if(this.dp.equals("00")){
			if(this.opcode.equals("0000")){
				if(this.op2.substring(4,8).equals("1001"))
					this.name="MUL";
				else
					this.name="AND";
			}
			else if(this.opcode.equals("0001")){
				if(this.op2.substring(4,8).equals("1001"))
					this.name="MLA";
				else
					this.name="EOR";
			}
			else if(this.opcode.equals("0010")){
				if(this.op2.substring(4,8).equals("1001"))
					this.name="MLS";
				else
					this.name="SUB";
			}
			else if(this.opcode.equals("0011")){
				this.name="RSB";
			}
			else if(this.opcode.equals("0100")){
				this.name="ADD";
			}
			else if(this.opcode.equals("0101")){
				this.name="ADC";
			}
			else if(this.opcode.equals("0110")){
				this.name="SBC";
			}
			else if(this.opcode.equals("0111")){
				this.name="RSC";
			}
			else if(this.opcode.equals("1000")){
				this.name="TST";
			}
			else if(this.opcode.equals("1001")){
				this.name="TEQ";
			}
			else if(this.opcode.equals("1010")){
				this.name="CMP";
			}
			else if(this.opcode.equals("1011")){
				this.name="CMN";
			}
			else if(this.opcode.equals("1100")){
				this.name="ORR";
			}
			else if(this.opcode.equals("1101")){
				this.name="MOV";
			}
			else if(this.opcode.equals("1110")){
				this.name="BIC";
			}
			else if(this.opcode.equals("1111")){
				this.name="MVN";
			}
		}
		else if(dp.equals("01")){
			if(this.s.equals("0")){
				this.name="STR";
			}
			else
				this.name="LDR";
		}
		else if(dp.equals("10")){
			if(this.opcode.charAt(0) == '0'){
				this.name="B";
			}
			else
				this.name="BL";
		}
	}

	public String decode(){
		String s = "Operation is " + this.name;
		String shift = "";
		long op1, op2, op3, dest, op4;

		if(this.dp.equals("00"))
		{
			if(this.immediate.equals("1")){
				op1 = Integer.parseInt(this.op1, 2);
				op2 = Long.parseLong(this.op2, 2);
				dest = Integer.parseInt(this.dest, 2);
				s += ", First Operand is R" + op1 + ", Immediate Second Operand is " + op2 + ", Destination Register is R" + dest;
			}
			else
			{
				if(this.name.equals("MUL") || this.name.equals("MLA") || this.name.equals("MLS"))
				{
					op1 = Integer.parseInt(this.op2.substring(0, 4), 2);
					op2 = Integer.parseInt(this.op2.substring(8, 12), 2);
					op3 = Integer.parseInt(this.dest, 2);
					dest = Integer.parseInt(this.op1, 2);
					s += ", Rs is R" + op1 + ", Rm is R" + op2 + ", Rn is R" + op3 + ", Destination Register is R" + dest;
				}
				else
				{
					op1 = Integer.parseInt(this.op1, 2);
					op2 = Integer.parseInt(this.op2.substring(8, 12), 2);
					dest = Integer.parseInt(this.dest, 2);

					s += ", First Operand is R" + op1 + ", Second Operand is " + op2 + ", Destination Register is R" + dest;	
					
					if(Long.parseLong(this.op2.substring(0,8),2) > 0)
					{
						if(this.op2.substring(5, 7).equals("00"))
							shift = "LSL";
						else if(this.op2.substring(5, 7).equals("01"))
							shift = "LSR";
						else if(this.op2.substring(5, 7).equals("10"))
							shift = "ASR";
						else
							shift = "RSR";

						if(this.op2.substring(7,8).equals("1"))
						{
							op4 = Integer.parseInt(this.op2.substring(0, 4), 2);
							s += ", with a " + shift + " shift of value in Shift Register R" + op4;
						}
						else
						{
							op4 = Integer.parseInt(this.op2.substring(0, 5), 2);
							s += ", with a " + shift + " shift of offset value " + op4;
						}
					}
				}
			}
		}
		else if(this.dp.equals("01"))
		{
			op1 = Integer.parseInt(this.op1, 2);
			dest = Integer.parseInt(this.dest, 2);
			if(immediate.equals("0"))
			{
				op2 = Long.parseLong(this.op2);
				s += ", First Operand is R" + op1 + ", Immediate offset is " + op2 +", Destination Register is R" + dest;
			}
			else
			{
				op2 = Long.parseLong(this.op2.substring(28, 12));
				s += ", First Operand is R" + op1 + ", Second Operand is R" + op2 + ", Destination Register is R" + dest;

				if(Long.parseLong(this.op2.substring(0, 8),2) > 0)
				{
					if(this.op2.substring(5, 7).equals("00"))
						shift = "LSL";
					else if(this.op2.substring(5, 7).equals("01"))
						shift = "LSR";
					else if(this.op2.substring(5, 7).equals("10"))
						shift = "ASR";
					else
						shift = "RSR";

					if(this.op2.substring(7,8).equals("1"))
					{
						op4 = Integer.parseInt(this.op2.substring(0, 4), 2);
						s += ", with a " + shift + " shift of value in Shift Register R" + op4;
					}
					else
					{
						op4 = Integer.parseInt(this.op2.substring(0, 5), 2);
						s += ", with a " + shift + " shift of offset value " + op4;
					}
				}
			}
		}
		else if(dp.equals("10"))
		{
			op1 = Long.parseLong(this.op2.substring(8,12), 2);
			if(this.opcode.substring(0,1).equals("0"))
				s += ", called Branch at address " + op1;
			else
				s += ", called Branch with Link, at address " + op1;
		}

		return s;
	}
	
}

class Read
{
	public static int[] memory = new int[36864];
	public static ArrayList<String> read(String file) throws IOException
	{
		ArrayList<String> instructions = new ArrayList<String>();
		try
		{
			BufferedReader in = new BufferedReader(new FileReader(file));
			String store = null;

			while((store = in.readLine()) != null)
				instructions.add(store);
			in.close();
		}
		catch(FileNotFoundException e)
		{
			System.out.println("Unable to open file!!!!!!!!!!");
		}

		return instructions;
	}

	public static void readingfile() throws IOException{
		ArrayList<String> instructions = new ArrayList<String>();

		instructions = read("abc.txt");

		ArrayList<String> addresses = new ArrayList<String>();

		for(int i = 0; i < instructions.size(); i++)
		{
			addresses.add(instructions.get(i).split(" ")[0]);
			instructions.set(i, Long.toBinaryString(Long.parseLong(instructions.get(i).split(" ")[1].substring(2), 16)));
		}

		ArrayList<Instruction> coded = new ArrayList<Instruction>();
		for(int i = 0; i < instructions.size(); i++)
			coded.add(new Instruction(addresses.get(i), instructions.get(i)));

		Instruction st = null;

		System.out.println("address   " + "unknown   " + "dp   " + "immediate_bit  " + "opcode   " + "s     " + "op1       " + "dest           " + "op2");

		for(int i = 0; i < coded.size(); i++)
		{
			st = coded.get(i);
			System.out.println(addresses.get(i) + "        " + st.condition + "    " + st.dp + "         " + st.immediate + "          " + st.opcode + "   " + st.s + "      " + st.op1 + "      " + st.dest + "      " + st.op2 + "   " + st.name);
		}

		ArrayList<String> idecoded=new ArrayList<String>();
		for(int i=0;i<coded.size();i++){
			idecoded.add(coded.get(i).decode());
		}
		execute(coded,idecoded);

	}
	public static int find(ArrayList<Instruction> coded,String s){
		for(int i=0;i<coded.size();i++){
			if(coded.get(i).address.equals(s)){
				return i;
			}
		}
		return -1;
	}
	public static void execute(ArrayList<Instruction> coded ,ArrayList<String> a){
		int pc=0;
		while(pc<coded.size()-1){
			//Instruction.registers[15]=c.get(pc).address;
			String operation=a.get(pc).substring(13,16);
			int compare=0;
			if(operation.equals("MOV")){
				int a1=Integer.parseInt(coded.get(pc).op1,2);
				int b1=Integer.parseInt(coded.get(pc).dest,2);
				int a2=Integer.parseInt(coded.get(pc).op2,2);
				System.out.println("DECODE: "+a.get(pc));
				if(coded.get(pc).immediate.equals("1")){

					System.out.println("Read registers R"+a1+"=0");
					System.out.println("EXECUTE: MOV "+a2+" in R"+b1);
					Instruction.registers[b1]=a2;
				}
				else{
					System.out.println("Read registers R"+a1+"=0");
					System.out.println("EXECUTE: MOV "+Instruction.registers[a2]+" in R"+b1);
					Instruction.registers[b1]=Instruction.registers[a2];
				}
				pc++;
			}
			else if(operation.equals("ADD")){
				int a1=Integer.parseInt(coded.get(pc).op1,2);
				int b1=Integer.parseInt(coded.get(pc).dest,2);
				int a2=Integer.parseInt(coded.get(pc).op2,2);
				System.out.println("DECODE: "+a.get(pc));
				if(coded.get(pc).immediate.equals("1")){
					System.out.println("Read registers R"+a1+"="+Instruction.registers[a1]);
					System.out.println("EXECUTE: ADD "+Instruction.registers[a1]+" and "+a2);
					Instruction.registers[b1]=Instruction.registers[a1]+a2;
				}
				else{
					System.out.println("Read registers R"+a1+"="+Instruction.registers[a1]+" R"+a2+"="+Instruction.registers[a2]);
					System.out.println("EXECUTE: ADD "+Instruction.registers[a1]+" and "+Instruction.registers[a2]);
					Instruction.registers[b1]=Instruction.registers[a1]+Instruction.registers[a2];
				}
				pc++;
				System.out.println(Instruction.registers[b1]);	
			}
			else if(operation.equals("SUB")){
				int a1=Integer.parseInt(coded.get(pc).op1,2);
				int b1=Integer.parseInt(coded.get(pc).dest,2);
				int a2=Integer.parseInt(coded.get(pc).op2,2);
				System.out.println("DECODE: "+a.get(pc));
				if(coded.get(pc).immediate.equals("1")){
					System.out.println("Read registers R"+a1+"="+Instruction.registers[a1]);
					System.out.println("EXECUTE: SUB "+Instruction.registers[a1]+" and "+a2);
					Instruction.registers[b1]=Instruction.registers[a1]-a2;
				}
				else{
					System.out.println("Read registers R"+a1+"="+Instruction.registers[a1]+" R"+a2+"="+Instruction.registers[a2]);
					System.out.println("EXECUTE: SUB "+Instruction.registers[a1]+" and "+Instruction.registers[a2]);
					Instruction.registers[b1]=Instruction.registers[a1]-Instruction.registers[a2];
				}
				pc++;
				System.out.println(Instruction.registers[b1]);	
			}
			else if(operation.equals("MUL")){
				int a1 = Integer.parseInt(coded.get(pc).op2.substring(0, 4), 2);
				int a2 = Integer.parseInt(coded.get(pc).op2.substring(8, 12), 2);
				int a3 = Integer.parseInt(coded.get(pc).dest, 2);
				int b1 = Integer.parseInt(coded.get(pc).op1, 2);				
				System.out.println("DECODE: "+a.get(pc));
				pc++;
				System.out.println("Read registers R"+a1+"="+Instruction.registers[a1]+" R"+a2+"="+Instruction.registers[a2]+" R"+a3+"="+Instruction.registers[a3]);
				System.out.println("EXECUTE: MUL "+Instruction.registers[a1]+" and "+Instruction.registers[a2]+", ADD "+Instruction.registers[a3]);
				Instruction.registers[b1]=Instruction.registers[a1]*Instruction.registers[a2]+Instruction.registers[a3];
				
				System.out.println(Instruction.registers[b1]);
			}
			else if(operation.equals("AND")){
				int a1=Integer.parseInt(coded.get(pc).op1,2);
				int b1=Integer.parseInt(coded.get(pc).dest,2);
				int a2=Integer.parseInt(coded.get(pc).op2,2);
				System.out.println("DECODE: "+a.get(pc));
				if(coded.get(pc).immediate.equals("1")){
					System.out.println("Read registers R"+a1+"="+Instruction.registers[a1]);
					System.out.println("EXECUTE: AND "+Instruction.registers[a1]+" and "+a2);
					Instruction.registers[b1]=Instruction.registers[a1]&a2;
				}
				else{
					System.out.println("Read registers R"+a1+"="+Instruction.registers[a1]+" R"+a2+"="+Instruction.registers[a2]);
					System.out.println("EXECUTE: AND "+Instruction.registers[a1]+" and "+Instruction.registers[a2]);
					Instruction.registers[b1]=Instruction.registers[a1]&Instruction.registers[a2];
				}
				pc++;
				System.out.println(Instruction.registers[b1]);	
			}
			else if(operation.equals("ORR")){
				int a1=Integer.parseInt(coded.get(pc).op1,2);
				int b1=Integer.parseInt(coded.get(pc).dest,2);
				int a2=Integer.parseInt(coded.get(pc).op2,2);
				System.out.println("DECODE: "+a.get(pc));
				if(coded.get(pc).immediate.equals("1")){
					System.out.println("Read registers R"+a1+"="+Instruction.registers[a1]);
					System.out.println("EXECUTE: ORR "+Instruction.registers[a1]+" and "+a2);
					Instruction.registers[b1]=Instruction.registers[a1]|a2;
				}
				else{
					System.out.println("Read registers R"+a1+"="+Instruction.registers[a1]+" R"+a2+"="+Instruction.registers[a2]);
					System.out.println("EXECUTE: ORR "+Instruction.registers[a1]+" and "+Instruction.registers[a2]);
					Instruction.registers[b1]=Instruction.registers[a1]|Instruction.registers[a2];
				}
				pc++;
				System.out.println(Instruction.registers[b1]);	
			}
			else if(operation.equals("EOR")){
				int a1=Integer.parseInt(coded.get(pc).op1,2);
				int b1=Integer.parseInt(coded.get(pc).dest,2);
				int a2=Integer.parseInt(coded.get(pc).op2,2);
				System.out.println("DECODE: "+a.get(pc));
				if(coded.get(pc).immediate.equals("1")){
					System.out.println("Read registers R"+a1+"="+Instruction.registers[a1]);
					System.out.println("EXECUTE: EOR "+Instruction.registers[a1]+" and "+a2);
					Instruction.registers[b1]=Instruction.registers[a1]^a2;
				}
				else{
					System.out.println("Read registers R"+a1+"="+Instruction.registers[a1]+" R"+a2+"="+Instruction.registers[a2]);
					System.out.println("EXECUTE: EOR "+Instruction.registers[a1]+" and "+Instruction.registers[a2]);
					Instruction.registers[b1]=Instruction.registers[a1]^Instruction.registers[a2];
				}
				pc++;
				System.out.println(Instruction.registers[b1]);	
			}
			else if(operation.equals("LDR")||operation.equals("STR")){
				int op2=0;
				int op1=0,dest=0,op4=0;
				int shift=0;
				op1 = Integer.parseInt(coded.get(pc).op1, 2);
				dest = Integer.parseInt(coded.get(pc).dest, 2);
				System.out.println("DECODE: "+a.get(pc));

				if(coded.get(pc).s.equals("1"))
				{
					if(coded.get(pc).immediate.equals("0"))
					{
						op2 = Integer.parseInt(coded.get(pc).op2, 2);

						if(coded.get(pc).opcode.substring(0, 1).equals("1"))
						{
							if(coded.get(pc).opcode.substring(1, 2).equals("1"))
							{
								Instruction.registers[dest] = memory[Instruction.registers[op1] + op2];
							}	
							else
							{
								Instruction.registers[dest] = memory[Instruction.registers[op1] - op2];
							}

							if(coded.get(pc).opcode.substring(3, 4).equals("1"))
								Instruction.registers[op1] = Instruction.registers[dest];
						}
						else
						{
							if(coded.get(pc).opcode.substring(1, 2).equals("1"))
							{
								Instruction.registers[dest] = memory[Instruction.registers[op1]];
								Instruction.registers[op1] = memory[Instruction.registers[op1] + op2];
							}	
							else
							{
								Instruction.registers[dest] = memory[Instruction.registers[op1]];
								Instruction.registers[op1] = memory[Instruction.registers[op1] - op2];
							}	
						}
					}
					else
					{
						op2 = Integer.parseInt(coded.get(pc).op2.substring(8, 12), 2);

						if(Integer.parseInt(coded.get(pc).op2.substring(0, 8), 2) > 0)
						{
							if(coded.get(pc).op2.substring(7,8).equals("1"))
							{
								op4 = Integer.parseInt(coded.get(pc).op2.substring(0, 4), 2);
								if(coded.get(pc).op2.substring(5, 7).equals("00"))
									shift = (int)Math.pow(2, Instruction.registers[op4]);
								else if(coded.get(pc).op2.substring(5, 7).equals("01"))
									shift = (int)Math.pow(2, -Instruction.registers[op4]);
							}
							else
							{
								op4 = Integer.parseInt(coded.get(pc).op2.substring(0, 5), 2);
								if(coded.get(pc).op2.substring(5, 7).equals("00"))
									shift = (int)Math.pow(2, op4);
								else if(coded.get(pc).op2.substring(5, 7).equals("01"))
									shift = (int)Math.pow(2, -op4);
							}
						}
						else
							shift = 1;

						if(coded.get(pc).opcode.substring(0, 1).equals("1"))
						{
							if(coded.get(pc).opcode.substring(1, 2).equals("1"))
							{
								Instruction.registers[dest] = memory[Instruction.registers[op1] + Instruction.registers[op2]*shift];
							}	
							else
							{
								Instruction.registers[dest] = memory[Instruction.registers[op1] - Instruction.registers[op2]*shift];
							}

							if(coded.get(pc).opcode.substring(3, 4).equals("1"))
								Instruction.registers[op1] = Instruction.registers[dest];
						}
						else
						{
							if(coded.get(pc).opcode.substring(1, 2).equals("1"))
							{
								Instruction.registers[dest] = Instruction.registers[op1];
								Instruction.registers[op1] = memory[Instruction.registers[op1] + Instruction.registers[op2]*shift];
							}	
							else
							{
								Instruction.registers[dest] = Instruction.registers[op1];
								Instruction.registers[op1] = memory[Instruction.registers[op1] - Instruction.registers[op2]*shift];
							}	
						}
					}
				}
				else
				{
					if(coded.get(pc).immediate.equals("0"))
					{
						op2 = Integer.parseInt(coded.get(pc).op2, 2);

						if(coded.get(pc).opcode.substring(0, 1).equals("1"))
						{
							if(coded.get(pc).opcode.substring(1, 2).equals("1"))
							{
								memory[Instruction.registers[op1] + op2] = Instruction.registers[dest];
							}	
							else
							{
								memory[Instruction.registers[op1] - op2] = Instruction.registers[dest];
							}

							if(coded.get(pc).opcode.substring(3, 4).equals("1"))
								Instruction.registers[op1] = Instruction.registers[dest];
						}
						else
						{
							if(coded.get(pc).opcode.substring(1, 2).equals("1"))
							{
								memory[Instruction.registers[op1]] = Instruction.registers[dest];
								Instruction.registers[op1] = memory[Instruction.registers[op1] + op2];
							}	
							else
							{
								memory[Instruction.registers[op1]]  = Instruction.registers[dest];
								Instruction.registers[op1] = memory[Instruction.registers[op1] - op2];
							}	
						}
					}
					else
					{
						op2 = Integer.parseInt(coded.get(pc).op2.substring(8, 12), 2);

						if(Integer.parseInt(coded.get(pc).op2.substring(0, 8), 2) > 0)
						{
							if(coded.get(pc).op2.substring(7,8).equals("1"))
							{
								op4 = Integer.parseInt(coded.get(pc).op2.substring(0, 4), 2);
								if(coded.get(pc).op2.substring(5, 7).equals("00"))
									shift = (int)Math.pow(2, Instruction.registers[op4]);
								else if(coded.get(pc).op2.substring(5, 7).equals("01"))
									shift = (int)Math.pow(2, -Instruction.registers[op4]);
							}
							else
							{
								op4 = Integer.parseInt(coded.get(pc).op2.substring(0, 5), 2);
								if(coded.get(pc).op2.substring(5, 7).equals("00"))
									shift = (int)Math.pow(2, op4);
								else if(coded.get(pc).op2.substring(5, 7).equals("01"))
									shift = (int)Math.pow(2, -op4);
							}
						}
						else
							shift = 1;

						if(coded.get(pc).opcode.substring(0, 1).equals("1"))
						{
							if(coded.get(pc).opcode.substring(1, 2).equals("1"))
							{
								memory[Instruction.registers[op1] + Instruction.registers[op2]*shift] = Instruction.registers[dest];
							}	
							else
							{
								memory[Instruction.registers[op1] - Instruction.registers[op2]*shift] = Instruction.registers[dest];
							}

							if(coded.get(pc).opcode.substring(3, 4).equals("1"))
								Instruction.registers[op1] = Instruction.registers[dest];
						}
						else
						{
							if(coded.get(pc).opcode.substring(1, 2).equals("1"))
							{
								memory[Instruction.registers[op1]] = Instruction.registers[dest];
								Instruction.registers[op1] = memory[Instruction.registers[op1] + Instruction.registers[op2]*shift];
							}	
							else
							{
								memory[Instruction.registers[op1]]  = Instruction.registers[dest];
								Instruction.registers[op1] = memory[Instruction.registers[op1] - Instruction.registers[op2]*shift];
							}	
						}
					}
				}	
			}
			else if(operation.equals("CMP")){
				int a1=Integer.parseInt(coded.get(pc).op1,2);
				int a2=Integer.parseInt(coded.get(pc).op2,2);
				System.out.println("DECODE: "+a.get(pc));
				if(coded.get(pc).immediate.equals("1")){
					compare=Instruction.registers[a1]-a2;
				}
				else{
					compare=Instruction.registers[a1]-Instruction.registers[a2];
				}
				pc++;
			}
			else if(operation.equals("B")){
				System.out.println("yellow");
				String q=coded.get(pc).condition;
				System.out.println("DECODE: "+a.get(pc));
				if(q.equals("EQ")){
					if(compare==0){
						String a1=(coded.get(pc).op2.substring(8,12));
						 a1="0x"+a1;
						pc=find(coded,a1);
					}
				}
				else if(q.equals("NE")){
					if(compare!=0){
						String a1=(coded.get(pc).op2.substring(8,12));
						 a1="0x"+a1;
						pc=find(coded,a1);
					}
				}
				else if(q.equals("LT")){
					if(compare<0){
						String a1=(coded.get(pc).op2.substring(8,12));
						 a1="0x"+a1;
						pc=find(coded,a1);
					}
				}
				else if(q.equals("GT")){
					if(compare>0){
						String a1=(coded.get(pc).op2.substring(8,12));
						a1="0x"+a1;
						pc=find(coded,a1);
					}
				}
				else if(q.equals("GE")){
					if(compare>=0){
						String a1=(coded.get(pc).op2.substring(8,12));
						 a1="0x"+a1;
						pc=find(coded,a1);
					}
				}
				else if(q.equals("LE")){
					if(compare<=0){
						String a1=(coded.get(pc).op2.substring(8,12));
						 a1="0x"+a1;
						pc=find(coded,a1);
					}
				}
				else{
					String a1=(coded.get(pc).op2.substring(8,12));
					 a1="0x"+a1;
					pc=find(coded,a1);
				}
			}
			else{
				pc++;
			}
		}
	}
}