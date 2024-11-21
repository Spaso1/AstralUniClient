package org.au.client.utill;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class CustomTextArea extends JTextArea {

    public CustomTextArea(int i, int i1) {
        super(i, i1);
    }

    @Override
    protected void processKeyEvent(KeyEvent e) {
        if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            if (getText().isEmpty() || getCaretPosition() == 0) {
                // 如果文本为空或光标在最开始位置，不处理 Backspace 键
                return;
            }
        }
        super.processKeyEvent(e);
    }
}
