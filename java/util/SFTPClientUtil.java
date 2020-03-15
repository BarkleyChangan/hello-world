package com.post.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;

/**
 * SFTP工具类:依赖jsch-0.1.55.jar
 * 
 * @author Barkley.Chang
 * @date 2020-03-06 15:41
 * 
 */
public final class SFTPClientUtil {
	private ChannelSftp channelSftp;
	private Session session;

	/**
	 * SFTP 登录用户名
	 */
	private String username;

	/**
	 * SFTP 登录密码
	 */
	private String password;

	/**
	 * 私钥路径
	 */
	private String privateKeyPath;

	/**
	 * 密钥口令
	 */
	private String passphrase;

	/**
	 * SFTP 服务器地址IP地址
	 */
	private String host;

	/**
	 * SFTP 端口
	 */
	private int port;

	/**
	 * 构造基于密码认证的channelSftp对象
	 */
	public SFTPClientUtil(String host, String username, String password, int port) {
		this.host = host;
		this.username = username;
		this.password = password;
		this.port = port;
	}

	/**
	 * 构造基于秘钥认证的channelSftp对象
	 */
	public SFTPClientUtil(String host, String username, String password, int port, String privateKeyPath, String passphrase) {
		this.host = host;
		this.username = username;
		this.password = password;
		this.port = port;
		this.privateKeyPath = privateKeyPath;
		this.passphrase = passphrase;
	}

