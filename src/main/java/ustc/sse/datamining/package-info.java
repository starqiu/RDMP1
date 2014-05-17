/**
 * @author mike
 * 
 *         this is just a config file
 */
package ustc.sse.datamining;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

class MYSQLCONFIG {

	// there are some configuration about mysql
	public static final java.lang.String USRNAME = "root";
	public static final java.lang.String PASSWORD = "";
	public static final java.lang.String DBURL = "jdbc:mysql://127.0.0.1:3306/tmailcontest";
	public static final java.lang.String DRIVER = "com.mysql.jdbc.Driver";
	// ----------------------------------------------------------------------

	public static final java.lang.String DBNAME = "tmailcontest";
	public static final java.lang.String TABLENAME = "tmail_firstseason";

	// ----------------------------------------------------------------------
	public static final java.lang.String isOnlypurchaseAction = " and type=1";
	public static final java.lang.String isClickAction = " and type=0";
	public static final java.lang.String isOnlyClickAction = " and (type=0 or type=2) ";
	public static final java.lang.String isFavoriteAction = " and type=2";
	public static final java.lang.String isShopCartAction = " and type=3";
	public static final java.lang.String isBuyOrCartAction = " and (type=1 or type=3)";
	public static final java.lang.String isAllAction = "";

	// ----------------------------------------------------------------------

	public static final DateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd");
	public static final java.lang.String DateBegin = "2013-04-15";
	public static final java.lang.String DateMiddle = "2013-08-15";
	public static final java.lang.String DateEnd = "2013-08-15";

	// ----------------------------------------------------------------------

	public static final java.lang.String DatelRPredictBegin = "2013-05-15";
	public static final java.lang.String DatelRTrainEnd = "2013-07-15";
	public static final java.lang.String DateThreshold = "2013-06-15";

	// ----------------------------------------------------------------------

	public static final int SVDPlusPlusRec = 1;
	public static final int PearsonCorrelationRec = 2;

	// ----------------------------------------------------------------------

	// public static final int OnlyClickETLModel = 0;
	public static final int OnlyClickItems = 1;
	public static final int isOnlyFavorItems = 2;
	public static final int isHighRate = 3;
}

class ETLCONFIG {
	// there are config about the file
	public static final boolean ISDEBUGMODEL = true;

	public static final java.lang.String SQLFILEPATH = "sql"
			+ java.io.File.separator + "cnpc2013.sql";
	public static final java.lang.String TMPPATH = "lane_tmp"
			+ java.io.File.separator;
	public static final java.lang.String RESULT = "lane_result"
			+ java.io.File.separator;
	public static final java.lang.String RESOURCE = "resource"
			+ java.io.File.separator;
	public static final java.lang.String BRANDS_FILE = "result"
			+ java.io.File.separator + "brands";
	public static final java.lang.String USERS_FILE = "result"
			+ java.io.File.separator + "users";
	public static final java.lang.String PREFERENCE_FILE = "result"
			+ java.io.File.separator + "preference";
}
