package test.cfg;

import ddt.utils.CFGUtils;
import ddt.utils.bean.cfg.CFG;

public class CFGUtilsTest {
	public static void main(String[] args) throws Exception {
		CFG cfg = new CFG("./input/Subject3.class");
		cfg.mainToDottyFile("./output/Subject3.dotty");

		CFGUtils.checkProperty(cfg, "./input/propertyInput", "./output/propertyOutput");
	}
}
