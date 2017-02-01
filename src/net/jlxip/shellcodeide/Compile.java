package net.jlxip.shellcodeide;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

public class Compile {
	public static boolean compile() {
		try {
			new ProcessBuilder("tcc"+File.separator+"tcc.exe", "compiled.c").start().waitFor();
			
			File Fcompiled = new File("compiled.exe");
			if(!Fcompiled.exists() || Fcompiled.isDirectory()) {
				JOptionPane.showMessageDialog(null, "The source code (compiled.c) could not be compiled.");
				return false;
			}
		} catch (IOException e) { return false; }	 // This should never be called.
		catch (InterruptedException e) {
			return false;
		}
		
		// DELETE THE SOURCE CODE
		File source = new File("compiled.c");
		source.delete();
		
		return true;
	}
}
