/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AWorksheetDataType extends PDataType
{
    private TWorksheet _worksheet_;

    public AWorksheetDataType()
    {
        // Constructor
    }

    public AWorksheetDataType(
        @SuppressWarnings("hiding") TWorksheet _worksheet_)
    {
        // Constructor
        setWorksheet(_worksheet_);

    }

    @Override
    public Object clone()
    {
        return new AWorksheetDataType(
            cloneNode(this._worksheet_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAWorksheetDataType(this);
    }

    public TWorksheet getWorksheet()
    {
        return this._worksheet_;
    }

    public void setWorksheet(TWorksheet node)
    {
        if(this._worksheet_ != null)
        {
            this._worksheet_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._worksheet_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._worksheet_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._worksheet_ == child)
        {
            this._worksheet_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._worksheet_ == oldChild)
        {
            setWorksheet((TWorksheet) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}