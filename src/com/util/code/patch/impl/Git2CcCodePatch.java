package com.util.code.patch.impl;

import java.util.Map;

public class Git2CcCodePatch extends CodePatchAbst {

	public Git2CcCodePatch(String srcPath, String destPath, String diffFrom, String diffTo) {
		super(srcPath, destPath, diffFrom, diffTo);
	}

	@Override
	public void postProcess(String srcPath, String destPath, String diffFrom, String diffTo) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void add(String src, String dest, Map<String, String> paramMap) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void modify(String src, String dest, Map<String, String> paramMap) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(String path, Map<String, String> paramMap) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void rename(String deletePath, String src, String dest, Map<String, String> paramMap) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public Map<String, String> initParamMap() {
		// TODO Auto-generated method stub
		return null;
	}

}
