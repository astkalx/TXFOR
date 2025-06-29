package com.astkalx.txfor.view;

import javax.swing.*;
import javax.swing.border.BevelBorder;

public class StatusBar extends JLabel {
    public StatusBar() {
        super(" Готово");
        setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
    }
    
    public void updateEncoding(String encoding) {
        setText(" Кодировка: " + encoding);
    }
}