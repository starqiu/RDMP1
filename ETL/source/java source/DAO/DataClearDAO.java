package com.dbs.sg.DTE12.DAO;

import java.sql.SQLException;

import com.dbs.sg.DTE12.config.TableList;
import com.dbs.sg.DTE12.config.TargetTableList;

public class DataClearDAO extends BaseTableDAO {

	public static final String COMMERCIAL = "COMMERCIAL";
	public static final String FACILITY = "FACILITY";
	public static final String OTC = "OTC";
	public static final String CURRENCYACA = "CURRENCYACA";
	public static final String CURRENCYACLM = "CURRENCYACLM";
	public static final String COUNTERPARTY = "COUNTERPARTY";
	public static final String FINANCIALSTATEMENT = "FINANCIALSTATEMENT";
	public static final String FINANCIALSTATEMENTRC = "FINANCIALSTATEMENTRC";
	public static final String MITIGANT = "MITIGANT";

	/**
	 * @throws SQLException
	 */
	public DataClearDAO(String configPath) throws SQLException {
		super(configPath);
	}

	/**
	 * @throws SQLException
	 */
	public DataClearDAO(String configPath, boolean isAutoCommit)
			throws SQLException {
		super(configPath, isAutoCommit);
	}
	/**
	 * Initialize DataBase
	 */
	public void initializeDataBase(TableList tableList) throws SQLException {
		initialize();
		StringBuffer SQLStatement = null;
		String tempSql = "TRUNCATE TABLE ";
		try {
			for (int i = 0; i < tableList.getTableNameCount(); i++) {
				SQLStatement = new StringBuffer();
				SQLStatement.append(tempSql).append(tableList.getTableName(i));
				prepareStatement(SQLStatement.toString());
				logger.info("Executing "+SQLStatement.toString());
				executeStatement();
			}
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new SQLException(e.getMessage());
		} finally {
			cleanUp();
		}
	}

	public void deleteCommData(TargetTableList targetTableList) throws SQLException {
		initialize();
		StringBuffer SQLStatement = null;
		String tempSql = "DELETE FROM ";

		try {
			for (int i = 0; i < targetTableList.getTargetTableCount(); i++) {
				SQLStatement = new StringBuffer();
				SQLStatement.append(tempSql).append(targetTableList.getTargetTable(i).getName()+
						" WHERE "+ targetTableList.getTargetTable(i).getWhere());
				prepareStatement(SQLStatement.toString());
				logger.info("Executing "+SQLStatement.toString());
				executeStatement();
			}
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new SQLException(e.getMessage());
		} finally {
			cleanUp();
		}
	}



