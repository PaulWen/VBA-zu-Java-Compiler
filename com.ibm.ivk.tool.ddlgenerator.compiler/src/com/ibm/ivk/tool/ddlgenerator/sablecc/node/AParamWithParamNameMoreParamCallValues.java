/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AParamWithParamNameMoreParamCallValues extends PMoreParamCallValues
{
    private PParamWithParamName _paramWithParamName_;

    public AParamWithParamNameMoreParamCallValues()
    {
        // Constructor
    }

    public AParamWithParamNameMoreParamCallValues(
        @SuppressWarnings("hiding") PParamWithParamName _paramWithParamName_)
    {
        // Constructor
        setParamWithParamName(_paramWithParamName_);

    }

    @Override
    public Object clone()
    {
        return new AParamWithParamNameMoreParamCallValues(
            cloneNode(this._paramWithParamName_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAParamWithParamNameMoreParamCallValues(this);
    }

    public PParamWithParamName getParamWithParamName()
    {
        return this._paramWithParamName_;
    }

    public void setParamWithParamName(PParamWithParamName node)
    {
        if(this._paramWithParamName_ != null)
        {
            this._paramWithParamName_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._paramWithParamName_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._paramWithParamName_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._paramWithParamName_ == child)
        {
            this._paramWithParamName_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._paramWithParamName_ == oldChild)
        {
            setParamWithParamName((PParamWithParamName) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}