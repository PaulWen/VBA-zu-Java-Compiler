/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class TAddition extends Token
{
    public TAddition()
    {
        super.setText("+");
    }

    public TAddition(int line, int pos)
    {
        super.setText("+");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TAddition(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTAddition(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TAddition text.");
    }
}
