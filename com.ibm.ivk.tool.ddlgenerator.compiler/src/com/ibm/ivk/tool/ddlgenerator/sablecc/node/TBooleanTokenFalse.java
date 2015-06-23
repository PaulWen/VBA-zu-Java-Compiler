/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class TBooleanTokenFalse extends Token
{
    public TBooleanTokenFalse()
    {
        super.setText("False");
    }

    public TBooleanTokenFalse(int line, int pos)
    {
        super.setText("False");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TBooleanTokenFalse(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTBooleanTokenFalse(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TBooleanTokenFalse text.");
    }
}