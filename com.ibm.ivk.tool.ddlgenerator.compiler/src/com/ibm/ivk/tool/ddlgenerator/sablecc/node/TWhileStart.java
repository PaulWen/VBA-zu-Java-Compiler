/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class TWhileStart extends Token
{
    public TWhileStart()
    {
        super.setText("While");
    }

    public TWhileStart(int line, int pos)
    {
        super.setText("While");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TWhileStart(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTWhileStart(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TWhileStart text.");
    }
}
