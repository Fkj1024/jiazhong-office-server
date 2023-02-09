package com.jiazhong.exctions;

public class SysException extends Exception{
    public SysException() {
        super("系统升级中...");
    }

    public SysException(String message) {
        super(message);
    }
}
