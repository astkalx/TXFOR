package com.astkalx.txfor.util;

import com.astkalx.txfor.model.AppState;
import com.astkalx.txfor.model.Document;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.nio.charset.UnsupportedCharsetException;

public class FileUtils {
    public static JFileChooser createFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        
        // Фильтр только для текстовых файлов
        FileNameExtensionFilter txmFilter = new FileNameExtensionFilter(
            "Текстовые файлы (*.txm, *.txt)", "txm", "txt");
        
        fileChooser.setFileFilter(txmFilter);
        
        return fileChooser;
    }
    
    public static void loadFile(File file, AppState state, Document document) 
            throws UnsupportedCharsetException, IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), state.currentCharset))) {
            
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            
            // Убираем лишний перенос строки в конце файла
            if (content.length() > 0 && content.charAt(content.length() - 1) == '\n') {
                content.setLength(content.length() - 1);
            }
            
            document.content = content.toString();
            state.currentFile = file;
            state.isModified = false;
        }
    }
    
    public static void saveToFile(File file, AppState state, Document document) 
            throws UnsupportedCharsetException, IOException {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), state.currentCharset))) {
            
            writer.write(document.content);
            state.currentFile = file;
            state.isModified = false;
        }
    }
}