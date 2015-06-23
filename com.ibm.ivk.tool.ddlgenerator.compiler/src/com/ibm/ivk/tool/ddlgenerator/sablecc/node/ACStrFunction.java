/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class ACStrFunction extends PCStrFunction
{
    private TCStr _cStr_;
    private TOpenParen _openParen_;
    private PValue _value_;
    private TCloseParen _closeParen_;

    public ACStrFunction()
    {
        // Constructor
    }

    public ACStrFunction(
        @SuppressWarnings("hiding") TCStr _cStr_,
        @SuppressWarnings("hiding") TOpenParen _openParen_,
        @SuppressWarnings("hiding") PValue _value_,
        @SuppressWarnings("hiding") TCloseParen _closeParen_)
    {
        // Constructor
        setCStr(_cStr_);

        setOpenParen(_openParen_);

        setValue(_value_);

        setCloseParen(_closeParen_);

    }

    @Override
    public Object clone()
    {
        return new ACStrFunction(
            cloneNode(this._cStr_),
            cloneNode(this._openParen_),
            cloneNode(this._value_),
            cloneNode(this._closeParen_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseACStrFunction(this);
    }

    public TCStr getCStr()
    {
        return this._cStr_;
    }

    public void setCStr(TCStr node)
    {
        if(this._cStr_ != null)
        {
            this._cStr_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._cStr_ = node;
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

    public PValue getValue()
    {
        return this._value_;
    }

    public void setValue(PValue node)
    {
        if(this._value_ != null)
        {
            this._value_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._value_ = node;
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
            + toString(this._cStr_)
            + toString(this._openParen_)
            + toString(this._value_)
            + toString(this._closeParen_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._cStr_ == child)
        {
            this._cStr_ = null;
            return;
        }

        if(this._openParen_ == child)
        {
            this._openParen_ = null;
            return;
        }

        if(this._value_ == child)
        {
            this._value_ = null;
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
        if(this._cStr_ == oldChild)
        {
            setCStr((TCStr) newChild);
            return;
        }

        if(this._openParen_ == oldChild)
        {
            setOpenParen((TOpenParen) newChild);
            return;
        }

        if(this._value_ == oldChild)
        {
            setValue((PValue) newChild);
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