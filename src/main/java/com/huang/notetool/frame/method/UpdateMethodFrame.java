package com.huang.notetool.frame.method;

import com.huang.notetool.frame.start.Note;
import com.huang.notetool.util.InitialComponent;
import com.huang.notetool.util.StringUtils;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * 更新方法
 *
 * @author huang
 */
public class UpdateMethodFrame extends AddMethodFrame {

    private Logger logger = Logger.getLogger(UpdateMethodFrame.class);
    /**
     * 是否更新成功
     */
    private boolean success;

    public UpdateMethodFrame(String selectValue, Note noteFrame) {
        super(noteFrame);
        //非Windows主题才能设置透明度
        if (!InitialComponent.isWindowsLookAndFeel()) {
            this.setUndecorated(true);
            this.getRootPane().setWindowDecorationStyle(JRootPane.WHEN_IN_FOCUSED_WINDOW);
            this.setOpacity(noteFrame.getOpacity());
        }
        this.setCursor(noteFrame.getCursor());
        this.setTitle("更新方法");
        logger.info("开始更新方法... ");
        //编号，方法名，语言，类型，方法使用，关键字，更新时间，方法描述，是否过时
        String[] selectMethodArr = selectValue.split("，");

        JButton updateBtn = new JButton("更新方法");
        JButton resetBtn = new JButton("写错了");
        JButton cancelBtn = new JButton("算了吧");
        JButton delBtn = new JButton("不想要了");

        btnPanel.removeAll();
        btnPanel.add(updateBtn);
        btnPanel.add(resetBtn);
        btnPanel.add(cancelBtn);
        btnPanel.add(delBtn);

        btnPanel.validate();
        btnPanel.repaint();

        cancelBtn.addActionListener(e -> dispose());

        resetBtn.addActionListener(resetAction(selectMethodArr));
        delBtn.addActionListener(delAction(selectMethodArr[0]));
        updateBtn.addActionListener(updateAction(selectMethodArr));

        initialInputMsg(selectMethodArr);
    }

    private ActionListener updateAction(String[] selectMethodArr) {
        return e -> {
            if (checkInputMsg(nameJField.getText().trim(), (String) mLanguageBox.getSelectedItem(), (String) mTypeBox.getSelectedItem(), keyWordJFiled.getText().trim(), mAnswerFiled.getText().trim(), descriptionArea.getText().trim(), selectMethodArr)) {
                boolean result = methodService.updateMethod(nameJField.getText().trim(), (String) mLanguageBox.getSelectedItem(), (String) mTypeBox.getSelectedItem(), keyWordJFiled.getText().trim(), mAnswerFiled.getText().trim(), descriptionArea.getText().trim(), selectMethodArr[0]);
                if (result) {
                    logger.info("更新 [" + selectMethodArr[0] + "] 成功");
                    success = true;
                    JOptionPane.showMessageDialog(null, "更新成功");
                    dispose();
                } else {
                    logger.info("更新 [" + selectMethodArr[0] + "] 失败 ");
                    success = false;
                    JOptionPane.showMessageDialog(null, "更新失败");
                }
            }
        };
    }

    private ActionListener delAction(String id) {
        return e -> {
            int isDelMsg = JOptionPane.showConfirmDialog(UpdateMethodFrame.this, "确定删除");
            if (isDelMsg == 0) {
                logger.info("已确认删除 --> UpdateMethodFrame");
                if (methodService.deleteById(id)) {
                    logger.info("成功删除 [" + id + "] ");
                    success = true;
                    JOptionPane.showMessageDialog(null, "删除成功");
                    dispose();
                } else {
                    logger.info("删除 [" + id + "] 失败 ");
                    success = false;
                    JOptionPane.showMessageDialog(null, "删除失败");
                }
            }
        };
    }

    public ActionListener resetAction(String[] selectMethodArr) {
        return e -> {
            logger.info("重置修改页面信息 ");
            initialInputMsg(selectMethodArr);
        };
    }

    /**
     * 初始化信息
     *
     * @param selectMethodArr
     */
    private void initialInputMsg(String[] selectMethodArr) {
        logger.info("初始化信息显示 ");
        //编号，方法名，语言，类型，方法使用，关键字，更新时间，使用次数，方法描述，是否过时
        nameJField.setText(selectMethodArr[1]);
        keyWordJFiled.setText(selectMethodArr[5]);
        mAnswerFiled.setText(selectMethodArr[4]);
        descriptionArea.setText(null == selectMethodArr[8] ? "" : selectMethodArr[8]);
        mTypeBox.setSelectedItem(selectMethodArr[3] == null ? "未知" : selectMethodArr[3]);
        mLanguageBox.setSelectedItem(selectMethodArr[2] == null ? "未知" : selectMethodArr[2]);
    }

    /**
     * 更新方法
     *
     * @param name
     * @param language
     * @param type
     * @param keyword
     * @param answer
     * @param description
     * @param msg
     * @return
     */
    private boolean checkInputMsg(String name, String language, String type, String keyword, String answer, String description, String[] msg) {
        logger.info("更新前检查输入信息的正确性 ");
        //编号，方法名，语言，类型，方法使用，关键字，更新时间，方法描述，是否过时
        if (name.equals(msg[1]) && language.equals(msg[2]) && type.equals(msg[3]) && keyword.equals(msg[5]) && answer.equals(msg[4]) && description.equals(msg[8])) {
            JOptionPane.showMessageDialog(UpdateMethodFrame.this, "信息没有改变");
            return false;
        } else if (StringUtils.isEmpty(name)) {
            JOptionPane.showMessageDialog(UpdateMethodFrame.this, "名字不能为空");
            nameJField.requestFocus();
            return false;
        } else if (StringUtils.isEmpty(keyword)) {
            JOptionPane.showMessageDialog(UpdateMethodFrame.this, "关键字不能为空");
            keyWordJFiled.requestFocus();
            return false;
        } else if (StringUtils.isEmpty(answer)) {
            JOptionPane.showMessageDialog(UpdateMethodFrame.this, "答案不能为空");
            mAnswerFiled.requestFocus();
            return false;
        } else if (StringUtils.isEmpty(description)) {
            JOptionPane.showMessageDialog(UpdateMethodFrame.this, "描述不能为空");
            descriptionArea.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    /**
     * 获取成功与否
     *
     * @return true or false
     */
    public boolean isSuccess() {
        return success;
    }
}
