package ustc.sse.datamining;

import ustc.sse.datamining.LaneCrossFoldLearner;
import ustc.sse.datamining.MikeAdaptiveLogisticRegression;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import org.apache.mahout.classifier.sgd.L1;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.Vector;

import com.google.common.collect.Lists;

public class LogResTrainData {

	// 当前使用指标0:截距；1-5时间；6之前是否购买过；7用户 Rate
	private static final int FEATURES = 3 + 1;
	private static int itemId = 0;

	private LaneCrossFoldLearner bestLearner;

	private static final Hashtable<Integer, List<Vector>> myPosiUsersTable = new Hashtable<Integer, List<Vector>>();
	private static final Hashtable<Integer, List<Vector>> myNegaUsersTable = new Hashtable<Integer, List<Vector>>();
	private static final Hashtable<Integer, List<Vector>> myPredictUsersTable = new Hashtable<Integer, List<Vector>>();

	RunningVariance myPreSta = new RunningVariance();
	RunningVariance myPosiSta = new RunningVariance();
	RunningVariance myNegaSta = new RunningVariance();

	public boolean appenduiWeightLog(
			Hashtable<Integer, LinkedList<userItemScalar>> myTrainLogTable,
			int userId, int itemId, double uiWeight) {

		boolean isItemLogExsit = myTrainLogTable.containsKey(itemId);
		boolean isUserLogExsit = false;
		// 如果有此条 item 记录，则接下来查找 user-item记录
		if (true == isItemLogExsit) {
			LinkedList<userItemScalar> myLogs = myTrainLogTable.get(itemId);
			for (userItemScalar myLog : myLogs) {
				if (userId == myLog.getUserId()) {
					// 如果有此条 user-item 记录，则 Scalar 加上 uiWeight。
					myLog.setScalar(uiWeight + myLog.getScalar());
					// myLog.setNumDay(numDay);
					isUserLogExsit = true;
					break;
				}
			}

			// 如果没有此条 user-item 记录，则添加并初始化 Scalar 为 uiWeight。
			if (false == isUserLogExsit) {
				userItemScalar myNewLog = new userItemScalar();
				myNewLog.setUserId(userId);
				myNewLog.setItemId(itemId);
				myNewLog.setScalar(uiWeight);
				// myNewLog.setNumDay(numDay);
				myLogs.add(myNewLog);
			}
		} else {
			// 如果没有此条 item 记录，则直接插入。
			LinkedList<userItemScalar> myNewlogs = new LinkedList<userItemScalar>();
			userItemScalar myNewlog = new userItemScalar();
			myNewlog.setUserId(userId);
			myNewlog.setItemId(itemId);
			myNewlog.setScalar(uiWeight);
			// myNewlog.setNumDay(numDay);
			myNewlogs.add(myNewlog);
			myTrainLogTable.put(itemId, myNewlogs);
		}

		return isUserLogExsit;
	}

	public double getUserRate(Hashtable<Integer, Object> myTrainRes, int myId) {
		Object TrainRes = myTrainRes.get(myId);
		double rate = ((UserWithItemsData) TrainRes).getClick2purchase();
		return rate * 10;
	}

	public double getItemRate(Hashtable<Integer, Object> myTrainRes, int myId) {
		Object TrainRes = myTrainRes.get(myId);
		double rate = ((ItemWithUsersData) TrainRes).getClick2purchase();
		return rate * 10;
	}

	public int isUIBuying(
			Hashtable<Integer, LinkedList<Integer>> myTargetItemUsersTable,
			int userId, int itemId) {

		int isUIBuying = 0;
		boolean isItemExist = myTargetItemUsersTable.containsKey(itemId);
		if (true == isItemExist) {
			LinkedList<Integer> myItems = myTargetItemUsersTable.get(itemId);
			boolean isUserExsit = myItems.contains(userId);
			if (true == isUserExsit) {
				isUIBuying = 1;
			}
		}

		return isUIBuying;
	}

