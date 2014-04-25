package ustc.sse.datamining;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.druid.pool.DruidPooledConnection;

public class ShoppingCar2Buy {

    static Logger logger = LogManager.getLogger(ShoppingCar2Buy.class);

    public static void main(String[] args) throws SQLException {
        // washdate();
    }

    // 清洗数据，将清洗好的数据放到etldata文件中
    public static void washdate() throws SQLException {
        DruidPooledConnection connection = DataUtils.getConnection();

        StringBuilder builder = new StringBuilder();

        PreparedStatement statement =
                connection.prepareStatement("select * from tmail_firstseason limit 100;");
        PreparedStatement statement2 =
                connection.prepareStatement("insert into etldata values(?,?,?,?);");
        PreparedStatement statement3 =
                connection.prepareStatement("delete from etldata where 1 = 1");
        ResultSet resultSet = statement.executeQuery();
        System.out.println(statement3.executeUpdate());
        while (resultSet.next()) {
            if (resultSet.getInt("type") != 0) {
                int userid = resultSet.getInt("user_id");
                int brandid = resultSet.getInt("brand_id");
                int type = resultSet.getInt("type");
                long date = resultSet.getDate("visit_datetime").getTime();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                format.format(new Date(date));
                builder.append(userid + ", "
                               + brandid + ", "
                               + type + ", "
                               + format.format(new Date(date)) + "\n");
                statement2.setInt(1, userid);
                statement2.setInt(2, brandid);
                statement2.setInt(3, type);
                statement2.setLong(4, date);
                statement2.executeUpdate();
            }
        }
        // System.out.println(builder.toString());
        DataAnalyze1.write2File(new File("etl/etldata"), builder.toString());
        resultSet.close();
        statement.close();
        statement2.close();
    }
}