	/**
	 * 连接channelSftp服务器
	 */
	public void connect() {
		try {
			JSch jsch = new JSch();
			if (privateKeyPath != null && privateKeyPath.trim().length() > 0) {
				// 使用密钥验证方式，密钥可以使有口令的密钥，也可以是没有口令的密钥
				if (passphrase != null && passphrase.trim().length() > 0) {
					// 设置私钥,私钥密码
					jsch.addIdentity(privateKeyPath, passphrase);
				} else {
					// 设置私钥
					jsch.addIdentity(privateKeyPath);
				}
			}

			session = jsch.getSession(username, host, port);

			if (password != null && password.trim().length() > 0) {
				session.setPassword(password);
			}

			Properties config = new Properties();
			// do not verify host key
			config.put("StrictHostKeyChecking", "no");
			// config.put("UseDNS", "no");

			session.setConfig(config);
			// session.setTimeout(3000);
			// session.setServerAliveInterval(92000);
			System.out.print(String.format("host:%s;username:%s;password:%s;port:%s;privateKeyPath:%s;passphrase:%s;", host, username, password, String.valueOf(port), privateKeyPath, passphrase));
			session.connect();

			// 参数sftp指明要打开的连接是sftp连接
			// Channel channel = session.openChannel("channelSftp");
			Channel channel = session.openChannel("sftp");
			channel.connect();

			channelSftp = (ChannelSftp) channel;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * sftp is connected
	 * 
	 * @return
	 */
	public boolean isConnected() {
		return channelSftp != null && channelSftp.isConnected();
	}

	/**
	 * 关闭连接channelSftp
	 */
	public void close() {
		if (channelSftp != null && channelSftp.isConnected()) {
			try {
				channelSftp.disconnect();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (session != null && session.isConnected()) {
			try {
				session.disconnect();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 将输入流的数据上传到channelSftp作为文件
	 * 
	 * @param directory
	 *            上传到该目录
	 * @param channelSftpFileName
	 *            channelSftp端文件名
	 * @param is
	 *            上传文件流
	 * @return
	 */
	public boolean uploadFile(String directory, String channelSftpFileName, InputStream is) {
		if (channelSftpFileName == null || channelSftpFileName.trim().length() == 0 || is == null) {
			return false;
		}

		try {
			cd(directory);
			channelSftp.put(is, channelSftpFileName, ChannelSftp.OVERWRITE); // 上传文件
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return false;
	}

	/**
	 * 根据本地文件路径上传文件
	 * 
	 * @param directory
	 *            上传到该目录
	 * @param channelSftpFileName
	 *            channelSftp端文件名
	 * @param localFullFileName
	 *            本地文件完整路径
	 * @return
	 */
	public boolean uploadFile(String directory, String channelSftpFileName, String localFullFileName) {
		if (channelSftpFileName == null || channelSftpFileName.trim().length() == 0 || localFullFileName == null || localFullFileName.trim().length() == 0) {
			return false;
		}

		try {
			cd(directory);
			File file = new File(localFullFileName);
			channelSftp.put(new FileInputStream(file), channelSftpFileName, ChannelSftp.OVERWRITE); // 上传文件
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 下载文件
	 * 
	 * @param directory
	 *            下载目录
	 * @param downloadFileName
	 *            下载的文件名
	 * @param saveFullFileName
	 *            文件保存在本地的完整路径
	 * @return
	 */
	public boolean downloadFile(String directory, String downloadFileName, String saveFullFileName) {
		System.out.println("directory:" + directory + " downloadFileName:" + downloadFileName + " saveFullFileName:" + saveFullFileName);
		if (downloadFileName == null || downloadFileName.trim().length() == 0 || saveFullFileName == null || saveFullFileName.trim().length() == 0) {
			return false;
		}

		File file = null;

		try {
			cd(directory);
			file = new File(saveFullFileName);
			channelSftp.get(downloadFileName, new FileOutputStream(file));
			return true;
		} catch (Exception e) {
			e.printStackTrace();

			if (file != null) {
				file.delete();
			}
		}

		return false;
	}

	// public InputStream downFile(String remotePath, String remoteFile) throws
	// Exception {
	// try {
	// if (sftp == null)
	// connect();
	// sftp.cd(remotePath);
	// return sftp.get(remoteFile);
	// } catch (SftpException e) {
	// logger.error("文件下载失败或文件不存在！", e);
	// throw e;
	// } finally {
	// // disconnect();
	// }
	// }
	//
	// public byte[] downLoad(String remotePath, String remoteFile) throws
	// Exception {
	// try {
	// if (sftp == null)
	// connect();
	// sftp.cd(remotePath);
	// InputStream input = sftp.get(remoteFile);
	// ByteArrayOutputStream output = new ByteArrayOutputStream();
	// byte[] buffer = new byte[10485760];
	// int n = 0;
	// while (-1 != (n = input.read(buffer))) {
	// output.write(buffer, 0, n);
	// }
	// return output.toByteArray();
	// } catch (SftpException e) {
	// logger.error("文件下载失败或文件不存在！", e);
	// } finally {
	// disconnect();
	// }
	// return null;
	// }

	/**
	 * 删除文件
	 * 
	 * @param directory
	 *            要删除文件所在目录
	 * @param deleteFile
	 *            要删除的文件
	 * @return
	 */
	public boolean delete(String directory, String deleteFile) {
		if (deleteFile == null || deleteFile.trim().length() == 0) {
			return false;
		}

		try {
			cd(directory);
			channelSftp.rm(deleteFile);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 查看目录下的文件
	 * 
	 * @param directory
	 *            要列出的目录
	 */
	public List<String> listFiles(String directory) {
		List<String> lstFile = new ArrayList<String>();

		if (directory == null || directory.trim().length() == 0) {
			return lstFile;
		}

		try {
			cd(directory);
			Vector vector = channelSftp.ls(directory);

			if (vector != null) {
				for (Iterator<ChannelSftp.LsEntry> iterator = vector.iterator(); iterator.hasNext();) {
					ChannelSftp.LsEntry lsEntry = iterator.next();
					String fileName = lsEntry.getFilename();

					if (fileName.equals(".") || fileName.equals("..")) {
						continue;
					}

					lstFile.add(fileName);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return lstFile;
	}

	/**
	 * 判断目录是否存在
	 * 
	 * @param directory
	 * @return
	 */
	public boolean isExistDirectory(String directory) {
		if (directory == null || directory.trim().length() == 0) {
			return false;
		}

		try {
			SftpATTRS sftpATTRS = channelSftp.lstat(directory);
			return sftpATTRS.isDir();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param directory
	 * @param fileName
	 * @return
	 */
	public boolean isExistFile(String directory, final String fileName) {
		if (directory == null || directory.trim().length() == 0 || fileName == null || fileName.trim().length() == 0) {
			return false;
		}

		final List<String> lstFile = new ArrayList<String>();
		ChannelSftp.LsEntrySelector selector = new ChannelSftp.LsEntrySelector() {
			@Override
			public int select(ChannelSftp.LsEntry lsEntry) {
				if (fileName.equalsIgnoreCase(lsEntry.getFilename())) {
					lstFile.add(fileName);
				}

				return 0;
			}
		};

		try {
			channelSftp.ls(directory, selector);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return lstFile.size() > 0;
	}

	/**
	 * 执行相关的命令
	 * 
	 * @param command
	 */
	// public void execCmd(String command) {
	// BufferedReader reader = null;
	// Channel channel = null;
	//
	// try {
	// while (command != null) {
	// channel = session.openChannel("exec");
	// ((ChannelExec) channel).setCommand(command);
	//
	// channel.setInputStream(null);
	// ((ChannelExec) channel).setErrStream(System.err);
	//
	// channel.connect();
	// InputStream in = channel.getInputStream();
	// reader = new BufferedReader(new InputStreamReader(in));
	// String buf = null;
	// while ((buf = reader.readLine()) != null) {
	// System.out.println(buf);
	// }
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// reader.close();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// }

	/**
	 * 执行cd命令
	 * 
	 * @param directory
	 */
	public void cd(String directory) {
		if (directory == null || directory.trim().length() == 0 || "/".equals(directory)) {
			return;
		}

		try {
			channelSftp.cd(directory);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// /**
	// * 从输入流中获取字节数组
	// *
	// * @param is
	// * @return
	// */
	// private static byte[] readInputStream(InputStream is) {
	// if (is == null) {
	// return new byte[0];
	// }
	//
	// byte[] buffer = new byte[1024];
	// int len = 0;
	// ByteArrayOutputStream baos = null;
	//
	// try {
	// baos = new ByteArrayOutputStream();
	// while ((len = is.read(buffer)) != -1) {
	// baos.write(buffer, 0, len);
	// }
	//
	// return baos.toByteArray();
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// if (baos != null) {
	// try {
	// baos.close();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// }
	//
	// return new byte[0];
	// }

	public static void main(String[] args) throws Exception {
		// CMD:
		// sftp -o port=1000 username@remote ip
		// sftp -oIdentityFile=/root/.ssh/id_rsa -oPort=8001 ems@114.255.225.37
		// Demo:
		// javac -encoding UTF-8 -classpath
		// .;/root/.ssh/download/jsch-0.1.55.jar -d .
		// /root/.ssh/download/SFTPClientUtil.java
		// javac -encoding UTF-8 -Djava.ext.dirs=./ -d .
		// /root/.ssh/download/SFTPClientUtil.java

		SFTPClientUtil channelSftp = null;
		try {
			channelSftp = new SFTPClientUtil("114.255.225.37", "ems", "", 8001, "/root/.ssh/id_rsa", "");
			channelSftp.connect();
			System.out.print("connect");
			// 遍历目录:
			// List<String> lstFile =
			// channelSftp.listFiles("/MailpckPush/download");
			// for (String fileName : lstFile) {
			// boolean result =
			// channelSftp.downloadFile("/MailpckPush/download", fileName,
			// "/root/.ssh/download/" + fileName);
			// System.out.println("fileName:" + fileName + ",result:" + result);
			// }

			// 下载:
			// boolean result =
			// channelSftp.downloadFile("/MailpckPush/download",
			// "ZGYZGHX00200交寄清单20250520182000.txt.enc",
			// "ZGYZGHX00200交寄清单20250520182000.txt.enc");
			// System.out.println("下载:" + result);

			// 上传:
			String dir = "/root/.ssh/download/";
			String[] arrLocalFiles = { "ZGYZEMS_LOGISSTATUSRETFILE_20200312002017.txt", "ZGYZEMS_LOGISSTATUSRETFILE_20200312002017.txt.sign", "ZGYZEMS_LOGISSTATUSRETFILE_20200312002017.txt.check" };
			for (String fileName : arrLocalFiles) {
				boolean result = channelSftp.uploadFile("/MailpckStatusUpload/upload", fileName, Paths.get(dir, fileName).toString());
				System.out.println("fileName上传:" + fileName + ",result:" + result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (channelSftp != null) {
				channelSftp.close();
			}
		}

		// File file1 = new
		// File("C:\\Users\\chang\\Desktop\\ZGYZEMS_LOGISSTATUSRETFILE_20200312002017.txt");
		// File file2 = new
		// File("C:\\Users\\chang\\Desktop\\ZGYZEMS_LOGISSTATUSRETFILE_20200312002017.txt.sign");
		//
		// System.out.println(file1.length()); // 622
		// System.out.println(file2.length()); // 256
		//
		// CheckFileJson checkFileJson = new CheckFileJson();
		// FileInfoJson fileInfoJson = new FileInfoJson();
		// fileInfoJson.setSourceFile("ZGYZEMS_LOGISSTATUSRETFILE_20200312002017.txt");
		// fileInfoJson.setSourceFileSize("622");
		// fileInfoJson.setSignFile("ZGYZEMS_LOGISSTATUSRETFILE_20200312002017.txt.sign");
		// fileInfoJson.setSignFileSize("256");
		// fileInfoJson.setRemark("EMS反传邮寄状态测试文件");
		// checkFileJson.setFileInfoJson(fileInfoJson);
		// System.out.println(FastJsonUtil.bean2json(checkFileJson));

		// System.out.println(APIPUtil.readFile(Paths.get("D:\\document\\project\\postBank\\接口文档\\ZGYZGHX00200交寄清单20250520182000.txt"),
		// 1));
	}
}