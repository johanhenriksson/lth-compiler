package tests;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import lang.ast.Function;
import lang.ast.Program;
import java.io.*;
import java.util.*;

/**
 * Tests name analysis
 */
@RunWith(Parameterized.class)
public class TestFuncCalls extends AbstractParameterizedTest {
	/**
	 * Directory where test files live
	 */
	private static final String TEST_DIR = "testfiles/func_calls";

	/**
	 * Construct a new JastAdd test
	 * @param filename filename of test input file
	 */
	public TestFuncCalls(String filename) {
		super(TEST_DIR, filename);
	}

    class FuncCmp implements Comparator<Function> {
        @Override
        public int compare(Function a, Function b) {
            return a.getName().getID().compareTo(b.getName().getID());
        }
    }

	/**
	 * Run the JastAdd test
	 */
	@Test
	public void runTest() throws Exception {
        PrintStream out = System.out;
        try {
            Program program = (Program) parse(inFile);

		    StringBuilder sb = new StringBuilder();
            sb.append("digraph G {\n");
            List<Function> functions = new ArrayList<Function>(program.functions());
            java.util.Collections.sort(functions, new FuncCmp());
            for(Function f : functions) {
                sb.append("  ").append(f.getName().getID()).append(";\n");
                List<Function> calls = new ArrayList<Function>(f.calls());
                java.util.Collections.sort(calls, new FuncCmp());

                for(Function c : calls) {
                    sb.append("  ");
                    sb.append(f.getName().getID());
                    sb.append(" -> ");
                    sb.append(c.getName().getID());
                    sb.append(";\n");
                }
            }
            sb.append("}\n");

            compareOutput(sb.toString(), outFile, expectedFile);
        } finally {
            System.setOut(out);
        }
    }

	@SuppressWarnings("javadoc")
	@Parameters(name = "{0}")
	public static Iterable<Object[]> getTests() {
		return getTestParameters(TEST_DIR);
	}
}
