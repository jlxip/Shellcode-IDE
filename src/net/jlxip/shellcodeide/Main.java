package net.jlxip.shellcodeide;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Main extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel droppedFile;
	private JLabel state;
	Boolean ready = false;
	private final Pattern Pdot = Pattern.compile(Pattern.quote("."));
	private JButton btnCSource;
	private JButton btnCompile;
	private JButton btnRun;

	public static void main(String[] args) {
		if(!checkExtension("nasm", "NASM")) return;
		
		Main frame = new Main();
		frame.setVisible(true);
	}

	public Main() {
		setTitle("Shellcode IDE");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 574, 357);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel dragpanel = new JPanel();
		dragpanel.setBounds(5, 5, 551, 242);
		dragpanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		contentPane.add(dragpanel);
		dragpanel.setLayout(new BorderLayout(0, 0));
		
	    dragpanel.setTransferHandler(new DragAndDrop(this));
		
		droppedFile = new JLabel("Drag and drop a .asm file here");
		droppedFile.setHorizontalAlignment(SwingConstants.CENTER);
		dragpanel.add(droppedFile, BorderLayout.CENTER);
		
		JButton btnCreateShellcode = new JButton("CREATE SHELLCODE");
		btnCreateShellcode.setToolTipText("Copy the shellcode to clipboard");
		btnCreateShellcode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!ready) return;
				
				String shellcode = GetShellcode.get(droppedFile.getText());
				if(shellcode == null) return;
				
		        CopyToClipboard.copy(shellcode);
		        state.setText("State: shellcode copied to clipboard.");
			}
		});
		btnCreateShellcode.setBounds(5, 260, 162, 25);
		contentPane.add(btnCreateShellcode);
		
		state = new JLabel("State: ready.");
		state.setBounds(5, 298, 551, 16);
		contentPane.add(state);
		
		btnCSource = new JButton("C SOURCE");
		btnCSource.setToolTipText("Copy the C source to clipboard");
		btnCSource.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!ready) return;
				
				String shellcode = GetShellcode.get(droppedFile.getText());
				if(shellcode == null) return;
				
				CopyToClipboard.copy(GetCSource.get(shellcode));
				state.setText("State: C source copied to clipboard.");
			}
		});
		btnCSource.setBounds(179, 260, 93, 25);
		contentPane.add(btnCSource);
		
		btnCompile = new JButton("COMPILE");
		btnCompile.setToolTipText("Compile the shellcode");
		btnCompile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!ready) return;
				
				if(!compile()) return;
				
				state.setText("State: shellcode compiled (compiled.exe).");
			}
		});
		btnCompile.setBounds(284, 260, 83, 25);
		contentPane.add(btnCompile);
		
		btnRun = new JButton("RUN");
		btnRun.setToolTipText("Run the shellcode");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!ready) return;
				
				if(!compile()) return;
				
				state.setText("State: executed.");
				
				try {
					new ProcessBuilder("cmd.exe", "/c", "start", "compiled.exe").start().waitFor();	// Run it, but visible e.e
				} catch (IOException e1) {}	// This should never be called.
				catch (InterruptedException e1) {
					return;
				}
			}
		});
		btnRun.setBounds(379, 260, 57, 25);
		contentPane.add(btnRun);
	}
	
	public void receiveFile(File file) {
		if(!Pdot.split(file.getName())[1].equals("asm")) {
			state.setText("State: the file must have the \".asm\" extension.");
			ready = false;
			return;
		}
		
		ready = true;
		droppedFile.setText(file.getAbsolutePath());
		
		return;
	}
	
	public static boolean checkExtension(String extension, String errorExtensionName) {
		File tcc = new File(extension);
		if(!tcc.exists() || !tcc.isDirectory()) {
			JOptionPane.showMessageDialog(null, errorExtensionName+" was not found in the directory.");
			return false;
		}
		
		return true;
	}
	
	public boolean compile() {
		if(!checkExtension("tcc", "TCC")) return false;
		
		String shellcode = GetShellcode.get(droppedFile.getText());
		if(shellcode == null) return false;
		
		try {
			FileWriter FWsource = new FileWriter("compiled.c");
			FWsource.write(GetCSource.get(shellcode));
			FWsource.close();
		} catch(IOException ioe) {	// ?
			ioe.printStackTrace();
			return false;
		}
		
		if(!Compile.compile()) {
			return false;
		}
		
		return true;
	}
}
