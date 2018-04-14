package com.util.code.patch.inf;

public interface GitAction {
	

	public void add(String src, String dest);
	public void modify(String src, String dest);
	public void delete(String path);
	public void rename(String deletePath, String src, String dest);
}
