/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class ACharacterStringValue extends PValue
{
    private PCharacterString _characterString_;

    public ACharacterStringValue()
    {
        // Constructor
    }

    public ACharacterStringValue(
        @SuppressWarnings("hiding") PCharacterString _characterString_)
    {
        // Constructor
        setCharacterString(_characterString_);

    }

    @Override
    public Object clone()
    {
        return new ACharacterStringValue(
            cloneNode(this._characterString_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseACharacterStringValue(this);
    }

    public PCharacterString getCharacterString()
    {
        return this._characterString_;
    }

    public void setCharacterString(PCharacterString node)
    {
        if(this._characterString_ != null)
        {
            this._characterString_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._characterString_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._characterString_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._characterString_ == child)
        {
            this._characterString_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._characterString_ == oldChild)
        {
            setCharacterString((PCharacterString) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
