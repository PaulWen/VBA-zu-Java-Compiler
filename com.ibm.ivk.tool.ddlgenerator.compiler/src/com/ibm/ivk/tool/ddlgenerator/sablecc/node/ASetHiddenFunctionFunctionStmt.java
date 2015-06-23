/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class ASetHiddenFunctionFunctionStmt extends PFunctionStmt
{
    private PSetHiddenFunction _setHiddenFunction_;
    private PGeneralStmt _generalStmt_;

    public ASetHiddenFunctionFunctionStmt()
    {
        // Constructor
    }

    public ASetHiddenFunctionFunctionStmt(
        @SuppressWarnings("hiding") PSetHiddenFunction _setHiddenFunction_,
        @SuppressWarnings("hiding") PGeneralStmt _generalStmt_)
    {
        // Constructor
        setSetHiddenFunction(_setHiddenFunction_);

        setGeneralStmt(_generalStmt_);

    }

    @Override
    public Object clone()
    {
        return new ASetHiddenFunctionFunctionStmt(
            cloneNode(this._setHiddenFunction_),
            cloneNode(this._generalStmt_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseASetHiddenFunctionFunctionStmt(this);
    }

    public PSetHiddenFunction getSetHiddenFunction()
    {
        return this._setHiddenFunction_;
    }

    public void setSetHiddenFunction(PSetHiddenFunction node)
    {
        if(this._setHiddenFunction_ != null)
        {
            this._setHiddenFunction_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._setHiddenFunction_ = node;
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
            + toString(this._setHiddenFunction_)
            + toString(this._generalStmt_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._setHiddenFunction_ == child)
        {
            this._setHiddenFunction_ = null;
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
        if(this._setHiddenFunction_ == oldChild)
        {
            setSetHiddenFunction((PSetHiddenFunction) newChild);
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