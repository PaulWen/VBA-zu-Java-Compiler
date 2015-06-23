/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AParamCallListWithoutParensPart extends PParamCallListWithoutParensPart
{
    private PParamCallListWithoutParensValue _paramCallListWithoutParensValue_;
    private TComma _comma_;

    public AParamCallListWithoutParensPart()
    {
        // Constructor
    }

    public AParamCallListWithoutParensPart(
        @SuppressWarnings("hiding") PParamCallListWithoutParensValue _paramCallListWithoutParensValue_,
        @SuppressWarnings("hiding") TComma _comma_)
    {
        // Constructor
        setParamCallListWithoutParensValue(_paramCallListWithoutParensValue_);

        setComma(_comma_);

    }

    @Override
    public Object clone()
    {
        return new AParamCallListWithoutParensPart(
            cloneNode(this._paramCallListWithoutParensValue_),
            cloneNode(this._comma_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAParamCallListWithoutParensPart(this);
    }

    public PParamCallListWithoutParensValue getParamCallListWithoutParensValue()
    {
        return this._paramCallListWithoutParensValue_;
    }

    public void setParamCallListWithoutParensValue(PParamCallListWithoutParensValue node)
    {
        if(this._paramCallListWithoutParensValue_ != null)
        {
            this._paramCallListWithoutParensValue_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._paramCallListWithoutParensValue_ = node;
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
            + toString(this._paramCallListWithoutParensValue_)
            + toString(this._comma_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._paramCallListWithoutParensValue_ == child)
        {
            this._paramCallListWithoutParensValue_ = null;
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
        if(this._paramCallListWithoutParensValue_ == oldChild)
        {
            setParamCallListWithoutParensValue((PParamCallListWithoutParensValue) newChild);
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