	public int getUIClickNum(int userId, int itemId,
			Hashtable<Integer, Object> mySpecialTrainTableETL) {

		int count = 0;
		ItemWithUsersData myItemETL = ((ItemWithUsersData) mySpecialTrainTableETL
				.get(itemId));
		LinkedList<ItemWithUsersData.User> users = myItemETL.getUsers();
		for (ItemWithUsersData.User myUser : users) {
			if (userId == myUser.getUserID()) {
				count++;
			}
		}

		return count;
	}

	public boolean append2Table(
			Hashtable<Integer, LinkedList<java.sql.Date>> myUserWithTime,
			ItemWithUsersData.User myUser) {

		java.sql.Date myVisitDaytime = myUser.getVisitDaytime();
		int userId = myUser.getUserID();
		boolean isAppend = false;

		boolean isContainUser = myUserWithTime.containsKey(userId);
		if (isContainUser == true) {
			LinkedList<java.sql.Date> timeLink = myUserWithTime.get(userId);
			timeLink.add(myVisitDaytime);
			isAppend = true;

		} else {
			LinkedList<java.sql.Date> newtimeLink = new LinkedList<java.sql.Date>();
			newtimeLink.add(myVisitDaytime);
			myUserWithTime.put(userId, newtimeLink);
			isAppend = true;
		}

		return isAppend;
	}

	public static int comDayTime(java.util.Date myEndDay,
			java.util.Date myCurTime) {

		Calendar myEndDayCalender = Calendar.getInstance();
		myEndDayCalender.setTime(myEndDay);

		Calendar myCurCalender = Calendar.getInstance();
		myCurCalender.setTime(myCurTime);

		int numEndDay = myEndDayCalender.get(Calendar.DAY_OF_YEAR);
		int numCurDay = myCurCalender.get(Calendar.DAY_OF_YEAR);

		if (numEndDay < numCurDay) {
			return -1;
		} else if (numEndDay == numCurDay) {
			return 0;
		} else if (numEndDay <= numCurDay + 1) {
			return 1;
		} else if (numEndDay <= numCurDay + 3) {
			return 2;
		} else if (numEndDay <= numCurDay + 7) {
			return 3;
		} else if (numEndDay <= numCurDay + 15) {
			return 4;
		} else if (numEndDay <= numCurDay + 31) {
			return 5;
		} else {
			return 6;
		}
	}

