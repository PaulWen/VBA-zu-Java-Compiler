/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class TGlobal extends Token
{
    public TGlobal()
    {
        super.setText("Global");
    }

    public TGlobal(int line, int pos)
    {
        super.setText("Global");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TGlobal(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTGlobal(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TGlobal text.");
    }
}
