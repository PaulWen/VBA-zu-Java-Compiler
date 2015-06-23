/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AConditionMoreParamCallValues extends PMoreParamCallValues
{
    private PCondition _condition_;

    public AConditionMoreParamCallValues()
    {
        // Constructor
    }

    public AConditionMoreParamCallValues(
        @SuppressWarnings("hiding") PCondition _condition_)
    {
        // Constructor
        setCondition(_condition_);

    }

    @Override
    public Object clone()
    {
        return new AConditionMoreParamCallValues(
            cloneNode(this._condition_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAConditionMoreParamCallValues(this);
    }

    public PCondition getCondition()
    {
        return this._condition_;
    }

    public void setCondition(PCondition node)
    {
        if(this._condition_ != null)
        {
            this._condition_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._condition_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._condition_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._condition_ == child)
        {
            this._condition_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._condition_ == oldChild)
        {
            setCondition((PCondition) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}