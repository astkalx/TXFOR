package com.astkalx.txfor.view;

import javax.swing.*;
import java.awt.event.ActionListener;

public class ToolBar extends JToolBar {
    public ToolBar(ActionListener listener) {
        setFloatable(false);
        
        addButton("Новый", "new", listener);
        addButton("Открыть", "open", listener);
        addButton("Сохранить", "save", listener);
        addSeparator();
        
        addButton("Отменить", "undo", listener);
        addButton("Повторить", "redo", listener);
        addSeparator();
        
        addButton("Вырезать", "cut", listener);
        addButton("Копировать", "copy", listener);
        addButton("Вставить", "paste", listener);
        addSeparator();
        
        addButton("Копировать из правого", "copyFromRight", listener);
    }
    
    private void addButton(String text, String command, ActionListener listener) {
        JButton button = new JButton(text);
        button.setActionCommand(command);
        button.addActionListener(listener);
        add(button);
    }
}