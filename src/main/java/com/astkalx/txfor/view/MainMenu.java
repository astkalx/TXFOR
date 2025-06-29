package com.astkalx.txfor.view;

import com.astkalx.txfor.Txfor;
import javax.swing.*;
import java.awt.event.ActionListener;

public class MainMenu extends JMenuBar {
    public JCheckBoxMenuItem wordWrapItem;
    public JMenu encodingMenu;
    private ButtonGroup encodingGroup;
    
    public MainMenu(ActionListener fileListener, ActionListener encodingListener, Txfor frame) {
        // Меню "Файл"
        JMenu fileMenu = new JMenu("Файл");
        addMenuItem(fileMenu, "Новый", "control N", "new", fileListener);
        addMenuItem(fileMenu, "Открыть...", "control O", "open", fileListener);
        fileMenu.addSeparator();
        addMenuItem(fileMenu, "Сохранить", "control S", "save", fileListener);
        addMenuItem(fileMenu, "Сохранить как...", null, "saveAs", fileListener);
        fileMenu.addSeparator();
        addMenuItem(fileMenu, "Выход", "alt F4", "exit", fileListener);
        
        // Меню "Правка"
        JMenu editMenu = new JMenu("Правка");
        addMenuItem(editMenu, "Отменить", "control Z", "undo", fileListener);
        addMenuItem(editMenu, "Повторить", "control Y", "redo", fileListener);
        editMenu.addSeparator();
        addMenuItem(editMenu, "Вырезать", "control X", "cut", fileListener);
        addMenuItem(editMenu, "Копировать", "control C", "copy", fileListener);
        addMenuItem(editMenu, "Вставить", "control V", "paste", fileListener);
        
        // Меню "Вид"
        JMenu viewMenu = new JMenu("Вид");
        wordWrapItem = new JCheckBoxMenuItem("Перенос слов");
        wordWrapItem.setActionCommand("wordWrap");
        wordWrapItem.addActionListener(fileListener);
        viewMenu.add(wordWrapItem);
        viewMenu.addSeparator();
        addMenuItem(viewMenu, "Сбросить разделение", null, "resetSplit", fileListener);
        
        // Подменю для выбора стиля интерфейса
        JMenu styleMenu = new JMenu("Стиль интерфейса");
        viewMenu.add(styleMenu);
        String[] styles = {"Metal", "Motif", "Nimbus", "Windows"};
        for (String style : styles) {
            JMenuItem item = new JMenuItem(style);
            item.setActionCommand("style:" + style);
            item.addActionListener(e -> frame.changeLookAndFeel(style));
            styleMenu.add(item);
        }
        
        // Меню "Кодировки" с радио-кнопками
        encodingMenu = new JMenu("Кодировки");
        encodingGroup = new ButtonGroup();
        
        String[] charsets = {"CP866", "CP1251", "ISO-8859-5", "KOI8-R"};
        for (String charset : charsets) {
            JRadioButtonMenuItem item = new JRadioButtonMenuItem(charset);
            item.setActionCommand("encoding:" + charset);
            item.addActionListener(encodingListener);
            encodingGroup.add(item);
            encodingMenu.add(item);
            
            // Устанавливаем CP866 как выбранный по умолчанию
            if (charset.equals("CP866")) {
                item.setSelected(true);
            }
        }
        
        // Меню "Справка"
        JMenu helpMenu = new JMenu("?");
        JMenuItem aboutItem = new JMenuItem("О программе");
        aboutItem.addActionListener(e -> AboutDialog.showDialog(frame));
        helpMenu.add(aboutItem);
        
        // Сборка меню
        add(fileMenu);
        add(editMenu);
        add(viewMenu);
        add(encodingMenu);
        add(Box.createHorizontalGlue());
        add(helpMenu);
    }
    
    private void addMenuItem(JMenu menu, String text, String accelerator, String command, ActionListener listener) {
        JMenuItem item = new JMenuItem(text);
        if (accelerator != null) {
            item.setAccelerator(KeyStroke.getKeyStroke(accelerator));
        }
        item.setActionCommand(command);
        item.addActionListener(listener);
        menu.add(item);
    }
}