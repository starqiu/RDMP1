package ustc.sse.datamining;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.pool.DruidPooledConnection;

public class DataUtils {
    private DataUtils() {}

    private static DruidDataSource dataSource  = null;
    private static final String    MYSQLCONFIG = "mysql.properties";

    static {
        Properties properties = loadPropertyFile(MYSQLCONFIG);
        try {
            dataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DruidPooledConnection getConnection() {
        DruidPooledConnection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (Exception e) {}
        return connection;
    }

    /**
     * add the two result map
     * 
     * @param result1
     *            HashMap<Integer, HashSet<Integer>>
     * @param result2
     *            HashMap<Integer, HashSet<Integer>>
     * @return result
     */
    public static HashMap<Integer, HashSet<Integer>> addResultMap(HashMap<Integer, HashSet<Integer>> result1,
                                                                  HashMap<Integer, HashSet<Integer>> result2) {
        for (Integer user : result1.keySet().toArray(new Integer[1])) {
            if (result2.containsKey(user)) {
                result2.get(user).addAll(result1.get(user));
            } else {
                result2.put(user, result1.get(user));
            }
        }
        return result2;
    }

    // 得到每项商品的购物趋势,如果趋势是正，则说明这个商品被狗买的趋势是递增的
    public static HashMap<Integer, ArrayList<Integer>> readTBrandsFromFileByUserID(File file)
            throws IOException {
        HashMap<Integer, ArrayList<Integer>> result = new HashMap<Integer, ArrayList<Integer>>();

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String tmp = "";
        while ((tmp = reader.readLine()) != null) {
            String data[] = tmp.split(",");
            ArrayList<Integer> months = new ArrayList<Integer>();
            int brand = Integer.valueOf(data[0]);
            for (int i = 1; i < data.length; i++) {
                months.add(Integer.valueOf(data[i]));
            }
            result.put(brand, months);
        }
        reader.close();
        return result;
    }

    // 读出用户的购买的情况，随月份变化
    public static HashMap<Integer, UserBuyByMonth> readUserBuyFromFileByUserID(File file)
            throws IOException {
        HashMap<Integer, UserBuyByMonth> result = new HashMap<Integer, DataUtils.UserBuyByMonth>();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String tmp = "";
        while ((tmp = reader.readLine()) != null) {
            String data[] = tmp.split("\\|");
            int userid = Integer.valueOf(data[0]);
            UserBuyByMonth userBuyByMonth = new UserBuyByMonth();
            userBuyByMonth.setUserid(userid);
            HashMap<Integer, ArrayList<Integer>> months =
                    new HashMap<Integer, ArrayList<Integer>>();
            for (int i = 1; i < data.length - 1; i++) {
                ArrayList<Integer> buyitems = new ArrayList<Integer>();
                String[] items = data[i].split(",");
                for (String item : items) {
                    if (item.equals(" "))
                        continue;
                    buyitems.add(Integer.valueOf(item));
                }
                months.put(i + 3, buyitems);
            }
            userBuyByMonth.setMonth(months);

            HashSet<Integer> Titems = new HashSet<Integer>();
            try {
                String[] items = data[6].split(",");
                for (String item : items) {
                    Titems.add(Integer.valueOf(item));
                }
                userBuyByMonth.setTBrand(Titems);
            } catch (Exception e) {
                userBuyByMonth.setTBrand(null);
            }

            result.put(userid, userBuyByMonth);
        }
        reader.close();
        return result;
    }

    // Model Test Code
    public static void main(String[] args) {
        try {
            System.out.println(readTBrandsFromFileByUserID(new File("sample/brand.csv")));
            System.out.println(readUserBuyFromFileByUserID(new File("sample/user.csv")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Properties loadPropertyFile(String fullFile) {
        if (null == fullFile || fullFile.equals(""))
            throw new IllegalArgumentException("Properties file path can not be null : " + fullFile);
        InputStream inputStream = null;
        Properties properties = null;
        try {
            inputStream = DataUtils.class.getClassLoader().getResourceAsStream(fullFile);
            properties = new Properties();
            properties.load(inputStream);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Properties file not found: " + fullFile);
        } catch (IOException e) {
            throw new IllegalArgumentException("Properties file can not be loading: " + fullFile);
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return properties;
    }

    static class UserBuyByMonth {
        int                                  userid;
        HashMap<Integer, ArrayList<Integer>> month;
        HashSet<Integer>                   TBrand;

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "UserBuyByMonth [userid=" + userid
                   + ", month="
                   + month
                   + ", TBrand="
                   + TBrand
                   + "]";
        }

        /* (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((TBrand == null) ? 0 : TBrand.hashCode());
            result = prime * result + ((month == null) ? 0 : month.hashCode());
            result = prime * result + userid;
            return result;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (!(obj instanceof UserBuyByMonth)) return false;
            UserBuyByMonth other = (UserBuyByMonth) obj;
            if (TBrand == null) {
                if (other.TBrand != null) return false;
            } else if (!TBrand.equals(other.TBrand)) return false;
            if (month == null) {
                if (other.month != null) return false;
            } else if (!month.equals(other.month)) return false;
            if (userid != other.userid) return false;
            return true;
        }

        /**
         * @return the userid
         */
        public int getUserid() {
            return userid;
        }

        /**
         * @param userid
         *            the userid to set
         */
        public void setUserid(int userid) {
            this.userid = userid;
        }

        /**
         * @return the month
         */
        public HashMap<Integer, ArrayList<Integer>> getMonth() {
            return month;
        }

        /**
         * @param month
         *            the month to set
         */
        public void setMonth(HashMap<Integer, ArrayList<Integer>> month) {
            this.month = month;
        }

        /**
         * @return the tBrand
         */
        public HashSet<Integer> getTBrand() {
            return TBrand;
        }

        /**
         * @param tBrand
         *            the tBrand to set
         */
        public void setTBrand(HashSet<Integer> tBrand) {
            TBrand = tBrand;
        }

    }
}
