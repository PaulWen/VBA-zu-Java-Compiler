/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class TFormat extends Token
{
    public TFormat()
    {
        super.setText("Format");
    }

    public TFormat(int line, int pos)
    {
        super.setText("Format");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TFormat(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTFormat(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TFormat text.");
    }
}
