package com.huang.notetool.frame.exception;

import com.huang.notetool.frame.start.Note;
import com.huang.notetool.service.ExceptionService;
import com.huang.notetool.util.InitialComponent;
import com.huang.notetool.util.StringUtils;
import com.huang.notetool.util.ToolUtil;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * 更新异常信息
 *
 * @author 黄
 */
public class UpdateExceptionFrame extends AddExceptionFrame {
    /**
     * 返回给主面板的信息
     */
    private String returnMsg;
    private ExceptionService exceptionService;
    private static Logger logger = Logger.getLogger(UpdateExceptionFrame.class);

    /**
     * 构造方法
     *
     * @param updateStr 需要更新的数据字符串
     * @param noteFrame 面板
     */
    public UpdateExceptionFrame(String updateStr, Note noteFrame) {
        super(noteFrame);
        //非Windows主题才能设置透明度
        if (!InitialComponent.isWindowsLookAndFeel()) {
            this.setUndecorated(true);
            this.getRootPane().setWindowDecorationStyle(JRootPane.WHEN_IN_FOCUSED_WINDOW);
            this.setOpacity(noteFrame.getOpacity());
        }
        this.setCursor(noteFrame.getCursor());
        logger.info("进入更新异常信息页面... ");
        setTitle("更新方法,不能有;");
        this.setResizable(true);
        Point point = ToolUtil.getExceptionFramePoint();
        Double x = noteFrame.getX() + noteFrame.getWidth() / 2 - point.getX() / 2 + 25;
        Double y = noteFrame.getY() + noteFrame.getHeight() / 2 - point.getY() / 2 + 25;
        this.setBounds(x.intValue(), y.intValue(), point.x - 50, point.y - 50);
        initial(updateStr);
        this.setDefaultCloseOperation(closeOperation());
    }

    private int closeOperation() {
        logger.info("离开修改页面 ");
        this.dispose();
        return DISPOSE_ON_CLOSE;
    }

    /**
     * 初始化控件
     *
     * @param updateStr 需要更新的数据字符串
     */
    private void initial(String updateStr) {
        exceptionService = new ExceptionService();
        String[] updateMsg = updateStr.split("，");
        logger.info("修改方法页面初始化成功 数据 [" + updateStr.length() + "] ");
        //设置异常信息类型,返回类型坐标
        int selectTypeIndex = setSelectExceptionType(updateMsg[3]);

        int selectLanguageIndex = setExceptionLanguageSelect(updateMsg[2]);
        //设置初始数据
        initialUpdateMsg(updateMsg);

        JButton updateBtn = new JButton("更新方法");
        JButton resetBtn = new JButton("写错了");
        JButton cancelBtn = new JButton("算了吧");
        JButton delBtn = new JButton("不想要了");
        JButton explainBtn = new JButton("怎么填嘛");

        superBtnPanel.removeAll();
        superBtnPanel.add(updateBtn);
        superBtnPanel.add(resetBtn);
        superBtnPanel.add(delBtn);
        superBtnPanel.add(explainBtn);
        superBtnPanel.add(cancelBtn);

        superBtnPanel.setBorder(BorderFactory.createEtchedBorder());

        cancelBtn.addActionListener(e -> closeOperation());
        //重置
        resetBtn.addActionListener(resetAction(selectTypeIndex, selectLanguageIndex, updateMsg));

        updateBtn.addActionListener(updateAction(updateMsg));

        delBtn.addActionListener(delBtnAction(updateMsg));

        explainBtn.addActionListener(explainActionListener());
    }

    /**
     * 设置初始数据
     *
     * @param updateMsg
     */
    private void initialUpdateMsg(String[] updateMsg) {
        exceptionMsg.setText(updateMsg[1]);

        exceptionAnswerMsg.setText(updateMsg[4]);

        if (StringUtils.isNotEmpty(updateMsg[6])) {
            exceptionCause.setText(updateMsg[6]);
        }
        if (StringUtils.isNotEmpty(updateMsg[5])) {
            keyFiled.setText(updateMsg[5]);
        }
    }

    /**
     * 说明
     *
     * @return
     */
    private ActionListener explainActionListener() {
        return e -> JOptionPane.showMessageDialog(UpdateExceptionFrame.this, ToolUtil.getHowToFillAddExceptionMsg);
    }

    /**
     * 删除信息
     *
     * @param updateMsg 更新信息
     * @return ActionListener
     */
    private ActionListener delBtnAction(String[] updateMsg) {
        return e -> {
            int isDelMsg = JOptionPane.showConfirmDialog(UpdateExceptionFrame.this, "f");
            if (isDelMsg == 0) {
                logger.info("已确认删除 ");
                String id = updateMsg[0];
                if (null != id && !"".equals(id)) {
                    if (exceptionService.deleteById(id)) {
                        logger.info("成功删除 [" + updateMsg[1] + "] ");
                        returnMsg = "删除成功";
                        JOptionPane.showMessageDialog(UpdateExceptionFrame.this, "删除成功");
                        closeOperation();
                    } else {
                        logger.info("删除 [" + updateMsg[1] + "] 失败 ");
                        JOptionPane.showMessageDialog(UpdateExceptionFrame.this, "删除失败");
                    }
                } else {
                    logger.info("删除 失败 ,ID丢失");
                    JOptionPane.showMessageDialog(UpdateExceptionFrame.this, "ID丢失");
                }
            }
        };
    }

