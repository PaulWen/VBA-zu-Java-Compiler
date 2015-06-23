/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class ARTrimFunction extends PRTrimFunction
{
    private TRTrim _rTrim_;
    private TOpenParen _openParen_;
    private PStringValue _stringValue_;
    private TCloseParen _closeParen_;

    public ARTrimFunction()
    {
        // Constructor
    }

    public ARTrimFunction(
        @SuppressWarnings("hiding") TRTrim _rTrim_,
        @SuppressWarnings("hiding") TOpenParen _openParen_,
        @SuppressWarnings("hiding") PStringValue _stringValue_,
        @SuppressWarnings("hiding") TCloseParen _closeParen_)
    {
        // Constructor
        setRTrim(_rTrim_);

        setOpenParen(_openParen_);

        setStringValue(_stringValue_);

        setCloseParen(_closeParen_);

    }

    @Override
    public Object clone()
    {
        return new ARTrimFunction(
            cloneNode(this._rTrim_),
            cloneNode(this._openParen_),
            cloneNode(this._stringValue_),
            cloneNode(this._closeParen_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseARTrimFunction(this);
    }

    public TRTrim getRTrim()
    {
        return this._rTrim_;
    }

    public void setRTrim(TRTrim node)
    {
        if(this._rTrim_ != null)
        {
            this._rTrim_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._rTrim_ = node;
    }

    public TOpenParen getOpenParen()
    {
        return this._openParen_;
    }

    public void setOpenParen(TOpenParen node)
    {
        if(this._openParen_ != null)
        {
            this._openParen_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._openParen_ = node;
    }

    public PStringValue getStringValue()
    {
        return this._stringValue_;
    }

    public void setStringValue(PStringValue node)
    {
        if(this._stringValue_ != null)
        {
            this._stringValue_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._stringValue_ = node;
    }

    public TCloseParen getCloseParen()
    {
        return this._closeParen_;
    }

    public void setCloseParen(TCloseParen node)
    {
        if(this._closeParen_ != null)
        {
            this._closeParen_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._closeParen_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._rTrim_)
            + toString(this._openParen_)
            + toString(this._stringValue_)
            + toString(this._closeParen_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._rTrim_ == child)
        {
            this._rTrim_ = null;
            return;
        }

        if(this._openParen_ == child)
        {
            this._openParen_ = null;
            return;
        }

        if(this._stringValue_ == child)
        {
            this._stringValue_ = null;
            return;
        }

        if(this._closeParen_ == child)
        {
            this._closeParen_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._rTrim_ == oldChild)
        {
            setRTrim((TRTrim) newChild);
            return;
        }

        if(this._openParen_ == oldChild)
        {
            setOpenParen((TOpenParen) newChild);
            return;
        }

        if(this._stringValue_ == oldChild)
        {
            setStringValue((PStringValue) newChild);
            return;
        }

        if(this._closeParen_ == oldChild)
        {
            setCloseParen((TCloseParen) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
