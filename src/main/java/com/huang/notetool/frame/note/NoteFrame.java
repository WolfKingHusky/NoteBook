package com.huang.notetool.frame.note;

import com.huang.notetool.frame.common.JRootParentDiag;
import com.huang.notetool.frame.common.NotePanel;
import com.huang.notetool.po.NoteMsg;
import com.huang.notetool.service.NoteMsgService;
import com.huang.notetool.tool.NormalSearch;
import com.huang.notetool.util.Base64Tool;
import com.huang.notetool.util.Constants;
import com.huang.notetool.util.StringUtils;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.UnsupportedEncodingException;

import static java.awt.event.InputEvent.CTRL_MASK;

/**
 * 笔记显示面板
 *
 * @author 黄先生
 * @version 1.0
 * @Content 此处说明类的解释
 * @date 2019-06-28 11:25:14
 * @History 序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-06-28   创建项目           完成
 * @LeftoverProblem 此处说明遗留问题和潜在的问题
 */
public class NoteFrame extends NotePanel {
    /**
     * 存放操作提示信息
     */
    private JLabel showMsgLabel;
    /**
     * 笔记类型
     */
    private String noteType;
    /**
     * 注入Service
     */
    private NoteMsgService noteMsgService;
    /**
     * 初始化日志
     */
    private Logger logger = Logger.getLogger(NoteFrame.class);
    /**
     * 笔记ID
     */
    private Integer noteId;

    /**
     * 构造方法，显示信息
     *
     * @param jFrame 基于面板
     */
    public NoteFrame(JFrame jFrame) {
        super(jFrame);
        noteMsgService = new NoteMsgService();
        showMsgLabel = new JLabel();
        btnPanel.add(showMsgLabel);
        initial();
    }

    /**
     * 初始化监听事件
     */
    private void initial() {
        saveBtn.addActionListener(saveBtnActionListener());
        delBtn.addActionListener(delBtnBtnActionListener());
        refreshBtn.addActionListener(refreshBtnBtnActionListener());
        //快捷键
        ActionMap actionMap = jTextArea.getActionMap();
        InputMap inputMap = jTextArea.getInputMap();
        KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_S, CTRL_MASK);
        inputMap.put(key, "saveOrUpdateNote");
        actionMap.put("saveOrUpdateNote", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveOrUpdateNote();
            }
        });
    }

    /**
     * 刷新事件监听
     *
     * @return ActionListener
     */
    private ActionListener refreshBtnBtnActionListener() {
        return e -> {
            logger.info("刷新笔记");
            if (this.noteId != null) {
                NoteMsg noteMsg = noteMsgService.findMsgById(String.valueOf(noteId));
                if (null != noteMsg && StringUtils.isNotEmpty(noteMsg.getNote())) {
                    try {
                        jTextArea.setText(Base64Tool.decode(noteMsg.getNote()));
                        showMsgLabel.setText("      刷新成功");
                    } catch (UnsupportedEncodingException e1) {
                        logger.warn(e);
                        jTextArea.setText(Constants.EMPTY_STR);
                        showMsgLabel.setText("      刷新失败");
                    }
                    this.setTitle(noteMsg.getName());
                }
            } else {
                jTextArea.setText(Constants.EMPTY_STR);
                this.setTitle("未命名");
            }
        };
    }

    /**
     * 删除事件监听
     *
     * @return ActionListener
     */
    private ActionListener delBtnBtnActionListener() {
        return e -> {
            if (null != noteId && StringUtils.isNotEmpty(jTextArea.getText().trim())) {
                //1:否  2:取消  0:确定
                int result = JOptionPane.showConfirmDialog(NoteFrame.this, "确定删除？");
                if (result==0) {
                    if (!noteMsgService.deleteById(String.valueOf(noteId))) {
                        logger.warn("删除笔记信息失败 >> id=" + noteId);
                        new JRootParentDiag(NoteFrame.this,"删除失败",Constants.NOTICE_IMAGE);
                        showMsgLabel.setText("      删除失败");
                    } else {
                        jTextArea.setText(Constants.EMPTY_STR);
                        noteId = null;
                        this.setTitle("未命名");
                        showMsgLabel.setText("      删除成功");
                    }
                }else {
                    logger.info("取消删除笔记信息");
                }
            }
        };
    }

    /**
     * 保存事件监听
     *
     * @return ActionListener
     */
    private ActionListener saveBtnActionListener() {
        return e -> saveOrUpdateNote();
    }

    /**
     * 更新或者保存数据
     */
    private void saveOrUpdateNote() {
        String content = jTextArea.getText();
        if (StringUtils.isEmpty(content)) {
            new JRootParentDiag(NoteFrame.this,"内容为空",Constants.NOTICE_IMAGE);
            jTextArea.requestFocus();
            return;
        }
        NoteMsg noteMsg = new NoteMsg();
        if (null != this.noteId) {
            noteMsg.setNote(content);
            noteMsg.setId(noteId);
            if (!noteMsgService.update(noteMsg)) {
                logger.warn("更新笔记信息失败 >> id=" + noteId);
                new JRootParentDiag(NoteFrame.this,"更新失败",Constants.NOTICE_IMAGE);
            } else {
                showMsgLabel.setText("      更新成功");
            }
            return;
        }
        String name;
        while (true) {
            name = JOptionPane.showInputDialog(NoteFrame.this, "请输入笔记名称", "笔记名称输入框", JOptionPane.PLAIN_MESSAGE);
            if (StringUtils.isNotEmpty((name))) {
                NoteMsg noteMsgFromDB = noteMsgService.findMsgByName(name);
                if (null != noteMsgFromDB) {
                    new JRootParentDiag(NoteFrame.this,"名称已存在，请重新输入",Constants.NOTICE_IMAGE);
                } else {
                    break;
                }
            }
        }
        noteMsg.setNote(content);
        noteMsg.setName(name);
        noteMsg.setNoteType(this.noteType);
        if (!noteMsgService.addNoteMsg(noteMsg)) {
            logger.warn("保存笔记信息失败");
            showMsgLabel.setText("      保存失败");
            new JRootParentDiag(NoteFrame.this,"保存失败",Constants.NOTICE_IMAGE);
        } else {
            NoteMsg newValue = noteMsgService.findMsgByName(name);
            this.setTitle(name);
            showMsgLabel.setText("      保存成功");
            if (null != newValue) {
                noteId = newValue.getId();
            }
        }
    }

    /**
     * 设置文本数据
     *
     * @param text 笔记信息
     * @param id   主键
     */
    public void setText(String text, Integer id) {
        try {
            jTextArea.setText(Base64Tool.decode(text));
            this.noteId = id;
        } catch (UnsupportedEncodingException e) {
            logger.warn(e);
            jTextArea.setText(Constants.EMPTY_STR);
            new JRootParentDiag(NoteFrame.this, e.getMessage(),Constants.NOTICE_IMAGE);
        }
    }

    /**
     * 设置标题名称
     *
     * @param title 笔记信息
     */
    @Override
    public void setTitle(String title) {
        super.setTitle(title);
    }

    /**
     * 点击新增按钮监听事件
     *
     * @return ActionListener
     */
    @Override
    protected ActionListener addBtnActionListener() {
        return e -> {
            super.setTitle("未命名");
            jTextArea.setText(Constants.EMPTY_STR);
            this.noteId = null;
            showMsgLabel.setText("      请输入");
        };
    }

    /**
     * 设置笔记类型
     *
     * @param noteType 笔记类型
     */
    public void setType(String noteType) {
        this.noteType = noteType;
    }

}
