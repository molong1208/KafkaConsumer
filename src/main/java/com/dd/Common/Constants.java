/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dd.Common;


public class Constants {
    public static final String SYS_ROOT_PATH = System.getProperty("user.dir");
    public static final String SYS_LINE_SEP = System.getProperty("line.separator");
    public static final String SYS_FILE_SEP = System.getProperty("file.separator");
    public static final String SYS_CONF_PATH = SYS_ROOT_PATH.concat(SYS_FILE_SEP).concat("conf").concat(SYS_FILE_SEP);
    public static final String SYS_LOG_PATH = SYS_ROOT_PATH.concat(SYS_FILE_SEP).concat("log").concat(SYS_FILE_SEP);
}
