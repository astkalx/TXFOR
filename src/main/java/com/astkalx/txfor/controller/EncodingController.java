package com.astkalx.txfor.controller;

import com.astkalx.txfor.model.AppState;
import com.astkalx.txfor.model.Document;
import java.nio.charset.Charset;
import javax.swing.JOptionPane;

public class EncodingController {
    private final AppState state;
    private final Document leftDocument;
    private final Document rightDocument;
    
    public EncodingController(AppState state, Document leftDocument, Document rightDocument) {
        this.state = state;
        this.leftDocument = leftDocument;
        this.rightDocument = rightDocument;
    }
    
    public void changeEncoding(String charsetName) {
        try {
            Charset newCharset = Charset.forName(charsetName);
            if (leftDocument != null && leftDocument.content != null) {
                // Конвертируем содержимое в новую кодировку
                byte[] bytes = leftDocument.content.getBytes(state.currentCharset);
                leftDocument.content = new String(bytes, newCharset);
            }
            if (rightDocument != null && rightDocument.content != null) {
                byte[] bytes = rightDocument.content.getBytes(state.currentCharset);
                rightDocument.content = new String(bytes, newCharset);
            }
            state.currentCharset = newCharset;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, 
                "Неподдерживаемая кодировка: " + charsetName, 
                "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }
}