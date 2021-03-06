/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class ARowsRowsExcelRow extends PExcelRow
{
    private PExcelSheet _excelSheet_;
    private TMethodChainingOperator _a_;
    private TRows _firstRows_;
    private PParamCallList _paramCallList_;
    private TMethodChainingOperator _b_;
    private TRows _secondRows_;

    public ARowsRowsExcelRow()
    {
        // Constructor
    }

    public ARowsRowsExcelRow(
        @SuppressWarnings("hiding") PExcelSheet _excelSheet_,
        @SuppressWarnings("hiding") TMethodChainingOperator _a_,
        @SuppressWarnings("hiding") TRows _firstRows_,
        @SuppressWarnings("hiding") PParamCallList _paramCallList_,
        @SuppressWarnings("hiding") TMethodChainingOperator _b_,
        @SuppressWarnings("hiding") TRows _secondRows_)
    {
        // Constructor
        setExcelSheet(_excelSheet_);

        setA(_a_);

        setFirstRows(_firstRows_);

        setParamCallList(_paramCallList_);

        setB(_b_);

        setSecondRows(_secondRows_);

    }

    @Override
    public Object clone()
    {
        return new ARowsRowsExcelRow(
            cloneNode(this._excelSheet_),
            cloneNode(this._a_),
            cloneNode(this._firstRows_),
            cloneNode(this._paramCallList_),
            cloneNode(this._b_),
            cloneNode(this._secondRows_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseARowsRowsExcelRow(this);
    }

    public PExcelSheet getExcelSheet()
    {
        return this._excelSheet_;
    }

    public void setExcelSheet(PExcelSheet node)
    {
        if(this._excelSheet_ != null)
        {
            this._excelSheet_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._excelSheet_ = node;
    }

    public TMethodChainingOperator getA()
    {
        return this._a_;
    }

    public void setA(TMethodChainingOperator node)
    {
        if(this._a_ != null)
        {
            this._a_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._a_ = node;
    }

    public TRows getFirstRows()
    {
        return this._firstRows_;
    }

    public void setFirstRows(TRows node)
    {
        if(this._firstRows_ != null)
        {
            this._firstRows_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._firstRows_ = node;
    }

    public PParamCallList getParamCallList()
    {
        return this._paramCallList_;
    }

    public void setParamCallList(PParamCallList node)
    {
        if(this._paramCallList_ != null)
        {
            this._paramCallList_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._paramCallList_ = node;
    }

    public TMethodChainingOperator getB()
    {
        return this._b_;
    }

    public void setB(TMethodChainingOperator node)
    {
        if(this._b_ != null)
        {
            this._b_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._b_ = node;
    }

    public TRows getSecondRows()
    {
        return this._secondRows_;
    }

    public void setSecondRows(TRows node)
    {
        if(this._secondRows_ != null)
        {
            this._secondRows_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._secondRows_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._excelSheet_)
            + toString(this._a_)
            + toString(this._firstRows_)
            + toString(this._paramCallList_)
            + toString(this._b_)
            + toString(this._secondRows_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._excelSheet_ == child)
        {
            this._excelSheet_ = null;
            return;
        }

        if(this._a_ == child)
        {
            this._a_ = null;
            return;
        }

        if(this._firstRows_ == child)
        {
            this._firstRows_ = null;
            return;
        }

        if(this._paramCallList_ == child)
        {
            this._paramCallList_ = null;
            return;
        }

        if(this._b_ == child)
        {
            this._b_ = null;
            return;
        }

        if(this._secondRows_ == child)
        {
            this._secondRows_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._excelSheet_ == oldChild)
        {
            setExcelSheet((PExcelSheet) newChild);
            return;
        }

        if(this._a_ == oldChild)
        {
            setA((TMethodChainingOperator) newChild);
            return;
        }

        if(this._firstRows_ == oldChild)
        {
            setFirstRows((TRows) newChild);
            return;
        }

        if(this._paramCallList_ == oldChild)
        {
            setParamCallList((PParamCallList) newChild);
            return;
        }

        if(this._b_ == oldChild)
        {
            setB((TMethodChainingOperator) newChild);
            return;
        }

        if(this._secondRows_ == oldChild)
        {
            setSecondRows((TRows) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
