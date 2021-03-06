/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AStringStringPartValue extends PStringPartValue
{
    private TStringToken _stringToken_;

    public AStringStringPartValue()
    {
        // Constructor
    }

    public AStringStringPartValue(
        @SuppressWarnings("hiding") TStringToken _stringToken_)
    {
        // Constructor
        setStringToken(_stringToken_);

    }

    @Override
    public Object clone()
    {
        return new AStringStringPartValue(
            cloneNode(this._stringToken_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAStringStringPartValue(this);
    }

    public TStringToken getStringToken()
    {
        return this._stringToken_;
    }

    public void setStringToken(TStringToken node)
    {
        if(this._stringToken_ != null)
        {
            this._stringToken_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._stringToken_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._stringToken_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._stringToken_ == child)
        {
            this._stringToken_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._stringToken_ == oldChild)
        {
            setStringToken((TStringToken) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
