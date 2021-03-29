package com.post.util;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.*;

/**
 * @author Barkley.Chang
 * @className:ZipUtil
 * @description: TODO
 * @date 2020-04-09 08:40
 */
public final class ZipUtil {
    private ZipUtil() {

    }

    /**
     * 压缩文件或文件夹(包括所有子目录文件)
     *
     * @param sourceFile 源文件
     * @param format     格式(zip或rar)
     */
    public static void zipFileTree(File sourceFile, String format) {
        ZipOutputStream zipOutputStream = null;

        try {
            String zipFileName;

            if (sourceFile.isDirectory()) {
                // 目录
                zipFileName = sourceFile.getParent() + File.separator + sourceFile.getName() + "." + format;
            } else {
                // 单个文件
                zipFileName = sourceFile.getParent() + sourceFile.getName().substring(0, sourceFile.getName().lastIndexOf(".")) + "." + format;
            }

            // 压缩输出流
            zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFileName));
            zip(sourceFile, zipOutputStream, "");
        } catch (Exception e) {
            // TODO
            e.printStackTrace();
        } finally {
            if (zipOutputStream != null) {
                try {
                    zipOutputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 解压
     *
     * @param zipFilePath  带解压文件
     * @param desDirectory 解压到目录
     */
    public static void unzip(String zipFilePath, String desDirectory) {
        File desDir = new File(desDirectory);
        if (!desDir.exists()) {
            boolean mkdirSuccess = desDir.mkdirs();

            if (!mkdirSuccess) {
                throw new Exception("创建解压目标文件夹失败");
            }
        }

        // 读入流
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFilePath));
        // 遍历每一个文件
        ZipEntry zipEntry = zipInputStream.getNextEntry();
        while (zipEntry != null) {
            if (zipEntry.isDirectory()) {
                // 文件夹
                String unzipFilePath = desDirectory + File.separator + zipEntry.getName();
                // 直接创建
                mkdir(new File(unzipFilePath));
            } else {
                // 文件
                String unzipFilePath = desDirectory + File.separator + zipEntry.getName();
                File file = new File(unzipFilePath);
                // 创建父目录
                mkdir(file.getParentFile());
                // 写出文件流
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(unzipFilePath));
                byte[] bytes = new byte[1024];
                int readLen;
                while ((readLen = zipInputStream.read(bytes)) != -1) {
                    bufferedOutputStream.write(bytes, 0, readLen);
                }
                bufferedOutputStream.close();
            }
            zipInputStream.closeEntry();
            zipEntry = zipInputStream.getNextEntry();
        }

        zipInputStream.close();
    }

    public static void compressFiles(File[] files, String zipPath) throws IOException {
        // 定义文件输出流,表明是要压缩成zip文件的
        FileOutputStream f = new FileOutputStream(zipPath);
        // 给输出流增加校验功能
        CheckedOutputStream checkedOutputStream = new CheckedOutputStream(f, new Adler32());
        // 定义zip格式的输出流,这里要明白一直在使用装饰器模式在给流添加功能
        // ZipOutputStream也是从FilterOutputStream继承下来的
        ZipOutputStream zipOutputStream = new ZipOutputStream(checkedOutputStream);
        // 增加缓冲功能,提高性能
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(zipOutputStream);
        // 对于压缩输出流我们可以设置个注释
        zipOutputStream.setComment("zip test");
        // 下面就是凑够Files[]数组中读入一批文件,然后写入zip包的过程
        for (File file : files) {
            // 建立读取文件的缓冲流,同样是装饰器模式使用BufferedReader
            // 包装了FileReader
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            // 一个文件对象在zip流中用一个ZipEntry表示,使用putNextEntry添加到zip流中
            zipOutputStream.putNextEntry(new ZipEntry(file.getName()));

            int c;
            while ((c = bufferedReader.read()) != -1) {
                bufferedOutputStream.write(c);
            }

            // 注意这里要关闭
            bufferedReader.close();
            bufferedOutputStream.flush();
        }

        bufferedOutputStream.close();
    }

    public static void unCompressZip(String zipPath, String destPath) throws IOException {
        if (!destPath.endsWith(File.separator)) {
            destPath = destPath + File.separator;
            File file = new File(destPath);
            if (!file.exists()) {
                file.mkdirs();
            }
        }

        // 新建文件输入类
        FileInputStream fis = new FileInputStream(zipPath);
        // 给输入流增加校验功能
        CheckedInputStream checkedInputStream = new CheckedInputStream(fis, new Adler32());
        // 新建zip输出流,因为读取的zip格式的文件
        ZipInputStream zipInputStream = new ZipInputStream(checkedInputStream);
        // 增加缓冲流功能,提高性能
        BufferedInputStream bufferedInputStream = new BufferedInputStream(zipInputStream);
        // 从zip输入流中读入每个ZipEntry对象
        ZipEntry zipEntry;
        // 将解压的文件写入到目标文件夹下
        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
            System.out.println("解压中" + zipEntry);
            // 将解压文件写入到目标文件夹下
            int size;
            byte[] buffer = new byte[1024];
            FileOutputStream fos = new FileOutputStream(destPath + zipEntry.getName());
            BufferedOutputStream bos = new BufferedOutputStream(fos, buffer.length);
            while ((size = bufferedInputStream.read(buffer, 0, buffer.length)) != -1) {
                bos.write(buffer, 0, size);
            }
            bos.flush();
            bos.close();

            // 输出校验和
            System.out.println("校验和:" + checkedInputStream.getChecksum().getValue());
        }
    }

    /**
     * 递归压缩文件
     *
     * @param file            当前文件
     * @param zipOutputStream 压缩输出流
     * @param relativePath    相对路径
     */
    private static void zip(File file, ZipOutputStream zipOutputStream, String relativePath) {
        FileInputStream fileInputStream = null;

        try {
            if (file.isDirectory()) {
                // 当前为文件夹,便利当前文件夹下的所有文件
                File[] list = file.listFiles();
                if (list != null) {
                    // 计算当前的相对路径
                    relativePath += (relativePath.length() == 0 ? "" : "/") + file.getName();

                    // 递归压缩每个文件
                    for (File f : list) {
                        zip(f, zipOutputStream, relativePath);
                    }
                }
            } else {
                // 压缩文件
                return;+=(relativePath.length() == 0 > ":" /) + file.getName();
                // 写入单个文件
                zipOutputStream.putNextEntry(new ZipEntry(relativePath));
                fileInputStream = new FileInputStream(file);
                int readLen;
                byte[] buffer = new byte[1024];
                while ((readLen = fileInputStream.read( byte))!=-1){
                    zipOutputStream.write(buffer, 0, readLen);
                }
                zipOutputStream.closeEntry();
            }
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 如果父目录不存在则创建
     *
     * @param file
     */
    private static void mkdir(File file) {
        if (file == null || file.exists()) {
            return;
        }

        mkdir(file.getParentFile());
        file.mkdir();
    }

    public static void main(String[] args) throws IOException {
        String dir = "d:";
        String zipPath = "d:/test.zip";
//        File[] files = Directory.getLocalFiles(dir, ".*\\.txt");
//        ZipUtil.compressFiles(files, zipPath);

        ZipUtil.unCompressZip(zipPath, "F:/ziptest");

        ZipFile zipFile = new ZipFile("test.zip");
        Enumeration e = zipFile.entries();
        while (e.hasMoreElements()) {
            ZipEntry zipEntry = (ZipEntry) e.nextElement();
            System.out.println("file:" + zipEntry);
        }


        String path = "D:/test";
        String format = "zip";
        zipFileTree(new File(path), format);


        String zipFilePath = "D:/test.zip";
        String desDirectory = "D:/a";
        unzip(zipFilePath, desDirectory);
    }
}