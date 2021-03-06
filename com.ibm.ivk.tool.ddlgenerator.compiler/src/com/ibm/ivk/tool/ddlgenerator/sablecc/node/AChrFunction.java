/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AChrFunction extends PChrFunction
{
    private TChr _chr_;
    private TOpenParen _openParen_;
    private TNumber _number_;
    private TCloseParen _closeParen_;

    public AChrFunction()
    {
        // Constructor
    }

    public AChrFunction(
        @SuppressWarnings("hiding") TChr _chr_,
        @SuppressWarnings("hiding") TOpenParen _openParen_,
        @SuppressWarnings("hiding") TNumber _number_,
        @SuppressWarnings("hiding") TCloseParen _closeParen_)
    {
        // Constructor
        setChr(_chr_);

        setOpenParen(_openParen_);

        setNumber(_number_);

        setCloseParen(_closeParen_);

    }

    @Override
    public Object clone()
    {
        return new AChrFunction(
            cloneNode(this._chr_),
            cloneNode(this._openParen_),
            cloneNode(this._number_),
            cloneNode(this._closeParen_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAChrFunction(this);
    }

    public TChr getChr()
    {
        return this._chr_;
    }

    public void setChr(TChr node)
    {
        if(this._chr_ != null)
        {
            this._chr_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._chr_ = node;
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

    public TNumber getNumber()
    {
        return this._number_;
    }

    public void setNumber(TNumber node)
    {
        if(this._number_ != null)
        {
            this._number_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._number_ = node;
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
            + toString(this._chr_)
            + toString(this._openParen_)
            + toString(this._number_)
            + toString(this._closeParen_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._chr_ == child)
        {
            this._chr_ = null;
            return;
        }

        if(this._openParen_ == child)
        {
            this._openParen_ = null;
            return;
        }

        if(this._number_ == child)
        {
            this._number_ = null;
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
        if(this._chr_ == oldChild)
        {
            setChr((TChr) newChild);
            return;
        }

        if(this._openParen_ == oldChild)
        {
            setOpenParen((TOpenParen) newChild);
            return;
        }

        if(this._number_ == oldChild)
        {
            setNumber((TNumber) newChild);
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
