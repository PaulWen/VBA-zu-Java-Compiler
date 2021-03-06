/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class ALBoundFunctionArithmeticExpressionPartValueNotNegated extends PArithmeticExpressionPartValueNotNegated
{
    private PLBoundFunction _lBoundFunction_;

    public ALBoundFunctionArithmeticExpressionPartValueNotNegated()
    {
        // Constructor
    }

    public ALBoundFunctionArithmeticExpressionPartValueNotNegated(
        @SuppressWarnings("hiding") PLBoundFunction _lBoundFunction_)
    {
        // Constructor
        setLBoundFunction(_lBoundFunction_);

    }

    @Override
    public Object clone()
    {
        return new ALBoundFunctionArithmeticExpressionPartValueNotNegated(
            cloneNode(this._lBoundFunction_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseALBoundFunctionArithmeticExpressionPartValueNotNegated(this);
    }

    public PLBoundFunction getLBoundFunction()
    {
        return this._lBoundFunction_;
    }

    public void setLBoundFunction(PLBoundFunction node)
    {
        if(this._lBoundFunction_ != null)
        {
            this._lBoundFunction_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._lBoundFunction_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._lBoundFunction_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._lBoundFunction_ == child)
        {
            this._lBoundFunction_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._lBoundFunction_ == oldChild)
        {
            setLBoundFunction((PLBoundFunction) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