	public int lRForgeDataModel(Hashtable<Integer, Object> myUserTable,
			ItemWithUsersData myItemsTable, String beforeDay, String endDay) {

		itemId = myItemsTable.getProductID();
		java.sql.Date myEndDay = null;
		java.sql.Date myBeforeDay = null;
		try {
			myEndDay = new java.sql.Date(MYSQLCONFIG.dateFormat.parse(endDay)
					.getTime());
			myBeforeDay = new java.sql.Date(MYSQLCONFIG.dateFormat.parse(
					beforeDay).getTime());
		} catch (ParseException e) {
			// do nothing
		}
		// targetVertor = Lists.newArrayList();
		// shuffleVectorOrder = Lists.newArrayList();

		Hashtable<Integer, LinkedList<java.sql.Date>> myUsersWithBuyTimeTable = new Hashtable<Integer, LinkedList<java.sql.Date>>();
		Hashtable<Integer, LinkedList<java.sql.Date>> myUsersWithClickTimeTable = new Hashtable<Integer, LinkedList<java.sql.Date>>();

		for (ItemWithUsersData.User myUser : myItemsTable.getUsers()) {
			int userActionType = myUser.getType();

			if (0 == userActionType || 2 == userActionType) {
				append2Table(myUsersWithClickTimeTable, myUser);
			} else {
				append2Table(myUsersWithBuyTimeTable, myUser);
			}

		}

		// 对购买 table 中的时间做排序
		Enumeration<Integer> iBuy = myUsersWithBuyTimeTable.keys();
		while (iBuy.hasMoreElements()) {
			int userId = iBuy.nextElement();
			LinkedList<java.sql.Date> myTimes = myUsersWithBuyTimeTable
					.get(userId);
			// 添加最前一天
			myTimes.add(myBeforeDay);
			// 添加最后一天
			myTimes.add(myEndDay);

			// 倒序
			Collections.sort(myTimes, new Comparator<java.util.Date>() {
				@Override
				public int compare(java.util.Date o1, java.util.Date o2) {
					return o2.compareTo(o1);
				}
			});

		}

		// 根据规则把點擊行爲分類到正、负、预测数据集中
		Enumeration<Integer> iClick = myUsersWithClickTimeTable.keys();
		while (iClick.hasMoreElements()) {

			List<Vector> myNewPredictList = Lists.newArrayList();
			List<Vector> myNewPosiList = Lists.newArrayList();
			List<Vector> myNewNegaList = Lists.newArrayList();
			int userId = iClick.nextElement();
			LinkedList<java.sql.Date> myClickTimes = myUsersWithClickTimeTable
					.get(userId);

			// 时间倒序
			Collections.sort(myClickTimes, new Comparator<java.util.Date>() {
				@Override
				public int compare(java.util.Date o1, java.util.Date o2) {
					return o2.compareTo(o1);
				}
			});

			double userRate = ((UserWithItemsData) myUserTable.get(userId))
					.getClick2purchase();
			boolean isUserBuyBefore = myUsersWithBuyTimeTable
					.containsKey(userId);

			// 如果用户之前没有购买过商品，则可能是预测数据/负样本
			if (false == isUserBuyBefore) {
				DenseVector myNewV = new DenseVector(FEATURES);
				java.util.Date myCurEndDay = myEndDay;
				for (java.util.Date myClickTime : myClickTimes) {
					int rt = comDayTime(myCurEndDay, myClickTime);
					if ((rt > 0 && rt <= 5) || myClickTime == myEndDay) {
						// 截距
						myNewV.set(0, 1);
						// 之前是否购买过
						myNewV.set(1, 0);
						// 用户转换率
						myNewV.set(2, userRate);
						// 如果在一个月内,即值为：1,2,3,4,5
						myNewV.set(3, myNewV.get(3) + getWeight(rt));
					} else {
						// 时间维度变了，如果用户有当前的时间向量则按照时间保存到对应的链表中。
						if (1 == myNewV.get(0) && myCurEndDay == myEndDay) {
							myNewPredictList.add(myNewV);
							myNewV = new DenseVector(FEATURES);
						} else if (1 == myNewV.get(0)
								&& myCurEndDay != myEndDay) {
							myNewNegaList.add(myNewV);
							myNewV = new DenseVector(FEATURES);
						}

						while (rt == 6) {
							// 往前递归时间，直到找到所属的月
							myCurEndDay = getLeastDayTime(31, myCurEndDay);
							rt = comDayTime(myCurEndDay, myClickTime);
						}

						if (rt > 0) {
							// 截距
							myNewV.set(0, 1);
							// 之前是否购买过
							myNewV.set(1, 0);
							// 用户转换率
							myNewV.set(2, userRate);
							// 如果在一个月内,即值为：1,2,3,4,5
							myNewV.set(3, myNewV.get(3) + getWeight(rt));
						}

					}

				}
				// 时间维度遍历完了，如果用户有当前的时间向量则保存。
				if (1 == myNewV.get(0) && myCurEndDay == myEndDay) {
					myNewPredictList.add(myNewV);
					myPreSta.addSample(myNewV.get(3));
				} else if (1 == myNewV.get(0) && myCurEndDay != myEndDay) {
					myNewNegaList.add(myNewV);
					myNegaSta.addSample(myNewV.get(3));
				}

				// 如果用户之前购买过商品，则可能是预测数据/正样本/负样本
			} else {
				DenseVector myNewV = new DenseVector(FEATURES);
				java.sql.Date myCurEndDay = myEndDay;
				java.sql.Date myMarginEndDay = myEndDay;
				LinkedList<java.sql.Date> myBuyTimes = myUsersWithBuyTimeTable
						.get(userId);
				java.sql.Date myMarginBeforeDay = getBeforeDay(myBuyTimes,
						myCurEndDay);
				for (java.util.Date myClickTime : myClickTimes) {
					// 更新時間邊界
					int isBiggerThanClick = comDayTime(myMarginBeforeDay,
							myClickTime);
					if (isBiggerThanClick > 0) {
						// 时间维度遍历完了，時間維度发生变化
						if (1 == myNewV.get(0) && myCurEndDay == myEndDay) {
							myNewPredictList.add(myNewV);
							myNewV = new DenseVector(FEATURES);
						} else if (1 == myNewV.get(0)
								&& myCurEndDay == myMarginEndDay) {
							myNewPosiList.add(myNewV);
							myNewV = new DenseVector(FEATURES);
						} else if (1 == myNewV.get(0)
								&& myCurEndDay != myMarginEndDay) {
							myNewNegaList.add(myNewV);
							myNewV = new DenseVector(FEATURES);
						}

						myCurEndDay = myMarginBeforeDay;
						myMarginEndDay = myMarginBeforeDay;
						myMarginBeforeDay = getBeforeDay(myBuyTimes,
								myCurEndDay);
					}

					int rt = comDayTime(myCurEndDay, myClickTime);
					// 如果在一个月内,即值为：1,2,3,4,5,或者是最後一天
					if ((rt > 0 && rt <= 5 && !myClickTime
							.equals(myMarginBeforeDay))
							|| myClickTime == myEndDay) {
						// 截距
						myNewV.set(0, 1);
						// 之前是否购买过
						int isBuyBefore = comDayTime(myBeforeDay,
								myMarginBeforeDay);
						if (0 == isBuyBefore) {
							myNewV.set(1, 0);
						} else {
							myNewV.set(1, 1);
						}
						// 用户转换率
						myNewV.set(2, userRate);
						// 如果在一个月内,即值为：1,2,3,4,5
						myNewV.set(3, myNewV.get(3) + getWeight(rt));

						// 如果不是和倆邊緣重合，則添加.
					} else if (rt != 0
							&& !myClickTime.equals(myMarginBeforeDay)) {
						// 時間維度发生变化
						if (1 == myNewV.get(0) && myCurEndDay == myEndDay) {
							myNewPredictList.add(myNewV);
							myPreSta.addSample(myNewV.get(3));
							myNewV = new DenseVector(FEATURES);
						} else if (1 == myNewV.get(0)
								&& myCurEndDay != myMarginEndDay) {
							myNewNegaList.add(myNewV);
							myNegaSta.addSample(myNewV.get(3));
							myNewV = new DenseVector(FEATURES);
						} else if (1 == myNewV.get(0)
								&& myCurEndDay == myMarginEndDay) {
							myNewPosiList.add(myNewV);
							myPosiSta.addSample(myNewV.get(3));
							myNewV = new DenseVector(FEATURES);
						}

						while (6 == rt) {
							// 往前递归时间，直到找到所属的月
							int isSmallThanMonth = comDayTime(myCurEndDay,
									myMarginBeforeDay);
							int isBiggerThan = comDayTime(myMarginBeforeDay,
									myClickTime);
							if (isSmallThanMonth < 6 || isBiggerThan > 0) {
								myCurEndDay = myMarginBeforeDay;
								myMarginEndDay = myMarginBeforeDay;
								myMarginBeforeDay = getBeforeDay(myBuyTimes,
										myCurEndDay);
							} else {
								myCurEndDay = getLeastDayTime(31, myCurEndDay);
							}

							rt = comDayTime(myCurEndDay, myClickTime);
						}

						if (rt > 0 && rt <= 5
								&& myClickTime != myMarginBeforeDay) {
							// 截距
							myNewV.set(0, 1);
							// 之前是否购买过
							int isBuyBefore = comDayTime(myBeforeDay,
									myMarginBeforeDay);
							if (0 == isBuyBefore) {
								myNewV.set(1, 0);
							} else {
								myNewV.set(1, 1);
							}
							// 用户转换率
							myNewV.set(2, userRate);
							// 如果在一个月内,即值为：1,2,3,4,5
							myNewV.set(3, myNewV.get(3) + getWeight(rt));
						}
					}
				}

				// 时间维度遍历完了，時間維度发生变化
				if (1 == myNewV.get(0) && myCurEndDay == myEndDay) {
					myNewPredictList.add(myNewV);
					myPreSta.addSample(myNewV.get(3));
					myNewV = new DenseVector(FEATURES);
				} else if (1 == myNewV.get(0) && myCurEndDay == myMarginEndDay) {
					myNewPosiList.add(myNewV);
					myPosiSta.addSample(myNewV.get(3));
					myNewV = new DenseVector(FEATURES);
				} else if (1 == myNewV.get(0) && myCurEndDay != myMarginEndDay) {
					myNewNegaList.add(myNewV);
					myNegaSta.addSample(myNewV.get(3));
					myNewV = new DenseVector(FEATURES);
				}

			}

			if (0 != myNewPredictList.size()) {
				myPredictUsersTable.put(userId, myNewPredictList);
			}
			if (0 != myNewPosiList.size()) {
				myPosiUsersTable.put(userId, myNewPosiList);
			}
			if (0 != myNewNegaList.size()) {
				myNegaUsersTable.put(userId, myNewNegaList);
			}
		}

		return 0;
	}

