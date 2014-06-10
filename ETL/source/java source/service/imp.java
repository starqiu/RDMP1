package com.dbs.sg.DTE12.service;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dbs.sg.DTE12.common.AESManager;
import com.dbs.sg.DTE12.common.LoadConfigXml;
import com.dbs.sg.DTE12.common.Logger;
import com.dbs.sg.DTE12.common.StreamGobbler;

/**
 * @author yangzsj
 * 
 */
public class imp {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("1");
			System.exit(1);
		}
		String configPath = args[0];

		LoadConfigXml configXml = LoadConfigXml.getConfig(configPath);
		AESManager aes = new AESManager(configPath);
		String pwd = aes.Descrypt(configXml.getKeyFile(), configXml
				.getEncryptFile());
		Logger logger = Logger.getLogger(configPath, imp.class);

		try {
			logger.info("Start to run imp with parameters:"
					+ Arrays.asList(args));

			List plist = new ArrayList();
			plist.add("imp");
			plist.add(configXml.getUserid() + "@" + configXml.getDBName());
			for (int j = 1; j < args.length; j++) {
				plist.add(args[j]);
			}
			String[] params = new String[plist.size()];
			for (int i = 0; i < plist.size(); i++) {
				params[i] = (String) plist.get(i);
			}

			Process proc = Runtime.getRuntime().exec(params);
			logger.debug("Call imp with parameters:" + plist);

			PrintWriter pr = new PrintWriter(proc.getOutputStream(), true);
			logger.debug("Call imp ok.");

			pr.println(pwd);
			logger.debug("put pwd ok.");

			StreamGobbler err = new StreamGobbler(configPath, proc, proc
					.getErrorStream(), "err");
			StreamGobbler input = new StreamGobbler(configPath, proc, proc
					.getInputStream(), "input");
			input.start();
			err.start();

			// BufferedReader br = new BufferedReader(new InputStreamReader(proc
			// .getInputStream()));
			// String line = null;
			// while ((line = br.readLine()) != null)
			// logger.debug("imp>" + line);
			int result = proc.waitFor();
			logger.info("imp>result:" + result);
			if (result != 0) {
				logger
						.error("Run imp failed with return code[" + result
								+ "].");
				System.out.println("1");
				System.exit(1);
			}

			logger.info("Run imp completely with parameters:"
					+ Arrays.asList(args));
			System.out.println("0");
		} catch (Exception e) {
			// e.printStackTrace();
			logger.error("Run imp failed.", e);
			System.out.println("1");
			System.exit(1);
		}

	}

}
