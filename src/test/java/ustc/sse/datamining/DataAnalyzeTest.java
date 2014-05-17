package ustc.sse.datamining;

import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import org.apache.mahout.math.DenseVector;

public class DataAnalyzeTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		double r = 0.104;
		r = r*10;
		boolean is = (r/1.0 == 1);
		
		
		DenseVector v = new DenseVector(3);
		v.set(0, 4.5);
		v.set(0, 6.5);
		v.set(0, 19.5);
		
		try {
			 java.util.Date mDate = MYSQLCONFIG.dateFormat.parse("2013-07-15");
			java.util.Date aDate=MYSQLCONFIG.dateFormat.parse("2013-07-25");
			long k=(mDate.getTime()-aDate.getTime())/(1000*60*60*24);
			Math.abs(k);
			int i = 0;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 test();
		ExcelWriter writer = null;
		try {
			writer = new ExcelWriter("D:\\test\\test.xlsx");
		} catch (IOException e) {
		}

		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		Calendar tempEarlierCal = Calendar.getInstance();
		try {
			tempEarlierCal.setTime(format1.parse(MYSQLCONFIG.DateThreshold));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tempEarlierCal.add(Calendar.MONTH, 1);
		Date tempSqlDate = new java.sql.Date(tempEarlierCal.getTime().getTime());
		tempEarlierCal.add(Calendar.MONTH, 1);
		tempEarlierCal.add(Calendar.DAY_OF_YEAR, 0);
		String Cal = tempEarlierCal.toString();
		java.util.Date tempSqlDate2 = tempEarlierCal.getTime();

		LinkedList<Integer> resultItems = new LinkedList<Integer>();
		resultItems.add(4);
		resultItems.add(234);
		resultItems.add(77);
		resultItems.add(2);
		boolean contains = resultItems.contains(77);

		System.out.println("results: ");
	}

	public static void test() {

		Hashtable<Integer, Integer> map = new Hashtable<Integer, Integer>();
		ValueComparator bvc = new ValueComparator(map);
		TreeMap<Integer, Integer> sorted_map = new TreeMap<Integer, Integer>(
				bvc);

		map.put(12, 99);
		map.put(2, 67);
		map.put(56, 67);
		map.put(32, 67);

		Enumeration<Integer> key = map.keys();
		while (key.hasMoreElements()) {
			int myk = key.nextElement();
			int j = map.get(myk);
			int i = 1;
		}
		// System.out.println("unsorted map: "+map);

		sorted_map.putAll(map);

		System.out.println("results: " + sorted_map);
	}

}

class ValueComparator implements Comparator<Integer> {

	Map<Integer, Integer> base;

	public ValueComparator(Map<Integer, Integer> base) {
		this.base = base;
	}

	// Note: this comparator imposes orderings that are inconsistent with
	// equals.
	public int compare(Integer a, Integer b) {
		if (base.get(a) >= base.get(b)) {
			return -1;
		} else {
			return 1;
		} // returning 0 would merge keys
	}
}

