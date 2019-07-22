package com.huang.notetool.frame.start;

import com.huang.notetool.po.SkinLookAndFeel;
import com.huang.notetool.service.SkinLookAndFeelService;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.List;

/**
 * 启动加载界面
 *
 * @author 黄先生
 * @version 2.0
 * @Content 项目重构和更新
 * @CreateDate 2019-05-20 12:55:39
 * @History 序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-05-20   创建项目           完成
 * @LeftoverProblem 此处说明遗留问题和潜在的问题
 * Compilation failed: internal java compiler error
 * 导致这个错误的原因主要是因为jdk版本问题，此处有两个原因，一个是编译版本不匹配，一个是当前项目jdk版本不支持
 */
public class NoteStartBar extends JWindow implements Runnable {
    /**
     * 进度条
     */
    private JProgressBar progressBar;
    /**
     * 信息
     */
    private String msg;
    /**
     * 皮肤Service
     */
    private SkinLookAndFeelService skinLookAndFeelService;
    /**
     * 日志
     */
    private Logger logger = Logger.getLogger(NoteStartBar.class);
    private SkinLookAndFeel skinLookAndFeel;

    public NoteStartBar() {
        skinLookAndFeelService = new SkinLookAndFeelService();
        logger.info("初始化进度条... ");
        //得到容器
        Container container = getContentPane();
        //设置光标
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        //图片的位置
        URL url = getClass().getResource("login.jpg");
        if (url != null) {
            //增加图片
            container.add(new JLabel(new ImageIcon(url)), BorderLayout.CENTER);
        }
        //实例化进度条
        progressBar = new JProgressBar(1, 100);
        //描绘文字
        progressBar.setStringPainted(true);
        //设置显示文字
        progressBar.setString("加载程序中,请稍候......");
        //设置背景色
        progressBar.setBackground(Color.white);
        //增加进度条到容器上
        container.add(progressBar, BorderLayout.SOUTH);
        //得到屏幕尺寸
        Dimension screen = getToolkit().getScreenSize();
        //窗口适应组件尺寸
        pack();
        //设置窗口位置
        setLocation((screen.width - getSize().width) / 2, (screen.height - getSize().height) / 2);
    }

    public NoteStartBar(String text) {
        this.msg = text;
    }


    /**
     * 启动线程
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        this.toFront();
        //显示窗口
        setVisible(true);
        try {
            if (null == progressBar) {
                //运行主程序
                showFrame();
                //释放窗口
                dispose();
                return;
            }
            for (int num = 0; num < 100; num++) {
                //线程休眠
                Thread.sleep(5);
                if (num == 90) {
                    //运行主程序
                    showFrame();
                }
                if (num == 30) {
                    List<SkinLookAndFeel> skinLookAndFeelList = skinLookAndFeelService.findAll();
                    if (null != skinLookAndFeelList
                            && !skinLookAndFeelList.isEmpty()) {
                        skinLookAndFeel = skinLookAndFeelList.get(0);
                    }
                }
                if (null != progressBar) {
                    //设置进度条值
                    progressBar.setValue(progressBar.getValue() + 1);
                }
            }
        } catch (Exception ex) {
            logger.warn(ex);
            //退出程序
            System.exit(0);
        }
        dispose(); //释放窗口
    }

    private void start() {
        //窗口前端显示
        this.toFront();
        //实例化线程
        //定义线程,进度条更新线程

        //Thread splashThread = ThreadPool.getThread(this);
        //开始运行线程
        // splashThread.start();
    }

    //    public static void main(String[] args) {
    //        NoteStartBar noteStartBar = new NoteStartBar();
    //        noteStartBar.start();
    //    }

    /**
     * 运行主窗口
     */
    private void showFrame() {
        new Note(msg, skinLookAndFeel);
    }
}

