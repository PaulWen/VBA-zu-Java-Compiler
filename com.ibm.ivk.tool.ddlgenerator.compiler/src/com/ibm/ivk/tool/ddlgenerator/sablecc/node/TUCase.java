/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class TUCase extends Token
{
    public TUCase()
    {
        super.setText("UCase");
    }

    public TUCase(int line, int pos)
    {
        super.setText("UCase");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TUCase(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTUCase(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TUCase text.");
    }
}
