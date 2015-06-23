/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AMethodCallInParensComparisonValue extends PComparisonValue
{
    private PMethodCallInParens _methodCallInParens_;

    public AMethodCallInParensComparisonValue()
    {
        // Constructor
    }

    public AMethodCallInParensComparisonValue(
        @SuppressWarnings("hiding") PMethodCallInParens _methodCallInParens_)
    {
        // Constructor
        setMethodCallInParens(_methodCallInParens_);

    }

    @Override
    public Object clone()
    {
        return new AMethodCallInParensComparisonValue(
            cloneNode(this._methodCallInParens_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAMethodCallInParensComparisonValue(this);
    }

    public PMethodCallInParens getMethodCallInParens()
    {
        return this._methodCallInParens_;
    }

    public void setMethodCallInParens(PMethodCallInParens node)
    {
        if(this._methodCallInParens_ != null)
        {
            this._methodCallInParens_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._methodCallInParens_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._methodCallInParens_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._methodCallInParens_ == child)
        {
            this._methodCallInParens_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._methodCallInParens_ == oldChild)
        {
            setMethodCallInParens((PMethodCallInParens) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}