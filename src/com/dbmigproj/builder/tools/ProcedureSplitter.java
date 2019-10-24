package com.dbmigproj.builder.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProcedureSplitter {

	public static final String LOWER_PATTERN = "(?i)ALTER TABLE \\((((')?(\\%)?(\\w+\\.)?\\w+(\\%)?(')?)|'')\\)";
	public static final String UPPER_PATTERN = "(?i)UPPER\\((((')?(\\%)?(\\w+\\.)?\\w+(\\%)?(')?)|'')\\)";
	
	public ProcedureSplitter() {
		// TODO Auto-generated constructor stub
	}
	
	public void split() {
		String output = null;
        String s =  
        "begin{comment}\n"+
        "this block should be removed\n"+
        "i.e. it need to be replaced\n"+
        "end{comment}\n"+
        "begin{comment}\n"+
        "this block should remains.\n"+
        "end{comment}\n"+
        "begin{comment}\n"+
        "this should be removed too.\n"+
        "end{comment}\n" +
        "begin{comment}\n"+
        "end{comment}\n";
        System.out.println();
        System.out.println(s);
        System.out.println();
//        Matcher m = Pattern.compile("\\\\begin\\{comment}(?s).*?\\\\end\\{comm.*?\\}").matcher(s);
        Matcher m = Pattern.compile("begin\\{comment}(?s).*?end\\{comment}").matcher(s);
        while(m.find())
        {
            System.out.println(m.group(0));
//            output = m.replaceAll("");
           // m.groupCount()
            //System.out.println(output);
        }

//        m = Pattern.compile("\\begin").matcher(s);
//        while(m.find())
//        {
////            System.out.println(m.group(0));
//            output = m.replaceAll("");
//            System.out.println(output);
//        }
	}

}
