/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AIfStmtFunctionStmt extends PFunctionStmt
{
    private PIfStmt _ifStmt_;
    private PGeneralStmt _generalStmt_;

    public AIfStmtFunctionStmt()
    {
        // Constructor
    }

    public AIfStmtFunctionStmt(
        @SuppressWarnings("hiding") PIfStmt _ifStmt_,
        @SuppressWarnings("hiding") PGeneralStmt _generalStmt_)
    {
        // Constructor
        setIfStmt(_ifStmt_);

        setGeneralStmt(_generalStmt_);

    }

    @Override
    public Object clone()
    {
        return new AIfStmtFunctionStmt(
            cloneNode(this._ifStmt_),
            cloneNode(this._generalStmt_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAIfStmtFunctionStmt(this);
    }

    public PIfStmt getIfStmt()
    {
        return this._ifStmt_;
    }

    public void setIfStmt(PIfStmt node)
    {
        if(this._ifStmt_ != null)
        {
            this._ifStmt_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._ifStmt_ = node;
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
            + toString(this._ifStmt_)
            + toString(this._generalStmt_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._ifStmt_ == child)
        {
            this._ifStmt_ = null;
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
        if(this._ifStmt_ == oldChild)
        {
            setIfStmt((PIfStmt) newChild);
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
