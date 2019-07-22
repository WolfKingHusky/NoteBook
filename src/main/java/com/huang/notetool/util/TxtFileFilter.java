package com.huang.notetool.util;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * 继承文件拦截器FileFilter
 * 实现对文件的限制访问
 * 在jFileChooser可以使用他来指定打开文件的类型
 *
 * @author 黄显示
 * @date 2019-05-05
 */
public class TxtFileFilter extends FileFilter {
    /**
     * 可接收的文件类型
     *
     * @param file 文件
     * @return true or false
     */
    @Override
    public boolean accept(File file) {
        String fileName = file.getName();
        //仅显示目录和 [*.txt;*.TXT;*.xls;*.xlsx] 文件
        return file.isDirectory()
                || fileName.toLowerCase().endsWith(".txt")
                || fileName.toLowerCase().endsWith(".xls") || fileName.toLowerCase().endsWith(".xlsx");
    }

    /**
     * 描述
     *
     * @return 描述
     */
    @Override
    public String getDescription() {
        //需要的文件类型
        return "*.txt;*.TXT;*.xls;*.xlsx";
    }
}
