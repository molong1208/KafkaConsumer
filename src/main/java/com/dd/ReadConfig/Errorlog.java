package com.dd.ReadConfig;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class Errorlog {
	private static String filename=null;
	public Errorlog()
	{
		Date today=new Date(0);
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		filename=format.format(today);
	}
	public static void isExist(String name)
	{
		File file=new File(name);
		if(!file.exists())
			file.mkdirs();
	}
	
	public static void isfileexist(String filename)
	{
		File file=new File(filename);
		if(!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	public static void getfilename()
	{
		Date today=new Date();
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		String name=format.format(today);
		filename="./Log/"+"Error_"+name+".txt";
		isExist("./Log");
		isfileexist(filename);
		//return filename;
	}
	public static void writeerror(String out,String file_name)
	{
		Date time=new Date();
		DateFormat format=new SimpleDateFormat("HH:mm:ss");
		String nowtime=format.format(time);
		if(file_name.length()==0)
			getfilename();
		else
		{
			filename="./Log/"+file_name;
			isfileexist(filename);
		}

			try {
				 FileWriter f = new FileWriter(filename,true);
				 if(file_name.length()==0)
					 f.write(nowtime+"\t"+out+"\r\n");
				 else
					 f.write(out+"\r\n");
				 f.close();
				} catch (IOException e) {
							// TODO Auto-generated catch block
						//e.printStackTrace();
						writeerror(e);
										}
		
		
	}
	
	public static void writeerror(String out,String file_name,boolean append)
	{
		Date time=new Date();
		DateFormat format=new SimpleDateFormat("HH:mm:ss");
		String nowtime=format.format(time);
		if(file_name.length()==0)
			getfilename();
		else
		{
			filename=file_name;
			isfileexist(filename);
		}

			try {
				 FileWriter f = new FileWriter(filename,append);
				 if(file_name.length()==0)
					 f.write(nowtime+"\t"+out+"\r\n");
				 else
					 f.write(out+"\r\n");
				 f.close();
				} catch (IOException e) {
							// TODO Auto-generated catch block
						//e.printStackTrace();
						writeerror(e);
										}
		
		
	}
	public static void writeerror(Exception e)
	{
		Date time=new Date();
		DateFormat format=new SimpleDateFormat("HH:mm:ss");
		String nowtime=format.format(time);
		getfilename();
		try
		{
			FileWriter f=new FileWriter(filename,true);
			PrintWriter p=new PrintWriter(f);
			p.println();
			p.println(nowtime);
			p.println(e.toString());
			e.printStackTrace(p);
			p.println();
			p.flush();
		}catch(IOException e1)
		{
			e1.printStackTrace();
		}
	}

}