//5. 常用的几种线程池
//5.1 newCachedThreadPool
//创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
//
//这种类型的线程池特点是：
//
//工作线程的创建数量几乎没有限制(其实也有限制的,数目为Interger. MAX_VALUE), 这样可灵活的往线程池中添加线程。
//如果长时间没有往线程池中提交任务，即如果工作线程空闲了指定的时间(默认为1分钟)，则该工作线程将自动终止。终止后，如果你又提交了新的任务，则线程池重新创建一个工作线程。
//在使用CachedThreadPool时，一定要注意控制任务的数量，否则，由于大量线程同时运行，很有会造成系统瘫痪。
//示例代码如下：
//
//复制代码
// 1 package test;
// 2 import java.util.concurrent.ExecutorService;
// 3 import java.util.concurrent.Executors;
// 4 public class ThreadPoolExecutorTest {
// 5  public static void main(String[] args) {
// 6   ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
// 7   for (int i = 0; i < 10; i++) {
// 8    final int index = i;
// 9    try {
//10     Thread.sleep(index * 1000);
//11    } catch (InterruptedException e) {
//12     e.printStackTrace();
//13    }
//14    cachedThreadPool.execute(new Runnable() {
//15     public void run() {
//16      System.out.println(index);
//17     }
//18    });
//19   }
//20  }
//21 }
//复制代码
//5.2 newFixedThreadPool
//创建一个指定工作线程数量的线程池。每当提交一个任务就创建一个工作线程，如果工作线程数量达到线程池初始的最大数，则将提交的任务存入到池队列中。
//
//FixedThreadPool
// 是一个典型且优秀的线程池，它具有线程池提高程序效率和节省创建线程时所耗的开销的优点。但是，在线程池空闲时，即线程池中没有可运行任务时，它不会释放工作线程，还会占用一定的系统资源。
//
//示例代码如下：
//
//
//
//复制代码
//package test;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//public class ThreadPoolExecutorTest {
// public static void main(String[] args) {
//  ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
//  for (int i = 0; i < 10; i++) {
//   final int index = i;
//   fixedThreadPool.execute(new Runnable() {
//    public void run() {
//     try {
//      System.out.println(index);
//      Thread.sleep(2000);
//     } catch (InterruptedException e) {
//      e.printStackTrace();
//     }
//    }
//   });
//  }
// }
//}
//复制代码
//
//
//因为线程池大小为3，每个任务输出index后sleep 2秒，所以每两秒打印3个数字。
//定长线程池的大小最好根据系统资源进行设置如Runtime.getRuntime().availableProcessors()。
//
//
//
//5.3 newSingleThreadExecutor
//创建一个单线程化的Executor，即只创建唯一的工作者线程来执行任务，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO,优先级)
// 执行。如果这个线程异常结束，会有另一个取代它，保证顺序执行。单工作线程最大的特点是可保证顺序地执行各个任务，并且在任意给定的时间不会有多个线程是活动的。
//
//示例代码如下：
//
//
//
//复制代码
//package test;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//public class ThreadPoolExecutorTest {
// public static void main(String[] args) {
//  ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
//  for (int i = 0; i < 10; i++) {
//   final int index = i;
//   singleThreadExecutor.execute(new Runnable() {
//    public void run() {
//     try {
//      System.out.println(index);
//      Thread.sleep(2000);
//     } catch (InterruptedException e) {
//      e.printStackTrace();
//     }
//    }
//   });
//  }
// }
//}
//复制代码
//
//
//5.4 newScheduleThreadPool
//创建一个定长的线程池，而且支持定时的以及周期性的任务执行，支持定时及周期性任务执行。
//
//延迟3秒执行，延迟执行示例代码如下：
//
//
//
//复制代码
//package test;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//public class ThreadPoolExecutorTest {
// public static void main(String[] args) {
//  ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
//  scheduledThreadPool.schedule(new Runnable() {
//   public void run() {
//    System.out.println("delay 3 seconds");
//   }
//  }, 3, TimeUnit.SECONDS);
// }
//}
//复制代码
//
//
//表示延迟1秒后每3秒执行一次，定期执行示例代码如下：
//
//
//
//复制代码
//package test;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//public class ThreadPoolExecutorTest {
// public static void main(String[] args) {
//  ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
//  scheduledThreadPool.scheduleAtFixedRate(new Runnable() {
//   public void run() {
//    System.out.println("delay 1 seconds, and excute every 3 seconds");
//   }
//  }, 1, 3, TimeUnit.SECONDS);
// }
//}
