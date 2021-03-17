package com.post.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Barkley.Chang
 * @className:RuntimeUtil
 * @description: TODO
 * @date 2021-02-11 19:31
 */
public final class RuntimeUtil {
    private RuntimeUtil() {
    }

    public static String runCmd() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process proc = runtime.exec(String.format("cmd /c %s", "cmd命令，如dir、type等,若要启动一个新的Console执行命令，只需要将dir改写为start dir即可"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String runBash() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process proc = runtime.exec(String.format("/bin/sh -c %s", "shell命令，如ls、cat等,若要启动一个新的Terminal执行命令，只需要将ls改写为xterm -e ls即可"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String cmdDemo() {
        StringBuilder sb = new StringBuilder();
        Runtime r = Runtime.getRuntime();
        try {
            Process proc = r.exec("cmd /c dir"); // 假设该操作为造成大量内容输出
            // 采用字符流读取缓冲池内容，腾出空间
            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream(), "gbk"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                System.out.println(line);
            }

            // 或采用字节流读取缓冲池内容，腾出空间
            // ByteArrayOutputStream pool = new ByteArrayOutputStream();
            // byte[] buffer = new byte[1024];
            // int count = -1;
            // while ((count = proc.getInputStream().read(buffer)) != -1){
            //   pool.write(buffer, 0, count);
            //   buffer = new byte[1024];
            // }
            // System.out.println(pool.toString("gbk"));

            int exitVal = proc.waitFor();
            System.out.println(exitVal == 0 ? "成功" : "失败");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public static void redirectInputOutputDemo() {
        try {
            ProcessBuilder pb = new ProcessBuilder("cmd");
            // 在标准输入中，通过换行符分隔多行命令。
            // commands.txt的内容为
            // javac Demo.java
            // java Demo
            File commands = new File("/path/to/commands.txt");
            File error = new File("/path/to/error");
            File output = new File("/path/to/output");

            pb.redirectInput(commands);
            pb.redirectOutput(output);
            pb.redirectError(error);

            pb.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}