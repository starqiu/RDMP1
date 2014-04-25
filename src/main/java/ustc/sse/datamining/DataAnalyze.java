package ustc.sse.datamining;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.Reader;
import org.apache.hadoop.io.Text;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericPreference;
import org.apache.mahout.cf.taste.impl.model.GenericUserPreferenceArray;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.AbstractRecommender;
import org.apache.mahout.cf.taste.impl.recommender.svd.SVDPlusPlusFactorizer;
import org.apache.mahout.cf.taste.impl.recommender.svd.SVDRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.Preference;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.common.Pair;
import org.apache.mahout.fpm.pfpgrowth.convertors.string.TopKStringPatterns;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class DataAnalyze {

	private static Hashtable<Integer, LinkedList<Integer>> myHightRecallResultTable = new Hashtable<Integer, LinkedList<Integer>>();
	private static Hashtable<Integer, LinkedList<Integer>> myHighPrecisionResultTable = new Hashtable<Integer, LinkedList<Integer>>();

	private static double hitbrand = 0;
	private static double all = 0;

	public static void main(String[] args) {
		try {
			//还在开发中
//			addLogisticRegression2Table(myHightRecallResultTable);
			
			//只点击为购买的商品模块，待调优的地方：迭代时间；阈值
			for (int i = 1; i <= 119; i++) {
				Hashtable<Integer, LinkedList<Integer>> rt = addOnlyClickedItemsModel2Table(
						i, myHightRecallResultTable, MYSQLCONFIG.OnlyClickItems);
				printCountOfTable(rt);
				StatisticsResultData rtSta = statisticsResult(
						MYSQLCONFIG.DateMiddle, MYSQLCONFIG.DateEnd, rt);
				System.out.printf(rtSta.toString() + "\n");
			}
			printCountOfTable(myHightRecallResultTable);
			writeResultToTXT(myHightRecallResultTable, ETLCONFIG.RESULT
					+ "predictModel.txt");

			//收藏模块。只迭代第21天
			for (int i = 21; i <= 21; i++) {
				Hashtable<Integer, LinkedList<Integer>> rt = addOnlyClickedItemsModel2Table(
						i, myHightRecallResultTable,
						MYSQLCONFIG.isOnlyFavorItems);
				printCountOfTable(rt);
				writeResultToTXT(rt, ETLCONFIG.RESULT + "favor.txt");
			}

			//最近7天购买商品模块
			Hashtable<Integer, LinkedList<Integer>> leastBuy7ItemsTable = getLeastDayTable(
					1, 14, MYSQLCONFIG.isOnlypurchaseAction, myHightRecallResultTable);
			printCountOfTable(leastBuy7ItemsTable);
			writeResultToTXT(leastBuy7ItemsTable, ETLCONFIG.RESULT
					+ "leastBuy2ItemsTable.txt");
			
			// 买了又买的周期性商品模块
			Hashtable<Integer, LinkedList<Integer>> myHotItemsTable = addHotItemsModel2Table(
					MYSQLCONFIG.DateBegin, MYSQLCONFIG.DateMiddle,
					myHightRecallResultTable);
			printCountOfTable(myHightRecallResultTable);
			writeResultToTXT(myHightRecallResultTable, ETLCONFIG.RESULT
					+ "myHightRecallResultTable.txt");


			// /////////////////////////////////////////////////////////


			// Hashtable<Integer, LinkedList<Integer>> fPGrowthTable =
			// fPGrowthFilterAdd2Table(
			// 723, "fList.seq", "frequentpatterns.seq", 0.002, 0.2,
			// myFinalResultTable);
			// StatisticsResultData myfPGrowthFilterAdd2TableStatistic =
			// statisticsResult(fPGrowthTable);
			// System.out.printf(myfPGrowthFilterAdd2TableStatistic.toString()
			// + "\n");
			// writeResultToTXT(fPGrowthTable, "fPGrowthTable");

			// StatisticsResultData myFinalStatistic =
			// statisticsResult(myHightRecallResultTable);
			// System.out.printf(myFinalStatistic.toString());
			//
			// writeResultToTXT(myHightRecallResultTable, ETLCONFIG.RESULT
			// + "seasonFirest_.txt");

		} catch (Exception e2) {
			// print
			e2.printStackTrace();

		}

	}

	public static Hashtable<Integer, LinkedList<Integer>> addLogisticRegression2Table(
			Hashtable<Integer, LinkedList<Integer>> resultTable)
			throws Exception {

		Hashtable<Integer, Object> myUserTable = getIndexByUsersData(
				MYSQLCONFIG.DateBegin, MYSQLCONFIG.DateMiddle,
				MYSQLCONFIG.isAllAction);

		Hashtable<Integer, Object> myItemsTable = getIndexByItemsData(
				MYSQLCONFIG.DateBegin, MYSQLCONFIG.DateMiddle,
				MYSQLCONFIG.isAllAction);

		// 开始训练数据
		LogResTrainData mylrTrain = new LogResTrainData();
		Enumeration<Integer> iBuy = myItemsTable.keys();
		while (iBuy.hasMoreElements()) {
			int itemId = iBuy.nextElement();
			Object myItem = myItemsTable.get(itemId);
			int[] itemAction = ((ItemWithUsersData) myItem)
					.getItemActionCount();
			if (itemAction[1] >= 5) {
				mylrTrain.lRForgeDataModel(myUserTable,
						(ItemWithUsersData) myItem, MYSQLCONFIG.DateBegin,
						MYSQLCONFIG.DateMiddle);
				mylrTrain.laneLRtrain();
				Hashtable<Integer, LinkedList<Integer>> rt = mylrTrain
						.lanePredict(resultTable);

				StatisticsResultData rtSta = statisticsResult(
						MYSQLCONFIG.DateMiddle, MYSQLCONFIG.DateEnd, rt);
				System.out.printf(rtSta.toString() + "\n");
			}
		}
		return null;
	}

	public static Hashtable<Integer, Object> getSpecificTypeItems(
			String dayBegin, String dayEnd, int actionType) throws Exception {

		switch (actionType) {

		case MYSQLCONFIG.OnlyClickItems: {

			// 最近一个月买过的商品
			Hashtable<Integer, Object> myItemsBuyTable = getIndexByItemsData(
					getLeastDayTime(3, dayBegin), dayEnd,
					MYSQLCONFIG.isBuyOrCartAction);
			Hashtable<Integer, LinkedList<Integer>> myItemsBuyTableIndex = getPredictItemUsersTable(myItemsBuyTable);

			Hashtable<Integer, Object> myItemsClickTable = getIndexByItemsData(
					dayBegin, dayEnd, MYSQLCONFIG.isClickAction);

			getRidOfDuplicate(myItemsClickTable, myItemsBuyTableIndex);

			return myItemsClickTable;
		}

		case MYSQLCONFIG.isOnlyFavorItems: {// 收藏，转化率0.006583552
			Hashtable<Integer, Object> myItemsBuyTable = getIndexByItemsData(
					getLeastDayTime(3, dayBegin), dayEnd,
					MYSQLCONFIG.isBuyOrCartAction);
			Hashtable<Integer, LinkedList<Integer>> myItemsBuyTableIndex = getPredictItemUsersTable(myItemsBuyTable);

			Hashtable<Integer, Object> myItemsFavorTable = getIndexByItemsData(
					dayBegin, dayEnd, MYSQLCONFIG.isFavoriteAction);
			getRidOfDuplicate(myItemsFavorTable, myItemsBuyTableIndex);

			return myItemsFavorTable;
		}

		case MYSQLCONFIG.isHighRate: {// 收藏，转化率0.006583552
			Hashtable<Integer, Object> myItemsBuyTable = getIndexByItemsData(
					getLeastDayTime(3, dayBegin), dayEnd,
					MYSQLCONFIG.isBuyOrCartAction);
			Hashtable<Integer, LinkedList<Integer>> myItemsBuyTableIndex = getPredictItemUsersTable(myItemsBuyTable);

			Hashtable<Integer, Object> myItemsTable = getIndexByItemsData(
					dayBegin, dayEnd, MYSQLCONFIG.isClickAction);
			getRidOfDuplicate(myItemsTable, myItemsBuyTableIndex);

			Hashtable<Integer, Object> myUsersTable = getIndexByUsersData(
					dayBegin, dayEnd, MYSQLCONFIG.isAllAction);

			myItemsTable = getHighRateItems(0.05, myUsersTable, myItemsTable);
			return myItemsTable;
		}

		default:
			return null;

		}

	}

	//
	public static Hashtable<Integer, LinkedList<Integer>> addOnlyClickedItemsModel2Table(
			int numLeastDay,
			Hashtable<Integer, LinkedList<Integer>> myHashTable, int model)
			throws SQLException {

		// 数据准备，包括用户索引、商品索引、目标表。
		Hashtable<Integer, LinkedList<Integer>> resultTable = new Hashtable<Integer, LinkedList<Integer>>();

		Hashtable<Integer, Object> mySpecialTrainTable;
		double threshold = 0.0;
		try {

			switch (model) {
			case MYSQLCONFIG.OnlyClickItems: {
				// 点击模式 (8-14, 8-15]
				switch (numLeastDay) {
				case 1: {
					threshold = 1;
				}
					break;

				case 2: {
					threshold = 3;
				}
					break;

				default: {
					threshold = 2 * (2 + (int) Math.log(numLeastDay));
					// threshold = Math.min(12, threshold);
				}

				}
				mySpecialTrainTable = getSpecificTypeItems(
						getLeastDayTime(numLeastDay, MYSQLCONFIG.DateMiddle),
						MYSQLCONFIG.DateMiddle, MYSQLCONFIG.OnlyClickItems);
			}
				break;

			case MYSQLCONFIG.isOnlyFavorItems: {
				threshold = 1;
				mySpecialTrainTable = getSpecificTypeItems(
						getLeastDayTime(numLeastDay, MYSQLCONFIG.DateMiddle),
						MYSQLCONFIG.DateMiddle, MYSQLCONFIG.isOnlyFavorItems);
			}
				break;

			case MYSQLCONFIG.isHighRate: {
				switch (numLeastDay) {
				case 1: {
					threshold = 1;
				}
					break;

				case 2: {
					threshold = 2;
				}
					break;

				default: {
					threshold = 2 * (1 + (int) Math.log(numLeastDay));
					// threshold = Math.min(12, threshold);
				}

				}
				mySpecialTrainTable = getSpecificTypeItems(
						getLeastDayTime(numLeastDay, MYSQLCONFIG.DateMiddle),
						MYSQLCONFIG.DateMiddle, MYSQLCONFIG.isHighRate);
			}
				break;

			default: {
				return null;
			}

			}

			Enumeration<Integer> itemsId = mySpecialTrainTable.keys();
			while (itemsId.hasMoreElements()) {
				int itemId = itemsId.nextElement();
				Object myItem = mySpecialTrainTable.get(itemId);
				ItemWithUsersData myItemt = (ItemWithUsersData) myItem;
				// 存放当前商品对应的用户-点击/收藏次数。
				Hashtable<Integer, Double> userWithClickCount = new Hashtable<Integer, Double>();
				for (ItemWithUsersData.User myUser : myItemt.getUsers()) {
					boolean isExsit = userWithClickCount.containsKey(myUser
							.getUserID());
					if (false == isExsit) {
						userWithClickCount.put(myUser.getUserID(), 1.0);
					} else {
						double curCount = userWithClickCount.get(myUser
								.getUserID());
						userWithClickCount
								.put(myUser.getUserID(), 1 + curCount);
					}
				}

				// 如果用户点击次数大于当前阈值则推荐
				Enumeration<Integer> usersId = userWithClickCount.keys();
				while (usersId.hasMoreElements()) {
					int userId = usersId.nextElement();
					double numClick = userWithClickCount.get(userId);

					if (numClick >= threshold) {
						boolean isAlreadyInThere = appendResult2HashTable(
								myHashTable, userId, itemId);
						if (false == isAlreadyInThere) {
							appendResult2HashTable(resultTable, userId, itemId);

						} else {
							appendResult2HashTable(myHighPrecisionResultTable,
									userId, itemId);
						}
					}
				}
			}
		} catch (Exception e) {
			//
			e.printStackTrace();
		}

		System.out.println("最近 :" + numLeastDay + "天，阈值为 :" + threshold);
		return resultTable;

	}

	public static Hashtable<Integer, LinkedList<Integer>> addActionTypeModel2Table(
			double click2PurchaseRateThreshold, int numDayTime,
			Hashtable<Integer, LinkedList<Integer>> myHashTable)
			throws SQLException {

		// 点击购买率超过 click2PurchaseRateThreshold 值的用户添加到表中
		Hashtable<Integer, Integer> highClick2PurchaseUsersTable = new Hashtable<Integer, Integer>();
		Hashtable<Integer, Object> candidateUsersTable = getIndexByUsersData(
				getLeastDayTime(numDayTime, MYSQLCONFIG.DateMiddle),
				MYSQLCONFIG.DateMiddle, MYSQLCONFIG.isAllAction);

		Enumeration<Integer> hotUsers = candidateUsersTable.keys();
		while (hotUsers.hasMoreElements()) {
			int itemId = hotUsers.nextElement();
			Object mostUsersCandiItem = candidateUsersTable.get(itemId);
			int[] actionCount = ((UserWithItemsData) mostUsersCandiItem)
					.getUserActionCount();
			double click2Buy = ((UserWithItemsData) mostUsersCandiItem)
					.getClick2purchase();

			if (click2Buy >= click2PurchaseRateThreshold) {
				highClick2PurchaseUsersTable.put(itemId, actionCount[1]
						+ actionCount[3]);

			}
		}

		Hashtable<Integer, Integer> highClick2PurchaseItemsTable = new Hashtable<Integer, Integer>();
		Hashtable<Integer, Object> candidateItemsTable = getIndexByItemsData(
				getLeastDayTime(numDayTime, MYSQLCONFIG.DateMiddle),
				MYSQLCONFIG.DateMiddle, MYSQLCONFIG.isAllAction);

		Enumeration<Integer> candidateItemsItems = candidateItemsTable.keys();
		while (candidateItemsItems.hasMoreElements()) {
			int itemId = candidateItemsItems.nextElement();
			Object candidateItem = candidateItemsTable.get(itemId);
			double click2BuyRateItem = ((ItemWithUsersData) candidateItem)
					.getClick2purchase();
			int[] actionCount = ((ItemWithUsersData) candidateItem)
					.getItemActionCount();

			if (click2BuyRateItem >= click2PurchaseRateThreshold) {
				highClick2PurchaseItemsTable.put(itemId, actionCount[1]
						+ actionCount[3]);

			}

		}

		Hashtable<Integer, LinkedList<Integer>> favoriteItemsTable = new Hashtable<Integer, LinkedList<Integer>>();
		Hashtable<Integer, Object> myFavoriteItems = getIndexByItemsData(
				getLeastDayTime(numDayTime, MYSQLCONFIG.DateMiddle),
				MYSQLCONFIG.DateMiddle, MYSQLCONFIG.isFavoriteAction);
		Enumeration<Integer> indexEmu = myFavoriteItems.keys();
		while (indexEmu.hasMoreElements()) {
			int itemId = indexEmu.nextElement();
			Object myItems = myFavoriteItems.get(itemId);
			for (ItemWithUsersData.User user : ((ItemWithUsersData) myItems)
					.getUsers()) {
				// 如果是高转换率用户收藏的商品，添加。
				if (true == highClick2PurchaseItemsTable.containsKey(itemId)) {
					boolean isAlreadyInThere = appendResult2HashTable(
							myHashTable, user.getUserID(), itemId);
					if (false == isAlreadyInThere) {
						appendResult2HashTable(favoriteItemsTable,
								user.getUserID(), itemId);
					} else {
						appendResult2HashTable(myHighPrecisionResultTable,
								user.getUserID(), itemId);
					}

				}
			}

		}

		return favoriteItemsTable;

	}

	public static Hashtable<Integer, LinkedList<Integer>> addDarkHorseItemsModel2Table(
			int numDay, Hashtable<Integer, LinkedList<Integer>> myHashTable)
			throws SQLException {

		Hashtable<Integer, Object> myItemsTable = getIndexByItemsData(
				getLeastDayTime(numDay, MYSQLCONFIG.DateMiddle),
				MYSQLCONFIG.DateMiddle, MYSQLCONFIG.isOnlypurchaseAction);
		Hashtable<Integer, Object> myItems1Table = getIndexByItemsData(
				getLeastDayTime(numDay + numDay, MYSQLCONFIG.DateMiddle),
				MYSQLCONFIG.DateMiddle, MYSQLCONFIG.isOnlypurchaseAction);

		Hashtable<Integer, LinkedList<Integer>> myDarkHorseTable = new Hashtable<Integer, LinkedList<Integer>>();
		LinkedList<Integer> myDarkHorses = new LinkedList<Integer>();
		Enumeration<Integer> indexEmu = myItemsTable.keys();
		while (indexEmu.hasMoreElements()) {
			int itemId = indexEmu.nextElement();
			Object myItems = myItemsTable.get(itemId);
			ItemWithUsersData myItemt = (ItemWithUsersData) myItems;
			int[] itemActon = myItemt.getItemActionCount();
			Object items1Id = myItems1Table.get(itemId);
			ItemWithUsersData items1Idt = (ItemWithUsersData) items1Id;
			int[] item1Acton = items1Idt.getItemActionCount();
			// 取销量上升趋势的商品。
			if (item1Acton[1] + item1Acton[3] >= (item1Acton[1] + item1Acton[3]) / 2) {
				for (ItemWithUsersData.User user : items1Idt.getUsers()) {
					myDarkHorses.add(itemId);
					boolean isAlreadyInThere = appendResult2HashTable(
							myHashTable, user.getUserID(), itemId);
					if (false == isAlreadyInThere) {
						appendResult2HashTable(myDarkHorseTable,
								user.getUserID(), itemId);
					} else {
						appendResult2HashTable(myHighPrecisionResultTable,
								user.getUserID(), itemId);
					}
				}
			}
		}

		return myDarkHorseTable;
	}

	/**
	 * 三大参数：最近/周期性热销商品/活跃用户
	 */
	public static Hashtable<Integer, LinkedList<Integer>> addHotItemsModel2Table(
			String dayTime1, String dayTime2,
			Hashtable<Integer, LinkedList<Integer>> myHashtable)
			throws SQLException {
		try {
			// 存放周期性热销商品, itemId-购买个数。
			Hashtable<Integer, Integer> myHotItemsTable = new Hashtable<Integer, Integer>();
			// 存放活跃用户。userId-购买个数。
			Hashtable<Integer, Integer> myActiveUsersTable = new Hashtable<Integer, Integer>();
			// 存放返回结果
			Hashtable<Integer, LinkedList<Integer>> rtHotItemsTable = new Hashtable<Integer, LinkedList<Integer>>();
			Hashtable<Integer, Object> userBuyingTable = getIndexByUsersData(
					dayTime1, dayTime2, MYSQLCONFIG.isOnlypurchaseAction);
			Hashtable<Integer, Object> itemBuyingTable = getIndexByItemsData(
					dayTime1, dayTime2, MYSQLCONFIG.isOnlypurchaseAction);

			// 第0次迭代，获得活跃用户表和周期性商品表。
			Enumeration<Integer> userEnum = userBuyingTable.keys();
			while (userEnum.hasMoreElements()) {
				int index = userEnum.nextElement();
				Object userWithItem = userBuyingTable.get(index);

				UserWithItemsData myUsert = (UserWithItemsData) userWithItem;
				LinkedList<UserWithItemsData.Product> myItems = myUsert
						.getProducts();

				for (int i = 0; i < myItems.size(); i++) {
					UserWithItemsData.Product itemI = (UserWithItemsData.Product) myItems
							.get(i);
					int[] itemAction = ((ItemWithUsersData) itemBuyingTable
							.get(itemI.getBrandID())).getItemActionCount();

					for (int j = i + 1; j < myItems.size(); j++) {
						UserWithItemsData.Product itemJ = (UserWithItemsData.Product) myItems
								.get(j);

						if (itemI.getBrandID() == itemJ.getBrandID()) {

							java.util.Date timeI = itemI.getVisitDaytime();
							java.util.Date timeJ = itemJ.getVisitDaytime();

							int dateDuraIJ = 0;
							try {
								dateDuraIJ = getDaytimeduration(timeI, timeJ);
							} catch (Exception e) {
								// do nothing
							}
							if (dateDuraIJ >= 3) {
								appendResult2HashTable(rtHotItemsTable,
										myUsert.getUserID(), itemI.getBrandID());

								appendResult2HashTable(myHashtable,
										myUsert.getUserID(), itemI.getBrandID());

								myHotItemsTable.put(itemI.getBrandID(),
										itemAction[1] + itemAction[3]);

								int[] userAction = myUsert.getUserActionCount();
								myActiveUsersTable.put(myUsert.getUserID(),
										userAction[1] + userAction[3]);
								continue;
							}

						}
					}

				}

			}
			printCountOfTable(myHightRecallResultTable);

			// 第一次迭代，对所有买过周期商品的用户再次推荐，包括只購買過一次。分析商品购买记录-推荐
			Hashtable<Integer, LinkedList<Integer>> itorFirstHotItemsPureTable = new Hashtable<Integer, LinkedList<Integer>>();
			Enumeration<Integer> hotItemEnum = myHotItemsTable.keys();
			while (hotItemEnum.hasMoreElements()) {
				int index = hotItemEnum.nextElement();
				boolean isExsit = itemBuyingTable.containsKey(index);
				if (true == isExsit) {
					// Object myItem = ItemIndexTable.get(index);
					LinkedList<ItemWithUsersData.User> myUsers = ((ItemWithUsersData) itemBuyingTable
							.get(index)).getUsers();

					for (ItemWithUsersData.User user : myUsers) {
						boolean isAlreadyInThere = appendResult2HashTable(
								myHashtable, user.getUserID(), index);
						if (false == isAlreadyInThere) {
							appendResult2HashTable(itorFirstHotItemsPureTable,
									user.getUserID(), index);
						} else {
							appendResult2HashTable(myHighPrecisionResultTable,
									user.getUserID(), index);
						}

					}

				}

			}
			printCountOfTable(itorFirstHotItemsPureTable);
			writeResultToTXT(itorFirstHotItemsPureTable, ETLCONFIG.RESULT
					+ "itorFirstHotItemsPureTable.txt");

			return itorFirstHotItemsPureTable;

		} catch (Exception e) {
			return null;
		}
	}

	//
	public static Hashtable<Integer, LinkedList<Integer>> addBigKillerItemsModel2Table(
			int numDay, double click2PurchaseRateThreshold,
			Hashtable<Integer, LinkedList<Integer>> myHashtable)
			throws SQLException {

		Hashtable<Integer, LinkedList<Integer>> rtPureTable = new Hashtable<Integer, LinkedList<Integer>>();

		// 获得用户 click2PurchaseRate
		Hashtable<Integer, Double> highClick2PurchaseUsersTable = new Hashtable<Integer, Double>();
		Hashtable<Integer, Object> candidateUsersTable = getIndexByUsersData(
				getLeastDayTime(numDay, MYSQLCONFIG.DateMiddle),
				getLeastDayTime(numDay + numDay, MYSQLCONFIG.DateMiddle),
				MYSQLCONFIG.isAllAction);
		double usersumRate = 0.0;
		double usercount = candidateUsersTable.size();
		Enumeration<Integer> hotUsers = candidateUsersTable.keys();
		while (hotUsers.hasMoreElements()) {
			int userId = hotUsers.nextElement();
			Object mostUsersCandiItem = candidateUsersTable.get(userId);
			double rateUser = ((UserWithItemsData) mostUsersCandiItem)
					.getClick2purchase();
			highClick2PurchaseUsersTable.put(userId, rateUser);
			usersumRate += rateUser;
		}
		double userMeanRate = usersumRate / usercount;

		// 获得商品 click2PurchaseRate
		numDay = Math.min(7, numDay);
		Hashtable<Integer, Double> highClick2PurchaseItemsTable = new Hashtable<Integer, Double>();
		Hashtable<Integer, Object> candidateItemsTable = getIndexByItemsData(
				getLeastDayTime(numDay, MYSQLCONFIG.DateMiddle),
				MYSQLCONFIG.DateMiddle, MYSQLCONFIG.isAllAction);

		double itemsumRate = 0.0;
		double itemcount = candidateItemsTable.size();
		Enumeration<Integer> candidateItemsItems = candidateItemsTable.keys();
		while (candidateItemsItems.hasMoreElements()) {

			int itemId = candidateItemsItems.nextElement();
			Object candidateItem = candidateItemsTable.get(itemId);
			double rateItem = ((ItemWithUsersData) candidateItem)
					.getClick2purchase();
			highClick2PurchaseItemsTable.put(itemId, rateItem);
			itemsumRate += rateItem;

		}
		double itemMeanRate = itemsumRate / itemcount;

		// 开始遍历
		Enumeration<Integer> itItems = highClick2PurchaseItemsTable.keys();
		while (itItems.hasMoreElements()) {
			int itemId = itItems.nextElement();
			Object candidateItem = candidateItemsTable.get(itemId);

			double rateItem = highClick2PurchaseItemsTable.get(itemId);
			for (ItemWithUsersData.User myUser : ((ItemWithUsersData) candidateItem)
					.getUsers()) {
				double rateUser = highClick2PurchaseUsersTable.get(myUser
						.getUserID());
				if (rateUser > click2PurchaseRateThreshold
						&& rateItem > click2PurchaseRateThreshold) {
					boolean isAlreadyInThere = appendResult2HashTable(
							myHashtable, myUser.getUserID(), itemId);
					if (false == isAlreadyInThere) {
						appendResult2HashTable(rtPureTable, myUser.getUserID(),
								itemId);
					} else {
						appendResult2HashTable(myHighPrecisionResultTable,
								myUser.getUserID(), itemId);
					}
				}
			}
		}

		return rtPureTable;
	}

	// 候选库：用户点击过的所有商品；过滤指标：user点击转换率；item点击转换率；最近7*1天。
	public static Hashtable<Integer, LinkedList<Integer>> addHighClick2PurchaseModel2Table(
			int numDay, double click2PurchaseRateThreshold,
			Hashtable<Integer, LinkedList<Integer>> myHashtable)
			throws SQLException {

		switch (numDay) {
		case 1: {
			// 对第一天做特殊处理
			click2PurchaseRateThreshold = 0.03;
		}
			break;

		default: {
			click2PurchaseRateThreshold = Math.min(0.045,
					click2PurchaseRateThreshold);
		}
			break;
		}

		Hashtable<Integer, LinkedList<Integer>> rtHighClick2PurchaseItemsPureTable = new Hashtable<Integer, LinkedList<Integer>>();

		// 点击购买率超过 click2PurchaseRateThreshold 值的用户添加到表中
		Hashtable<Integer, Integer> highClick2PurchaseUsersTable = new Hashtable<Integer, Integer>();
		Hashtable<Integer, Object> candidateUsersTable = getIndexByUsersData(
				getLeastDayTime(numDay, MYSQLCONFIG.DateMiddle),
				MYSQLCONFIG.DateMiddle, MYSQLCONFIG.isAllAction);

		Enumeration<Integer> hotUsers = candidateUsersTable.keys();
		while (hotUsers.hasMoreElements()) {
			int userId = hotUsers.nextElement();
			Object mostUsersCandiItem = candidateUsersTable.get(userId);
			int[] actionCount = ((UserWithItemsData) mostUsersCandiItem)
					.getUserActionCount();
			double click2Buy = ((UserWithItemsData) mostUsersCandiItem)
					.getClick2purchase();

			if (click2Buy >= click2PurchaseRateThreshold) {
				highClick2PurchaseUsersTable.put(userId, actionCount[1]
						+ actionCount[3]);

			}
		}

		// 点击购买率超过 click2PurchaseRateThreshold 值的商品添加到表中
		// Hashtable<Integer, Integer> highClick2PurchaseItemsTable = new
		// Hashtable<Integer, Integer>();
		Hashtable<Integer, Object> candidateItemsTable = getIndexByItemsData(
				getLeastDayTime(numDay, MYSQLCONFIG.DateMiddle),
				MYSQLCONFIG.DateMiddle, MYSQLCONFIG.isAllAction);

		Enumeration<Integer> candidateItemsItems = candidateItemsTable.keys();
		while (candidateItemsItems.hasMoreElements()) {
			int itemId = candidateItemsItems.nextElement();
			Object candidateItem = candidateItemsTable.get(itemId);
			double click2BuyRateItem = ((ItemWithUsersData) candidateItem)
					.getClick2purchase();

			for (ItemWithUsersData.User myUser : ((ItemWithUsersData) candidateItem)
					.getUsers()) {
				//
				boolean isHighClick2PurchaseUser = highClick2PurchaseUsersTable
						.containsKey(myUser.getUserID());
				boolean isHighClick2PurchaseItem = click2BuyRateItem > click2PurchaseRateThreshold;

				// 点击购买率超过 click2PurchaseRateThreshold 值的用户。
				if (true == isHighClick2PurchaseUser
						&& true == isHighClick2PurchaseItem) {
					boolean isAlreadyInThere = appendResult2HashTable(
							myHashtable, myUser.getUserID(), itemId);
					if (false == isAlreadyInThere) {
						appendResult2HashTable(
								rtHighClick2PurchaseItemsPureTable,
								myUser.getUserID(), itemId);
					} else {
						appendResult2HashTable(myHighPrecisionResultTable,
								myUser.getUserID(), itemId);
					}
				}
			}
		}
		// }

		return rtHighClick2PurchaseItemsPureTable;
	}

	public static String getLeastDayTime(int numDay, String baseDay)
			throws SQLException {

		Calendar myProductcalender = Calendar.getInstance();
		try {
			myProductcalender.setTime(MYSQLCONFIG.dateFormat.parse(baseDay));
		} catch (ParseException e) {
		}

		myProductcalender.add(Calendar.DAY_OF_YEAR, -numDay);
		String newdayTime1 = MYSQLCONFIG.dateFormat.format(myProductcalender
				.getTime());

		return newdayTime1;
	}

	public static void getRidOfLeastUsersItems(int numDayTime,
			Hashtable<Integer, Integer> myActiveUsersTable,
			Hashtable<Integer, Integer> myHotItemsTable, String actionType)
			throws SQLException {

		Hashtable<Integer, Object> userBuyingTable = getIndexByUsersData(
				getLeastDayTime(numDayTime, MYSQLCONFIG.DateMiddle),
				MYSQLCONFIG.DateMiddle, actionType);
		Enumeration<Integer> indexUsers = userBuyingTable.keys();
		int numGetRidOfUsers = 0;
		while (indexUsers.hasMoreElements()) {
			int userId = indexUsers.nextElement();
			boolean isExsit = userBuyingTable.containsKey(userId);
			if (false == isExsit) {
				numGetRidOfUsers++;
				userBuyingTable.remove(userId);
			}
		}

		Hashtable<Integer, Object> itemBuyingTable = getIndexByItemsData(
				getLeastDayTime(numDayTime, MYSQLCONFIG.DateMiddle),
				MYSQLCONFIG.DateMiddle, actionType);
		Enumeration<Integer> indexItems = itemBuyingTable.keys();
		int numGetRidOfItems = 0;
		while (indexItems.hasMoreElements()) {
			int itemId = indexItems.nextElement();
			boolean isExsit = itemBuyingTable.containsKey(itemId);
			if (false == isExsit) {
				numGetRidOfItems++;
				itemBuyingTable.remove(itemId);
			}
		}

		int test = 1;

	}

	// 高点击转换率用户+高 user-item 点击次数+注入时间向量
	public static void addActiveUserClickNotBuy(int day7AsOne,
			Hashtable<Integer, Integer> myActiveUsersTable,
			Hashtable<Integer, LinkedList<Integer>> myResultTable,
			Hashtable<Integer, LinkedList<Integer>> myHashTable)
			throws SQLException {

		Hashtable<Integer, Object> myUsersClickTable = getIndexByUsersData(
				getLeastDayTime(day7AsOne * 7, MYSQLCONFIG.DateMiddle),
				MYSQLCONFIG.DateMiddle, MYSQLCONFIG.isClickAction);
		Enumeration<Integer> indexEmu = myUsersClickTable.keys();
		while (indexEmu.hasMoreElements()) {
			int userId = indexEmu.nextElement();
			boolean isActiveUser = myActiveUsersTable.containsKey(userId);
			if (true == isActiveUser) {
				// 用户点击商品id-点击次数
				Hashtable<Integer, Integer> countClickTable = new Hashtable<Integer, Integer>();
				Object userWithItem = myUsersClickTable.get(userId);
				UserWithItemsData userWithItemt = (UserWithItemsData) userWithItem;
				LinkedList<UserWithItemsData.Product> myItems = userWithItemt
						.getProducts();
				for (UserWithItemsData.Product myItem : myItems) {

					if (countClickTable.containsKey(myItem.getBrandID()) == false) {
						countClickTable.put(myItem.getBrandID(), 1);
					} else {
						countClickTable.put(myItem.getBrandID(),
								countClickTable.get(myItem.getBrandID()) + 1);
					}
				}

				// 用户点击商品的次数大于阈值的推荐。
				Enumeration<Integer> itemsId = countClickTable.keys();
				while (itemsId.hasMoreElements()) {
					int itemId = itemsId.nextElement();
					if (countClickTable.get(itemId) >= 2 + Math
							.log(7 * day7AsOne)) {
						boolean isAlreadyInThere = appendResult2HashTable(
								myHashTable, userId, itemId);
						if (false == isAlreadyInThere) {
							appendResult2HashTable(myResultTable, userId,
									itemId);
						} else {
							appendResult2HashTable(myHighPrecisionResultTable,
									userId, itemId);
						}
					}
				}
			}

		}

	}

	// 高转换率用户和热门商品的俩者的交集
	public static void addHightRateUserClickHotItems(int day7AsOne,
			Hashtable<Integer, Integer> myHotItemsTable,
			Hashtable<Integer, Integer> myActiveUsersTable,
			Hashtable<Integer, LinkedList<Integer>> myResultTable,
			Hashtable<Integer, LinkedList<Integer>> myHashTable)
			throws SQLException {

		// 点击购买率超过 click2PurchaseRateThreshold 值的用户添加到表中
		// Hashtable<Integer, Integer> highClick2PurchaseUsersTable = new
		// Hashtable<Integer, Integer>();
		// Hashtable<Integer, Object> candidateUsersTable = getIndexByUsersData(
		// getLeastDayTime(day7AsOne*7),
		// getLeastDayTime((day7AsOne-1)*7), MYSQLCONFIG.isAllAction);
		//
		// Enumeration<Integer> hotUsers = candidateUsersTable.keys();
		// while (hotUsers.hasMoreElements()) {
		// int itemId = hotUsers.nextElement();
		// Object mostUsersCandiItem = candidateUsersTable.get(itemId);
		// int[] actionCount = ((UserWithItemsData) mostUsersCandiItem)
		// .getUserActionCount();
		// double click2Buy = ((UserWithItemsData) mostUsersCandiItem)
		// .getClick2purchase();
		//
		// if (click2Buy >= 0.05 &&(actionCount[1] + actionCount[3]) >= 10) {
		// highClick2PurchaseUsersTable.put(itemId, actionCount[1]
		// + actionCount[3]);
		//
		// }
		// }

		Hashtable<Integer, Object> myUsersClickTable = getIndexByUsersData(
				getLeastDayTime(day7AsOne * 7, MYSQLCONFIG.DateMiddle),
				MYSQLCONFIG.DateMiddle, MYSQLCONFIG.isClickAction);
		Hashtable<Integer, Integer> tempTable = new Hashtable<Integer, Integer>();
		// 基于这些低价、易耗商品集合，可以给每个点击过这些商品的人推荐。
		Enumeration<Integer> indexEmu = myUsersClickTable.keys();
		while (indexEmu.hasMoreElements()) {
			int userId = indexEmu.nextElement();
			Object userWithItem = myUsersClickTable.get(userId);
			UserWithItemsData userWithItemt = (UserWithItemsData) userWithItem;
			LinkedList<UserWithItemsData.Product> myItems = userWithItemt
					.getProducts();
			for (UserWithItemsData.Product myItem : myItems) {
				boolean isActiveUsers = myActiveUsersTable.containsKey(userId);
				boolean isClickHotItem = myHotItemsTable.containsKey(myItem
						.getBrandID());
				// 点击的是热门商品并且是高转换率用户的
				if (true == isClickHotItem && true == isActiveUsers) {
					boolean isExsit = tempTable
							.containsKey(myItem.getBrandID());
					if (true == isExsit) {
						tempTable.put(myItem.getBrandID(),
								tempTable.get(myItem.getBrandID()) + 1);
					} else {
						tempTable.put(myItem.getBrandID(), 1);
					}
				}

			}

			// 从符合条件的item table 中取用户点击数大于2的商品。
			Enumeration<Integer> indexTemp = tempTable.keys();
			while (indexTemp.hasMoreElements()) {
				int itemId = indexTemp.nextElement();
				if (tempTable.get(itemId) >= 30) {
					boolean isAlreadyInThere = appendResult2HashTable(
							myHashTable, userId, itemId);
					if (false == isAlreadyInThere) {
						appendResult2HashTable(myResultTable, userId, itemId);

					} else {
						appendResult2HashTable(myHighPrecisionResultTable,
								userId, itemId);
					}
				}
			}
		}

		int tes = 1;
	}

	public static Hashtable<Integer, LinkedList<Integer>> leastDayClickButNotBuy2Table(
			int numLeastDay,
			Hashtable<Integer, LinkedList<Integer>> myFinalResultTable) {

		Hashtable<Integer, LinkedList<Integer>> myleastDayClickTable = new Hashtable<Integer, LinkedList<Integer>>();
		Hashtable<Integer, LinkedList<Integer>> myleastDayBuyingTable = new Hashtable<Integer, LinkedList<Integer>>();
		Hashtable<Integer, LinkedList<Integer>> resultTable = new Hashtable<Integer, LinkedList<Integer>>();

		for (int j = 1; j <= numLeastDay; j++) {
			try {
				getLeastDayTable((int) Math.log(j) + 2, j,
						MYSQLCONFIG.isClickAction, myleastDayClickTable);

				// getLeastDayTable(1, j+3, MYSQLCONFIG.isOnlypurchaseAction,
				// myleastDayBuyingTable);

				Enumeration<Integer> indexEmue = myleastDayClickTable.keys();
				while (indexEmue.hasMoreElements()) {
					int index = indexEmue.nextElement();
					LinkedList<Integer> myClickItems = myleastDayClickTable
							.get(index);

					if (myleastDayBuyingTable.containsKey(index) == false) {
						resultTable.put(index, myClickItems);
					} else {
						LinkedList<Integer> results = new LinkedList<Integer>();
						LinkedList<Integer> myBuyingItems = myleastDayBuyingTable
								.get(index);
						for (int i = 0; i < myClickItems.size(); i++) {
							if (myBuyingItems.contains(myClickItems.get(i)) == false) {
								results.add(myClickItems.get(i));
							}
						}
						resultTable.put(index, results);
					}

				}
				// printCountOfTable(resultTable);
				// 添加到总表中
				Enumeration<Integer> indexEmu = resultTable.keys();
				while (indexEmu.hasMoreElements()) {
					int index = indexEmu.nextElement();
					LinkedList<Integer> myClickItems = resultTable.get(index);
					for (int i = 0; i < myClickItems.size(); i++) {
						appendResult2HashTable(myFinalResultTable, index,
								myClickItems.get(i));
					}

				}
			} catch (SQLException e) {
			}
		}

		return resultTable;
	}

	/**
	 * 方法名称：getSortedHashtable 参数：Hashtable h 引入被处理的散列表
	 * 描述：将引入的hashtable.entrySet进行排序，并返回
	 */
	public static Map.Entry[] getSortedHashtableByKey(Hashtable h) {

		Set set = h.entrySet();

		Map.Entry[] entries = (Map.Entry[]) set.toArray(new Map.Entry[set
				.size()]);

		Arrays.sort(entries, new Comparator() {
			public int compare(Object arg0, Object arg1) {
				Object key1 = ((Map.Entry) arg0).getKey();
				Object key2 = ((Map.Entry) arg1).getKey();
				return ((Comparable) key1).compareTo(key2);
			}

		});

		return entries;
	}

	/**
	 * 方法名称：getSortedHashtable 参数：Hashtable h 引入被处理的散列表
	 * 描述：将引入的hashtable.entrySet进行排序，并返回
	 */
	@SuppressWarnings("unchecked")
	public static Map.Entry[] getSortedHashtableByValue(Hashtable h,
			final boolean isDesc) {

		Set set = h.entrySet();

		Map.Entry[] entries = (Map.Entry[]) set.toArray(new Map.Entry[set
				.size()]);

		Arrays.sort(entries, new Comparator() {
			public int compare(Object arg0, Object arg1) {
				// Object key1 = ((Map.Entry) arg0).getKey();
				// Object key2 = ((Map.Entry) arg1).getKey();

				Object key1 = ((Map.Entry) arg0).getValue();
				int[] action1 = ((ItemWithUsersData) key1).getItemActionCount();

				Object key2 = ((Map.Entry) arg1).getValue();
				int[] action2 = ((ItemWithUsersData) key2).getItemActionCount();

				if (true == isDesc) {

					return ((Comparable) action1[1]).compareTo(action2[1]);
				} else {

					return ((Comparable) action2[1]).compareTo(action1[1]);
				}
			}

		});

		return entries;
	}

	public static ArrayList<Map.Entry<Integer, Double>> sortValue(
			Hashtable<Integer, Double> t) {

		// Transfer as List and sort it
		ArrayList<Map.Entry<Integer, Double>> l = new ArrayList(t.entrySet());
		Collections.sort(l, new Comparator<Map.Entry<Integer, Double>>() {

			public int compare(Map.Entry<Integer, Double> o1,
					Map.Entry<Integer, Double> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});

		return l;
	}

	public static Hashtable<Integer, LinkedList<Integer>> getRidOfDuplicate(
			Hashtable<Integer, Object> myItemsTable,
			Hashtable<Integer, LinkedList<Integer>> myCompareItemsTable)
			throws Exception {

		Enumeration<Integer> indexEmu = myCompareItemsTable.keys();
		while (indexEmu.hasMoreElements()) {
			int itemId = indexEmu.nextElement();

			if (true == myItemsTable.containsKey(itemId)) {
				// 遍历 myItemsTable 并筛选出重复的记录。
				LinkedList<Integer> myComUsers = myCompareItemsTable
						.get(itemId);
				Object myItems = myItemsTable.get(itemId);
				ItemWithUsersData myItemt = (ItemWithUsersData) myItems;
				LinkedList<ItemWithUsersData.User> myUsers = myItemt.getUsers();
				int numRemove = 0;
				for (int i = 0; i < myUsers.size();) {
					ItemWithUsersData.User myUser = myUsers.get(i);
					boolean isDuplicate = myComUsers.contains(myUser
							.getUserID());
					if (true == isDuplicate) {
						myUsers.remove(i);
						numRemove++;
					} else {
						i++;
					}

				}
			}

		}

		return null;
	}

	public static int removeThisUserId(
			LinkedList<ItemWithUsersData.User> myUsers, int userId)
			throws Exception {

		int numRemove = 0;
		for (int i = 0; i < myUsers.size();) {
			ItemWithUsersData.User myUser = myUsers.get(i);
			if (myUser.getUserID() == userId) {
				myUsers.remove(i);
				numRemove++;
			} else {
				i++;
			}
		}
		return numRemove;
	}

	public static Hashtable<Integer, Object> getHighRateItems(double shredHold,
			Hashtable<Integer, Object> myUsersTable,
			Hashtable<Integer, Object> myItemsTable) throws Exception {

		Hashtable<Integer, Object> rtTable = new Hashtable<Integer, Object>();
		Enumeration<Integer> itemsIds = myItemsTable.keys();
		while (itemsIds.hasMoreElements()) {
			int itemId = itemsIds.nextElement();
			Object myItem = myItemsTable.get(itemId);
			ItemWithUsersData newItem = new ItemWithUsersData();
			for (ItemWithUsersData.User myUser : ((ItemWithUsersData) myItem)
					.getUsers()) {
				int userId = myUser.getUserID();
				Object cUser = myUsersTable.get(userId);
				double rate = ((UserWithItemsData) cUser).getClick2purchase();
				if (rate >= shredHold) {
					newItem.addUserToList(myUser);
				}

			}
			if (newItem.getUsers().size() != 0) {
				newItem.setProductID(itemId);
				rtTable.put(itemId, newItem);
			}
		}

		return rtTable;
	}

	public static ItemSimilarity getItemsNeighborhood(
			LinkedList<Object> itemsWithUsers) throws Exception {

		FastByIDMap<PreferenceArray> preferenceMap = new FastByIDMap<PreferenceArray>();
		// Hashtable<Integer, Integer> numUserBuyItemsTable = new
		// Hashtable<Integer, Integer>();
		DataModel dataModelAll;
		// get unique item preference for one user
		for (Object itemsWithUser : itemsWithUsers) {
			ItemWithUsersData itemsWithUsert = (ItemWithUsersData) itemsWithUser;
			Hashtable<Integer, Float> usersWithWeightTable = new Hashtable<Integer, Float>();

			for (ItemWithUsersData.User myUser : itemsWithUsert.getUsers()) {
				float currentUserPref = 0;
				boolean isAlreadyContainUserId = usersWithWeightTable
						.containsKey(myUser.getUserID());

				if (true == isAlreadyContainUserId) {

					currentUserPref = usersWithWeightTable.get(myUser
							.getUserID());
				}

				int brandPref = getItemWeight(myUser.getType());
				currentUserPref += brandPref;
				usersWithWeightTable.put(myUser.getUserID(), currentUserPref);
			}

			Enumeration<Integer> usersIds = usersWithWeightTable.keys();
			List<Preference> itemPreferences = new ArrayList<Preference>();
			while (usersIds.hasMoreElements()) {
				int usersId = usersIds.nextElement();
				float userPref = usersWithWeightTable.get(usersId);
				itemPreferences.add(new GenericPreference(itemsWithUsert
						.getProductID(), usersId, userPref));
			}
			GenericUserPreferenceArray itemsArray = new GenericUserPreferenceArray(
					itemPreferences);
			preferenceMap.put(itemsWithUsert.getProductID(), itemsArray);

			// int[] userAction = itemsWithUsert.getItemActionCount();
			// numUserBuyItemsTable.put(itemsWithUsert.getProductID(),
			// userAction[1] + userAction[3]);

		}
		dataModelAll = new GenericDataModel(preferenceMap);
		ItemSimilarity similarity = new LogLikelihoodSimilarity(dataModelAll);
		// ItemBasedRecommender recommender = new
		// GenericBooleanPrefItemBasedRecommender(
		// dataModelAll, similarity);
		// UserSimilarity similarity = new PearsonCorrelationSimilarity(
		// dataModelAll);
		// UserNeighborhood neighborhood = new NearestNUserNeighborhood(10,
		// similarity, dataModelAll);
		return similarity;
	}

	public static Hashtable<Integer, LinkedList<Integer>> getRecommendItems(
			String dayTime1, String dayTime2, String userActionType,
			int recommenderType) throws Exception {

		Hashtable<Integer, Integer> numUserBuyItemsTable = new Hashtable<Integer, Integer>();
		Hashtable<Integer, LinkedList<Integer>> myresultTable = new Hashtable<Integer, LinkedList<Integer>>();

		FastByIDMap<PreferenceArray> preferenceMap = new FastByIDMap<PreferenceArray>();
		AbstractRecommender recommender = null;
		DataModel dataModelAll;
		Hashtable<Integer, Object> userWithItemsTable = getIndexByUsersData(
				dayTime1, dayTime2, userActionType);
		// LinkedList<Object> userWithItems = getDataIndexByUsersETL(dayTime,
		// userActionType);

		// get unique item preference for one user
		Enumeration<Integer> indexEmu = userWithItemsTable.keys();
		while (indexEmu.hasMoreElements()) {
			int index = indexEmu.nextElement();
			Object userWithItem = userWithItemsTable.get(index);
			UserWithItemsData userWithItemt = (UserWithItemsData) userWithItem;
			Hashtable<Integer, Float> itemsWithWeightTable = new Hashtable<Integer, Float>();

			for (UserWithItemsData.Product myProduct : userWithItemt
					.getProducts()) {
				float currentBrandPref = 0;
				boolean isAlreadyContainBrandId = itemsWithWeightTable
						.containsKey(myProduct.getBrandID());

				if (true == isAlreadyContainBrandId) {

					currentBrandPref = itemsWithWeightTable.get(myProduct
							.getBrandID());
				}

				int brandPref = getItemWeight(myProduct.getType());
				currentBrandPref += brandPref;
				itemsWithWeightTable.put(myProduct.getBrandID(),
						currentBrandPref);
			}

			Enumeration<Integer> brandIds = itemsWithWeightTable.keys();
			List<Preference> userPreferences = new ArrayList<Preference>();
			while (brandIds.hasMoreElements()) {
				int brandId = brandIds.nextElement();
				float brandPref = itemsWithWeightTable.get(brandId);
				userPreferences.add(new GenericPreference(userWithItemt
						.getUserID(), brandId, brandPref));
			}
			GenericUserPreferenceArray userArray = new GenericUserPreferenceArray(
					userPreferences);
			preferenceMap.put(userWithItemt.getUserID(), userArray);

			int[] userAction = userWithItemt.getUserActionCount();
			numUserBuyItemsTable.put(userWithItemt.getUserID(), userAction[1]
					+ userAction[3]);

		}

		dataModelAll = new GenericDataModel(preferenceMap);
		UserSimilarity similarity = new PearsonCorrelationSimilarity(
				dataModelAll);
		UserNeighborhood neighborhood = new NearestNUserNeighborhood(20,
				similarity, dataModelAll);

		// get svd recommend
		Enumeration<Integer> indexEmue = userWithItemsTable.keys();
		while (indexEmue.hasMoreElements()) {
			int index = indexEmue.nextElement();
			Object userWithItem = userWithItemsTable.get(indexEmu);
			UserWithItemsData userWithItemt = (UserWithItemsData) userWithItem;
			long curUserID = userWithItemt.getUserID();

			FastByIDMap<PreferenceArray> clsfPreferenceMap = new FastByIDMap<PreferenceArray>();
			long[] myneighborhood = neighborhood.getUserNeighborhood(curUserID);
			for (int i = 0; i < myneighborhood.length; i++) {
				PreferenceArray userArray = preferenceMap
						.get(myneighborhood[i]);
				clsfPreferenceMap.put(myneighborhood[i], userArray);
			}
			clsfPreferenceMap.put(curUserID, preferenceMap.get(curUserID));

			DataModel clsfDataModel = new GenericDataModel(clsfPreferenceMap);
			recommender = new SVDRecommender(clsfDataModel,
					new SVDPlusPlusFactorizer(clsfDataModel, 0, 10));

			int howMany = numUserBuyItemsTable.get((int) curUserID) + 1;
			LinkedList<Integer> items = new LinkedList<Integer>();
			for (RecommendedItem r : recommender.recommend(curUserID, howMany)) {
				long itemId = r.getItemID();
				if (itemId < Integer.MIN_VALUE || itemId > Integer.MAX_VALUE) {
					throw new IllegalArgumentException(
							itemId
									+ " cannot be cast to int without changing its value.");
				}
				items.add((int) itemId);

			}
			myresultTable.put((int) curUserID, items);

		}

		return myresultTable;
	}

	// public static void optimizePredictResultTest() throws Exception {

	// Random r = new Random();
	// for (int i = 0; i <= 100; i++) {
	// // 生成[0,10)区间的小数
	// int dRandomA = r.nextInt(10);
	// // System.out.printf("dRecallRandom %.4f", dRecallRandom);
	// // 生成[1,5)区间的小数
	// int dRandomB = r.nextInt(4)+1;
	// Hashtable<Integer, LinkedList<Integer>> cycleBuyinyTable =
	// cycleBuyinyAdd2Table(
	// MYSQLCONFIG.DateStartPoint, dRandomA, dRandomB,
	// myFinalResultTable);
	// StatisticsResultData mycycleBuyinyAdd2TableStatistic =
	// statisticsResult(cycleBuyinyTable);
	//
	// if(mycycleBuyinyAdd2TableStatistic.getF1Score() >=0.02){
	// System.out.printf(mycycleBuyinyAdd2TableStatistic.toString()
	// + "\tdRandomA:" + dRandomA + "\tdRandomB:" + dRandomB
	// + "\n");
	//
	// }
	// // writeResultToTXT(cycleBuyinyTable, "cycleBuyinyTable");
	//
	// }

	// // LinkedList<Object> myStatistics = new LinkedList<Object>();
	// LinkedList<StatisticsResultData> myStatistics = new
	// LinkedList<StatisticsResultData>();
	//
	// Calendar cal = Calendar.getInstance();
	// try {
	// cal.setTime(MYSQLCONFIG.dateFormat
	// .parse(MYSQLCONFIG.DateAxialPoint));
	// } catch (ParseException e1) {
	// // do nothing
	// }
	//
	// for (int i = 1; i <= 13; i++) {
	// try {
	// cal.add(Calendar.DAY_OF_YEAR, -7);
	// Hashtable<Integer, LinkedList<Integer>> predictBuyItemsTable =
	// getPredictItems(
	// MYSQLCONFIG.dateFormat.format(cal.getTime()),
	// MYSQLCONFIG.isAllAction);
	// Hashtable<Integer, LinkedList<Integer>> mycycleBuyinyAdd2Table =
	// cycleBuyinyAdd2Table(
	// MYSQLCONFIG.DateStartPoint, predictBuyItemsTable);
	//
	// Hashtable<Integer, LinkedList<Integer>> resultf =
	// fPGrowthFilterAdd2Table(
	// 751, "fList.seq", "frequentpatterns.seq", 0.001, 0.1,
	// predictBuyItemsTable);
	//
	// StatisticsResultData myStatistic =
	// statisticsResult(predictBuyItemsTable);
	// myStatistics.add(myStatistic);
	//
	// } catch (SQLException e) {
	// // do nothing
	// }
	// }
	//
	// if (ETLCONFIG.ISDEBUGMODEL) {
	// writeStatistics2ExcelFile(myStatistics);
	// }
	//
	// }

	// public static void optimizeFPGrowthResultTest() throws Exception {
	//
	// Random r = new Random();
	// LinkedList<StatisticsResultData> myFPGrowthStatistics = new
	// LinkedList<StatisticsResultData>();
	// for (int i = 0; i <= 1; i++) {
	// // 生成[0,0.005)区间的小数
	// double dRecallRandom = r.nextDouble() / 200;
	// // System.out.printf("dRecallRandom %.4f", dRecallRandom);
	// // 生成[0,1.0)区间的小数
	// double dPrecisonRandom = r.nextDouble();
	//
	// // Hashtable<Integer, LinkedList<Integer>>
	// // myappendFPGrowth2Table = appendFPGrowth2Table(
	// // 686, null, "fList.seq", "frequentpatterns.seq", 0.005, 0.1);
	// Hashtable<Integer, LinkedList<Integer>> myAppendFPGrowth2Table =
	// fPGrowthFilterAdd2Table(
	// 686, "fList.seq", "frequentpatterns.seq", dRecallRandom,
	// dPrecisonRandom, myFinalResultTable);
	//
	// StatisticsResultData myFPGrowthStatistic =
	// statisticsResult(myAppendFPGrowth2Table);
	//
	// System.out
	// .printf("dRecallRandom:%.4f dPrecisonRandom:%.4f Precison:%.4f recall:%.4f f1score:%.4f \n",
	// dRecallRandom, dPrecisonRandom,
	// myFPGrowthStatistic.getPrecision(),
	// myFPGrowthStatistic.getRecall(),
	// myFPGrowthStatistic.getF1Score());
	//
	// myFPGrowthStatistics.add(myFPGrowthStatistic);
	// }
	// if (ETLCONFIG.ISDEBUGMODEL) {
	// writeStatistics2ExcelFile(myFPGrowthStatistics);
	// }
	// }

	/**
	 * This method get least 7daybuying items
	 */
	public static Hashtable<Integer, LinkedList<Integer>> getLeastDayTable(
			int minNum, int leastDayTimeNmuber, String userActionType,
			Hashtable<Integer, LinkedList<Integer>> myHashtable)
			throws SQLException {

		Hashtable<Integer, Object> userWithItemsTable = getIndexByUsersData(
				getLeastDayTime(leastDayTimeNmuber, MYSQLCONFIG.DateMiddle),
				MYSQLCONFIG.DateMiddle, userActionType);
		// userWithItemsTable = getItemsIndexByUsersETL(userWithItemsTable);
		Hashtable<Integer, LinkedList<Integer>> resultUserItems = new Hashtable<Integer, LinkedList<Integer>>();

		Enumeration<Integer> indexEmu = userWithItemsTable.keys();
		while (indexEmu.hasMoreElements()) {
			int index = indexEmu.nextElement();
			Object userWithItem = userWithItemsTable.get(index);
			UserWithItemsData usert = (UserWithItemsData) userWithItem;
			LinkedList<UserWithItemsData.Product> products = usert
					.getProducts();

			Collections.sort(products,
					new Comparator<UserWithItemsData.Product>() {
						@Override
						public int compare(UserWithItemsData.Product o1,
								UserWithItemsData.Product o2) {
							return Integer.valueOf(o1.getBrandID()).compareTo(
									o2.getBrandID());
						}
					});

			int oldBrandId = products.get(0).getBrandID();
			int count = 1;
			for (int k = 1; k < products.size(); k++) {
				int curBrandId = products.get(k).getBrandID();
				if (curBrandId == oldBrandId) {
					count++;
				} else {
					if (count >= minNum) {
						boolean isAlreadyInThere = appendResult2HashTable(
								myHashtable, usert.getUserID(), curBrandId);
						if (false == isAlreadyInThere) {

							appendResult2HashTable(resultUserItems,
									usert.getUserID(), curBrandId);
						} else {
							appendResult2HashTable(myHighPrecisionResultTable,
									usert.getUserID(), curBrandId);
						}
					}
					// 归零
					oldBrandId = curBrandId;
					count = 1;
				}

			}

		}

		return resultUserItems;
	}

	/**
	 * This method get least 7daybuying items
	 */
	public static Hashtable<Integer, Object> getLeastNumItemsTable(int minNum,
			Hashtable<Integer, Object> itemsTable) throws SQLException {

		// Hashtable<Integer, Object> userWithItemsTable = getIndexByUsersData(
		// getLeastDayTime(leastDayTimeNmuber, MYSQLCONFIG.DateMiddle),
		// MYSQLCONFIG.DateMiddle, userActionType);
		// userWithItemsTable = getItemsIndexByUsersETL(userWithItemsTable);
		Hashtable<Integer, Object> resultItems = new Hashtable<Integer, Object>();

		Enumeration<Integer> indexEmu = itemsTable.keys();
		while (indexEmu.hasMoreElements()) {
			int index = indexEmu.nextElement();
			Object items = itemsTable.get(index);
			ItemWithUsersData itemt = (ItemWithUsersData) items;
			LinkedList<ItemWithUsersData.User> users = itemt.getUsers();

			Collections.sort(users, new Comparator<ItemWithUsersData.User>() {
				@Override
				public int compare(ItemWithUsersData.User o1,
						ItemWithUsersData.User o2) {
					return Integer.valueOf(o1.getUserID()).compareTo(
							o2.getUserID());
				}
			});

			int oldUserId = -1;
			int oldIndex = 0;
			int count = 0;
			LinkedList<ItemWithUsersData.User> myNewUsers = new LinkedList<ItemWithUsersData.User>();
			// 从开始遍历
			for (int k = 0; k < users.size(); k++) {
				int curUserId = users.get(k).getUserID();
				if (curUserId == oldUserId) {
					count++;
				} else {
					if (count >= minNum) {
						// 把下标从 oldIndex 到 count+oldIndex 的记录添加到新表中。
						for (int j = oldIndex; j < count + oldIndex; j++) {
							myNewUsers.add(users.get(j));
						}
					}
					// 归零
					oldUserId = curUserId;
					count = 1;
					oldIndex = k;
				}

				// 如果是最后一个记录的话
				if (k == users.size() - 1 && count >= minNum) {
					// 把下标从 oldIndex 到 count+oldIndex 的记录添加到新表中。
					for (int j = oldIndex; j < count + oldIndex; j++) {
						myNewUsers.add(users.get(j));
					}
				}
			}

			if (myNewUsers.size() >= 1) {
				ItemWithUsersData myNewitem = new ItemWithUsersData();
				myNewitem.setProductID(itemt.getProductID());
				myNewitem.setUsers(myNewUsers);
				resultItems.put(itemt.getProductID(), myNewitem);
			}
		}

		return resultItems;
	}

	// get trainleaner
	public static int printCountOfTable(
			Hashtable<Integer, LinkedList<Integer>> myItemsIfUserClickIt) {

		int count = 0;
		Enumeration<Integer> i = myItemsIfUserClickIt.keys();
		while (i.hasMoreElements()) {
			int index = i.nextElement();
			LinkedList<Integer> userWithItem = myItemsIfUserClickIt.get(index);
			count += userWithItem.size();

		}
		System.out.println("预测了" + count + "个");
		return count;
	}

	//
	// // ///////////////// predict
	// public static Hashtable<Integer, LinkedList<Integer>> lRAddPredict2Table(
	// double threshold, Hashtable<Integer, Integer> predictItemsTable,
	// LaneCrossFoldLearner myCrossFoldLearner,
	// Hashtable<Integer, LinkedList<Integer>> resultTable)
	// throws SQLException {
	//
	// if (null == myCandidatePredictUsersTable) {
	// // 04-07 three month users's allAction count
	// myCandidatePredictUsersTable = getIndexByUsersData(
	// MYSQLCONFIG.DatelRPredictBegin, MYSQLCONFIG.DateEnd,
	// MYSQLCONFIG.isAllAction);
	// myCandidatePredictUsersTable =
	// getIndexByUsersDataETL(myCandidatePredictUsersTable);
	// }
	//
	// if (null == myCandidatePredictItemsTable) {
	// // 05-08 three month history records are to be predicted.
	// myCandidatePredictItemsTable = getIndexByItemsData(
	// MYSQLCONFIG.DatelRPredictBegin, MYSQLCONFIG.DateEnd,
	// MYSQLCONFIG.isAllAction);
	// myCandidatePredictItemsTable =
	// getIndexByItemsDataETL(myCandidatePredictItemsTable);
	// }
	//
	// LinkedList<ItemWithUsersData> myPredictItems = new
	// LinkedList<ItemWithUsersData>();
	// Hashtable<Integer, LinkedList<Integer>> resultItemsTable = new
	// Hashtable<Integer, LinkedList<Integer>>();
	//
	// Enumeration<Integer> indexEmu = myCandidatePredictItemsTable.keys();
	// while (indexEmu.hasMoreElements()) {
	// int index = indexEmu.nextElement();
	// Object myItem = myCandidatePredictItemsTable.get(index);
	// ItemWithUsersData myItemt = (ItemWithUsersData) myItem;
	// boolean isContain = predictItemsTable.containsKey(myItemt
	// .getProductID());
	// if (isContain) {
	// myPredictItems.add(myItemt);
	// }
	//
	// }
	//
	// LinkedList<userItemScalar> myuserItemScalars = new
	// LinkedList<userItemScalar>();
	// for (Object myPredictItem : myPredictItems) {
	// ItemWithUsersData myPredictItemt = (ItemWithUsersData) myPredictItem;
	// LogResTrainData myLogResPredictData = new LogResTrainData();
	// myLogResPredictData.lRForgeDataModel(myPredictItemt, null,
	// myCandidatePredictUsersTable);
	//
	// ArrayList<Integer> userVector = myLogResPredictData.getUserVector();
	// List<Vector> valueVector = myLogResPredictData.getValueVector();
	// // int isBuying = 0;
	// for (int k = 0; k < valueVector.size(); k++) {
	// // isBuying =
	// // myCrossFoldLearner.classifyFull(valueVector.get(k))
	// // .maxValueIndex();
	// double scalar = myCrossFoldLearner.classifyScalar(valueVector
	// .get(k));
	// if (scalar >= threshold) {
	// userItemScalar myuserItemScalar = new userItemScalar();
	// myuserItemScalar.setItemId(myPredictItemt.getProductID());
	// myuserItemScalar.setScalar(scalar);
	// myuserItemScalar.setUserId(userVector.get(k));
	//
	// myuserItemScalars.add(myuserItemScalar);
	// }
	//
	// }
	//
	// }
	//
	// Collections.sort(myuserItemScalars, new Comparator<userItemScalar>() {
	// @Override
	// public int compare(userItemScalar o1, userItemScalar o2) {
	// return Double.valueOf(o2.getScalar()).compareTo(o1.getScalar());
	// }
	// });
	//
	// int minSize = Math.min(myuserItemScalars.size(), 200);
	// // get top minSize
	// for (int i = 0; i < minSize; i++) {
	// userItemScalar cur = myuserItemScalars.get(i);
	// appendResult2HashTable(resultItemsTable, cur.getUserId(),
	// cur.getItemId());
	//
	// appendResult2HashTable(resultTable, cur.getUserId(),
	// cur.getItemId());
	// }
	// return resultItemsTable;
	//
	// }

	// public static int addConsumableItemsWithLR(String dayTime,
	// Hashtable<Integer, Integer> consumableItemsTable,
	// Hashtable<Integer, LinkedList<Integer>> resultItemsTable,
	// Hashtable<Integer, LinkedList<Integer>> myHashtable)
	// throws SQLException {
	//
	// CrossFoldLearner myCrossFoldLearner =
	// lRGetTrainItems(consumableItemsTable);
	//
	// // ///////////////////////////predict
	// LinkedList<Object> myPredictItems = getUsersIndexByItems("2013-05-15",
	// "2013-08-15", MYSQLCONFIG.isAllAction);
	// LinkedList<ItemWithUsersData> myPredictTrainItemsList = new
	// LinkedList<ItemWithUsersData>();
	// for (Object myItem : myPredictItems) {
	// ItemWithUsersData myItemt = (ItemWithUsersData) myItem;
	// boolean isContain = consumableItemsTable.containsKey(myItemt
	// .getProductID());
	// if (isContain) {
	// myPredictTrainItemsList.add(myItemt);
	// }
	//
	// }
	//
	// for (Object myPredictTrainItem : myPredictTrainItemsList) {
	// ItemWithUsersData myPredictTrainItemt = (ItemWithUsersData)
	// myPredictTrainItem;
	//
	// LogResTrainData myLogResPredictData = new LogResTrainData();
	// myLogResPredictData.forgeLogResData(myPredictTrainItemt, null,
	// "2013-08-15");
	//
	// ArrayList<Integer> userVector = myLogResPredictData.getUserVector();
	// List<Vector> valueVector = myLogResPredictData.getValueVector();
	// int isBuying = 0;
	// for (int k = 0; k < valueVector.size(); k++) {
	// isBuying = myCrossFoldLearner.classifyFull(valueVector.get(k))
	// .maxValueIndex();
	// if (1 == isBuying) {
	// appendResult2HashTable(resultItemsTable, userVector.get(k),
	// myPredictTrainItemt.getProductID());
	//
	// appendResult2HashTable(myHashtable, userVector.get(k),
	// myPredictTrainItemt.getProductID());
	//
	// }
	// }
	//
	// }
	//
	// return 0;
	// }

	/**
	 * This method put every <key value> result to one big final hashtable，
	 * 
	 * @param userId
	 *            ，represent key
	 * @param brandId
	 *            ，represent values
	 */
	public static boolean appendResult2HashTable(
			Hashtable<Integer, LinkedList<Integer>> myTable, int userId,
			int brandId) throws SQLException {
		boolean isAlreadyInThere = true;
		if (myTable.containsKey(userId)) {
			LinkedList<Integer> items = myTable.get(userId);
			boolean isContain = items.contains(brandId);
			if (false == isContain) {
				items.add(brandId);
				myTable.put(userId, items);
				isAlreadyInThere = false;
			}
		} else {
			LinkedList<Integer> items = new LinkedList<Integer>();
			items.add(brandId);
			myTable.put(userId, items);
			isAlreadyInThere = false;
		}
		//
		// if (false == isAlreadyInThere && resultType != -1) {
		//
		// UserWithPredictCountData myUserWithPredictCountData =
		// myUserBuyingItemsNPerMonth
		// .get(userId);
		// int[] fPGrowthFilterItemsN = myUserWithPredictCountData
		// .getUserPredictCount();
		// fPGrowthFilterItemsN[resultType]++;
		// myUserWithPredictCountData
		// .setUserPredictCount(fPGrowthFilterItemsN);
		// myUserBuyingItemsNPerMonth.put(userId, myUserWithPredictCountData);
		// }

		return isAlreadyInThere;
	}

	/**
	 * This method calculates the result's precision/recall/f1score.
	 * 
	 * @param dayTime
	 *            ,predictBuyItems daytime interval, between
	 *            MYSQLCONFIG.DateThreshold and dayTime,like from "2013-07-15"
	 *            to "2013-06-15"
	 * @return parameters PredictDateHolder, includes precision/recall/f1score
	 *         scores.
	 */
	public static StatisticsResultData statisticsResult(String dayTime1,
			String dayTime2,
			Hashtable<Integer, LinkedList<Integer>> predictBuyItemsNTable)
			throws SQLException {
		StatisticsResultData hiteditemsStatistics = new StatisticsResultData();

		try {
			// String dayTime1 = MYSQLCONFIG.DateMiddle;
			// String dayTime2 = MYSQLCONFIG.DateEnd;

			Hashtable<Integer, Object> userWithItemsTable = getIndexByUsersData(
					dayTime1, dayTime2, MYSQLCONFIG.isBuyOrCartAction);

			// LinkedList<Object> StaticsUsers = new LinkedList<Object>();
			// allHitBrands对用户i预测的品牌列表与用户i真实购买的品牌交集的个数
			double allHitBrands = 0;
			// allpBrands为对用户i 预测他(她)会购买的品牌列表个数
			double allpBrands = 0;
			// allbBrands为用户i 真实购买的品牌个数
			double allbBrands = 0;

			Enumeration<Integer> key = predictBuyItemsNTable.keys();
			while (key.hasMoreElements()) {
				int userId = key.nextElement();
				allpBrands += predictBuyItemsNTable.get(userId).size();
			}

			Enumeration<Integer> indexEmu = userWithItemsTable.keys();
			while (indexEmu.hasMoreElements()) {
				int index = indexEmu.nextElement();
				Object userWithItem = userWithItemsTable.get(index);
				UserWithItemsData actualBuyItems = (UserWithItemsData) userWithItem;
				int userId = actualBuyItems.getUserID();
				allbBrands += actualBuyItems.getProducts().size();

				boolean isContainsKey = predictBuyItemsNTable
						.containsKey(userId);
				if (isContainsKey) {
					for (UserWithItemsData.Product actualBuyItem : actualBuyItems
							.getProducts()) {
						LinkedList<Integer> predictBuyItems = predictBuyItemsNTable
								.get(userId);
						boolean isContainThisProduct = predictBuyItems
								.contains(actualBuyItem.getBrandID());
						if (isContainThisProduct) {
							allHitBrands++;

							// System.out
							// .println(" select * from tmail_firstseason where`user_id`= "
							// + userId
							// + "  and `brand_id` = "
							// + actualBuyItem.getBrandID());
						}
					}
				}

			}

			double allPrecision = allHitBrands / allpBrands;
			double allRecall = allHitBrands / allbBrands;
			double allF1 = 2 * allPrecision * allRecall
					/ (allRecall + allPrecision);

			hiteditemsStatistics.setPrecision(allPrecision);
			hiteditemsStatistics.setRecall(allRecall);
			hiteditemsStatistics.setF1Score(allF1);
			// predictBuy.setDayTime(dayTime);
			hitbrand += allHitBrands;
			all += allpBrands;
			System.out.println(" hitbrand " + hitbrand + " precision "
					+ hitbrand / all);
			if (ETLCONFIG.ISDEBUGMODEL) {
				// write2File(StaticsUsers);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return hiteditemsStatistics;
	}

	/**
	 * This method write fPGrowth recommend items to user hashtalbe
	 * 
	 * @param configuration
	 * @param frequentPatternsFileName
	 * @param transactionCount
	 *            ,overall users's number who buying products.
	 * @param frequency
	 * @param minSupport
	 * @param minConfidence
	 * @return parameters Hashtable<Integer, LinkedList<Integer>>
	 */
	public static Hashtable<Integer, LinkedList<Integer>> fPGrowthFilterAdd2Table(
			LinkedList<Object> UserHasAlreadyBuyitems, double support,
			double confidence,
			Hashtable<Integer, LinkedList<Integer>> myHashtable) {

		Hashtable<Integer, LinkedList<Integer>> resultUsersTable = new Hashtable<Integer, LinkedList<Integer>>();
		Hashtable<Integer, LinkedList<Integer>> myFPGrowthItemsTable = null;
		// LinkedList<Object> UserHasAlreadyBuyitems = null;
		try {
			// UserHasAlreadyBuyitems = getDataIndexByUsersETL(dayTime,
			// userActionType);
			// UserHasAlreadyBuyitems = getDataIndexByUsersETL(
			// MYSQLCONFIG.DateStartPoint, MYSQLCONFIG.isAllAction);
			int itemsN = fPGrowthPrepareSeqFile(UserHasAlreadyBuyitems);

			Configuration configuration = new Configuration();
			Map<Integer, Long> frequency = fPGrowthFilterReadFrequency(
					configuration, "fList.seq");
			myFPGrowthItemsTable = fPGrowthFilterReadFrequentPatterns(
					configuration, "frequentpatterns.seq", itemsN, frequency,
					support, confidence);

			for (Object UserHasAlreadyBuyitem : UserHasAlreadyBuyitems) {
				UserWithItemsData UserHasAlreadyBuyitemt = (UserWithItemsData) UserHasAlreadyBuyitem;
				int userId = UserHasAlreadyBuyitemt.getUserID();
				// int count = 0;

				for (UserWithItemsData.Product product : UserHasAlreadyBuyitemt
						.getProducts()) {
					if (myFPGrowthItemsTable.containsKey(product.getBrandID())) {
						LinkedList<Integer> itemsValue = myFPGrowthItemsTable
								.get(product.getBrandID());
						for (Integer itemValue : itemsValue) {
							appendResult2HashTable(resultUsersTable, userId,
									itemValue);

							appendResult2HashTable(myHashtable, userId,
									itemValue);

						}
						// addSingleton2HashTable(resultUsersTable, userId,
						// product.getBrandID());
						//
						// addSingleton2HashTable(myHashtable, userId,
						// product.getBrandID());

						// LinkedList<Integer> items = myFPGrowthItemsTable
						// .get(productt.getBrandID());

						// for (Integer item : items) {
						// addResult2HashTable(myFPGrowthUsersTable, userId,
						// item, MYSQLCONFIG.isNilType);
						//
						// // add to myOverallResult hashtable
						// boolean isAlreadyInThere = addResult2HashTable(
						// myHashtable, userId, item,
						// MYSQLCONFIG.isFPGrowthType);
						//
						// if (false == isAlreadyInThere) {
						// count++;
						// }
						// addResult2HashTable(myFPGrowthUsersTable, userId,
						// productt.getBrandID(), MYSQLCONFIG.isNilType);
						//
						// // add to myOverallResult hashtable
						// boolean isAlreadyInThere = addResult2HashTable(
						// myHashtable, userId,
						// productt.getBrandID(),
						// MYSQLCONFIG.isFPGrowthType);
						//
						// if (false == isAlreadyInThere) {
						// count++;
						// }
						// }
					}
				}

			}
		} catch (Exception e) {
			// do nothing
		}

		return resultUsersTable;
	}

	/**
	 * This method get the Frequency Patterns.
	 * 
	 * @param configuration
	 * @param frequentPatternsFileName
	 * @param transactionCount
	 *            ,overall users's number who buying products.
	 * @param frequency
	 * @param minSupport
	 * @param minConfidence
	 * @return parameters Hashtable<Integer, LinkedList<Integer>>
	 */
	public static Hashtable<Integer, LinkedList<Integer>> fPGrowthFilterReadFrequentPatterns(
			Configuration configuration, String frequentPatternsFileName,
			int transactionCount, Map<Integer, Long> frequency,
			double minSupport, double minConfidence) throws Exception {
		FileSystem fs = FileSystem.get(configuration);

		Reader frequentPatternsReader = new SequenceFile.Reader(fs, new Path(
				frequentPatternsFileName), configuration);
		Text key = new Text();
		TopKStringPatterns value = new TopKStringPatterns();
		Hashtable<Integer, LinkedList<Integer>> myreadFrequentPatternsTable = new Hashtable<Integer, LinkedList<Integer>>();
		while (frequentPatternsReader.next(key, value)) {
			long firstFrequencyItem = -1;
			String firstItemId = null;
			List<Pair<List<String>, Long>> patterns = value.getPatterns();
			int i = 0;
			for (Pair<List<String>, Long> pair : patterns) {

				List<String> itemList = pair.getFirst();
				Long occurrence = pair.getSecond();
				if (i == 0) {
					firstFrequencyItem = occurrence;
					firstItemId = itemList.get(0);
				} else {
					double support = (double) occurrence / transactionCount;
					double confidence = (double) occurrence
							/ firstFrequencyItem;
					if (support > minSupport && confidence > minConfidence) {

						for (String itemId : itemList) {
							if (!itemId.equals(firstItemId)) {

								appendResult2HashTable(
										myreadFrequentPatternsTable,
										Integer.parseInt(firstItemId),
										Integer.parseInt(itemId));
							}
						}

					}
				}
				i++;
			}
		}
		frequentPatternsReader.close();
		return myreadFrequentPatternsTable;
	}

	/**
	 * This method get the Frequency
	 * items.like{[brand_idA,3],[brand_idB,8].....}
	 * 
	 * @param configuration
	 * @param frequecnyFileName
	 *            ,fList.seq file name
	 * @return parameters Map<Integer, Long>, structure
	 *         like{[brand_idA,3],[brand_idB,8].....}
	 */
	public static Map<Integer, Long> fPGrowthFilterReadFrequency(
			Configuration configuration, String frequecnyFileName)
			throws Exception {
		FileSystem fs = FileSystem.get(configuration);
		Reader frequencyReader = new SequenceFile.Reader(fs, new Path(
				frequecnyFileName), configuration);
		Map<Integer, Long> frequency = new HashMap<Integer, Long>();
		Text key = new Text();
		LongWritable value = new LongWritable();
		while (frequencyReader.next(key, value)) {
			frequency.put(Integer.parseInt(key.toString()), value.get());
		}
		return frequency;
	}

	/**
	 * This method get data indexd by userid and save it to output.dat file
	 * 
	 * @param null
	 * @return parameters null
	 */
	public static int fPGrowthPrepareSeqFile(
			LinkedList<Object> IndexdByUseridItems) throws Exception {

		// LinkedList<Object> IndexdByUseridItems = getDataIndexByUsers(dayTime,
		// userActionType);
		int fPGrowthDataCount = 0;
		Hashtable<Integer, LinkedList<Integer>> myTable = new Hashtable<Integer, LinkedList<Integer>>();
		for (Object IndexdByUseridItem : IndexdByUseridItems) {
			UserWithItemsData IndexdByUseridItemt = (UserWithItemsData) IndexdByUseridItem;
			LinkedList<UserWithItemsData.Product> products = IndexdByUseridItemt
					.getProducts();
			for (Object product : products) {
				UserWithItemsData.Product productt = (UserWithItemsData.Product) product;
				appendResult2HashTable(myTable,
						IndexdByUseridItemt.getUserID(), productt.getBrandID());
			}
		}

		// int transactionCount = 0;
		boolean alreadyExists = new File("output.dat").exists();
		if (alreadyExists) {
			Process p = Runtime.getRuntime().exec(
					new String[] { "rm", "output.dat" });
			p.waitFor();

		}
		FileWriter datWriter = new FileWriter("output.dat");
		boolean isFirstElement = true;
		Enumeration<Integer> key = myTable.keys();
		while (key.hasMoreElements()) {
			int UserId = key.nextElement();
			LinkedList<Integer> items = myTable.get(UserId);
			isFirstElement = true;
			for (Integer item : items) {
				if (isFirstElement) {
					isFirstElement = false;
				} else {
					datWriter.append(",");
				}
				datWriter.append(item + "");
			}
			datWriter.append("\n");
			fPGrowthDataCount++;
		}

		datWriter.close();
		System.out.println("Wrote " + fPGrowthDataCount + " transactions.");

		// turn output.dat
		hadoopFrequentpatternsCommand();
		return fPGrowthDataCount;
	}

	public static int executeCommand(String[] command, boolean isDebugModel) {

		StringBuffer output = new StringBuffer();

		Process p;
		int val = 0;
		try {

			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			val = p.exitValue();
			if (isDebugModel) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(p.getInputStream()));

				String line = "";
				while ((line = reader.readLine()) != null) {
					output.append(line + "\n");
				}

				System.out.println(output);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return val;
	}

	public static void hadoopFrequentpatternsCommand() {

		LinkedList<String[]> commands = new LinkedList<String[]>();

		// if jps not run, hadoop namenode -format

		boolean frequentpatternsExists = new File("frequentpatterns.seq")
				.exists();
		if (frequentpatternsExists) {
			commands.add(new String[] { "rm", "frequentpatterns.seq" });

		}
		boolean fListExists = new File("fList.seq").exists();
		if (fListExists) {
			commands.add(new String[] { "rm", "fList.seq" });

		}

		commands.add(new String[] { "jps" });
		commands.add(new String[] { "/usr/local/hadoop/bin/hadoop", "fs",
				"-put", "output.dat", "output.dat" });
		commands.add(new String[] { "/usr/local/mahout/bin/mahout", "fpg",
				"-i", "output.dat", "-o", "patterns", "-k", "10", "-method",
				"mapreduce", "-s", "2" });
		commands.add(new String[] { "/usr/local/hadoop/bin/hadoop", "fs",
				"-getmerge", "patterns/frequentpatterns",
				"frequentpatterns.seq" });
		commands.add(new String[] { "/usr/local/hadoop/bin/hadoop", "fs",
				"-get", "patterns/fList", "fList.seq" });
		commands.add(new String[] { "/usr/local/hadoop/bin/hadoop", "fs",
				"-rm", "output.dat" });

		for (String[] command : commands) {
			int val = executeCommand(command, true);
			if (val != 0) {
				System.out.println(command[0] + command[command.length - 1]
						+ " failed!\n");
				executeCommand(new String[] { "/usr/local/hadoop/bin/hadoop",
						"fs", "-rm", "output.dat" }, true);
				val = executeCommand(command, true);

				if (val != 0) {
					break;
				}
			}
		}

	}

	/**
	 * This method get predict items from MYSQLCONFIG.DateThreshold to dayTime
	 * 
	 * @param dayTime
	 *            ,predict after this daytime.
	 * @param userActionType
	 *            ,predict to specific user action,like
	 *            onlyclick,onlypurchase,allaction.
	 * @return Hashtable<Integer, LinkedList<Integer>>, structure
	 *         like<userid1,(brandid1
	 *         ,brandid2...);userid2,(brandid1,brandid2...)...>
	 */
	public static Hashtable<Integer, LinkedList<Integer>> getPredictItemUsersTable(
			Hashtable<Integer, Object> items) throws SQLException {
		Hashtable<Integer, LinkedList<Integer>> resultUserItems = new Hashtable<Integer, LinkedList<Integer>>();

		Enumeration<Integer> indexEmu = items.keys();
		while (indexEmu.hasMoreElements()) {
			int index = indexEmu.nextElement();
			Object item = items.get(index);
			ItemWithUsersData itemt = (ItemWithUsersData) item;
			LinkedList<Integer> userIds = new LinkedList<Integer>();
			LinkedList<ItemWithUsersData.User> users = itemt.getUsers();
			for (ItemWithUsersData.User user : users) {
				boolean isContain = userIds.contains(user.getUserID());
				if (false == isContain) {
					userIds.add(user.getUserID());
				}
			}
			resultUserItems.put(itemt.getProductID(), userIds);

		}

		return resultUserItems;

	}

	/*
	 * 大赛给出的182,880条交易数据中， 总的点击行为次数为：174,539,占百分比为0.954390857；
	 * 总的购买行为次数为：6,984,占百分比为0.038188976；总的收藏行为次数为：1,204,占百分比为0.006583552；
	 * 总的购物车行为车次数为：153，占百分比为0.000836614;
	 */
	public static int getItemWeight(int iType) {

		int userActive = 0;
		switch (iType) {
		case 0: {// 点击
			userActive = 1;
		}
			break;

		case 1: {// 购买，转化率0.038188976
			userActive = 26;
		}
			break;

		case 2: {// 收藏，转化率0.006583552
			userActive = 10;
		}
			break;

		case 3: {// 购物车可与购买归为一类
			userActive = 26;
		}
			break;
		}

		return userActive;
	}

	/*
	 * 大赛给出的182,880条交易数据中， 总的点击行为次数为：174,539,占百分比为0.954390857；
	 * 总的购买行为次数为：6,984,占百分比为0.038188976；总的收藏行为次数为：1,204,占百分比为0.006583552；
	 * 总的购物车行为车次数为：153，占百分比为0.000836614;
	 */
	public static int getItemTimeWeight(int iType, java.util.Date Daytime) {
		String myDaytime = MYSQLCONFIG.DateBegin;
		int dayDiff = 0;
		try {
			dayDiff = getDaytimeduration(Daytime,
					MYSQLCONFIG.dateFormat.parse(myDaytime));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int timeLogarithm = (int) (Math.log(dayDiff) / Math.log(2));

		int userActive = 0;
		switch (iType) {
		case 0: {// 点击
			userActive = 1;
		}
			break;

		case 1: {// 购买，转化率0.038188976
			userActive = 10;
		}
			break;

		case 2: {// 收藏，转化率0.006583552
			userActive = 2;
		}
			break;

		case 3: {// 购物车可与购买归为一类
			userActive = 10;
		}
			break;
		}

		return userActive * (timeLogarithm + 1);
	}

	public static void writeResultToTXT(
			Hashtable<Integer, LinkedList<Integer>> myResult,
			String filePathName) throws Exception {

		boolean alreadyExists = new File(filePathName).exists();
		if (alreadyExists) {
			Process p = Runtime.getRuntime().exec(
					new String[] { "rm", filePathName });
			p.waitFor();

		}

		BufferedWriter out = new BufferedWriter(new FileWriter(filePathName));
		Enumeration<Integer> key = myResult.keys();
		int userId = 0;
		while (key.hasMoreElements()) {
			userId = key.nextElement();
			LinkedList<Integer> items = myResult.get(userId);
			out.write(userId + " \t ");
			boolean isFirstItem = true;
			for (Integer item : items) {
				if (true == isFirstItem) {
					out.write(item + "");
					isFirstItem = false;
				} else {
					out.write(" , " + item);
				}
			}
			out.write(" " + "\n");
		}
		out.close();
	}

	// write the tmp list to file
	private static void write2File(List<Object> items) {
		// write2File(items, 1);
		try {
			File file = new File(ETLCONFIG.TMPPATH
					+ items.get(0).getClass().getName());
			FileWriter writer = new FileWriter(file);
			for (Object object : items) {
				writer.write(object.toString() + "\n");
			}
			writer.close();
		} catch (Exception e) {
			// ignore the exception
		}
	}

	// write to file with and without append model
	private static void write2File(List<Object> items, String fileName) {
		File file = new File(ETLCONFIG.TMPPATH + fileName);

		try {
			FileWriter writer = new FileWriter(file);
			for (Object object : items) {
				writer.write(object.toString() + "\n");
			}
			writer.close();
		} catch (Exception e) {
			// ignore the exception
		}
	}

	// write to excelFile
	public static void writeStatistics2ExcelFile(
			LinkedList<StatisticsResultData> datas) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Sample sheet");

		int rownum = 0;
		Row row = null;
		Cell cell = null;

		row = sheet.createRow(rownum++);
		cell = row.createCell(0);
		cell.setCellValue("precision");
		cell = row.createCell(1);
		cell.setCellValue("recall");
		cell = row.createCell(2);
		cell.setCellValue("f1score");
		for (StatisticsResultData data : datas) {
			int cellnum = 0;
			row = sheet.createRow(rownum++);
			cell = row.createCell(cellnum++);
			cell.setCellValue(data.getPrecision());
			cell = row.createCell(cellnum++);
			cell.setCellValue(data.getRecall());
			cell = row.createCell(cellnum++);
			cell.setCellValue(data.getF1Score());
		}

		try {
			FileOutputStream out = new FileOutputStream(new File(
					ETLCONFIG.TMPPATH + datas.get(0).getClass().getName()
							+ ".xls"));
			workbook.write(out);
			out.close();
			System.out.println("Excel written successfully..");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// in order to add double quote:"2013-01-03"->""2013-01-03""
	private static String addDoubleQuote(String str) {

		StringBuilder ConnSQLStrBld = new StringBuilder();
		ConnSQLStrBld.append(str);
		ConnSQLStrBld.insert(ConnSQLStrBld.length(), '"');
		ConnSQLStrBld.insert(0, '"');
		return ConnSQLStrBld.toString();
	}

	public static int getDaytimeduration(java.util.Date d1, java.util.Date d2)
			throws Exception {
		// Date d1 = MYSQLCONFIG.dateFormat.parse(datTime1);
		// Date d2 = MYSQLCONFIG.dateFormat.parse(datTime2);
		long diff = Math.abs(d1.getTime() - d2.getTime());
		long days = diff / (1000 * 60 * 60 * 24);
		return (int) days;
	}

	/**
	 * This method get useridA next month buying products number
	 * 
	 * @param datTime
	 *            ，between datTime and MYSQLCONFIG.DateThreshold,we can
	 *            calculate the number of products that specific userid buying.
	 */

	/**
	 * This method get the date LinkedList<Object> structure indexed by userid.
	 * 
	 * @param dayTime1
	 *            ,predict after this daytime.
	 * @param userActionType
	 *            ,predict to specific user action,like
	 *            onlyclick,onlypurchase,allaction.
	 * @return parameters LinkedList<Object>, structure like<userid1,(brandid1
	 *         ,brandid2...);userid2,(brandid1,brandid2...)...>
	 */
	public static Hashtable<Integer, Object> getIndexByUsersData(
			String dayTime1, String dayTime2, String userActionType)
			throws SQLException {
		// long begintime = System.currentTimeMillis();
		String sqlStat = null;

		try {
			Date date1 = MYSQLCONFIG.dateFormat.parse(dayTime1);
			Date date2 = MYSQLCONFIG.dateFormat.parse(dayTime2);
			int com = date2.compareTo(date1);
			if (com > 0) {

				sqlStat = "select * from tmail_firstseason where visit_datetime <= "
						+ addDoubleQuote(dayTime2)
						+ " and visit_datetime > "
						+ addDoubleQuote(dayTime1);
			} else {

				sqlStat = "select * from tmail_firstseason where visit_datetime <= "
						+ addDoubleQuote(dayTime1)
						+ " and visit_datetime > "
						+ addDoubleQuote(dayTime2);
			}

		} catch (ParseException e) {
			// do nothing
		}

		// LinkedList<Object> users = new LinkedList<Object>();
		Hashtable<Integer, Object> userItemsTable = new Hashtable<Integer, Object>();

		java.sql.Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DriverManager.getConnection(MYSQLCONFIG.DBURL,
				MYSQLCONFIG.USRNAME, MYSQLCONFIG.PASSWORD);

		sqlStat = sqlStat + userActionType;
		// preparedStatement = connection.prepareStatement(sqlStat);
		statement = connection.createStatement();
		statement.executeQuery(sqlStat);
		resultSet = statement.getResultSet();

		while (resultSet.next()) {
			int user_id = resultSet.getInt(2);
			boolean isContainThisUserId = userItemsTable.containsKey(user_id);
			if (isContainThisUserId) {
				Object tmp = userItemsTable.get(user_id);
				UserWithItemsData exsitUser = (UserWithItemsData) tmp;

				UserWithItemsData.Product product = exsitUser.new Product();
				product.setBrandID(resultSet.getInt(3));
				int type = resultSet.getInt(4);
				product.setType(type);
				product.setVisitDaytime(resultSet.getDate(5));
				exsitUser.addProductToList(product);

				int userActionCount[] = exsitUser.getUserActionCount();
				userActionCount[type]++;
				// userWithItems.setUserActionCount(userActionCount);

				int userActive = exsitUser.getWeight();
				userActive += getItemWeight(type);
				exsitUser.setWeight(userActive);

				double click2PurchaseRate = (double) (userActionCount[1] + userActionCount[3])
						/ (double) (userActionCount[0] + userActionCount[1]
								+ userActionCount[2] + userActionCount[3]);
				exsitUser.setClick2purchase(click2PurchaseRate);
				// userItemsTable.put(userWithItems.getUserID(), userWithItems);

			} else {

				int userActionCount[] = { 0, 0, 0, 0 };
				UserWithItemsData newUser = new UserWithItemsData();
				newUser.setUserID(user_id);
				UserWithItemsData.Product product = newUser.new Product();
				product.setBrandID(resultSet.getInt(3));
				int type = resultSet.getInt(4);
				int userActive = getItemWeight(type);
				product.setType(type);
				product.setVisitDaytime(resultSet.getDate(5));
				userActionCount[type]++;
				newUser.addProductToList(product);
				newUser.setUserActionCount(userActionCount);
				newUser.setWeight(userActive);
				double click2PurchaseRate = (double) (userActionCount[1] + userActionCount[3])
						/ (double) (userActionCount[0] + userActionCount[1]
								+ userActionCount[2] + userActionCount[3]);
				newUser.setClick2purchase(click2PurchaseRate);

				userItemsTable.put(user_id, newUser);
			}

		}
		connection.close();

		return userItemsTable;

	}

	public static Hashtable<Integer, Object> getIndexByUsersDataETL(
			Hashtable<Integer, Object> myItemsIndexByUsers) {

		// set mulpti actiontype on same item in one day as one actiontype.
		Enumeration<Integer> indexEmu = myItemsIndexByUsers.keys();
		Hashtable<Integer, Object> myItemsIndexByUsersNew = new Hashtable<Integer, Object>();
		while (indexEmu.hasMoreElements()) {
			int index = indexEmu.nextElement();
			Object userWithItem = myItemsIndexByUsers.get(index);
			UserWithItemsData userWithItemt = (UserWithItemsData) userWithItem;
			UserWithItemsData userWithItemtNew = new UserWithItemsData();
			// Hashtable<Integer, Float> itemsWithWeightTable = new
			// Hashtable<Integer, Float>();
			LinkedList<UserWithItemsData.Product> myProducts = userWithItemt
					.getProducts();
			LinkedList<UserWithItemsData.Product> myProductsNew = new LinkedList<UserWithItemsData.Product>();
			int[] userActon = { 0, 0, 0, 0 };
			for (UserWithItemsData.Product myProduct : myProducts) {
				boolean isExsitSameItem = isExsitSameActiontypeBrandidDaytime(
						myProduct, myProductsNew);
				if (false == isExsitSameItem) {
					myProductsNew.add(myProduct);
					userActon[myProduct.getType()]++;

					// add item to userWithItemtNew.
					userWithItemtNew.addProductToList(myProduct);
				}
			}

			// set useid to userWithItemtNew.
			userWithItemtNew.setUserID(userWithItemt.getUserID());

			// set userActon to userWithItemtNew.
			userWithItemtNew.setUserActionCount(userActon);

			// set click2purchase to userWithItemtNew.
			double click2purchase = (userActon[1] + userActon[3])
					/ (userActon[0] + userActon[2] + userActon[1] + userActon[3]);
			userWithItemtNew.setClick2purchase(click2purchase);

			// put userWithItemtNew to hashtable.
			myItemsIndexByUsersNew.put(userWithItemt.getUserID(),
					userWithItemtNew);
		}

		return myItemsIndexByUsersNew;
	}

	public static boolean isExsitSameActiontypeBrandidDaytime(
			UserWithItemsData.Product myProduct,
			LinkedList<UserWithItemsData.Product> myNewProducts) {

		int myProductId = myProduct.getBrandID();
		int myProductType = myProduct.getType();
		Calendar myProductcalender = Calendar.getInstance();
		myProductcalender.setTime(myProduct.getVisitDaytime());
		int myProductMonth = myProductcalender.get(Calendar.MONTH);
		int myProductDay = myProductcalender.get(Calendar.DAY_OF_MONTH);

		for (UserWithItemsData.Product myCurProduct : myNewProducts) {
			int myCurProductId = myCurProduct.getBrandID();
			int myCurProductType = myCurProduct.getType();
			Calendar myCurProductcalender = Calendar.getInstance();
			myCurProductcalender.setTime(myCurProduct.getVisitDaytime());
			int myCurProductMonth = myCurProductcalender.get(Calendar.MONTH);
			int myCurProductDay = myCurProductcalender
					.get(Calendar.DAY_OF_MONTH);

			if ((myCurProductId == myProductId)
					&& (myCurProductType == myProductType)
					&& (myCurProductMonth == myProductMonth)
					&& (myCurProductDay == myProductDay)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * This method get the date LinkedList<Object> structure indexed by brandid.
	 * 
	 * @param dayTime2
	 *            ,predict after this daytime.
	 * @param userActionType
	 *            ,predict to specific user action,like
	 *            onlyclick,onlypurchase,allaction.
	 * @return parameters LinkedList<Object>, structure like<brandid1,(userid1
	 *         ,userid1...);brandid2,(userid1,userid1...)...>
	 */
	public static Hashtable<Integer, Object> getIndexByItemsData(
			String dayTime1, String dayTime2, String userActionType)
			throws SQLException {
		long begintime = System.currentTimeMillis();
		String sqlStat = null;
		try {
			Date date1 = MYSQLCONFIG.dateFormat.parse(dayTime1);
			Date date2 = MYSQLCONFIG.dateFormat.parse(dayTime2);
			int com = date1.compareTo(date2);
			if (com > 0) {

				sqlStat = "select * from tmail_firstseason where visit_datetime <= "
						+ addDoubleQuote(dayTime1)
						+ " and visit_datetime > "
						+ addDoubleQuote(dayTime2);
			} else {

				sqlStat = "select * from tmail_firstseason where visit_datetime <= "
						+ addDoubleQuote(dayTime2)
						+ " and visit_datetime > "
						+ addDoubleQuote(dayTime1);
			}

		} catch (ParseException e) {
			// do nothing
		}

		LinkedList<Object> items = new LinkedList<Object>();
		Hashtable<Integer, Object> ItemsTable = new Hashtable<Integer, Object>();

		java.sql.Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DriverManager.getConnection(MYSQLCONFIG.DBURL,
				MYSQLCONFIG.USRNAME, MYSQLCONFIG.PASSWORD);

		sqlStat = sqlStat + userActionType;
		// preparedStatement = connection.prepareStatement(sqlStat);
		statement = connection.createStatement();
		statement.executeQuery(sqlStat);
		resultSet = statement.getResultSet();

		while (resultSet.next()) {
			int brand_id = resultSet.getInt(3);
			boolean isContainsKey = ItemsTable.containsKey(brand_id);
			if (isContainsKey) {
				Object tmp = ItemsTable.get(brand_id);
				ItemWithUsersData exsitItem = (ItemWithUsersData) tmp;

				ItemWithUsersData.User users = exsitItem.new User();
				users.setUserID(resultSet.getInt(2));
				int type = resultSet.getInt(4);
				users.setType(type);
				users.setVisitDaytime(resultSet.getDate(5));
				exsitItem.addUserToList(users);

				int itemActionCount[] = exsitItem.getItemActionCount();
				itemActionCount[type]++;
				exsitItem.setItemActionCount(itemActionCount);

				int itemPopular = exsitItem.getWeight();
				itemPopular += getItemWeight(type);
				exsitItem.setWeight(itemPopular);

				double click2PurchaseRate = (double) (itemActionCount[1] + itemActionCount[3])
						/ (double) (itemActionCount[0] + itemActionCount[1]
								+ itemActionCount[2] + itemActionCount[3]);
				exsitItem.setClick2purchase(click2PurchaseRate);

				ItemsTable.put(exsitItem.getProductID(), exsitItem);

			} else {

				int itemActionCount[] = { 0, 0, 0, 0 };
				ItemWithUsersData newItem = new ItemWithUsersData();
				ItemWithUsersData.User users = newItem.new User();
				int type = resultSet.getInt(4);

				int userActive = getItemWeight(type);
				itemActionCount[type]++;
				users.setUserID(resultSet.getInt(2));
				users.setType(type);
				users.setVisitDaytime(resultSet.getDate(5));

				newItem.setWeight(userActive);
				newItem.setItemActionCount(itemActionCount);
				newItem.setProductID(brand_id);
				newItem.addUserToList(users);

				double click2PurchaseRate = (double) (itemActionCount[1] + itemActionCount[3])
						/ (double) (itemActionCount[0] + itemActionCount[1]
								+ itemActionCount[2] + itemActionCount[3]);
				newItem.setClick2purchase(click2PurchaseRate);

				ItemsTable.put(newItem.getProductID(), newItem);
			}

		}
		connection.close();
		return ItemsTable;

	}

	public static Hashtable<Integer, Object> getIndexByItemsDataETL(
			Hashtable<Integer, Object> myItemsTable) {

		// set mulpti actiontype on same item in one day as one actiontype.
		Enumeration<Integer> indexEmu = myItemsTable.keys();
		Hashtable<Integer, Object> myItemsIndexByUsersNew = new Hashtable<Integer, Object>();
		while (indexEmu.hasMoreElements()) {
			int index = indexEmu.nextElement();
			Object itemtWithusers = myItemsTable.get(index);
			ItemWithUsersData itemtWithusert = (ItemWithUsersData) itemtWithusers;
			ItemWithUsersData itemtWithusersNew = new ItemWithUsersData();
			// Hashtable<Integer, Float> itemsWithWeightTable = new
			// Hashtable<Integer, Float>();
			LinkedList<ItemWithUsersData.User> myUsers = itemtWithusert
					.getUsers();
			LinkedList<ItemWithUsersData.User> myUsersNew = new LinkedList<ItemWithUsersData.User>();
			int[] itemActon = { 0, 0, 0, 0 };
			for (ItemWithUsersData.User myUser : myUsers) {
				boolean isExsitSameItem = isExsitSameActiontypeUserIdDaytime(
						myUser, myUsersNew);
				if (false == isExsitSameItem) {
					myUsersNew.add(myUser);
					itemActon[myUser.getType()]++;

					// add item to userWithItemtNew.
					itemtWithusersNew.addUserToList(myUser);
				}
			}

			// set useid to userWithItemtNew.
			itemtWithusersNew.setProductID(itemtWithusert.getProductID());

			// set userActon to userWithItemtNew.
			itemtWithusersNew.setItemActionCount(itemActon);

			// set click2purchase to userWithItemtNew.
			// double click2purchase = (itemActon[1] + itemActon[3])
			// / (itemActon[0] + itemActon[2] + itemActon[1] + itemActon[3]);
			// itemtWithusersNew.setClick2purchase(click2purchase);

			// put userWithItemtNew to hashtable.
			myItemsIndexByUsersNew.put(itemtWithusert.getProductID(),
					itemtWithusersNew);
		}

		return myItemsIndexByUsersNew;
	}

	public static boolean isExsitSameActiontypeUserIdDaytime(
			ItemWithUsersData.User myUser,
			LinkedList<ItemWithUsersData.User> myNewUsers) {

		int myUserId = myUser.getUserID();
		int myUserType = myUser.getType();
		Calendar myProductcalender = Calendar.getInstance();
		myProductcalender.setTime(myUser.getVisitDaytime());
		int myUserMonth = myProductcalender.get(Calendar.MONTH);
		int myUserDay = myProductcalender.get(Calendar.DAY_OF_MONTH);

		for (ItemWithUsersData.User myCurUser : myNewUsers) {
			int myCurUserId = myCurUser.getUserID();
			int myCurUserType = myCurUser.getType();
			Calendar myCurUsercalender = Calendar.getInstance();
			myCurUsercalender.setTime(myCurUser.getVisitDaytime());
			int myCurUserMonth = myCurUsercalender.get(Calendar.MONTH);
			int myCurUserDay = myCurUsercalender.get(Calendar.DAY_OF_MONTH);

			if ((myCurUserId == myUserId) && (myCurUserType == myUserType)
					&& (myCurUserMonth == myUserMonth)
					&& (myCurUserDay == myUserDay)) {
				return true;
			}
		}

		return false;
	}

	// public static Hashtable<Integer, UserWithPredictCountData>
	// getUserbuyingItemsNPerMonth(
	// String datTime) throws Exception {
	// if (null != myUserBuyingItemsNPerMonth) {
	// return myUserBuyingItemsNPerMonth;
	// }
	//
	// myUserBuyingItemsNPerMonth = new Hashtable<Integer,
	// UserWithPredictCountData>();
	// int myMonthduration = getMonthduration(datTime,
	// MYSQLCONFIG.DateThreshold);
	// LinkedList<Object> IndexdByUseridItems = getUsersSimpl(datTime,
	// MYSQLCONFIG.isOnlypurchaseAction);
	//
	// for (Object IndexdByUseridItem : IndexdByUseridItems) {
	// UserWithPredictCountData myUserWithCount = new
	// UserWithPredictCountData();
	// int[] userbuyingItemsN = { 0, 0, 0 };
	// UserWithItemsData IndexdByUseridItemt = (UserWithItemsData)
	// IndexdByUseridItem;
	// myUserWithCount.setUserID(IndexdByUseridItemt.getUserID());
	// int[] UserActionCount = IndexdByUseridItemt.getUserActionCount();
	// userbuyingItemsN[2] = (UserActionCount[1] + UserActionCount[3])
	// / myMonthduration + myMonthduration;
	// myUserWithCount.setUserPredictCount(userbuyingItemsN);
	//
	// myUserBuyingItemsNPerMonth.put(IndexdByUseridItemt.getUserID(),
	// myUserWithCount);
	// }
	//
	// return myUserBuyingItemsNPerMonth;
	// }

	// // get top100 items
	// public static HashMap<Integer, Integer> getHotItems() throws SQLException
	// {
	// java.sql.Statement statement = null;
	// ResultSet resultSet = null;
	// HashMap<Integer, Integer> itemsMap = new HashMap<Integer, Integer>();
	//
	// // TODO
	// String sqlStat =
	// "select * from item_sort where weight >=100 group by brand_id";
	// Connection connection = DriverManager.getConnection(MYSQLCONFIG.DBURL,
	// MYSQLCONFIG.USRNAME, MYSQLCONFIG.PASSWORD);
	//
	// statement = connection.createStatement();
	// statement.executeQuery(sqlStat);
	// resultSet = statement.getResultSet();
	// while (resultSet.next()) {
	// itemsMap.put(resultSet.getInt(2), resultSet.getInt(3));
	// }
	//
	// return itemsMap;
	// }

	// /**
	// * This method get the brand name which mapping to brand_id
	// *
	// * @param mappingFileName
	// * ,mapping File in which there are structures like {[brand_idA,brand
	// nameA],[brand_idB,brand nameB].....}
	// * @return parameters Map<Integer, String>
	// */
	// public static Map<Integer, String> fPGrowthReadMapping(String
	// mappingFileName)
	// throws Exception {
	// Map<Integer, String> itemById = new HashMap<Integer, String>();
	// BufferedReader csvReader = new BufferedReader(new
	// FileReader(mappingFileName));
	// while (true) {
	// String line = csvReader.readLine();
	// if (line == null) {
	// break;
	// }
	//
	// String[] tokens = line.split(",", 2);
	// itemById.put(Integer.parseInt(tokens[1]), tokens[0]);
	// }
	// return itemById;
	// }
	// //
	// public static LinkedList<Object> getItems() throws SQLException {
	// if (items != null) {
	// return items;
	// }
	// // if the companies is null then make it happen
	// items = new LinkedList<Object>();
	// LinkedList<Integer> itemIds = new LinkedList<Integer>();
	// // PreparedStatement preparedStatement;
	// java.sql.Statement statement = null;
	// ResultSet resultSet = null;
	// String sqlStat =
	// "select brand_id from tmail_firstseason group by brand_id;";
	//
	// Connection connection = DriverManager.getConnection(MYSQLCONFIG.DBURL,
	// MYSQLCONFIG.USRNAME, MYSQLCONFIG.PASSWORD);
	//
	// statement = connection.createStatement();
	// statement.executeQuery(sqlStat);
	// resultSet = statement.getResultSet();
	// while (resultSet.next()) {
	// itemIds.add(resultSet.getInt(1));
	// }
	//
	// for (int itemId : itemIds) {
	// sqlStat = "select * from tmail_firstseason where brand_id="
	// + itemId + ";";
	// // preparedStatement = connection.prepareStatement(sqlStat);
	// statement = connection.createStatement();
	// statement.executeQuery(sqlStat);
	// resultSet = statement.getResultSet();
	// int clickCount = 0;
	// int purchaseCount = 0;
	// int FavoriteCount = 0;
	// int ShopcartCount = 0;
	// int itemPopular = 0;
	// Item item = new Item();
	//
	// while (resultSet.next()) {
	//
	// Item.User user = item.new User();
	// user.setBrandID(resultSet.getInt(3));
	// int type = resultSet.getInt(4);
	// user.setType(resultSet.getInt(4));
	// user.setVisitDaytime(resultSet.getDate(5));
	//
	// itemPopular = getItemWeight(itemPopular, type);
	//
	// switch (type) {
	// case 0: {
	// clickCount++;
	// }
	// break;
	//
	// case 1: {
	// purchaseCount++;
	// }
	// break;
	//
	// case 2: {
	// FavoriteCount++;
	// }
	// break;
	//
	// case 3: {
	// purchaseCount++;
	// // ShopcartCount++;
	// }
	// break;
	// }
	// item.addUsers(user);
	//
	// }
	//
	// item.setProductID(itemId);
	// item.setClickCount(clickCount);
	// item.setPurchaseCount(purchaseCount);
	// item.setFavoriteCount(FavoriteCount);
	// item.setShopcartCount(ShopcartCount);
	//
	// if (clickCount != 0) {
	// double temp = (double) (purchaseCount + ShopcartCount)
	// / clickCount;
	// BigDecimal b = new BigDecimal(temp);
	// // 小数取四位
	// temp = b.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
	// item.setClick2purchase(temp);
	// } else {
	// // 点击转化率设为平均值
	// item.setClick2purchase(0.0390);
	// }
	//
	// item.setWeight(itemPopular);
	// items.add(item);
	// }
	//
	// logger.debug("load users compeletely");
	//
	// if (ETLCONFIG.ISDEBUGMODEL) {
	// write2File(items);
	// }
	//
	// connection.close();
	//
	// return items;
	// }

	// public static LinkedList<Object> getUsers(int month, String
	// userActionType)
	// throws SQLException {
	// long begintime = System.currentTimeMillis();
	// // if (users != null) {
	// // return users;
	// // }
	// // if the companies is null then make it happen
	// LinkedList<Object> users = new LinkedList<Object>();
	// LinkedList<Integer> userIds = new LinkedList<Integer>();
	// // PreparedStatement preparedStatement;
	//
	// // String sqlStat =
	// // "select user_id from tmail_firstseason group by user_id;";
	// String sqlStat =
	// "select * from tmail_firstseason where MONTH(visit_datetime) ="
	// + month + userActionType + "group by user_id;";
	//
	// java.sql.Statement statement = null;
	// ResultSet resultSet = null;
	// Connection connection = DriverManager.getConnection(MYSQLCONFIG.DBURL,
	// MYSQLCONFIG.USRNAME, MYSQLCONFIG.PASSWORD);
	//
	// statement = connection.createStatement();
	// statement.executeQuery(sqlStat);
	// resultSet = statement.getResultSet();
	// while (resultSet.next()) {
	// userIds.add(resultSet.getInt(2));
	// }
	//
	// for (int userId : userIds) {
	// sqlStat = "select * from tmail_firstseason where user_id=" + userId
	// + " and MONTH(visit_datetime) = " + month + userActionType;
	//
	// // preparedStatement = connection.prepareStatement(sqlStat);
	// statement = connection.createStatement();
	// statement.executeQuery(sqlStat);
	// resultSet = statement.getResultSet();
	// int userActionCount[] = { 0, 0, 0, 0 };
	// int userActive = 0;
	//
	// DateUser user = new DateUser();
	//
	// while (resultSet.next()) {
	//
	// DateUser.Product product = user.new Product();
	// product.setBrandID(resultSet.getInt(3));
	// int type = resultSet.getInt(4);
	// userActive = getItemWeight(userActive, type);
	// product.setType(resultSet.getInt(4));
	// product.setVisitDaytime(resultSet.getDate(5));
	// userActionCount[type]++;
	// user.setProducts(product);
	//
	// }
	//
	// user.setUserID(userId);
	// user.setUserActionCount(userActionCount);
	// user.setWeight(userActive);
	//
	// double temp = (double) (userActionCount[1] + userActionCount[3])
	// / userActionCount[0];
	// BigDecimal b = new BigDecimal(temp);
	// // 小数取四位
	// temp = b.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
	// user.setClick2purchase(temp);
	//
	// users.add(user);
	// }
	//
	// logger.debug("load users compeletely");
	//
	// if (ETLCONFIG.ISDEBUGMODEL) {
	// write2File(users);
	// }
	//
	// connection.close();
	// long endtime = System.currentTimeMillis();
	// long costTime = (endtime - begintime) / 1000;
	// System.out.println("getUsers function use seconds:" + costTime);
	// logger.debug("getUsers function use seconds:", costTime);
	// return users;
	// }

}