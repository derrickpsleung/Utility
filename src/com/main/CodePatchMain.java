package com.main;
import com.util.code.patch.impl.CodePatchAbst;
import com.util.code.patch.impl.Git2GitCodePatch;


public class CodePatchMain {


	private static final String BACKUP_PATH = "D:\\Derrick Leung\\BEA\\clearcase\\backup\\";
	

	public static void main(String args[]) throws Exception{
		
		String srcPath = "D:\\FPS repo\\ccb-clearcase\\TICO016_CM_CR1700234-003_FPS_GIT\\CCB_CM_VOB\\CCB_Source\\";
		String destPath = "D:\\FPS repo\\ccb2\\";
		
		String diffFrom = "4834477b8deda6172fb91cc26c94cd1638f399bd";
		String diffTo = "0d275a4d9a15a73c6fa92d25bd0eaa6d10a5ba6a";
		
		//GIT(CC) to GIT
		CodePatchAbst codePatch = new Git2GitCodePatch(srcPath, destPath, diffFrom, diffTo, BACKUP_PATH);
		
		//GIT to GIT(CC) and CC
//		CodePatchAbst codePatch = new Git2CcCodePatch(destPath, srcPath, diffFrom, diffTo);
		
		codePatch.process();
	}
	
	
}
