package com.huang.notetool.quote;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

public class dingshiqi implements Runnable {
    static Process process = null;
    private Thread thread;
    Calendar gamestart = Calendar.getInstance();
    long gamebagin = gamestart.getTimeInMillis();
    String limitname = JOptionPane.showInputDialog(null, "定时关闭程序名：", "关闭程序", JOptionPane.PLAIN_MESSAGE);
    String limittime = JOptionPane.showInputDialog(null, "定时关闭程序时间(s)：", "关闭程序", JOptionPane.PLAIN_MESSAGE);
    int gettime = 0;

    public dingshiqi() {
        if (limitname == null)
            limitname = "";
        if (limittime == null)
            limittime = "";
        for (int i = 0; i < limittime.length(); i++) {
            if (limittime.charAt(i) <= '9' && limittime.charAt(i) >= '0') {
                gettime = 0;
            } else {
                gettime = 2;
                break;
            }
        }
        if (gettime == 0)
            ccv();//设置窗口可视
        else {
            new dingshiqi();
            System.exit(0);
        }
		/*addKeyListener(new KeyAdapter(){
			 public void keyPressed(KeyEvent e) {
				 
			 } 
			 public void keyReleased(KeyEvent e) {
				 
			 } 
			 public void keyTyped(KeyEvent e) { 
				 if(e.getKeyChar()=='v'||e.getKeyChar()=='V'){ 
					 //add(cv,"Center");
				 }
				 } }); */
        if (thread == null || !thread.isAlive())
            thread = new Thread(this);
        thread.start();
    }

    private void ccv() {
        String getxchen = getxianv() + ".exe";
        //System.out.println(getxchen);
        Calendar now = Calendar.getInstance();
        long gamenow = now.getTimeInMillis();
        int jcku = 0;
        String[] jclist = new String[1000];

        int jisulist = 0, fenduant = 0;
        String hot = jcso();
        for (int i = 0; i < hot.length(); i++) {
            if (hot.charAt(i) == '\n') {
                jclist[jisulist++] = hot.substring(fenduant, i);
                fenduant = i + 1;
            }
        }
        jclist[jisulist + 1] = hot.substring(fenduant);

        String[] lastlist = new String[jisulist + 1];

        for (int k = 0; k <= jisulist; k++) {
            lastlist[k] = jclist[k];
        }
        if (lastlist.length != 0) {
            for (int k = 0; k < jisulist; k++) {
                if (lastlist[k].length() != 0 && lastlist[k].equalsIgnoreCase(getxchen)) {
                    jcku = 2;
                    break;
                }
                //else
                //jcku=0;
            }
        } else
            jcku = 0;
        if (jcku == 2) {
            long limttt = Long.parseLong(limittime);
            if ((gamenow - gamebagin) >= limttt * 1000) {//秒钟
                closeit();
            }
        } else {
            JOptionPane.showMessageDialog(null, "程序已关闭", "error", JOptionPane.CANCEL_OPTION);  //显示消息
            System.exit(0);
        }
    }

    private String getxianv() {
        //String limtn="";
        if (limitname.length() != 0) {
            if (limitname.equalsIgnoreCase("酷狗") || limitname.equalsIgnoreCase("酷狗音乐") || limitname.equalsIgnoreCase("KuGou") || limitname.equalsIgnoreCase("KGmusic")) {
                limitname = "KuGou";
            } else if (limitname.equalsIgnoreCase("lol") || limitname.equalsIgnoreCase("英雄联盟") || limitname.equalsIgnoreCase("Client")) {
                limitname = "Client";
            } else if (limitname.equalsIgnoreCase("乐视") || limitname.equalsIgnoreCase("乐视TV") || limitname.equalsIgnoreCase("乐视视频") || limitname.equalsIgnoreCase("LeTVLoader") || limitname.equalsIgnoreCase("Letv")) {
                limitname = "LeTVLoader";
            } else if (limitname.equalsIgnoreCase("vc++") || limitname.equalsIgnoreCase("c/c++编译器") || limitname.equalsIgnoreCase("c++编译器") || limitname.equalsIgnoreCase("c编译器") || limitname.equalsIgnoreCase("MSDEV") || limitname.equalsIgnoreCase("Microsoft Visual C++")) {
                limitname = "MSDEV";
            } else if (limitname.equalsIgnoreCase("java") || limitname.equalsIgnoreCase("Java编译器") || limitname.equalsIgnoreCase("MyEclipse 10") || limitname.equalsIgnoreCase("MyEclipse") || limitname.equalsIgnoreCase("myeclipse") || limitname.equalsIgnoreCase("javaw")) {
                limitname = "javaw";
            } else if (limitname.equalsIgnoreCase("qq") || limitname.equalsIgnoreCase("腾讯qq") || limitname.equalsIgnoreCase("QQApp")) {
                limitname = "QQ";
            } else if (limitname.equalsIgnoreCase("搜狗") || limitname.equalsIgnoreCase("搜狗浏览器") || limitname.equalsIgnoreCase("搜狗高速浏览器") || limitname.equalsIgnoreCase("SogouExplorer")) {
                limitname = "SogouExplorer";
            } else if (limitname.equalsIgnoreCase("wps") || limitname.equalsIgnoreCase("wps文字")) {
                limitname = "wps";
            } else if (limitname.equalsIgnoreCase("et") || limitname.equalsIgnoreCase("wps表格") || limitname.equalsIgnoreCase("w表格")) {
                limitname = "et";
            } else if (limitname.equalsIgnoreCase("wps演示") || limitname.equalsIgnoreCase("wpp") || limitname.equalsIgnoreCase("ppt")) {
                limitname = "wpp";
            } else if (limitname.equalsIgnoreCase("迅雷") || limitname.equalsIgnoreCase("迅雷下载") || limitname.equalsIgnoreCase("迅雷极速版") || limitname.equalsIgnoreCase("Thunder")) {
                limitname = "Thunder";
            } else {
                JOptionPane.showMessageDialog(null, "还未收录，敬请期待\n可将问题反馈给：\n\tQQ:1976688792", "error", JOptionPane.CANCEL_OPTION);  //显示消息
                new dingshiqi();
                System.exit(0);
            }
        }
        return limitname;
    }


    private void closeit() {
        String ggft = "";
        if (limitname.length() != 0) {
            ggft = getxianv();
            if (ggft.equalsIgnoreCase("javaw")) {
                try {
                    Runtime.getRuntime().exec("taskkill /F /IM myeclipse.exe");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            try {
                Runtime.getRuntime().exec("taskkill /F /IM " + limitname + ".exe");
            } catch (IOException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(null, "关闭程序失败", "error", JOptionPane.CANCEL_OPTION);  //显示消息
            }
        } else {
            new dingshiqi();
            System.exit(0);
        }

    }

    public String jcso() {
        String hot = "";
        try {
            process = Runtime.getRuntime().exec("cmd.exe   /c   tasklist");
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = " ";
            while ((line = input.readLine()) != null) {
                String hget = line;
                if (hget.indexOf(".exe") + 1 == 0)
                    ;
                else
                    hot += hget.substring(0, hget.indexOf(".exe") + 1) + "exe" + "\n";
                //hot+=hget+"\n";

            }
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "not found", "提示", JOptionPane.NO_OPTION);  //显示消息
        }
        return hot;
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error", "error", JOptionPane.ERROR_MESSAGE);  //显示消息
                System.exit(0);
            }
            ccv();
        }
    }

    //public static void main(String[] args){
    //new dingshiqi();
    //}

}
