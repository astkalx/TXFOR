package com.astkalx.txfor.parser;

import com.astkalx.txfor.util.TagManager;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TableParser {
    private final TagManager tagManager;
    
    public TableParser(TagManager tagManager) {
        this.tagManager = tagManager;
    }
    
    public String parse(String input) {
        // Реализация парсинга таблиц будет добавлена позже
        // Сейчас просто заменяем основные теги
        return replaceBasicTags(input);
    }
    
    private String replaceBasicTags(String input) {
        // Замена простых тегов
        input = input.replace("\\hline", String.valueOf(tagManager.getBasicSymbol("hline")));
        input = input.replace("\\dhline", String.valueOf(tagManager.getDoubleSymbol("dhline")));
        input = input.replace("\\vline", String.valueOf(tagManager.getBasicSymbol("vline")));
        input = input.replace("\\dvline", String.valueOf(tagManager.getDoubleSymbol("dvline")));
        
        // Замена специальных символов
        input = input.replace("\\sqrt", String.valueOf(tagManager.getSpecialSymbol("sqrt")));
        input = input.replace("\\degree", String.valueOf(tagManager.getSpecialSymbol("degree")));
        input = input.replace("\\square", String.valueOf(tagManager.getSpecialSymbol("square")));
        input = input.replace("\\diamond", String.valueOf(tagManager.getSpecialSymbol("diamond")));
        input = input.replace("\\check", String.valueOf(tagManager.getSpecialSymbol("check")));
        input = input.replace("\\crossmark", String.valueOf(tagManager.getSpecialSymbol("crossmark")));
        
        return input;
    }
}