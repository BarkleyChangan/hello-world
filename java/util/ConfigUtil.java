package com.hnpost.icbcapi.util;

import java.net.URL;
import java.net.URLDecoder;
import java.util.Locale;

/**
 * @author Barkley.Chang
 * @className:ConfigUtil
 * @description: TODO
 * @date 2021-03-17 14:06
 */
public final class ConfigUtil {
    private ConfigUtil() {
    }

    /**
     * 获取是否以jar包的方式直接运行
     *
     * @return
     */
    public static boolean isRunningJar() {
        URL url = ConfigUtil.class.getResource("");
        String protocol = url.getProtocol();
        return "jar".equalsIgnoreCase(protocol);
    }

    /**
     * 获取配置文件路径
     *
     * @param fileName
     * @return
     */
    public static String getConfigFilePath(String fileName) {
        boolean isRunningJar = isRunningJar();
        return isRunningJar ? getRunJarFilePath(fileName) : getRunClassFilePath(fileName);
    }

    /**
     * 获取运行jar包(java -jar post_icbc_api.jar)所在目录(不支持中文)
     *
     * @param fileName
     * @return
     */
    public static String getRunJarFilePath(String fileName) {
        if (fileName == null || fileName.trim().length() == 0) {
            return "";
        }

        String filePath = "";
        URL URL = ConfigUtil.class.getProtectionDomain().getCodeSource().getLocation();

        try {
            // 转化为UTF-8编码,支持中文
            filePath = URLDecoder.decode(URL.getPath(), "UTF-8");
            System.out.println("filePath:" + filePath);
            if (filePath.toLowerCase(Locale.getDefault()).endsWith(".jar")) {
                // 获取jar包所在目录
                filePath = filePath.substring(0, filePath.lastIndexOf("/") + 1);
            }

            filePath += fileName;
            System.out.println("getRunJarFilePath:" + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return filePath;
    }

    /**
     * 获取ClassPath下文件路径(不需要以'/'开头)
     *
     * @param fileName
     * @return
     */
    public static String getRunClassFilePath(String fileName) {
        if (fileName == null || fileName.trim().length() == 0) {
            return "";
        }

        URL url = ConfigUtil.class.getClassLoader().getResource(fileName);

        if (url == null) {
            LogUtil.info(ConfigUtil.class.getSimpleName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "方法" + fileName + "获取到的URL为空");
            return "";
        }

        System.out.println("getRunClassFilePath:" + url.getPath());
        return url.getPath();
    }
}