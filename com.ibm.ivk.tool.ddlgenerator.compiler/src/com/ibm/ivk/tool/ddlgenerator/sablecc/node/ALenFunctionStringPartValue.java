/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class ALenFunctionStringPartValue extends PStringPartValue
{
    private PLenFunction _lenFunction_;

    public ALenFunctionStringPartValue()
    {
        // Constructor
    }

    public ALenFunctionStringPartValue(
        @SuppressWarnings("hiding") PLenFunction _lenFunction_)
    {
        // Constructor
        setLenFunction(_lenFunction_);

    }

    @Override
    public Object clone()
    {
        return new ALenFunctionStringPartValue(
            cloneNode(this._lenFunction_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseALenFunctionStringPartValue(this);
    }

    public PLenFunction getLenFunction()
    {
        return this._lenFunction_;
    }

    public void setLenFunction(PLenFunction node)
    {
        if(this._lenFunction_ != null)
        {
            this._lenFunction_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._lenFunction_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._lenFunction_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._lenFunction_ == child)
        {
            this._lenFunction_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._lenFunction_ == oldChild)
        {
            setLenFunction((PLenFunction) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
