/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class ADllFunctionCall extends PDllFunctionCall
{
    private PComment _comment_;
    private TDllFunctionCallToken _dllFunctionCallToken_;

    public ADllFunctionCall()
    {
        // Constructor
    }

    public ADllFunctionCall(
        @SuppressWarnings("hiding") PComment _comment_,
        @SuppressWarnings("hiding") TDllFunctionCallToken _dllFunctionCallToken_)
    {
        // Constructor
        setComment(_comment_);

        setDllFunctionCallToken(_dllFunctionCallToken_);

    }

    @Override
    public Object clone()
    {
        return new ADllFunctionCall(
            cloneNode(this._comment_),
            cloneNode(this._dllFunctionCallToken_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseADllFunctionCall(this);
    }

    public PComment getComment()
    {
        return this._comment_;
    }

    public void setComment(PComment node)
    {
        if(this._comment_ != null)
        {
            this._comment_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._comment_ = node;
    }

    public TDllFunctionCallToken getDllFunctionCallToken()
    {
        return this._dllFunctionCallToken_;
    }

    public void setDllFunctionCallToken(TDllFunctionCallToken node)
    {
        if(this._dllFunctionCallToken_ != null)
        {
            this._dllFunctionCallToken_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._dllFunctionCallToken_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._comment_)
            + toString(this._dllFunctionCallToken_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._comment_ == child)
        {
            this._comment_ = null;
            return;
        }

        if(this._dllFunctionCallToken_ == child)
        {
            this._dllFunctionCallToken_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._comment_ == oldChild)
        {
            setComment((PComment) newChild);
            return;
        }

        if(this._dllFunctionCallToken_ == oldChild)
        {
            setDllFunctionCallToken((TDllFunctionCallToken) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
