/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class ASetCellFunctionFunctionStmt extends PFunctionStmt
{
    private PSetCellFunction _setCellFunction_;
    private PGeneralStmt _generalStmt_;

    public ASetCellFunctionFunctionStmt()
    {
        // Constructor
    }

    public ASetCellFunctionFunctionStmt(
        @SuppressWarnings("hiding") PSetCellFunction _setCellFunction_,
        @SuppressWarnings("hiding") PGeneralStmt _generalStmt_)
    {
        // Constructor
        setSetCellFunction(_setCellFunction_);

        setGeneralStmt(_generalStmt_);

    }

    @Override
    public Object clone()
    {
        return new ASetCellFunctionFunctionStmt(
            cloneNode(this._setCellFunction_),
            cloneNode(this._generalStmt_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseASetCellFunctionFunctionStmt(this);
    }

    public PSetCellFunction getSetCellFunction()
    {
        return this._setCellFunction_;
    }

    public void setSetCellFunction(PSetCellFunction node)
    {
        if(this._setCellFunction_ != null)
        {
            this._setCellFunction_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._setCellFunction_ = node;
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
            + toString(this._setCellFunction_)
            + toString(this._generalStmt_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._setCellFunction_ == child)
        {
            this._setCellFunction_ = null;
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
        if(this._setCellFunction_ == oldChild)
        {
            setSetCellFunction((PSetCellFunction) newChild);
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