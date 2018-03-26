package com.neusoft.graphene.basecomponent.document.typeset;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.neusoft.graphene.basecomponent.document.LexDocument;
import com.neusoft.graphene.basecomponent.document.LexFontFamily;
import com.neusoft.graphene.basecomponent.document.LexPage;
import com.neusoft.graphene.basecomponent.document.ResourceOps;
import com.neusoft.graphene.basecomponent.document.element.DocumentPanel;
import com.neusoft.graphene.basecomponent.document.element.DocumentReset;
import com.neusoft.graphene.basecomponent.document.element.DocumentText;
import com.neusoft.graphene.basecomponent.document.element.DocumentElementAbs;
import com.neusoft.graphene.basecomponent.document.exception.TypesetBuildException;
import com.neusoft.graphene.basecomponent.document.typeset.element.TypesetElement;
import com.neusoft.graphene.basecomponent.document.typeset.element.TypesetPanel;
import com.neusoft.graphene.basecomponent.document.typeset.element.TypesetText;
import com.neusoft.graphene.basecomponent.document.typeset.enviroment.TextDimensionSimple;
import com.neusoft.graphene.basecomponent.formula.Formula;
import com.neusoft.graphene.basecomponent.formula.Value;


public class TypesetDocument extends LexDocument {
    private static final long serialVersionUID = 1L;

    private boolean fill;

    private Typeset typeset;

    public TypesetDocument(Typeset typeset) {
        this.typeset = typeset;

        check();
    }

    public void check() {
        if (TypesetText.getTextDimension() == null) {
            System.out.println("Typeset - WARN: TextDimension is null，");
            System.out.println("Typeset - Call TypesetUtil.setTextDimension(ITextDimension textDimension) first.");
            System.out.println("Typeset - Use TextDimensionSimple instead now, the text may be out of shape.");
            System.out.println("Typeset - If the system has graphics environment, you can use TextDimensionAwt for better result.");

            TypesetUtil.setTextDimension(new TextDimensionSimple());
        }
    }

    public void build(TypesetParameters typesetParameters, ResourceOps ops) {
        initParameter(typesetParameters);

        for (LexFontFamily lexFontFamily : typeset.getFontFamilyMap().values())
            ((TypesetFontFamily) lexFontFamily).build(typesetParameters);

        int num = typeset.getParagraphNum();

        for (int i = 0; i < num; i++) {
            try {
                Object paragraph = typeset.getParagraph(i);

                if (paragraph instanceof TypesetParagraph)
                    buildParagraph((TypesetParagraph) paragraph, typesetParameters, ops);

                else if (paragraph instanceof TypesetParagraphLoop)
                    ((TypesetParagraphLoop) paragraph).build(typesetParameters, this, ops);
            } catch (TypesetBuildException e) {
                e.printStackTrace();
            }
        }


        //替换总页数
        int total = this.pageSize();
        Map<String, Object> pageMap = (Map<String, Object>) typesetParameters.get("PAGE");
        pageMap.put("TOTAL", total);

        for (int i = 0; i < total; i++) {
            LexPage page = this.getPage(i);
            int c = page.getElementNum();
            for (int j = 0; j < c; j++) {
                resetAtFinal(page.getElement(j), typesetParameters);
            }
        }
    }

    private void initParameter(TypesetParameters typesetParameters) {
        Map<String, String> systemMap = new HashMap<>();
        systemMap.put("PRINT_TIME", new Date().toString());

        typesetParameters.set("SYSTEM", systemMap);

        if (typesetParameters.get("RESOURCE_PATH") == null)
            typesetParameters.set("RESOURCE_PATH", TypesetUtil.getResourcePath());

        Map<String, Object> pageMap = new HashMap<>();
        pageMap.put("SEQUENCE", null);
        pageMap.put("TOTAL", null);

        typesetParameters.set("PAGE", pageMap);
    }

