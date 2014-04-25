package ustc.sse.datamining;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.svd.SVDPlusPlusFactorizer;
import org.apache.mahout.cf.taste.impl.recommender.svd.SVDRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class DataAnalyze1 {

    static Logger                    logger      = LogManager.getLogger(DataAnalyze.class.getName());

    static HashMap<Integer, Integer> userBuyNums = new HashMap<Integer, Integer>();

    public static void main(String[] args) {
         analyzeSimilarity();
        analyzeUserAction();
        // analyzeBrandTimes();
        // analyzeSimilarity();
        // findTBrandIDByUserID();
        // svdplus2();
        findShoppingCar2Buy();
    }

    /**
     * 用来分析用户的购物喜好，比如浏览几次之后会加入购物车或者购买
     * 
     * @param items
     *            The Whole Data
     */
    public static HashMap<Integer, DetailsItem> analyzeUserAction() {
        Integer[] users = DataHolder.getUserID();
        StringBuilder builder = new StringBuilder();
        HashMap<Integer, DetailsItem> userDetails = new HashMap<Integer, DetailsItem>();
        // 这边的数据为，某个用户买了多少商品，找出最多的人
        for (Integer userid : users) {
            LinkedList<DataItem> detailsItem = DataHolder.getDataByUserID(userid);

            for (DataItem item : detailsItem) {
                DetailsItem userItem;

                if (userDetails.containsKey(userid)) {
                    userItem = userDetails.get(userid);
                    userDetails.remove(userid);
                } else {
                    userItem = new DetailsItem();
                    userItem.setUserid(userid);
                }
                switch (item.getType()) {
                    case 0: {// click
                        userItem.setClicknum(userItem.getClicknum() + 1);
                        break;
                    }

                    case 1: {// purchese
                        userItem.setBuynum(userItem.getBuynum() + 1);
                        break;
                    }

                    case 2: {// mark
                        userItem.setMarknum(userItem.getMarknum() + 1);
                        break;
                    }
                    case 3: {// shopping car
                        userItem.setBuynum(userItem.getBuynum() + 1);
                        break;
                    }
                }
                userDetails.put(userid, userItem);
            }
            // 输出用户购买的商品的详细条目。
            if (userDetails.size() != 0) {
                // System.out.println(userDetails.size() +
                // userDetails.get(userid).toString());
                builder.append(userDetails.get(userid).toString() + "\n");
                // System.out.println(user + ":" + userDetails.size());
                // System.out.println(userDetails);
            }
        }
        write2File(new File(ETLCONFIG.USERS_FILE), builder.toString());
        System.out.println(userDetails.size());
        /*
         * 想到，对于经常购物的人员，我们可以进行细致的分析，而对于大部分购物或者加入购物车的人来说，数据量不够
         * 无法进行详细的分析，所以，我建议只针对部分经常购物的人进行推荐，而不是对所有人。
         * 如果某些用户的习惯比较明显，建议直接单独列出推荐，以及寻找刚性需求
         * (即购买次数比较大，三个月中购物次数大于30)。
         */
        for (Integer user : users) {
            userBuyNums.put(user, userDetails.get(user).getBuynum());
        }
        return userDetails;

    }

    /**
     * 用來分析特定品牌的數據，寻找对于大部分用户的刚性需求
     */
    public static void analyzeBrandTimes() {
        Integer[] brands = DataHolder.getBrandID();

        StringBuilder builder = new StringBuilder();
        // 这边的数据为，某一个品牌的商品被买了多少次，找出买的最多的牌子
        for (Integer brandid : brands) {
            LinkedList<DataItem> brandItems = DataHolder.getDataByBrandID(brandid);
            HashMap<Integer, DetailsItem> brandDetails = new HashMap<Integer, DetailsItem>();

            for (DataItem item : brandItems) {
                DetailsItem detailsItem;
                if (brandDetails.containsKey(brandid)) {
                    detailsItem = brandDetails.get(brandid);
                    brandDetails.remove(brandid);
                } else {
                    detailsItem = new DetailsItem();
                    detailsItem.setBrandid(brandid);
                }

                switch (item.getType()) {
                    case 0: {// click
                        detailsItem.setClicknum(detailsItem.getClicknum() + 1);
                        break;
                    }

                    case 1: {// purchese
                        detailsItem.setBuynum(detailsItem.getBuynum() + 1);
                        break;
                    }

                    case 2: {// mark
                        detailsItem.setMarknum(detailsItem.getMarknum());
                        break;
                    }
                    case 3: {// shopping car
                        detailsItem.setBuynum(detailsItem.getBuynum() + 1);
                        break;
                    }
                }

                if (detailsItem.getBuynum() != 0) {
                    brandDetails.put(brandid, detailsItem);
                }
            }
            // 输出商品商标的详细条目。
            System.out.println(brandDetails.get(brandid));
            if (brandDetails.size() != 0) {
                // System.out.println(user + ":" + userDetails.size());
                // System.out.println(brandDetails);
                builder.append(brandDetails.get(brandid).toString() + "\n");
            }
        }
        write2File(new File(ETLCONFIG.BRANDS_FILE), builder.toString());
    }

    /**
     * Write the data to file
     * 
     * @param file
     */
    public static void write2File(File file, String message) {
        try {
            File path = new File(file.getParent());
            path.mkdirs();
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(file);
            writer.write(message);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            logger.warn(e.getMessage());
        }
    }

    public static HashMap<Integer, LinkedList<Integer>> analyzeSimilarity() {
        File modelFile = new File(ETLCONFIG.PREFERENCE_FILE);
        Integer[] users = DataHolder.getUserID();

        HashMap<Integer, LinkedList<Integer>> results = new HashMap<Integer, LinkedList<Integer>>();

        DataModel model;
        try {
            model = new FileDataModel(modelFile);

            RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
            RecommenderBuilder recommenderBuilder = new RecommenderBuilder() {
                @Override
                public Recommender buildRecommender(DataModel model)
                    throws TasteException {
                    UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
                    UserNeighborhood neighborhood = new NearestNUserNeighborhood(5,
                                                                                 similarity,
                                                                                 model);
                    return new GenericUserBasedRecommender(model,
                                                           neighborhood,
                                                           similarity);
                }
            };

            Recommender recommender = recommenderBuilder.buildRecommender(model);

            for (Integer user : users) {
                List<RecommendedItem> re = recommender.recommend((long) user, 10);
                // System.out.println("User : " + user);
                for (RecommendedItem recommendedItem : re) {
                    if (recommendedItem.getValue() > 52) {
                        if (results.containsKey(user)) {
                            results.get(user).add((int) recommendedItem.getItemID());
                        } else {
                            LinkedList<Integer> temp = new LinkedList<Integer>();
                            temp.add((int) recommendedItem.getItemID());
                            results.put(user, temp);
                        }
                        // System.out.println(recommendedItem.getItemID() + " "
                        // + recommendedItem.getValue());
                    }
                }
                // System.out.println();
            }

            evaluator.evaluate(recommenderBuilder, null, model, 0.95, 0.05);
            // 0.95说明我们计算95%的数据，余下的作为测试数据
            // System.out.println(score);
            // 这个结果因人而异，不过大体上应该是在0.89附近
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        System.out.print(results);
        return results;
    }

    public static HashMap<Integer, HashSet<Integer>> findBuyedBrandsByUserID() {
        HashMap<Integer, HashSet<Integer>> results = new HashMap<Integer, HashSet<Integer>>();
        StringBuilder builder = new StringBuilder();
        StringBuilder result = new StringBuilder();
        Integer[] users = DataHolder.getUserID();
        HashMap<Integer, HashSet<DataItem>> resultmap = new HashMap<Integer, HashSet<DataItem>>();
        for (Integer user : users) {
            HashSet<DataItem> buyGoods = new HashSet<DataItem>();
            LinkedList<DataItem> items = DataHolder.getDataByUserID(user);
            for (DataItem item : items) {
                if (item.getType() == 1)
                    buyGoods.add(item);
                else continue;
            }
            if (buyGoods.size() != 0) {
                // System.out.println(buyGoods);
                resultmap.put(user, buyGoods);
                builder.append(buyGoods + "\n");
            }
        }
        users = null;
        users = new Integer[10];
        users = resultmap.keySet().toArray(users);

        for (Integer user : users) {
            HashSet<Integer> brands = new HashSet<Integer>();

            result.append(user + "\t");
            for (DataItem item : resultmap.get(user)) {
                if (brands.add(item.getBrandid())) {
                    result.append(item.getBrandid() + ",");
                }
            }
            result.deleteCharAt(result.length() - 1);
            result.append("\n");
            results.put(user, brands);
        }
        write2File(new File("result/predict.cvs"), result.toString());
        write2File(new File("result/buy.cvs"), builder.toString());
        return results;
    }

    /**
     * SVD++
     * 
     * @return
     */
    public static HashMap<Integer, HashSet<Integer>> svdplus2() {
        analyzeUserAction();

        HashMap<Integer, HashSet<Integer>> results = new HashMap<Integer, HashSet<Integer>>();
        try {
            FileDataModel model = new FileDataModel(new File("result/preference"));
            SVDPlusPlusFactorizer factorizer = new SVDPlusPlusFactorizer(model, 0, 3);
            SVDRecommender recommender = new SVDRecommender(model, factorizer);
            for (Integer user : DataHolder.getUserID()) {
                HashSet<Integer> brands = new HashSet<Integer>();
                for (RecommendedItem brand : recommender.recommend(user, userBuyNums.get(user) / 4 + 1)) {
                    brands.add((int) brand.getItemID());
                }
                results.put(user, brands);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TasteException e) {
            e.printStackTrace();
        }
        return results;
    }

    public static HashMap<Integer, HashSet<Integer>> getMostBrands() {
        analyzeUserAction();
        HashMap<Integer, HashSet<Integer>> results = new HashMap<Integer, HashSet<Integer>>();
        Integer users[] = DataHolder.getUserID();
        HashSet<Integer> brands = findMostBrands();
        for (Integer user : users) {
            if (userBuyNums.get(user) >= 0) {
                results.put(user, brands);
            }
        }
        return results;
    }

    private static HashSet<Integer> findMostBrands() {
        HashSet<Integer> brands = new HashSet<Integer>();
        try {
            Connection connection = DriverManager.getConnection(MYSQLCONFIG.DBURL,
                                                                MYSQLCONFIG.USRNAME,
                                                                MYSQLCONFIG.PASSWORD);
            if (connection != null) {
                Statement statement = connection.createStatement();
                ResultSet resultSet =
                        statement.executeQuery("select count(distinct user_id),  brand_id "
                                               + "from tmail_firstseason  where type = 1  "
                                               + "group by brand_id ;");
                int num = 0;
                while (resultSet.next()) {
                    ++num;
                    int usernums = resultSet.getInt(1);
                    int brandid = resultSet.getInt("brand_id");
                    if (usernums > 20) {
                        brands.add(brandid);
                    }
                }
                logger.info("Data Loaded Success!" + "SUM: " + num);
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return brands;
    }

    public static void findShoppingCar2Buy(){
        Integer[] users = DataHolder.getUserID();
        for (Integer user : users) {
            LinkedList<DataItem> items = DataHolder.getDataByUserID(user);
            for (DataItem item : items) {
                System.out.println(item);
            }
        }
    }
}

/*
 * TODO 算出周期性，用户，购买周期。
 * TODO 8月数据作为最近时间的分析。不同时间数据的权重不同
 * TODO 九月的数据分为8-15~9-1和9-2~9-15, 新旧商品的比例，新商品购买转换率 
 *              相似用户分类
 * TODO 无意义的点击数的删除，比如购买之后好奇心态的点击， 注意与周期性的购买进行区分
 */
