/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AMethodChainingMoreParamCallValues extends PMoreParamCallValues
{
    private PMethodChaining _methodChaining_;

    public AMethodChainingMoreParamCallValues()
    {
        // Constructor
    }

    public AMethodChainingMoreParamCallValues(
        @SuppressWarnings("hiding") PMethodChaining _methodChaining_)
    {
        // Constructor
        setMethodChaining(_methodChaining_);

    }

    @Override
    public Object clone()
    {
        return new AMethodChainingMoreParamCallValues(
            cloneNode(this._methodChaining_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAMethodChainingMoreParamCallValues(this);
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

    @Override
    public String toString()
    {
        return ""
            + toString(this._methodChaining_);
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

        throw new RuntimeException("Not a child.");
    }
}
