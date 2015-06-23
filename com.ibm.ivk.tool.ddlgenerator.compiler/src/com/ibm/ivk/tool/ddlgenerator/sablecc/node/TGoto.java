/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class TGoto extends Token
{
    public TGoto()
    {
        super.setText("GoTo");
    }

    public TGoto(int line, int pos)
    {
        super.setText("GoTo");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TGoto(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTGoto(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TGoto text.");
    }
}
