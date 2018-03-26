package com.neusoft.graphene.basecomponent.document.typeset.parser;

import java.io.InputStream;

import com.neusoft.graphene.basecomponent.document.typeset.Typeset;

public interface TypesetParser
{
	public Typeset parse(InputStream inputStream);
}
