/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class ACloseFileFunction extends PCloseFileFunction
{
    private TClose _close_;
    private PMethodChaining _fileNumber_;

    public ACloseFileFunction()
    {
        // Constructor
    }

    public ACloseFileFunction(
        @SuppressWarnings("hiding") TClose _close_,
        @SuppressWarnings("hiding") PMethodChaining _fileNumber_)
    {
        // Constructor
        setClose(_close_);

        setFileNumber(_fileNumber_);

    }

    @Override
    public Object clone()
    {
        return new ACloseFileFunction(
            cloneNode(this._close_),
            cloneNode(this._fileNumber_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseACloseFileFunction(this);
    }

    public TClose getClose()
    {
        return this._close_;
    }

    public void setClose(TClose node)
    {
        if(this._close_ != null)
        {
            this._close_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._close_ = node;
    }

    public PMethodChaining getFileNumber()
    {
        return this._fileNumber_;
    }

    public void setFileNumber(PMethodChaining node)
    {
        if(this._fileNumber_ != null)
        {
            this._fileNumber_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._fileNumber_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._close_)
            + toString(this._fileNumber_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._close_ == child)
        {
            this._close_ = null;
            return;
        }

        if(this._fileNumber_ == child)
        {
            this._fileNumber_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._close_ == oldChild)
        {
            setClose((TClose) newChild);
            return;
        }

        if(this._fileNumber_ == oldChild)
        {
            setFileNumber((PMethodChaining) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
