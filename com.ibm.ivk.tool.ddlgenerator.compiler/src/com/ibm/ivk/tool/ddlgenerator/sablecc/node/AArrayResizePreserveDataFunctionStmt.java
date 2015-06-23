/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AArrayResizePreserveDataFunctionStmt extends PFunctionStmt
{
    private PArrayResizePreserveData _arrayResizePreserveData_;
    private PGeneralStmt _generalStmt_;

    public AArrayResizePreserveDataFunctionStmt()
    {
        // Constructor
    }

    public AArrayResizePreserveDataFunctionStmt(
        @SuppressWarnings("hiding") PArrayResizePreserveData _arrayResizePreserveData_,
        @SuppressWarnings("hiding") PGeneralStmt _generalStmt_)
    {
        // Constructor
        setArrayResizePreserveData(_arrayResizePreserveData_);

        setGeneralStmt(_generalStmt_);

    }

    @Override
    public Object clone()
    {
        return new AArrayResizePreserveDataFunctionStmt(
            cloneNode(this._arrayResizePreserveData_),
            cloneNode(this._generalStmt_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAArrayResizePreserveDataFunctionStmt(this);
    }

    public PArrayResizePreserveData getArrayResizePreserveData()
    {
        return this._arrayResizePreserveData_;
    }

    public void setArrayResizePreserveData(PArrayResizePreserveData node)
    {
        if(this._arrayResizePreserveData_ != null)
        {
            this._arrayResizePreserveData_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._arrayResizePreserveData_ = node;
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
            + toString(this._arrayResizePreserveData_)
            + toString(this._generalStmt_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._arrayResizePreserveData_ == child)
        {
            this._arrayResizePreserveData_ = null;
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
        if(this._arrayResizePreserveData_ == oldChild)
        {
            setArrayResizePreserveData((PArrayResizePreserveData) newChild);
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