/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class TConst extends Token
{
    public TConst()
    {
        super.setText("Const");
    }

    public TConst(int line, int pos)
    {
        super.setText("Const");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TConst(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTConst(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TConst text.");
    }
}