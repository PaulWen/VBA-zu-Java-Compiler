/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AMidFunctionMoreParamCallValues extends PMoreParamCallValues
{
    private PMidFunction _midFunction_;

    public AMidFunctionMoreParamCallValues()
    {
        // Constructor
    }

    public AMidFunctionMoreParamCallValues(
        @SuppressWarnings("hiding") PMidFunction _midFunction_)
    {
        // Constructor
        setMidFunction(_midFunction_);

    }

    @Override
    public Object clone()
    {
        return new AMidFunctionMoreParamCallValues(
            cloneNode(this._midFunction_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAMidFunctionMoreParamCallValues(this);
    }

    public PMidFunction getMidFunction()
    {
        return this._midFunction_;
    }

    public void setMidFunction(PMidFunction node)
    {
        if(this._midFunction_ != null)
        {
            this._midFunction_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._midFunction_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._midFunction_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._midFunction_ == child)
        {
            this._midFunction_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._midFunction_ == oldChild)
        {
            setMidFunction((PMidFunction) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}