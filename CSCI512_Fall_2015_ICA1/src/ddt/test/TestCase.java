package ddt.test;

public interface TestCase {
	public void setUp() throws Exception;
	public void test() throws Exception;
	public void tearDown() throws Exception;
}
