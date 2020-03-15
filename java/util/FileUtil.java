package com.post.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Path;

import org.apache.commons.lang3.StringUtils;

/**
 * 操作文件工具类
 * 
 * @author Barkley.Chang
 * @date 2020-03-14 16:05
 * 
 */
public final class FileUtil {
	private FileUtil() {
	}

	/**
	 * 读取文件
	 * 
	 * @param path
	 * @param bufferSize
	 *            缓冲区大小,默认1M
	 * @param charSet
	 * @return
	 * @author Barkley.Chang
	 * @date 2020-03-10 14:20
	 */
	public static String readFile(Path path, int bufferSize, String charSet) {
		if (path == null) {
			return "";
		}

		File file = path.toFile();
		if (!file.exists()) {
			return "";
		}

		bufferSize = bufferSize < 1 ? 1 : bufferSize;
		charSet = charSet == null ? "UTF-8" : charSet;
		StringBuilder sb = new StringBuilder();
		BufferedInputStream bis = null;
		BufferedReader bi = null;

		try {
			bis = new BufferedInputStream(new FileInputStream(file));
			bi = new BufferedReader(new InputStreamReader(bis, charSet), bufferSize * 1024 * 1024);
			int index = 0;
			String line = "";

			while ((line = bi.readLine()) != null) {
				if (index != 0) {
					sb.append(System.lineSeparator());
				}

				sb.append(line);
				index++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bi != null) {
				try {
					bi.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (bis != null) {
				try {
					bis.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();
	}

	/**
	 * 将内容写入文件中
	 * 
	 * @param fullFileName
	 * @param content
	 * @param charSet
	 * @return
	 * @author Barkley.Chang
	 * @date 2020-03-10 16:31
	 */
	public static boolean writeFile(String fullFileName, String content, String charSet) {
		if (StringUtils.isBlank(fullFileName)) {
			return false;
		}

		charSet = charSet == null ? "UTF-8" : charSet;
		File file = new File(fullFileName);
		FileOutputStream fos = null;

		try {
			fos = new FileOutputStream(file);
			fos.write(content.getBytes(charSet));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return false;
	}

	/**
	 * 删除文件或文件夹(如果是文件夹保留当前目录)
	 * 
	 * @param path
	 *            文件或文件夹
	 * @return
	 */
	public static boolean delete(String path) {
		File file = new File(path);

		if (!file.exists()) {
			return false;
		}

		boolean reuslt = false;
		if (file.isFile()) {
			reuslt = deleteFile(path);
		} else if (file.isDirectory()) {
			File[] files = file.listFiles();

			if (files != null && files.length > 0) {
				for (File tmpFile : files) {
					if (tmpFile.isFile()) {
						// 删除子文件
						reuslt = deleteFile(tmpFile.getAbsolutePath());

						if (!reuslt) {
							break;
						}
					} else if (tmpFile.isDirectory()) {
						// 删除子目录
						reuslt = deleteDirectory(tmpFile.getAbsolutePath());

						if (!reuslt) {
							break;
						}
					}
				}
			}
		}

		return reuslt;
	}

	/**
	 * 删除单个文件
	 * 
	 * @param fullFileName
	 *            文件全路径
	 * @return
	 */
	public static boolean deleteFile(String fullFileName) {
		File file = new File(fullFileName);

		if (!file.exists() || !file.isFile()) {
			return false;
		}

		boolean result = false;

		try {
			result = file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 删除目录及目录下的文件(当前目录也一起删除)
	 * 
	 * @param dir
	 *            要删除的目录
	 * @return 目录删除成功返回true，否则返回false
	 */
	public static boolean deleteDirectory(String dir) {
		if (dir == null || dir.trim().length() == 0) {
			return false;
		}

		// 如果dir不以文件分隔符结尾，自动添加文件分隔符
		if (!dir.endsWith(File.separator)) {
			dir = dir + File.separator;
		}

		File dirFile = new File(dir);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}

		boolean flag = true;
		// 删除文件夹中的所有文件包括子目录
		File[] files = dirFile.listFiles();

		if (files != null && files.length > 0) {
			for (File tmpFile : files) {
				if (tmpFile.isFile()) {
					// 删除子文件
					flag = deleteFile(tmpFile.getAbsolutePath());

					if (!flag) {
						break;
					}
				} else if (tmpFile.isDirectory()) {
					// 删除子目录
					flag = deleteDirectory(tmpFile.getAbsolutePath());

					if (!flag) {
						break;
					}
				}
			}
		}

		if (!flag) {
			return false;
		}

		// 删除当前目录
		try {
			flag = dirFile.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return flag;
	}

	/**
	 * 拷贝文件
	 * 
	 * @param sourceFullFileName
	 * @param destFullFileName
	 * @param bufferSize
	 *            缓冲区大小
	 * @return
	 */
	public static boolean copy(String sourceFullFileName, String destFullFileName, int bufferSize) {
		if (sourceFullFileName == null || sourceFullFileName.trim().length() == 0 || destFullFileName == null || destFullFileName.trim().length() == 0) {
			return false;
		}

		File sourceFile = new File(sourceFullFileName);
		File destFile = new File(destFullFileName);

		if (!sourceFile.exists() || !sourceFile.isFile()) {
			return false;
		}

		boolean result = false;
		InputStream in = null;
		OutputStream out = null;
		bufferSize = bufferSize < 1 ? 1024 : bufferSize;

		try {
			in = new FileInputStream(sourceFile);
			out = new FileOutputStream(destFile);

			byte[] buffer = new byte[bufferSize];
			int len;

			while ((len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return result;
	}

	/**
	 * Java NIO方式拷贝文件
	 * 
	 * @param sourceFullFileName
	 * @param destFullFileName
	 */
	public static boolean copyNio(String sourceFullFileName, String destFullFileName) {
		if (sourceFullFileName == null || sourceFullFileName.trim().length() == 0 || destFullFileName == null || destFullFileName.trim().length() == 0) {
			return false;
		}

		File sourceFile = new File(sourceFullFileName);
		File destFile = new File(destFullFileName);

		if (!sourceFile.exists() || !sourceFile.isFile()) {
			return false;
		}

		boolean result = false;
		FileChannel input = null;
		FileChannel output = null;

		try {
			input = new FileInputStream(sourceFile).getChannel();
			output = new FileOutputStream(destFile).getChannel();
			output.transferFrom(input, 0, input.size());
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (output != null) {
				try {
					output.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return result;
	}

	public static void main(String[] args) {
		// String dir = "C:\\Users\\chang\\Desktop\\解密enc文件工具\\backup\\";
		// System.out.println(delete(dir));
		// System.out.println(deleteDirectory(dir));

		System.out.println(copy("C:\\Users\\chang\\Desktop\\碑林区学生健康监测台帐.xls", "C:\\Users\\chang\\Desktop\\bootstrap-3.3.7-dist\\456.xls", 1024));
		// System.out.println(copyNio("C:\\Users\\chang\\Desktop\\碑林区学生健康监测台帐 - 数据.xls",
		// "C:\\Users\\chang\\Desktop\\bootstrap-3.3.7-dist\\123.xls"));
	}
}