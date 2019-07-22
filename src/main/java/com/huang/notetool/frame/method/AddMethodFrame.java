package com.huang.notetool.frame.method;

import com.huang.notetool.frame.start.Note;
import com.huang.notetool.service.MethodService;
import com.huang.notetool.util.*;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * 添加方法
 *
 * @author huang
 */
public class AddMethodFrame extends JDialog {
    /**
     * service
     */
    protected MethodService methodService;
    /**
     * 方法名字
     */
    protected JTextField nameJField;
    /**
     * 方法类型
     */
    protected JComboBox mTypeBox;
    /**
     * 方法采用的语言
     */
    protected JComboBox mLanguageBox;
    /**
     * 关键字
     */
    protected JTextField keyWordJFiled;
    /**
     * 具体方法
     */
    protected JTextField mAnswerFiled;
    /**
     * 描述
     */
    protected JTextArea descriptionArea;
    /**
     * 存放按钮的面板
     */
    protected JPanel btnPanel;
    /**
     * 日志
     */
    private static Logger logger = Logger.getLogger(AddMethodFrame.class);

    public AddMethodFrame(Note answerFrame) {
        //非Windows主题才能设置透明度
        if (!InitialComponent.isWindowsLookAndFeel()) {
            this.setUndecorated(true);
            this.getRootPane().setWindowDecorationStyle(JRootPane.WHEN_IN_FOCUSED_WINDOW);
            this.setOpacity(answerFrame.getOpacity());
        }
        this.setCursor(answerFrame.getCursor());
        this.setTitle("注意：方法名字写完回车,多个关键字以英文,隔开");
        //容器
        Container container = this.getContentPane();
        container.setLayout(new GridLayout(1, 1));
        //model 设置为false，在弹出此窗口还可以点击主窗口
        this.setModal(false);
        //可以改变大小
        this.setResizable(true);
        Point point = answerFrame.getLocation();
        setBounds(point.x + answerFrame.getWidth() / 2 - ToolUtil.getAddMethodFramePoint.x / 2, point.y + answerFrame.getHeight() / 2 - ToolUtil.getAddMethodFramePoint.y / 2, ToolUtil.getAddMethodFramePoint.x, ToolUtil.getAddMethodFramePoint.y);
        initial();
    }

    /**
     * 初始化控件和监听事件
     */
    private void initial() {
        logger.info("添加方法页面启动成功  ");
        //实例化
        methodService = new MethodService();
        //方法名字
        JLabel nameJLabel = new JLabel("名    字 ：");
        //方法类型
        JLabel typeJLabel = new JLabel("类    型 ：");
        //方法采用的语言
        JLabel languageJLabel = new JLabel("语    言 ：");
        //关键字
        JLabel keyWordJLabel = new JLabel("关 键 字：");
        //具体方法
        JLabel answerJLabel = new JLabel("答    案 ：");
        JLabel descriptionJLabel = new JLabel("描    述 ：");
        nameJField = InitialComponent.setJTExtField(null, null, true);
        mTypeBox = new JComboBox(ToolUtil.methodType);
        mLanguageBox = new JComboBox(ToolUtil.methodLanguage);
        keyWordJFiled = InitialComponent.setJTExtField(null, null, true);
        mAnswerFiled = InitialComponent.setJTExtField(null, null, true);
        descriptionArea = InitialComponent.setJTextArea(null, null);

        descriptionArea.addKeyListener(new LimitKeyIn());
        nameJField.addKeyListener(new LimitKeyIn());
        keyWordJFiled.addKeyListener(new LimitKeyIn());
        mAnswerFiled.addKeyListener(new LimitKeyIn());

        JScrollPane jSDescription = new JScrollPane(descriptionArea);

        Box nameBox = getBox(nameJLabel, nameJField);

        Box typeBox = new Box(BoxLayout.X_AXIS);
        typeBox.add(typeJLabel);
        typeBox.add(mTypeBox);

        Box languageBox = new Box(BoxLayout.X_AXIS);
        languageBox.add(languageJLabel);
        languageBox.add(mLanguageBox);

        Box keyWordBox = getBox(keyWordJLabel, keyWordJFiled);

        Box answerBox = getBox(answerJLabel, mAnswerFiled);

        Box descriptionBox = new Box(BoxLayout.X_AXIS);
        descriptionBox.add(descriptionJLabel);
        descriptionBox.add(jSDescription);

        JButton addBtn = new JButton("添加新成员");
        JButton resetBtn = new JButton("写错了");
        JButton cancelBtn = new JButton("算了吧");

        btnPanel = new JPanel();
        btnPanel.add(addBtn);
        btnPanel.add(resetBtn);
        btnPanel.add(cancelBtn);

        JPanel msgPanel = new JPanel();
        msgPanel.setLayout(new BorderLayout());

        JPanel jTextFieldPanel = new JPanel();
        JPanel allTextPanel = new JPanel();

        jTextFieldPanel.setLayout(new GridLayout(5, 1));
        allTextPanel.setLayout(new GridLayout(2, 1));

        jTextFieldPanel.add(nameBox);
        jTextFieldPanel.add(languageBox);
        jTextFieldPanel.add(typeBox);
        jTextFieldPanel.add(answerBox);
        jTextFieldPanel.add(keyWordBox);

        allTextPanel.add(jTextFieldPanel);
        allTextPanel.add(descriptionBox);
        msgPanel.add(allTextPanel, BorderLayout.CENTER);
        msgPanel.add(btnPanel, BorderLayout.SOUTH);

        /**线边框
         //setBorder(BorderFactory.createLineBorder(Color.red, 3));
         //蚀刻边框
         //setBorder(BorderFactory.createEtchedBorder());
         //斜面边框(凸)
         // setBorder(BorderFactory.createRaisedBevelBorder());
         //斜面边框(凹)
         // setBorder(BorderFactory.createLoweredBevelBorder());
         //标题边框
         //setBorder(BorderFactory.createTitledBorder("标题"));
         //标签边框(右)
         //TitledBorder tb = BorderFactory.createTitledBorder("标题");
         //tb.setTitleJustification(TitledBorder.RIGHT);
         //setBorder(tb);
         //花色边框
         // setBorder(BorderFactory.createMatteBorder(1, 5, 1, 1, Color.yellow));
         //组合边框
         //Border b1 = BorderFactory.createLineBorder(Color.blue, 2);
         //Border b2 = BorderFactory.createEtchedBorder();
         //setBorder(BorderFactory.createCompoundBorder(b1, b2));**/

        msgPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        this.add(msgPanel);
        //取消添加
        cancelBtn.addActionListener(cancelBtnActionListener());
        //重置
        resetBtn.addActionListener(resetBtnActionListener());
        //开始添加方法
        addBtn.addActionListener(addBtnActionListener());

    }

