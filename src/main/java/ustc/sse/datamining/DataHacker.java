package ustc.sse.datamining;

import java.io.File;

public class DataHacker {
    public static void main(String[] args) {
        Integer[] users = DataHolder.getUserID();
        Integer[] brands = DataHolder.getBrandID();
        StringBuilder builder = new StringBuilder();
        for (Integer user : users) {
            builder.append(user + "\t");
            for (Integer brand : brands) {
                builder.append(brand + ",");
            }
            builder.deleteCharAt(builder.length() - 1);
            builder.append("\n");
        }
        DataAnalyze1.write2File(new File("mike_tmp/hacker.txt"),
                               builder.toString());
    }
}
