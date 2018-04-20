package com.main;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.util.code.patch.impl.CodePatchAbst;
import com.util.code.patch.impl.Git2GitCodePatch;


public class CodePatchMain {
	
	static {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hhmmss");
        System.setProperty("current.date", dateFormat.format(new Date()));
	}
	final static Logger logger = Logger.getLogger(CodePatchMain.class);

	public static void main(String args[]) throws Exception{

		String backUpPath = "D:\\workspace\\eclipse-workspace\\_backup\\";
		
		String srcPath = "D:\\workspace\\eclipse-workspace\\Utility\\";
		String destPath = "D:\\workspace\\eclipse-workspace\\UtilityBk\\";
		
		String diffFrom = "2e5d0636a5c0a08cf0924f6f6164e1323e35270e";
		String diffTo = "c99601490fdef39efd1e269b6e9722ca772d2ef7";
		
		//GIT(CC) to GIT
		CodePatchAbst codePatch = new Git2GitCodePatch(srcPath, destPath, diffFrom, diffTo, backUpPath);
		
		//GIT to GIT(CC) and CC
//		String actName = "";
//		String actId = "";
//		CodePatchAbst codePatch = new Git2GitCcCodePatch(destPath, srcPath, diffFrom, diffTo, backUpPath, actName, actId);
		
		codePatch.process();
	}
	
	
}
