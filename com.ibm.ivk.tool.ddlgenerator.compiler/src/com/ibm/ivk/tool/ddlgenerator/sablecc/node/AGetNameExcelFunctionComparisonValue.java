/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AGetNameExcelFunctionComparisonValue extends PComparisonValue
{
    private PGetNameExcelFunction _getNameExcelFunction_;

    public AGetNameExcelFunctionComparisonValue()
    {
        // Constructor
    }

    public AGetNameExcelFunctionComparisonValue(
        @SuppressWarnings("hiding") PGetNameExcelFunction _getNameExcelFunction_)
    {
        // Constructor
        setGetNameExcelFunction(_getNameExcelFunction_);

    }

    @Override
    public Object clone()
    {
        return new AGetNameExcelFunctionComparisonValue(
            cloneNode(this._getNameExcelFunction_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAGetNameExcelFunctionComparisonValue(this);
    }

    public PGetNameExcelFunction getGetNameExcelFunction()
    {
        return this._getNameExcelFunction_;
    }

    public void setGetNameExcelFunction(PGetNameExcelFunction node)
    {
        if(this._getNameExcelFunction_ != null)
        {
            this._getNameExcelFunction_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._getNameExcelFunction_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._getNameExcelFunction_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._getNameExcelFunction_ == child)
        {
            this._getNameExcelFunction_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._getNameExcelFunction_ == oldChild)
        {
            setGetNameExcelFunction((PGetNameExcelFunction) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
