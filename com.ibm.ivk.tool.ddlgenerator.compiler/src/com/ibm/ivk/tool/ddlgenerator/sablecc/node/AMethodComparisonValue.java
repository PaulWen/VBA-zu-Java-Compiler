/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AMethodComparisonValue extends PComparisonValue
{
    private PMethodCall _methodCall_;

    public AMethodComparisonValue()
    {
        // Constructor
    }

    public AMethodComparisonValue(
        @SuppressWarnings("hiding") PMethodCall _methodCall_)
    {
        // Constructor
        setMethodCall(_methodCall_);

    }

    @Override
    public Object clone()
    {
        return new AMethodComparisonValue(
            cloneNode(this._methodCall_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAMethodComparisonValue(this);
    }

    public PMethodCall getMethodCall()
    {
        return this._methodCall_;
    }

    public void setMethodCall(PMethodCall node)
    {
        if(this._methodCall_ != null)
        {
            this._methodCall_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._methodCall_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._methodCall_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._methodCall_ == child)
        {
            this._methodCall_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._methodCall_ == oldChild)
        {
            setMethodCall((PMethodCall) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