    /**
     * 获取Box布局
     *
     * @param nameJLabel JLabel
     * @param jTextField JTextField
     * @return Box
     */
    private Box getBox(JLabel nameJLabel, JTextField jTextField) {
        Box nameBox = new Box(BoxLayout.X_AXIS);
        nameBox.add(nameJLabel);
        nameBox.add(jTextField);
        return nameBox;
    }

    /**
     * 开始添加方法
     *
     * @return ActionListener
     */
    private ActionListener addBtnActionListener() {
        return e -> {
            logger.info("开始添加方法 ... ");
            String name = nameJField.getText().trim();
            String type = (String) mTypeBox.getSelectedItem();
            String language = (String) mLanguageBox.getSelectedItem();
            String keyWord = keyWordJFiled.getText().trim();
            String answer = mAnswerFiled.getText().trim();
            String description = descriptionArea.getText().trim();
            if (StringUtils.isEmpty(name)) {
                logger.info("添加方法的方法名字为空");
                JOptionPane.showMessageDialog(null, "名字不能为空");
//                获取JFrame焦点
                nameJField.requestFocus();
            } else if (StringUtils.isEmpty(keyWord)) {
                logger.info("添加方法的关键字为空");
                JOptionPane.showMessageDialog(null, "关键字不能为空");
//                获取JFrame焦点
                keyWordJFiled.requestFocus();
            } else if (StringUtils.isEmpty(answer)) {
                logger.info("添加方法的答案为空");
                JOptionPane.showMessageDialog(null, "答案不能为空");
                //获取JFrame焦点
                mAnswerFiled.requestFocus();
            } else {
                logger.info("开始添加方法 ");
                if (!methodService.addMethod(name, type, language, keyWord, answer, description)) {
                    logger.info("添加方法 [" + nameJField.getText().trim() + "] 失败 ");
                    JOptionPane.showMessageDialog(null, "添加方法失败");
                } else {
                    logger.info("添加方法 [" + nameJField.getText().trim() + "] 成功");
                    JOptionPane.showMessageDialog(null, "添加方法成功");
                    resetWay();
                }
            }
        }
                ;
    }

    /**
     * 重置
     *
     * @return ActionListener
     */
    private ActionListener resetBtnActionListener() {
        return e -> resetWay();
    }

    private void resetWay() {
        logger.info("重置添加内容 ");
        nameJField.setText(Constants.EMPTY_STR);
        mTypeBox.setSelectedIndex(0);
        mLanguageBox.setSelectedIndex(0);
        keyWordJFiled.setText(Constants.EMPTY_STR);
        mAnswerFiled.setText(Constants.EMPTY_STR);
        descriptionArea.setText(Constants.EMPTY_STR);
    }

    /**
     * 取消添加
     *
     * @return ActionListener
     */
    private ActionListener cancelBtnActionListener() {
        return e -> {
            logger.info("取消添加方法  ");
            this.dispose();
        };
    }
}
