/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AFreeFileFunction extends PFreeFileFunction
{
    private TFreeFile _freeFile_;
    private TOpenParen _openParen_;
    private TCloseParen _closeParen_;

    public AFreeFileFunction()
    {
        // Constructor
    }

    public AFreeFileFunction(
        @SuppressWarnings("hiding") TFreeFile _freeFile_,
        @SuppressWarnings("hiding") TOpenParen _openParen_,
        @SuppressWarnings("hiding") TCloseParen _closeParen_)
    {
        // Constructor
        setFreeFile(_freeFile_);

        setOpenParen(_openParen_);

        setCloseParen(_closeParen_);

    }

    @Override
    public Object clone()
    {
        return new AFreeFileFunction(
            cloneNode(this._freeFile_),
            cloneNode(this._openParen_),
            cloneNode(this._closeParen_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAFreeFileFunction(this);
    }

    public TFreeFile getFreeFile()
    {
        return this._freeFile_;
    }

    public void setFreeFile(TFreeFile node)
    {
        if(this._freeFile_ != null)
        {
            this._freeFile_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._freeFile_ = node;
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
            + toString(this._freeFile_)
            + toString(this._openParen_)
            + toString(this._closeParen_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._freeFile_ == child)
        {
            this._freeFile_ = null;
            return;
        }

        if(this._openParen_ == child)
        {
            this._openParen_ = null;
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
        if(this._freeFile_ == oldChild)
        {
            setFreeFile((TFreeFile) newChild);
            return;
        }

        if(this._openParen_ == oldChild)
        {
            setOpenParen((TOpenParen) newChild);
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
