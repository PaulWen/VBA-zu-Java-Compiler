/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class TRTrim extends Token
{
    public TRTrim()
    {
        super.setText("RTrim");
    }

    public TRTrim(int line, int pos)
    {
        super.setText("RTrim");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TRTrim(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTRTrim(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TRTrim text.");
    }
}
