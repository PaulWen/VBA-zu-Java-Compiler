/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class TWorkbook extends Token
{
    public TWorkbook()
    {
        super.setText("Workbook");
    }

    public TWorkbook(int line, int pos)
    {
        super.setText("Workbook");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TWorkbook(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTWorkbook(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TWorkbook text.");
    }
}
