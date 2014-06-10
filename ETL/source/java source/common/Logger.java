
package com.dbs.sg.DTE12.common;
/**
 * Imports classes
 */
import org.apache.log4j.Level;
import org.apache.log4j.xml.DOMConfigurator;


public class Logger
{
	private org.apache.log4j.Logger log4jLogger = null;
//	private gcsFactory iGcsFactory;
//	private gcs iGcs;
	
	public static Logger getLogger(String configPath, String arg)
	{
		Logger logger = new Logger();
		LoadConfigXml configXml = LoadConfigXml.getConfig(configPath);
//		System.out.println(configXml.getBasePath());
//		System.out.println(configXml.getJavaLogFile());
		logger.log4jLogger = org.apache.log4j.Logger.getLogger(arg);
		DOMConfigurator.configure(configXml.getJavaLogFile());
		return logger;
	}

	public static Logger getLogger(String configPath,Class className)
	{
		return getLogger(configPath, className.getName());
	}

	public static Logger getLogger(String configPath,Object object)
	{
		return getLogger(configPath,object.getClass().getName());
	}
	
	public void log(Level l, Object message)
	{
		log4jLogger.log(l, message);
	}
	public void log(Level l, Object message, Throwable e)
	{
		log4jLogger.log(l, message, e);
	}	

	public void debug(Object message)
	{
//		System.out.println(message);
		log4jLogger.debug(message);
	}
	public void debug(Object message, Throwable e)
	{
		log4jLogger.debug(message, e);
	}		
	
	public void info(Object message)
	{
//		System.out.println(message);
		log4jLogger.info(message);
	}
	public void info(Object message, Throwable e)
	{
		log4jLogger.info(message, e);
	}	
	
	public void warn(Object message)
	{
		log4jLogger.warn(message);
	}
	public void warn(Object message, Throwable e)
	{
		log4jLogger.warn(message, e);
	}	

	public void error(Object message)
	{
//		System.err.println(message);
		log4jLogger.error(message);
	}
	public void error(Object message, Throwable e)
	{
//		System.err.println(e);
		//e.printStackTrace();
		log4jLogger.error(message, e);
	}
	
	public void fatal(Object message)
	{
		log4jLogger.fatal(message);
	}
	public void fatal(Object message, Throwable e)
	{
		log4jLogger.fatal(message, e);
	}	

	public boolean isDebugEnabled()
	{
		return log4jLogger.isDebugEnabled();
	}
	
	public boolean isInfoEnabled()
	{
		return log4jLogger.isInfoEnabled();
	}
	
}
