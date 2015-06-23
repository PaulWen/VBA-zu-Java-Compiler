/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import java.util.*;
import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AForStmtStep extends PForStmtStep
{
    private TForStart _forStart_;
    private PVarSet _varSet_;
    private TTo _to_;
    private PValue _limit_;
    private TForStep _forStep_;
    private PArithmeticExpression _arithmeticExpression_;
    private TEndOfLine _endOfLine_;
    private final LinkedList<PFunctionStmt> _functionStmt_ = new LinkedList<PFunctionStmt>();
    private TForEnd _forEnd_;
    private PMethodChaining _counter_;

    public AForStmtStep()
    {
        // Constructor
    }

    public AForStmtStep(
        @SuppressWarnings("hiding") TForStart _forStart_,
        @SuppressWarnings("hiding") PVarSet _varSet_,
        @SuppressWarnings("hiding") TTo _to_,
        @SuppressWarnings("hiding") PValue _limit_,
        @SuppressWarnings("hiding") TForStep _forStep_,
        @SuppressWarnings("hiding") PArithmeticExpression _arithmeticExpression_,
        @SuppressWarnings("hiding") TEndOfLine _endOfLine_,
        @SuppressWarnings("hiding") List<PFunctionStmt> _functionStmt_,
        @SuppressWarnings("hiding") TForEnd _forEnd_,
        @SuppressWarnings("hiding") PMethodChaining _counter_)
    {
        // Constructor
        setForStart(_forStart_);

        setVarSet(_varSet_);

        setTo(_to_);

        setLimit(_limit_);

        setForStep(_forStep_);

        setArithmeticExpression(_arithmeticExpression_);

        setEndOfLine(_endOfLine_);

        setFunctionStmt(_functionStmt_);

        setForEnd(_forEnd_);

        setCounter(_counter_);

    }

    @Override
    public Object clone()
    {
        return new AForStmtStep(
            cloneNode(this._forStart_),
            cloneNode(this._varSet_),
            cloneNode(this._to_),
            cloneNode(this._limit_),
            cloneNode(this._forStep_),
            cloneNode(this._arithmeticExpression_),
            cloneNode(this._endOfLine_),
            cloneList(this._functionStmt_),
            cloneNode(this._forEnd_),
            cloneNode(this._counter_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAForStmtStep(this);
    }

    public TForStart getForStart()
    {
        return this._forStart_;
    }

    public void setForStart(TForStart node)
    {
        if(this._forStart_ != null)
        {
            this._forStart_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._forStart_ = node;
    }

    public PVarSet getVarSet()
    {
        return this._varSet_;
    }

    public void setVarSet(PVarSet node)
    {
        if(this._varSet_ != null)
        {
            this._varSet_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._varSet_ = node;
    }

    public TTo getTo()
    {
        return this._to_;
    }

    public void setTo(TTo node)
    {
        if(this._to_ != null)
        {
            this._to_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._to_ = node;
    }

    public PValue getLimit()
    {
        return this._limit_;
    }

    public void setLimit(PValue node)
    {
        if(this._limit_ != null)
        {
            this._limit_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._limit_ = node;
    }

    public TForStep getForStep()
    {
        return this._forStep_;
    }

    public void setForStep(TForStep node)
    {
        if(this._forStep_ != null)
        {
            this._forStep_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._forStep_ = node;
    }

    public PArithmeticExpression getArithmeticExpression()
    {
        return this._arithmeticExpression_;
    }

    public void setArithmeticExpression(PArithmeticExpression node)
    {
        if(this._arithmeticExpression_ != null)
        {
            this._arithmeticExpression_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._arithmeticExpression_ = node;
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

    public TForEnd getForEnd()
    {
        return this._forEnd_;
    }

    public void setForEnd(TForEnd node)
    {
        if(this._forEnd_ != null)
        {
            this._forEnd_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._forEnd_ = node;
    }

    public PMethodChaining getCounter()
    {
        return this._counter_;
    }

    public void setCounter(PMethodChaining node)
    {
        if(this._counter_ != null)
        {
            this._counter_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._counter_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._forStart_)
            + toString(this._varSet_)
            + toString(this._to_)
            + toString(this._limit_)
            + toString(this._forStep_)
            + toString(this._arithmeticExpression_)
            + toString(this._endOfLine_)
            + toString(this._functionStmt_)
            + toString(this._forEnd_)
            + toString(this._counter_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._forStart_ == child)
        {
            this._forStart_ = null;
            return;
        }

        if(this._varSet_ == child)
        {
            this._varSet_ = null;
            return;
        }

        if(this._to_ == child)
        {
            this._to_ = null;
            return;
        }

        if(this._limit_ == child)
        {
            this._limit_ = null;
            return;
        }

        if(this._forStep_ == child)
        {
            this._forStep_ = null;
            return;
        }

        if(this._arithmeticExpression_ == child)
        {
            this._arithmeticExpression_ = null;
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

        if(this._forEnd_ == child)
        {
            this._forEnd_ = null;
            return;
        }

        if(this._counter_ == child)
        {
            this._counter_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._forStart_ == oldChild)
        {
            setForStart((TForStart) newChild);
            return;
        }

        if(this._varSet_ == oldChild)
        {
            setVarSet((PVarSet) newChild);
            return;
        }

        if(this._to_ == oldChild)
        {
            setTo((TTo) newChild);
            return;
        }

        if(this._limit_ == oldChild)
        {
            setLimit((PValue) newChild);
            return;
        }

        if(this._forStep_ == oldChild)
        {
            setForStep((TForStep) newChild);
            return;
        }

        if(this._arithmeticExpression_ == oldChild)
        {
            setArithmeticExpression((PArithmeticExpression) newChild);
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

        if(this._forEnd_ == oldChild)
        {
            setForEnd((TForEnd) newChild);
            return;
        }

        if(this._counter_ == oldChild)
        {
            setCounter((PMethodChaining) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
