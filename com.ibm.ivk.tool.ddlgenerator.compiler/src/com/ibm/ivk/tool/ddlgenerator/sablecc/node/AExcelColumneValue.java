/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AExcelColumneValue extends PExcelColumneValue
{
    private TColumns _columns_;

    public AExcelColumneValue()
    {
        // Constructor
    }

    public AExcelColumneValue(
        @SuppressWarnings("hiding") TColumns _columns_)
    {
        // Constructor
        setColumns(_columns_);

    }

    @Override
    public Object clone()
    {
        return new AExcelColumneValue(
            cloneNode(this._columns_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAExcelColumneValue(this);
    }

    public TColumns getColumns()
    {
        return this._columns_;
    }

    public void setColumns(TColumns node)
    {
        if(this._columns_ != null)
        {
            this._columns_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._columns_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._columns_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._columns_ == child)
        {
            this._columns_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._columns_ == oldChild)
        {
            setColumns((TColumns) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