	public int getWeight(int value) {
		return 5 - value;
	}
	public LaneCrossFoldLearner laneLRtrain() {

		MikeAdaptiveLogisticRegression lr = new MikeAdaptiveLogisticRegression(
				2, FEATURES, new L1());
		double meanPosi = myPosiSta.getMk();
		double varPosi = myPosiSta.getRunningVariance();
		// 训练负样本
		Enumeration<Integer> indexPosi = myPosiUsersTable.keys();
		while (indexPosi.hasMoreElements()) {
			int userId = indexPosi.nextElement();
			List<Vector> myPosiUserLogs = myPosiUsersTable.get(userId);
			for (int i = 0; i < myPosiUserLogs.size(); i++) {
				if(myPosiUserLogs.get(i).get(3) >=  meanPosi){
					lr.train(1, myPosiUserLogs.get(i));
				}
			}
		}
		
		double meanNega = myNegaSta.getMk();
		// 训练负样本
		Enumeration<Integer> indexNega = myNegaUsersTable.keys();
		while (indexNega.hasMoreElements()) {
			int userId = indexNega.nextElement();
			List<Vector> myNegaUserLogs = myNegaUsersTable.get(userId);
			for (int i = 0; i < myNegaUserLogs.size(); i++) {
				if(myNegaUserLogs.get(i).get(3) >=  meanNega){
					lr.train(0, myNegaUserLogs.get(i));
				}
			}
		}

		lr.close();

		bestLearner = lr.getBest().getPayload().getLearner();
		System.out.println("auc=" + bestLearner.auc() + " percentCorrect="
				+ bestLearner.percentCorrect() + " LogLikelihood="
				+ bestLearner.getLogLikelihood());

		return bestLearner;
	}

