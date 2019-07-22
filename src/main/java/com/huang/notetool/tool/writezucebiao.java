package com.huang.notetool.tool;

import javax.swing.*;
import java.io.*;


public class writezucebiao extends JFrame {
    String ret;
    BufferedReader in;
    FileReader read;
    BufferedWriter out;
    FileWriter writer;//读取和写入文件

    public writezucebiao(String sth) {
        // TODO Auto-generated constructor stub
        String fpath = "f:\\自己的记事本\\配置文件\\pz.dll";
        writezucebiao(fpath, sth);

    }

    private void writezucebiao(String fpath, String sth) {
        // TODO Auto-generated method stub
        File file = new File(fpath);
        try {
            writer = new FileWriter(file);
            out = new BufferedWriter(writer);
            out.write(sth);
            out.close();
            writer.close();
        } catch (IOException e1) {
            JOptionPane.showMessageDialog(null, "File wrong");
        }

    }

}
