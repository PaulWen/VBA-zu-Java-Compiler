/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class TSelectedSheets extends Token
{
    public TSelectedSheets()
    {
        super.setText("SelectedSheets");
    }

    public TSelectedSheets(int line, int pos)
    {
        super.setText("SelectedSheets");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TSelectedSheets(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTSelectedSheets(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TSelectedSheets text.");
    }
}