	public Hashtable<Integer, LinkedList<Integer>> lanePredict(
			Hashtable<Integer, LinkedList<Integer>> resultTable) {

		Hashtable<Integer, LinkedList<Integer>> rtItemsTable = new Hashtable<Integer, LinkedList<Integer>>();
		try {
			// ArrayList<Integer> userVector = getUserVector();
			// ArrayList<Integer> itemVector = getItemVector();
			// List<Vector> dataVectorList = getValueVector();

			// 训练负样本
			Enumeration<Integer> indexPre = myPredictUsersTable.keys();
			while (indexPre.hasMoreElements()) {
				int userId = indexPre.nextElement();
				List<Vector> myPreUserLogs = myPredictUsersTable.get(userId);
				for (int i = 0; i < myPreUserLogs.size(); i++) {
					Vector v = bestLearner.classifyFull(myPreUserLogs.get(i));
					int score = bestLearner.classifyFull(myPreUserLogs.get(i))
							.maxValueIndex();

					if (1 == score) {
						boolean isAlreadyInThere;
						isAlreadyInThere = appendResult2HashTable(resultTable,
								userId, itemId);
						if (false == isAlreadyInThere) {
							appendResult2HashTable(rtItemsTable, userId, itemId);
						}
					}
				}
			}

		} catch (SQLException e) {
			// do nothing
		}
		return rtItemsTable;
	}

