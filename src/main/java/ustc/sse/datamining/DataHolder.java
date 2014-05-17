package ustc.sse.datamining;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class used to hold the data instead of get data from the database each
 * time.
 * 
 * @author mike
 * 
 */
public class DataHolder {

    static Logger         logger     = LogManager.getLogger(DataHolder.class.getName());

    static List<DataItem> dataItems  = new LinkedList<DataItem>();

    static Connection     connection = null;

    static {
        try {
            connection = DriverManager.getConnection(MYSQLCONFIG.DBURL,
                                                     MYSQLCONFIG.USRNAME,
                                                     MYSQLCONFIG.PASSWORD);
            if (connection != null) {
                Statement statement = connection.createStatement();
                /*ResultSet resultSet = statement.executeQuery("SELECT * FROM "
                                                                             + "tmailcontest.tmail_firstseason "
                                                                             + "where visit_datetime "
                                                                             + "not like '2013-08%%%' "
                                                                             + "and visit_datetime "
                                                                             + "not like '2013-07%%%';");*/
                ResultSet resultSet = statement.executeQuery("SELECT * FROM "
                                                             + "tmailcontest.tmail_firstseason "
                                                             + "where visit_datetime "
                                                             + " < '2013-08-15' "
                                                             + "and visit_datetime > '2013-06-01';");
                int num = 0;
                while (resultSet.next()) {
                    ++num;
                    int userid = resultSet.getInt("user_id");
                    int brandid = resultSet.getInt("brand_id");
                    int type = resultSet.getInt("type");

                    Matcher matcher = Pattern.compile("(.*)-(.*)-(.*)").matcher(resultSet.getString("visit_datetime"));
                    Calendar calendar = Calendar.getInstance();

                    if (matcher.find()) {
                        calendar.set(Integer.valueOf(matcher.group(1)),
                                     Integer.valueOf(matcher.group(2)),
                                     Integer.valueOf(matcher.group(3)));
                    }

                    DataItem dataItem = new DataItem();
                    dataItem.setBrandid(brandid);
                    dataItem.setUserid(userid);
                    dataItem.setDate(calendar.getTime());
                    dataItem.setType(type);

                    dataItems.add(dataItem);
                }
                logger.info("Data Loaded Success!" + "SUM: " + num);
            }

            connection.close();
        } catch (SQLException e) {
            logger.warn(e.getMessage());
        }
    }

    /**
     * get the data each event, which has been classfied by the userid
     * 
     * @param userid
     *            userid
     * @return list\<DataItem\>
     */
    public static LinkedList<DataItem> getDataByUserID(int userid) {
        LinkedList<DataItem> items = new LinkedList<DataItem>();
        for (DataItem iterable : dataItems) {
            if (iterable.getUserid() == userid) {
                items.add(iterable);
            }
        }
        return items;
    }

    /**
     * get the data each event, which has been classfied by the userid
     * 
     * @param brandid
     *            brandid
     * @return list\<DataItem\>
     */
    public static LinkedList<DataItem> getDataByBrandID(int brandid) {
        LinkedList<DataItem> items = new LinkedList<DataItem>();
        for (DataItem iterable : dataItems) {
            if (iterable.getBrandid() == brandid) {
                items.add(iterable);
            }
        }
        return items;
    }

    /**
     * Get all user id
     * 
     * @return int[]
     */
    public static Integer[] getUserID() {
        Integer[] users = new Integer[10];
        HashSet<Integer> usersMap = new HashSet<Integer>();
        for (DataItem item : dataItems) {
            usersMap.add(item.getUserid());
        }
        return usersMap.toArray(users);
    }

    /**
     * Get all brand id
     * 
     * @return int[]
     */
    public static Integer[] getBrandID() {
        Integer[] brands = new Integer[10];
        HashSet<Integer> brandsMap = new HashSet<Integer>();
        for (DataItem item : dataItems) {
            brandsMap.add(item.getBrandid());
        }
        return brandsMap.toArray(brands);
    }

}
