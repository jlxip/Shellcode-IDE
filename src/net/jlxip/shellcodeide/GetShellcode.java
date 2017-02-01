package net.jlxip.shellcodeide;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JOptionPane;

public class GetShellcode {
	public static String get(String path) {
		// nasm -f bin -o *.bin *.asm
		try {
			Process nasm = new ProcessBuilder("nasm"+File.separator+"nasm.exe", "-f", "bin", "-o", "tmp.bin", "\""+path+"\"").start();
			InputStream is = nasm.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			
			String line = br.readLine();
			if(br.readLine() != null) {
				String output = line;
				while((line = br.readLine()) != null) {
					output += line + "\n";
				}
				
				JOptionPane.showMessageDialog(null, "An error has ocurred during the execution of NASM:\n"+output);
				br.close();
				isr.close();
				is.close();
				return null;
			}
			
			br.close();
			isr.close();
			is.close();
		} catch (IOException e) {}	 // This should never be called.
		
		
		// READ tmp.bin
		File tmp = new File("tmp.bin");
		byte[] array = null;
		try {
			FileInputStream fis = new FileInputStream(tmp);
            BufferedInputStream bis = new BufferedInputStream(fis);
            array = new byte[bis.available()];
            bis.read(array);
            bis.close();
            fis.close();
		} catch(Exception e) {
			e.printStackTrace();	// This should never be called.
		}
		
		// TO HEXADECIMAL
		final StringBuilder builder = new StringBuilder();
        for(byte b : array) {
            builder.append(String.format("\\x%02x", b));
        }
        
        // DELETE tmp.bin
        tmp.delete();
        
        return builder.toString();
	}
}
