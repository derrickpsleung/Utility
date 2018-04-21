package com.util.code.patch.impl;

import java.io.IOException;

import com.util.file.FileUtil;

public class Git2CcCodePatch extends CodePatchAbst {

	public Git2CcCodePatch(String destPath, String activityName, String activityId) throws IOException, InterruptedException {
		super(destPath, destPath, null, null);
		FileUtil.ccCreateNewActivity(destPath, activityName, activityId);
		
		// stage all files
		String cmd1 = String.format("cd \"%s\" && git add -A", destPath);
		String[] command1 = { GIT_CMD_PATH, cmd1 };
		FileUtil.runCmd(command1, "Stage all files result:");
	}

	@Override
	public void postProcess(String srcPath, String destPath, String diffFrom, String diffTo) throws Exception {
	
	}

	@Override
	public void add(String src, String dest) throws Exception {
		FileUtil.ccCheckInNewFile(dest);
	}

	@Override
	public void modify(String src, String dest) throws Exception {
		FileUtil.ccCheckInHijackedFile(dest);
	}

	@Override
	public void delete(String path) throws Exception {
		FileUtil.ccCheckInDeletedFile(path);
	}

	@Override
	public void rename(String deletePath, String src, String dest) throws Exception {
		FileUtil.ccCheckInDeletedFile(deletePath);
		FileUtil.ccCheckInNewFile(dest);
	}

	@Override
	public String diffCmd(String diffPath, String diffFrom, String diffTo) {
		return String.format("cd \"%s\" && git diff --name-status --binary --staged && exit", diffPath);
	}


}
