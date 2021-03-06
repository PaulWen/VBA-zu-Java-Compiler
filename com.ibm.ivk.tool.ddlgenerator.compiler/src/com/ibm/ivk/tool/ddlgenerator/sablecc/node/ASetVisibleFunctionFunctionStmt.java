/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class ASetVisibleFunctionFunctionStmt extends PFunctionStmt
{
    private PSetVisibleFunction _setVisibleFunction_;
    private PGeneralStmt _generalStmt_;

    public ASetVisibleFunctionFunctionStmt()
    {
        // Constructor
    }

    public ASetVisibleFunctionFunctionStmt(
        @SuppressWarnings("hiding") PSetVisibleFunction _setVisibleFunction_,
        @SuppressWarnings("hiding") PGeneralStmt _generalStmt_)
    {
        // Constructor
        setSetVisibleFunction(_setVisibleFunction_);

        setGeneralStmt(_generalStmt_);

    }

    @Override
    public Object clone()
    {
        return new ASetVisibleFunctionFunctionStmt(
            cloneNode(this._setVisibleFunction_),
            cloneNode(this._generalStmt_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseASetVisibleFunctionFunctionStmt(this);
    }

    public PSetVisibleFunction getSetVisibleFunction()
    {
        return this._setVisibleFunction_;
    }

    public void setSetVisibleFunction(PSetVisibleFunction node)
    {
        if(this._setVisibleFunction_ != null)
        {
            this._setVisibleFunction_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._setVisibleFunction_ = node;
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
            + toString(this._setVisibleFunction_)
            + toString(this._generalStmt_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._setVisibleFunction_ == child)
        {
            this._setVisibleFunction_ = null;
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
        if(this._setVisibleFunction_ == oldChild)
        {
            setSetVisibleFunction((PSetVisibleFunction) newChild);
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
