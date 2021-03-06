/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AConstModifier extends PModifier
{
    private TConst _const_;

    public AConstModifier()
    {
        // Constructor
    }

    public AConstModifier(
        @SuppressWarnings("hiding") TConst _const_)
    {
        // Constructor
        setConst(_const_);

    }

    @Override
    public Object clone()
    {
        return new AConstModifier(
            cloneNode(this._const_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAConstModifier(this);
    }

    public TConst getConst()
    {
        return this._const_;
    }

    public void setConst(TConst node)
    {
        if(this._const_ != null)
        {
            this._const_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._const_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._const_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._const_ == child)
        {
            this._const_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._const_ == oldChild)
        {
            setConst((TConst) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
