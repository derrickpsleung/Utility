package com.main;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CodePatch {

	private static final String GIT_CMD_PATH="D:\\PortableGit\\git-cmd.exe";
	private static final String BACKUP_PATH="D:\\Derrick Leung\\BEA\\clearcase\\backup\\";
	
	private static final String DELIM__TAB = "\t";

	public static void main(String args[]){
		String srcPath = "D:\\FPS repo\\ccb-clearcase\\TICO016_CM_CR1700234-003_FPS_GIT\\CCB_CM_VOB\\CCB_Source\\";
		String destPath = "D:\\FPS repo\\ccb2\\";
		
		String diffFrom = "4834477b8deda6172fb91cc26c94cd1638f399bd";
		String diffTo = "0d275a4d9a15a73c6fa92d25bd0eaa6d10a5ba6a";
		
		try {
			//ClearCase to GIT
//			codePatch(srcPath, destPath, diffFrom, diffTo);
			
			//GIT to ClearCase
			codePatch(destPath, srcPath, diffFrom, diffTo);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
	}
	
	private static void codePatch(String srcPath, String destPath, String diffFrom, String diffTo) throws IOException, InterruptedException{

		//stash changes
		String cmd1 = String.format("cd %s && git stash save && exit", destPath);
		String[] command1 = {GIT_CMD_PATH, cmd1};
		runCmd(command1, "Stash result:");
		
		//make temporary directory for deleted file
		String subFolder = new SimpleDateFormat("yyyy_MM_dd_HHmmss_SSSSSS").format(new Date());
		String cmd3 = String.format("md \"%s\"", BACKUP_PATH+subFolder);
		String[] command3 = {"CMD","/C", cmd3};
		runCmd(command3, "Make directory result:");
		
		//diff > get the result list > copy files from source to destination
		String cmd2 = String.format("cd %s && git diff --binary --name-status %s %s && exit", srcPath, diffFrom, diffTo);
		String[] command2 = {GIT_CMD_PATH, cmd2};
		List<String> rsltList = runCmd(command2, "Diff result:");
		

		for(String rslt: rsltList){
			
			System.out.println("XXXXXXXXXXXXXXXXXXX");
			System.out.println(rslt);
			
			String[] rsltArr = rslt.split(DELIM__TAB);
			
			if(rsltArr.length == 2){
			
				String status = rsltArr[0];
				String relativePath = rsltArr[1].replace("/", "\\");
				
				removeAttr("-r", destPath + relativePath);

				if("A".equals(status) || "M".equals(status)){
					
					String from = srcPath + relativePath;
					String to = destPath + relativePath.substring(0, relativePath.lastIndexOf("\\")+1);
					
					addFile(from, to);
					
				}else if("D".equals(status)){
					
					moveFileToBackUp(destPath + relativePath, BACKUP_PATH+subFolder+"\\");
					
				}else{
					System.out.println( String.format("[Error] Cannot handle: %s",rslt));
				}
				
			}
			if(rsltArr.length == 3){
				
				String status = rsltArr[0];
				String relativePathToBeDeleted = rsltArr[1].replace("/", "\\");
				String relativePathToBeAdded = rsltArr[2].replace("/", "\\");
				
				removeAttr("-r", destPath + relativePathToBeDeleted);
				
				if ("R".equals(status.substring(0, 1))){
					
					moveFileToBackUp(destPath + relativePathToBeDeleted, BACKUP_PATH+subFolder+"\\");
					
					String from = srcPath + relativePathToBeAdded;
					String to = destPath + relativePathToBeAdded.substring(0, relativePathToBeAdded.lastIndexOf("\\")+1);
					addFile(from, to);
					
				}else{
					System.out.println( String.format("[Error] Cannot handle: %s",rslt));
				}
			}
			
		}
		
		
	}

	private static void moveFileToBackUp(String destPath, String backUpPath) throws IOException, InterruptedException{
		String cmd = String.format("move /Y \"%s\" \"%s\"", destPath, backUpPath);
		System.out.println("cmd: "+cmd);
		String[] command4 = {"CMD","/C", cmd};
		runCmd(command4, "move result:");
	}
	private static void addFile(String fromPath, String toPath) throws IOException, InterruptedException{
		String cmd = String.format("xcopy /Y \"%s\" \"%s\"", fromPath, toPath);
		System.out.println("cmd: "+cmd);
		String[] command = {"CMD","/C", cmd};
		runCmd(command, "copy result:");
	}
	
	private static void removeAttr(String attr, String path) throws IOException, InterruptedException{
		String cmd0 = String.format("attrib %s \"%s\"", attr, path);
		String[] command0 = {"CMD","/C", cmd0};
		runCmd(command0, "attrib result:");
	}
	
	private static List<String> runCmd(String[] command, String msg)
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
