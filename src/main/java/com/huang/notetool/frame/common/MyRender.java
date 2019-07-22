package com.huang.notetool.frame.common;

import com.huang.notetool.util.StringUtils;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.EventObject;

/**
 * 表格编辑事件
 * 渲染器  编辑器
 *
 * @author 黄先生
 * @version 1.0
 * @Content 此处说明类的解释
 * @date 2019-06-20 11:19:04
 * @History 序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-06-20   创建项目           完成
 * @LeftoverProblem 此处说明遗留问题和潜在的问题
 */
public class MyRender implements TableCellEditor, TableCellRenderer {
    /**
     * 日志
     */
    private Logger logger = Logger.getLogger(MyRender.class);


    /**
     * 面板
     */
    private JPanel btnPanel;
    private JPanel defaultPanel;
    /**
     * 按钮
     */
    private JButton editBtn;
    private JButton viewBtn;
    private JButton delBtn;

    public JButton getEditBtn() {
        return editBtn;
    }

    public JButton getViewBtn() {
        return viewBtn;
    }

    public JButton getDelBtn() {
        return delBtn;
    }

    public JPanel getBtnPanel() {
        return btnPanel;
    }

    public JPanel getDefaultPanel() {
        return defaultPanel;
    }

    /**
     * ChangeEvent用于通知感兴趣的参与者事件源中的状态已发生更改。
     * private ChangeEvent changeEvent = new ChangeEvent(this);
     * //单行
     * //返回第一个选定列的索引；如果没有选定的列，则返回 -1。
     * //int getSelectedColumn()
     * //返回第一个选定行的索引；如果没有选定的行，则返回 -1。
     * //int getSelectedRow()
     * //返回 row 和 column 位置的单元格值。
     * //Object getValueAt(int row, int column)
     * //
     * ////多行
     * //int[] getSelectedColumns()
     * //返回所有选定列的索引。
     * //int[] getSelectedRows()
     * //返回所有选定行的索引。
     * //Object getValueAt(int row, int column)
     * //返回 row 和 column 位置的单元格值。
     */
    public MyRender() {
        //按钮
        editBtn = new JButton("修改");
        viewBtn = new JButton("查看");
        delBtn = new JButton("删除");
        btnPanel = new JPanel();
        defaultPanel = new JPanel();
        Box box = new Box(BoxLayout.X_AXIS);
        box.add(viewBtn);
        box.add(editBtn);
        box.add(delBtn);
        btnPanel.add(box);
    }

