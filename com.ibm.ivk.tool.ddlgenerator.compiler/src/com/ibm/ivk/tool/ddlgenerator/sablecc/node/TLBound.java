/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class TLBound extends Token
{
    public TLBound()
    {
        super.setText("LBound");
    }

    public TLBound(int line, int pos)
    {
        super.setText("LBound");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TLBound(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTLBound(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TLBound text.");
    }
}
