package com.util.code.patch.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.util.file.FileUtil;

public class Git2GitCodePatch extends CodePatchAbst {

	private static final String PARAM__BACKUP_PATH = "BACKUP_PATH";

	private String backUpPath;

	public Git2GitCodePatch(String srcPath, String destPath, String diffFrom, String diffTo, String backUpPath) throws IOException, InterruptedException {
		
		super(srcPath, destPath, diffFrom, diffTo);

		// stash changes
		String cmd1 = String.format("cd %s && git stash save && exit", destPath);
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
	public Map<String, String> initParamMap() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put(PARAM__BACKUP_PATH, this.backUpPath);
		return paramMap;
	}

	@Override
	public void postProcess(String srcPath, String destPath, String diffFrom, String diffTo) throws Exception {

	}

	@Override
	public void add(String src, String dest, Map<String, String> paramMap) throws Exception {
		String cmd = String.format("xcopy /Y \"%s\" \"%s\"", src, dest);
		String[] command = { "CMD", "/C", cmd };
		FileUtil.runCmd(command, "copy result:");
	}

	@Override
	public void modify(String src, String dest, Map<String, String> paramMap) throws Exception {
		add(src, dest, paramMap);
	}

	@Override
	public void delete(String path, Map<String, String> paramMap) throws Exception {
		String cmd = String.format("move /Y \"%s\" \"%s\"", path, paramMap.get(PARAM__BACKUP_PATH));
		String[] command = { "CMD", "/C", cmd };
		FileUtil.runCmd(command, "move result:");
	}

	@Override
	public void rename(String deletePath, String src, String dest, Map<String, String> paramMap) throws Exception {
		delete(deletePath, paramMap);
		add(src, dest, paramMap);
	}



}
