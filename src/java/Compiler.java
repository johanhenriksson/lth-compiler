package lang;

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

import java.io.FileReader;
import lang.ast.LangParser;
import lang.ast.LangScanner;
import lang.ast.Program;

public class Compiler { 
    public static void main(String[] args) throws IOException, Exception {
        Compiler c = new Compiler();
        c.Compile(args[0]);
    }

	public void Compile(String sourceFile) throws IOException, Exception {
        File library_obj = getFile("./src/clib/lib.o");
        
        File inFile = getFile(sourceFile);
        File outFile = getFileReplaceExtension(inFile.getName(), ".out");
		Program program = (Program)parse(inFile);

        //make sure there are no errors
		if(program.errors().size() > 0) {
            System.out.println(program.errors().toString());
            return;
        }

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
		execute(Arrays.asList(execFile.getAbsolutePath()));
	}

	private void execute(List<String> cmd) throws IOException, InterruptedException {
		ProcessBuilder pb = new ProcessBuilder(cmd);
		Process process = pb.start();
		process.getOutputStream().close();
		process.waitFor();

		String standardError = inputStreamToString(process.getErrorStream());
        if (standardError.length() > 0) {
            System.out.println("Error:");
            System.out.println(standardError);
            System.exit(1);
        }
        
        System.out.println(inputStreamToString(process.getInputStream()));
	}

	private String inputStreamToString(InputStream is) {
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line;
			while ((line = br.readLine()) != null) {
                if(line.trim().length() == 0)
                    continue;
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return sb.toString();
	}

	protected File getFileReplaceExtension(File file, String extension) {
		return getFileReplaceExtension(file.getName(), extension);
	}

	protected File getFileReplaceExtension(String filename, String extension) {
		String simpleName = filename.substring(0, filename.lastIndexOf('.'));
		return getFile(simpleName+extension);
	}

	protected File getFile(String filename) {
		return new File(".", filename);
	}

	protected static Object parse(File file) throws IOException, Exception {
		LangScanner scanner = new LangScanner(new FileReader(file));
		LangParser parser = new LangParser();
		return parser.parse(scanner);
	}
}
