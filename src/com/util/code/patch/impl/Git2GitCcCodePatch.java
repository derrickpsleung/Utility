package com.util.code.patch.impl;

import java.io.IOException;

import com.util.file.FileUtil;

public class Git2GitCcCodePatch extends Git2GitCodePatch {

	public Git2GitCcCodePatch(String srcPath, String destPath, String diffFrom, String diffTo,
			String backUpPath, String activityName, String activityId) throws IOException, InterruptedException {
		super(srcPath, destPath, diffFrom, diffTo, backUpPath);
		FileUtil.ccCreateNewActivity(destPath, activityName, activityId);
	}

	@Override
	public void postProcess(String srcPath, String destPath, String diffFrom, String diffTo) throws Exception {
		super.postProcess(srcPath, destPath, diffFrom, diffTo);

	}

	@Override
	public void add(String src, String dest) throws Exception {
		super.add(src, dest);
		FileUtil.ccCheckInNewFile(dest);
	}

	@Override
	public void modify(String src, String dest) throws Exception {
		super.modify(src, dest);
		FileUtil.ccCheckInHijackedFile(dest);
	}

	@Override
	public void delete(String path) throws Exception {
		super.delete(path);
		FileUtil.ccCheckInDeletedFile(path);
	}

	@Override
	public void rename(String deletePath, String src, String dest) throws Exception {
		super.rename(deletePath, src, dest);
		FileUtil.ccCheckInDeletedFile(deletePath);
		FileUtil.ccCheckInNewFile(dest);
	}


}
