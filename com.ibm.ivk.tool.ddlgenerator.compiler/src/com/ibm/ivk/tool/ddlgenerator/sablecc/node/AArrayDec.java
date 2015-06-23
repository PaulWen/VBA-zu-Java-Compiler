/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AArrayDec extends PArrayDec
{
    private PModifierId _modifierId_;
    private PParamCallList _paramCallList_;
    private TAs _as_;
    private PDataType _dataType_;

    public AArrayDec()
    {
        // Constructor
    }

    public AArrayDec(
        @SuppressWarnings("hiding") PModifierId _modifierId_,
        @SuppressWarnings("hiding") PParamCallList _paramCallList_,
        @SuppressWarnings("hiding") TAs _as_,
        @SuppressWarnings("hiding") PDataType _dataType_)
    {
        // Constructor
        setModifierId(_modifierId_);

        setParamCallList(_paramCallList_);

        setAs(_as_);

        setDataType(_dataType_);

    }

    @Override
    public Object clone()
    {
        return new AArrayDec(
            cloneNode(this._modifierId_),
            cloneNode(this._paramCallList_),
            cloneNode(this._as_),
            cloneNode(this._dataType_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAArrayDec(this);
    }

    public PModifierId getModifierId()
    {
        return this._modifierId_;
    }

    public void setModifierId(PModifierId node)
    {
        if(this._modifierId_ != null)
        {
            this._modifierId_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._modifierId_ = node;
    }

    public PParamCallList getParamCallList()
    {
        return this._paramCallList_;
    }

    public void setParamCallList(PParamCallList node)
    {
        if(this._paramCallList_ != null)
        {
            this._paramCallList_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._paramCallList_ = node;
    }

    public TAs getAs()
    {
        return this._as_;
    }

    public void setAs(TAs node)
    {
        if(this._as_ != null)
        {
            this._as_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._as_ = node;
    }

    public PDataType getDataType()
    {
        return this._dataType_;
    }

    public void setDataType(PDataType node)
    {
        if(this._dataType_ != null)
        {
            this._dataType_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._dataType_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._modifierId_)
            + toString(this._paramCallList_)
            + toString(this._as_)
            + toString(this._dataType_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._modifierId_ == child)
        {
            this._modifierId_ = null;
            return;
        }

        if(this._paramCallList_ == child)
        {
            this._paramCallList_ = null;
            return;
        }

        if(this._as_ == child)
        {
            this._as_ = null;
            return;
        }

        if(this._dataType_ == child)
        {
            this._dataType_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._modifierId_ == oldChild)
        {
            setModifierId((PModifierId) newChild);
            return;
        }

        if(this._paramCallList_ == oldChild)
        {
            setParamCallList((PParamCallList) newChild);
            return;
        }

        if(this._as_ == oldChild)
        {
            setAs((TAs) newChild);
            return;
        }

        if(this._dataType_ == oldChild)
        {
            setDataType((PDataType) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
