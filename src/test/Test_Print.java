package test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JApplet;
import javax.swing.JTextArea;

public class Test_Print extends JApplet {
	
  int ipt;

	@Override
	public void init() {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		pw.println("testing 1");
		pw.println("testing 2");
		pw.println("testing 3");
		pw.format("%4$2s %3$2s %2$2s %1$2s\n", "a", "b", "c", "d"); // ->
																																// " d  c  b  a"
		pw.format("e = %+10.4f\n", Math.E); // -> "e =    +2.7183"
		pw.format("$%(,.2f\n", -6217.581); // -> "$(6,217.58)"
		pw.close();

		JTextArea ja = new JTextArea(30, 60);
		ja.setText(sw.toString());
		add(ja);

		// also with a PrintStream
		
		try {
			File file = new File("test.txt");
			System.out.println(file.getAbsolutePath());
			PrintStream ps = new PrintStream(new FileOutputStream(file));
			ps.println(file.getAbsolutePath());
			ps.format("%4$2s %3$2s %2$2s %1$2s\n", "a", "b", "c", "d"); // -> " d  c  b  a"
			ps.format("e = %+10.4f\n", Math.E); // -> "e =    +2.7183"
			ps.format("$%(,.2f\n", -6217.581); // -> "$(6,217.58)"
			ps.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

} 