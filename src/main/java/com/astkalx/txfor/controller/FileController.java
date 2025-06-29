package com.astkalx.txfor.controller;

import com.astkalx.txfor.model.AppState;
import com.astkalx.txfor.model.Document;
import com.astkalx.txfor.util.FileUtils;
import java.io.File;
import javax.swing.*;

public class FileController {
    private final AppState state;
    private final Document leftDocument;
    
    public FileController(AppState state, Document leftDocument) {
        this.state = state;
        this.leftDocument = leftDocument;
    }
    
    public void newFile() {
        if (!confirmUnsavedChanges()) return;
        leftDocument.content = "";
        state.currentFile = null;
        state.isModified = false;
    }
    
    public void openFile(JFrame parent) {
        if (!confirmUnsavedChanges()) return;
        
        JFileChooser fileChooser = FileUtils.createFileChooser();
        fileChooser.setDialogTitle("Открыть файл");
        
        if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                FileUtils.loadFile(selectedFile, state, leftDocument);
                
                // Устанавливаем курсор в начало документа
                SwingUtilities.invokeLater(() -> {
                    if (parent instanceof com.astkalx.txfor.Txfor) {
                        ((com.astkalx.txfor.Txfor) parent).resetCaretPosition();
                    }
                });
            } catch (Exception ex) {
                showError(parent, "Ошибка при открытии файла", ex);
            }
        }
    }
    
    public void saveFile(JFrame parent) {
        if (state.currentFile == null) {
            saveAsFile(parent);
        } else {
            try {
                FileUtils.saveToFile(state.currentFile, state, leftDocument);
            } catch (Exception ex) {
                showError(parent, "Ошибка при сохранении файла", ex);
            }
        }
    }
    
    public void saveAsFile(JFrame parent) {
        JFileChooser fileChooser = FileUtils.createFileChooser();
        fileChooser.setDialogTitle("Сохранить как");
        fileChooser.setSelectedFile(new File("document.txm"));
        
        if (fileChooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (!selectedFile.getName().contains(".")) {
                selectedFile = new File(selectedFile.getAbsolutePath() + ".txm");
            }
            
            try {
                FileUtils.saveToFile(selectedFile, state, leftDocument);
            } catch (Exception ex) {
                showError(parent, "Ошибка при сохранении файла", ex);
            }
        }
    }
    
    public boolean confirmUnsavedChanges() {
        if (!state.isModified) return true;
        
        int option = JOptionPane.showConfirmDialog(
            null,
            "Документ содержит несохраненные изменения. Сохранить?",
            "Подтверждение",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (option == JOptionPane.YES_OPTION) {
            return true;
        }
        return option == JOptionPane.NO_OPTION;
    }
    
    private void showError(JFrame parent, String title, Exception ex) {
        JOptionPane.showMessageDialog(parent,
            ex.getMessage(),
            title,
            JOptionPane.ERROR_MESSAGE);
    }
}