package ddt.utils.bean.node;

import org.apache.bcel.generic.Instruction;

public class LineNode extends Node {
	// Offset of line
	public LineNode(Integer offset, Instruction instruction) {
		// TODO: <1 HIGH> finish constructor of LineNode.
		super(offset.toString());

		Short code = test.TestCFGBuilder.map.get(instruction.getName());
		if (code == null) {
			test.TestCFGBuilder.map.put(instruction.getName(), instruction.getOpcode());
		} else
			assert (code.equals(instruction.getOpcode()));
	}
}