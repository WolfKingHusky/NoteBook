package com.huang.notetool.frame.common;

import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * 用于继承的JTABLE
 *
 * @author 黄先生
 * @version 1.0
 * @Content 此处说明类的解释
 * @CreateDate 2019-05-25 18:04:27
 * @History 序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-05-25   创建项目           完成
 * @LeftoverProblem 此处说明遗留问题和潜在的问题
 */
public class MsgTableFrame extends JRootParentFrame {
    /**
     * 日志
     */
    private Logger logger = Logger.getLogger(MsgTableFrame.class);

    /**
     * 列名,表头
     */
    protected Object[] headerArr;

    /**
     * 表格所有行数据
     */
    protected Object[][] tableData;

    /**
     * 表格
     */
    protected JTable jTable;
    /**
     * 表格模式
     */
    protected DefaultTableModel model;
    /**
     * table过滤器
     */
    RowSorter<DefaultTableModel> sorter;
    /**
     * 按钮所在行
     */
    protected Integer btnColumn;
    /**
     * 表的行数
     */
    private int rowNum = -1;
    /**
     * 表的最大行数
     */
    private int biggerRowNum = -1;
    private int biggerRowNumLimit = -1;

    public MsgTableFrame() {
    }
    //        super("note pad");
    //        try {
    //            //注释的是java6的
    //            // UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
    //            // 将LookAndFeel设置成Windows样式
    //            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    //        } catch (Exception ex) {
    //            logger.warn("加载系统样式失败" + ex);
    //        }
    //        //得到屏幕尺寸
    //        Dimension screen = getToolkit().getScreenSize();
    //        //窗口风格
    //        setLayout(new BorderLayout());
    //        //窗口大小
    //        setSize((screen.width) * 2 / 3, (screen.height) * 2 / 3);
    //        //窗口位置
    //        setLocation((screen.width - getSize().width) / 2, (screen.height - getSize()
    //        .height) / 2);
    //        //可视化
    //        setVisible(true);
    //        //初始化控件
    //        initial();
    //        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    //    }


    /**
     * Object[] headerArr = {"软件名称", "版本号", "出版商", "安装路径", "安装时间"};
     *
     * @param headerArr 表头
     * @param tableData 表数据初始化大小
     */
    public void initial(Object[] headerArr, Object[][] tableData) {
        logger.info("初始化表格设置... ");
        this.headerArr = headerArr;
        this.tableData = tableData;
        this.biggerRowNum = tableData.length;
    }

    /**
     * 增加行
     *
     * @param data 行数据
     */
    public void addRow(Object[] data, int columnNum) {
        rowNum++;
        if (rowNum >= biggerRowNum) {
            logger.warn("超过最大行限制 ... ");
            if (biggerRowNumLimit == -1) {
                JOptionPane.showMessageDialog(this, "超过最大行[" + biggerRowNum + "]限制");
                biggerRowNumLimit++;
            }
            //超过限制，增加行
            this.model.addRow(new Object[columnNum]);
        }
        int column = 0;
        for (Object value : data) {
            this.jTable.getModel().setValueAt(value, rowNum, column);
            column++;
            if (column == columnNum) {
                break;
            }
        }
        this.repaint();
    }

    /**
     * 表格初始化
     *
     * @return JTable
     */

    public JTable initialTable(Integer rowHeight) {
        logger.info("初始化表格... ");
        model = new DefaultTableModel(tableData, headerArr);
        jTable = new JTable(model);
        jTable.addMouseMotionListener(tableMouseMoveListener());
        sorter = new TableRowSorter<>(model);
        jTable.setRowSorter(sorter);
        if (null != rowHeight) {
            jTable.setRowHeight(rowHeight);
        }
        return jTable;
    }

    /**
     * 获取表格
     *
     * @return JTable
     */

    public JTable getTable() {
        logger.info("获取表格... ");
        return jTable;
    }

    /**
     * 获取过滤器
     *
     * @return RowSorter
     */
    public RowSorter<DefaultTableModel> getSorter() {
        return sorter;
    }

    public void setSorter(RowSorter<DefaultTableModel> sorter) {
        this.sorter = sorter;
    }

    public DefaultTableModel getModel() {
        return model;
    }

    /**
     * 为列添加按钮
     */
    public void addButton(int column, MyRender myRender) {
        //设置编辑器,可以添加按钮
        jTable.getColumnModel().getColumn(column).setCellEditor(myRender);
        jTable.getColumnModel().getColumn(column).setCellRenderer(myRender);

        btnColumn = column;
        //        jTable.updateUI();
    }

    /**
     * 鼠标移动事件
     *
     * @return MouseMotionAdapter
     */
    private MouseMotionAdapter tableMouseMoveListener() {
        return new MouseMotionAdapter() {
            /**
             * Invoked when the mouse button has been moved on a component
             * (with no buttons no down).
             * 鼠标按键在组件上单击（按下并释放）时调用。
             *
             * @param e MouseEvent
             */
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                JTable table = (JTable) e.getSource();

                Point p = e.getPoint();
                //得到选中的行列的索引值
                int row = table.rowAtPoint(p);
                if (row >= 0) {
                    jTable.setRowSelectionInterval(row, row);
                }
            }
        };
    }

    /**
     * 清空表格数据
     *
     * @param initialTableRowNum 初始化表格大小
     * @return true or false
     */
    public boolean clear(int initialTableRowNum) {
        try {
            ((DefaultTableModel) (this.jTable.getModel())).getDataVector().clear();
            ((DefaultTableModel) (this.jTable.getModel())).setRowCount(initialTableRowNum);
            this.biggerRowNum = initialTableRowNum;
            rowNum = -1;
        } catch (Exception e) {
            logger.warn(e);
            return false;
        }
        return true;
    }
}
