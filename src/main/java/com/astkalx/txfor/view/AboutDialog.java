package com.astkalx.txfor.view;

import javax.swing.*;
import java.awt.*;

public class AboutDialog extends JDialog {

    public AboutDialog(JFrame parent) {
        super(parent, "О программе", true);
        setSize(300, 200);
        setLocationRelativeTo(parent);
        setResizable(false);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel label = new JLabel("<html><center><h1>TXFOR</h1>" +
                "<p>Текстовый редактор</p>" +
                "<p>Версия 1.0</p>" +
                "<p>&copy; 2025</p></center></html>", SwingConstants.CENTER);
        
        panel.add(label, BorderLayout.CENTER);
        
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> dispose());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(panel);
    }
    
    public static void showDialog(JFrame parent) {
        AboutDialog dialog = new AboutDialog(parent);
        dialog.setVisible(true);
    }
}