/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AOpenFileFunctionFunctionStmt extends PFunctionStmt
{
    private POpenFileFunction _openFileFunction_;
    private PGeneralStmt _generalStmt_;

    public AOpenFileFunctionFunctionStmt()
    {
        // Constructor
    }

    public AOpenFileFunctionFunctionStmt(
        @SuppressWarnings("hiding") POpenFileFunction _openFileFunction_,
        @SuppressWarnings("hiding") PGeneralStmt _generalStmt_)
    {
        // Constructor
        setOpenFileFunction(_openFileFunction_);

        setGeneralStmt(_generalStmt_);

    }

    @Override
    public Object clone()
    {
        return new AOpenFileFunctionFunctionStmt(
            cloneNode(this._openFileFunction_),
            cloneNode(this._generalStmt_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAOpenFileFunctionFunctionStmt(this);
    }

    public POpenFileFunction getOpenFileFunction()
    {
        return this._openFileFunction_;
    }

    public void setOpenFileFunction(POpenFileFunction node)
    {
        if(this._openFileFunction_ != null)
        {
            this._openFileFunction_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._openFileFunction_ = node;
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
            + toString(this._openFileFunction_)
            + toString(this._generalStmt_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._openFileFunction_ == child)
        {
            this._openFileFunction_ = null;
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
        if(this._openFileFunction_ == oldChild)
        {
            setOpenFileFunction((POpenFileFunction) newChild);
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