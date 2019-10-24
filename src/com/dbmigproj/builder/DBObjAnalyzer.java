package com.dbmigproj.builder;


import static com.dbmigproj.builder.utils.Constants.COLON_SPACE_DELIM;
import static com.dbmigproj.builder.utils.Constants.COMMA_SPACE_DELIM;
import static com.dbmigproj.builder.utils.Constants.DOT_DELIM;
import static com.dbmigproj.builder.utils.Constants.FILE_SEPERATOR;
import static com.dbmigproj.builder.utils.Constants.LINE_SEPERATOR;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DBObjAnalyzer {

	private static final String SOURCE_DIR = "C:\\Users\\narahna\\Documents\\SCT_Extn_Tool\\MyInfoSQL21\\";

	private static final String DESTINATION_DIR = "C:\\Users\\narahna\\Documents\\SCT_Extn_Tool\\MyInfoSQL4\\";

	private Map<String, List<String>> dbObjLocations = null;
	private Map<String, List<String>> dbObjComparision = null;

	private Map<String, Integer> dbObjCountBySchema = null;
	private Map<String, Integer> dbExtraObjCountBySchema = null;

	public static void main(String[] args) {
		DBObjAnalyzer pa = new DBObjAnalyzer();
		try {
			pa.startAnalyze(SOURCE_DIR, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void startAnalyze(String dirPath, int type) throws Exception {
		File parentDir = new File(dirPath);
		dbObjLocations = new HashMap<String, List<String>>();
		dbObjComparision = new HashMap<String, List<String>>();
		dbObjCountBySchema = new HashMap<String, Integer>();
		dbExtraObjCountBySchema = new HashMap<String, Integer>();
		if (parentDir.isDirectory()) {
			File[] files = parentDir.listFiles();
			analyzeSQLFiles(files);
		} else {
			throw new Exception("Please provide directory location");
		}
		generateObjOccurencesCSV();
		generateObjCountsCSV(dbObjCountBySchema, false);
		generateObjCountsCSV(dbExtraObjCountBySchema, true);
	}

	private BufferedWriter generateObjOccurencesCSV() throws IOException, Exception {
		BufferedWriter bw = null;
		FileWriter fw = null;
		Set<String> keys = dbObjLocations.keySet();
		Iterator<String> itr = keys.iterator();
		String key = null;
		List<String> dbSchemas = null;
		List<String> comparisions = null;

		File fl = new File(DESTINATION_DIR + "DBObjectsComparisionReport.csv");
		try {
			fl.createNewFile();
			fl.exists();
			fw = new FileWriter(fl);
			bw = new BufferedWriter(fw);

			bw.write("Object Name");
			bw.write(COMMA_SPACE_DELIM);
			bw.write("Type");
			bw.write(COMMA_SPACE_DELIM);
			bw.write("Occurrences");
			bw.write(COMMA_SPACE_DELIM);
			bw.write("Schemas");
			bw.write(COMMA_SPACE_DELIM);
			bw.write("Diff Evaluated");
			bw.write(COMMA_SPACE_DELIM);
			bw.write("Total Diffs");
			bw.write(COMMA_SPACE_DELIM);
			bw.write("Diff %");
			bw.write(COMMA_SPACE_DELIM);
			bw.write("Manual Diff");
			bw.write(LINE_SEPERATOR);
			while (itr.hasNext()) {
				key = itr.next();
				dbSchemas = dbObjLocations.get(key);
				comparisions = dbObjComparision.get(key);
				bw.write(key);
				bw.write(COMMA_SPACE_DELIM);
				bw.write(findObjType(key.toLowerCase()));
				bw.write(COMMA_SPACE_DELIM);
				bw.write(String.valueOf(dbSchemas.size()));
				bw.write(COMMA_SPACE_DELIM);
				bw.write(convertToString(dbSchemas, COLON_SPACE_DELIM));
				bw.write(COMMA_SPACE_DELIM);
				bw.write(convertToString(comparisions, COLON_SPACE_DELIM));
				bw.write(COMMA_SPACE_DELIM);
				bw.write(String.valueOf(comparisions.size()));
				bw.write(COMMA_SPACE_DELIM);
				bw.write(convertCompToString(comparisions, COLON_SPACE_DELIM));
				bw.write(LINE_SEPERATOR);
			}
		} catch (FileNotFoundException e) {
			throw new Exception(e);
		} finally {
			close(bw);
		}
		return bw;
	}

	private void generateObjCountsCSV(Map<String, Integer> dbObjCount, boolean extraData) throws IOException, Exception {
		BufferedWriter bw = null;
		FileWriter fw = null;
		String key = null;
		Set<String> keys = dbObjCount.keySet();
		Iterator<String> itr = keys.iterator();
		File fl = new File(DESTINATION_DIR + (extraData ? "DBExtraObjsCount.csv" : "DBObjectsCount.csv"));
		try {
			fl.createNewFile();
			fl.exists();
			fw = new FileWriter(fl);
			bw = new BufferedWriter(fw);
			bw.write("Instance");
			bw.write(COMMA_SPACE_DELIM);
			bw.write("Database");
			bw.write(COMMA_SPACE_DELIM);
			if(extraData == false) {
				bw.write("Schema");
				bw.write(COMMA_SPACE_DELIM);
			}
			bw.write("Type");
			bw.write(COMMA_SPACE_DELIM);
			bw.write("Count");
			bw.write(LINE_SEPERATOR);

			while (itr.hasNext()) {
				key = itr.next();
				bw.write(key.replace(DOT_DELIM, COMMA_SPACE_DELIM));
				bw.write(COMMA_SPACE_DELIM);
				bw.write(String.valueOf(dbObjCount.get(key)));
				bw.write(LINE_SEPERATOR);
			}
		} catch (FileNotFoundException e) {
			throw new Exception(e);
		} finally {
			close(bw);
		}
	}

	// private char findObjType(String key) {
	// char objType = ' ';
	// if (key.startsWith("sp"))
	// objType = 'P';
	// else if (key.startsWith("udf") || key.startsWith("fn"))
	// objType = 'F';
	// else if (key.startsWith("vw") || key.startsWith("tbl"))
	// objType = 'V';
	// else if (key.contains("_sp"))
	// objType = 'P';
	// else if (key.contains("_tbl") || key.contains("_qry"))
	// objType = 'V';
	// return objType;
	// }

	private char findObjType(String key) {
		char objType = ' ';
		if (key.startsWith("sp"))
			objType = 'P';
		else if (key.startsWith("udf"))
			objType = 'F';
		else if (key.startsWith("vw"))
			objType = 'V';
		else if (key.startsWith("tbl"))
			objType = 'T';
		return objType;
	}

	private String convertToString(List<String> dbSchemas, String delim) {
		StringBuffer buff = new StringBuffer();
		int i = 1;
		for (String sc : dbSchemas) {
			buff.append(i);
			buff.append(')');
			buff.append(sc);
			buff.append(delim);
			i++;
		}
		return buff.toString();
	}

	private String convertCompToString(List<String> objComparisions, String delim) {
		StringBuffer buff = new StringBuffer();
		int i = 1;
		for (String sc : objComparisions) {
			if (sc.endsWith(":0.0"))
				continue;
			buff.append(i);
			buff.append(')');
			buff.append(sc);
			buff.append(delim);
			i++;
		}
		if(objComparisions.size() == 0)
			buff.append("NA");
		else if (buff.length() == 0)
			buff.append("IDENTICAL");

		buff.append(COMMA_SPACE_DELIM);
		buff.append(String.valueOf(i - 1));

		return buff.toString();
	}

	private void analyzeSQLFiles(File[] files) throws Exception {
		for (File fl : files) {
			if (fl.isDirectory()) {
				analyzeSQLFiles(fl.listFiles());
			} else {
				analyzeDBObjects(fl);
			}
		}
	}

	private void analyzeDBObjects(File fl) throws Exception {

		String[] folders = fl.getCanonicalPath().split("\\\\");
		int index = folders.length;
		String objName = folders[--index];
		if (objName.endsWith(".sql") == false || objName.equals("errorscript.sql")) {
			System.err.println("Ignoring the file to process : " + fl.getCanonicalPath());
			return;
		}
		String objType = folders[--index];
		String schemaName = folders[--index];
		String dbName = folders[--index];
		String instanceName = folders[--index];
		String dbSchema = null;
		if (schemaName.equals("extra_objs")) {
			dbSchema = instanceName + DOT_DELIM + dbName + DOT_DELIM + objType;
			if (dbExtraObjCountBySchema.containsKey(dbSchema) == false) {
				dbExtraObjCountBySchema.put(dbSchema, 1);
			} else {
				dbExtraObjCountBySchema.put(dbSchema, dbExtraObjCountBySchema.get(dbSchema) + 1);
			}
		} else {
			if (dbObjLocations.containsKey(objName) == false) {
				dbObjLocations.put(objName, new ArrayList<String>());
				dbObjComparision.put(objName, new ArrayList<String>());
			}
			dbSchema = instanceName + DOT_DELIM + dbName + DOT_DELIM + schemaName + DOT_DELIM + objType;
			dbObjLocations.get(objName).add(dbSchema);
//			compareDBObjectsLineToLine(objName); 
			compareDBObjectsCharToChar(objName);
			if (dbObjCountBySchema.containsKey(dbSchema) == false) {
				dbObjCountBySchema.put(dbSchema, 1);
			} else {
				dbObjCountBySchema.put(dbSchema, dbObjCountBySchema.get(dbSchema) + 1);
			}
		}
	}
	
	private void compareDBObjectsLineToLine(String objName) {
		List<String> locations = dbObjLocations.get(objName);
		int lastItemIndex = locations.size() - 1;
		for (int i = 0; i < lastItemIndex; i++) {
			String lines = compareDBObjectLineToLine(objName, locations.get(lastItemIndex), locations.get(i));
			dbObjComparision.get(objName).add((i + 1) + "-" + (lastItemIndex + 1) + ":" + lines);
		}
	}

	private void compareDBObjectsCharToChar(String objName) {
		List<String> locations = dbObjLocations.get(objName);
		int lastItemIndex = locations.size() - 1;
		for (int i = 0; i < lastItemIndex; i++) {
			double percentage = compareDBObjectCharToChar(objName, locations.get(lastItemIndex), locations.get(i));
			dbObjComparision.get(objName).add((i + 1) + "-" + (lastItemIndex + 1) + ":" + percentage);
		}
	}

	private String compareDBObjectLineToLine(String objName, String sourceObjLoc, String destObjLoc) {
		File file1 = new File(SOURCE_DIR + sourceObjLoc.replace(DOT_DELIM, FILE_SEPERATOR) + FILE_SEPERATOR + objName);
		File file2 = new File(SOURCE_DIR + destObjLoc.replace(DOT_DELIM, FILE_SEPERATOR) + FILE_SEPERATOR + objName);
		FileReader fr1 = null;
		BufferedReader br1 = null;
		FileReader fr2 = null;
		BufferedReader br2 = null;
		StringBuffer buff = new StringBuffer();
		try {
			fr1 = new FileReader(file1);
			br1 = new BufferedReader(fr1);
			fr2 = new FileReader(file2);
			br2 = new BufferedReader(fr2);

			String line1 = null;
			String line2 = null;
			int lineNum=0;
			int maxLimit = 5;
			while ((line1 = br1.readLine()) != null && (line2 = br2.readLine()) != null) {
				lineNum++;
				//if (line1.trim().equalsIgnoreCase(line2.trim()) == false) {
				if (extractUnwantedCode(line1).equalsIgnoreCase(extractUnwantedCode(line2)) == false) {
					buff.append(lineNum);
					buff.append("#");
					maxLimit--;
				}
				if(maxLimit==0)
					break;
			}
			if(line1 == null)
				line2 = br2.readLine();
			if(line1 != null || line2 != null) {
				buff.append(++lineNum);
			}
			if(buff.length() == 0)
				buff.append("0.0");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close(br1);
			close(br2);
		}
		return buff.toString();
	}
	
	private String extractUnwantedCode(String code) {
		code = code.trim().toLowerCase();
		code = code.replace("coalesce(", "");
		code = code.replace("COALESCE(", "");
		code = code.replace(", '')", "");
		return code;
	}

	private double compareDBObjectCharToChar(String objName, String sourceObjLoc, String destObjLoc) {
		File file1 = new File(SOURCE_DIR + sourceObjLoc.replace(DOT_DELIM, FILE_SEPERATOR) + FILE_SEPERATOR + objName);
		File file2 = new File(SOURCE_DIR + destObjLoc.replace(DOT_DELIM, FILE_SEPERATOR) + FILE_SEPERATOR + objName);
		FileInputStream fis1 = null;
		BufferedInputStream bis1 = null;
		FileInputStream fis2 = null;
		BufferedInputStream bis2 = null;
		double unmatchedPercent = 100;
		try {
			fis1 = new FileInputStream(file1);
			bis1 = new BufferedInputStream(fis1);
			fis2 = new FileInputStream(file2);
			bis2 = new BufferedInputStream(fis2);

			int data;
			long unmatchedCount = 0L;
			long matchedCount = 0L;
			while ((data = bis1.read()) != -1) {
				if (data != bis2.read())
					unmatchedCount++;
				else
					matchedCount++;
			}
			unmatchedPercent = unmatchedCount * 100 / (matchedCount + unmatchedCount);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close(bis1);
			close(bis2);
		}
		return unmatchedPercent;
	}

	private void close(BufferedInputStream bis) {
		if (bis != null)
			try {
				bis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

	}

	private void close(BufferedWriter bw) {
		if (bw != null)
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

	}

	private void close(BufferedReader br) {
		if (br != null)
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
	}
}
