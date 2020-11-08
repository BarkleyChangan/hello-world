package com.post.util;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *@className:ShellUtil
 *@description: Shell命令运行工具类封装
 *@author Barkley.Chang
 *@date 2020-11-08 20:42
 */
public final class ShellUtil {
    // 错误日志读取线程池（不设上限）
    private final static ExecutorService errReadThreadPool = Executors.newCachedThreadPool(new NamedThreadFactory("ReadProcessErrout"));
    // 最后一次异常信息
    private final static Map<Thread, ProcessErrorLogDescriptor> lastErrorHolder = new ConcurrentHashMap<>();

    private ShellUtil() {
    }

    {

    }

    /**
     * @see #runShellCommandSync(String, String[], Charset, String)
     */
    public static int runShellCommandSync(String baseShellDir, String[] cmd, Charset outputCharset) throws IOException {
        return runShellCommandSync(baseShellDir, cmd, outputCharset, null);
    }

    /**
     * 真正运行Shell命令
     * @param baseShellDir 运行命令所在目录(先切换到该目录后再运行命令)
     * @param cmd 命令数组
     * @param outputCharset 日志输出字符集,一般Windows为GBK，Linux为UTF8
     * @param logFilePath 日志输出文件路径,为空直接输出到当前应用日志中,否则写入该文件
     * @return 进程退出码-0:成功;其他:
     * @throws IOException 执行异常时抛出
     */
    public static int runShellCommandSync(String baseShellDir, String[] cmd, Charset outputCharset,
                                          String logFilePath) throws IOException {
        long startTime = System.currentTimeMillis();
        boolean needReadProcessOutLogStreamByHand = true;
        System.out.println("【CLI】receive new Command.baseDir:" + baseShellDir + ", cmd:" + String.join(" ", cmd) + "," +
                "logFile:" + logFilePath);
        ProcessBuilder processBuilder = new ProcessBuilder(cmd);
        processBuilder.directory(new File(baseShellDir));
        initErrorLogHolder(logFilePath, outputCharset);
        int exitCode = 0;

        try {
            if (logFilePath != null) {
                ensureFilePathExists(logFilePath);
//                String redirectLogInfoAndErrCmd = " > " + logFilePath + " 2>&1 ";
//                cmd = mergeTwoArr(cmd, redirectLogInfoAndErrCmd.split("\\s+"));
                processBuilder.redirectErrorStream(true);
                processBuilder.redirectOutput(new File(logFilePath));
                needReadProcessOutLogStreamByHand = false;
            }

            Process process = processBuilder.start();

            if (needReadProcessOutLogStreamByHand) {
                readProcessOutLogStream(process, outputCharset);
            }

            try {
                process.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
                setProcessLastError("中断异常:" + e.getMessage());
            } finally {
                exitCode = process.exitValue();
                System.out.println("【CLI】process costTime:" + (System.currentTimeMillis() - startTime) + "ms, exitCode:" + String.valueOf(exitCode));
            }

            if (exitCode != 0) {
                throw new ShellProcessExecException(exitCode, "进程返回异常信息,returnCode:" + exitCode + ",lastError:" + getProcessLastError());
            }
        } finally {
            removeErrorLogHolder();
        }
    }

    /**
     * 使用Runtime.exec()运行Shell
     * @return
     */
    public static int runShellWithRuntime(String baseShellDir, String[] cmd, Charset outputCharset) throws IOException {
        long startTime = System.currentTimeMillis();
        initErrorLogHolder(null, outputCharset);
        Process process = Runtime.getRuntime().exec(cmd, null, new File(baseShellDir));
        int exitCode;

        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
            setProcessLastError("中断异常:" + e.getMessage());
        } catch (Throwable e) {
            e.printStackTrace();
            setProcessLastError(e.getMessage());
        } finally {
            exitCode = process.exitValue();
            System.out.println("【CLI】process costTime:" + (System.currentTimeMillis() - startTime) + "ms, exitCode:" + String.valueOf(exitCode));
        }

        if (exitCode != 0) {
            throw new ShellProcessExecException(exitCode, "进程返回异常信息, returnCode:" + exitCode + ", lastError:" + getProcessLastError());
        }

