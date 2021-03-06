/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AWithParamsExcelSelection extends PExcelSelection
{
    private TSelection _selection_;
    private TMethodChainingOperator _methodChainingOperator_;
    private TFormatConditions _formatConditions_;
    private PParamCallList _paramCallList_;

    public AWithParamsExcelSelection()
    {
        // Constructor
    }

    public AWithParamsExcelSelection(
        @SuppressWarnings("hiding") TSelection _selection_,
        @SuppressWarnings("hiding") TMethodChainingOperator _methodChainingOperator_,
        @SuppressWarnings("hiding") TFormatConditions _formatConditions_,
        @SuppressWarnings("hiding") PParamCallList _paramCallList_)
    {
        // Constructor
        setSelection(_selection_);

        setMethodChainingOperator(_methodChainingOperator_);

        setFormatConditions(_formatConditions_);

        setParamCallList(_paramCallList_);

    }

    @Override
    public Object clone()
    {
        return new AWithParamsExcelSelection(
            cloneNode(this._selection_),
            cloneNode(this._methodChainingOperator_),
            cloneNode(this._formatConditions_),
            cloneNode(this._paramCallList_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAWithParamsExcelSelection(this);
    }

    public TSelection getSelection()
    {
        return this._selection_;
    }

    public void setSelection(TSelection node)
    {
        if(this._selection_ != null)
        {
            this._selection_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._selection_ = node;
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

    public TFormatConditions getFormatConditions()
    {
        return this._formatConditions_;
    }

    public void setFormatConditions(TFormatConditions node)
    {
        if(this._formatConditions_ != null)
        {
            this._formatConditions_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._formatConditions_ = node;
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
            + toString(this._selection_)
            + toString(this._methodChainingOperator_)
            + toString(this._formatConditions_)
            + toString(this._paramCallList_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._selection_ == child)
        {
            this._selection_ = null;
            return;
        }

        if(this._methodChainingOperator_ == child)
        {
            this._methodChainingOperator_ = null;
            return;
        }

        if(this._formatConditions_ == child)
        {
            this._formatConditions_ = null;
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
        if(this._selection_ == oldChild)
        {
            setSelection((TSelection) newChild);
            return;
        }

        if(this._methodChainingOperator_ == oldChild)
        {
            setMethodChainingOperator((TMethodChainingOperator) newChild);
            return;
        }

        if(this._formatConditions_ == oldChild)
        {
            setFormatConditions((TFormatConditions) newChild);
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
