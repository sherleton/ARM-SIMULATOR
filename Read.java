import java.util.*;
import java.io.*;

class Instruction
{
	String _31_28_;
	String dp;
	String immediate;
	String opcode;
	String s;
	String op1;
	String dest;
	String op2;

	Instruction(String bin)
	{
		_31_28_ = bin.substring(0,4);
		dp = bin.substring(4,6);
		immediate = bin.substring(6,7);
		opcode = bin.substring(7,11);
		s = bin.substring(11,12);
		op1 = bin.substring(12,16);
		dest = bin.substring(16,20);
		op2 = bin.substring(20,32);
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
			coded.add(new Instruction(instructions.get(i)));

		Instruction st = null;

		System.out.println("address   " + "unknown   " + "dp   " + "immediate_bit  " + "opcode   " + "s     " + "op1       " + "dest           " + "op2");

		for(int i = 0; i < coded.size(); i++)
		{
			st = coded.get(i);
			System.out.println(addresses.get(i) + "        " + st._31_28_ + "    " + st.dp + "         " + st.immediate + "          " + st.opcode + "   " + st.s + "      " + st.op1 + "      " + st.dest + "      " + st.op2);
		}

	}
}