	/*public void deleteCommData(String[] xmlType, String batchId,
			CommonTableCondition ctc) throws SQLException {
		initialize();
		String SQLStatement = null;
		List sqlStr = new ArrayList();


		 // String[] xmlType = null; configXmlHelper = new LoadConfigXml(); if
		 // (configXmlHelper.getBatch(batchId).getOutputXmlFileList() == null) {
		 // return; } xmlType = configXmlHelper.getOutputXMLTypeList(batchId);

		for (int i = 0; i < xmlType.length; i++) {
			if (ctc != null) {
				if (ctc.getWhereForReport() != null)
					sqlStr.add("DELETE FROM TBLGCS_BATCH_REPORT WHERE "
							+ ctc.getWhereForReport());
				else {
					logger.error("confirgure item whereForReport is null!!! : "
							+ batchId + ",please check!");
					return;
				}

				if (ctc.getWhereForDatabox() == null) {
					logger
							.error("confirgure item whereForDatabox is null!!! : "
									+ batchId + ",please check!");
					return;
				}
				if (xmlType[i] == null || xmlType[i].trim() == "") {
					logger.error("XML TYPE is null!!! : " + xmlType[i]
							+ ",please check!");
					return;
				} else if (COMMERCIAL.equals(xmlType[i])) {
					sqlStr.add("DELETE FROM TBLCOMM_EVENT WHERE "
							+ ctc.getWhereForDatabox());
					sqlStr.add("DELETE FROM TBLCOMM_STRUCTURE WHERE "
							+ ctc.getWhereForDatabox());
					sqlStr.add("DELETE FROM TBLCOMM_ROLE WHERE "
							+ ctc.getWhereForDatabox());
					sqlStr.add("DELETE FROM TBLCOMMERCIAL WHERE "
							+ ctc.getWhereForDatabox());

				} else if (OTC.equals(xmlType[i])) {
					sqlStr.add("DELETE FROM TBLOTC_FREE_FORMAT_EVENT WHERE "
							+ ctc.getWhereForDatabox());
					sqlStr.add("DELETE FROM TBLOTC_FREE_FORMAT_ROLE WHERE "
							+ ctc.getWhereForDatabox());
					sqlStr
							.add("DELETE FROM TBLOTC_FREE_FORMAT_STRUCTURE WHERE "
									+ ctc.getWhereForDatabox());
					sqlStr.add("DELETE FROM TBLOTC_FREE_FORMAT WHERE "
							+ ctc.getWhereForDatabox());
				} else if (CURRENCYACA.equals(xmlType[i])
						|| CURRENCYACLM.equalsIgnoreCase(xmlType[i])) {
					sqlStr.add("DELETE FROM TBLCURRENCY WHERE "
							+ ctc.getWhereForDatabox());
					sqlStr.add("DELETE FROM TBLCURRENCY_RATE WHERE "
							+ ctc.getWhereForDatabox());
				} else if (COUNTERPARTY.equals(xmlType[i])) {
					sqlStr.add("DELETE FROM TBLCOUNTERPARTY WHERE "
							+ ctc.getWhereForDatabox());
					sqlStr.add("DELETE FROM TBLCOUNTERPARTY_UDF WHERE "
							+ ctc.getWhereForDatabox());
					sqlStr.add("DELETE FROM tblcounterparty_aliases WHERE "
							+ ctc.getWhereForDatabox());
					sqlStr.add("DELETE FROM tblcounterparty_industry WHERE "
							+ ctc.getWhereForDatabox());
					sqlStr.add("DELETE FROM tblcounterparty_managers WHERE "
							+ ctc.getWhereForDatabox());
					sqlStr.add("DELETE FROM tblcounterparty_structure WHERE "
							+ ctc.getWhereForDatabox());
					sqlStr
							.add("DELETE FROM tblcounterparty_registration WHERE "
									+ ctc.getWhereForDatabox());
				} else if (FACILITY.equals(xmlType[i])) {
					// getSeqNoSqlstr = "SELECT SEQNO FROM TBLFACILITY WHERE
					// APP_ID=FUNC.GETPARA_APP_ID(?)";
					sqlStr.add("DELETE FROM TBLACAFACILITY WHERE "
							+ ctc.getWhereForDatabox());
					sqlStr.add("DELETE FROM TBLACAFACILITY_EVENTS WHERE "
							+ ctc.getWhereForDatabox());
					// Added by Alex on 20091223 for tables:
					// tblFacility_Aliases, tblFacility_Risks_Ons,
					// tblFacility_Roles, tblFacility_Takers
					sqlStr.add("DELETE FROM TBLACAFACILITY_ALIAS WHERE "
							+ ctc.getWhereForDatabox());
					sqlStr.add("DELETE FROM TBLACAFACILITY_RISKS WHERE "
							+ ctc.getWhereForDatabox());
					sqlStr.add("DELETE FROM TBLACAFACILITY_ROLES WHERE "
							+ ctc.getWhereForDatabox());
					sqlStr.add("DELETE FROM TBLACAFACILITY_TAKERS WHERE "
							+ ctc.getWhereForDatabox());
				} else if (FINANCIALSTATEMENT.equals(xmlType[i])) {
					sqlStr.add("DELETE FROM TBLFINANCIAL WHERE "
							+ ctc.getWhereForDatabox());
					sqlStr.add("DELETE FROM TBLFINANCIAL_VALUE WHERE "
							+ ctc.getWhereForDatabox());
				} // ADD BY Jason 20071218
				else if (FINANCIALSTATEMENTRC.equals(xmlType[i])) {
					sqlStr.add("DELETE FROM TBLFINANCIAL_RC WHERE "
							+ ctc.getWhereForDatabox());
					sqlStr.add("DELETE FROM TBLFINANCIAL_RC_VALUE WHERE "
							+ ctc.getWhereForDatabox());
				} else if (MITIGANT.equals(xmlType[i])) {
					sqlStr
							.add("DELETE FROM TBLMITIGANT_MARKETVALUATION TA WHERE "
									+ ctc.getWhereForDatabox());
					sqlStr.add("DELETE FROM TBLMITIGANT_UDF TA WHERE "
							+ ctc.getWhereForDatabox());
					sqlStr.add("DELETE FROM TBLMITIGANT WHERE "
							+ ctc.getWhereForDatabox());
				}
			} else {
				sqlStr.add("DELETE FROM TBLGCS_BATCH_REPORT WHERE BATCHID = ?");

				if (xmlType[i] == null || xmlType[i].trim() == "") {
					logger.error("XML TYPE is null!!! : " + xmlType[i]
							+ ",please check!");
					return;
				} else if (COMMERCIAL.equals(xmlType[i])) {
					// getSeqNoSqlstr = "SELECT SEQNO FROM TBLCOMMERCIAL WHERE
					// BAAPID=FUNC.GETPARA_APP_ID(?)";
					sqlStr
							.add("DELETE FROM TBLCOMM_EVENT WHERE BAAPID=FUNC.GETPARA_APP_ID(?)");
					sqlStr
							.add("DELETE FROM TBLCOMM_STRUCTURE WHERE BAAPID=FUNC.GETPARA_APP_ID(?)");
					sqlStr
							.add("DELETE FROM TBLCOMM_ROLE WHERE BAAPID=FUNC.GETPARA_APP_ID(?)");
					sqlStr
							.add("DELETE FROM TBLCOMMERCIAL WHERE BAAPID=FUNC.GETPARA_APP_ID(?)");

				} else if (OTC.equals(xmlType[i])) {
					// getSeqNoSqlstr = "SELECT TBLOTC_FREE_FORMAT.SEQNO FROM
					// TBLOTC_FREE_FORMAT WHERE
					// TBLOTC_FREE_FORMAT.BAAPID=FUNC.GETPARA_APP_ID(?)";
					sqlStr
							.add("DELETE FROM TBLOTC_FREE_FORMAT_EVENT WHERE BAAPID = FUNC.GETPARA_APP_ID(?)");
					sqlStr
							.add("DELETE FROM TBLOTC_FREE_FORMAT_ROLE WHERE BAAPID = FUNC.GETPARA_APP_ID(?)");
					sqlStr
							.add("DELETE FROM TBLOTC_FREE_FORMAT_STRUCTURE WHERE BAAPID = FUNC.GETPARA_APP_ID(?)");
					sqlStr
							.add("DELETE FROM TBLOTC_FREE_FORMAT WHERE BAAPID = FUNC.GETPARA_APP_ID(?)");
				} else if (CURRENCYACA.equals(xmlType[i])
						|| CURRENCYACLM.equalsIgnoreCase(xmlType[i])) {
					sqlStr.add("DELETE FROM TBLCURRENCY WHERE ? IS NOT NULL");
					sqlStr
							.add("DELETE FROM TBLCURRENCY_RATE WHERE ? IS NOT NULL");
				} else if (COUNTERPARTY.equals(xmlType[i])) {
					sqlStr
							.add("DELETE FROM TBLCOUNTERPARTY WHERE ? IS NOT NULL");
					sqlStr
							.add("DELETE FROM TBLCOUNTERPARTY_UDF WHERE ? IS NOT NULL");
					sqlStr
							.add("DELETE FROM tblcounterparty_aliases WHERE ? IS NOT NULL");
					sqlStr
							.add("DELETE FROM tblcounterparty_industry WHERE ? IS NOT NULL");
					sqlStr
							.add("DELETE FROM tblcounterparty_managers WHERE ? IS NOT NULL");
					sqlStr
							.add("DELETE FROM tblcounterparty_structure WHERE ? IS NOT NULL");
					sqlStr
							.add("DELETE FROM tblcounterparty_registration WHERE ? IS NOT NULL");
				} else if (FACILITY.equals(xmlType[i])) {
					// getSeqNoSqlstr = "SELECT SEQNO FROM TBLFACILITY WHERE
					// APP_ID=FUNC.GETPARA_APP_ID(?)";
					sqlStr.add("DELETE FROM TBLACAFACILITY WHERE CTYPE=?");
					sqlStr
							.add("DELETE FROM TBLACAFACILITY_EVENTS WHERE CTYPE=?");
					// Added by Alex on 20091223 for tables:
					// tblFacility_Aliases, tblFacility_Risks_Ons,
					// tblFacility_Roles, tblFacility_Takers
					sqlStr
							.add("DELETE FROM TBLACAFACILITY_ALIAS WHERE CTYPE=?");
					sqlStr
							.add("DELETE FROM TBLACAFACILITY_RISKS WHERE CTYPE=?");
					sqlStr
							.add("DELETE FROM TBLACAFACILITY_ROLES WHERE CTYPE=?");
					sqlStr
							.add("DELETE FROM TBLACAFACILITY_TAKERS WHERE CTYPE=?");
				} else if (FINANCIALSTATEMENT.equals(xmlType[i])) {
					sqlStr.add("DELETE FROM TBLFINANCIAL WHERE ? IS NOT NULL");
					sqlStr
							.add("DELETE FROM TBLFINANCIAL_VALUE WHERE ? IS NOT NULL");
				} // ADD BY Jason 20071218
				else if (FINANCIALSTATEMENTRC.equals(xmlType[i])) {
					sqlStr
							.add("DELETE FROM TBLFINANCIAL_RC WHERE ? IS NOT NULL");
					sqlStr
							.add("DELETE FROM TBLFINANCIAL_RC_VALUE WHERE ? IS NOT NULL");
				} else if (MITIGANT.equals(xmlType[i])) {
					sqlStr
							.add("DELETE FROM TBLMITIGANT_MARKETVALUATION TA WHERE 1 <= (SELECT COUNT(*) FROM TBLMITIGANT TB WHERE TA.SEQNO=TB.SEQNO AND TB.APPID=FUNC.GETPARA_APP_ID(?))");
					sqlStr
							.add("DELETE FROM TBLMITIGANT_UDF TA WHERE 1 <= (SELECT COUNT(*) FROM TBLMITIGANT TB WHERE TA.SEQNO=TB.SEQNO AND TB.APPID=FUNC.GETPARA_APP_ID(?))");
					sqlStr
							.add("DELETE FROM TBLMITIGANT WHERE APPID = FUNC.GETPARA_APP_ID(?)");
				} else {
					// logger.error("Error XML TYPE : "+xmlType[i]+",please
					// check!");
					// throw new SQLException("Error XML TYPE :
					// "+xmlType[i]+",please check!");
				}
			}
		}
		try {
			for (int i = 0; i < sqlStr.size(); i++) {
				SQLStatement = sqlStr.get(i).toString();
				prepareStatement(SQLStatement);

				  // modify by Jason 20070713 if
				 // (SQLStatement.indexOf("TBLGCS_BATCH_REPORT") > 0)
				 // setStringArgument(1, batchId); else

				setStringArgument(1, batchId);
				executeStatement();
				super.commit();
			}
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new SQLException(e.getMessage());
		} finally {
			cleanUp();
		}
	}*/

}
