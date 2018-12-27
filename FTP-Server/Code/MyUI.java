package FTPServer;

import java.awt.event.*;
import java.io.File;
import javax.swing.*;

public class MyUI extends JFrame implements ActionListener{
	JButton open = null;
	public String path = null;
	public String fileName = null;
	
	public MyUI() {
		open = new JButton("open");
		this.add(open);
	    this.setBounds(800, 400, 200, 200);
	    this.setVisible(true);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    open.addActionListener(this);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void actionPerformed(ActionEvent event) {
		JFileChooser jfc=new JFileChooser();
	    jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );
	    jfc.showDialog(new JLabel(), "选择");
	    File file=jfc.getSelectedFile();
	    if(file.isDirectory()){
	      System.out.println("文件夹:"+file.getAbsolutePath());
	    }else if(file.isFile()){
	      System.out.println("文件:"+file.getAbsolutePath());
	    }
	    path = file.getAbsolutePath();
	    fileName = file.getName();
	 }
	
	public String getStr() {
		return path;
	}
	
}
