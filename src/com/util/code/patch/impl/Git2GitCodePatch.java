package com.util.code.patch.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.util.file.FileUtil;

public class Git2GitCodePatch extends CodePatchAbst {


	private String backUpPath;

	public Git2GitCodePatch(String srcPath, String destPath, String diffFrom, String diffTo, String backUpPath) throws IOException, InterruptedException {
		
		super(srcPath, destPath, diffFrom, diffTo);

		// stash changes
		String cmd1 = String.format("cd \"%s\" && git stash save && exit", destPath);
		String[] command1 = { GIT_CMD_PATH, cmd1 };
		FileUtil.runCmd(command1, "Stash result:");

		// make temporary directory for deleted file
		String subFolder = new SimpleDateFormat("yyyy_MM_dd_HHmmss_SSSSSS").format(new Date());
		String cmd3 = String.format("md \"%s\"", backUpPath + subFolder);
		String[] command3 = { "CMD", "/C", cmd3 };
		FileUtil.runCmd(command3, "Make directory result:");

		this.backUpPath = backUpPath + subFolder;

	}
	
	@Override
	public void postProcess(String srcPath, String destPath, String diffFrom, String diffTo) throws Exception {

	}

	@Override
	public void add(String src, String dest) throws Exception {
		FileUtil.addFile(src, dest);
	}

	@Override
	public void modify(String src, String dest) throws Exception {
		add(src, dest);
	}

	@Override
	public void delete(String path) throws Exception {
		FileUtil.moveFile(path, this.backUpPath);
	}

	@Override
	public void rename(String deletePath, String src, String dest) throws Exception {
		delete(deletePath);
		add(src, dest);
	}

	@Override
	public String diffCmd(String diffPath, String diffFrom, String diffTo) {
		return String.format("cd \"%s\" && git diff --binary --name-status %s %s && exit", diffPath, diffFrom, diffTo);
	}



}
