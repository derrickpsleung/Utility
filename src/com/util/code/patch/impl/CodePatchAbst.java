package com.util.code.patch.impl;

import java.io.IOException;
import java.util.List;

import com.util.file.FileUtil;

public abstract class CodePatchAbst {

	private final String GIT_CMD_PATH = "D:\\PortableGit\\git-cmd.exe";
	private final String DELIM__TAB = "\t";

	private String srcPath;
	private String destPath;
	private String diffFrom;
	private String diffTo;

	public CodePatchAbst(String srcPath, String destPath, String diffFrom, String diffTo) {
		this.srcPath = srcPath;
		this.destPath = destPath;
		this.diffFrom = diffFrom;
		this.diffTo = diffTo;
	}

	public void process() throws IOException, InterruptedException {

		// git diff to get the result list
		String cmd2 = String.format("cd %s && git diff --binary --name-status %s %s && exit", srcPath, diffFrom, diffTo);
		String[] command2 = { GIT_CMD_PATH, cmd2 };
		List<String> rsltList = FileUtil.runCmd(command2, "Diff result:");

		for (String rslt : rsltList) {

			System.out.println("XXXXXXXXXXXXXXXXXXX");
			System.out.println(rslt);

			String[] rsltArr = rslt.split(DELIM__TAB);

			if (rsltArr.length == 2) {

				String status = rsltArr[0];
				String relativePath = rsltArr[1].replace("/", "\\");

				removeAttr("-r", destPath + relativePath);

				if ("A".equals(status) || "M".equals(status)) {

					String from = srcPath + relativePath;
					String to = destPath + relativePath.substring(0, relativePath.lastIndexOf("\\") + 1);

					addFile(from, to);

				} else if ("D".equals(status)) {

					moveFileToBackUp(destPath + relativePath, BACKUP_PATH + subFolder + "\\");

				} else {
					System.out.println(String.format("[Error] Cannot handle: %s", rslt));
				}

			}
			if (rsltArr.length == 3) {

				String status = rsltArr[0];
				String relativePathToBeDeleted = rsltArr[1].replace("/", "\\");
				String relativePathToBeAdded = rsltArr[2].replace("/", "\\");

				removeAttr("-r", destPath + relativePathToBeDeleted);

				if ("R".equals(status.substring(0, 1))) {

					moveFileToBackUp(destPath + relativePathToBeDeleted, BACKUP_PATH + subFolder + "\\");

					String from = srcPath + relativePathToBeAdded;
					String to = destPath + relativePathToBeAdded.substring(0, relativePathToBeAdded.lastIndexOf("\\") + 1);
					addFile(from, to);

				} else {
					System.out.println(String.format("[Error] Cannot handle: %s", rslt));
				}
			}

		}

	}

	public abstract void preProcess(String srcPath, String destPath, String diffFrom, String diffTo) throws Exception;

	public abstract void postProcess(String srcPath, String destPath, String diffFrom, String diffTo) throws Exception;

	public abstract void add(String src, String dest) throws Exception;

	public abstract void modify(String src, String dest) throws Exception;

	public abstract void delete(String path) throws Exception;

	public abstract void rename(String deletePath, String src, String dest) throws Exception;

}
