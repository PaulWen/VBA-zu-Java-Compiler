/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AShortIfStmtArithmeticExpressionPartValueNotNegated extends PArithmeticExpressionPartValueNotNegated
{
    private PShortIfStmt _shortIfStmt_;

    public AShortIfStmtArithmeticExpressionPartValueNotNegated()
    {
        // Constructor
    }

    public AShortIfStmtArithmeticExpressionPartValueNotNegated(
        @SuppressWarnings("hiding") PShortIfStmt _shortIfStmt_)
    {
        // Constructor
        setShortIfStmt(_shortIfStmt_);

    }

    @Override
    public Object clone()
    {
        return new AShortIfStmtArithmeticExpressionPartValueNotNegated(
            cloneNode(this._shortIfStmt_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAShortIfStmtArithmeticExpressionPartValueNotNegated(this);
    }

    public PShortIfStmt getShortIfStmt()
    {
        return this._shortIfStmt_;
    }

    public void setShortIfStmt(PShortIfStmt node)
    {
        if(this._shortIfStmt_ != null)
        {
            this._shortIfStmt_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._shortIfStmt_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._shortIfStmt_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._shortIfStmt_ == child)
        {
            this._shortIfStmt_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._shortIfStmt_ == oldChild)
        {
            setShortIfStmt((PShortIfStmt) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
