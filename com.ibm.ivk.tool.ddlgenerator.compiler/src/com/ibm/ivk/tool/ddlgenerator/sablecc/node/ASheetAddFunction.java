/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class ASheetAddFunction extends PAddFunction
{
    private PExcelSheet _excelSheet_;
    private TMethodChainingOperator _methodChainingOperator_;
    private TAdd _add_;
    private PParamCallList _paramCallList_;

    public ASheetAddFunction()
    {
        // Constructor
    }

    public ASheetAddFunction(
        @SuppressWarnings("hiding") PExcelSheet _excelSheet_,
        @SuppressWarnings("hiding") TMethodChainingOperator _methodChainingOperator_,
        @SuppressWarnings("hiding") TAdd _add_,
        @SuppressWarnings("hiding") PParamCallList _paramCallList_)
    {
        // Constructor
        setExcelSheet(_excelSheet_);

        setMethodChainingOperator(_methodChainingOperator_);

        setAdd(_add_);

        setParamCallList(_paramCallList_);

    }

    @Override
    public Object clone()
    {
        return new ASheetAddFunction(
            cloneNode(this._excelSheet_),
            cloneNode(this._methodChainingOperator_),
            cloneNode(this._add_),
            cloneNode(this._paramCallList_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseASheetAddFunction(this);
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

    public TAdd getAdd()
    {
        return this._add_;
    }

    public void setAdd(TAdd node)
    {
        if(this._add_ != null)
        {
            this._add_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._add_ = node;
    }

    public PParamCallList getParamCallList()
    {
        return this._paramCallList_;
    }

    public void setParamCallList(PParamCallList node)
    {
        if(this._paramCallList_ != null)
        {
            this._paramCallList_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._paramCallList_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._excelSheet_)
            + toString(this._methodChainingOperator_)
            + toString(this._add_)
            + toString(this._paramCallList_);
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

        if(this._add_ == child)
        {
            this._add_ = null;
            return;
        }

        if(this._paramCallList_ == child)
        {
            this._paramCallList_ = null;
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

        if(this._add_ == oldChild)
        {
            setAdd((TAdd) newChild);
            return;
        }

        if(this._paramCallList_ == oldChild)
        {
            setParamCallList((PParamCallList) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
