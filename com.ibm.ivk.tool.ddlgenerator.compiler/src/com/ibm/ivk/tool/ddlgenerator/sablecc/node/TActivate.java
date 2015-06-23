/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class TActivate extends Token
{
    public TActivate()
    {
        super.setText("Activate");
    }

    public TActivate(int line, int pos)
    {
        super.setText("Activate");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TActivate(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTActivate(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TActivate text.");
    }
}
