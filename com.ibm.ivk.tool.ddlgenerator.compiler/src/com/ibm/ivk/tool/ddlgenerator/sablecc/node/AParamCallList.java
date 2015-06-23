/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import java.util.*;
import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AParamCallList extends PParamCallList
{
    private TOpenParen _openParen_;
    private final LinkedList<PParamCallPart> _paramCallPart_ = new LinkedList<PParamCallPart>();
    private PParamCallValue _paramCallValue_;
    private TCloseParen _closeParen_;

    public AParamCallList()
    {
        // Constructor
    }

    public AParamCallList(
        @SuppressWarnings("hiding") TOpenParen _openParen_,
        @SuppressWarnings("hiding") List<PParamCallPart> _paramCallPart_,
        @SuppressWarnings("hiding") PParamCallValue _paramCallValue_,
        @SuppressWarnings("hiding") TCloseParen _closeParen_)
    {
        // Constructor
        setOpenParen(_openParen_);

        setParamCallPart(_paramCallPart_);

        setParamCallValue(_paramCallValue_);

        setCloseParen(_closeParen_);

    }

    @Override
    public Object clone()
    {
        return new AParamCallList(
            cloneNode(this._openParen_),
            cloneList(this._paramCallPart_),
            cloneNode(this._paramCallValue_),
            cloneNode(this._closeParen_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAParamCallList(this);
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

    public LinkedList<PParamCallPart> getParamCallPart()
    {
        return this._paramCallPart_;
    }

    public void setParamCallPart(List<PParamCallPart> list)
    {
        this._paramCallPart_.clear();
        this._paramCallPart_.addAll(list);
        for(PParamCallPart e : list)
        {
            if(e.parent() != null)
            {
                e.parent().removeChild(e);
            }

            e.parent(this);
        }
    }

    public PParamCallValue getParamCallValue()
    {
        return this._paramCallValue_;
    }

    public void setParamCallValue(PParamCallValue node)
    {
        if(this._paramCallValue_ != null)
        {
            this._paramCallValue_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._paramCallValue_ = node;
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
            + toString(this._paramCallPart_)
            + toString(this._paramCallValue_)
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

        if(this._paramCallPart_.remove(child))
        {
            return;
        }

        if(this._paramCallValue_ == child)
        {
            this._paramCallValue_ = null;
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

        for(ListIterator<PParamCallPart> i = this._paramCallPart_.listIterator(); i.hasNext();)
        {
            if(i.next() == oldChild)
            {
                if(newChild != null)
                {
                    i.set((PParamCallPart) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }

                i.remove();
                oldChild.parent(null);
                return;
            }
        }

        if(this._paramCallValue_ == oldChild)
        {
            setParamCallValue((PParamCallValue) newChild);
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
