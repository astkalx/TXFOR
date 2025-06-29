package com.astkalx.txfor;

import com.astkalx.txfor.controller.EncodingController;
import com.astkalx.txfor.controller.FileController;
import com.astkalx.txfor.model.AppState;
import com.astkalx.txfor.model.Document;
import com.astkalx.txfor.parser.TableParser;
import com.astkalx.txfor.util.TagManager;
import com.astkalx.txfor.view.EditorPanel;
import com.astkalx.txfor.view.MainMenu;
import com.astkalx.txfor.view.StatusBar;
import com.astkalx.txfor.view.ToolBar;
import com.astkalx.txfor.view.AboutDialog;

import javax.swing.*;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.*;
import java.nio.charset.Charset;

public class Txfor extends JFrame {
    private final AppState state = new AppState();
    private final Document leftDocument = new Document();
    private final Document rightDocument = new Document();
    
    private final FileController fileController;
    private final EncodingController encodingController;
    private final TagManager tagManager;
    private final TableParser tableParser;
    
    private final EditorPanel leftPanel;
    private final EditorPanel rightPanel;
    private final JSplitPane splitPane;
    private final StatusBar statusBar;
    
    private Point leftScrollPosition;
    private Point rightScrollPosition;
    
    private final UndoManager undoManager = new UndoManager();
    
