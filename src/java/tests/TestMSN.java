package tests;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import lang.ast.Program;
import lang.MSNVisitor;

/**
 * Tests AST dumping
 * @author Jesper Öqvist <jesper.oqvist@cs.lth.se>
 */
@RunWith(Parameterized.class)
public class TestMSN extends AbstractParameterizedTest {
	/**
	 * Directory where test files live
	 */
	private static final String TEST_DIR = "testfiles/msn";

	/**
	 * Construct a new JastAdd test
	 * @param testFile filename of test input file
	 */
	public TestMSN(String testFile) {
		super(TEST_DIR, testFile);
	}

	/**
	 * Run the JastAdd test
	 */
	@Test
	public void runTest() throws Exception {
		Program program = (Program)parse(inFile);

        int msn = MSNVisitor.result(program);
        String value = String.valueOf(msn);

		compareOutput(value, outFile, expectedFile);
	}

	@SuppressWarnings("javadoc")
	@Parameters(name = "{0}")
	public static Iterable<Object[]> getTests() {
		return getTestParameters(TEST_DIR);
	}
}
