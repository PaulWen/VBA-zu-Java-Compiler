/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class ASheetGetNameExcelFunction extends PGetNameExcelFunction
{
    private PExcelSheet _excelSheet_;
    private TMethodChainingOperator _methodChainingOperator_;
    private TName _name_;

    public ASheetGetNameExcelFunction()
    {
        // Constructor
    }

    public ASheetGetNameExcelFunction(
        @SuppressWarnings("hiding") PExcelSheet _excelSheet_,
        @SuppressWarnings("hiding") TMethodChainingOperator _methodChainingOperator_,
        @SuppressWarnings("hiding") TName _name_)
    {
        // Constructor
        setExcelSheet(_excelSheet_);

        setMethodChainingOperator(_methodChainingOperator_);

        setName(_name_);

    }

    @Override
    public Object clone()
    {
        return new ASheetGetNameExcelFunction(
            cloneNode(this._excelSheet_),
            cloneNode(this._methodChainingOperator_),
            cloneNode(this._name_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseASheetGetNameExcelFunction(this);
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

    public TName getName()
    {
        return this._name_;
    }

    public void setName(TName node)
    {
        if(this._name_ != null)
        {
            this._name_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._name_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._excelSheet_)
            + toString(this._methodChainingOperator_)
            + toString(this._name_);
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

        if(this._methodChainingOperator_ == child)
        {
            this._methodChainingOperator_ = null;
            return;
        }

        if(this._name_ == child)
        {
            this._name_ = null;
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

        if(this._methodChainingOperator_ == oldChild)
        {
            setMethodChainingOperator((TMethodChainingOperator) newChild);
            return;
        }

        if(this._name_ == oldChild)
        {
            setName((TName) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}