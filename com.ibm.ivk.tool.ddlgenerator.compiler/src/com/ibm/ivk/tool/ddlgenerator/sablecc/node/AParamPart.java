/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AParamPart extends PParamPart
{
    private PParamValue _paramValue_;
    private TComma _comma_;

    public AParamPart()
    {
        // Constructor
    }

    public AParamPart(
        @SuppressWarnings("hiding") PParamValue _paramValue_,
        @SuppressWarnings("hiding") TComma _comma_)
    {
        // Constructor
        setParamValue(_paramValue_);

        setComma(_comma_);

    }

    @Override
    public Object clone()
    {
        return new AParamPart(
            cloneNode(this._paramValue_),
            cloneNode(this._comma_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAParamPart(this);
    }

    public PParamValue getParamValue()
    {
        return this._paramValue_;
    }

    public void setParamValue(PParamValue node)
    {
        if(this._paramValue_ != null)
        {
            this._paramValue_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._paramValue_ = node;
    }

    public TComma getComma()
    {
        return this._comma_;
    }

    public void setComma(TComma node)
    {
        if(this._comma_ != null)
        {
            this._comma_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._comma_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._paramValue_)
            + toString(this._comma_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._paramValue_ == child)
        {
            this._paramValue_ = null;
            return;
        }

        if(this._comma_ == child)
        {
            this._comma_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._paramValue_ == oldChild)
        {
            setParamValue((PParamValue) newChild);
            return;
        }

        if(this._comma_ == oldChild)
        {
            setComma((TComma) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}