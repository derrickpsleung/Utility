package com.util.file;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class FileUtil {

	public static void moveFile(String srcPath, String destPath) throws IOException, InterruptedException{
		String cmd = String.format("move /Y \"%s\" \"%s\"", srcPath, destPath);
		String[] command = {"CMD","/C", cmd};
		runCmd(command, "move result:");
	}
	
	public static void addFile(String fromPath, String toPath) throws IOException, InterruptedException{
		String cmd = String.format("xcopy /Y \"%s\" \"%s\"", fromPath, toPath);
		String[] command = {"CMD","/C", cmd};
		runCmd(command, "copy result:");
	}
	
	public static void removeAttr(String attr, String path) throws IOException, InterruptedException{
		String cmd = String.format("attrib %s \"%s\"", attr, path);
		String[] command = {"CMD","/C", cmd};
		runCmd(command, "attrib result:");
	}
	
	public static List<String> runCmd(String[] command, String msg)
			throws IOException, InterruptedException {
		
		List<String> rsltLst = new ArrayList<String>();
		ProcessBuilder pb = new ProcessBuilder(command);
		pb.redirectErrorStream(true);
		
		Process proc = pb.start();
		
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		System.out.println(msg);
		String s = null;
		while ((s = stdInput.readLine())!=null){
			System.out.println(s);
			rsltLst.add(s);
		}
		
		proc.waitFor();
		
		return rsltLst;
	}
	
}
