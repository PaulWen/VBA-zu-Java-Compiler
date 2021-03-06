/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AArithmeticExpressionPart extends PArithmeticExpressionPart
{
    private PArithmeticExpressionPartValue _arithmeticExpressionPartValue_;
    private PArithmeticOperator _arithmeticOperator_;

    public AArithmeticExpressionPart()
    {
        // Constructor
    }

    public AArithmeticExpressionPart(
        @SuppressWarnings("hiding") PArithmeticExpressionPartValue _arithmeticExpressionPartValue_,
        @SuppressWarnings("hiding") PArithmeticOperator _arithmeticOperator_)
    {
        // Constructor
        setArithmeticExpressionPartValue(_arithmeticExpressionPartValue_);

        setArithmeticOperator(_arithmeticOperator_);

    }

    @Override
    public Object clone()
    {
        return new AArithmeticExpressionPart(
            cloneNode(this._arithmeticExpressionPartValue_),
            cloneNode(this._arithmeticOperator_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAArithmeticExpressionPart(this);
    }

    public PArithmeticExpressionPartValue getArithmeticExpressionPartValue()
    {
        return this._arithmeticExpressionPartValue_;
    }

    public void setArithmeticExpressionPartValue(PArithmeticExpressionPartValue node)
    {
        if(this._arithmeticExpressionPartValue_ != null)
        {
            this._arithmeticExpressionPartValue_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._arithmeticExpressionPartValue_ = node;
    }

    public PArithmeticOperator getArithmeticOperator()
    {
        return this._arithmeticOperator_;
    }

    public void setArithmeticOperator(PArithmeticOperator node)
    {
        if(this._arithmeticOperator_ != null)
        {
            this._arithmeticOperator_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._arithmeticOperator_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._arithmeticExpressionPartValue_)
            + toString(this._arithmeticOperator_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._arithmeticExpressionPartValue_ == child)
        {
            this._arithmeticExpressionPartValue_ = null;
            return;
        }

        if(this._arithmeticOperator_ == child)
        {
            this._arithmeticOperator_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._arithmeticExpressionPartValue_ == oldChild)
        {
            setArithmeticExpressionPartValue((PArithmeticExpressionPartValue) newChild);
            return;
        }

        if(this._arithmeticOperator_ == oldChild)
        {
            setArithmeticOperator((PArithmeticOperator) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
