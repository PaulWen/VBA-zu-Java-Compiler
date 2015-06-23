/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class ACStrFunctionComparisonValue extends PComparisonValue
{
    private PCStrFunction _cStrFunction_;

    public ACStrFunctionComparisonValue()
    {
        // Constructor
    }

    public ACStrFunctionComparisonValue(
        @SuppressWarnings("hiding") PCStrFunction _cStrFunction_)
    {
        // Constructor
        setCStrFunction(_cStrFunction_);

    }

    @Override
    public Object clone()
    {
        return new ACStrFunctionComparisonValue(
            cloneNode(this._cStrFunction_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseACStrFunctionComparisonValue(this);
    }

    public PCStrFunction getCStrFunction()
    {
        return this._cStrFunction_;
    }

    public void setCStrFunction(PCStrFunction node)
    {
        if(this._cStrFunction_ != null)
        {
            this._cStrFunction_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._cStrFunction_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._cStrFunction_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._cStrFunction_ == child)
        {
            this._cStrFunction_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._cStrFunction_ == oldChild)
        {
            setCStrFunction((PCStrFunction) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
