package tests;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import lang.ast.Program;

/**
 * Tests AST dumping
 * @author Jesper Öqvist <jesper.oqvist@cs.lth.se>
 */
@RunWith(Parameterized.class)
public class TestLLVMGen extends AbstractParameterizedTest {
	/**
	 * Directory where test files live
	 */
	private static final String TEST_DIR = "testfiles/codegen";

	/**
	 * Construct a new JastAdd test
	 * @param testFile filename of test input file
	 */
	public TestLLVMGen(String testFile) {
		super(TEST_DIR, testFile);
	}

	/**
	 * Run the JastAdd test
	 */
	@Test
	public void runTest() throws Exception {
		Program program = (Program)parse(inFile);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        program.llvmGen(ps);
        String content = baos.toString("UTF8");

		compareOutput(content, outFile, expectedFile);
	}

	@SuppressWarnings("javadoc")
	@Parameters(name = "{0}")
	public static Iterable<Object[]> getTests() {
		return getTestParameters(TEST_DIR);
	}
}
