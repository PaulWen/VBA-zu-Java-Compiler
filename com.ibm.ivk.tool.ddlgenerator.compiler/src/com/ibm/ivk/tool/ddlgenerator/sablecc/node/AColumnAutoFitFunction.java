/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AColumnAutoFitFunction extends PAutoFitFunction
{
    private PExcelColumn _excelColumn_;
    private TMethodChainingOperator _methodChainingOperator_;
    private TAutoFit _autoFit_;

    public AColumnAutoFitFunction()
    {
        // Constructor
    }

    public AColumnAutoFitFunction(
        @SuppressWarnings("hiding") PExcelColumn _excelColumn_,
        @SuppressWarnings("hiding") TMethodChainingOperator _methodChainingOperator_,
        @SuppressWarnings("hiding") TAutoFit _autoFit_)
    {
        // Constructor
        setExcelColumn(_excelColumn_);

        setMethodChainingOperator(_methodChainingOperator_);

        setAutoFit(_autoFit_);

    }

    @Override
    public Object clone()
    {
        return new AColumnAutoFitFunction(
            cloneNode(this._excelColumn_),
            cloneNode(this._methodChainingOperator_),
            cloneNode(this._autoFit_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAColumnAutoFitFunction(this);
    }

    public PExcelColumn getExcelColumn()
    {
        return this._excelColumn_;
    }

    public void setExcelColumn(PExcelColumn node)
    {
        if(this._excelColumn_ != null)
        {
            this._excelColumn_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._excelColumn_ = node;
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

    public TAutoFit getAutoFit()
    {
        return this._autoFit_;
    }

    public void setAutoFit(TAutoFit node)
    {
        if(this._autoFit_ != null)
        {
            this._autoFit_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._autoFit_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._excelColumn_)
            + toString(this._methodChainingOperator_)
            + toString(this._autoFit_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._excelColumn_ == child)
        {
            this._excelColumn_ = null;
            return;
        }

        if(this._methodChainingOperator_ == child)
        {
            this._methodChainingOperator_ = null;
            return;
        }

        if(this._autoFit_ == child)
        {
            this._autoFit_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._excelColumn_ == oldChild)
        {
            setExcelColumn((PExcelColumn) newChild);
            return;
        }

        if(this._methodChainingOperator_ == oldChild)
        {
            setMethodChainingOperator((TMethodChainingOperator) newChild);
            return;
        }

        if(this._autoFit_ == oldChild)
        {
            setAutoFit((TAutoFit) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
