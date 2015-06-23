/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import java.util.*;
import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AIfStmt extends PIfStmt
{
    private TIf _if_;
    private PValue _value_;
    private TThen _then_;
    private TEndOfLine _endOfLine_;
    private final LinkedList<PFunctionStmt> _functionStmt_ = new LinkedList<PFunctionStmt>();
    private TEndIf _endIf_;

    public AIfStmt()
    {
        // Constructor
    }

    public AIfStmt(
        @SuppressWarnings("hiding") TIf _if_,
        @SuppressWarnings("hiding") PValue _value_,
        @SuppressWarnings("hiding") TThen _then_,
        @SuppressWarnings("hiding") TEndOfLine _endOfLine_,
        @SuppressWarnings("hiding") List<PFunctionStmt> _functionStmt_,
        @SuppressWarnings("hiding") TEndIf _endIf_)
    {
        // Constructor
        setIf(_if_);

        setValue(_value_);

        setThen(_then_);

        setEndOfLine(_endOfLine_);

        setFunctionStmt(_functionStmt_);

        setEndIf(_endIf_);

    }

    @Override
    public Object clone()
    {
        return new AIfStmt(
            cloneNode(this._if_),
            cloneNode(this._value_),
            cloneNode(this._then_),
            cloneNode(this._endOfLine_),
            cloneList(this._functionStmt_),
            cloneNode(this._endIf_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAIfStmt(this);
    }

    public TIf getIf()
    {
        return this._if_;
    }

    public void setIf(TIf node)
    {
        if(this._if_ != null)
        {
            this._if_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._if_ = node;
    }

    public PValue getValue()
    {
        return this._value_;
    }

    public void setValue(PValue node)
    {
        if(this._value_ != null)
        {
            this._value_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._value_ = node;
    }

    public TThen getThen()
    {
        return this._then_;
    }

    public void setThen(TThen node)
    {
        if(this._then_ != null)
        {
            this._then_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._then_ = node;
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

    public LinkedList<PFunctionStmt> getFunctionStmt()
    {
        return this._functionStmt_;
    }

    public void setFunctionStmt(List<PFunctionStmt> list)
    {
        this._functionStmt_.clear();
        this._functionStmt_.addAll(list);
        for(PFunctionStmt e : list)
        {
            if(e.parent() != null)
            {
                e.parent().removeChild(e);
            }

            e.parent(this);
        }
    }

    public TEndIf getEndIf()
    {
        return this._endIf_;
    }

    public void setEndIf(TEndIf node)
    {
        if(this._endIf_ != null)
        {
            this._endIf_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._endIf_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._if_)
            + toString(this._value_)
            + toString(this._then_)
            + toString(this._endOfLine_)
            + toString(this._functionStmt_)
            + toString(this._endIf_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._if_ == child)
        {
            this._if_ = null;
            return;
        }

        if(this._value_ == child)
        {
            this._value_ = null;
            return;
        }

        if(this._then_ == child)
        {
            this._then_ = null;
            return;
        }

        if(this._endOfLine_ == child)
        {
            this._endOfLine_ = null;
            return;
        }

        if(this._functionStmt_.remove(child))
        {
            return;
        }

        if(this._endIf_ == child)
        {
            this._endIf_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._if_ == oldChild)
        {
            setIf((TIf) newChild);
            return;
        }

        if(this._value_ == oldChild)
        {
            setValue((PValue) newChild);
            return;
        }

        if(this._then_ == oldChild)
        {
            setThen((TThen) newChild);
            return;
        }

        if(this._endOfLine_ == oldChild)
        {
            setEndOfLine((TEndOfLine) newChild);
            return;
        }

        for(ListIterator<PFunctionStmt> i = this._functionStmt_.listIterator(); i.hasNext();)
        {
            if(i.next() == oldChild)
            {
                if(newChild != null)
                {
                    i.set((PFunctionStmt) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }

                i.remove();
                oldChild.parent(null);
                return;
            }
        }

        if(this._endIf_ == oldChild)
        {
            setEndIf((TEndIf) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
