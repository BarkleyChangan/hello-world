package com.post.common.util;

import com.post.common.constant.GlobalConstant;

/**
 * 字符编码转换工具
 * 
 * @author Barkley.Chang
 * @date 2020-03-17 12:17
 * 
 */
public final class CharacterCodeUtil {

	private CharacterCodeUtil() {
	}

	/**
	 * 将UTF8字符串转为GBK
	 * 
	 * @param utf8Str
	 * @return
	 */
	public static String getGBKFromUTF8(String utf8Str) {
		if (utf8Str == null) {
			return "";
		}

		String result = "";

		try {
			result = new String(utf8Str.getBytes(GlobalConstant.UTF8), GlobalConstant.GBK);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 将GBK字符串转为UTF-8
	 * 
	 * @param gbkStr
	 * @return
	 */
	public static String getUTF8FromGBK(String gbkStr) {
		if (gbkStr == null) {
			return "";
		}

		String result = "";

		try {
			result = new String(getUTF8BytesFromGBK(gbkStr), GlobalConstant.UTF8);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 获取GBK字符串的字节数组
	 * 
	 * @param gbkStr
	 * @return
	 */
	private static byte[] getUTF8BytesFromGBK(String gbkStr) {
		if (gbkStr == null) {
			return new byte[0];
		}

		int n = gbkStr.length();
		byte[] utfBytes = new byte[3 * n];
		int k = 0;
		for (int i = 0; i < n; i++) {
			int m = gbkStr.charAt(i);
			if (m < 128 && m >= 0) {
				utfBytes[k++] = (byte) m;
				continue;
			}
			utfBytes[k++] = (byte) (0xe0 | (m >> 12));
			utfBytes[k++] = (byte) (0x80 | ((m >> 6) & 0x3f));
			utfBytes[k++] = (byte) (0x80 | (m & 0x3f));
		}

		if (k < utfBytes.length) {
			byte[] tmp = new byte[k];
			System.arraycopy(utfBytes, 0, tmp, 0, k);
			return tmp;
		}

		return utfBytes;
	}
}