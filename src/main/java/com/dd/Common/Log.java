package com.dd.Common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.Logger;

import com.dd.Common.Constants;



public class Log {

    private static SimpleDateFormat formatFileName = new SimpleDateFormat("yyyyMMdd");
    private static SimpleDateFormat formatLogTime = new SimpleDateFormat("yyyy_MM_dd HH:mm:ss");
    private static String logFileSuffix = "_error.txt";
    private static Logger logger = Logger.getLogger(Log.class);

    //write error log into yyyymmdd_error.txt
    //one log file one day
    public static void writeErrorMsg(Class obj, Exception e, String desc) {
    	
        FileWriter fw = null;
        try {
            fw = new FileWriter(getFilePath(), true);
            fw.write(Constants.SYS_LINE_SEP);
            fw.write("LogTime=" + formatLogTime.format(Calendar.getInstance().getTime()));
            fw.write(Constants.SYS_LINE_SEP);
            fw.write("==================================================================");
            fw.write(Constants.SYS_LINE_SEP);
            fw.write("ERROR OCCOURED IN:" + obj.getName());
            fw.write(Constants.SYS_LINE_SEP);
            fw.write("ERROR DESCRPTION:");
            fw.write(desc);
            fw.write(e.getLocalizedMessage());
            fw.write(Constants.SYS_LINE_SEP);
            fw.write("ERROR DESCRPTION:");
            StackTraceElement[] trace = e.getStackTrace();
            for (StackTraceElement s : trace) {
                fw.write("\tat ");
                fw.write(s.toString());
                fw.write(Constants.SYS_LINE_SEP);
            }
            fw.write("==================================================================");
            fw.write(Constants.SYS_LINE_SEP);
        } catch (IOException ex) {
            logErrorMsg(desc, ex);
        } finally {
            try {
                fw.close();
            } catch (IOException ex) {
                logErrorMsg("", ex);
            }
            logErrorMsg("", e);
        }
    }
    //write error log into yyyymmdd_error.txt
    //one log file one day

    public static void writeErrorMsg(Class obj, String desc) {
    	
        FileWriter fw = null;
        try {

            fw = new FileWriter(getFilePath(), true);
            fw.write(Constants.SYS_LINE_SEP);
            fw.write("LogTime=" + formatLogTime.format(Calendar.getInstance().getTime()));
            fw.write(Constants.SYS_LINE_SEP);
            fw.write("==================================================================");
            fw.write(Constants.SYS_LINE_SEP);
            fw.write("ERROR OCCOURED IN:" + obj.getName());
            fw.write(Constants.SYS_LINE_SEP);
            fw.write("ERROR DESCRPTION:");
            fw.write(desc);
            fw.write(Constants.SYS_LINE_SEP);
            fw.write("ERROR DESCRPTION:");
            fw.write(Constants.SYS_LINE_SEP);
            fw.write("==================================================================");
            fw.write(Constants.SYS_LINE_SEP);
        } catch (IOException ex) {
            logErrorMsg("", ex);
        } finally {
            try {
                fw.close();
            } catch (IOException ex1) {
                logErrorMsg("", ex1);
            }
            logErrorMsg(desc);
        }
    }

    public static void logFatalMsg(String desc, Exception e) {
        logger.fatal(desc, e);
    }

    public static void logErrorMsg(String desc, Exception e) {
        logger.error(desc, e);
    }

    public static void logErrorMsg(String desc) {
        logger.error(desc);
    }

    public static void logWarnMsg(String desc) {
        logger.warn(desc);
    }

    public static void logInfoMsg(String msg) {
        logger.info(msg);
    }

    public static void logDebugMsg(String msg) {
        logger.debug(msg);
    }

    private static String getFilePath() throws IOException {
    	
        String fileName = formatFileName.format(Calendar.getInstance().getTime()).toString();
        String fullName = Constants.SYS_LOG_PATH.concat(fileName + logFileSuffix);
        File logFloder = new File(Constants.SYS_LOG_PATH);
        File logFile = new File(fullName);
        if (!logFloder.exists()) {
            logFloder.mkdir();
        }
        if (!logFile.exists()) {
            logFile.createNewFile();
        }
        return fullName;
    }
    
    public static void logTime(String desc) {
        FileWriter fw = null;

        try {
            fw = new FileWriter(getFilePath(), true);
            fw.write(desc);
            fw.write(Constants.SYS_LINE_SEP);
        } catch (IOException ex) {
            logErrorMsg(desc, ex);
        } finally {   
            try {
                fw.close();
            } catch (IOException ex) {
            }
        }
    }
}
