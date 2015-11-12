package test;

import java.util.HashMap;
import java.util.Map;

import ddt.utils.bean.CFG;

public class TestCFGBuilder {
	public static Map<String, Short> map = new HashMap<String, Short>();

	public static void main(String[] args) throws Exception {
		new CFG("./input/class/edu/usc/sql/courses/tawa/AY15/Subject3.class").mainToDottyFile("./output/Subject3.dotty");
		// new CFG("./input/example/Subject1.class").toDottyFile("./output/example/Subject1.dotty");
	}
}
