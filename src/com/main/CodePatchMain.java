package com.main;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.util.code.patch.impl.CodePatchAbst;
import com.util.code.patch.impl.Git2CcCodePatch;
import com.util.code.patch.impl.Git2GitCodePatch;

public class CodePatchMain {

	static {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hhmmss");
		System.setProperty("current.date", dateFormat.format(new Date()));
	}
	final static Logger logger = Logger.getLogger(CodePatchMain.class);

	public static void main(String args[]) throws Exception {

		String backUpPath = "D:\\workspace\\eclipse-workspace\\_backup\\";

		String gitPath = "D:\\workspace\\eclipse-workspace\\Utility\\";
		String ccPath = "D:\\workspace\\eclipse-workspace\\UtilityBk\\";

		String diffFrom = "2e5d0636a5c0a08cf0924f6f6164e1323e35270e";
		String diffTo = "a059485c13c966fe9118b850f18ecc006656b60a";

//		ccToGit(ccPath, gitPath, diffFrom, diffTo, backUpPath);
		gitToCc(gitPath, ccPath, diffFrom, diffTo, backUpPath);

	}

	public static void ccToGit(String srcPath, String destPath, String diffFrom, String diffTo, String backUpPath) throws Exception {
		// GIT(CC) to GIT
		CodePatchAbst codePatch = new Git2GitCodePatch(srcPath, destPath, diffFrom, diffTo, backUpPath);
		codePatch.process();
	}

	public static void gitToCc(String srcPath, String destPath, String diffFrom, String diffTo, String backUpPath) throws Exception {
		// GIT to GIT(CC)
		CodePatchAbst codePatch = new Git2GitCodePatch(srcPath, destPath, diffFrom, diffTo, backUpPath);
		codePatch.process();

		String actName = "";
		String actId = "";
		// GIT(CC) to CC
		codePatch = new Git2CcCodePatch(destPath, actName, actId);
//		codePatch.process();
	}

}