	public static java.sql.Date getBeforeDay(
			LinkedList<java.sql.Date> myBuyTimes, java.sql.Date curDay) {
		// java.util.Date preDay = myBuyTimes.get(0);
		java.sql.Date Day = myBuyTimes.get(myBuyTimes.size() - 1);

		for (java.sql.Date myBuyTime : myBuyTimes) {
			int rt = comDayTime(myBuyTime, curDay);
			if (rt < 0) {
				Day = myBuyTime;
				break;
			}
		}

		return Day;
	}

	public static java.util.Date getEndDay(
			LinkedList<java.util.Date> myBuyTimes, java.util.Date curDay) {
		java.util.Date preDay = myBuyTimes.get(0);
		java.util.Date Day = myBuyTimes.get(0);

		for (java.util.Date myBuyTime : myBuyTimes) {
			int rt = comDayTime(myBuyTime, curDay);
			if (rt <= 0) {
				preDay = Day;
				Day = myBuyTime;
				break;
			}
		}

		return preDay;
	}

	public static java.sql.Date getLeastDayTime(int numDay,
			java.util.Date baseDay) {

		Calendar myProductcalender = Calendar.getInstance();
		myProductcalender.setTime(baseDay);

		myProductcalender.add(Calendar.DAY_OF_YEAR, -numDay);

		return new java.sql.Date(myProductcalender.getTime().getTime());
	}

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
		return isAlreadyInThere;
	}

	public double getTimeVectorWeight(java.sql.Date myDayTime, int actionType,
			String endDay) {

		Calendar calEnd = Calendar.getInstance();
		Calendar calCur = Calendar.getInstance();
		calCur.setTime(myDayTime);
		try {
			calEnd.setTime(MYSQLCONFIG.dateFormat.parse(endDay));
		} catch (ParseException e) {
			// do nothing
		}

		double daytimeDiff = calEnd.get(Calendar.DAY_OF_YEAR)
				- calCur.get(Calendar.DAY_OF_YEAR);

		double logWeight = Math.log(daytimeDiff + 1);
		double rtWeight = 0;
		switch (actionType) {
		// 点击
		case 0:
		case 2: {
			rtWeight = 1 / Math.pow(4, logWeight);
			// weight.setScalar(1.0 / logWeight);
		}
			break;

		// 购买
		case 1:
		case 3: {
			// rtWeight =2*logWeight;
		}
			break;

		}

		return rtWeight;

	}

	// public int lRForgeDataModel(ItemWithUsersData myTrainItemt,
	// Hashtable<Integer, LinkedList<Integer>> myTargetItemsTable,
	// Hashtable<Integer, Object> userWithItemsTable) {
	//
	// LinkedList<ItemWithUsersData.User> myUsersDatas = myTrainItemt
	// .getUsers();
	//
	// Collections.sort(myUsersDatas,
	// new Comparator<ItemWithUsersData.User>() {
	// @Override
	// public int compare(ItemWithUsersData.User o1,
	// ItemWithUsersData.User o2) {
	// return Integer.valueOf(o1.getUserID()).compareTo(
	// o2.getUserID());
	// }
	// });
	//
	// int oldUserId = -1;
	// int curUserId = 0;
	// DenseVector valueVector = null;
	// double[] userItemAction = { 0, 0};
	// int brandId = myTrainItemt.getProductID();
	// int[] itemAction = myTrainItemt.getItemActionCount();
	// for (Object myUsersData : myUsersDatas) {
	//
	// ItemWithUsersData.User myUsersDatat = (ItemWithUsersData.User)
	// myUsersData;
	// curUserId = myUsersDatat.getUserID();
	//
	// if (curUserId != oldUserId) {
	//
	// valueVector = new DenseVector(FEATURES);
	// userVector.add(curUserId);
	// valueVectorList.add(valueVector);
	//
	// // item购买次数
	// // valueVector.set(0, itemAction[1] + itemAction[3]);
	// // item点击次数
	// // valueVector.set(1, itemAction[0] + itemAction[2]);
	//
	// UserWithItemsData myUser = (UserWithItemsData) userWithItemsTable
	// .get(curUserId);
	// int[] userAction = myUser.getUserActionCount();
	//
	// // user购买频率
	// // valueVector.set(2, userAction[1] + userAction[3]);
	// // user点击频率
	// // valueVector.set(3, userAction[0] + userAction[2]);
	// oldUserId = curUserId;
	// // return userAction alue back to 0
	// for (int j = 0; j < userItemAction.length; j++) {
	// userItemAction[j] = 0;
	// }
	// if (isTrainModel) {
	//
	// shuffleVectorOrder.add(shuffleVectorOrder.size());
	// if (myTargetItemsTable.containsKey(brandId)
	// && myTargetItemsTable.get(brandId).contains(
	// curUserId)) {
	// targetVertor.add(1);
	// } else {
	// targetVertor.add(0);
	// }
	// }
	//
	// }
	//
	// getTimeVectorWeight( myUsersDatat.getVisitDaytime(),
	// myUsersDatat.getType(), userItemAction);
	//
	// // item-user购买时间
	// valueVector.set(0, userItemAction[0]);
	// // item-user点击时间
	// valueVector.set(1, userItemAction[1]);
	//
	// }
	//
	// return 0;
	// }

	public double getItemLeastTwoMonthTrend(
			LinkedList<ItemWithUsersData.User> myUsersDatas) {
		double LeastOneMonth = 0;
		double LeastTwoMonth = 0;
		for (Object myUsersData : myUsersDatas) {
			ItemWithUsersData.User myUsersDatat = (ItemWithUsersData.User) myUsersData;
			java.sql.Date myDate = myUsersDatat.getVisitDaytime();
			Calendar calender = Calendar.getInstance();
			try {
				calender.setTime(MYSQLCONFIG.dateFormat
						.parse(MYSQLCONFIG.DatelRTrainEnd));
				calender.add(Calendar.MONTH, -1);
				String start = MYSQLCONFIG.dateFormat
						.format(calender.getTime());
				String end = MYSQLCONFIG.DatelRTrainEnd;
				boolean isBetweenLeastOne = isBetweenDayTime(myDate.toString(),
						start, end);
				if (isBetweenLeastOne) {
					LeastOneMonth++;
				}

				end = start;
				calender.add(Calendar.MONTH, -1);
				start = MYSQLCONFIG.dateFormat.format(calender.getTime());
				boolean isBetweenLeastTwo = isBetweenDayTime(myDate.toString(),
						start, end);
				if (isBetweenLeastTwo) {
					LeastTwoMonth++;
				}

			} catch (Exception e) {
				// do nothing
			}

		}

		double denominator = LeastOneMonth + LeastTwoMonth;
		if (0 == denominator) {
			return 0.0;
		} else {
			// 用于减小销售量小的商品的权重。
			int weight = (int) Math.min(LeastOneMonth, 1);
			return (LeastOneMonth - weight) / denominator;
		}

	}

	public double getUserLeastTwoMonthTrend(
			LinkedList<UserWithItemsData.Product> myItemsDatas) {
		double LeastOneMonth = 0;
		double LeastTwoMonth = 0;
		for (Object myItemsData : myItemsDatas) {
			UserWithItemsData.Product myItemsDatat = (UserWithItemsData.Product) myItemsData;
			int type = myItemsDatat.getType();
			if (1 == type || 3 == type) {

				java.util.Date myDate = myItemsDatat.getVisitDaytime();
				Calendar calender = Calendar.getInstance();
				try {
					calender.setTime(MYSQLCONFIG.dateFormat
							.parse(MYSQLCONFIG.DatelRTrainEnd));
					calender.add(Calendar.MONTH, -1);
					String start = MYSQLCONFIG.dateFormat.format(calender
							.getTime());
					String end = MYSQLCONFIG.DatelRTrainEnd;
					boolean isBetweenLeastOne = isBetweenDayTime(
							myDate.toString(), start, end);
					if (isBetweenLeastOne) {
						LeastOneMonth++;
					}

					end = start;
					calender.add(Calendar.MONTH, -1);
					start = MYSQLCONFIG.dateFormat.format(calender.getTime());
					boolean isBetweenLeastTwo = isBetweenDayTime(
							myDate.toString(), start, end);
					if (isBetweenLeastTwo) {
						LeastTwoMonth++;
					}

				} catch (Exception e) {
					// do nothing
				}

			}
		}

		double denominator = LeastOneMonth + LeastTwoMonth;
		if (0.0 == denominator) {
			return 0.0;
		} else {
			// 用于减小销售量小的商品的权重。
			int weight = (int) Math.min(LeastOneMonth, 1);
			return (LeastOneMonth - weight) / denominator;
		}

	}

	public boolean isBetweenDayTime(String dayTime, String start, String end) {

		try {
			java.util.Date startDate = MYSQLCONFIG.dateFormat.parse(start);
			java.util.Date endDate = MYSQLCONFIG.dateFormat.parse(end);
			java.util.Date dayTimeDate = MYSQLCONFIG.dateFormat.parse(dayTime);

			if (startDate.compareTo(dayTimeDate) > 0) {
				return false;
			}
			if (endDate.compareTo(dayTimeDate) < 0) {
				return false;
			}
		} catch (ParseException e) {
			// do nothing
		}

		return true;
	}

	// public void setBestShreshold(double min, double max,
	// CrossFoldLearner myCrossFoldLearnerF, List<Integer> test,
	// int counter) {
	//
	// double para = (min + max) / 2;
	// double middle = min + para;
	// double result1 = getShreshold(min, myCrossFoldLearnerF, test);
	// double result2 = getShreshold(middle, myCrossFoldLearnerF, test);
	// double result3 = getShreshold(max, myCrossFoldLearnerF, test);
	// double[] result = { result1, result2, result3 };
	//
	// int maxResultIndex = getMax(result);
	//
	// if (++counter >= 3) {
	// shreshold = result[maxResultIndex];
	// } else {
	// double newMin = 0;
	// double newMax = 0;
	// switch (maxResultIndex) {
	// case 0: {
	// newMin = min;
	// newMax = middle;
	// }
	// break;
	//
	// case 2: {
	// newMin = middle;
	// newMax = max;
	// }
	// break;
	//
	// case 1: {
	// newMin = (min + middle) / 2;
	// newMax = (middle + max) / 2;
	// }
	// break;
	// }
	//
	// setBestShreshold(newMin, newMax, myCrossFoldLearnerF, test, counter);
	// }
	// }

	public int getMax(double[] result) {
		double max = result[0];
		int maxIndex = 0;
		for (int i = 1; i < result.length; i++) {
			if (result[i] > max) {
				max = result[i];
				maxIndex = i;
			}
		}
		return maxIndex;
	}

	// public double getShreshold(double para,
	// CrossFoldLearner myCrossFoldLearnerF, List<Integer> test) {
	// int numHitReal = 0;
	// int numAcceptWrong = 0;
	// for (Integer k : test) {
	// double scalar = bestLearner.classifyScalar(valueVectorList.get(k));
	// if (scalar >= para) {
	// shreshold = 1;
	// }
	//
	// if (1 == shreshold && 0 == targetVertor.get(k)) {
	// numAcceptWrong++;
	// } else if (1 == shreshold && 1 == targetVertor.get(k)) {
	// numHitReal++;
	// }
	//
	// if (1 == targetVertor.get(k)) {
	// System.out.printf(scalar + ", ");
	// }
	// }
	//
	// double hitReal = (double) (numHitReal) / (double) (test.size());
	// double acceptWrong = (double) (numAcceptWrong) / (double) (test.size());
	// double accuracy = hitReal / (acceptWrong + hitReal);
	// System.out.printf("accuracy is %f\n", accuracy);
	//
	// return accuracy;
	//
	// }

	// /////////////////////////////////////////////////

	// public double getmeanScalar() {
	// return meanScalar;
	// }

	public LaneCrossFoldLearner getBestLearner() {
		return bestLearner;
	}

	// public List<Vector> getValueVector() {
	// return valueVectorList;
	// }
	//
	// public ArrayList<Integer> getUserVector() {
	// return userVector;
	// }
	//
	// public ArrayList<Integer> getItemVector() {
	// return itemVector;
	// }
	//
	// public void setItemVector(ArrayList<Integer> itemVector) {
	// this.itemVector = itemVector;
	// }

}