    public Txfor() {
        // Устанавливаем кодировку по умолчанию
        state.currentCharset = Charset.forName("CP866");
        
        // Инициализация TagManager и TableParser
        tagManager = createTagManager();
        tableParser = new TableParser(tagManager);
        
        // Инициализация контроллеров
        fileController = new FileController(state, leftDocument);
        encodingController = new EncodingController(state, leftDocument, rightDocument);
        
        // Настройка окна
        setTitle("TXFOR - Текстовый редактор");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitApplication();
            }
        });
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        // Создание компонентов
        leftPanel = new EditorPanel(true, "");
        rightPanel = new EditorPanel(false, "Только для чтения");
        
        splitPane = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            leftPanel,
            rightPanel
        );
        splitPane.setResizeWeight(state.dividerRatio);
        splitPane.setDividerLocation(state.dividerRatio);
        splitPane.setContinuousLayout(true);
        splitPane.setOneTouchExpandable(true);
        
        statusBar = new StatusBar();
        
        // Создание меню и панели инструментов
        MainMenu menu = new MainMenu(this::handleFileAction, this::handleEncodingAction, this);
        setJMenuBar(menu);
        
        ToolBar toolBar = new ToolBar(this::handleFileAction);
        
        // Компоновка интерфейса
        Container contentPane = getContentPane();
        contentPane.add(toolBar, BorderLayout.NORTH);
        contentPane.add(splitPane, BorderLayout.CENTER);
        contentPane.add(statusBar, BorderLayout.SOUTH);
        
        // Обработка изменений текста
        leftPanel.textArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { 
                state.isModified = true;
                updateTitle(); 
                updatePreview();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { 
                state.isModified = true;
                updateTitle(); 
                updatePreview();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { 
                state.isModified = true;
                updateTitle(); 
                updatePreview();
            }
        });
        
        // Инициализация менеджера отмены
        leftPanel.textArea.getDocument().addUndoableEditListener(e -> {
            undoManager.addEdit(e.getEdit());
            updateUndoRedoState();
        });
        
        // Инициализация горячих клавиш
        initKeyBindings();
        
        // Инициализируем позиции прокрутки
        leftScrollPosition = new Point(0, 0);
        rightScrollPosition = new Point(0, 0);
        
        // Обновление интерфейса
        updateTitle();
        statusBar.updateEncoding(state.currentCharset.displayName());
    }
    
    private TagManager createTagManager() {
        try {
            // Путь к конфигурации внутри JAR
            String configPath = "config/tags.ini";
            return new TagManager(configPath);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Ошибка загрузки конфигурации тегов: " + e.getMessage(),
                "Ошибка", JOptionPane.ERROR_MESSAGE);
            return new TagManager(); // Создаем пустой TagManager
        }
    }
    
    private void initKeyBindings() {
        JTextArea textArea = leftPanel.textArea;
        InputMap inputMap = textArea.getInputMap(JComponent.WHEN_FOCUSED);
        ActionMap actionMap = textArea.getActionMap();

        // Новый файл
        inputMap.put(KeyStroke.getKeyStroke("control N"), "new");
        actionMap.put("new", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleFileAction(new ActionEvent(e.getSource(), e.getID(), "new"));
            }
        });

        // Открыть
        inputMap.put(KeyStroke.getKeyStroke("control O"), "open");
        actionMap.put("open", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleFileAction(new ActionEvent(e.getSource(), e.getID(), "open"));
            }
        });

        // Сохранить
        inputMap.put(KeyStroke.getKeyStroke("control S"), "save");
        actionMap.put("save", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleFileAction(new ActionEvent(e.getSource(), e.getID(), "save"));
            }
        });

        // Отменить
        inputMap.put(KeyStroke.getKeyStroke("control Z"), "undo");
        actionMap.put("undo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleFileAction(new ActionEvent(e.getSource(), e.getID(), "undo"));
            }
        });

        // Повторить
        inputMap.put(KeyStroke.getKeyStroke("control Y"), "redo");
        actionMap.put("redo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleFileAction(new ActionEvent(e.getSource(), e.getID(), "redo"));
            }
        });

        // Вырезать
        inputMap.put(KeyStroke.getKeyStroke("control X"), "cut");
        actionMap.put("cut", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleFileAction(new ActionEvent(e.getSource(), e.getID(), "cut"));
            }
        });

        // Копировать
        inputMap.put(KeyStroke.getKeyStroke("control C"), "copy");
        actionMap.put("copy", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleFileAction(new ActionEvent(e.getSource(), e.getID(), "copy"));
            }
        });

        // Вставить
        inputMap.put(KeyStroke.getKeyStroke("control V"), "paste");
        actionMap.put("paste", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleFileAction(new ActionEvent(e.getSource(), e.getID(), "paste"));
            }
        });
    }
    
    private void updateTitle() {
        String title = "TXFOR";
        if (state.currentFile != null) {
            title = state.currentFile.getName() + " - " + title;
        }
        if (state.isModified) {
            title = "* " + title;
        }
        title += " [" + state.currentCharset.displayName() + "]";
        setTitle(title);
    }
    
    private void updatePreview() {
        String content = leftPanel.textArea.getText();
        String preview = tableParser.parse(content);
        rightPanel.textArea.setText(preview);
    }
    
    public void resetCaretPosition() {
        leftPanel.textArea.setCaretPosition(0);
        leftPanel.getViewport().setViewPosition(new Point(0, 0));
    }
    
    private void saveScrollPositions() {
        leftScrollPosition = leftPanel.getViewport().getViewPosition();
        rightScrollPosition = rightPanel.getViewport().getViewPosition();
    }
    
    private void restoreScrollPositions() {
        SwingUtilities.invokeLater(() -> {
            leftPanel.getViewport().setViewPosition(leftScrollPosition);
            rightPanel.getViewport().setViewPosition(rightScrollPosition);
        });
    }
    
    private void handleFileAction(ActionEvent e) {
        String command = e.getActionCommand();
        
        switch (command) {
            case "new":
                fileController.newFile();
                leftPanel.textArea.setText("");
                break;
            case "open":
                fileController.openFile(this);
                leftPanel.textArea.setText(leftDocument.content);
                updatePreview();
                break;
            case "save":
                leftDocument.content = leftPanel.textArea.getText();
                fileController.saveFile(this);
                break;
            case "saveAs":
                leftDocument.content = leftPanel.textArea.getText();
                fileController.saveAsFile(this);
                break;
            case "exit":
                exitApplication();
                break;
            case "cut":
                leftPanel.textArea.cut();
                break;
            case "copy":
                leftPanel.textArea.copy();
                break;
            case "paste":
                leftPanel.textArea.paste();
                break;
            case "wordWrap":
                boolean wrap = ((JCheckBoxMenuItem) e.getSource()).isSelected();
                leftPanel.textArea.setLineWrap(wrap);
                leftPanel.textArea.setWrapStyleWord(wrap);
                break;
            case "resetSplit":
                splitPane.setDividerLocation(0.5);
                break;
            case "copyFromRight":
                String selectedText = rightPanel.textArea.getSelectedText();
                if (selectedText != null) {
                    leftPanel.textArea.replaceSelection(selectedText);
                }
                break;
            case "undo":
                if (undoManager.canUndo()) {
                    undoManager.undo();
                }
                break;
            case "redo":
                if (undoManager.canRedo()) {
                    undoManager.redo();
                }
                break;
        }
        
        updateTitle();
    }
    
    private void handleEncodingAction(ActionEvent e) {
        saveScrollPositions();
        
        String charsetName = e.getActionCommand().split(":")[1];
        encodingController.changeEncoding(charsetName);
        
        leftPanel.textArea.setText(leftDocument.content);
        rightPanel.textArea.setText(rightDocument.content);
        
        restoreScrollPositions();
        
        updateTitle();
        statusBar.updateEncoding(state.currentCharset.displayName());
    }
    
    private void exitApplication() {
        if (fileController.confirmUnsavedChanges()) {
            System.exit(0);
        }
    }
    
    private void updateUndoRedoState() {
        // Можно добавить активацию/деактивацию кнопок
    }
    
    public void changeLookAndFeel(String styleName) {
        try {
            String lookAndFeel = "";
            switch (styleName) {
                case "Metal":
                    lookAndFeel = "javax.swing.plaf.metal.MetalLookAndFeel";
                    break;
                case "Motif":
                    lookAndFeel = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
                    break;
                case "Nimbus":
                    lookAndFeel = "javax.swing.plaf.nimbus.NimbusLookAndFeel";
                    break;
                case "Windows":
                    lookAndFeel = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
                    break;
            }
            UIManager.setLookAndFeel(lookAndFeel);
            SwingUtilities.updateComponentTreeUI(this);
            pack();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                new Txfor().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}