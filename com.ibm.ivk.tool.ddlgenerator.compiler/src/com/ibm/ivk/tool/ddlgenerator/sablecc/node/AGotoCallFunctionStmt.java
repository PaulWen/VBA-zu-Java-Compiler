/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AGotoCallFunctionStmt extends PFunctionStmt
{
    private PGotoCall _gotoCall_;
    private PGeneralStmt _generalStmt_;

    public AGotoCallFunctionStmt()
    {
        // Constructor
    }

    public AGotoCallFunctionStmt(
        @SuppressWarnings("hiding") PGotoCall _gotoCall_,
        @SuppressWarnings("hiding") PGeneralStmt _generalStmt_)
    {
        // Constructor
        setGotoCall(_gotoCall_);

        setGeneralStmt(_generalStmt_);

    }

    @Override
    public Object clone()
    {
        return new AGotoCallFunctionStmt(
            cloneNode(this._gotoCall_),
            cloneNode(this._generalStmt_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAGotoCallFunctionStmt(this);
    }

    public PGotoCall getGotoCall()
    {
        return this._gotoCall_;
    }

    public void setGotoCall(PGotoCall node)
    {
        if(this._gotoCall_ != null)
        {
            this._gotoCall_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._gotoCall_ = node;
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
            + toString(this._gotoCall_)
            + toString(this._generalStmt_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._gotoCall_ == child)
        {
            this._gotoCall_ = null;
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
        if(this._gotoCall_ == oldChild)
        {
            setGotoCall((PGotoCall) newChild);
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
