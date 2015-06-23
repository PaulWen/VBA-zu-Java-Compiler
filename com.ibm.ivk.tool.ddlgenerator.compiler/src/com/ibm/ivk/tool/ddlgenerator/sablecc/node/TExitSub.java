/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class TExitSub extends Token
{
    public TExitSub()
    {
        super.setText("Exit Sub");
    }

    public TExitSub(int line, int pos)
    {
        super.setText("Exit Sub");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TExitSub(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTExitSub(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TExitSub text.");
    }
}
