package com.astkalx.txfor.util;

import java.io.File;
import org.ini4j.Ini;
import org.ini4j.IniPreferences;
import java.util.prefs.Preferences;

public class TagManager {
    private final Preferences prefs;
    
    public TagManager() {
        this.prefs = null;
    }
    
    public TagManager(String configPath) throws Exception {
        Ini ini = new Ini(new File(configPath));
        this.prefs = new IniPreferences(ini);
    }
    
    public char getSymbol(String section, String key) {
        if (prefs == null) return '?';
        
        String value = prefs.node(section).get(key, "");
        if (value.startsWith("\\u")) {
            return (char) Integer.parseInt(value.substring(2), 16);
        }
        return value.charAt(0);
    }
    
    public char getJoint(String type) {
        return getSymbol("joints", type);
    }
    
    public char getBasicSymbol(String key) {
        return getSymbol("basic", key);
    }
    
    public char getDoubleSymbol(String key) {
        return getSymbol("double", key);
    }
    
    public char getSpecialSymbol(String key) {
        return getSymbol("symbols", key);
    }
}