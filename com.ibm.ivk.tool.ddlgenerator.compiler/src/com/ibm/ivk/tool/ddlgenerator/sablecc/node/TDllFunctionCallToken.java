/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class TDllFunctionCallToken extends Token
{
    public TDllFunctionCallToken()
    {
        super.setText("SetHandleCount% 1000");
    }

    public TDllFunctionCallToken(int line, int pos)
    {
        super.setText("SetHandleCount% 1000");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TDllFunctionCallToken(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTDllFunctionCallToken(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TDllFunctionCallToken text.");
    }
}
