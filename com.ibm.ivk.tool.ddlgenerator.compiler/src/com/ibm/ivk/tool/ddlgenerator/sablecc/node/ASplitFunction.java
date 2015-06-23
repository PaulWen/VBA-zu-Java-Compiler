/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class ASplitFunction extends PSplitFunction
{
    private TSplit _split_;
    private TOpenParen _openParen_;
    private PStringValue _string_;
    private TComma _comma_;
    private PStringValue _delimiter_;
    private TCloseParen _closeParen_;

    public ASplitFunction()
    {
        // Constructor
    }

    public ASplitFunction(
        @SuppressWarnings("hiding") TSplit _split_,
        @SuppressWarnings("hiding") TOpenParen _openParen_,
        @SuppressWarnings("hiding") PStringValue _string_,
        @SuppressWarnings("hiding") TComma _comma_,
        @SuppressWarnings("hiding") PStringValue _delimiter_,
        @SuppressWarnings("hiding") TCloseParen _closeParen_)
    {
        // Constructor
        setSplit(_split_);

        setOpenParen(_openParen_);

        setString(_string_);

        setComma(_comma_);

        setDelimiter(_delimiter_);

        setCloseParen(_closeParen_);

    }

    @Override
    public Object clone()
    {
        return new ASplitFunction(
            cloneNode(this._split_),
            cloneNode(this._openParen_),
            cloneNode(this._string_),
            cloneNode(this._comma_),
            cloneNode(this._delimiter_),
            cloneNode(this._closeParen_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseASplitFunction(this);
    }

    public TSplit getSplit()
    {
        return this._split_;
    }

    public void setSplit(TSplit node)
    {
        if(this._split_ != null)
        {
            this._split_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._split_ = node;
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

    public PStringValue getDelimiter()
    {
        return this._delimiter_;
    }

    public void setDelimiter(PStringValue node)
    {
        if(this._delimiter_ != null)
        {
            this._delimiter_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._delimiter_ = node;
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
            + toString(this._split_)
            + toString(this._openParen_)
            + toString(this._string_)
            + toString(this._comma_)
            + toString(this._delimiter_)
            + toString(this._closeParen_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._split_ == child)
        {
            this._split_ = null;
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

        if(this._comma_ == child)
        {
            this._comma_ = null;
            return;
        }

        if(this._delimiter_ == child)
        {
            this._delimiter_ = null;
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
        if(this._split_ == oldChild)
        {
            setSplit((TSplit) newChild);
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

        if(this._comma_ == oldChild)
        {
            setComma((TComma) newChild);
            return;
        }

        if(this._delimiter_ == oldChild)
        {
            setDelimiter((PStringValue) newChild);
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
