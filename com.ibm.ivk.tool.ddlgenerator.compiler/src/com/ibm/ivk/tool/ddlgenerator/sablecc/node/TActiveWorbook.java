/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class TActiveWorbook extends Token
{
    public TActiveWorbook()
    {
        super.setText("ActiveWorkbook");
    }

    public TActiveWorbook(int line, int pos)
    {
        super.setText("ActiveWorkbook");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TActiveWorbook(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTActiveWorbook(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TActiveWorbook text.");
    }
}
