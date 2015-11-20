package test.dejavu;

import java.util.Set;

import ddt.utils.CFGUtils;
import ddt.utils.ComUtils;
import ddt.utils.DejaVuUtils;
import ddt.utils.bean.cfg.Method;
import ddt.utils.bean.cfg.Node;

public class TestDangerousNode {
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		final String classFilePath0 = "./input/0_MyInfo_jsp.class";
		final String classFilePath1 = "./input/6_MyInfo_jsp.class";
		final String methoPartdName = "Form_Show";
		final String dangerousLinesOutput = "./output/dangerousLines";

		final String[] accepts = { methoPartdName };
		final String[] rejects = { "cobertura" };
		Method method0 = CFGUtils.buildCFG(classFilePath0, accepts, rejects).getMethodByPartName(methoPartdName);
		Method method1 = CFGUtils.buildCFG(classFilePath1, accepts, rejects).getMethodByPartName(methoPartdName);

		Set<Node> dangerousNodes = DejaVuUtils.getDangerousNodes(method0, method1);
		ComUtils.writeLinesByNodes(dangerousNodes, dangerousLinesOutput);
	}
}