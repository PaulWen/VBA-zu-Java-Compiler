/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import java.util.*;
import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class ATypeDec extends PTypeDec
{
    private final LinkedList<PModifier> _modifier_ = new LinkedList<PModifier>();
    private TTypeStart _typeStart_;
    private PId _id_;
    private TEndOfLine _endOfLine_;
    private final LinkedList<PTypeDecValue> _typeDecValue_ = new LinkedList<PTypeDecValue>();
    private TTypeEnd _typeEnd_;

    public ATypeDec()
    {
        // Constructor
    }

    public ATypeDec(
        @SuppressWarnings("hiding") List<PModifier> _modifier_,
        @SuppressWarnings("hiding") TTypeStart _typeStart_,
        @SuppressWarnings("hiding") PId _id_,
        @SuppressWarnings("hiding") TEndOfLine _endOfLine_,
        @SuppressWarnings("hiding") List<PTypeDecValue> _typeDecValue_,
        @SuppressWarnings("hiding") TTypeEnd _typeEnd_)
    {
        // Constructor
        setModifier(_modifier_);

        setTypeStart(_typeStart_);

        setId(_id_);

        setEndOfLine(_endOfLine_);

        setTypeDecValue(_typeDecValue_);

        setTypeEnd(_typeEnd_);

    }

    @Override
    public Object clone()
    {
        return new ATypeDec(
            cloneList(this._modifier_),
            cloneNode(this._typeStart_),
            cloneNode(this._id_),
            cloneNode(this._endOfLine_),
            cloneList(this._typeDecValue_),
            cloneNode(this._typeEnd_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseATypeDec(this);
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

    public TTypeStart getTypeStart()
    {
        return this._typeStart_;
    }

    public void setTypeStart(TTypeStart node)
    {
        if(this._typeStart_ != null)
        {
            this._typeStart_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._typeStart_ = node;
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

    public TEndOfLine getEndOfLine()
    {
        return this._endOfLine_;
    }

    public void setEndOfLine(TEndOfLine node)
    {
        if(this._endOfLine_ != null)
        {
            this._endOfLine_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._endOfLine_ = node;
    }

    public LinkedList<PTypeDecValue> getTypeDecValue()
    {
        return this._typeDecValue_;
    }

    public void setTypeDecValue(List<PTypeDecValue> list)
    {
        this._typeDecValue_.clear();
        this._typeDecValue_.addAll(list);
        for(PTypeDecValue e : list)
        {
            if(e.parent() != null)
            {
                e.parent().removeChild(e);
            }

            e.parent(this);
        }
    }

    public TTypeEnd getTypeEnd()
    {
        return this._typeEnd_;
    }

    public void setTypeEnd(TTypeEnd node)
    {
        if(this._typeEnd_ != null)
        {
            this._typeEnd_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._typeEnd_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._modifier_)
            + toString(this._typeStart_)
            + toString(this._id_)
            + toString(this._endOfLine_)
            + toString(this._typeDecValue_)
            + toString(this._typeEnd_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._modifier_.remove(child))
        {
            return;
        }

        if(this._typeStart_ == child)
        {
            this._typeStart_ = null;
            return;
        }

        if(this._id_ == child)
        {
            this._id_ = null;
            return;
        }

        if(this._endOfLine_ == child)
        {
            this._endOfLine_ = null;
            return;
        }

        if(this._typeDecValue_.remove(child))
        {
            return;
        }

        if(this._typeEnd_ == child)
        {
            this._typeEnd_ = null;
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

        if(this._typeStart_ == oldChild)
        {
            setTypeStart((TTypeStart) newChild);
            return;
        }

        if(this._id_ == oldChild)
        {
            setId((PId) newChild);
            return;
        }

        if(this._endOfLine_ == oldChild)
        {
            setEndOfLine((TEndOfLine) newChild);
            return;
        }

        for(ListIterator<PTypeDecValue> i = this._typeDecValue_.listIterator(); i.hasNext();)
        {
            if(i.next() == oldChild)
            {
                if(newChild != null)
                {
                    i.set((PTypeDecValue) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }

                i.remove();
                oldChild.parent(null);
                return;
            }
        }

        if(this._typeEnd_ == oldChild)
        {
            setTypeEnd((TTypeEnd) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