        return exitCode;
    }

    /**
     * 确保文件夹存在
     * @param filePath 文件路径
     * @throws IOException 创建文件夹异常
     */
    public static void ensureFilePathExists(String filePath)
            throws IOException {
        File path = new File(filePath);
        if (path.exists()) {
            return;
        }

        File p = path.getParentFile();
        if (p.mkdirs()) {
            System.out.println("为文件创建目录:" + p.getPath() + "成功!");
            return;
        }

        System.out.println("为文件创建目录:" + p.getPath() + "失败!!!");
    }

    /**
     * 合并两个数组数据
     * @param arrFirst 左边数组
     * @param arrAppend 要添加的数组
     * @return 合并后的数组
     */
    public static String[] mergeTwoArr(String[] arrFirst, String[] arrAppend) {
        String[] merged = new String[arrFirst.length + arrAppend.length];
        System.arraycopy(arrFirst, 0, merged, 0, arrFirst.length);
        System.arraycopy(arrAppend, 0, merged, arrFirst.length, arrAppend.length);

        return merged;
    }

    /**
     * 删除以某字符结尾的字符
     *
     * @param originalStr 原始字符
     * @param toTrimChar 要检测的字
     * @return 裁剪后的字符串
     */
    public static String trimEndWith(String originalStr, char toTrimChar) {
        char[] value = originalStr.toCharArray();
        int i = value.length - 1;
        while (i > 0 && value[i] == toTrimChar) {
            i--;
        }

        return new String(value, 0, i + 1);
    }

    /**
     * 主动读取进程的标准输出信息日志
     * @param process 进程实体
     * @param outputCharset 日志字符集
     * @throws IOException 读取异常时抛出
     */
    private static void readProcessOutLogStream(Process process, Charset outputCharset) throws IOException {
        try (BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream(), outputCharset))) {
            Thread parentThread = Thread.currentThread();
            // 另启一个线程读取错误消息,必须先启动该线程
            errReadThreadPool.submit(() -> {
                try {
                    try (BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream(),
                            outputCharset))) {
                        String err;
                        while ((err = stdError.readLine()) != null) {
                            System.out.println("【CLI】" + err);
                            setProcessLastError(parentThread, err);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    setProcessLastError(parentThread, e.getMessage());
                }
            });

            // 外部线程读取标准输出信息
            String stdOut;
            while ((stdOut = stdInput.readLine()) != null) {
                System.out.println("【CLI】" + stdOut);
            }
        }
    }

    /**
     * 新建一个进程错误信息容器
     * @param logFilePath
     * @param outputCharset
     */
    private static void initErrorLogHolder(String logFilePath, Charset outputCharset) {
        lastErrorHolder.put(Thread.currentThread(), new ProcessErrorLogDescriptor(logFilePath, outputCharset));
    }

    /**
     * 移除错误日志监听
     */
    private static void removeErrorLogHolder() {
        lastErrorHolder.remove(Thread.currentThread());
    }

    /**
     * 获取进程最后错误信息(NOTE:该方法只会在父线程中调用)
     * @return
     */
    private static String getProcessLastError() {
        Thread thread = Thread.currentThread();
        return lastErrorHolder.get(thread).getLastError;
    }

    /**
     * 设置最后一个错误信息描述(NOTE:使用当前线程或自定义)
     * @param lastError
     */
    private static void setProcessLastError(String lastError) {
        lastErrorHolder.get(Thread.currentThread()).setLastError(lastError);
    }

    private static void setProcessLastError(Thread thread, String lastError) {
        lastError.get(thread).setLastError(lastError);
    }

    /**
     * 判断当前系统是否是Windows
     * @return
     */
    public static boolean isWindowSystemOS() {
        return System.getProperty("os.name").toLowerCase().startsWith("win");
    }

    /**
     * 进程错误信息描述封装
     */
    private static class ProcessErrorLogDescriptor {
        // 错误信息记录文件
        private String logFile;
        // 最后一行错误信息
        private String lastError;
        private Charset charset;

        ProcessErrorLogDescriptor(String logFile, Charset outputCharset) {
            this.logFile = logFile;
            this.charset = outputCharset;
        }

        String getLastError() {
            if (lastError != null) {
                return lastError;
            }

            try {
                if (logFile == null) {
                    return null;
                }

                StringBuilder sb = new StringBuilder();
                // 没有FileUtils工具类,故而注释
//                List<String> lines = FileUtils.readLines(new File(logFile), charset);
//                for (int i = lines.size() - 1; i >= 0; i--) {
//                    sb.insert(0, lines.get(i) + "\n");
//                    if (sb.length() > 200) {
//                        break;
//                    }
//                }
                return sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        void setLastError(String err) {
            if (lastError == null) {
                this.lastError = err;
                return;
            }

            this.lastError = lastError + "\n" + err;
            if (lastError.length() > 200) {
                lastError = lastError.substring(lastError.length() - 200);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        testRuntimeExec();
        testProcessBuilder();
        testRuntimeShell();
        testRuntimeShellWithErr();
        testProcessShell1();
        testProcessShell1WithErr();
        testProcessShell1WithErr2();
        System.out.println("finish...");
    }

    public static void testRuntimeShell() throws IOException {
        int errCode = ShellUtil.runShellWithRuntime("E:\\tmp", new String[]{"cmd", "/c", "dir"},
                Charset.forName("gbk"));
//        Assert.assertEquals("进程返回码不正确", 0, errCode);
    }

    public static void testRuntimeShellWithErr() throws IOException {
        int errCode;
        errCode = ShellUtil.runShellWithRuntime("E:\\tmp",
                new String[]{"cmd", "/c", "dir2"}, Charset.forName("gbk"));
//        Assert.fail("dir2 应该要执行失败，但却通过了，请查找原因");
    }

    public static void testProcessShell1() throws IOException {
        int errCode;
        errCode = ShellUtil.runShellCommandSync("/tmp",
                new String[]{"cmd", "/c", "dir"}, Charset.forName("gbk"));
//        Assert.assertEquals("进程返回码不正确", 0, errCode);

        String logPath = "/tmp/cmd.log";
        errCode = ShellUtil.runShellCommandSync("/tmp",
                new String[]{"cmd", "/c", "dir"}, Charset.forName("gbk"), logPath);
//        Assert.assertTrue("结果日志文件不存在", new File(logPath).exists());
    }

    public static void testProcessShell1WithErr() throws IOException {
        int errCode;
        errCode = ShellUtil.runShellCommandSync("/tmp",
                new String[]{"cmd", "/c", "dir2"}, Charset.forName("gbk"));
//        Assert.fail("dir2 应该要执行失败，但却通过了，请查找原因");
    }

    public static void testProcessShell1WithErr2() throws IOException {
        int errCode;
        String logPath = "/tmp/cmd2.log";
        try {
            errCode = ShellUtil.runShellCommandSync("/tmp",
                    new String[]{"cmd", "/c", "dir2"}, Charset.forName("gbk"), logPath);
        } catch (ShellProcessExecException e) {
            e.printStackTrace();
            throw e;
        }
//        Assert.assertTrue("结果日志文件不存在", new File(logPath).exists());
    }

    static void testRuntimeExec() {
        try {
            Process process = Runtime.getRuntime().exec("cmd.exe /c dir");
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void testProcessBuilder() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("ipconfig");
        // 将标准输入流和错误输入流合并,通过标准输入流读取信息
        processBuilder.redirectErrorStream(true);

        try {
            // 启动进程
            Process process = processBuilder.start();
            // 获取输入流
            InputStream inputStream = process.getInputStream();
            // 转成字符输入流
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "GBK");
            int len = -1;
            char[] c = new char[1024];
            StringBuilder sb = new StringBuilder();
            // 读取进程输入流内容
            while ((len = inputStreamReader.read(c)) != -1) {
                String s = new String(c, 0, len);
                sb.append(s);
                System.out.println(s);
            }
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    static Process exec(String[] cmdArray, String[] envp, File dir) {
//        return new ProcessBuilder(cmdArray)
//                .environment(envp)
//                .directory(dir)
//                .start();
//    }
}