    /**
     * 更新
     *
     * @param updateMsg 更新信息
     * @return
     */
    private ActionListener updateAction(String[] updateMsg) {
        return e -> {

            String selectType = (String) exceptionTypeList.getSelectedValue();
            String selectLanguage = (String) exceptionLanguageList.getSelectedItem();
            String exceptionMsgStr = exceptionMsg.getText().trim();
            String exceptionAnswerMsgStr = exceptionAnswerMsg.getText().trim();
            String exceptionCauseStr = exceptionCause.getText().trim();
            String keyWordStr = keyFiled.getText().trim();

            if (checkInputMsg(exceptionMsgStr, selectLanguage, selectType, keyWordStr, exceptionAnswerMsgStr, exceptionCauseStr, updateMsg)) {
                boolean result = exceptionService.updateException(exceptionMsgStr, selectLanguage, selectType, keyWordStr, exceptionAnswerMsgStr, exceptionCauseStr, updateMsg[0]);
                if (result) {
                    returnMsg = "更新成功";
                    logger.info(returnMsg);
                    JOptionPane.showMessageDialog(UpdateExceptionFrame.this, returnMsg);
                    if (null != returnMsg && !"".equals(returnMsg)) {
                        closeOperation();
                    }
                } else {
                    returnMsg = "更新失败";
                    JOptionPane.showMessageDialog(UpdateExceptionFrame.this, "更新失败");
                }
            }
        };
    }

    private ActionListener resetAction(int selectTypeIndex, int selectLanguageIndex, String[] updateMsg) {
        return e -> {
            logger.info("重置修改页面信息 ");
            exceptionTypeList.setSelectedIndex(selectTypeIndex);
            exceptionLanguageList.setSelectedIndex(selectLanguageIndex);
            initialUpdateMsg(updateMsg);
        };
    }

    /**
     * 设置异常语言类型
     *
     * @param updateLanguage
     * @return
     */
    private int setExceptionLanguageSelect(String updateLanguage) {
        String[] languageList = ToolUtil.exceptionLanguage;
        int languageSelecetindex = languageList.length - 1;
        for (int num = 0; num < languageList.length; num++) {
            if (languageList[num].equals(updateLanguage)) {
                languageSelecetindex = num;
                break;
            }
        }
        exceptionLanguageList.setSelectedIndex(languageSelecetindex);
        return languageSelecetindex;
    }

    /**
     * 设置异常信息类型
     *
     * @param msgType 更新信息的类型
     */
    private int setSelectExceptionType(String msgType) {
        //类型
        String[] exceptionType = ToolUtil.exceptionType;
        //默认是最后一个未知
        int typeSelectIndex = exceptionType.length - 1;
        for (int num = 0; num < exceptionType.length; num++) {
            if (exceptionType[num].equals(msgType)) {
                typeSelectIndex = num;
                break;
            }
        }
        exceptionTypeList.setSelectedIndex(typeSelectIndex);
        return typeSelectIndex;
    }

    private boolean checkInputMsg(String name, String language, String type, String keyword, String answer, String description, String[] msg) {
        if (name.equals(msg[1]) && language.equals(msg[2]) && type.equals(msg[3]) && keyword.equals(msg[5].replaceAll("，", ",")) && answer.equals(msg[4]) && description.equals(msg[8].replaceAll("，", ","))) {
            JOptionPane.showMessageDialog(UpdateExceptionFrame.this, "信息没有改变");
            return false;
        } else if (StringUtils.isEmpty(name)) {
            JOptionPane.showMessageDialog(UpdateExceptionFrame.this, "异常信息不能为空");
            exceptionMsg.requestFocus();
            return false;
        } else if (StringUtils.isEmpty(keyword)) {
            JOptionPane.showMessageDialog(UpdateExceptionFrame.this, "关键字不能为空");
            keyFiled.requestFocus();
            return false;
        } else if (StringUtils.isEmpty(answer)) {
            JOptionPane.showMessageDialog(UpdateExceptionFrame.this, "答案不能为空");
            exceptionAnswerMsg.requestFocus();
            return false;
        } else if (StringUtils.isEmpty(msg[0])) {
            JOptionPane.showMessageDialog(UpdateExceptionFrame.this, "ID丢失，请重新进入此页面");
            returnMsg = null;
            this.dispose();
            return false;
        } else {
            logger.info("准备修改 ");
            return true;
        }
    }

    /**
     * 给第三方的返回信息
     *
     * @return 提示信息
     */
    public String getUpdateMsg() {
        return returnMsg;
    }
}
