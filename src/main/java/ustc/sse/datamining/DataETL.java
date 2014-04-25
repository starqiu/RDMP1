package ustc.sse.datamining;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class DataETL {

    public static void main(String[] args) {
        Integer[] users = DataHolder.getUserID();
        File file = new File(ETLCONFIG.PREFERENCE_FILE);
        StringBuilder builder = new StringBuilder();
        for (Integer userid : users) {
            LinkedList<DataItem> userItems = new LinkedList<DataItem>();
            for (DataItem dataItem : DataHolder.getDataByUserID(userid)) {
                DataItem userItem = null;
                int index;
                if ((index = userItems.indexOf(dataItem)) != -1) {
                    userItem = userItems.get(index);
                    userItems.remove(index);
                } else {
                    userItem = dataItem;
                }

                switch (dataItem.getType()) {
                    case 0: {// click
                        userItem.setPreference(userItem.getPreference() + 0);
                        break;
                    }
                    case 1: {// buy
                        userItem.setPreference(userItem.getPreference() + 50);
                        break;
                    }
                    case 2: {// mark
                        userItem.setPreference(userItem.getPreference() + 10);
                        break;
                    }
                    case 3: { // add market car
                        userItem.setPreference(userItem.getPreference() + 25);
                        break;
                    }
                }
                userItems.add(userItem);
            }
            for (DataItem dataItem : userItems) {
                System.out.println(dataItem);
                builder.append(dataItem.getUserid() + ","
                               + dataItem.getBrandid() + ","
                               + (int) (dataItem.getPreference()) + "\n");
            }
            write2File(builder.toString(), file);
        }
    }

    public static void write2File(String string, File file) {
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            FileWriter writer = new FileWriter(file, true);

            writer.write(string);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
