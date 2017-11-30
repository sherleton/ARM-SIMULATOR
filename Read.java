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
	String nbin;

	Instruction(String addr,String bin,String n)
	{
		this.nbin=n;
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
			this.cond="EQ";
		}
		else if(this.condition.equals("0001")){
			this.cond="NE";
		}
		else if(this.condition.equals("0010")){
			this.cond="CS";
		}
		else if(this.condition.equals("0011")){
			this.cond="CC";
		}
		else if(this.condition.equals("0100")){
			this.cond="MI";
		}
		else if(this.condition.equals("0101")){
			this.cond="PL";
		}
		else if(this.condition.equals("0110")){
			this.cond="VS";
		}
		else if(this.condition.equals("0111")){
			this.cond="VC";
		}
		else if(this.condition.equals("1000")){
			this.cond="HI";
		}
		else if(this.condition.equals("1001")){
			this.cond="LS";
		}
		else if(this.condition.equals("1010")){
			this.cond="GE";
		}
		else if(this.condition.equals("1011")){
			this.cond="LT";
		}
		else if(this.condition.equals("1100")){
			this.cond="GT";
		}
		else if(this.condition.equals("1101")){
			this.cond="LE";
		}		
		else if(this.condition.equals("1110")){
			this.cond="AL";
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
		else if(dp.equals("11")){
			this.name="SWI";
		}
	}
	
	public String fetch(){
		String s="Fetch Instruction "+this.nbin+" from address "+this.address;
		System.out.println(s);
		return s;
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
				op2 = Long.parseLong(this.op2, 2);
				s += ", First Operand is R" + op1 + ", Immediate offset is " + op2 +", Destination Register is R" + dest;
			}
			else
			{
				op2 = Long.parseLong(this.op2.substring(28, 12), 2);
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
		else if(dp.equals("11"))
		{
			String str = this.op2.substring(4, 12);
			s += ", with command ";
			if(str.equals("00000000"))
				s += "SWI_PrChr";
			else if(str.equals("00000010") || str.equals("01101001"))
				s += "SWI_PrStr";
			else if(str.equals("00010001"))
				s = "";
			else if(str.equals("01101010"))
				s += "SWI_RdStr";
			else if(str.equals("01101011"))
				s += "SWI_PrInt";
			else if(str.equals("01101100"))
				s += "SWI_RdInt";
		}

		return s;
	}	
}

class Read
{
	public static int n = 36864;
	public static int[] memory = new int[n];
	public static Object r0;
	public static int compare = 0;
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

		instructions = read(ChooseFile.path);

		ArrayList<String> addresses = new ArrayList<String>();
		String[] temp=new String[instructions.size()];

		for(int i = 0; i < instructions.size(); i++)
		{
			addresses.add(instructions.get(i).split(" ")[0]);
			temp[i]=instructions.get(i).split(" ")[1].toLowerCase();
			instructions.set(i, Long.toBinaryString(Long.parseLong(instructions.get(i).split(" ")[1].substring(2), 16)));
		}

		String s = "", te = "0";
		int l;
		for(int i = 0; i < instructions.size(); i++)
		{
			if(instructions.get(i).length() < 32)
			{
				s = instructions.get(i);
				l = 32 - s.length();
				for(int j = 0; j < l-1; j++)
					te += "0";
				instructions.set(i, te+s);
			}

		}

		ArrayList<Instruction> coded = new ArrayList<Instruction>();
		for(int i = 0; i < instructions.size(); i++){
			coded.add(new Instruction(addresses.get(i), instructions.get(i),temp[i]));
		}

		Instruction st = null;

		System.out.println("address   " + "unknown   " + "dp   " + "immediate_bit  " + "opcode   " + "s     " + "op1       " + "dest           " + "op2");

		for(int i = 0; i < coded.size(); i++)
		{
			st = coded.get(i);
			System.out.println(addresses.get(i) + "        " + st.condition + "    " + st.dp + "         " + st.immediate + "          " + st.opcode + "   " + st.s + "      " + st.op1 + "      " + st.dest + "      " + st.op2 + "   " + st.name);
		}

