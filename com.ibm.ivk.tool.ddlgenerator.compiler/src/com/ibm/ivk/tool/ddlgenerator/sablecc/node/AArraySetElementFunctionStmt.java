/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AArraySetElementFunctionStmt extends PFunctionStmt
{
    private PArraySetElement _arraySetElement_;
    private PGeneralStmt _generalStmt_;

    public AArraySetElementFunctionStmt()
    {
        // Constructor
    }

    public AArraySetElementFunctionStmt(
        @SuppressWarnings("hiding") PArraySetElement _arraySetElement_,
        @SuppressWarnings("hiding") PGeneralStmt _generalStmt_)
    {
        // Constructor
        setArraySetElement(_arraySetElement_);

        setGeneralStmt(_generalStmt_);

    }

    @Override
    public Object clone()
    {
        return new AArraySetElementFunctionStmt(
            cloneNode(this._arraySetElement_),
            cloneNode(this._generalStmt_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAArraySetElementFunctionStmt(this);
    }

    public PArraySetElement getArraySetElement()
    {
        return this._arraySetElement_;
    }

    public void setArraySetElement(PArraySetElement node)
    {
        if(this._arraySetElement_ != null)
        {
            this._arraySetElement_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._arraySetElement_ = node;
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
            + toString(this._arraySetElement_)
            + toString(this._generalStmt_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._arraySetElement_ == child)
        {
            this._arraySetElement_ = null;
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
        if(this._arraySetElement_ == oldChild)
        {
            setArraySetElement((PArraySetElement) newChild);
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