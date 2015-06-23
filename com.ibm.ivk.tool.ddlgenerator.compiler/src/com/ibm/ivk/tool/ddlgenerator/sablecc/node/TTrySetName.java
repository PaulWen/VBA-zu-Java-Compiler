/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class TTrySetName extends Token
{
    public TTrySetName()
    {
        super.setText("On Error GoTo setName");
    }

    public TTrySetName(int line, int pos)
    {
        super.setText("On Error GoTo setName");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TTrySetName(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTTrySetName(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TTrySetName text.");
    }
}