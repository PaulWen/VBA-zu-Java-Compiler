/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class ACommentGeneralStmt extends PGeneralStmt
{
    private PComment _comment_;

    public ACommentGeneralStmt()
    {
        // Constructor
    }

    public ACommentGeneralStmt(
        @SuppressWarnings("hiding") PComment _comment_)
    {
        // Constructor
        setComment(_comment_);

    }

    @Override
    public Object clone()
    {
        return new ACommentGeneralStmt(
            cloneNode(this._comment_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseACommentGeneralStmt(this);
    }

    public PComment getComment()
    {
        return this._comment_;
    }

    public void setComment(PComment node)
    {
        if(this._comment_ != null)
        {
            this._comment_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._comment_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._comment_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._comment_ == child)
        {
            this._comment_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._comment_ == oldChild)
        {
            setComment((PComment) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}