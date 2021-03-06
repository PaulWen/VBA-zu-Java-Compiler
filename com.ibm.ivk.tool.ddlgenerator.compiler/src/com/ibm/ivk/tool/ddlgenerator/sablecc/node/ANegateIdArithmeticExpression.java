/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class ANegateIdArithmeticExpression extends PArithmeticExpression
{
    private TSubtraction _subtraction_;
    private PId _id_;

    public ANegateIdArithmeticExpression()
    {
        // Constructor
    }

    public ANegateIdArithmeticExpression(
        @SuppressWarnings("hiding") TSubtraction _subtraction_,
        @SuppressWarnings("hiding") PId _id_)
    {
        // Constructor
        setSubtraction(_subtraction_);

        setId(_id_);

    }

    @Override
    public Object clone()
    {
        return new ANegateIdArithmeticExpression(
            cloneNode(this._subtraction_),
            cloneNode(this._id_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseANegateIdArithmeticExpression(this);
    }

    public TSubtraction getSubtraction()
    {
        return this._subtraction_;
    }

    public void setSubtraction(TSubtraction node)
    {
        if(this._subtraction_ != null)
        {
            this._subtraction_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._subtraction_ = node;
    }

    public PId getId()
    {
        return this._id_;
    }

    public void setId(PId node)
    {
        if(this._id_ != null)
        {
            this._id_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._id_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._subtraction_)
            + toString(this._id_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._subtraction_ == child)
        {
            this._subtraction_ = null;
            return;
        }

        if(this._id_ == child)
        {
            this._id_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._subtraction_ == oldChild)
        {
            setSubtraction((TSubtraction) newChild);
            return;
        }

        if(this._id_ == oldChild)
        {
            setId((PId) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
