/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AMsgBoxFunction extends PMsgBoxFunction
{
    private TMsgBox _msgBox_;
    private PParamCallList _paramCallList_;

    public AMsgBoxFunction()
    {
        // Constructor
    }

    public AMsgBoxFunction(
        @SuppressWarnings("hiding") TMsgBox _msgBox_,
        @SuppressWarnings("hiding") PParamCallList _paramCallList_)
    {
        // Constructor
        setMsgBox(_msgBox_);

        setParamCallList(_paramCallList_);

    }

    @Override
    public Object clone()
    {
        return new AMsgBoxFunction(
            cloneNode(this._msgBox_),
            cloneNode(this._paramCallList_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAMsgBoxFunction(this);
    }

    public TMsgBox getMsgBox()
    {
        return this._msgBox_;
    }

    public void setMsgBox(TMsgBox node)
    {
        if(this._msgBox_ != null)
        {
            this._msgBox_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._msgBox_ = node;
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

    @Override
    public String toString()
    {
        return ""
            + toString(this._msgBox_)
            + toString(this._paramCallList_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._msgBox_ == child)
        {
            this._msgBox_ = null;
            return;
        }

        if(this._paramCallList_ == child)
        {
            this._paramCallList_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._msgBox_ == oldChild)
        {
            setMsgBox((TMsgBox) newChild);
            return;
        }

        if(this._paramCallList_ == oldChild)
        {
            setParamCallList((PParamCallList) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
