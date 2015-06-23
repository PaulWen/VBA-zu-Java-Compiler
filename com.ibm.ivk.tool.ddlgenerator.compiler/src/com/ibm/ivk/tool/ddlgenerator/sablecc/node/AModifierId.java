/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import java.util.*;
import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AModifierId extends PModifierId
{
    private final LinkedList<PModifier> _modifier_ = new LinkedList<PModifier>();
    private PId _id_;

    public AModifierId()
    {
        // Constructor
    }

    public AModifierId(
        @SuppressWarnings("hiding") List<PModifier> _modifier_,
        @SuppressWarnings("hiding") PId _id_)
    {
        // Constructor
        setModifier(_modifier_);

        setId(_id_);

    }

    @Override
    public Object clone()
    {
        return new AModifierId(
            cloneList(this._modifier_),
            cloneNode(this._id_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAModifierId(this);
    }

    public LinkedList<PModifier> getModifier()
    {
        return this._modifier_;
    }

    public void setModifier(List<PModifier> list)
    {
        this._modifier_.clear();
        this._modifier_.addAll(list);
        for(PModifier e : list)
        {
            if(e.parent() != null)
            {
                e.parent().removeChild(e);
            }

            e.parent(this);
        }
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
            + toString(this._modifier_)
            + toString(this._id_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._modifier_.remove(child))
        {
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
        for(ListIterator<PModifier> i = this._modifier_.listIterator(); i.hasNext();)
        {
            if(i.next() == oldChild)
            {
                if(newChild != null)
                {
                    i.set((PModifier) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }

                i.remove();
                oldChild.parent(null);
                return;
            }
        }

        if(this._id_ == oldChild)
        {
            setId((PId) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}