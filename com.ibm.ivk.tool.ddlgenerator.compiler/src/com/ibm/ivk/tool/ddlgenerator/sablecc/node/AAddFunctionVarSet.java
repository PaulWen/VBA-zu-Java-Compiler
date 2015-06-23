/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AAddFunctionVarSet extends PVarSet
{
    private PMethodChaining _methodChaining_;
    private TAllocation _allocation_;
    private PAddFunction _addFunction_;

    public AAddFunctionVarSet()
    {
        // Constructor
    }

    public AAddFunctionVarSet(
        @SuppressWarnings("hiding") PMethodChaining _methodChaining_,
        @SuppressWarnings("hiding") TAllocation _allocation_,
        @SuppressWarnings("hiding") PAddFunction _addFunction_)
    {
        // Constructor
        setMethodChaining(_methodChaining_);

        setAllocation(_allocation_);

        setAddFunction(_addFunction_);

    }

    @Override
    public Object clone()
    {
        return new AAddFunctionVarSet(
            cloneNode(this._methodChaining_),
            cloneNode(this._allocation_),
            cloneNode(this._addFunction_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAAddFunctionVarSet(this);
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

    public PAddFunction getAddFunction()
    {
        return this._addFunction_;
    }

    public void setAddFunction(PAddFunction node)
    {
        if(this._addFunction_ != null)
        {
            this._addFunction_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._addFunction_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._methodChaining_)
            + toString(this._allocation_)
            + toString(this._addFunction_);
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

        if(this._addFunction_ == child)
        {
            this._addFunction_ = null;
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

        if(this._addFunction_ == oldChild)
        {
            setAddFunction((PAddFunction) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}