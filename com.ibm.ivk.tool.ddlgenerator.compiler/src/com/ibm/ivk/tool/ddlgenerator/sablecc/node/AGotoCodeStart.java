/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AGotoCodeStart extends PGotoCodeStart
{
    private TGotoId _gotoId_;

    public AGotoCodeStart()
    {
        // Constructor
    }

    public AGotoCodeStart(
        @SuppressWarnings("hiding") TGotoId _gotoId_)
    {
        // Constructor
        setGotoId(_gotoId_);

    }

    @Override
    public Object clone()
    {
        return new AGotoCodeStart(
            cloneNode(this._gotoId_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAGotoCodeStart(this);
    }

    public TGotoId getGotoId()
    {
        return this._gotoId_;
    }

    public void setGotoId(TGotoId node)
    {
        if(this._gotoId_ != null)
        {
            this._gotoId_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._gotoId_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._gotoId_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._gotoId_ == child)
        {
            this._gotoId_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._gotoId_ == oldChild)
        {
            setGotoId((TGotoId) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
