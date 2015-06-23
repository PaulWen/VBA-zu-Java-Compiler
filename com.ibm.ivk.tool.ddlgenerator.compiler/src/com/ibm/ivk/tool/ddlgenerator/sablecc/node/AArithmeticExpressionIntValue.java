/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AArithmeticExpressionIntValue extends PIntValue
{
    private PArithmeticExpression _arithmeticExpression_;

    public AArithmeticExpressionIntValue()
    {
        // Constructor
    }

    public AArithmeticExpressionIntValue(
        @SuppressWarnings("hiding") PArithmeticExpression _arithmeticExpression_)
    {
        // Constructor
        setArithmeticExpression(_arithmeticExpression_);

    }

    @Override
    public Object clone()
    {
        return new AArithmeticExpressionIntValue(
            cloneNode(this._arithmeticExpression_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAArithmeticExpressionIntValue(this);
    }

    public PArithmeticExpression getArithmeticExpression()
    {
        return this._arithmeticExpression_;
    }

    public void setArithmeticExpression(PArithmeticExpression node)
    {
        if(this._arithmeticExpression_ != null)
        {
            this._arithmeticExpression_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._arithmeticExpression_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._arithmeticExpression_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._arithmeticExpression_ == child)
        {
            this._arithmeticExpression_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._arithmeticExpression_ == oldChild)
        {
            setArithmeticExpression((PArithmeticExpression) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}