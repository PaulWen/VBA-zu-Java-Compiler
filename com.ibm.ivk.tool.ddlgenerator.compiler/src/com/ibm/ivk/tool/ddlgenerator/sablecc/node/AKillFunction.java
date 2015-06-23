/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AKillFunction extends PKillFunction
{
    private TKill _kill_;
    private PStringValue _stringValue_;

    public AKillFunction()
    {
        // Constructor
    }

    public AKillFunction(
        @SuppressWarnings("hiding") TKill _kill_,
        @SuppressWarnings("hiding") PStringValue _stringValue_)
    {
        // Constructor
        setKill(_kill_);

        setStringValue(_stringValue_);

    }

    @Override
    public Object clone()
    {
        return new AKillFunction(
            cloneNode(this._kill_),
            cloneNode(this._stringValue_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAKillFunction(this);
    }

    public TKill getKill()
    {
        return this._kill_;
    }

    public void setKill(TKill node)
    {
        if(this._kill_ != null)
        {
            this._kill_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._kill_ = node;
    }

    public PStringValue getStringValue()
    {
        return this._stringValue_;
    }

    public void setStringValue(PStringValue node)
    {
        if(this._stringValue_ != null)
        {
            this._stringValue_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._stringValue_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._kill_)
            + toString(this._stringValue_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._kill_ == child)
        {
            this._kill_ = null;
            return;
        }

        if(this._stringValue_ == child)
        {
            this._stringValue_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._kill_ == oldChild)
        {
            setKill((TKill) newChild);
            return;
        }

        if(this._stringValue_ == oldChild)
        {
            setStringValue((PStringValue) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}