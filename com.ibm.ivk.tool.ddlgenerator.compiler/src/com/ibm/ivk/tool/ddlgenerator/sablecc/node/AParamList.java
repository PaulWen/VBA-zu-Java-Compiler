/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import java.util.*;
import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AParamList extends PParamList
{
    private TOpenParen _openParen_;
    private final LinkedList<PParamPart> _paramPart_ = new LinkedList<PParamPart>();
    private PParamValue _paramValue_;
    private TCloseParen _closeParen_;

    public AParamList()
    {
        // Constructor
    }

    public AParamList(
        @SuppressWarnings("hiding") TOpenParen _openParen_,
        @SuppressWarnings("hiding") List<PParamPart> _paramPart_,
        @SuppressWarnings("hiding") PParamValue _paramValue_,
        @SuppressWarnings("hiding") TCloseParen _closeParen_)
    {
        // Constructor
        setOpenParen(_openParen_);

        setParamPart(_paramPart_);

        setParamValue(_paramValue_);

        setCloseParen(_closeParen_);

    }

    @Override
    public Object clone()
    {
        return new AParamList(
            cloneNode(this._openParen_),
            cloneList(this._paramPart_),
            cloneNode(this._paramValue_),
            cloneNode(this._closeParen_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAParamList(this);
    }

    public TOpenParen getOpenParen()
    {
        return this._openParen_;
    }

    public void setOpenParen(TOpenParen node)
    {
        if(this._openParen_ != null)
        {
            this._openParen_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._openParen_ = node;
    }

    public LinkedList<PParamPart> getParamPart()
    {
        return this._paramPart_;
    }

    public void setParamPart(List<PParamPart> list)
    {
        this._paramPart_.clear();
        this._paramPart_.addAll(list);
        for(PParamPart e : list)
        {
            if(e.parent() != null)
            {
                e.parent().removeChild(e);
            }

            e.parent(this);
        }
    }

    public PParamValue getParamValue()
    {
        return this._paramValue_;
    }

    public void setParamValue(PParamValue node)
    {
        if(this._paramValue_ != null)
        {
            this._paramValue_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._paramValue_ = node;
    }

    public TCloseParen getCloseParen()
    {
        return this._closeParen_;
    }

    public void setCloseParen(TCloseParen node)
    {
        if(this._closeParen_ != null)
        {
            this._closeParen_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._closeParen_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._openParen_)
            + toString(this._paramPart_)
            + toString(this._paramValue_)
            + toString(this._closeParen_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._openParen_ == child)
        {
            this._openParen_ = null;
            return;
        }

        if(this._paramPart_.remove(child))
        {
            return;
        }

        if(this._paramValue_ == child)
        {
            this._paramValue_ = null;
            return;
        }

        if(this._closeParen_ == child)
        {
            this._closeParen_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._openParen_ == oldChild)
        {
            setOpenParen((TOpenParen) newChild);
            return;
        }

        for(ListIterator<PParamPart> i = this._paramPart_.listIterator(); i.hasNext();)
        {
            if(i.next() == oldChild)
            {
                if(newChild != null)
                {
                    i.set((PParamPart) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }

                i.remove();
                oldChild.parent(null);
                return;
            }
        }

        if(this._paramValue_ == oldChild)
        {
            setParamValue((PParamValue) newChild);
            return;
        }

        if(this._closeParen_ == oldChild)
        {
            setCloseParen((TCloseParen) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}