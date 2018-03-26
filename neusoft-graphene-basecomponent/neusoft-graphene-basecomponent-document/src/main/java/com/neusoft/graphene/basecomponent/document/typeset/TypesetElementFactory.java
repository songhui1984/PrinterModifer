package com.neusoft.graphene.basecomponent.document.typeset;

import com.neusoft.graphene.basecomponent.document.typeset.element.TypesetElement;

/**
 * Created by lerrain on 2017/8/18.
 */
public interface TypesetElementFactory
{
    public TypesetElement parse(Typeset typeset, Object node);
}
