package com.dbs.sg.DTE12.service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import com.dbs.sg.DTE12.DAO.DictionaryParaUpdateDAO;
import com.dbs.sg.DTE12.common.Logger;

public class ReadDateCard {

	private static Logger logger;

	public static Date binary2Date(byte[] date, int baseYear) {
		// System.out.println(bytesToHex(date));
		int day = date[1] & 0x1F;
		int month = ((date[0] & 0x01) << 3) | ((date[1] & 0xE0) >> 5);
		int year = (date[0] & 0xFE) >> 1;
		// System.out.println("" + day + "," + month + "," + year);
		Calendar c = Calendar.getInstance();
		c.set(baseYear + year, month - 1, day);
		return c.getTime();
	}

	public static Date binary2Date(byte[] src, int offset, int baseYear) {
		// System.out.println(bytesToHex(date));
		byte[] date = sub(src, offset, 2);
		int day = date[1] & 0x1F;
		int month = ((date[0] & 0x01) << 3) | ((date[1] & 0xE0) >> 5);
		int year = (date[0] & 0xFE) >> 1;
		// System.out.println("" + day + "," + month + "," + year);
		Calendar c = Calendar.getInstance();
		c.set(baseYear + year, month - 1, day);
		return c.getTime();
	}

	/**
	 * @param pdIn
	 * @return
	 */
	public static byte[] unzipComp3(byte[] pdIn) {
		byte[] res = new byte[pdIn.length * 2 - 1];
		for (int i = 0; i < pdIn.length; i++) {
			byte aByte = pdIn[i];
			byte b = (byte) (0xF0 | (byte) (aByte >> 4)); // HO first
			res[i * 2] = b;
			if (i < pdIn.length - 1) {
				res[i * 2 + 1] = (byte) (0xF0 | aByte);
			}
		}
		return res;
	}

	public static byte[] unzipComp3(byte[] src, int offset, int len) {
		byte[] res = new byte[len * 2 - 1];
		for (int i = 0; i < len; i++) {
			byte aByte = src[offset + i];
			byte b = (byte) (0xF0 | (byte) (aByte >> 4)); // HO first
			res[i * 2] = b;
			if (i < len - 1) {
				res[i * 2 + 1] = (byte) (0xF0 | aByte);
			}
		}
		return res;
	}

	/**
	 * @param src
	 * @param offset
	 * @param length
	 * @return
	 */
	public static byte[] sub(byte[] src, int offset, int length) {
		byte[] res = new byte[length];
		for (int i = 0; i < length; i++)
			res[i] = src[i + offset];
		return res;
	}

	/**
	 * @param ba1
	 * @param ba2
	 * @return
	 */
	public static byte[] append(byte[] ba1, byte[] ba2) {
		byte[] res = new byte[ba1.length + ba2.length];
		for (int i = 0; i < ba1.length; i++)
			res[i] = ba1[i];
		for (int j = 0; j < ba2.length; j++)
			res[j + ba1.length] = ba2[j];
		return res;
	}

