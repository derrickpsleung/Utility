package com.util.domain;

public enum GitActionEnum {
	ADD("A"),
	MODIFY("M"),
	DELETE("D"),
	RENAME("R");

	private String action;

	private GitActionEnum(String action) {
		this.action = action;
	}

	public String getAction() {
		return this.action;
	}
}
