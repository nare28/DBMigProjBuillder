package com.dbmigproj.builder.utils;

import java.io.File;

public class Constants {
	
	public static int OBJTYPE_TABLES = 1;
	public static int OBJTYPE_SEQ = 2;
	public static int OBJTYPE_CONSTRAINTS = 3;
	public static int OBJTYPE_SP = 4;
	
	public static int DIRTYPE_INSTANCES = 1;
	public static int DIRTYPE_DB = 2;
	public static int DIRTYPE_SCHEMA = 3;
	
	public static String LINE_SEPERATOR = System.lineSeparator();
	public static String FILE_SEPERATOR = File.separator;
	
	public static final String DOT_DELIM = ".";

	public static final String COLON_SPACE_DELIM = "; ";

	public static final String COMMA_SPACE_DELIM = ", ";
}

