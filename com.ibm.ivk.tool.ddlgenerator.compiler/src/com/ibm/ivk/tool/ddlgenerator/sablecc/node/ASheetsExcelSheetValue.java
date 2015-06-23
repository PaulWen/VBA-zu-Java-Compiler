/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class ASheetsExcelSheetValue extends PExcelSheetValue
{
    private TSheets _sheets_;

    public ASheetsExcelSheetValue()
    {
        // Constructor
    }

    public ASheetsExcelSheetValue(
        @SuppressWarnings("hiding") TSheets _sheets_)
    {
        // Constructor
        setSheets(_sheets_);

    }

    @Override
    public Object clone()
    {
        return new ASheetsExcelSheetValue(
            cloneNode(this._sheets_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseASheetsExcelSheetValue(this);
    }

    public TSheets getSheets()
    {
        return this._sheets_;
    }

    public void setSheets(TSheets node)
    {
        if(this._sheets_ != null)
        {
            this._sheets_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._sheets_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._sheets_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._sheets_ == child)
        {
            this._sheets_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._sheets_ == oldChild)
        {
            setSheets((TSheets) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}