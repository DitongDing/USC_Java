package ddt.utils;

import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.*;

import ddt.utils.bean.cfg.CFG;

public class CFGUtils {
	public static CFG buildCFG(String classFilePath, String[] accepts, String[] rejects) {
		CFG result = null;
		try {
			result = new CFG(classFilePath, accepts, rejects);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String getMethodName(JavaClass cls, org.apache.bcel.classfile.Method method) {
		String methodName = String.format("%s.%s%s", cls.getClassName(), method.getName(), method.getSignature());
		return methodName;
	}

	public static String getMethodName(InvokeInstruction instruction, ConstantPoolGen CPG) {
		return String.format("%s.%s%s", instruction.getReferenceType(CPG), instruction.getMethodName(CPG),
				instruction.getSignature(CPG));
	}

	public static String getTargetOffset(IfInstruction ifInstruction) {
		return "" + ifInstruction.getTarget().getPosition();
	}

	public static String getTargetOffset(GotoInstruction gotoInstruction) {
		return "" + gotoInstruction.getTarget().getPosition();
	}

	public static String getIfMeanning(IfInstruction ifInstruction) {
		// String top_1 = "stack[top-1]";
		// String top_0 = "stack[top]";
		String top_1 = "";
		String top_0 = "";
		String zero = "0";
		String NULL = "null";
		String left = "";
		String right = "";
		String op = "";
		if (ifInstruction instanceof IF_ACMPEQ || ifInstruction instanceof IF_ICMPEQ) {
			left = top_1;
			op = "==";
			right = top_0;
		} else if (ifInstruction instanceof IF_ACMPNE || ifInstruction instanceof IF_ICMPNE) {
			left = top_1;
			op = "!=";
			right = top_0;
		} else if (ifInstruction instanceof IF_ICMPGE) {
			left = top_1;
			op = ">=";
			right = top_0;
		} else if (ifInstruction instanceof IF_ICMPGT) {
			left = top_1;
			op = ">";
			right = top_0;
		} else if (ifInstruction instanceof IF_ICMPLE) {
			left = top_1;
			op = "<=";
			right = top_0;
		} else if (ifInstruction instanceof IF_ICMPLT) {
			left = top_1;
			op = "<";
			right = top_0;
		} else if (ifInstruction instanceof IFEQ) {
			left = top_0;
			op = "==";
			right = zero;
		} else if (ifInstruction instanceof IFGE) {
			left = top_0;
			op = ">=";
			right = zero;
		} else if (ifInstruction instanceof IFGT) {
			left = top_0;
			op = ">";
			right = zero;
		} else if (ifInstruction instanceof IFLE) {
			left = top_0;
			op = "<=";
			right = zero;
		} else if (ifInstruction instanceof IFLT) {
			left = top_0;
			op = "<";
			right = zero;
		} else if (ifInstruction instanceof IFNE) {
			left = top_0;
			op = "!=";
			right = zero;
		} else if (ifInstruction instanceof IFNONNULL) {
			left = top_0;
			op = "!=";
			right = NULL;
		} else if (ifInstruction instanceof IFNULL) {
			left = top_0;
			op = "==";
			right = NULL;
		}
		return String.format("%s %s %s", left, op, right);
	}

	public static String getMethodClassName(String methodName) {
		return methodName.substring(0, methodName.lastIndexOf('.'));
	}

	public static String getMethodShortName(String methodName) {
		return methodName.substring(methodName.lastIndexOf('.') + 1, methodName.indexOf('('));
	}

	public static String getNodeDescription(InstructionHandle instructionHandle, ConstantPoolGen CPG) {
		Instruction instruction = instructionHandle.getInstruction();
		String result = "";
		if (instruction instanceof InvokeInstruction) {
			InvokeInstruction invokeInstruction = (InvokeInstruction) instruction;
			String methodName = CFGUtils.getMethodName(invokeInstruction, CPG);
			result = "Call " + getMethodShortName(methodName);
		} else if (instruction instanceof GotoInstruction) {
			GotoInstruction gotoInstruction = (GotoInstruction) instruction;
			String targetOffset = CFGUtils.getTargetOffset(gotoInstruction);
			result = "Goto " + targetOffset;
		} else {
			result = "";
		}
		return result;
	}
}