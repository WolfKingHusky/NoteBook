package com.huang.notetool.util;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * 限制键盘输入
 *
 * @author huang
 * @date 2018-12-03
 */
public class LimitKeyIn extends KeyAdapter {

    @Override
    public void keyTyped(KeyEvent e) {
        // 限制输入 [;] 和 [；] --> LimitKeyIn
        if (e.getKeyChar() == ';' || e.getKeyChar() == '；') {
            e.consume();
        }
    }
}
