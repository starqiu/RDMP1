package com.dbs.sg.DTE12.service;

import java.sql.SQLException;

import com.dbs.sg.DTE12.DAO.DataClearDAO;
import com.dbs.sg.DTE12.common.LoadConfigXml;
import com.dbs.sg.DTE12.common.Logger;
import com.dbs.sg.DTE12.config.TableList;
import com.dbs.sg.DTE12.config.TargetTableList;

public class DatabaseInitialize {
	/**
	 * Logger
	 */
	private static Logger logger;

	private void initialize(String configPath, String batchId)
			throws SQLException {
		LoadConfigXml configXmlHelper = LoadConfigXml.getConfig(configPath);
		DataClearDAO dataClearDAO = new DataClearDAO(configPath,false);
		// get table list for the special batch
		TableList tableList = configXmlHelper.getBatch(batchId).getTableList();
		TargetTableList trgtList = configXmlHelper.getBatch(batchId).getTargetTableList();
		//String[] xmlType = configXmlHelper.getOutputXMLTypeList(batchId);
		//CommonTableCondition ctc = configXmlHelper.getBatch(batchId).getCommonTableCondition();

		try {
			// truncate temp tables for the special batch
			dataClearDAO.initializeDataBase(tableList);
			// delete data from databox table.
			dataClearDAO.deleteCommData(trgtList);
			// commit transaction.
			dataClearDAO.commit();
		} catch (SQLException e) {
			logger.error("Error occured while initialize.", e);
			if (dataClearDAO != null)
				dataClearDAO.rollback();
			throw new SQLException(e.getMessage());
		} finally {
			if (dataClearDAO != null)
				dataClearDAO.close();
		}
	}

	/***************************************************************************
	 * PARAMETER 1: String: configPath PARAMETER 2: String: batchId
	 **************************************************************************/
	public static void main(String[] args) {
		String batchId = null;
		String configPath = null;
		// TableList tableList = null;
		DatabaseInitialize databaseInitialize = null;
		// check paramter,if param is null, out print 1 and return;
		if (args.length < 2) {
//			logger
//					.error("configPath and BatchId can not be null,need input paramters");
			System.out.println("-1");
			return;
		} else {
			configPath = args[0];
			batchId = args[1];
		}
		logger = Logger.getLogger(args[0], DatabaseInitialize.class);
		logger.info("configPath: " + configPath + ",BatchId: " + batchId
				+ ",DataBase initialize start.");
		try {
			databaseInitialize = new DatabaseInitialize();
			databaseInitialize.initialize(configPath, batchId);
			logger.info("configPath: " + configPath + ",BatchId: " + batchId
					+ ",DataBase initialize successful.");
			System.out.println("0");
		} catch (NullPointerException e) {
			logger.error("configPath: " + configPath + ",BatchId: " + batchId
					+ ",NullPointerException ?.", e);
			logger.error("configPath: " + configPath + ",BatchId: " + batchId
					+ " can not been found,DataBase initialize fail.");
			System.out.println("1");
		} catch (SQLException e) {
			logger.error("configPath: " + configPath + ",BatchId: " + batchId
					+ ",SQLException ?.", e);
			System.out.println("1");
		}
	}
}
