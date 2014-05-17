package ustc.sse.datamining;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;

public class Level4UserBuy {

    // 0 是点击， 1 是收藏 ，2 是购物车
    static HashMap<Integer, Double[]> usersData = new HashMap<Integer, Double[]>();

    public static void main(String[] args) {
        Integer[] users = DataHolder.getUserID();
        for (Integer user : users) {
            usersData.put(user, new Double[3]);
        }

        Click2Buy();
        Mark2Buy();
        SCar2Buy();

        display();
    }

    public static void display() {
        StringBuilder builder = new StringBuilder();
        for (Integer user : usersData.keySet()) {
            builder.append("" + user + "\t\t");
            builder.append("," + (int) (usersData.get(user)[0] + 0) + "\t");
            builder.append("," + (int) (usersData.get(user)[1] + 0) + "\t");
            builder.append("," + (int) (usersData.get(user)[2] + 0) + "\n");
        }
        System.out.println(builder.toString());
        DataAnalyze1.write2File(new File("etl/levels"), builder.toString());
    }

    public static void Click2Buy() {
        Integer users[] = DataHolder.getUserID();
        for (Integer user : users) {
            int clicknum = 0, buynum = 0;
            LinkedList<DataItem> items = DataHolder.getDataByUserID(user);
            for (DataItem item : items) {
                switch (item.getType()) {
                    case 0: {
                        clicknum++;
                        break;
                    }
                    case 1: {
                        buynum++;
                        break;
                    }
                }
            }
            if (buynum != 0 && clicknum != 0) {
                usersData.get(user)[0] = (double) clicknum / (double) buynum;
            } else {
                usersData.get(user)[0] = 0.0;
            }
        }
    }

    public static void Mark2Buy() {
        Integer users[] = DataHolder.getUserID();
        for (Integer user : users) {
            int marknum = 0, buynum = 0;
            LinkedList<DataItem> items = DataHolder.getDataByUserID(user);
            for (DataItem item : items) {
                switch (item.getType()) {
                    case 2: {
                        marknum++;
                        break;
                    }
                    case 1: {
                        buynum++;
                        break;
                    }
                }
            }
            if (buynum != 0 && marknum != 0) {
                usersData.get(user)[1] = (double) marknum / (double) buynum * 100;
            } else {
                usersData.get(user)[1] = 0.0;
            }
        }
    }

    public static void SCar2Buy() {
        Integer users[] = DataHolder.getUserID();
        for (Integer user : users) {
            int scarnum = 0, buynum = 0;
            LinkedList<DataItem> items = DataHolder.getDataByUserID(user);
            for (DataItem item : items) {
                switch (item.getType()) {
                    case 3: {
                        scarnum++;
                        break;
                    }
                    case 1: {
                        buynum++;
                        break;
                    }
                }
            }
            if (buynum != 0 && scarnum != 0) {
                usersData.get(user)[2] = (double) scarnum / (double) buynum * 100;
            } else {
                usersData.get(user)[2] = 0.0;
            }
        }
    }
}
