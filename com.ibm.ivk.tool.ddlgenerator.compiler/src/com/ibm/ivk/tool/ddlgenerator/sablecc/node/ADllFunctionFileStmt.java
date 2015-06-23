/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class ADllFunctionFileStmt extends PFileStmt
{
    private PDllFunction _dllFunction_;
    private PGeneralStmt _generalStmt_;

    public ADllFunctionFileStmt()
    {
        // Constructor
    }

    public ADllFunctionFileStmt(
        @SuppressWarnings("hiding") PDllFunction _dllFunction_,
        @SuppressWarnings("hiding") PGeneralStmt _generalStmt_)
    {
        // Constructor
        setDllFunction(_dllFunction_);

        setGeneralStmt(_generalStmt_);

    }

    @Override
    public Object clone()
    {
        return new ADllFunctionFileStmt(
            cloneNode(this._dllFunction_),
            cloneNode(this._generalStmt_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseADllFunctionFileStmt(this);
    }

    public PDllFunction getDllFunction()
    {
        return this._dllFunction_;
    }

    public void setDllFunction(PDllFunction node)
    {
        if(this._dllFunction_ != null)
        {
            this._dllFunction_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._dllFunction_ = node;
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
            + toString(this._dllFunction_)
            + toString(this._generalStmt_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._dllFunction_ == child)
        {
            this._dllFunction_ = null;
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
        if(this._dllFunction_ == oldChild)
        {
            setDllFunction((PDllFunction) newChild);
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
