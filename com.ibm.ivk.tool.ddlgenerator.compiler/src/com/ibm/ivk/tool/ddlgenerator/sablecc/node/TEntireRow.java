/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class TEntireRow extends Token
{
    public TEntireRow()
    {
        super.setText("EntireRow");
    }

    public TEntireRow(int line, int pos)
    {
        super.setText("EntireRow");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TEntireRow(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTEntireRow(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TEntireRow text.");
    }
}
