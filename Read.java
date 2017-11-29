import java.util.*;
import java.io.*;

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
				this.name="STORE";
			}
			else
				this.name="Load";
		}
		else if(dp.equals("10")){
			if(this.opcode.charAt(0) == '0'){
				this.name="B";
			}
			else
				this.name="BL";
		}
	}

	public void decode(){
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
					s += ", First Operand is R" + op1 + ", Second Operand is R" + op2 + ", Third Operand is R" + op3 + ", Destination Register is R" + dest;
				}
				else
				{
					op1 = Integer.parseInt(this.op1, 2);
					op2 = Integer.parseInt(this.op2.substring(8, 12), 2);
					dest = Integer.parseInt(this.dest, 2);

					s += ", First Operand is R" + op1 + ", Second Operand is " + op2 + ", Destination Register is R" + dest;	
					
					if(Long.parseLong(this.op2.substring(0, 8), 2) > 0)
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
				op2 = Long.parseLong(this.op2, 2);
				s += ", First Operand is R" + op1 + ", Immediate offset is " + op2 +", Destination Register is R" + dest;
			}
			else
			{
				op2 = Long.parseLong(this.op2.substring(8, 12), 2);
				s += ", First Operand is R" + op1 + ", Second Operand is R" + op2 + ", Destination Register is R" + dest;

				if(Long.parseLong(this.op2.substring(0, 8), 2) > 0)
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

		System.out.println(s);
	}
}

class Read
{
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

		for(int i = 0; i< coded.size(); i++)
			coded.get(i).decode();

	}
}