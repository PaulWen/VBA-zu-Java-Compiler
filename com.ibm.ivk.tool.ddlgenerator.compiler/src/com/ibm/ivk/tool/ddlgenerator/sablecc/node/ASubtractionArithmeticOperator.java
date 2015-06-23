/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class ASubtractionArithmeticOperator extends PArithmeticOperator
{
    private TSubtraction _subtraction_;

    public ASubtractionArithmeticOperator()
    {
        // Constructor
    }

    public ASubtractionArithmeticOperator(
        @SuppressWarnings("hiding") TSubtraction _subtraction_)
    {
        // Constructor
        setSubtraction(_subtraction_);

    }

    @Override
    public Object clone()
    {
        return new ASubtractionArithmeticOperator(
            cloneNode(this._subtraction_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseASubtractionArithmeticOperator(this);
    }

    public TSubtraction getSubtraction()
    {
        return this._subtraction_;
    }

    public void setSubtraction(TSubtraction node)
    {
        if(this._subtraction_ != null)
        {
            this._subtraction_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._subtraction_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._subtraction_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._subtraction_ == child)
        {
            this._subtraction_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._subtraction_ == oldChild)
        {
            setSubtraction((TSubtraction) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}