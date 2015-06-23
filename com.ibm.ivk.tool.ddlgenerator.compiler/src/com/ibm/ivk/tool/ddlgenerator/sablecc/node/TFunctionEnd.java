/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class TFunctionEnd extends Token
{
    public TFunctionEnd()
    {
        super.setText("End Function");
    }

    public TFunctionEnd(int line, int pos)
    {
        super.setText("End Function");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TFunctionEnd(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTFunctionEnd(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TFunctionEnd text.");
    }
}
