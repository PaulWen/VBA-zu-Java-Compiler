/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AMethodCallWithoutParensFunctionStmt extends PFunctionStmt
{
    private PMethodCallWithoutParens _methodCallWithoutParens_;
    private PGeneralStmt _generalStmt_;

    public AMethodCallWithoutParensFunctionStmt()
    {
        // Constructor
    }

    public AMethodCallWithoutParensFunctionStmt(
        @SuppressWarnings("hiding") PMethodCallWithoutParens _methodCallWithoutParens_,
        @SuppressWarnings("hiding") PGeneralStmt _generalStmt_)
    {
        // Constructor
        setMethodCallWithoutParens(_methodCallWithoutParens_);

        setGeneralStmt(_generalStmt_);

    }

    @Override
    public Object clone()
    {
        return new AMethodCallWithoutParensFunctionStmt(
            cloneNode(this._methodCallWithoutParens_),
            cloneNode(this._generalStmt_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAMethodCallWithoutParensFunctionStmt(this);
    }

    public PMethodCallWithoutParens getMethodCallWithoutParens()
    {
        return this._methodCallWithoutParens_;
    }

    public void setMethodCallWithoutParens(PMethodCallWithoutParens node)
    {
        if(this._methodCallWithoutParens_ != null)
        {
            this._methodCallWithoutParens_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._methodCallWithoutParens_ = node;
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
            + toString(this._methodCallWithoutParens_)
            + toString(this._generalStmt_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._methodCallWithoutParens_ == child)
        {
            this._methodCallWithoutParens_ = null;
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
        if(this._methodCallWithoutParens_ == oldChild)
        {
            setMethodCallWithoutParens((PMethodCallWithoutParens) newChild);
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