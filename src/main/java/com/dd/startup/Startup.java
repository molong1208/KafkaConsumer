package com.dd.startup;

import org.apache.log4j.PropertyConfigurator;

import com.dd.Common.SystemConfig;

import TestKafka.first.StartupSimple;

public class Startup{

    public static void main(String[] args) {
  	  	PropertyConfigurator.configure("./conf/log4j.properties");	
    	StartupSimple.StartProcess(SystemConfig.MODE);
    }
    
}