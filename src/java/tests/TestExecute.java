package tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.nio.file.Files;
import java.io.ByteArrayOutputStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import lang.ast.Program;

/**
 * Tests code generation
 *
 * @Author Niklas Fors
 */
@RunWith(Parameterized.class)
public class TestExecute extends AbstractParameterizedTest {
	/**
	 * Directory where test files live
	 */
	private static final String TEST_DIR = "testfiles/exec";

	/**
	 * Construct a new code generation test
	 * @param filename filename of test input file
	 */
	public TestExecute(String filename) {
		super(TEST_DIR, filename);
	}

	protected File getFileReplaceExtension(File file, String extension) {
		return getFileReplaceExtension(file.getName(), extension);
	}

	protected File getFileReplaceExtension(String filename, String extension) {
		String simpleName = filename.substring(0, filename.lastIndexOf('.'));
		return getTestFile(simpleName+extension);
	}

	/**
	 * Run the code generation test
	 */
	@Test
	public void runTest() throws IOException, Exception {
        File library_obj = getTestFile("../../src/clib/lib.o");
        
		Program program = (Program) parse(inFile);

		assertEquals("[]", program.errors().toString());

        /* compile program to LLVM IR */
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        program.llvmGen(ps);
        String content = baos.toString("UTF8");

        // write out file
		Files.write(outFile.toPath(), content.getBytes());

		// Assemble bitcode 
		File bitcodeFile = getFileReplaceExtension(inFile.getName(), ".out.bc");
		ArrayList<String> llvm_as = new ArrayList<String>();
		llvm_as.add("llvm-as");
		llvm_as.add(outFile.getAbsolutePath());
        execute(llvm_as);

		// Generate object file
		File objectFile = getFileReplaceExtension(inFile.getName(), ".out.o");
		ArrayList<String> llc = new ArrayList<String>();
		llc.add("llc");
		llc.add("--filetype=obj");
		llc.add(bitcodeFile.getAbsolutePath());
		execute(llc);

		// Link object file and generate executable file
		File execFile = getFileReplaceExtension(inFile.getName(), ".exec");
		ArrayList<String> cmdLd = new ArrayList<String>();
		cmdLd.add("ld");
        
        // if osx
        cmdLd.add("-macosx_version_min"); 
        cmdLd.add("10.10");

        // link with libraries
        cmdLd.add("-lSystem");
        cmdLd.add("-lcrt1.o");

        // output file
		cmdLd.add("-o");
		cmdLd.add(execFile.getAbsolutePath());

        // link object code
		cmdLd.add(objectFile.getAbsolutePath());

        // link with simplic library
        cmdLd.add(library_obj.getAbsolutePath());

		execute(cmdLd);

		// Run the executable file
		String output = execute(Arrays.asList(execFile.getAbsolutePath()));
        if (!output.equals(""))
            fail("Unexpected return value " + output);
	}

	private String execute(List<String> cmd) throws IOException, InterruptedException {
		ProcessBuilder pb = new ProcessBuilder(cmd);
		Process process = pb.start();
		process.getOutputStream().close();
		process.waitFor();

		String standardError = inputStreamToString(process.getErrorStream());
		assertEquals(
			"Standard error was not empty when running command '" + cmd.get(0) + "'",
			"", standardError);
		assertEquals(
			"Exit code was not zero (error occured) when running command '" + cmd.get(0) + "'",
			0, process.exitValue());

		return inputStreamToString(process.getInputStream());
	}
	
	private String inputStreamToString(InputStream is) {
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return sb.toString();
	}

	@SuppressWarnings("javadoc")
	@Parameters(name = "{0}")
	public static Iterable<Object[]> getTests() {
		return getTestParameters(TEST_DIR);
	}
}
