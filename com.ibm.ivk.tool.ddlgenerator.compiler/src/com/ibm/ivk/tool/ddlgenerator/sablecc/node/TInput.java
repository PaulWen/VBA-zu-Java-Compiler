/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class TInput extends Token
{
    public TInput()
    {
        super.setText("Input");
    }

    public TInput(int line, int pos)
    {
        super.setText("Input");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TInput(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTInput(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TInput text.");
    }
}
