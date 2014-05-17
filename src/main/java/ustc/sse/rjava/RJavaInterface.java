/*
 * ============================================================
 * The SSE USTC Software License
 * 
 * RJavaInterface.java
 * 2014-3-3
 * 
 * Copyright (c) 2006 China Payment and Remittance Service Co.,Ltd        
 * All rights reserved.
 * ============================================================
 */
package ustc.sse.rjava;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rosuda.JRI.RMainLoopCallbacks;
import org.rosuda.JRI.Rengine;

/**
 * 实现功能： R与Java的接口,提供Rengine对象<br />
 * <p>
 * date	    author            email		           notes<br />
 * --------	---------------------------	---------------<br />
 * 2014-3-3	 邱星            starqiu@mail.ustc.edu.cn	      新建类<br /></p>
 *
 */
public  class RJavaInterface implements RMainLoopCallbacks{
	
	private Log log = LogFactory.getLog(RJavaInterface.class);
	public Rengine re ;
	//= new Rengine(new String[] { "--vanilla" }, false, new RJavaInterface());

	public RJavaInterface() {
		super();
	}
	
	public void callRJava() {
        if (!re.waitForR()) {
        	log.error("Can not load R!");
        }
        /*String path = re.jriChooseFile(0);
        String cmd = "source('"+path+"')";
        String rv = re.eval(cmd).asString();
        System.out.println(rv);
        String cmd2="area(10)";
        REXP res = re.eval(cmd2);
        System.out.println(res.asDouble());
        res = re.eval("a");
        for (int b : res.asIntArray()) {
			System.out.println(b);
		}*/
        re.end();
    }

	public String rReadConsole(Rengine re, String prompt, int addToHistory) {
		log.info(prompt);
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			String s = br.readLine();
			return (s == null || s.length() == 0) ? s : s + "\n";
		} catch (Exception e) {
			log.info("jriReadConsole exception: " + e.getMessage());
		}
		return null;
	}
	public String rChooseFile(Rengine re, int newFile) {
		FileDialog fd = new FileDialog(new Frame(),
				(newFile == 0) ? "Select a file" : "Select a new file",
						(newFile == 0) ? FileDialog.LOAD : FileDialog.SAVE);
		fd.setVisible(true);
		String res = null;
		if (fd.getDirectory() != null)
			res = fd.getDirectory();
		if (fd.getFile() != null)
			res = (res == null) ? fd.getFile() : (res + fd.getFile());
			return res;
	}
	
	public void rWriteConsole(Rengine re, String text, int oType) {
		log.info(text);
	}
	
	public void rBusy(Rengine re, int which) {
		log.info("rBusy(" + which + ")\n");
	}
	
	public void rShowMessage(Rengine re, String message) {
		log.info("rShowMessage(" + message + ")\n");
	}
	
	public void rFlushConsole(Rengine re) {
	}
	public void rSaveHistory(Rengine re, String filename) {
	}
	public void rLoadHistory(Rengine re, String filename) {
	}
}

