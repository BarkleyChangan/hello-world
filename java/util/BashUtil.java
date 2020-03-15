package com.post.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

import com.post.common.constant.GlobalConstant;

/**
 * 执行/bin/sh命令的工具类
 * 
 * @author Barkley.Chang
 * @date 2020-03-14 17:11
 * 
 */
public final class BashUtil {
	private BashUtil() {
	}

	/**
	 * 执行shell命令
	 * 
	 * @param sh
	 * 
	 * @return
	 * @author Barkley.Chang
	 * @date 2020-03-10 08:40
	 */
	public static final String exec(String shell) {
		if (shell == null || shell.trim().length() == 0) {
			return "";
		}

		String result = "";

		try {
			String[] sh = new String[] { "/bin/sh", "-c", shell };
			// Execute Shell Command
			System.out.println("sh:" + Arrays.toString(sh));
			ProcessBuilder pb = new ProcessBuilder(sh);
			Process p = pb.start();
			result = getShellOut(p);
			System.out.println("result:\"" + result + "\"");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 读取输出流数据
	 * 
	 * @param p
	 *            进程
	 * @return 从输出流中读取的结果
	 * @author Barkley.Chang
	 * @date 2020-03-10 08:58
	 */
	private static final String getShellOut(Process p) {
		if (p == null) {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		BufferedInputStream in = null;
		BufferedReader br = null;

		try {
			in = new BufferedInputStream(p.getInputStream());
			br = new BufferedReader(new InputStreamReader(in, GlobalConstant.UTF8));
			String s;
			int index = 0;

			while ((s = br.readLine()) != null) {
				if (index > 0) {
					// 追加换行符
					sb.append(System.lineSeparator());
				}

				sb.append(s);
				index++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();
	}
}