		execute(coded);
		System.out.println("EXIT");

	}
	public static int find(ArrayList<Instruction> coded,String s){
		for(int i=0;i<coded.size();i++){
			if(coded.get(i).address.equals(s)){
				return i;
			}
		}
		return -1;
	}
	public static void execute(ArrayList<Instruction> coded ) throws IOException{
		int pc=0;
		Scanner b = new Scanner(System.in);
		while(pc<coded.size()-1){
			String a=coded.get(pc).decode();
			//Instruction.registers[15]=c.get(pc).address;
			String operation=a.split(",")[0].split(" ")[2];
			if(operation.equals("MOV")){
				int a1=Integer.parseInt(coded.get(pc).op1,2);
				int b1=Integer.parseInt(coded.get(pc).dest,2);
				int a2=Integer.parseInt(coded.get(pc).op2,2);
				System.out.println("DECODE: "+a);
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
				memory(coded.get(pc));
				writeback(coded.get(pc));
				pc++;
			}
			else if(operation.equals("ADD")){
				int a1=Integer.parseInt(coded.get(pc).op1,2);
				int b1=Integer.parseInt(coded.get(pc).dest,2);
				int a2=Integer.parseInt(coded.get(pc).op2,2);
				System.out.println("DECODE: "+a);
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
				memory(coded.get(pc));
				writeback(coded.get(pc));
				pc++;
			}
			else if(operation.equals("SUB")){
				int a1=Integer.parseInt(coded.get(pc).op1,2);
				int b1=Integer.parseInt(coded.get(pc).dest,2);
				int a2=Integer.parseInt(coded.get(pc).op2,2);
				System.out.println("DECODE: "+a);
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
				memory(coded.get(pc));
				writeback(coded.get(pc));
				pc++;
			}
			else if(operation.equals("MUL")){
				int a1 = Integer.parseInt(coded.get(pc).op2.substring(0, 4), 2);
				int a2 = Integer.parseInt(coded.get(pc).op2.substring(8, 12), 2);
				int a3 = Integer.parseInt(coded.get(pc).dest, 2);
				int b1 = Integer.parseInt(coded.get(pc).op1, 2);				
				System.out.println("DECODE: "+a);
				System.out.println("Read registers R"+a1+"="+Instruction.registers[a1]+" R"+a2+"="+Instruction.registers[a2]+" R"+a3+"="+Instruction.registers[a3]);
				System.out.println("EXECUTE: MUL "+Instruction.registers[a1]+" and "+Instruction.registers[a2]+", ADD "+Instruction.registers[a3]);
				Instruction.registers[b1]=Instruction.registers[a1]*Instruction.registers[a2]+Instruction.registers[a3];
				memory(coded.get(pc));
				writeback(coded.get(pc));
				pc++;
			}
			else if(operation.equals("AND")){
				int a1=Integer.parseInt(coded.get(pc).op1,2);
				int b1=Integer.parseInt(coded.get(pc).dest,2);
				int a2=Integer.parseInt(coded.get(pc).op2,2);
				System.out.println("DECODE: "+a);
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
				memory(coded.get(pc));
				writeback(coded.get(pc));
				pc++;
			}
			else if(operation.equals("ORR")){
				int a1=Integer.parseInt(coded.get(pc).op1,2);
				int b1=Integer.parseInt(coded.get(pc).dest,2);
				int a2=Integer.parseInt(coded.get(pc).op2,2);
				System.out.println("DECODE: "+a);
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
				memory(coded.get(pc));
				writeback(coded.get(pc));
				pc++;
			}
			else if(operation.equals("EOR")){
				int a1=Integer.parseInt(coded.get(pc).op1,2);
				int b1=Integer.parseInt(coded.get(pc).dest,2);
				int a2=Integer.parseInt(coded.get(pc).op2,2);
				System.out.println("DECODE: "+a);
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
				memory(coded.get(pc));
				writeback(coded.get(pc));
				pc++;
			}
			else if(operation.equals("LDR")||operation.equals("STR")){
				int op2=0;
				int op1=0,dest=0,op4=0;
				int shift=0;
				op1 = Integer.parseInt(coded.get(pc).op1, 2);
				dest = Integer.parseInt(coded.get(pc).dest, 2);
				System.out.println("DECODE: "+a);

				if(coded.get(pc).s.equals("1"))
				{
					if(coded.get(pc).immediate.equals("0"))
					{
						op2 = Integer.parseInt(coded.get(pc).op2, 2);

						if(coded.get(pc).opcode.substring(0, 1).equals("1"))
						{
							if(coded.get(pc).opcode.substring(3, 4).equals("1"))
							{
								if(coded.get(pc).opcode.substring(1, 2).equals("1"))
									Instruction.registers[op1] += op2;
								else
									Instruction.registers[op1] -= op2;
								Instruction.registers[dest] = memory[Instruction.registers[op1] % n];
							}
							else
							{
								if(coded.get(pc).opcode.substring(1, 2).equals("1"))
									Instruction.registers[dest] = memory[(Instruction.registers[op1] + op2 + n) % n];
								else
									Instruction.registers[dest] = memory[(Instruction.registers[op1] - op2 + n) % n];
							}
						}
						else
						{
							if(coded.get(pc).opcode.substring(1, 2).equals("1"))
							{
								Instruction.registers[dest] = memory[(Instruction.registers[op1] + n) % n];
								Instruction.registers[op1] = Instruction.registers[op1] + op2;
							}	
							else
							{
								Instruction.registers[dest] = memory[(Instruction.registers[op1] + n) % n];
								Instruction.registers[op1] = Instruction.registers[op1] - op2;
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
							if(coded.get(pc).opcode.substring(3, 4).equals("1"))
							{
								if(coded.get(pc).opcode.substring(1, 2).equals("1"))
									Instruction.registers[op1] += (Instruction.registers[op2]*shift);
								else
									Instruction.registers[op1] -= (Instruction.registers[op2]*shift);
								Instruction.registers[dest] = memory[Instruction.registers[op1] % n];
							}
							else
							{
								if(coded.get(pc).opcode.substring(1, 2).equals("1"))
									Instruction.registers[dest] = memory[(Instruction.registers[op1] + (Instruction.registers[op2]*shift)) % n];
								else
									Instruction.registers[dest] = memory[(Instruction.registers[op1] - (Instruction.registers[op2]*shift)) % n];
							}
						}
						else
						{
							if(coded.get(pc).opcode.substring(1, 2).equals("1"))
							{
								Instruction.registers[dest] = memory[Instruction.registers[op1] % n];
								Instruction.registers[op1] = Instruction.registers[op1] + (Instruction.registers[op2]*shift);
							}	
							else
							{
								Instruction.registers[dest] = memory[Instruction.registers[op1] % n];
								Instruction.registers[op1] = Instruction.registers[op1] - (Instruction.registers[op2]*shift);
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
							if(coded.get(pc).opcode.substring(3, 4).equals("1"))
							{
								if(coded.get(pc).opcode.substring(1, 2).equals("1"))
									Instruction.registers[op1] += op2;
								else
									Instruction.registers[op1] -= op2;
								memory[Instruction.registers[op1]%n] = Instruction.registers[dest];
							}
							else
							{
								if(coded.get(pc).opcode.substring(1, 2).equals("1"))
									memory[(Instruction.registers[op1] + op2 + n) % n] = Instruction.registers[dest];
								else
									memory[(Instruction.registers[op1] - op2 + n) % n] = Instruction.registers[dest];
							}
						}
						else
						{
							if(coded.get(pc).opcode.substring(1, 2).equals("1"))
							{
								memory[Instruction.registers[op1] % n] = Instruction.registers[dest];
								Instruction.registers[op1] = Instruction.registers[op1] + op2;
							}	
							else
							{
								memory[Instruction.registers[op1] % n]  = Instruction.registers[dest];
								Instruction.registers[op1] = Instruction.registers[op1] - op2;
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
							if(coded.get(pc).opcode.substring(3, 4).equals("1"))
							{
								if(coded.get(pc).opcode.substring(1, 2).equals("1"))
									Instruction.registers[op1] += Instruction.registers[op2]*shift;
								else
									Instruction.registers[op1] -= Instruction.registers[op2]*shift;
								memory[Instruction.registers[op1]%n] = Instruction.registers[dest];
							}
							else
							{
								if(coded.get(pc).opcode.substring(1, 2).equals("1"))
									memory[(Instruction.registers[op1] + Instruction.registers[op2]*shift) % n] = Instruction.registers[dest];
								else
									memory[(Instruction.registers[op1] - Instruction.registers[op2]*shift) % n] = Instruction.registers[dest];
							}
						}
						else
						{
							if(coded.get(pc).opcode.substring(1, 2).equals("1"))
							{
								memory[Instruction.registers[op1] % n] = Instruction.registers[dest];
								Instruction.registers[op1] = Instruction.registers[op1] + Instruction.registers[op2]*shift;
							}	
							else
							{
								memory[Instruction.registers[op1] % n]  = Instruction.registers[dest];
								Instruction.registers[op1] = Instruction.registers[op1] - Instruction.registers[op2]*shift;
							}
						}
					}
				}
				memory(coded.get(pc));
				writeback(coded.get(pc));
				pc++;	
			}
			else if(operation.equals("CMP")){
				int a1=Integer.parseInt(coded.get(pc).op1,2);
				int a2=Integer.parseInt(coded.get(pc).op2,2);
				System.out.println("DECODE: "+a);
				if(coded.get(pc).immediate.equals("1")){
					compare=Instruction.registers[a1]-a2;
				}
				else{
					compare=Instruction.registers[a1]-Instruction.registers[a2];
				}
				memory(coded.get(pc));
				writeback(coded.get(pc));
				pc++;
			}
			else if(operation.equals("B")){
				String q=coded.get(pc).cond;
				System.out.println("DECODE: "+a);
				
				Instruction i = coded.get(pc);
				String ss = i.opcode.substring(1,4) + i.s + i.op1 + i.dest + i.op2;
				int k = 23;
				while(k >= 0)
				{
					if(ss.substring(k, k+1).equals("1"))
						break;
					else
						k--;
				}
				String a1 = "";
				for(int v = 0; v < k; v++)
				{
					if(ss.substring(v,v+1).equals("0"))
						a1 += "1";
					else
						a1 += "0";
				}
				a1 += ss.substring(k,24);
				int dec = Integer.parseInt(a1,2);
				a1 = Integer.toString(dec << 2, 16);

				if(q.equals("EQ")){
					if(compare==0){
						 a1="0x"+a1;
						pc=find(coded,a1);
					}
				}
				else if(q.equals("NE")){
					if(compare!=0){
						 a1="0x"+a1;
						pc=find(coded,a1);
					}
				}
				else if(q.equals("LT")){
					if(compare<0){
						 a1="0x"+a1;
						pc=find(coded,a1);
					}
				}
				else if(q.equals("GT")){
					if(compare>0){
						a1="0x"+a1;
						pc=find(coded,a1);
					}
				}
				else if(q.equals("GE")){
					if(compare>=0){
						 a1="0x"+a1;
						pc=find(coded,a1);
					}
				}
				else if(q.equals("LE")){
					if(compare<=0){
						 a1="0x"+a1;
						pc=find(coded,a1);
					}
				}
				else{
					 a1="0x"+a1;
					pc=find(coded,a1);
				}
			}
			else if(operation.equals("SWI")){
				String str = coded.get(pc).op2.substring(4,12);
				System.out.println("DECODE: "+a);
				System.out.print("EXECUTE: ");
				if(str.equals("00000000")){
					System.out.println("Display Character : " + r0);
				}
				else if(str.equals("00000010") || str.equals("01101001")){
					System.out.println("Display String + " + r0);
				}
				else if(str.equals("01101010")){
					System.out.println("Read String : ");
					System.out.println("Enter the string : ");
					r0 = new String(b.nextLine());
					Instruction.registers[1] = 0;
				}
				else if(str.equals("01101011")){
					System.out.println("Display Integer : " + Instruction.registers[1]);
				}
				else if(str.equals("01101100")){
					System.out.println("Read Integer : ");
					System.out.println("Enter the Integer : ");
					r0 = new Integer(b.nextInt());
					Instruction.registers[0] = (int)r0;
				}
				memory(coded.get(pc));
				writeback(coded.get(pc));
				pc++;
			}
			else{
				memory(coded.get(pc));
				writeback(coded.get(pc));
				pc++;
			}
			
			System.out.println("");
		}
	}
	public static void memory(Instruction i){
		if(i.name.equals("LDR")){
			int dest = Integer.parseInt(i.op1, 2), shift = 1, op4;
			long op2;
			if(i.immediate.equals("0"))
			{
				op2 = Integer.parseInt(i.op2, 2);
			}
			else
			{
				op2 = Integer.parseInt(i.op2.substring(8, 12), 2);
				if(Integer.parseInt(i.op2.substring(0, 8), 2) > 0)
				{
					if(i.op2.substring(7,8).equals("1"))
					{
						op4 = Integer.parseInt(i.op2.substring(0, 4), 2);
						if(i.op2.substring(5, 7).equals("00"))
							shift = (int)Math.pow(2, Instruction.registers[op4]);
						else if(i.op2.substring(5, 7).equals("01"))
							shift = (int)Math.pow(2, -Instruction.registers[op4]);
					}
					else
					{
						op4 = Integer.parseInt(i.op2.substring(0, 5), 2);
						if(i.op2.substring(5, 7).equals("00"))
							shift = (int)Math.pow(2, op4);
						else if(i.op2.substring(5, 7).equals("01"))
							shift = (int)Math.pow(2, -op4);
					}
				}
				else
					shift = 1;
				op2 = Instruction.registers[(int)op2] * shift;
			}
			int op22 = (int)(op2%n);

			if(i.opcode.substring(1, 2).equals("1"))
				dest = (Instruction.registers[dest] + op22)%n;
			else
				dest = (Instruction.registers[dest] - op22)%n;

			System.out.println("MEMORY: Loads value from address: " + dest);
		}
		else if(i.name.equals("STR")){
			int dest = Integer.parseInt(i.op1, 2), shift = 1, op4;
			long op2;
			if(i.immediate.equals("0"))
			{
				op2 = Integer.parseInt(i.op2, 2);
			}
			else
			{
				op2 = Integer.parseInt(i.op2.substring(8, 12), 2);
				if(Integer.parseInt(i.op2.substring(0, 8), 2) > 0)
				{
					if(i.op2.substring(7,8).equals("1"))
					{
						op4 = Integer.parseInt(i.op2.substring(0, 4), 2);
						if(i.op2.substring(5, 7).equals("00"))
							shift = (int)Math.pow(2, Instruction.registers[op4]);
						else if(i.op2.substring(5, 7).equals("01"))
							shift = (int)Math.pow(2, -Instruction.registers[op4]);
					}
					else
					{
						op4 = Integer.parseInt(i.op2.substring(0, 5), 2);
						if(i.op2.substring(5, 7).equals("00"))
							shift = (int)Math.pow(2, op4);
						else if(i.op2.substring(5, 7).equals("01"))
							shift = (int)Math.pow(2, -op4);
					}
				}
				else
					shift = 1;
				op2 = Instruction.registers[(int)op2] * shift;
			}
			int op22 = (int)(op2%n);

			if(i.opcode.substring(1, 2).equals("1"))
				dest = (Instruction.registers[dest] + op22)%n;
			else
				dest = (Instruction.registers[dest] - op22)%n;

			System.out.println("MEMORY: Stores value to address: " + dest);
		}
		else{
			System.out.println("MEMORY: No memory operation");
		}		
	}
	public static void writeback(Instruction i){
		if(i.name.equals("LDR")){
			System.out.println("WRITEBACK: No Writeback");
		}
		else if(i.name.equals("STR")){
			System.out.println("WRITEBACK: No Writeback");
		}
		else if(i.name.equals("B")){
			System.out.println("WRITEBACK: No writeback");
		}
		else if(i.name.equals("CMP")){
			System.out.println("WRITEBACK: No writeback");
		}
		else{ 
			int b1=Integer.parseInt(i.dest,2);
			if(i.name.equals("MUL")){
				b1=Integer.parseInt(i.op1,2);
				System.out.println("WRITEBACK: "+Instruction.registers[b1]+" to R"+b1);
			}
			else{
				System.out.println("WRITEBACK: "+Instruction.registers[b1]+" to R"+b1);	
			}
		}
	}
}