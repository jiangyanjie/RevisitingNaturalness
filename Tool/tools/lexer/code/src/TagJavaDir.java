import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.util.Collection;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.apache.commons.io.FileUtils;

public class TagJavaDir {

	public static String[] buildTable() throws Exception {
		String []table = new String[200];  // only 105 tokens actually...
		for (Field field : JavaLexer.class.getDeclaredFields()) {
			if (field.getType().equals(int.class)) {
				table[field.getInt(null)] = field.getName();
			}
		}
		return table;
	}

	public static void main(String[] argv) throws Exception {
		if (argv.length == 0) {
			System.out.println("Usage: TagJavaDir [src path]");
		} else {
			String [] tokenTable = buildTable();
			Collection<File> filePaths = (Collection<File>) FileUtils.listFiles(new File(argv[0]), new String[] { "java" }, true);
			for (File filePath : filePaths) {
				String fileContent = FileUtils.readFileToString(filePath);
				ANTLRInputStream input = new ANTLRInputStream(fileContent);
				JavaLexer lexel = new JavaLexer(input);
				CommonTokenStream tokens = new CommonTokenStream(lexel);

				tokens.fill();

				BufferedWriter output = new BufferedWriter(new FileWriter(filePath.getAbsoluteFile() + ".tags"));
				int currentLineNumber = 1;
				int lineNumber = 1;
				for (Token token : tokens.getTokens()) {
					if (token.getType() == -1) // EOF
						break;
					lineNumber = token.getLine();
					if (lineNumber != currentLineNumber) {
						output.write("\n");
						currentLineNumber = lineNumber;
						if (token.getCharPositionInLine() > 0)
							output.write(String.format("%" + token.getCharPositionInLine() +"s", ""));
					}

					// System.out.println(token);
					output.write(token.getText() + "<=>" + tokenTable[token.getType()] + " ");
				}
				output.flush();
				output.close();
			}
			System.out.println("Done");
		}
	}
}
