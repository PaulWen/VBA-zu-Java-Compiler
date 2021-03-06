/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AGreaterThanOrEqualLogicalOperator extends PLogicalOperator
{
    private TGreaterThanOrEqual _greaterThanOrEqual_;

    public AGreaterThanOrEqualLogicalOperator()
    {
        // Constructor
    }

    public AGreaterThanOrEqualLogicalOperator(
        @SuppressWarnings("hiding") TGreaterThanOrEqual _greaterThanOrEqual_)
    {
        // Constructor
        setGreaterThanOrEqual(_greaterThanOrEqual_);

    }

    @Override
    public Object clone()
    {
        return new AGreaterThanOrEqualLogicalOperator(
            cloneNode(this._greaterThanOrEqual_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAGreaterThanOrEqualLogicalOperator(this);
    }

    public TGreaterThanOrEqual getGreaterThanOrEqual()
    {
        return this._greaterThanOrEqual_;
    }

    public void setGreaterThanOrEqual(TGreaterThanOrEqual node)
    {
        if(this._greaterThanOrEqual_ != null)
        {
            this._greaterThanOrEqual_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._greaterThanOrEqual_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._greaterThanOrEqual_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._greaterThanOrEqual_ == child)
        {
            this._greaterThanOrEqual_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._greaterThanOrEqual_ == oldChild)
        {
            setGreaterThanOrEqual((TGreaterThanOrEqual) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
