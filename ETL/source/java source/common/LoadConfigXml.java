package com.dbs.sg.DTE12.common;

import java.util.HashMap;
import java.util.Map;

import com.dbs.sg.DTE12.config.gcs;
import com.dbs.sg.DTE12.config.gcsFactory;
import com.dbs.sg.DTE12.config.Batchlist.batch;

/**
 * 
 */
public class LoadConfigXml 
{
	public static final String CONFIG_FILE_NAME = "config.xml";
	public static final String file_separator = System.getProperty("file.separator");
	private gcsFactory iGcsFactory;
	private gcs iGcs;
	private batch ibatch;
	private static Map configs = java.util.Collections.synchronizedMap(new HashMap());
	
	public static LoadConfigXml getConfig(String configPath){
		if (configs.containsKey(configPath)){
			return (LoadConfigXml)configs.get(configPath);
		} else {
			LoadConfigXml config = new LoadConfigXml(configPath);
			configs.put(configPath, config);
			return config;
		}
	}
	/**
	 * Logger
	 */
//	private static Logger logger;
			
	public LoadConfigXml(String configPath)
	{
		iGcsFactory = new gcsFactory();
		iGcsFactory.setPackageName("com.dbs.sg.DTE12.config");
    
		// Load the document
		iGcs = (gcs) iGcsFactory.loadDocument("file:" + configPath + file_separator + CONFIG_FILE_NAME);
//		System.err.println("iGcs=" + iGcs);
//		logger = Logger.getLogger(configPath, LoadConfigXml.class);
	}
	
	public batch getBatch(String batchId)
	{
		if (batchId == "" || batchId == null)
		{
//			logger.error("batchId is null.");
			return null;
		}

		for(int i=0;i<iGcs.getBatchlist().getBatchCount();i++)
		{
			ibatch = iGcs.getBatchlist().getBatch(i);
			if(batchId.equalsIgnoreCase(ibatch.getId()))
			{
				return ibatch;
			}
		}
		return null;
	}
	
	public String getJavaLogFile()
	{
		return this.getBasePath()+iGcs.getCommon().getJavaLogFile();
	}	
	
	public String getKeyFile()
	{
		return this.getBasePath()+iGcs.getCommon().getKeyFile();
	}	
	
	public String getEncryptFile()
	{
		return this.getBasePath()+iGcs.getCommon().getEncryptFile();
	}			
	
	public String getConnectionstring()
	{
		return iGcs.getDbConnection().getConnectionstring();
	}
	
	public String getUserid()
	{
		return iGcs.getDbConnection().getUserid();
	}	
	
	public String getDBName()
	{
		return iGcs.getDbConnection().getDBName();
	}		

	public String getPageSize()
	{
		return iGcs.getDbConnection().getPageSize();
	}
	
	public String getXMLSchemal()
	{
		return this.getBasePath()+iGcs.getCommon().getXMLSchemal();
	}	
			
	public String getBasePath()
	{
		return iGcs.getCommon().getBasePath();
	}			

	public String getDataboxSize()
	{
		return iGcs.getCommon().getDataboxSize();
	}
	
	public String[] getOutputXMLTypeList(String batchId) {
		// logger.debug("count :
		// "+getBatch(batchId).getOutputXmlFileList().getFileCount());
		if (getBatch(batchId) != null
				&& getBatch(batchId).getOutputXmlFileList() != null) {
			String[] result = new String[getBatch(batchId)
					.getOutputXmlFileList().getFileCount()];
			for (int i = 0; i < this.getBatch(batchId).getOutputXmlFileList()
					.getFileCount(); i++) {
				// logger.debug("i : "+i);
				// logger.debug("type :
				// "+this.getBatch(batchId).getOutputXmlFileList().getFile(i).getType());
				result[i] = this.getBatch(batchId).getOutputXmlFileList()
						.getFile(i).getType();
			}
			return result;
		}
		return new String[] {};
	}	
	
	public String[] getOutputXMLFileNameList(String batchId)
	{
		String[] result = new String[getBatch(batchId).getOutputXmlFileList().getFileCount()];
		for (int i=0;i<this.getBatch(batchId).getOutputXmlFileList().getFileCount();i++)
		{
			result[i] = this.getBasePath()+this.getBatch(batchId).getOutputXmlFileList().getFile(i).getFileName();			
		}		
		return result;
	}					
	
	public String getVersionNo(String batchId,String xmlType)
	{
		String result = null;	
		for (int i=0;i<this.getBatch(batchId).getOutputXmlFileList().getFileCount();i++)
		{
			if (xmlType.equals(this.getBatch(batchId).getOutputXmlFileList().getFile(i).getType()))
			{
				result = this.getBatch(batchId).getOutputXmlFileList().getFile(i).getVersionNO();
			}			
		}	
		if (result == null)
		{
//			logger.error("batchId : "+batchId+"; xmlType :"+xmlType+"; can not be found this Version No,please check.");
		}
		return result;
	}
}
