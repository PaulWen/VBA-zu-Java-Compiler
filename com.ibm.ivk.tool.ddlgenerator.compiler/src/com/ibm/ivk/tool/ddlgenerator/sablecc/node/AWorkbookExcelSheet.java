/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AWorkbookExcelSheet extends PExcelSheet
{
    private PExcelWorkbook _excelWorkbook_;
    private TMethodChainingOperator _methodChainingOperator_;
    private PExcelSheetValue _excelSheetValue_;

    public AWorkbookExcelSheet()
    {
        // Constructor
    }

    public AWorkbookExcelSheet(
        @SuppressWarnings("hiding") PExcelWorkbook _excelWorkbook_,
        @SuppressWarnings("hiding") TMethodChainingOperator _methodChainingOperator_,
        @SuppressWarnings("hiding") PExcelSheetValue _excelSheetValue_)
    {
        // Constructor
        setExcelWorkbook(_excelWorkbook_);

        setMethodChainingOperator(_methodChainingOperator_);

        setExcelSheetValue(_excelSheetValue_);

    }

    @Override
    public Object clone()
    {
        return new AWorkbookExcelSheet(
            cloneNode(this._excelWorkbook_),
            cloneNode(this._methodChainingOperator_),
            cloneNode(this._excelSheetValue_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAWorkbookExcelSheet(this);
    }

    public PExcelWorkbook getExcelWorkbook()
    {
        return this._excelWorkbook_;
    }

    public void setExcelWorkbook(PExcelWorkbook node)
    {
        if(this._excelWorkbook_ != null)
        {
            this._excelWorkbook_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._excelWorkbook_ = node;
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

    public PExcelSheetValue getExcelSheetValue()
    {
        return this._excelSheetValue_;
    }

    public void setExcelSheetValue(PExcelSheetValue node)
    {
        if(this._excelSheetValue_ != null)
        {
            this._excelSheetValue_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._excelSheetValue_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._excelWorkbook_)
            + toString(this._methodChainingOperator_)
            + toString(this._excelSheetValue_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._excelWorkbook_ == child)
        {
            this._excelWorkbook_ = null;
            return;
        }

        if(this._methodChainingOperator_ == child)
        {
            this._methodChainingOperator_ = null;
            return;
        }

        if(this._excelSheetValue_ == child)
        {
            this._excelSheetValue_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._excelWorkbook_ == oldChild)
        {
            setExcelWorkbook((PExcelWorkbook) newChild);
            return;
        }

        if(this._methodChainingOperator_ == oldChild)
        {
            setMethodChainingOperator((TMethodChainingOperator) newChild);
            return;
        }

        if(this._excelSheetValue_ == oldChild)
        {
            setExcelSheetValue((PExcelSheetValue) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
