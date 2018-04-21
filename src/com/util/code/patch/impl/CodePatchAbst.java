package com.util.code.patch.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.util.domain.GitActionEnum;
import com.util.file.FileUtil;

public abstract class CodePatchAbst {

	
	final static Logger logger = Logger.getLogger(CodePatchAbst.class);
	
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
		String cmd = diffCmd(srcPath, diffFrom, diffTo);
		String[] command2 = { GIT_CMD_PATH, cmd };
		List<String> rsltList = FileUtil.runCmd(command2, "Diff result:");


		for (String rslt : rsltList) {

			logger.info("XXXXXXXXXXXXXXXXXXX");
			logger.info(rslt);

			String[] rsltArr = rslt.split(DELIM__TAB);

			if (rsltArr.length == 2) {

				String status = rsltArr[0];
				String relativeFilePath = rsltArr[1].replace("/", "\\");

				if (GitActionEnum.ADD.getAction().equals(status)) {

					String srcFilePath = srcPath + relativeFilePath;
					String destFolderPath = destPath + relativeFilePath.substring(0, relativeFilePath.lastIndexOf("\\") + 1);
					add(srcFilePath, destFolderPath);

				} else if (GitActionEnum.MODIFY.getAction().equals(status)) {

					FileUtil.removeAttr("-r", destPath + relativeFilePath);
					String srcFilePath = srcPath + relativeFilePath;
					String destFolderPath = destPath + relativeFilePath.substring(0, relativeFilePath.lastIndexOf("\\") + 1);
					modify(srcFilePath, destFolderPath);

				} else if (GitActionEnum.DELETE.getAction().equals(status)) {

					FileUtil.removeAttr("-r", destPath + relativeFilePath);
					String destFilePath = destPath + relativeFilePath;
					delete(destFilePath);

				} else {
					logger.info(String.format("[Error] Cannot handle: %s", rslt));
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

					rename(destDeleteFilePath, srcFilePath, destFolderPath);

				} else {
					logger.info(String.format("[Error] Cannot handle: %s", rslt));
				}
			}

		}

		postProcess(srcPath, destPath, diffFrom, diffTo);

	}


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
	public abstract void add(String srcPath, String destPath) throws Exception;

	/**
	 * @param srcPath
	 * @param destPath
	 * @param paramMap
	 * @throws Exception
	 */
	public abstract void modify(String srcPath, String destPath) throws Exception;

	/**
	 * @param path
	 * @param paramMap
	 * @throws Exception
	 */
	public abstract void delete(String path) throws Exception;

	/**
	 * @param deletePath
	 * @param srcPath
	 * @param destPath
	 * @param paramMap
	 * @throws Exception
	 */
	public abstract void rename(String deletePath, String srcPath, String destPath) throws Exception;
	
	/**
	 * @param diffPath
	 * @param diffFrom
	 * @param diffTo
	 * @return
	 */
	public abstract String diffCmd(String diffPath, String diffFrom, String diffTo);

}
