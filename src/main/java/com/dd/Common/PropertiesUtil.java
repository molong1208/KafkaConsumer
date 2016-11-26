package com.dd.Common;


import com.dd.Common.Constants;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;


public class PropertiesUtil {
	
    private static HashMap hs;
    private static PropertiesUtil prpUtil = new PropertiesUtil();

    private PropertiesUtil() {
        hs = new HashMap();
        hs.put("sys", "sysconfig.properties");
        hs.put("db", "dbconfig.properties");
        hs.put("index", "indexconfig.properties");
    }

    private Properties getProperties(String fileAlias) {
        Properties prp = new Properties();
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(Constants.SYS_CONF_PATH.concat((String) hs.get(fileAlias)));
            prp.load(fis);
        } catch (IOException e) {
            Log.writeErrorMsg(getClass(), e, "");
        } catch (Exception ex) {
            Log.writeErrorMsg(getClass(), ex, "");
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ex) {
                    Log.writeErrorMsg(getClass(), ex, "IOException");
                }
            }
        }
        return prp;
    }

    public String getStringValue(String fileAlias, String name) {
        Properties prp = getProperties(fileAlias);
        return prp.getProperty(name);
    }

    public int getIntValue(String fileAlias, String name) {
        Properties prp = getProperties(fileAlias);
        return Convert.toInteger(prp.getProperty(name));
    }

    public long getLongValue(String fileAlias, String name) {
        Properties prp = getProperties(fileAlias);
        return Convert.toLong(prp.getProperty(name));
    }

    public static PropertiesUtil getInstance() {
        return prpUtil;
    }
}
