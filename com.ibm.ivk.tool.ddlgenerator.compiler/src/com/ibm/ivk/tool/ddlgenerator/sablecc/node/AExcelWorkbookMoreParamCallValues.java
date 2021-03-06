/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AExcelWorkbookMoreParamCallValues extends PMoreParamCallValues
{
    private PExcelWorkbook _excelWorkbook_;

    public AExcelWorkbookMoreParamCallValues()
    {
        // Constructor
    }

    public AExcelWorkbookMoreParamCallValues(
        @SuppressWarnings("hiding") PExcelWorkbook _excelWorkbook_)
    {
        // Constructor
        setExcelWorkbook(_excelWorkbook_);

    }

    @Override
    public Object clone()
    {
        return new AExcelWorkbookMoreParamCallValues(
            cloneNode(this._excelWorkbook_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAExcelWorkbookMoreParamCallValues(this);
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

    @Override
    public String toString()
    {
        return ""
            + toString(this._excelWorkbook_);
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

        throw new RuntimeException("Not a child.");
    }
}
