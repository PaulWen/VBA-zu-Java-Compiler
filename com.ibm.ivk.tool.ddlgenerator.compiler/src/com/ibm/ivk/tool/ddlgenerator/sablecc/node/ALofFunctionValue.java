/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class ALofFunctionValue extends PValue
{
    private PLofFunction _lofFunction_;

    public ALofFunctionValue()
    {
        // Constructor
    }

    public ALofFunctionValue(
        @SuppressWarnings("hiding") PLofFunction _lofFunction_)
    {
        // Constructor
        setLofFunction(_lofFunction_);

    }

    @Override
    public Object clone()
    {
        return new ALofFunctionValue(
            cloneNode(this._lofFunction_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseALofFunctionValue(this);
    }

    public PLofFunction getLofFunction()
    {
        return this._lofFunction_;
    }

    public void setLofFunction(PLofFunction node)
    {
        if(this._lofFunction_ != null)
        {
            this._lofFunction_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._lofFunction_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._lofFunction_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._lofFunction_ == child)
        {
            this._lofFunction_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._lofFunction_ == oldChild)
        {
            setLofFunction((PLofFunction) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}