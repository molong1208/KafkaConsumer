package com.dd.ReadConfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Set;

public class ReadConfig {
	public Set<String>  ReadFile(String fileName)
	{
	    File file = new File(fileName);
	    BufferedReader reader = null;
	    Set<String> ret = new HashSet<String>();
	    try {
	      System.out.println("以行为单位读取文件内容，一次读一整行：");
	      reader = new BufferedReader(new FileReader(file));
	      String tempString = null;
	      int line = 1;
	      long start=System.currentTimeMillis();
	      while ((tempString = reader.readLine()) != null) {
	    	  if(!tempString.startsWith("#"))
	    	  {
	    		  String[] split_string = tempString.split("\t");
	    		  ret.add(split_string[0]);
	    		  
	    	  }
	        line++;
	      }
	      reader.close();
	      long end=System.currentTimeMillis();
	    } catch (IOException e) {
	      e.printStackTrace();
	    } finally {
	      if (reader != null) {
	        try {
	          reader.close();
	        } catch (IOException e1) {
	        }
	      }
	    }
	    return ret;
	}

}
