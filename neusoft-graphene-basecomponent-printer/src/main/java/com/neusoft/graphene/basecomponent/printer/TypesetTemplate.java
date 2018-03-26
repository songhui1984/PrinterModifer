package com.neusoft.graphene.basecomponent.printer;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.neusoft.graphene.basecomponent.document.typeset.Typeset;
import com.neusoft.graphene.basecomponent.document.typeset.TypesetUtil;
import com.neusoft.graphene.basecomponent.printer.mongo.MongoFileOps;
import com.neusoft.graphene.basecomponent.printer.tools.Common;
import com.neusoft.graphene.basecomponent.printer.tools.Disk;

import lombok.Data;


@Data
@Service
public class TypesetTemplate {
	Long id;
	Long signId;

	String code;
	String name;

	String workDir;
	
	String testFile;
	String templateFile;

	Typeset typeset;

//	File test;
	
//	InputStream testFileInputStream;
	
	String fileContext;

//	loading... 1/iyb_proposal/iyb建议书1/template.xml
//	workDir+templateFile iyb_proposaltemplate.xml
	public void refresh(MongoFileOps fileOps) {
		if (!Common.isEmpty(this.getTestFile()))
			try {
				fileContext = Disk.InputStream2String(fileOps.getByFileName(workDir+"_"+testFile), "utf-8");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		else
//			test = null;
//			testFileInputStream = null;
			fileContext = null;
	

		if (!Common.isEmpty(templateFile)) {
			System.out.println("loading... " + id + "/" + code + "/" + name + "/" + templateFile);

			try {
//				this.typeset = TypesetUtil.parseTypeset(
//						Common.pathOf(TypesetUtil.getResourcePath(), Common.pathOf(workDir, templateFile)));
				
				System.out.println("workDir+templateFile"+workDir+templateFile);
				
				this.typeset = TypesetUtil.parseTypeset(fileOps.getByFileName(workDir+"_"+templateFile));
				
				this.typeset.setId(code);
				
			} catch (Exception e) {
				this.typeset = null;
				e.printStackTrace();
			}
		} else {
			this.typeset = null;
		}
	}
}
