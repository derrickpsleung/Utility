package com.main;
import com.util.code.patch.impl.CodePatchAbst;
import com.util.code.patch.impl.Git2GitCodePatch;


public class CodePatchMain {


	private static final String BACKUP_PATH = "D:\\workspace\\eclipse-workspace\\_backup\\";
	

	public static void main(String args[]) throws Exception{
		
		String srcPath = "D:\\workspace\\eclipse-workspace\\Utility\\";
		String destPath = "D:\\workspace\\eclipse-workspace\\UtilityBk\\";
		
		String diffFrom = "2e5d0636a5c0a08cf0924f6f6164e1323e35270e";
		String diffTo = "c99601490fdef39efd1e269b6e9722ca772d2ef7";
		
		//GIT(CC) to GIT
		CodePatchAbst codePatch = new Git2GitCodePatch(srcPath, destPath, diffFrom, diffTo, BACKUP_PATH);
		
		//GIT to GIT(CC) and CC
//		CodePatchAbst codePatch = new Git2CcCodePatch(destPath, srcPath, diffFrom, diffTo);
		
		codePatch.process();
	}
	
	
}
