package com.post.demo;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Barkley.Chang
 * @className:Application
 * @description: 体验Java优雅停服
 * @date 2020-05-21 08:53
 */
public class Application {
    private ScheduledThreadPoolExecutor moniorService;

    public Application() {
        moniorService = new ScheduledThreadPoolExecutor(1);
    }

    /**
     * 启动监控该服务,监控一下内存信息
     */
    public void start() {
        System.out.printf("启动监控服务:%s%n", Thread.currentThread().getId());

        moniorService.scheduleWithFixedDelay(() -> {
            System.out.printf("最大内存:%dm,已分配内存:%dm,已分配内存中的剩余内存:%dm,最大可用内存:%dm%n",
                    Runtime.getRuntime().maxMemory() / 1024 / 1024,
                    Runtime.getRuntime().totalMemory() / 1024 / 1024,
                    Runtime.getRuntime().freeMemory() / 1024 / 1024,
                    (Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory() + Runtime.getRuntime()
                            .freeMemory()) / 1024 / 1024);
        }, 2, 2, TimeUnit.SECONDS);
    }

    /**
     * 释放资源(代码来源于Flume源码)
     * 主要用于关闭线程池
     */
    public void stop() {
        System.out.printf("开始关闭线程:%s%n", Thread.currentThread().getId());

        if (moniorService != null) {
            moniorService.shutdown();
            try {
                moniorService.awaitTermination(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (!moniorService.isTerminated()) {
                moniorService.shutdownNow();

                try {
                    while (!moniorService.isTerminated()) {
                        moniorService.awaitTermination(10, TimeUnit.SECONDS);
                    }
                } catch (InterruptedException e) {
                    System.err.println("Interrupted while waiting for monitor service to stop");
                }
            }
        }

        System.out.printf("线程池关闭完成:%s%n", Thread.currentThread().getId());
    }

    /**
     * 应用程序入口
     *
     * @param args
     */
    public static void main(String[] args) {
        Application application = new Application();
        // 启动服务(每隔一段时间监控输出一下内存信息)
        application.start();

        // 添加钩子,实现优雅停服(主要验证钩子作用)
        final Application appReference = application;
        Runtime.getRuntime().addShutdownHook(new Thread("shutdown-hook") {
            @Override
            public void run() {
                System.out.println("接收到退出的讯号，开始打扫战场，释放资源，完成优雅停服");
                appReference.stop();
            }
        });
        
        System.out.println("服务启动完成");
    }
}