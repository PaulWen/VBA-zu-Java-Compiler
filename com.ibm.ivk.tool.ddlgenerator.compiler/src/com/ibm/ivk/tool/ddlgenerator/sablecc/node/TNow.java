/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class TNow extends Token
{
    public TNow()
    {
        super.setText("Now");
    }

    public TNow(int line, int pos)
    {
        super.setText("Now");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TNow(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTNow(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TNow text.");
    }
}