package com.util.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class FileUtil {

	final static Logger logger = Logger.getLogger(FileUtil.class);

	public static void moveFile(String srcPath, String destPath) throws IOException, InterruptedException {
		String cmd = String.format("move /Y \"%s\" \"%s\"", srcPath, destPath);
		String[] command = { "CMD", "/C", cmd };
		runCmd(command, "move result:");
	}

	public static void addFile(String fromPath, String toPath) throws IOException, InterruptedException {
		String cmd = String.format("xcopy /Y \"%s\" \"%s\"", fromPath, toPath);
		String[] command = { "CMD", "/C", cmd };
		runCmd(command, "copy result:");
	}

	public static void removeAttr(String attr, String path) throws IOException, InterruptedException {
		String cmd = String.format("attrib %s \"%s\"", attr, path);
		String[] command = { "CMD", "/C", cmd };
		runCmd(command, "attrib result:");
	}

	public static void ccCreateNewActivity(String vobPath, String activityName, String activityId) throws IOException, InterruptedException {
		String cmd = String.format("cd \"%s\" && cleartool -headline \"%s\" %s", vobPath, activityName, activityId);
		String[] command = { "CMD", "/C", cmd };
		runCmd(command, "cc create new activity result:");
	}

	public static void ccCheckInNewFile(String toPath) throws IOException, InterruptedException {
		String cmd = String.format("cleartool mkele -nc \"%s\" && cleartool checkin -nc \"%s\"", toPath, toPath);
		String[] command = { "CMD", "/C", cmd };
		runCmd(command, "cc check in new file result:");
	}

	public static void ccCheckInHijackedFile(String toPath) throws IOException, InterruptedException {
		String cmd = String.format("cleartool checkout -nc -usehijack \"%s\" && cleartool checkin -nc \"%s\"", toPath, toPath);
		String[] command = { "CMD", "/C", cmd };
		runCmd(command, "cc check in hijacked file result:");
	}

	public static void ccCheckInDeletedFile(String toPath) throws IOException, InterruptedException {
		// TODO
	}

	public static List<String> runCmd(String[] command, String msg) throws IOException, InterruptedException {

		List<String> rsltLst = new ArrayList<String>();
		ProcessBuilder pb = new ProcessBuilder(command);
		pb.redirectErrorStream(true);

		Process proc = pb.start();

		BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		logger.info(msg);
		String str = null;
		while ((str = stdInput.readLine()) != null) {
			logger.info(str);
			rsltLst.add(str);
		}

		proc.waitFor();

		return rsltLst;
	}

}
