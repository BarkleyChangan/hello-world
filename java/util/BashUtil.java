package com.post.common.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

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
	 * @param shell
	 * 
	 * @return
	 * @author Barkley.Chang
	 * @date 2020-03-10 08:40
	 */
	public static final String exec(String shell) {
		if (shell == null || shell.trim().length() == 0) {
			return "";
		}

		StringBuilder result = new StringBuilder();
		Process process = null;
		BufferedReader buffIn = null;
		BufferedReader buffError = null;

		try {
			// 执行命令, 返回一个子进程对象（命令在子进程中执行）
			process = Runtime.getRuntime().exec(new String[] { "/bin/sh", "-c", shell }, null, null);
			// 获取命令执行结果, 有两个结果: 正常的输出 和 错误的输出（PS: 子进程的输出就是主进程的输入）
			buffIn = new BufferedReader(new InputStreamReader(process.getInputStream(), GlobalConstant.UTF8));
			buffError = new BufferedReader(new InputStreamReader(process.getErrorStream(), GlobalConstant.UTF8));
			// 方法阻塞, 等待命令执行完成（成功会返回0）
			process.waitFor();

			// 读取输出
			String line = null;

			while ((line = buffIn.readLine()) != null) {
				result.append(line).append(System.lineSeparator());
			}

			while ((line = buffError.readLine()) != null) {
				result.append(line).append(System.lineSeparator());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (buffIn != null) {
				try {
					buffIn.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (buffError != null) {
				try {
					buffError.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// 销毁子进程
			if (process != null) {
				try {
					process.destroy();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return result.toString().trim();
	}
}