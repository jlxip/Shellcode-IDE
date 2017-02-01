package net.jlxip.shellcodeide;

public class GetCSource {
	public static String get(String shellcode) {
		String source = "";
		
		source += "unsigned char shellcode[] = \"" + shellcode + "\";\n\n";
		source += "main() {\n";
		source += "	int (*runshellcode)() = (int (*)())shellcode;\n";
		source += "	runshellcode();\n";
		source += "}";
		
		return source;
	}
}
