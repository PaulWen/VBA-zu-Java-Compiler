/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class ALessThanOrEqualLogicalOperator extends PLogicalOperator
{
    private TLessThanOrEqual _lessThanOrEqual_;

    public ALessThanOrEqualLogicalOperator()
    {
        // Constructor
    }

    public ALessThanOrEqualLogicalOperator(
        @SuppressWarnings("hiding") TLessThanOrEqual _lessThanOrEqual_)
    {
        // Constructor
        setLessThanOrEqual(_lessThanOrEqual_);

    }

    @Override
    public Object clone()
    {
        return new ALessThanOrEqualLogicalOperator(
            cloneNode(this._lessThanOrEqual_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseALessThanOrEqualLogicalOperator(this);
    }

    public TLessThanOrEqual getLessThanOrEqual()
    {
        return this._lessThanOrEqual_;
    }

    public void setLessThanOrEqual(TLessThanOrEqual node)
    {
        if(this._lessThanOrEqual_ != null)
        {
            this._lessThanOrEqual_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._lessThanOrEqual_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._lessThanOrEqual_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._lessThanOrEqual_ == child)
        {
            this._lessThanOrEqual_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._lessThanOrEqual_ == oldChild)
        {
            setLessThanOrEqual((TLessThanOrEqual) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}