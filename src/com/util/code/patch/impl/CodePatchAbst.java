package com.util.code.patch.impl;

import java.util.List;
import java.util.Map;

import com.util.domain.GitActionEnum;
import com.util.file.FileUtil;

public abstract class CodePatchAbst {

	protected static final String GIT_CMD_PATH = "C:\\Program Files\\Git\\git-cmd.exe";
	protected static final String DELIM__TAB = "\t";

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

	public void process() throws Exception {

		// git diff to get the result list
		String cmd2 = String.format("cd %s && git diff --binary --name-status %s %s && exit", srcPath, diffFrom, diffTo);
		String[] command2 = { GIT_CMD_PATH, cmd2 };
		List<String> rsltList = FileUtil.runCmd(command2, "Diff result:");

		Map<String, String> paramMap = initParamMap();
		for (String rslt : rsltList) {

			System.out.println("XXXXXXXXXXXXXXXXXXX");
			System.out.println(rslt);

			String[] rsltArr = rslt.split(DELIM__TAB);

			if (rsltArr.length == 2) {

				String status = rsltArr[0];
				String relativeFilePath = rsltArr[1].replace("/", "\\");

				if (GitActionEnum.ADD.getAction().equals(status)) {

					String srcFilePath = srcPath + relativeFilePath;
					String destFolderPath = destPath + relativeFilePath.substring(0, relativeFilePath.lastIndexOf("\\") + 1);
					add(srcFilePath, destFolderPath, paramMap);

				} else if (GitActionEnum.MODIFY.getAction().equals(status)) {

					FileUtil.removeAttr("-r", destPath + relativeFilePath);
					String srcFilePath = srcPath + relativeFilePath;
					String destFolderPath = destPath + relativeFilePath.substring(0, relativeFilePath.lastIndexOf("\\") + 1);
					modify(srcFilePath, destFolderPath, paramMap);

				} else if (GitActionEnum.DELETE.getAction().equals(status)) {

					FileUtil.removeAttr("-r", destPath + relativeFilePath);
					String destFilePath = destPath + relativeFilePath;
					delete(destFilePath, paramMap);

				} else {
					System.out.println(String.format("[Error] Cannot handle: %s", rslt));
				}

			}
			if (rsltArr.length == 3) {

				String status = rsltArr[0];
				String relativePathToBeDeleted = rsltArr[1].replace("/", "\\");
				String relativePathToBeAdded = rsltArr[2].replace("/", "\\");

				if (GitActionEnum.RENAME.getAction().equals(status.substring(0, 1))) {

					String destDeleteFilePath = destPath + relativePathToBeDeleted;
					FileUtil.removeAttr("-r", destDeleteFilePath);

					String srcFilePath = srcPath + relativePathToBeAdded;
					String destFolderPath = destPath + relativePathToBeAdded.substring(0, relativePathToBeAdded.lastIndexOf("\\") + 1);

					rename(destDeleteFilePath, srcFilePath, destFolderPath, paramMap);

				} else {
					System.out.println(String.format("[Error] Cannot handle: %s", rslt));
				}
			}

		}

		postProcess(srcPath, destPath, diffFrom, diffTo);

	}

	/**
	 * @return
	 */
	public abstract Map<String, String> initParamMap();

	/**
	 * @param srcPath
	 * @param destPath
	 * @param diffFrom
	 * @param diffTo
	 * @throws Exception
	 */
	public abstract void postProcess(String srcPath, String destPath, String diffFrom, String diffTo) throws Exception;

	/**
	 * @param srcPath
	 * @param destPath
	 * @param paramMap
	 * @throws Exception
	 */
	public abstract void add(String srcPath, String destPath, Map<String, String> paramMap) throws Exception;

	/**
	 * @param srcPath
	 * @param destPath
	 * @param paramMap
	 * @throws Exception
	 */
	public abstract void modify(String srcPath, String destPath, Map<String, String> paramMap) throws Exception;

	/**
	 * @param path
	 * @param paramMap
	 * @throws Exception
	 */
	public abstract void delete(String path, Map<String, String> paramMap) throws Exception;

	/**
	 * @param deletePath
	 * @param srcPath
	 * @param destPath
	 * @param paramMap
	 * @throws Exception
	 */
	public abstract void rename(String deletePath, String srcPath, String destPath, Map<String, String> paramMap) throws Exception;

}
