/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AExitSubFunctionStmt extends PFunctionStmt
{
    private TExitSub _exitSub_;
    private PGeneralStmt _generalStmt_;

    public AExitSubFunctionStmt()
    {
        // Constructor
    }

    public AExitSubFunctionStmt(
        @SuppressWarnings("hiding") TExitSub _exitSub_,
        @SuppressWarnings("hiding") PGeneralStmt _generalStmt_)
    {
        // Constructor
        setExitSub(_exitSub_);

        setGeneralStmt(_generalStmt_);

    }

    @Override
    public Object clone()
    {
        return new AExitSubFunctionStmt(
            cloneNode(this._exitSub_),
            cloneNode(this._generalStmt_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAExitSubFunctionStmt(this);
    }

    public TExitSub getExitSub()
    {
        return this._exitSub_;
    }

    public void setExitSub(TExitSub node)
    {
        if(this._exitSub_ != null)
        {
            this._exitSub_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._exitSub_ = node;
    }

    public PGeneralStmt getGeneralStmt()
    {
        return this._generalStmt_;
    }

    public void setGeneralStmt(PGeneralStmt node)
    {
        if(this._generalStmt_ != null)
        {
            this._generalStmt_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._generalStmt_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._exitSub_)
            + toString(this._generalStmt_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._exitSub_ == child)
        {
            this._exitSub_ = null;
            return;
        }

        if(this._generalStmt_ == child)
        {
            this._generalStmt_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._exitSub_ == oldChild)
        {
            setExitSub((TExitSub) newChild);
            return;
        }

        if(this._generalStmt_ == oldChild)
        {
            setGeneralStmt((PGeneralStmt) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
