/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class TForEnd extends Token
{
    public TForEnd()
    {
        super.setText("Next");
    }

    public TForEnd(int line, int pos)
    {
        super.setText("Next");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TForEnd(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTForEnd(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TForEnd text.");
    }
}