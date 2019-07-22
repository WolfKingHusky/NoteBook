//package com.huang.notetool.frame.format.git;
//
//import com.huang.notetool.frame.common.JRootParentFrame;
//import com.huang.notetool.frame.common.MsgTableFrame;
//import com.huang.notetool.frame.common.MyRender;
//import com.huang.notetool.frame.common.ShowMsgSinglePanel;
//import com.huang.notetool.frame.format.linux.LinuxCmdEditFrame;
//import com.huang.notetool.frame.format.linux.LinuxCmdViewFrame;
//import com.huang.notetool.frame.format.linux.addLinuxCmdFrame;
//import com.huang.notetool.service.LinuxCmdService;
//import com.huang.notetool.tool.ChooseWayFrame;
//import com.huang.notetool.util.StringUtils;
//import com.huang.notetool.util.TxtFileFilter;
//import org.apache.log4j.Logger;
//
//import javax.swing.*;
//import javax.swing.table.DefaultTableModel;
//import javax.swing.table.TableRowSorter;
//import java.awt.*;
//import java.awt.event.*;
//
///**
// * TODO:
// * Git命令大全，支持导入
// *
// * @author huang
// * @date 2019-06-19
// */
//public class GitCmdFrame extends JRootParentFrame implements Runnable {
//    private static final int INITIAL_TABLE_ROW_NUM = 30;
//    /**
//     * 日志
//     */
//    private Logger logger = Logger.getLogger(GitCmdFrame.class);
//    /**
//     * Service
//     */
//    private LinuxCmdService linuxCmdService;
//    /**
//     * 表格显示框
//     */
//    private JTextPane textPane;
//    /**
//     * 获取面板控件
//     */
//    private MsgTableFrame myTable;
//
//    public GitCmdFrame() {
//        linuxCmdService = new LinuxCmdService();
//    }
//
//    /**
//     * 构造方法
//     *
//     * @param ignored 忽略
//     */
//    public GitCmdFrame(String ignored) {
//        logger.info("初始化显示面板");
//        textPane = new JTextPane();
//        myTable = new MsgTableFrame();
//        linuxCmdService = new LinuxCmdService();
//
//        JPanel searchPanel = initialSearchPanel();
//
//        this.setLayout(new BorderLayout());
//        this.setTitle("Linux命令大全，可搜索(输入搜索条件后回车)，查看和删除，以及导入导出");
//        this.setLocation(100, 100);
//        this.setSize(1200, 600);
//        Object[] headerArr = {"编号", "Linux命令", "作用", "用法", "使用权限", "实例", "操作"};
//        int countSize = getCountSize();
//        myTable.initial(headerArr, new Object[countSize][7]);
//        JScrollPane jScrollPane = new JScrollPane(myTable.initialTable(30));
//
//        JPanel btnPanel = new JPanel();
//        JButton exitBtn = new JButton("退出");
//        JButton refreshBtn = new JButton("刷新");
//        JButton addBtn = new JButton("新增");
//        JButton clearBtn = new JButton("清空");
//        JButton importBtn = new JButton("导入");
//        JButton exportBtn = new JButton("导出");
//
//        btnPanel.add(addBtn);
//        btnPanel.add(refreshBtn);
//        btnPanel.add(importBtn);
//        btnPanel.add(exportBtn);
//        btnPanel.add(clearBtn);
//        btnPanel.add(exitBtn);
//
//        this.add(searchPanel, BorderLayout.NORTH, FlowLayout.LEFT);
//        this.add(jScrollPane, BorderLayout.CENTER);
//        this.add(btnPanel, BorderLayout.SOUTH);
//
//        this.setVisible(true);
//        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
//
//        exitBtn.addActionListener(exitAction());
//        refreshBtn.addActionListener(refreshBtnAction());
//        clearBtn.addActionListener(clearBtnAction());
//        //导入
//        importBtn.addActionListener(importAction());
//        //导出
//        exportBtn.addActionListener(exportAction());
//        addBtn.addActionListener(addBtnAction());
//
//        initial(countSize);
//
//    }
//
//    /**
//     * 初始化搜索面板
//     */
//    private JPanel initialSearchPanel() {
//        JPanel searchPanel = new JPanel();
//        searchPanel.setLayout(new BorderLayout());
//        JPanel linuxNamePanel = new JPanel();
//
//        JTextField linuxField = new JTextField(40);
//        JTextField usageField = new JTextField(40);
//        JTextField permissionField = new JTextField(40);
//        //SwingConstants.LEFT JLabel靠左
//        linuxNamePanel.add(new JLabel("命令名称：", SwingConstants.LEFT));
//        linuxNamePanel.add(linuxField);
//        searchPanel.setBorder(BorderFactory.createLoweredBevelBorder());
//        searchPanel.add(linuxNamePanel, BorderLayout.BEFORE_LINE_BEGINS);
//
//        JPanel secondPanel = new JPanel();
//        secondPanel.add(new JLabel("命令作用："));
//        secondPanel.add(usageField);
//        secondPanel.add(new JLabel("命令权限："));
//        secondPanel.add(permissionField);
//        //BorderLayout.BEFORE_LINE_BEGINS面板靠左
//        searchPanel.add(secondPanel, BorderLayout.SOUTH, 1);
//
//        int linuxNameIndex = 1;
//        int usageIndex = 2;
//        int permissionIndex = 4;
//        linuxField.addKeyListener(linuxCmdKeyListener(linuxField, linuxNameIndex, usageField, permissionField));
//        usageField.addKeyListener(usageFieldKeyListener(usageField, usageIndex, linuxField, permissionField));
//        permissionField.addKeyListener(permissionFieldKeyListener(permissionField, permissionIndex, linuxField, usageField));
//        return searchPanel;
//    }
//
//    /**
//     * 输入键值监听事件
//     *
//     * @param permissionField Linux命令使用权限过滤框
//     * @param permissionIndex Linux命令使用权限所在列
//     * @param linuxField      Linux命令过滤框
//     * @param usageField      作用过滤框
//     * @return KeyAdapter
//     */
//    private KeyListener permissionFieldKeyListener(JTextField permissionField, int permissionIndex, JTextField linuxField, JTextField usageField) {
//        return getKeyAdapterCommon(permissionField, permissionIndex);
//    }
//
//    /**
//     * 输入键值监听事件
//     *
//     * @param usageField      Linux命令作用过滤框
//     * @param usageIndex      Linux命令作用所在列
//     * @param linuxField
//     * @param permissionField
//     * @return KeyAdapter
//     */
//    private KeyListener usageFieldKeyListener(JTextField usageField, int usageIndex, JTextField linuxField, JTextField permissionField) {
//        return getKeyAdapterCommon(usageField, usageIndex);
//    }
//
//    /**
//     * 输入键值监听事件
//     *
//     * @param linuxField      Linux命令过滤框
//     * @param linuxNameIndex  Linux命令Name所在列
//     * @param usageField
//     * @param permissionField
//     * @return KeyAdapter
//     */
//    private KeyAdapter linuxCmdKeyListener(JTextField linuxField, int linuxNameIndex, JTextField usageField, JTextField permissionField) {
//        return getKeyAdapterCommon(linuxField, linuxNameIndex);
//    }
//
//    /**
//     * 输入键值监听事件
//     *
//     * @param jTextField  过滤框
//     * @param columnIndex 过滤列所在
//     * @return KeyAdapter
//     */
//    private KeyAdapter getKeyAdapterCommon(JTextField jTextField, int columnIndex) {
//        return new KeyAdapter() {
//            /**
//             * Invoked when a key has been released.
//             *
//             * @param e KeyEvent
//             */
//            @Override
//            public void keyTyped(KeyEvent e) {
//                //键入某个键时调用此方法。
//                super.keyTyped(e);
//                JTable table = myTable.getTable();
//                if (table == null) {
//                    return;
//                }
//
//                TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) myTable.getSorter();
//                if (sorter == null) {
//                    sorter = new TableRowSorter<>(myTable.getModel());
//                    myTable.setSorter(sorter);
//                }
//
//                String text = jTextField.getText().trim();
//
//                if (StringUtils.isEmpty(text)) {
//                    sorter.setRowFilter(null);
//                } else {
//                    //设置RowFilter 用于从模型中过滤条目，使得这些条目不会在视图中显示
//                    //LinuxName所在列（indices里面的integer是需要过滤的列，没有默认是所有）
//                    sorter.setRowFilter(RowFilter.regexFilter(text, columnIndex));
//                }
//            }
//        };
//    }
//
//    /**
//     * 新增事件
//     *
//     * @return ActionListener
//     */
//    private ActionListener addBtnAction() {
//
//        return e -> {
//            addLinuxCmdFrame addLinuxCmdFrame = new addLinuxCmdFrame(GitCmdFrame.this);
//            addLinuxCmdFrame.initial();
//            addLinuxCmdFrame.setVisible(true);
//            refreshData();
//        };
//    }
//
//    /**
//     * 导出数据
//     *
//     * @return ActionListener
//     */
//    private ActionListener exportAction() {
//        return e -> {
//            logger.info("正在导出 Linux命令 数据... ");
//            String filePath = ChooseWayFrame.chooseFile(null, null, JFileChooser.FILES_AND_DIRECTORIES, false);
//            if (StringUtils.isNotEmpty(filePath)) {
//                String fileType = ChooseWayFrame.getExportFileType(this);
//                if (linuxCmdService.exportLinuxCmdMsg(filePath, fileType)) {
//                    logger.info("导出Linux命令数据成功... ");
//                    JOptionPane.showMessageDialog(null, "导出数据成功");
//                } else {
//                    logger.info("导出Linux命令数据失败... ");
//                    JOptionPane.showMessageDialog(null, "导出数据失败");
//                }
//            }
//        };
//    }
//
//    /**
//     * 导入
//     *
//     * @return ActionListener
//     */
//    private ActionListener importAction() {
//        return e -> {
//            logger.info("正在准备导入 Linux命令 数据... ");
//            TxtFileFilter txtFileFilter = new TxtFileFilter();
//            String filePath = ChooseWayFrame.chooseFile(super.getContentPane(), txtFileFilter, JFileChooser.FILES_ONLY, true);
//            if (StringUtils.isNotEmpty(filePath)) {
//                logger.info("导入 Linux命令 数据准备中... ");
//                String returnMsg = linuxCmdService.importMethod(filePath);
//                new ShowMsgSinglePanel(returnMsg, GitCmdFrame.this);
//                refreshData();
//            }
//        };
//    }
//
//    /**
//     * 获取总的数量
//     *
//     * @return 总的数量
//     */
//    private int getCountSize() {
//        int countSize = linuxCmdService.countSize();
//        if (countSize < INITIAL_TABLE_ROW_NUM) {
//            countSize = INITIAL_TABLE_ROW_NUM;
//        }
//        return countSize;
//    }
//
//    /**
//     * 清空数据
//     *
//     * @return ActionListener
//     */
//    private ActionListener clearBtnAction() {
//        return e -> {
//            linuxCmdService.deleteAll();
//            JOptionPane.showMessageDialog(this, "成功清空数据");
//            refreshData();
//
//        };
//    }
//
//    private ActionListener refreshBtnAction() {
//        return e -> refreshData();
//    }
//
//    /**
//     * 刷新数据
//     */
//    private void refreshData() {
//        if (null != myTable) {
//            int countSize = getCountSize();
//            //初始化大小
//            int initialTableRow = countSize;
//            if (countSize < INITIAL_TABLE_ROW_NUM) {
//                initialTableRow = INITIAL_TABLE_ROW_NUM;
//            }
//            if (!myTable.clear(initialTableRow)) {
//                logger.warn("刷新 LinuxCmdFrame 失败");
//                JOptionPane.showMessageDialog(this, "刷新失败");
//                return;
//            }
//            logger.info("刷新 LinuxCmdFrame 成功");
//            initial(countSize);
//            validate();
//            repaint();
//        }
//    }
//
//    private ActionListener exitAction() {
//        return e -> this.dispose();
//    }
//
//    /**
//     * 初始化
//     *
//     * @param countSize 数据库总的条目数
//     */
//    private void initial(int countSize) {
//        textPane.setText("Linux命令信息：");
//        boolean result = countSize > 0;
//        int beginRow = 0;
//        int endRow = 200;
//        int btnColumn = 6;
//        while (result) {
//            String[] msgArr = linuxCmdService.getAllLinuxCmdAndExampleReturnArr(beginRow, endRow);
//            if (msgArr == null || msgArr.length < 200) {
//                result = false;
//            } else {
//                beginRow = endRow;
//                endRow = endRow + endRow;
//            }
//            if (msgArr != null && msgArr.length > 0) {
//                for (String linuxCmd : msgArr) {
//                    //{"编号", "Linux命令", "作用", "用法", "使用权限", "实例", "操作"};
//                    //使用权限：所有者，Root用户，管理员，根据进程而定，普通用户，特殊用户，其他
//                    Object[] showMsgArr = new String[7];
//                    //主键编号，Linux命令，作用，用法，使用权限，更新时间，更新次数,参数说明,多个实例(使用中文；作为换行)，添加时间
//                    String[] msgProArr = linuxCmd.split("，");
//                    //主键编号
//                    showMsgArr[0] = msgProArr[0];
//                    showMsgArr[1] = msgProArr[1];
//                    showMsgArr[2] = msgProArr[2];
//                    showMsgArr[3] = msgProArr[3] == null ? "" : msgProArr[3];
//                    showMsgArr[4] = msgProArr[4] == null ? "" : msgProArr[4];
//                    showMsgArr[5] = msgProArr[8] == null ? "" : msgProArr[8];
//                    myTable.addRow(showMsgArr, btnColumn);
//                }
//            }
//        }
//
//        MyRender myRender = new MyRender();
//        myTable.addButton(btnColumn, myRender);
//        myRender.getEditBtn().addActionListener(editBtnAction());
//        myRender.getViewBtn().addActionListener(viewBtnAction());
//        myRender.getDelBtn().addMouseListener(delBtnAction(btnColumn, myRender));
//        myTable.getTable().updateUI();
//        // myRender.getBtnPanel().addMouseListener(btnPanelAction(btnColumn, myRender));
//    }
//
//    private MouseAdapter btnPanelAction(int btnColumn, MyRender myRender) {
////        *void mouseClicked(MouseEvent e)
////             *
////             * 鼠标进入到组件上时调用。
////             *  void mouseEntered(MouseEvent e)
////             *
////             *鼠标离开组件时调用。
////             * void mouseExited(MouseEvent e)
////             *
////             * 鼠标按键在组件上按下时调用。
////             * void mousePressed(MouseEvent e)
////             *
////             * 鼠标按钮在组件上释放时调用。
////             *void mouseReleased(MouseEvent e)
//        return new MouseAdapter() {
//            /**
//             * {@inheritDoc}
//             * 鼠标离开组件时调用
//             * @param e
//             */
//            @Override
//            public void mouseExited(MouseEvent e) {
//                super.mouseExited(e);
//                JTable table = myTable.getTable();
//                int row = table.getSelectedRow();
//                if (row != -1) {
//                    Object value = table.getValueAt(row, btnColumn);
//                    //if (null == value || StringUtils.isEmpty(value.toString())) {
//                    table.setValueAt(myRender.getBtnPanel(), row, btnColumn);
//                    //table.updateUI();
//                    // }
//                }
//
//            }
//        };
//    }
//
//    /**
//     * 查删除按钮点击事件
//     *
//     * @param btnColumn 按钮所在行数
//     * @param myRender  编辑器
//     * @return ActionListener
//     */
//    private MouseAdapter delBtnAction(int btnColumn, MyRender myRender) {
//        ////单行
//        //返回第一个选定列的索引；如果没有选定的列，则返回 -1。
//        //int getSelectedColumn()
//        //返回第一个选定行的索引；如果没有选定的行，则返回 -1。
//        //int getSelectedRow()
//        //返回 row 和 column 位置的单元格值。
//        //Object getValueAt(int row, int column)
//        //
//        ////多行
//        //int[] getSelectedColumns()
//        //返回所有选定列的索引。
//        //int[] getSelectedRows()
//        //返回所有选定行的索引。
//        //Object getValueAt(int row, int column)
//        //返回 row 和 column 位置的单元格值。
//        return new MouseAdapter() {
//            /**
//             * 鼠标按键在组件上按下时调用
//             *
//             * @param e MouseEvent
//             */
//            @Override
//            public void mousePressed(MouseEvent e) {
//                logger.info("删除Linux信息按钮点击事件");
//                JTable table = myTable.getTable();
//                //得到选中的行列的索引值
//                int row = table.getSelectedRow();
//                if (row != -1) {
//                    String id = (String) table.getValueAt(row, 0);
//                    if (StringUtils.isEmpty(id)) {
//                        logger.warn("id为空，删除Linux命令失败");
//                        JOptionPane.showMessageDialog(GitCmdFrame.this, "删除失败，请重新选中此行");
//                        return;
//                    }
//                    logger.info("即将进行删除操作");
//                    //1:否  2:取消  0:确定
//                    int result = JOptionPane.showConfirmDialog(GitCmdFrame.this, "确定删除？");
//                    if (result == 0) {
//                        if (!linuxCmdService.deleteById(id)) {
//                            logger.warn("删除Linux命令失败[[ id=" + id);
//                            JOptionPane.showMessageDialog(GitCmdFrame.this, "删除失败");
//                            return;
//                        }
//                        logger.warn("删除Linux命令成功[[ id=" + id);
//                        JOptionPane.showMessageDialog(GitCmdFrame.this, "删除成功");
//                        refreshData();
//                        int rowInTable = myTable.getTable().getRowCount();
//                        int dbCount = linuxCmdService.countSize();
//                        if (rowInTable > dbCount) {
//                            myTable.getTable().setValueAt(myRender.getDefaultPanel(), dbCount, btnColumn);
//                            myTable.getTable().updateUI();
//                        }
//                    }
//                }
//            }
//
////            /**
////             * 鼠标离开组件时调用
////             * {@inheritDoc}
////             *
////             * @param e MouseEvent
////             */
////            @Override
////            public void mouseExited(MouseEvent e) {
////                super.mouseExited(e);
////                JTable table = myTable.getTable();
////                int row = table.getSelectedRow();
////                if (row != -1) {
////                    Object value = table.getValueAt(row, btnColumn);
////                    if (null == value || StringUtils.isEmpty(value.toString())) {
////                        table.setValueAt(myRender.getBtnPanel(), row, btnColumn);
////                    }
////                }
////
////            }
//        };
//    }
//
//    /**
//     * 查看按钮点击事件
//     *
//     * @return ActionListener
//     */
//    private ActionListener viewBtnAction() {
//        return e -> {
//            logger.info("查看Linux信息按钮点击事件");
//            JTable table = myTable.getTable();
//            //得到选中的行列的索引值
//            int row = table.getSelectedRow();
//            if (row != -1) {
//                String id = (String) table.getValueAt(row, 0);
//                if (StringUtils.isEmpty(id)) {
//                    logger.warn("id为空，查看Linux命令失败");
//                    JOptionPane.showMessageDialog(GitCmdFrame.this, "查看Linux命令失败，请重新选中此行");
//                    return;
//                }
//                //需要新增按钮事件
//                LinuxCmdViewFrame linuxCmdViewFrame = new LinuxCmdViewFrame(this);
//                linuxCmdViewFrame.initial(id);
//                linuxCmdViewFrame.setVisible(true);
//                logger.info("查看按钮点击事件");
//            }
//        };
//    }
//
//    /**
//     * 编辑按钮点击事件
//     *
//     * @return ActionListener
//     */
//    private ActionListener editBtnAction() {
//        return e -> {
//            logger.info("编辑Linux信命令息按钮点击事件");
//            JTable table = myTable.getTable();
//            //得到选中的行列的索引值
//            int row = table.getSelectedRow();
//            if (row != -1) {
//                String id = (String) table.getValueAt(row, 0);
//                if (StringUtils.isEmpty(id)) {
//                    logger.warn("id为空，查看Linux命令失败");
//                    JOptionPane.showMessageDialog(GitCmdFrame.this, "查看Linux命令失败，请重新选中此行");
//                    return;
//                }
//                //需要新增按钮事件
//                LinuxCmdEditFrame linuxCmdEditFrame = new LinuxCmdEditFrame(this);
//                linuxCmdEditFrame.initial(id);
//                linuxCmdEditFrame.setVisible(true);
//                refreshData();
//            }
//        };
//    }
////    /**
////     * 鼠标点击事件
////     *
////     * @param btnColumn 按钮所在行
////     * @return MouseListener
////     */
////    private MouseListener tableMouseListener(Integer btnColumn) {
////        return new MouseAdapter() {
////            /**
////             * {@inheritDoc}
////             *void mouseClicked(MouseEvent e)
////             *
////             * 鼠标进入到组件上时调用。
////             *  void mouseEntered(MouseEvent e)
////             *
////             *鼠标离开组件时调用。
////             * void mouseExited(MouseEvent e)
////             *
////             * 鼠标按键在组件上按下时调用。
////             * void mousePressed(MouseEvent e)
////             *
////             * 鼠标按钮在组件上释放时调用。
////             *void mouseReleased(MouseEvent e)
////             * @param e
////             */
////            @Override
////            public void mousePressed(MouseEvent e) {
////                super.mousePressed(e);
////                JTable table = (JTable) e.getSource();
////                //得到选中的行列的索引值
////                int column = table.getSelectedColumn();
//////                table.getSelectionModel().
////                int row = table.getSelectedRow();
////                if (row != -1) {
////                    String id = (String) table.getValueAt(row, 0);
////                    if (StringUtils.isEmpty(id)) {
////                        logger.warn("id为空，删除Linux命令失败");
////                        JOptionPane.showMessageDialog(LinuxCmdFrame.this, "删除失败，请重新选中此行");
////                        return;
////                    }
////                    if (btnColumn != null) {
////                        //说明存在按钮
////                        JPanel panel = (JPanel) table.getCellEditor(row, btnColumn).getTableCellEditorComponent(table,null,true,row,btnColumn);
////                        Point point=table.getCellEditor(row, btnColumn).getTableCellEditorComponent(table,null,true,row,btnColumn).getMousePosition();
////                        if (null == panel){
////                            logger.warn("panel组件为空，删除Linux命令失败");
////
////                            return;
////                        }
////                        JButton jButton = (JButton) panel.getComponentAt(point);
////                        //修改
////                        //查看
////                        //删除
////                        if (jButton.getText().contains("删除")) {
////                            logger.info("即将进行删除操作");
////                            if (!linuxCmdService.deleteById(id)) {
////                                logger.warn("删除Linux命令失败");
////                                JOptionPane.showMessageDialog(LinuxCmdFrame.this, "删除失败");
////                                return;
////                            }
////                            refreshData();
////                            return;
////                        } else if (jButton.getText().contains("查看")) {
////
////                        } else if (jButton.getText().contains("修改")) {
////
////                        }
////                    }
////                    //boolean result = ;
////                }
////            }
////        };
////    }
//
//
//    /**
//     * When an object implementing interface <code>Runnable</code> is used
//     * to create a thread, starting the thread causes the object's
//     * <code>run</code> method to be called in that separately executing
//     * thread.
//     * <p>
//     * The general contract of the method <code>run</code> is that it may
//     * take any action whatsoever.
//     *
//     * @see Thread#run()
//     */
//    @Override
//    public void run() {
//        new GitCmdFrame(null);
//    }
//
////    public static void main(String[] args) {
////        new LinuxCmdFrame(null);
////    }
//}