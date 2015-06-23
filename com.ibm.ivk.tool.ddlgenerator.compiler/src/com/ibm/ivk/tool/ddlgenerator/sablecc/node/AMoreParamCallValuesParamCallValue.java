/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AMoreParamCallValuesParamCallValue extends PParamCallValue
{
    private PMoreParamCallValues _moreParamCallValues_;

    public AMoreParamCallValuesParamCallValue()
    {
        // Constructor
    }

    public AMoreParamCallValuesParamCallValue(
        @SuppressWarnings("hiding") PMoreParamCallValues _moreParamCallValues_)
    {
        // Constructor
        setMoreParamCallValues(_moreParamCallValues_);

    }

    @Override
    public Object clone()
    {
        return new AMoreParamCallValuesParamCallValue(
            cloneNode(this._moreParamCallValues_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAMoreParamCallValuesParamCallValue(this);
    }

    public PMoreParamCallValues getMoreParamCallValues()
    {
        return this._moreParamCallValues_;
    }

    public void setMoreParamCallValues(PMoreParamCallValues node)
    {
        if(this._moreParamCallValues_ != null)
        {
            this._moreParamCallValues_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._moreParamCallValues_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._moreParamCallValues_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._moreParamCallValues_ == child)
        {
            this._moreParamCallValues_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._moreParamCallValues_ == oldChild)
        {
            setMoreParamCallValues((PMoreParamCallValues) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}