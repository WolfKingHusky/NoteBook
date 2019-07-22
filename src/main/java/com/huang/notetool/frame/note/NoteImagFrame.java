package com.huang.notetool.frame.note;

import com.huang.notetool.frame.common.JRootParentDiag;
import com.huang.notetool.frame.common.NoteImagPanel;
import com.huang.notetool.po.NoteMsg;
import com.huang.notetool.service.NoteMsgService;
import com.huang.notetool.tool.ImageTool;
import com.huang.notetool.util.Base64Tool;
import com.huang.notetool.util.Constants;
import com.huang.notetool.util.StringUtils;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.UnsupportedEncodingException;

import static java.awt.event.InputEvent.CTRL_MASK;

/**
 * 笔记显示面板(含图片)
 *
 * @author 黄先生
 * @version 1.0
 * @Content 此处说明类的解释
 * @date 2019-06-28 11:25:14
 * @History 序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-06-28   创建项目           完成
 * @LeftoverProblem 此处说明遗留问题和潜在的问题
 */
public class NoteImagFrame extends NoteImagPanel {
    /**
     * 图片流开始字符串
     */
    private static final String PICTURE_BEGIN = "picture begin";
    /**
     * 图片流结束字符串
     */
    private static final String PICTURE_END = "picture end";
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
    private Logger logger = Logger.getLogger(NoteImagFrame.class);
    /**
     * 笔记ID
     */
    private Integer noteId;

