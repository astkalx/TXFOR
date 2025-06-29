package com.astkalx.txfor.view;

import javax.swing.*;
import java.awt.*;

public class EditorPanel extends JScrollPane {
    public final JTextArea textArea;
    
    public EditorPanel(boolean editable, String tooltip) {
        textArea = new JTextArea();
        textArea.setFont(new Font("DejaVu Sans Mono", Font.PLAIN, 14));
        textArea.setTabSize(4);
        textArea.setLineWrap(false);
        textArea.setWrapStyleWord(false);
        textArea.setEditable(editable);
        
        if (!editable) {
            textArea.setBackground(new Color(240, 240, 240));
            textArea.setForeground(new Color(80, 80, 80));
            textArea.setToolTipText(tooltip);
        }
        
        setViewportView(textArea);
    }
}