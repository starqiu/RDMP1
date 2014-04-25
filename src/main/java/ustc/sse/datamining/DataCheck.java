package ustc.sse.datamining;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DataCheck {

    static Logger                             logger     = LogManager.getLogger(DataCheck.class);

    static HashSet<DataItem>                  dataItems  = new HashSet<DataItem>();

    static Connection                         connection = null;

    static {
        try {
            connection = DriverManager.getConnection(MYSQLCONFIG.DBURL,
                                                     MYSQLCONFIG.USRNAME,
                                                     MYSQLCONFIG.PASSWORD);
            if (connection != null) {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM "
                                                             + "tmailcontest.tmail_firstseason "
                                                             + "where visit_datetime "
                                                             + "like '2013-07%%%' "
                                                             + "and type = 1 ");

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
            // SQL connection failed
            logger.warn(e.getMessage());
        }
    }

    static HashMap<Integer, HashSet<Integer>> realmap    = new HashMap<Integer, HashSet<Integer>>();
    static HashMap<Integer, HashSet<Integer>> predictmap = new HashMap<Integer, HashSet<Integer>>();

    public static void main(String[] args) {
        for (DataItem item : dataItems) {
            if (realmap.containsKey(item.getUserid())) {
                if (realmap.get(item.getUserid()).contains(item.getBrandid())) {
                    continue;
                } else {
                    realmap.get(item.getUserid()).add(item.getBrandid());
                }
            } else {
                HashSet<Integer> goodset = new HashSet<Integer>();
                goodset.add(item.getBrandid());
                realmap.put(item.getUserid(), goodset);
            }
        }
        DataAnalyze1.write2File(new File("mike_tmp/real"), realmap.toString());
        // -----------------------------------------------------------------------------------
        predictmap = DataAnalyze1.findBuyedBrandsByUserID();
        // -----------------------------------------------------------------------------------
        DataAnalyze1.write2File(new File("mike_tmp/predict"), predictmap.toString());

        double precision = calculatePrecision(realmap, predictmap);
        double recall = calculateRecall(realmap, predictmap);
        double f1 = calculateF1(precision, recall);

        System.out.println("recall : " + recall * 100 + "%");
        System.out.println("precision : " + precision * 100 + "%");
        System.out.println("f1 : " + f1 * 100 + "%");
    }

    public static double calculatePrecision(HashMap<Integer, HashSet<Integer>> real,
                                            HashMap<Integer, HashSet<Integer>> predict) {

        int hit = 0;
        int sum = sumTimes(predict);

        Set<Integer> prediotusers = predict.keySet();
        for (Integer user : prediotusers) {
            if (real.containsKey(user)) {
                for (Integer brand : predict.get(user)) {
                    if (real.get(user).contains(brand)) {
                        hit++;
                    }
                }
            }
        }

        return (double) hit / sum;
    }

    public static double calculateRecall(HashMap<Integer, HashSet<Integer>> real,
                                         HashMap<Integer, HashSet<Integer>> predict) {
        int hit = 0;
        int sum = sumTimes(real);

        Set<Integer> realusers = real.keySet();
        for (Integer user : realusers) {
            if (predict.containsKey(user)) {
                for (Integer brand : real.get(user)) {
                    if (predict.get(user).contains(brand)) {
                        hit++;
                    }
                }
            }
        }

        return (double) hit / sum;
    }

    public static int sumTimes(HashMap<Integer, HashSet<Integer>> param) {
        int sum = 0;
        Set<Integer> users = param.keySet();
        for (Integer user : users) {
            sum += param.get(user).size();
        }
        return sum;
    }

    public static double calculateF1(double p, double r) {
        return (2 * p * r) / (p + r);
    }
}
