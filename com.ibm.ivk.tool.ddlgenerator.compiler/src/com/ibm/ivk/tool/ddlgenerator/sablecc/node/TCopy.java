/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class TCopy extends Token
{
    public TCopy()
    {
        super.setText("Copy");
    }

    public TCopy(int line, int pos)
    {
        super.setText("Copy");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TCopy(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTCopy(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TCopy text.");
    }
}