package com.neusoft.graphene.basecomponent.document;

import java.io.Serializable;

import lombok.Data;

@Data
public class LexFontFamily implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;

	private String path;

	public LexFontFamily(String name, String path) {
		this.name = name;
		this.path = path;
	}

	public LexFont derive(float size) {
		return new LexFont(this, size);
	}
}
