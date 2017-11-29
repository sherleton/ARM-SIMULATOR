import java.util.*;
import java.io.*;

class Instruction
{
	static HashMap<String,Instruction> map=new HashMap<String,Instruction>();
	
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
}

public class Read
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

	public static void main(String[] args) throws IOException{
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

	}
}