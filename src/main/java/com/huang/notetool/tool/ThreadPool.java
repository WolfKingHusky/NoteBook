package com.huang.notetool.tool;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池
 *
 * @author 黄先生
 * @version 1.0
 * @Content 此处说明类的解释
 * @CreateDate 2019-05-21 15:34:41
 * @History 序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-05-21   创建项目           完成
 * @LeftoverProblem 此处说明遗留问题和潜在的问题
 */
public class ThreadPool {
    /**
     * 创建通用线程池
     */
    private static ThreadPoolExecutor threadPoolExecutor;

    /**
     * 手动创建线程池
     * 参数含义：
     * -- corePoolSize : 线程池中常驻的线程数量。核心线程数，默认情况下核心线程会一直存活，即使处于闲置状态也不会受存keepAliveTime限制。除非将allowCoreThreadTimeOut设置为true。
     * -- maximumPoolSize : 线程池所能容纳的最大线程数。超过这个数的线程将被阻塞。当任务队列为没有设置大小的LinkedBlockingDeque时，这个值无效。
     * --  keepAliveTime : 当线程数量多于 corePoolSize 时，空闲线程的存活时长，超过这个时间就会被回收
     * -- unit : keepAliveTime 的时间单位
     * -- workQueue : 存放待处理任务的队列，该队列只接收 Runnable 接口
     * -- threadFactory : 线程创建工厂
     * -- handler : 当线程池中的资源已经全部耗尽，添加新线程被拒绝时，会调用RejectedExecutionHandler的rejectedExecution方法，参考 ThreadPoolExecutor 类中的内部策略类
     */
    private static void createThreadPool() {
        // 创建线程工厂
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("xxx-pool-%d")
                .build();
        // 创建通用线程池
        threadPoolExecutor = new ThreadPoolExecutor(
                2,
                200,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024),
                threadFactory,
                new ThreadPoolExecutor.AbortPolicy());
    }

    /**
     * 关闭线程池
     */
    public static void shutDownThreadPool() {
        threadPoolExecutor.shutdown();
    }

    /**
     * 获取线程池中的线程
     *
     * @param runnable 线程
     * @return 线程
     */
    private static Thread getThread(Runnable runnable) {
        if (null == threadPoolExecutor || null == threadPoolExecutor.getThreadFactory()) {
            createThreadPool();
        }
        return threadPoolExecutor.getThreadFactory().newThread(runnable);
    }

    /**
     * 开始线程
     *
     * @param runnable 线程
     * @param waitTime 等待时间，单位ms
     */
    public static void startThread(Runnable runnable, Long waitTime) throws InterruptedException {
        Thread splashThread = getThread(runnable);
        if (null != waitTime) {
            Thread.sleep(waitTime);
        }
        splashThread.start();
    }

    /**
     * 关闭线程池线程
     */
    public static void shutDownThread(Thread thread) {
        thread.interrupt();
    }
}
