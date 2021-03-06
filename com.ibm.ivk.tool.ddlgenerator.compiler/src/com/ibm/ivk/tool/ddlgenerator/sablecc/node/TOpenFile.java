/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class TOpenFile extends Token
{
    public TOpenFile()
    {
        super.setText("Open");
    }

    public TOpenFile(int line, int pos)
    {
        super.setText("Open");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TOpenFile(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTOpenFile(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TOpenFile text.");
    }
}
