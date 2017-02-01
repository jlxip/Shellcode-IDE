package net.jlxip.shellcodeide;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Arwin extends JDialog {
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField library;
	private JTextField function;
	private JTextField address;

	public Arwin() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		setTitle("Arwin GUI");
		setBounds(100, 100, 537, 284);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblLibrary = new JLabel("Library:");
			lblLibrary.setBounds(12, 16, 44, 16);
			contentPanel.add(lblLibrary);
		}
		
		library = new JTextField();
		library.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				finish();
			}
		});
		library.setBounds(68, 10, 352, 28);
		contentPanel.add(library);
		library.setColumns(10);
		
		JLabel lblFunction = new JLabel("Function:");
		lblFunction.setBounds(12, 57, 53, 16);
		contentPanel.add(lblFunction);
		
		function = new JTextField();
		function.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				finish();
			}
		});
		function.setBounds(78, 51, 441, 28);
		contentPanel.add(function);
		function.setColumns(10);
		
		JButton btnGetAddress = new JButton("GET ADDRESS");
		btnGetAddress.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				finish();
			}
		});
		btnGetAddress.setBounds(12, 92, 507, 110);
		contentPanel.add(btnGetAddress);
		
		JLabel lblAddress = new JLabel("Address:");
		lblAddress.setBounds(12, 217, 56, 16);
		contentPanel.add(lblAddress);
		
		address = new JTextField();
		address.setEditable(false);
		address.setBounds(78, 211, 441, 28);
		contentPanel.add(address);
		address.setColumns(10);
		
		JButton btnKerneldll = new JButton("kernel32.dll");
		btnKerneldll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				library.setText("kernel32.dll");
			}
		});
		btnKerneldll.setBounds(426, 12, 99, 25);
		contentPanel.add(btnKerneldll);
	}
	
	private final String ArwinError = "Error: could find the function in the library!";
	private final Pattern Pspace = Pattern.compile(Pattern.quote(" "));
	private final Pattern P0x = Pattern.compile(Pattern.quote("0x"));
	public void finish() {
		String secondLine = "";
		
		try {
			Process process = new ProcessBuilder("arwin"+File.separator+"arwin.exe", library.getText(), function.getText()).start();
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			
			br.readLine();	// Not necessary
			secondLine = br.readLine();
			
			br.close();
			isr.close();
			is.close();
		} catch (IOException e) {}
		
		if(secondLine.equals(ArwinError)) {
			address.setText(secondLine);
			return;
		}
		
		String rawAddress = Pspace.split(secondLine)[4];
		address.setText(P0x.split(rawAddress)[1] + "h");
	}
}
