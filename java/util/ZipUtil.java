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
    }
}