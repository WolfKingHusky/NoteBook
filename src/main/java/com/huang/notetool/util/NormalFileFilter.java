package com.huang.notetool.util;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * 继承文件拦截器FileFilter
 * 实现对文件的限制访问
 * 在jFileChooser可以使用他来指定打开文件的类型
 *
 * @author 黄先生
 * @date 2019-05-05
 */
public class NormalFileFilter extends FileFilter {
    /**
     * 可接收的文件类型
     *
     * @param file 文件
     * @return true or false
     */
    @Override
    public boolean accept(File file) {
        String fileName = file.getName();
        //仅显示目录和txt、log、sql、bat、json、tmp、exc、xml、html、dll、cmd、sh
        return file.isDirectory()
                || fileName.toLowerCase().endsWith(".txt")
                || fileName.toLowerCase().endsWith(".log")
                || fileName.toLowerCase().endsWith(".sql")
                || fileName.toLowerCase().endsWith(".json")
                || fileName.toLowerCase().endsWith(".tmp")
                || fileName.toLowerCase().endsWith(".exc")
                || fileName.toLowerCase().endsWith(".xml")
                || fileName.toLowerCase().endsWith(".html")
                || fileName.toLowerCase().endsWith(".dll")
                || fileName.toLowerCase().endsWith(".bat")
                || fileName.toLowerCase().endsWith(".cmd")
                || fileName.toLowerCase().endsWith(".sh")
                || fileName.toLowerCase().endsWith(".conf")
                || fileName.toLowerCase().endsWith(".properties")
                || fileName.toLowerCase().endsWith(".yml")
                || fileName.toLowerCase().endsWith(".idea")
                || fileName.toLowerCase().endsWith(".iml")
                || fileName.toLowerCase().endsWith(".md")
                || fileName.toLowerCase().endsWith(".gitignore")
                || fileName.toLowerCase().endsWith(".exe4j");
    }

    @Override
    public String getDescription() {
        // 需要的文件类型
        return "*.normalFile;";
    }

    /**
     * 根据类型判断文件是否为图片
     *
     * @param type 文件类型
     * @return 是否
     */
    public static boolean isaPicture(String type) {
        //bmp,jpg,png,tif,gif,pcx,tga,exif,fpx,
        // svg,psd,cdr,pcd,dxf,ufo,eps,ai,raw,WMF,webp
        return type.contains("png")
                || type.contains("svg")
                || type.contains("raw")
                || type.contains("ai")
                || type.contains("eps")
                || type.contains("ufo")
                || type.contains("psd")
                || type.contains("cdr")
                || type.contains("pcd")
                || type.contains("dxf")
                || type.contains("jpg")
                || type.contains("bmp")
                || type.contains("tif")
                || type.contains("pcx")
                || type.contains("tga")
                || type.contains("fpx")
                || type.contains("exif")
                || type.contains("gif")
                || type.contains("icon");
    }
}
