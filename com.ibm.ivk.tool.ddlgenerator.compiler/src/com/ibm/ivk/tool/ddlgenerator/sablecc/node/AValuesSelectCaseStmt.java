/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import java.util.*;
import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AValuesSelectCaseStmt extends PSelectCaseStmt
{
    private TSelectCase _selectCase_;
    private PParamCallListWithoutParens _paramCallListWithoutParens_;
    private TEndOfLine _endOfLine_;
    private final LinkedList<PFunctionStmt> _functionStmt_ = new LinkedList<PFunctionStmt>();

    public AValuesSelectCaseStmt()
    {
        // Constructor
    }

    public AValuesSelectCaseStmt(
        @SuppressWarnings("hiding") TSelectCase _selectCase_,
        @SuppressWarnings("hiding") PParamCallListWithoutParens _paramCallListWithoutParens_,
        @SuppressWarnings("hiding") TEndOfLine _endOfLine_,
        @SuppressWarnings("hiding") List<PFunctionStmt> _functionStmt_)
    {
        // Constructor
        setSelectCase(_selectCase_);

        setParamCallListWithoutParens(_paramCallListWithoutParens_);

        setEndOfLine(_endOfLine_);

        setFunctionStmt(_functionStmt_);

    }

    @Override
    public Object clone()
    {
        return new AValuesSelectCaseStmt(
            cloneNode(this._selectCase_),
            cloneNode(this._paramCallListWithoutParens_),
            cloneNode(this._endOfLine_),
            cloneList(this._functionStmt_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAValuesSelectCaseStmt(this);
    }

    public TSelectCase getSelectCase()
    {
        return this._selectCase_;
    }

    public void setSelectCase(TSelectCase node)
    {
        if(this._selectCase_ != null)
        {
            this._selectCase_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._selectCase_ = node;
    }

    public PParamCallListWithoutParens getParamCallListWithoutParens()
    {
        return this._paramCallListWithoutParens_;
    }

    public void setParamCallListWithoutParens(PParamCallListWithoutParens node)
    {
        if(this._paramCallListWithoutParens_ != null)
        {
            this._paramCallListWithoutParens_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._paramCallListWithoutParens_ = node;
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

    @Override
    public String toString()
    {
        return ""
            + toString(this._selectCase_)
            + toString(this._paramCallListWithoutParens_)
            + toString(this._endOfLine_)
            + toString(this._functionStmt_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._selectCase_ == child)
        {
            this._selectCase_ = null;
            return;
        }

        if(this._paramCallListWithoutParens_ == child)
        {
            this._paramCallListWithoutParens_ = null;
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

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._selectCase_ == oldChild)
        {
            setSelectCase((TSelectCase) newChild);
            return;
        }

        if(this._paramCallListWithoutParens_ == oldChild)
        {
            setParamCallListWithoutParens((PParamCallListWithoutParens) newChild);
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

        throw new RuntimeException("Not a child.");
    }
}