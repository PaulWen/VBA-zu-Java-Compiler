/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class TIn extends Token
{
    public TIn()
    {
        super.setText("In");
    }

    public TIn(int line, int pos)
    {
        super.setText("In");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TIn(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTIn(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TIn text.");
    }
}
