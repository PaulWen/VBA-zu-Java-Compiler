/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AMsgBoxFunctionWithoutParensFunctionStmt extends PFunctionStmt
{
    private PMsgBoxFunctionWithoutParens _msgBoxFunctionWithoutParens_;
    private PGeneralStmt _generalStmt_;

    public AMsgBoxFunctionWithoutParensFunctionStmt()
    {
        // Constructor
    }

    public AMsgBoxFunctionWithoutParensFunctionStmt(
        @SuppressWarnings("hiding") PMsgBoxFunctionWithoutParens _msgBoxFunctionWithoutParens_,
        @SuppressWarnings("hiding") PGeneralStmt _generalStmt_)
    {
        // Constructor
        setMsgBoxFunctionWithoutParens(_msgBoxFunctionWithoutParens_);

        setGeneralStmt(_generalStmt_);

    }

    @Override
    public Object clone()
    {
        return new AMsgBoxFunctionWithoutParensFunctionStmt(
            cloneNode(this._msgBoxFunctionWithoutParens_),
            cloneNode(this._generalStmt_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAMsgBoxFunctionWithoutParensFunctionStmt(this);
    }

    public PMsgBoxFunctionWithoutParens getMsgBoxFunctionWithoutParens()
    {
        return this._msgBoxFunctionWithoutParens_;
    }

    public void setMsgBoxFunctionWithoutParens(PMsgBoxFunctionWithoutParens node)
    {
        if(this._msgBoxFunctionWithoutParens_ != null)
        {
            this._msgBoxFunctionWithoutParens_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._msgBoxFunctionWithoutParens_ = node;
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
            + toString(this._msgBoxFunctionWithoutParens_)
            + toString(this._generalStmt_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._msgBoxFunctionWithoutParens_ == child)
        {
            this._msgBoxFunctionWithoutParens_ = null;
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
        if(this._msgBoxFunctionWithoutParens_ == oldChild)
        {
            setMsgBoxFunctionWithoutParens((PMsgBoxFunctionWithoutParens) newChild);
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