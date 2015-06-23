/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class ADivisionArithmeticOperator extends PArithmeticOperator
{
    private TDivision _division_;

    public ADivisionArithmeticOperator()
    {
        // Constructor
    }

    public ADivisionArithmeticOperator(
        @SuppressWarnings("hiding") TDivision _division_)
    {
        // Constructor
        setDivision(_division_);

    }

    @Override
    public Object clone()
    {
        return new ADivisionArithmeticOperator(
            cloneNode(this._division_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseADivisionArithmeticOperator(this);
    }

    public TDivision getDivision()
    {
        return this._division_;
    }

    public void setDivision(TDivision node)
    {
        if(this._division_ != null)
        {
            this._division_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._division_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._division_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._division_ == child)
        {
            this._division_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._division_ == oldChild)
        {
            setDivision((TDivision) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
