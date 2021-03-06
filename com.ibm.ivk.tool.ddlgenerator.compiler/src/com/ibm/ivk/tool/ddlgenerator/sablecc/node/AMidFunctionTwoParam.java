/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AMidFunctionTwoParam extends PMidFunctionTwoParam
{
    private TMid _mid_;
    private TOpenParen _openParen_;
    private PStringValue _string_;
    private TComma _a_;
    private PValue _beginIndex_;
    private TCloseParen _closeParen_;

    public AMidFunctionTwoParam()
    {
        // Constructor
    }

    public AMidFunctionTwoParam(
        @SuppressWarnings("hiding") TMid _mid_,
        @SuppressWarnings("hiding") TOpenParen _openParen_,
        @SuppressWarnings("hiding") PStringValue _string_,
        @SuppressWarnings("hiding") TComma _a_,
        @SuppressWarnings("hiding") PValue _beginIndex_,
        @SuppressWarnings("hiding") TCloseParen _closeParen_)
    {
        // Constructor
        setMid(_mid_);

        setOpenParen(_openParen_);

        setString(_string_);

        setA(_a_);

        setBeginIndex(_beginIndex_);

        setCloseParen(_closeParen_);

    }

    @Override
    public Object clone()
    {
        return new AMidFunctionTwoParam(
            cloneNode(this._mid_),
            cloneNode(this._openParen_),
            cloneNode(this._string_),
            cloneNode(this._a_),
            cloneNode(this._beginIndex_),
            cloneNode(this._closeParen_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAMidFunctionTwoParam(this);
    }

    public TMid getMid()
    {
        return this._mid_;
    }

    public void setMid(TMid node)
    {
        if(this._mid_ != null)
        {
            this._mid_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._mid_ = node;
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

    public PStringValue getString()
    {
        return this._string_;
    }

    public void setString(PStringValue node)
    {
        if(this._string_ != null)
        {
            this._string_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._string_ = node;
    }

    public TComma getA()
    {
        return this._a_;
    }

    public void setA(TComma node)
    {
        if(this._a_ != null)
        {
            this._a_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._a_ = node;
    }

    public PValue getBeginIndex()
    {
        return this._beginIndex_;
    }

    public void setBeginIndex(PValue node)
    {
        if(this._beginIndex_ != null)
        {
            this._beginIndex_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._beginIndex_ = node;
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
            + toString(this._mid_)
            + toString(this._openParen_)
            + toString(this._string_)
            + toString(this._a_)
            + toString(this._beginIndex_)
            + toString(this._closeParen_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._mid_ == child)
        {
            this._mid_ = null;
            return;
        }

        if(this._openParen_ == child)
        {
            this._openParen_ = null;
            return;
        }

        if(this._string_ == child)
        {
            this._string_ = null;
            return;
        }

        if(this._a_ == child)
        {
            this._a_ = null;
            return;
        }

        if(this._beginIndex_ == child)
        {
            this._beginIndex_ = null;
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
        if(this._mid_ == oldChild)
        {
            setMid((TMid) newChild);
            return;
        }

        if(this._openParen_ == oldChild)
        {
            setOpenParen((TOpenParen) newChild);
            return;
        }

        if(this._string_ == oldChild)
        {
            setString((PStringValue) newChild);
            return;
        }

        if(this._a_ == oldChild)
        {
            setA((TComma) newChild);
            return;
        }

        if(this._beginIndex_ == oldChild)
        {
            setBeginIndex((PValue) newChild);
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
