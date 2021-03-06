/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class ACellSelectFunction extends PSelectFunction
{
    private PExcelCell _excelCell_;
    private TMethodChainingOperator _methodChainingOperator_;
    private TSelect _select_;

    public ACellSelectFunction()
    {
        // Constructor
    }

    public ACellSelectFunction(
        @SuppressWarnings("hiding") PExcelCell _excelCell_,
        @SuppressWarnings("hiding") TMethodChainingOperator _methodChainingOperator_,
        @SuppressWarnings("hiding") TSelect _select_)
    {
        // Constructor
        setExcelCell(_excelCell_);

        setMethodChainingOperator(_methodChainingOperator_);

        setSelect(_select_);

    }

    @Override
    public Object clone()
    {
        return new ACellSelectFunction(
            cloneNode(this._excelCell_),
            cloneNode(this._methodChainingOperator_),
            cloneNode(this._select_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseACellSelectFunction(this);
    }

    public PExcelCell getExcelCell()
    {
        return this._excelCell_;
    }

    public void setExcelCell(PExcelCell node)
    {
        if(this._excelCell_ != null)
        {
            this._excelCell_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._excelCell_ = node;
    }

    public TMethodChainingOperator getMethodChainingOperator()
    {
        return this._methodChainingOperator_;
    }

    public void setMethodChainingOperator(TMethodChainingOperator node)
    {
        if(this._methodChainingOperator_ != null)
        {
            this._methodChainingOperator_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._methodChainingOperator_ = node;
    }

    public TSelect getSelect()
    {
        return this._select_;
    }

    public void setSelect(TSelect node)
    {
        if(this._select_ != null)
        {
            this._select_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._select_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._excelCell_)
            + toString(this._methodChainingOperator_)
            + toString(this._select_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._excelCell_ == child)
        {
            this._excelCell_ = null;
            return;
        }

        if(this._methodChainingOperator_ == child)
        {
            this._methodChainingOperator_ = null;
            return;
        }

        if(this._select_ == child)
        {
            this._select_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._excelCell_ == oldChild)
        {
            setExcelCell((PExcelCell) newChild);
            return;
        }

        if(this._methodChainingOperator_ == oldChild)
        {
            setMethodChainingOperator((TMethodChainingOperator) newChild);
            return;
        }

        if(this._select_ == oldChild)
        {
            setSelect((TSelect) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
