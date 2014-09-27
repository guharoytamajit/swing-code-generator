package com.tamajit;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Method;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public class Main {
	/** Runs a sample program that shows dropped files */
	public static void main(String[] args) {
		javax.swing.JFrame frame = new javax.swing.JFrame("FileDrop");
		// javax.swing.border.TitledBorder dragBorder = new
		// javax.swing.border.TitledBorder( "Drop 'em" );
		final javax.swing.JTextArea text = new javax.swing.JTextArea();
		frame.getContentPane().add(new javax.swing.JScrollPane(text),
				java.awt.BorderLayout.CENTER);

		new FileDrop(System.out, text, /* dragBorder, */
		new FileDrop.Listener() {
			public void filesDropped(java.io.File[] files) {
				for (int i = 0; i < files.length; i++) {
					try {
						 text.append(files[i].getCanonicalPath() + "\n");
						compile(files[i].getCanonicalPath());
						String generatedCode = generate(files[i].getName());
						text.append(generatedCode+ "\n");
					} // end try
					catch (Exception e) {
						e.printStackTrace();
					}
				} // end for: through each dropped file
			} // end filesDropped
		}); // end FileDrop.Listener

		frame.setBounds(100, 100, 300, 400);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	} // end main

	public static void compile(String fileName) {
		System.setProperty("java.home",
				"C:\\Program Files (x86)\\Java\\jdk1.7.0_45");
		JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
		javaCompiler.run(null, System.out, System.err, "-d", "bin", fileName);
	}

	public static String generate(String filename) throws Exception {
		String classname = filename.substring(0,filename.indexOf("."));
		Class hello = Class.forName(classname);
		Method[] methods = hello.getMethods();

		VelocityEngine ve = new VelocityEngine();
		Template t = ve.getTemplate("a.vm");

		VelocityContext c = new VelocityContext();
		c.put("methods", methods);
		StringWriter sw = new StringWriter();
		// FileWriter fw = new FileWriter("Hello.java");
		 t.merge(c, sw);
		// fw.flush();
		// fw.close();
		return sw.toString();
	}
}
