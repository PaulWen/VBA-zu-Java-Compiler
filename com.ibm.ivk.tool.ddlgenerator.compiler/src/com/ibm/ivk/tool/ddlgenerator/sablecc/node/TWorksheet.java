/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class TWorksheet extends Token
{
    public TWorksheet()
    {
        super.setText("Worksheet");
    }

    public TWorksheet(int line, int pos)
    {
        super.setText("Worksheet");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TWorksheet(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTWorksheet(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TWorksheet text.");
    }
}
