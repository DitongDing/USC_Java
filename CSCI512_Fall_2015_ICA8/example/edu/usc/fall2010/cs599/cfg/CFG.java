/**
 * 
 */
package edu.usc.fall2010.cs599.cfg;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.GotoInstruction;
import org.apache.bcel.generic.IfInstruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.ReturnInstruction;
import org.apache.bcel.generic.Select;

public class CFG {
	// Static Dotty file strings.
	protected static final String[] dottyFileHeader = new String[] { "digraph control_flow_graph {", "", "	node [shape = rectangle]; entry exit;",
			"	node [shape = circle];", "" };
	protected static final String[] dottyFileFooter = new String[] { "}" };
	protected static final String dottyEntryNode = "entry";
	protected static final String dottyExitNode = "exit";
	// Dotty file edge templates.
	protected static final String dottyLineFormat = "	%1$s -> %2$s;%n";
	protected static final String dottyLineLabelFormat = "	%1$s -> %2$s [label = \"%3$s\"];%n";

	// Map associating line number with instruction.
	SortedMap<Integer, InstructionHandle> statements = new TreeMap<Integer, InstructionHandle>();

	/**
	 * Loads an instruction list and creates a new CFG.
	 * 
	 * @param instructions
	 *            Instruction list from the method to create the CFG from.
	 */
	public CFG(InstructionList instructions) {
		System.out.println();
	}

	/**
	 * Generates a Dotty file representing the CFG.
	 * 
	 * @param out
	 *            OutputStream to write the dotty file to.
	 */
	public void generateDotty(OutputStream _out) {
		
	}

	/**
	 * Main method. Generate a Dotty file with the CFG representing a given class file.
	 * 
	 * @param args
	 *            Expects two arguments: <input-class-file> <output-dotty-file>
	 */
	public static void main(String[] args) {
		PrintStream error = System.err;
		// PrintStream debug = new PrintStream( new OutputStream() {} );
		PrintStream debug = new PrintStream(System.out);

		// Check arguments.
		if (args.length != 2) {
			error.println("Wrong number of arguments.");
			error.println("Usage: CFG <input-class-file> <output-dotty-file>");
			System.exit(1);
		}
		String inputClassFilename = args[0];
		String outputDottyFilename = args[1];

		// Parse class file.
		debug.println("Parsing " + inputClassFilename + ".");
		JavaClass cls = null;
		try {
			cls = (new ClassParser(inputClassFilename)).parse();
		} catch (IOException e) {
			e.printStackTrace(debug);
			error.println("Error while parsing " + inputClassFilename + ".");
			System.exit(1);
		}

		// Search for main method.
		debug.println("Searching for main method:");
		Method mainMethod = null;
		for (Method m : cls.getMethods()) {
			debug.println("   " + m.getName());
			if ("main".equals(m.getName())) {
				mainMethod = m;
				break;
			}
		}
		if (mainMethod == null) {
			error.println("No main method found in " + inputClassFilename + ".");
			System.exit(1);
		}

		// Create CFG.
		debug.println("Creating CFG object.");
		CFG cfg = new CFG(new InstructionList(mainMethod.getCode().getCode()));

		// Output Dotty file.
		debug.println("Generating Dotty file.");
		try {
			OutputStream output = new FileOutputStream(outputDottyFilename);
			cfg.generateDotty(output);
			output.close();
		} catch (IOException e) {
			e.printStackTrace(debug);
			error.println("Error while writing to " + outputDottyFilename + ".");
			System.exit(1);
		}

		debug.println("Done.");
	}
}