package com.neusoft.graphene.basecomponent.document.export;

import com.neusoft.graphene.basecomponent.document.LexDocument;
import com.neusoft.graphene.basecomponent.document.ResourceOps;

public interface Painter
{
	int AUTO		= 0;
	int DIRECTORY	= 1;
	int FILE		= 2;
	int STREAM		= 3;
	int OBJECT		= 4;
	int FILEPATH	= 5;
	
	void paint(LexDocument lexDocument, Object canvas, int canvasType, ResourceOps ops);
}
