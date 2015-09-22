package ddt.test;

public class Run {
	public static void main(String[] args) throws Exception {
		if(args.length == 1) {
			TestCase testCase = null;
			if("a".equals(args[0]))
				testCase = new TestA();
			if("b".equals(args[0]))
				testCase = new TestB();
			if("c".equals(args[0]))
				testCase = new TestC();
			if(testCase != null) {
				testCase.setUp();
				testCase.test();
				testCase.tearDown();
			}
		}
	}
}