    /**
     * 构造方法，显示信息
     *
     * @param jFrame 基于面板
     */
    public NoteImagFrame(JFrame jFrame) {
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
        ActionMap actionMap = jTextPane.getActionMap();
        InputMap inputMap = jTextPane.getInputMap();
        KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_S, CTRL_MASK);
        inputMap.put(key, "saveOrUpdateNote");
        actionMap.put("saveOrUpdateNote", ShiftSave());
    }

    private AbstractAction ShiftSave() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveOrUpdateNote();
            }
        };
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
                        setText(noteMsg.getNote(), noteMsg.getId());
                        showMsgLabel.setText("      刷新成功");
                    } catch (Exception e1) {
                        logger.warn(e);
                        jTextPane.setText(Constants.EMPTY_STR);
                        showMsgLabel.setText("      刷新失败");
                    }
                    this.setTitle(noteMsg.getName());
                }
            } else {
                jTextPane.setText(Constants.EMPTY_STR);
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
            if (null != noteId) {
                //1:否  2:取消  0:确定
                int result = JOptionPane.showConfirmDialog(NoteImagFrame.this, "确定删除？");
                if (result==0) {
                    if (!noteMsgService.deleteById(String.valueOf(noteId))) {
                        logger.warn("删除笔记信息失败 >> id=" + noteId);
                        new JRootParentDiag(NoteImagFrame.this,"删除失败",Constants.NOTICE_IMAGE);
                        showMsgLabel.setText("      删除失败");
                    } else {
                        jTextPane.setText(Constants.EMPTY_STR);
                        noteId = null;
                        this.setTitle("未命名");
                        showMsgLabel.setText("      删除成功");
                    }
                }else {
                    logger.info("取消删除图片笔记信息");
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
        String content = jTextPane.getText();
        if (StringUtils.isEmpty(content)) {
            new JRootParentDiag(NoteImagFrame.this,"内容为空",Constants.NOTICE_IMAGE);
            jTextPane.requestFocus();
            return;
        }
        NoteMsg noteMsg = new NoteMsg();
        StringBuilder value = new StringBuilder();
        //获取读取的StyledDocument
        StyledDocument styledDocument = jTextPane.getStyledDocument();
        //遍历读取的StyledDocument
        for (int i = 0; i < styledDocument.getLength(); i++) {
            //如果发现是icon元素，那么：
            if ("icon".equals(styledDocument.getCharacterElement(i).getName())) {
                Element ele = styledDocument.getCharacterElement(i);
                ImageIcon icon = (ImageIcon) StyleConstants.getIcon(ele.getAttributes());
                BufferedImage bufferedImage = ImageTool.imageToBufferedImage(icon.getImage());
                value.append(PICTURE_BEGIN);
                value.append(Base64Tool.encodeBufferImag(bufferedImage));
                value.append(PICTURE_END);
            } else {//如果不是icon（可以判断是文字，因为目前只有图片元素插入）
                try {
                    String contentStr = styledDocument.getText(i, 1);
                    value.append(Base64Tool.encode(contentStr));
                    value.append("#");
                } catch (BadLocationException e1) {
                    logger.warn(e1);
                }
            }
        }
        noteMsg.setNote(value.toString());
        if (null != this.noteId) {
            noteMsg.setId(noteId);
            if (!noteMsgService.update(noteMsg)) {
                logger.warn("更新笔记信息失败 >> id=" + noteId);
                new JRootParentDiag(NoteImagFrame.this,"更新失败",Constants.NOTICE_IMAGE);
            } else {
                showMsgLabel.setText("      更新成功");
            }
            return;
        }
        String name;
        while (true) {
            name = JOptionPane.showInputDialog(NoteImagFrame.this, "请输入笔记名称", "笔记名称输入框", JOptionPane.PLAIN_MESSAGE);
            if (StringUtils.isNotEmpty((name))) {
                NoteMsg noteMsgFromDB = noteMsgService.findMsgByName(name);
                if (null != noteMsgFromDB) {
                    new JRootParentDiag(NoteImagFrame.this,"名称已存在，请重新输入",Constants.NOTICE_IMAGE);
                } else {
                    break;
                }
            }
        }
        noteMsg.setName(name);
        noteMsg.setNoteType(this.noteType);
        if (!noteMsgService.addNoteMsg(noteMsg)) {
            logger.warn("保存笔记信息失败");
            new JRootParentDiag(NoteImagFrame.this,"保存失败",Constants.NOTICE_IMAGE);
        } else {
            NoteMsg newValue = noteMsgService.findMsgByName(name);
            this.setTitle(name);
            if (null != newValue) {
                noteId = newValue.getId();
            }
            showMsgLabel.setText("      保存成功");
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
            this.noteId = id;
            if (StringUtils.isEmpty(text)) {
                logger.warn("内容为空");
                return;
            }
            jTextPane.removeAll();
            jTextPane.repaint();
            jTextPane.setText("");
            String firstDecode = Base64Tool.decode(text);
            if (StringUtils.isEmpty(firstDecode)) {
                logger.warn("解密出错，解密后数据为空");
                return;
            }
            if (!firstDecode.contains(PICTURE_BEGIN)) {
                jTextPane.setText(Base64Tool.decode(firstDecode));
            } else {
                String first = firstDecode.substring(0, firstDecode.indexOf(PICTURE_BEGIN));
                String[] msgArr = firstDecode.split(PICTURE_BEGIN);
                if (first.length() > 0) {
                    StringBuilder textBuilder = getDecodeMsg(msgArr[0]);
                    jTextPane.setText(textBuilder.toString());
                }
                //遍历每一图片和图片后面的信息
                for (int num = 1; num < msgArr.length; num++) {
                    String[] picMsgArr = msgArr[num].split(PICTURE_END);
                    super.insertPic(Base64Tool.decodeBufferImag(picMsgArr[0]));
                    if (picMsgArr.length > 1) {
                        StringBuilder textBuilder = getDecodeMsg(picMsgArr[1]);
                        super.insert(textBuilder.toString(), this.jTextPane.getFont());
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            logger.warn(e);
            jTextPane.setText(Constants.EMPTY_STR);
            new JRootParentDiag(NoteImagFrame.this,e.getMessage(),Constants.NOTICE_IMAGE);
        }
    }

    private StringBuilder getDecodeMsg(String s) throws UnsupportedEncodingException {
        StringBuilder textBuilder = new StringBuilder();
        String[] wordArr = s.split("#");
        for (String wordMsg : wordArr) {
            textBuilder.append(Base64Tool.decode(wordMsg));
        }
        return textBuilder;
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
            jTextPane.setText(Constants.EMPTY_STR);
            this.noteId = null;
            showMsgLabel.setText("      请输入 ");
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
