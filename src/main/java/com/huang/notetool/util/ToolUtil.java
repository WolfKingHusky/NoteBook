package com.huang.notetool.util;

import java.awt.*;

/**
 * 存放可以变动的常量信息
 *
 * @author 黄先生
 * @version 1.0
 * @Content 此处说明类的解释
 * @date 2019-05-22 22:03:12
 * @History 序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-05-22   创建项目           完成
 * @LeftoverProblem 此处说明遗留问题和潜在的问题
 */
public class ToolUtil {
    /**
     * 查询32位注册表信息QUERY
     */
    public static final String CMD_QUERY_REG_32_STR = "cmd /c reg query " +
            "HKEY_LOCAL_MACHINE\\SOFTWARE\\Wow6432Node\\Microsoft\\Windows\\CurrentVersion" +
            "\\Uninstall\\ && reg query HKEY_CURRENT_USER\\SOFTWARE\\Wow6432Node\\Microsoft" +
            "\\Windows\\CurrentVersion\\Uninstall\\";
    /**
     * 查询64位注册表信息QUERY
     */
    public static final String CMD_QUERY_REG_64_STR = "cmd /c reg query " +
            "HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\ " +
            "&& reg query HKEY_CURRENT_USER\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion" +
            "\\Uninstall\\" +
            "&& reg query HKLM\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\";
    /**
     * Linux命令权限
     */
    public static final String[] LINUX_PERMISSION = {"所有者", "Root用户", "管理员", "根据进程而定", "普通用户",
            "特殊用户", "其他"};
    /**
     * 关于我们 2.0.1
     */
    public static String aboutAsMsg = "本次更新：\n   1、加入工具和引用菜单颜色\n   2、优化响应时间\n   " +
            "3、引用和工具菜单内的弹窗支持双击换行切换，撤销和CTRL+B还原撤销\n   4、解决查询数据卡死问题和网络搜索\n   5、累了咋办菜单支持酷狗和优酷\n   " +
            "6、字数统计优化\n   7、加入部分快捷键\n   8、加入部分快捷键\n   9、工具菜单加入加解密和字符串转码\n   10、插入菜单增加系统安装软件查看\n  " +
            " 11、审阅菜单查看进程优化，2s更新一次\n   12、移除多余jar包\n关于我们：\n   遇到问题或者是BUG，敬请发送信息到邮箱\n      " +
            "1976688792@qq.com\n   期待您的加入";
    /**
     * 关于我们 2.0.2
     */
    public static String aboutAsMsg2 = "本次更新：\n   1、移除无用代码和包\n   2、优化响应时间\n   3、增加Linux命令大全工具\n  " +
            " 4、工具弹窗不限定，可以同时开启多个\n   5、字数统计优化\n   6、查看安装软件移除及时通讯\n   7、加入字符串转码工具\n   " +
            "8、加入查看进程可选着关闭，比打开任务管理器快\n   9、优化保存逻辑\n  关于我们：\n   遇到问题或者是BUG，敬请发送信息到邮箱\n      " +
            "1976688792@qq.com\n   期待您的加入";
    /**
     * 关于我们 2.0.3
     */
    public static String aboutAsMsg3 = "本次更新：\n   1、修复错误信息过长无法退出提示窗口\n   2、增加视图->窗体风格菜单，可变窗口风格\n " +
            "  3、日志优化\n   4、查看进程窗口融入不同风格\n   5、输入框融入不同风格\n   6、新增笔记菜单，进入图片笔记即可操作笔记信息和经粘贴图片\n   " +
            "7、窗体切换后，可点击左上角java图标设置窗体不同风格\n   8、支持快捷键CTRL+Z撤销 ，CTRL+B撤销还原 ，CTRL+R只读 ， CTRL+W可写\n " +
            "  9、导入的格式和导出的格式一样，目前只支持txt格式\n   关于我们：\n   遇到问题或者是BUG，敬请发送信息到邮箱\n      1976688792@qq" +
            ".com\n   期待您的加入";

    /**
     * 增加方法框初始化大小
     */
    public static Point getAddMethodFramePoint = new Point(500, 400);
    /**
     * 搜索方法框初始化大小
     */
    public static Point getSearchMethodFramePoint = new Point(400, 130);

    /**
     * 管理方法框初始化大小
     */
    public static Point getMethodMannerFramePoint() {
        return new Point(800, 500);
    }

    /**
     * 异常信息面板大小
     */
    public static Point getExceptionFramePoint() {
        return new Point(850, 500);
    }

    /**
     * 异常信息面板大小
     */
    public static Point getNoteJListPoint() {
        return new Point(180, 400);
    }

    /**
     * 添加异常信息的使用方法介绍
     */
    public static String getHowToFillAddExceptionMsg = "1、左边是Type\n2、中间上面是语言\n3、中间下面是关键字，格式：关键字," +
            "关键字..\n4、中间左边是异常信息\n5、中间右边是异常信息的答案\n5、右边是根本原因\n之间双击可以换行";
    /**
     * 默认异常语言
     */
    public static String[] exceptionLanguage = {"Java", "JavaScript", "Spring", "SpringBoot",
            "SpringCloud", "Mybatis", "Hibernate", "Jsf", "JSwing", "Ejb", "J2EE", "Jpa",
            "PrimeFaces", "Bootstrap", "DataBase", "Maven", "Git", "Shell", "RabbitMQ", "Tomcat",
            "Mirth",
            "GlassFish",
            "WebLogic", "Vue", "Python", "中间件", "其他", "未知"};
    /**
     * 异常信息默认类型
     */
    public static String[] exceptionType = {
            "Throwable", "Error", "Exception", "RuntimeException",
            //算术异常类
            "ArithmeticException",
            //空指针异常类
            "NullPointerException",
            //类型不存在异常
            "TypeNotPresentException",
            //线程问题
            "ThreadException",
            //非法
            "Illegal",
            //初始化异常
            "InitialException",
            //类相关异常或者错误
            "ClassError", "NoSuchMethodError",
            //操作数据库异常,AssertionError
            "SQLException","ListenerExecutionFailedException",
            "HttpMessageNotWritableException",
            //转换异常
            "FormatException", "TransferException", "InvocationException",
            //文件异常
            "IOException",
            //违背安全原则异常
            "SecurityException",
            //数组下标越界异常,NegativeArrayException,outOfBounds
            "ArrayException",
            //方法异常
            "MethodException", "DeploymentException",
            //类型强制转换异常
            "ClassCastException", "RunTimeException", "SerializationException",
            "NoClassDefFoundError",
            "AccessLocalException", "NoRouteToHostException", "BindingException",
            "InvocationTargetException",
            "自定义", "其他", "未知"
    };
    /**
     * 获取方法类型
     */
    public static String[] methodType = {
            "String", "JSwing", "Spring", "SpringBoot", "SpringCloud", "Boolean", "boolean",
            "Integer", "int", "Double", "double", "Long", "long", "Object", "Class", "Pojo",
            "Thread", "Void", "Component", "Idea", "Sql", "DB", "Invoke", "Xml", "Properties",
            "配置", "其他", "未知"
    };
    /**
     * 获取方法语言
     */
    public static String[] methodLanguage = {
            "Java", "MySql", "Oracle", "SqlServer", "Sqlite", "Hibernate", "Mybatis", "NoneDB",
            "PrimeFaces", "Html", "JavaScript", "C", "C++", "Python", "Bootstrap", "Vue", "Vb",
            "微机", "JavaScript", "Regex", "数据库", "其他", "未知"
    };
}
