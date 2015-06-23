/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AArithmeticExpressionInParensValue extends PValue
{
    private TOpenParen _openParen_;
    private PArithmeticExpression _arithmeticExpression_;
    private TCloseParen _closeParen_;

    public AArithmeticExpressionInParensValue()
    {
        // Constructor
    }

    public AArithmeticExpressionInParensValue(
        @SuppressWarnings("hiding") TOpenParen _openParen_,
        @SuppressWarnings("hiding") PArithmeticExpression _arithmeticExpression_,
        @SuppressWarnings("hiding") TCloseParen _closeParen_)
    {
        // Constructor
        setOpenParen(_openParen_);

        setArithmeticExpression(_arithmeticExpression_);

        setCloseParen(_closeParen_);

    }

    @Override
    public Object clone()
    {
        return new AArithmeticExpressionInParensValue(
            cloneNode(this._openParen_),
            cloneNode(this._arithmeticExpression_),
            cloneNode(this._closeParen_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAArithmeticExpressionInParensValue(this);
    }

    public TOpenParen getOpenParen()
    {
        return this._openParen_;
    }

    public void setOpenParen(TOpenParen node)
    {
        if(this._openParen_ != null)
        {
            this._openParen_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._openParen_ = node;
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

    public TCloseParen getCloseParen()
    {
        return this._closeParen_;
    }

    public void setCloseParen(TCloseParen node)
    {
        if(this._closeParen_ != null)
        {
            this._closeParen_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._closeParen_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._openParen_)
            + toString(this._arithmeticExpression_)
            + toString(this._closeParen_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._openParen_ == child)
        {
            this._openParen_ = null;
            return;
        }

        if(this._arithmeticExpression_ == child)
        {
            this._arithmeticExpression_ = null;
            return;
        }

        if(this._closeParen_ == child)
        {
            this._closeParen_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._openParen_ == oldChild)
        {
            setOpenParen((TOpenParen) newChild);
            return;
        }

        if(this._arithmeticExpression_ == oldChild)
        {
            setArithmeticExpression((PArithmeticExpression) newChild);
            return;
        }

        if(this._closeParen_ == oldChild)
        {
            setCloseParen((TCloseParen) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
