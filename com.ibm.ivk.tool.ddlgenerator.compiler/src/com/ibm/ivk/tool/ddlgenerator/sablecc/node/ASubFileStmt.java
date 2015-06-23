/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class ASubFileStmt extends PFileStmt
{
    private PSub _sub_;
    private PGeneralStmt _generalStmt_;

    public ASubFileStmt()
    {
        // Constructor
    }

    public ASubFileStmt(
        @SuppressWarnings("hiding") PSub _sub_,
        @SuppressWarnings("hiding") PGeneralStmt _generalStmt_)
    {
        // Constructor
        setSub(_sub_);

        setGeneralStmt(_generalStmt_);

    }

    @Override
    public Object clone()
    {
        return new ASubFileStmt(
            cloneNode(this._sub_),
            cloneNode(this._generalStmt_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseASubFileStmt(this);
    }

    public PSub getSub()
    {
        return this._sub_;
    }

    public void setSub(PSub node)
    {
        if(this._sub_ != null)
        {
            this._sub_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._sub_ = node;
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
            + toString(this._sub_)
            + toString(this._generalStmt_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._sub_ == child)
        {
            this._sub_ = null;
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
        if(this._sub_ == oldChild)
        {
            setSub((PSub) newChild);
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