	/**
	 * @param buf
	 * @return
	 */
	static String bytesToHex(byte[] buf) {
		final String HexChars = "0123456789ABCDEF";
		StringBuffer sb = new StringBuffer((buf.length / 2) * 5 + 3);
		for (int i = 0; i < buf.length; i++) {
			byte b = buf[i];
			b = (byte) (b >> 4); // Hit to bottom
			b = (byte) (b & 0x0F); // get HI byte
			sb.append(HexChars.charAt(b));
			b = buf[i]; // refresh
			b = (byte) (b & 0x0F); // get LOW byte
			sb.append(HexChars.charAt(b));
			if (i % 2 == 1)
				sb.append(' ');
		}
		return sb.toString();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			if (args.length < 4) {
				System.out.println("arguments wrong");
				System.out
						.println("Usage: java com.dbs.sg.DTE12.service.ReadDateCard [configPath] [EBCDICFilePath] [current_business_date] [business_date]");
				System.out.println("1");
				System.exit(1);
			}
			logger = Logger.getLogger(args[0], ReadDateCard.class);
			DictionaryParaUpdateDAO dao = new DictionaryParaUpdateDAO(args[0]);

			// SimpleDateFormat yyDDD = new SimpleDateFormat("yyDDD");
			SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat yyMMdd = new SimpleDateFormat("yyMMdd");
			SimpleDateFormat ZyyMMdd = new SimpleDateFormat("0yyMMdd");
			SimpleDateFormat dbsDateFormat = new SimpleDateFormat("yyyy/MM/dd");
			// SimpleDateFormat MMMddyyyy = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
			Calendar c = Calendar.getInstance();
			// c.add(Calendar.MONTH, -18);
			String today = "004" + yyMMdd.format(c.getTime());
			logger.info("Begin to read datecard file with args "
					+ Arrays.asList(args));
			boolean bToday = false;
			FileOutputStream fosc = new FileOutputStream(args[2]);
			FileOutputStream fosb = new FileOutputStream(args[3]);
			FileInputStream fis = new FileInputStream(args[1]);
			byte[] rec = new byte[50];
			int rnum = 0;
			do {
				rnum = fis.read(rec);
				String s = new String(rec, "Cp1047");
				if (s.startsWith(today)) {
					bToday = true;
					// 33-36 next working day 0YYMMDD+
					Date dNextWorkingday = ZyyMMdd.parse(new String(
							unzipComp3(sub(rec, 33, 4)), "Cp1047"));
					//2010.05.11 Jason start, update business date with batch run date instead of next working date.
					String busdate = yyyyMMdd.format(c.getTime());
					logger.info("Begin to update business date[" + busdate
							+ "]...");
					dao.updateSystemDate(busdate);
					//2010.05.11 Jason end
					//2010.05.13 Jason start, new parameter NEXT_BUS_DATE with next working day
					dao.updateDictPara("_ALL_", "NEXT_BUS_DATE", yyyyMMdd.format(dNextWorkingday));
					//2010.05.13 Jason end
					logger.info("Update business date successfully.");
					logger
							.info("Begin to generate date card for Algo[system date="
									+ dbsDateFormat.format(c.getTime())
									+ "busdate="
									+ dbsDateFormat.format(dNextWorkingday)
									+ "]...");
					fosc.write(dbsDateFormat.format(c.getTime()).getBytes());
					fosb
							.write(dbsDateFormat.format(dNextWorkingday)
									.getBytes());
					logger.info("Generate date card for Algo successfully.");
					// 0-2 004
					// 3-8 yyMMdd
					// 9-22 MMM dd, yyyy
					// 23-25 today YYDDD+
					// 26-28 last working day YYDDD+
					// 29-32 last working day 0YYMMDD+
					// 33-36 next working day 0YYMMDD+
					// 37 week code,
					// ***** '1' - SUNDAY
					// ***** '2' - MONDAY
					// ***** '3' - TUESDAY
					// ***** '4' - WEDNESDAY
					// ***** '5' - THURDAY
					// ***** '6' - FRIDAY
					// ***** '7' - SATURDAY
					// ***** '8' - HOLIDAY
					// 38-39 today binary yyyyyyyMMMMddddd
					// 40-42 next working day YYDDD+
					// byte[] res = new byte[rec.length * 2];
					// res = append(sub(rec, 0, 23), unzipComp3(sub(rec, 23,
					// 3)));
					// res = append(res, unzipComp3(sub(rec,26,3)));
					// res = append(res, unzipComp3(sub(rec,29,4)));
					// res = append(res, unzipComp3(sub(rec,33,4)));
					// res = append(res, sub(rec,37,1));
					// res = new String(res, "Cp1047").getBytes("ASCII");
					// res = append(res, yyyyMMdd.format(binary2Date(sub(rec,
					// 38, 2),1999)).getBytes("ASCII"));
					// res = append(res, new String(unzipComp3(sub(rec,40,3)),
					// "Cp1047").getBytes("ASCII"));
					// String dateLine = new String(unzipComp3(rec, 23, 3),
					// "Cp1047");
					// if (today.equals(dateLine)) {
					// }
					// System.out.println(bytesToHex(rec));
					// System.out.println(bytesToHex(res));
					// fos.write(res);
				}
			} while (rnum >= 0);

			fis.close();
			fosc.flush();
			fosc.close();
			fosb.flush();
			fosb.close();

			if (!bToday) {
				throw new Exception("Datecard file[" + args[1]
						+ "] do not include date["
						+ yyyyMMdd.format(c.getTime()) + "].");
			}

			logger.info("Read datecard file finished.");
			System.out.println("0");
			System.exit(0);
		} catch (Exception e) {
			logger.error("Error while read datecard file.", e);
			System.out.println("1");
			System.exit(1);
		}
	}

}
