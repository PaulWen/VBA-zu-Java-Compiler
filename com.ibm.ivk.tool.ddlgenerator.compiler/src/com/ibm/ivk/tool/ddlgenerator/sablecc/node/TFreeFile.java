/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class TFreeFile extends Token
{
    public TFreeFile()
    {
        super.setText("FreeFile");
    }

    public TFreeFile(int line, int pos)
    {
        super.setText("FreeFile");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TFreeFile(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTFreeFile(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TFreeFile text.");
    }
}
