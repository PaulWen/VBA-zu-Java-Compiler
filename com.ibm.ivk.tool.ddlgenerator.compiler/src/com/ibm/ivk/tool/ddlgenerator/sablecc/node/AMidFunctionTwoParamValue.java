/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AMidFunctionTwoParamValue extends PValue
{
    private PMidFunctionTwoParam _midFunctionTwoParam_;

    public AMidFunctionTwoParamValue()
    {
        // Constructor
    }

    public AMidFunctionTwoParamValue(
        @SuppressWarnings("hiding") PMidFunctionTwoParam _midFunctionTwoParam_)
    {
        // Constructor
        setMidFunctionTwoParam(_midFunctionTwoParam_);

    }

    @Override
    public Object clone()
    {
        return new AMidFunctionTwoParamValue(
            cloneNode(this._midFunctionTwoParam_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAMidFunctionTwoParamValue(this);
    }

    public PMidFunctionTwoParam getMidFunctionTwoParam()
    {
        return this._midFunctionTwoParam_;
    }

    public void setMidFunctionTwoParam(PMidFunctionTwoParam node)
    {
        if(this._midFunctionTwoParam_ != null)
        {
            this._midFunctionTwoParam_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._midFunctionTwoParam_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._midFunctionTwoParam_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._midFunctionTwoParam_ == child)
        {
            this._midFunctionTwoParam_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._midFunctionTwoParam_ == oldChild)
        {
            setMidFunctionTwoParam((PMidFunctionTwoParam) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
