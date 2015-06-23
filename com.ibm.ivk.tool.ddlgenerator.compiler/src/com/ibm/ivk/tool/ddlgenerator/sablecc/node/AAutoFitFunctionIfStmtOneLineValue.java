/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AAutoFitFunctionIfStmtOneLineValue extends PIfStmtOneLineValue
{
    private PAutoFitFunction _autoFitFunction_;

    public AAutoFitFunctionIfStmtOneLineValue()
    {
        // Constructor
    }

    public AAutoFitFunctionIfStmtOneLineValue(
        @SuppressWarnings("hiding") PAutoFitFunction _autoFitFunction_)
    {
        // Constructor
        setAutoFitFunction(_autoFitFunction_);

    }

    @Override
    public Object clone()
    {
        return new AAutoFitFunctionIfStmtOneLineValue(
            cloneNode(this._autoFitFunction_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAAutoFitFunctionIfStmtOneLineValue(this);
    }

    public PAutoFitFunction getAutoFitFunction()
    {
        return this._autoFitFunction_;
    }

    public void setAutoFitFunction(PAutoFitFunction node)
    {
        if(this._autoFitFunction_ != null)
        {
            this._autoFitFunction_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._autoFitFunction_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._autoFitFunction_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._autoFitFunction_ == child)
        {
            this._autoFitFunction_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._autoFitFunction_ == oldChild)
        {
            setAutoFitFunction((PAutoFitFunction) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