    protected void buildParagraph(TypesetParagraph tParagraph, TypesetParameters tParams, ResourceOps ops) {

        Formula visible_Fromula = tParagraph.getVisible();
        //是否跳过改段落
        boolean pass = visible_Fromula == null || Value.booleanOf(visible_Fromula, tParams);

        if (pass) {
            //段落模板构建
            TypesetPaper tPaper = tParagraph.getTypesetPaper();
            if (tPaper == null)
                tPaper = typeset.getPageDefineDefault();

            tParams.setY(0);
            tParams.setDatum(0);
            tParams.setWindage(0);

            tParams.setPaper(tPaper);

            TypesetPanel body = tPaper.getBody();

            int bodyX = body.getX_TypesetNumber().value(tParams);
            int bodyY = body.getY_TypesetNumber().value(tParams);
            int bodyHeight = tPaper.getBody().getHeight().value(tParams);

            //页面坐标
            tParams.setPageTop(0);
            tParams.setPageBottom(bodyHeight);

            buildLexPage(tPaper, tParams, ops);

            //基准坐标
            tParams.setY(0);
            tParams.setDatum(0);
            tParams.setWindage(0);

            //段落排版
            List<DocumentElementAbs> lexElement_list = new ArrayList<>();
            int pargraph_elementSize = tParagraph.getElementSize();

            for (int index = 0; index < pargraph_elementSize; index++) {
                try {
                    TypesetElement tElement = tParagraph.getElement(index);

                    DocumentElementAbs lexElement = tElement.isShow(tParams) ? tElement.build(tParams, ops) : null;

                    if (lexElement != null)
                        lexElement_list.add(lexElement);
                } catch (TypesetBuildException e) {
                    e.printStackTrace();
                }
            }

            //重置基准坐标，避免在构建模板时候使用错误的基准坐标
            tParams.setY(0);
            tParams.setDatum(0);
            tParams.setWindage(0);

            int lexElement_size = lexElement_list.size();

            for (DocumentElementAbs aLexElement_list : lexElement_list) {
                addElement(aLexElement_list, tParams, tPaper, bodyX, bodyY, bodyHeight, ops);
            }

            //每一个章节的最后一页如果仅仅是指针移到该页,该页却没有任何内容的话,就移去该页.
            if (!fill)
                this.removePage(this.pageSize() - 1);
        }
    }

    private void resetAtFinal(DocumentElementAbs element, TypesetParameters tParams) {
        if (element instanceof DocumentText) {
            DocumentText text = (DocumentText) element;

            if (text.getResetAtFinal_Formula() != null)
                text.getResetAtFinal_Formula().run(tParams);

        } else if (element instanceof DocumentPanel) {
            DocumentPanel panel = (DocumentPanel) element;
            int c = panel.getElementCount();
            for (int i = 0; i < c; i++) {
                resetAtFinal(panel.getElement(i), tParams);
            }
        }
    }


