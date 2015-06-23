/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class ANotValueCondition extends PCondition
{
    private TNot _not_;
    private PLogicalValue _logicalValue_;

    public ANotValueCondition()
    {
        // Constructor
    }

    public ANotValueCondition(
        @SuppressWarnings("hiding") TNot _not_,
        @SuppressWarnings("hiding") PLogicalValue _logicalValue_)
    {
        // Constructor
        setNot(_not_);

        setLogicalValue(_logicalValue_);

    }

    @Override
    public Object clone()
    {
        return new ANotValueCondition(
            cloneNode(this._not_),
            cloneNode(this._logicalValue_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseANotValueCondition(this);
    }

    public TNot getNot()
    {
        return this._not_;
    }

    public void setNot(TNot node)
    {
        if(this._not_ != null)
        {
            this._not_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._not_ = node;
    }

    public PLogicalValue getLogicalValue()
    {
        return this._logicalValue_;
    }

    public void setLogicalValue(PLogicalValue node)
    {
        if(this._logicalValue_ != null)
        {
            this._logicalValue_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._logicalValue_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._not_)
            + toString(this._logicalValue_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._not_ == child)
        {
            this._not_ = null;
            return;
        }

        if(this._logicalValue_ == child)
        {
            this._logicalValue_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._not_ == oldChild)
        {
            setNot((TNot) newChild);
            return;
        }

        if(this._logicalValue_ == oldChild)
        {
            setLogicalValue((PLogicalValue) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}