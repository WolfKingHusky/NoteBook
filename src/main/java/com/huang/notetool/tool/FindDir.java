package com.huang.notetool.tool;

/**
 * 查找文件的项目路径
 *
 * @author 黄先生
 * @version 1.0
 * @Content 此处说明类的解释
 * @CreateDate 2019-05-27 09:28:33
 * @History 序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-05-27   创建项目           完成
 * @LeftoverProblem 此处说明遗留问题和潜在的问题
 */
public class FindDir {
    /**
     * 获取项目的所在路径
     *
     * @return 路径
     */
    public static String getProjectDir() {
        // 第一种：获取类加载的根路径   D:\git\daotie\daotie\target\classes
        // File f = new File(this.getClass().getResource("/").getPath());
        // System.out.println(f);

        // 获取当前类的所在工程路径; 如果不加“/”  获取当前类的加载目录  D:\git\daotie\daotie\target\classes\my
        // File f2 = new File(this.getClass().getResource("").getPath());
        // System.out.println(f2);

        // 第二种：获取项目路径    D:\git\daotie\daotie
        // File directory = new File("");// 参数为空
        // String courseFile = directory.getCanonicalPath();
        //  System.out.println(courseFile);


        // 第三种：  file:/D:/git/daotie/daotie/target/classes/
        // URL xmlpath = this.getClass().getClassLoader().getResource("");
        // System.out.println(xmlpath);


        // 第四种： D:\git\daotie\daotie
        //System.out.println(System.getProperty("user.dir"));
        /*
         * 结果： C:\Documents and Settings\Administrator\workspace\projectName
         * 获取当前工程路径
         */

        // 第五种：  获取所有的类路径 包括jar包的路径
        // System.out.println(System.getProperty("java.class.path"));
        //fileDir=fileDir.substring(0,fileDir.lastIndexOf(System.getProperty("file.separator")));
        return System.getProperty("user.dir");
    }
}
