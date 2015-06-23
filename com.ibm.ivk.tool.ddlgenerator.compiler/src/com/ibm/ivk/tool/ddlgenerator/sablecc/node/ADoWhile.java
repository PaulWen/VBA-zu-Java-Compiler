/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import java.util.*;
import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class ADoWhile extends PDoWhile
{
    private TDoWhileStart _doWhileStart_;
    private PValue _condition_;
    private TEndOfLine _endOfLine_;
    private final LinkedList<PFunctionStmt> _functionStmt_ = new LinkedList<PFunctionStmt>();
    private TDoWhileEnd _doWhileEnd_;

    public ADoWhile()
    {
        // Constructor
    }

    public ADoWhile(
        @SuppressWarnings("hiding") TDoWhileStart _doWhileStart_,
        @SuppressWarnings("hiding") PValue _condition_,
        @SuppressWarnings("hiding") TEndOfLine _endOfLine_,
        @SuppressWarnings("hiding") List<PFunctionStmt> _functionStmt_,
        @SuppressWarnings("hiding") TDoWhileEnd _doWhileEnd_)
    {
        // Constructor
        setDoWhileStart(_doWhileStart_);

        setCondition(_condition_);

        setEndOfLine(_endOfLine_);

        setFunctionStmt(_functionStmt_);

        setDoWhileEnd(_doWhileEnd_);

    }

    @Override
    public Object clone()
    {
        return new ADoWhile(
            cloneNode(this._doWhileStart_),
            cloneNode(this._condition_),
            cloneNode(this._endOfLine_),
            cloneList(this._functionStmt_),
            cloneNode(this._doWhileEnd_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseADoWhile(this);
    }

    public TDoWhileStart getDoWhileStart()
    {
        return this._doWhileStart_;
    }

    public void setDoWhileStart(TDoWhileStart node)
    {
        if(this._doWhileStart_ != null)
        {
            this._doWhileStart_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._doWhileStart_ = node;
    }

    public PValue getCondition()
    {
        return this._condition_;
    }

    public void setCondition(PValue node)
    {
        if(this._condition_ != null)
        {
            this._condition_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._condition_ = node;
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

    public TDoWhileEnd getDoWhileEnd()
    {
        return this._doWhileEnd_;
    }

    public void setDoWhileEnd(TDoWhileEnd node)
    {
        if(this._doWhileEnd_ != null)
        {
            this._doWhileEnd_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._doWhileEnd_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._doWhileStart_)
            + toString(this._condition_)
            + toString(this._endOfLine_)
            + toString(this._functionStmt_)
            + toString(this._doWhileEnd_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._doWhileStart_ == child)
        {
            this._doWhileStart_ = null;
            return;
        }

        if(this._condition_ == child)
        {
            this._condition_ = null;
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

        if(this._doWhileEnd_ == child)
        {
            this._doWhileEnd_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._doWhileStart_ == oldChild)
        {
            setDoWhileStart((TDoWhileStart) newChild);
            return;
        }

        if(this._condition_ == oldChild)
        {
            setCondition((PValue) newChild);
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

        if(this._doWhileEnd_ == oldChild)
        {
            setDoWhileEnd((TDoWhileEnd) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}