package com.astkalx.txfor.model;

import java.io.File;
import java.nio.charset.Charset;

public class AppState {
    public File currentFile = null;
    public boolean isModified = false;
    public Charset currentCharset;
    public double dividerRatio = 0.5;
}