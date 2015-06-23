/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AExcelCell extends PExcelCell
{
    private PExcelSheet _excelSheet_;
    private TMethodChainingOperator _methodChainingOperator_;
    private PExcelCellValue _excelCellValue_;

    public AExcelCell()
    {
        // Constructor
    }

    public AExcelCell(
        @SuppressWarnings("hiding") PExcelSheet _excelSheet_,
        @SuppressWarnings("hiding") TMethodChainingOperator _methodChainingOperator_,
        @SuppressWarnings("hiding") PExcelCellValue _excelCellValue_)
    {
        // Constructor
        setExcelSheet(_excelSheet_);

        setMethodChainingOperator(_methodChainingOperator_);

        setExcelCellValue(_excelCellValue_);

    }

    @Override
    public Object clone()
    {
        return new AExcelCell(
            cloneNode(this._excelSheet_),
            cloneNode(this._methodChainingOperator_),
            cloneNode(this._excelCellValue_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAExcelCell(this);
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

    public PExcelCellValue getExcelCellValue()
    {
        return this._excelCellValue_;
    }

    public void setExcelCellValue(PExcelCellValue node)
    {
        if(this._excelCellValue_ != null)
        {
            this._excelCellValue_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._excelCellValue_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._excelSheet_)
            + toString(this._methodChainingOperator_)
            + toString(this._excelCellValue_);
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

        if(this._excelCellValue_ == child)
        {
            this._excelCellValue_ = null;
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

        if(this._excelCellValue_ == oldChild)
        {
            setExcelCellValue((PExcelCellValue) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