    /**
     *  在LexPage上添加一个LexElement元素
     * <p>
     * 避免DocumentPanel过大，跨页时不可分割造成大片空白，所以拆除DocumentPanel，将上面的元素直接放在页面上
     * @param lexElementabs
     * @param tParams
     * @param tPaper
     * @param bodyX
     * @param bodyY
     * @param bodyHeight
     * @param ops
     */
    private void addElement(DocumentElementAbs lexElementabs, TypesetParameters tParams,
                            TypesetPaper tPaper, int bodyX, int bodyY, int bodyHeight, ResourceOps ops) {
        if (lexElementabs instanceof DocumentPanel && lexElementabs.canSplit()
                && ((DocumentPanel) lexElementabs).getType() == 0) {
            DocumentPanel documentPanel = (DocumentPanel) lexElementabs;
            DocumentPanel title_documentPanel = (DocumentPanel) documentPanel.getAdditional("title");

            int count = documentPanel.getElementCount();
            for (int index = 0; index < count; index++) {
                DocumentElementAbs panelElement = documentPanel.getElement(index);

                panelElement.setX(panelElement.getX() + documentPanel.getX());
                panelElement.setY(panelElement.getY() + documentPanel.getY());

                if (title_documentPanel != null) {
                    // panel嵌套的原因，在上面的setY中加windage会造成重复添加，所以在add到页面上才添加，但是前面判断跨页也需要windage，所以加上
                    if (panelElement.getY() + panelElement.getHeight() + tParams.getWindage() > tParams.getPageBottom()) {
                        tParams.setPageTop(panelElement.getY() + tParams.getWindage());
                        tParams.setPageBottom(tParams.getPageTop() + bodyHeight);

                        buildLexPage(tPaper, tParams, ops);

                        int x = title_documentPanel.getX() + documentPanel.getX();

                        int y = panelElement.getY();

                        DocumentPanel titleCopy = (DocumentPanel) title_documentPanel.copy();
                        titleCopy.setX(x);
                        titleCopy.setY(y);

                        addElement(titleCopy, tParams, tPaper, bodyX, bodyY, bodyHeight, ops);

                        tParams.setWindage(tParams.getWindage() + title_documentPanel.getHeight());

                    } else if (index == 0) {
                        if (panelElement.getY() + title_documentPanel.getHeight() + tParams.getWindage() > tParams.getPageBottom()) {
                            tParams.setPageTop(panelElement.getY() + tParams.getWindage());
                            tParams.setPageBottom(tParams.getPageTop() + bodyHeight);

                            buildLexPage(tPaper, tParams, ops);
                        }

                        int x = title_documentPanel.getX() + documentPanel.getX();
                        int y = panelElement.getY();

                        DocumentPanel titleCopy = (DocumentPanel) title_documentPanel.copy();
                        titleCopy.setX(x);
                        titleCopy.setY(y);

                        addElement(titleCopy, tParams, tPaper, bodyX, bodyY, bodyHeight, ops);

                        tParams.setWindage(tParams.getWindage() + title_documentPanel.getHeight());
                    }
                }

                addElement(panelElement, tParams, tPaper, bodyX, bodyY, bodyHeight, ops);
            }

            return;
        }

        if (lexElementabs instanceof DocumentPanel && lexElementabs.canSplit()
                && lexElementabs.getY() + lexElementabs.getHeight() > tParams.getPageBottom()) {

            DocumentPanel documentPanel = (DocumentPanel) lexElementabs;
            int size = documentPanel.getElementCount();
            for (int index = 0; index < size; index++) {
                DocumentElementAbs panel_Element = documentPanel.getElement(index);
                panel_Element.setX(panel_Element.getX() + documentPanel.getX());
                panel_Element.setY(panel_Element.getY() + documentPanel.getY());
                addElement(panel_Element, tParams, tPaper, bodyX, bodyY, bodyHeight, ops);
            }

            return;
        }
        if (lexElementabs instanceof DocumentReset) {
            DocumentReset lexElement = (DocumentReset) lexElementabs;
            lexElement.setY(lexElement.getY() + tParams.getWindage());

            tParams.setPageTop(lexElement.getY());
            tParams.setPageBottom(tParams.getPageTop() + bodyHeight);

            // 如果这个newpage在最后，应该删除，没有内容的页面不保留。
            buildLexPage(tPaper, tParams, ops);

            // tvs.setWindage(0);
            return;
        }
        if (lexElementabs != null) {
            lexElementabs.setY(lexElementabs.getY() + tParams.getWindage());

            if (lexElementabs.getY() + lexElementabs.getHeight() > tParams.getPageBottom()) {
                tParams.setPageTop(lexElementabs.getY());
                tParams.setPageBottom(tParams.getPageTop() + bodyHeight);

                buildLexPage(tPaper, tParams, ops);
            }

            lexElementabs.setX(bodyX + lexElementabs.getX());

            lexElementabs.setY(bodyY + lexElementabs.getY() - tParams.getPageTop());
            this.getPage(this.pageSize() - 1).add(lexElementabs);

            fill = true;

        }
    }


    private void buildLexPage(TypesetPaper tPaper, TypesetParameters tParams, ResourceOps ops) {
        LexPage page = new LexPage();
        page.setPaper(tPaper.getPaper());

        super.addPage2List(page);

        Map<String, Integer> pageMap = (Map<String, Integer>) tParams.get("PAGE");

        pageMap.put("SEQUENCE", this.pageSize());

        int typesetPanelList_size = tPaper.getTypesetPanelListSize();

        for (int index = 0; index < typesetPanelList_size; index++) {
            TypesetPanel tp = tPaper.getTemplate(index);

            DocumentElementAbs lElement = tp.build(tParams, ops);

            if (lElement != null)
                page.add(lElement);
        }

        fill = false;

    }


}
