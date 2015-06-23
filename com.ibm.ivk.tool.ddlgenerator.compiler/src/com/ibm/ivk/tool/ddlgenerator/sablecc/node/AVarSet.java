/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AVarSet extends PVarSet
{
    private PMethodChaining _methodChaining_;
    private TAllocation _allocation_;
    private PValue _value_;

    public AVarSet()
    {
        // Constructor
    }

    public AVarSet(
        @SuppressWarnings("hiding") PMethodChaining _methodChaining_,
        @SuppressWarnings("hiding") TAllocation _allocation_,
        @SuppressWarnings("hiding") PValue _value_)
    {
        // Constructor
        setMethodChaining(_methodChaining_);

        setAllocation(_allocation_);

        setValue(_value_);

    }

    @Override
    public Object clone()
    {
        return new AVarSet(
            cloneNode(this._methodChaining_),
            cloneNode(this._allocation_),
            cloneNode(this._value_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAVarSet(this);
    }

    public PMethodChaining getMethodChaining()
    {
        return this._methodChaining_;
    }

    public void setMethodChaining(PMethodChaining node)
    {
        if(this._methodChaining_ != null)
        {
            this._methodChaining_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._methodChaining_ = node;
    }

    public TAllocation getAllocation()
    {
        return this._allocation_;
    }

    public void setAllocation(TAllocation node)
    {
        if(this._allocation_ != null)
        {
            this._allocation_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._allocation_ = node;
    }

    public PValue getValue()
    {
        return this._value_;
    }

    public void setValue(PValue node)
    {
        if(this._value_ != null)
        {
            this._value_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._value_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._methodChaining_)
            + toString(this._allocation_)
            + toString(this._value_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._methodChaining_ == child)
        {
            this._methodChaining_ = null;
            return;
        }

        if(this._allocation_ == child)
        {
            this._allocation_ = null;
            return;
        }

        if(this._value_ == child)
        {
            this._value_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._methodChaining_ == oldChild)
        {
            setMethodChaining((PMethodChaining) newChild);
            return;
        }

        if(this._allocation_ == oldChild)
        {
            setAllocation((TAllocation) newChild);
            return;
        }

        if(this._value_ == oldChild)
        {
            setValue((PValue) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