    /**
     * Sets an initial <code>value</code> for the editor.  This will cause
     * the editor to <code>stopEditing</code> and lose any partially
     * edited value if the editor is editing when this method is called. <p>
     * <p>
     * Returns the component that should be added to the client's
     * <code>Component</code> hierarchy.  Once installed in the client's
     * hierarchy this component will then be able to draw and receive
     * user input.
     *
     * @param table      the <code>JTable</code> that is asking the
     *                   editor to edit; can be <code>null</code>
     * @param value      the value of the cell to be edited; it is
     *                   up to the specific editor to interpret
     *                   and draw the value.  For example, if value is
     *                   the string "true", it could be rendered as a
     *                   string or it could be rendered as a check
     *                   box that is checked.  <code>null</code>
     *                   is a valid value
     * @param isSelected true if the cell is to be rendered with
     *                   highlighting
     * @param row        the row of the cell being edited
     * @param column     the column of the cell being edited
     * @return the component for editing
     */
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (!isSelected) {

            //未选中此行，点击面板的时候会变为空白
            logger.info("isSelected = [" + isSelected + "], row = [" + row + "], column = [" + column + "]");
            //让面板选中此行，没有选中，获取不到此行的columnModel，导致更新cell数据娶不到CellEditorComponent报错
            //TODO:solute
        }
        defaultPanel.setBackground(table.getSelectionBackground());
        if (null == table.getValueAt(row, 0) || StringUtils.isEmpty(table.getValueAt(row, 0).toString())) {
            return defaultPanel;
        }
        //设置背景和表格保持一致
        btnPanel.setBackground(table.getSelectionBackground());
        return btnPanel;
    }

    /**
     * Returns the value contained in the editor.
     *
     * @return the value contained in the editor
     */
    @Override
    public Object getCellEditorValue() {

        return btnPanel;
    }

    /**
     * Asks the editor if it can start editing using <code>anEvent</code>.
     * <code>anEvent</code> is in the invoking component coordinate system.
     * The editor can not assume the Component returned by
     * <code>getCellEditorComponent</code> is installed.  This method
     * is intended for the use of client to avoid the cost of setting up
     * and installing the editor component if editing is not possible.
     * If editing can be started this method returns true.
     *
     * @param anEvent the event the editor should use to consider
     *                whether to begin editing or not
     * @return true if editing can be started
     * @see #shouldSelectCell
     */
    @Override
    public boolean isCellEditable(EventObject anEvent) {
        return true;
    }

    /**
     * Returns true if the editing cell should be selected, false otherwise.
     * Typically, the return value is true, because is most cases the editing
     * cell should be selected.  However, it is useful to return false to
     * keep the selection from changing for some types of edits.
     * eg. A table that contains a column of check boxes, the user might
     * want to be able to change those checkboxes without altering the
     * selection.  (See Netscape Communicator for just such an example)
     * Of course, it is up to the client of the editor to use the return
     * value, but it doesn't need to if it doesn't want to.
     *
     * @param anEvent the event the editor should use to start
     *                editing
     * @return true if the editor would like the editing cell to be selected;
     * otherwise returns false
     * @see #isCellEditable
     */
    @Override
    public boolean shouldSelectCell(EventObject anEvent) {
        //JTable中的按钮有时需要点击两次才能有反应，这样可不行,此处需要为true
        return true;
    }

    /**
     * Tells the editor to stop editing and accept any partially edited
     * value as the value of the editor.  The editor returns false if
     * editing was not stopped; this is useful for editors that validate
     * and can not accept invalid entries.
     *
     * @return true if editing was stopped; false otherwise
     */
    @Override
    public boolean stopCellEditing() {
        return true;
    }

    /**
     * Tells the editor to cancel editing and not accept any partially
     * edited value.
     */
    @Override
    public void cancelCellEditing() {

    }

    /**
     * Adds a listener to the list that's notified when the editor
     * stops, or cancels editing.
     *
     * @param cellEditorListener the CellEditorListener
     */
    @Override
    public void addCellEditorListener(CellEditorListener cellEditorListener) {
        //编辑监听
        //设置点击的时候不隐藏面板显示空白
        //未选中此行，点击面板的时候会变为空白
        //让changeEvent去通知编辑器已经结束编辑
        //在editingStopped方法中，JTable调用getCellEditorValue()取回单元格的值，
        //getCellEditorComponent
        //并且把这个值传递给TableValues(TableModel)的setValueAt()
//        cellEditorListener.editingStopped(changeEvent);
//        cellEditorListener.editingStopped(changeEvent);
    }

    /**
     * Removes a listener from the list that's notified
     *
     * @param l the CellEditorListener
     */
    @Override
    public void removeCellEditorListener(CellEditorListener l) {

    }

    /**
     * Returns the component used for drawing the cell.  This method is
     * used to configure the renderer appropriately before drawing.
     * <p>
     * The <code>TableCellRenderer</code> is also responsible for rendering the
     * the cell representing the table's current DnD drop location if
     * it has one. If this renderer cares about rendering
     * the DnD drop location, it should query the table directly to
     * see if the given row and column represent the drop location:
     * <pre>
     *     JTable.DropLocation dropLocation = table.getDropLocation();
     *     if (dropLocation != null
     *             &amp;&amp; !dropLocation.isInsertRow()
     *             &amp;&amp; !dropLocation.isInsertColumn()
     *             &amp;&amp; dropLocation.getRow() == row
     *             &amp;&amp; dropLocation.getColumn() == column) {
     *
     *         // this cell represents the current drop location
     *         // so render it specially, perhaps with a different color
     *     }
     * </pre>
     * <p>
     * During a printing operation, this method will be called with
     * <code>isSelected</code> and <code>hasFocus</code> values of
     * <code>false</code> to prevent selection and focus from appearing
     * in the printed output. To do other customization based on whether
     * or not the table is being printed, check the return value from
     * {@link JComponent#isPaintingForPrint()}.
     *
     * @param table      the <code>JTable</code> that is asking the
     *                   renderer to draw; can be <code>null</code>
     * @param value      the value of the cell to be rendered.  It is
     *                   up to the specific renderer to interpret
     *                   and draw the value.  For example, if
     *                   <code>value</code>
     *                   is the string "true", it could be rendered as a
     *                   string or it could be rendered as a check
     *                   box that is checked.  <code>null</code> is a
     *                   valid value
     * @param isSelected true if the cell is to be rendered with the
     *                   selection highlighted; otherwise false
     * @param hasFocus   if true, render cell appropriately.  For
     *                   example, put a special border on the cell, if
     *                   the cell can be edited, render in the color used
     *                   to indicate editing
     * @param row        the row index of the cell being drawn.  When
     *                   drawing the header, the value of
     *                   <code>row</code> is -1
     * @param column     the column index of the cell being drawn
     * @see JComponent#isPaintingForPrint()
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        defaultPanel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
        if (null == table.getValueAt(row, 0) || StringUtils.isEmpty(table.getValueAt(row, 0).toString())) {
            return defaultPanel;
        }
        btnPanel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
        return btnPanel;
    }
}
