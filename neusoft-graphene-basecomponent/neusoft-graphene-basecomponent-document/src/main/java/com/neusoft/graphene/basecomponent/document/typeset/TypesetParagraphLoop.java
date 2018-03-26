package com.neusoft.graphene.basecomponent.document.typeset;

import com.neusoft.graphene.basecomponent.document.ResourceOps;
import com.neusoft.graphene.basecomponent.document.exception.TypesetBuildException;
import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.Formula;
import com.neusoft.graphene.basecomponent.formula.Value;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TypesetParagraphLoop {

    private Formula from;
    private Formula to;
    private Formula step;

    private Formula value;

    private String name;

//	private String[] vars;
//	int loopValue;
//	Factors varSet;

    private List<TypesetParagraph> paragraphs = new ArrayList<>();

    public TypesetParagraphLoop(Formula from, Formula to, Formula step, String name) {
        this.from = from;
        this.to = to;
        this.step = step;

        this.name = name == null ? "I" : name;
    }

    public TypesetParagraphLoop(Formula value, String name) {
        this.name = name == null ? "I" : name;
        this.value = value;
    }

    public void addParagraph(TypesetParagraph tp) {
        paragraphs.add(tp);
    }


    public void build(TypesetParameters typesetPars, TypesetDocument td, ResourceOps ops) {
        try {
            Object runValue =
                    this.value == null ? null : value.run(typesetPars);

            TypesetParameters typesetParsInner = new TypesetParameters(typesetPars);
            typesetParsInner.setPaper(typesetPars.getPaper());

//            if (this.value != null && runValue == null) // 有循环的list value，不过是空，那么直接跳过
//            {
//            } else
            if (runValue instanceof Map) // value = "..."//if go this branch no go next runValue != null
            {
                typesetParsInner.declare(name);

                Map map = (Map) runValue;

                for (Object o : map.keySet()) {
                    typesetParsInner.set(name, o);
                    int num = paragraphs.size();
                    for (TypesetParagraph paragraph : paragraphs)
                        td.buildParagraph(paragraph, typesetParsInner, ops);
                }
            } else if (runValue != null) // value = "..."
            {
                int size = 0;
                if (runValue instanceof List)
                    size = ((List) runValue).size();
                else if (runValue instanceof Object[])
                    size = ((Object[]) runValue).length;

                typesetParsInner.declare(name);

                for (int i = 0; i < size; i++) {
                    if (runValue instanceof List)
                        typesetParsInner.set(name, ((List) runValue).get(i));
                    else
                        typesetParsInner.set(name, ((Object[]) runValue)[i]);

                    int num = paragraphs.size();
                    for (TypesetParagraph paragraph : paragraphs) td.buildParagraph(paragraph, typesetParsInner, ops);
                }
            } else // from="..." to="..." step="..."
            {
                int f = 0, t = 2, s = 1;
                try {
                    f = Value.intOf(from, typesetPars);
                } catch (Exception e) {
                    if (TypesetUtil.getMode() == TypesetUtil.MODE_FAIL) {
                        throw new TypesetBuildException("exception in loop<from>'s formula: " + value, e);
                    } else if (TypesetUtil.getMode() == TypesetUtil.MODE_ALWAYS) {
                        f = 0;
                        System.out.println("exception in loop<from> formula: " + value + ", set it 0 instead.");
                    }
                }

                try {
                    t = Value.intOf(to, typesetPars);
                } catch (Exception e) {
                    if (TypesetUtil.getMode() == TypesetUtil.MODE_FAIL) {
                        throw new TypesetBuildException("exception in loop<to>'s formula: " + value, e);
                    } else if (TypesetUtil.getMode() == TypesetUtil.MODE_ALWAYS) {
                        t = 1;
                        System.out.println("exception in loop<to> formula: " + value + ", set it 1 instead.");
                    }
                }

                try {
                    s = step == null ? 1 : Value.intOf(step, typesetPars);
                } catch (Exception e) {
                    if (TypesetUtil.getMode() == TypesetUtil.MODE_FAIL) {
                        throw new TypesetBuildException("exception in loop<step>'s formula: " + value, e);
                    } else if (TypesetUtil.getMode() == TypesetUtil.MODE_ALWAYS) {
                        s = 1;
                        System.out.println("exception in loop<step> formula: " + value + ", set it 1 instead.");
                    }
                }

                typesetParsInner.declare(name);
                for (int i = f; i <= t; i += s) {
                    typesetParsInner.set(name, i);

                    int num = paragraphs.size();
                    for (int j = 0; j < num; j++)
                        td.buildParagraph(paragraphs.get(i), typesetParsInner, ops);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
