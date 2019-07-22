package com.huang.notetool.frame.method;

import com.huang.notetool.frame.start.Note;
import com.huang.notetool.po.Method;
import com.huang.notetool.service.MethodService;
import com.huang.notetool.util.InitialComponent;
import com.huang.notetool.util.LimitKeyIn;
import com.huang.notetool.util.StringUtils;
import com.huang.notetool.util.ToolUtil;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * 本实例代码可以看到，JDialog窗体和JFrame窗体形式基本相同，甚至在设置窗体的特性
 * 时调用的方法名称都基本相同，如设置窗体的大小，设置窗体的关闭状态等
 * 搜索方法
 *
 * @author 黄
 */
public class SearchMethodFrame extends JDialog {
    /**
     * 输入框
     */
    private JTextField msgJFiled;
    /**
     * 类型选择框
     */
    private JComboBox typeSelectBox;
    /**
     * 关键字和内容组成的
     */
    private String msgKeyword;
    /**
     * 类型
     */
    private String searchType;
    /**
     * service
     */
    private MethodService methodService;
    /**
     * 日志
     */
    private static Logger logger = Logger.getLogger(SearchMethodFrame.class);
    /**
     * 返回信息
     */
    public static String returnMsg;

    /**
     * 搜索框
     *
     * @param noteFrame 主面板
     */
    public SearchMethodFrame(Note noteFrame) {
        //实例化一个JDialog类对象，指定对话框的父窗体，窗体标题，和类型
        //super((Dialog) null, "查询规范 ：查询内容_关键字,关键字。。");
        //非Windows主题才能设置透明度
        if (!InitialComponent.isWindowsLookAndFeel()) {
            this.setUndecorated(true);
            this.getRootPane().setWindowDecorationStyle(JRootPane.WHEN_IN_FOCUSED_WINDOW);
            this.setOpacity(noteFrame.getOpacity());
        }
        this.setCursor(noteFrame.getCursor());
        this.setTitle("查询规范 ：查询内容_关键字,关键字。。");
        //创建一个容器
        Container container = this.getContentPane();
        container.setLayout(new GridLayout(1, 1));
        this.setModal(true);
        this.setResizable(false);
        Point point = noteFrame.getLocation();
        //居中显示
        setBounds(point.x + noteFrame.getWidth() / 2 - ToolUtil.getSearchMethodFramePoint.x / 2, point.y + noteFrame.getHeight() / 2 - ToolUtil.getSearchMethodFramePoint.y / 2, ToolUtil.getSearchMethodFramePoint.x, ToolUtil.getSearchMethodFramePoint.y);
        initial(noteFrame);
    }

    /**
     * 初始化控件
     *
     * @param noteFrame 主面板
     */
    private void initial(Note noteFrame) {
        logger.info("初始化搜索面板 ...");
        methodService = new MethodService();
        JLabel noteJLabel = new JLabel("        ");
        noteJLabel.setBackground(Color.gray);
        msgJFiled = new JTextField("", 60);
        typeSelectBox = new JComboBox(ToolUtil.methodType);
        //搜索按钮
        JButton searchBtn = new JButton("找找看");
        //重置按钮
        JButton resetBtn = new JButton("填错了");
        JButton cancelBtn = new JButton("算了吧");
        JButton searchDetailBtn = new JButton("还不够");

        Box msgBox = new Box(BoxLayout.X_AXIS);
        msgBox.add(typeSelectBox);
        msgBox.add(msgJFiled);

        JPanel btnJPanel = new JPanel();
        btnJPanel.add(searchBtn);
        btnJPanel.add(resetBtn);
        btnJPanel.add(searchDetailBtn);
        btnJPanel.add(cancelBtn);

        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.add(noteJLabel, "North");

        //在容器中添加标签
        jPanel.add(msgBox, "Center");
        jPanel.add(btnJPanel, "South");
        this.add(jPanel);

        jPanel.setBorder(BorderFactory.createLoweredBevelBorder());

        // 返回按钮
        cancelBtn.addActionListener(cancelBtnAction());
        // 重置按钮
        resetBtn.addActionListener(resetBtnAction());
        // 高级搜索
        searchDetailBtn.addActionListener(searchDetailAction(noteFrame));
        //搜索
        searchBtn.addActionListener(searchAction());
        //限制输入；
        msgJFiled.addKeyListener(new LimitKeyIn());
    }

    /**
     * 搜索
     *
     * @return
     */
    private ActionListener searchAction() {
        return e -> {
            msgKeyword = msgJFiled.getText().trim();
            searchType = (String) typeSelectBox.getSelectedItem();
            if (StringUtils.isEmpty(msgKeyword)) {
                JOptionPane.showMessageDialog(null, "没有输入怎么查");
                logger.info("搜索框里面的内容太少，不能查找 ");
            }
            //JScrollPane js = new JScrollPane(answerTextArea);
            //this.add(js,"Center");这就是一个星期没有解决的问题所在，注释掉就不会占用面板了
            logger.info("搜索框里面的内容 [" + msgKeyword + "] 和类型 [" + searchType + "] ");
            if (StringUtils.isNotEmpty(msgKeyword)) {
                String[] content = {msgKeyword, searchType};
                logger.info("开始通过 [" + msgKeyword + "][" + searchType + "] 从数据库查找信息 ");
                List<Method> methodList = methodService.getMethodAnswer(content);
                if (null != methodList && !methodList.isEmpty()) {
                    logger.info("成功从数据库查找信息 ");
                    this.dispose();
                    returnMsg = generateMethodReturn(methodList);
                } else {
                    JOptionPane.showMessageDialog(null, "查不到呢，期待您的更新。。");
                    logger.info("通过 [" + msgKeyword + "][" + searchType + "] 从数据库未查找到信息 ");
                }
            }
        };
    }

    /**
     * 高级搜索
     *
     * @param noteFrame
     * @return
     */
    private ActionListener searchDetailAction(Note noteFrame) {
        return e -> {
            //TODO:add
            // this.dispose();
            //new SearchMethodDatilFrame(noteFrame).setVisible(true);
        };
    }

    /**
     * 重置按钮
     *
     * @return
     */
    private ActionListener resetBtnAction() {
        return e -> {
            msgJFiled.setText("");
            typeSelectBox.setSelectedIndex(0);
        };
    }

    /**
     * 返回按钮
     *
     * @return
     */
    private ActionListener cancelBtnAction() {
        return e -> {
            logger.info("取消查询 ");
            this.dispose();
        };
    }

    /**
     * 提供其他类获取数据
     *
     * @return 方法字符串
     */
    public String getSearchMsg() {
        return returnMsg;
    }

    /**
     * 将查询到的数据显示到输入框
     *
     * @param methodList 方法列表
     * @return 字符串
     */
    private String generateMethodReturn(List<Method> methodList) {
        StringBuilder returnValue = new StringBuilder();
        //换行
        String line = System.getProperty("line.separator");
        for (Method method : methodList) {
            if (null != method) {
                returnValue.append("方法名字:").append(method.getName()).append(line);
                returnValue.append("方法类型:").append(method.getType()).append(line);
                returnValue.append("方法答案:").append(method.getAnswer()).append(line);
                if (StringUtils.isNotEmpty(method.getDescription())) {
                    returnValue.append("方法描述:").append(method.getDescription().replaceAll("[\n\t\r]", "")).append(line);
                }
                returnValue.append(line).append(line).append(line).append(line);
            }
        }
        return returnValue.toString();
    }
}
