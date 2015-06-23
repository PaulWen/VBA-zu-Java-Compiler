/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class TNotEqual extends Token
{
    public TNotEqual()
    {
        super.setText("<>");
    }

    public TNotEqual(int line, int pos)
    {
        super.setText("<>");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TNotEqual(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTNotEqual(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TNotEqual text.");
    }
}