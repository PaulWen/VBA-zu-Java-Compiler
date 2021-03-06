/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.ibm.ivk.tool.ddlgenerator.sablecc.node;

import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AExcelActiveWindow extends PExcelActiveWindow
{
    private TActiveWindow _activeWindow_;

    public AExcelActiveWindow()
    {
        // Constructor
    }

    public AExcelActiveWindow(
        @SuppressWarnings("hiding") TActiveWindow _activeWindow_)
    {
        // Constructor
        setActiveWindow(_activeWindow_);

    }

    @Override
    public Object clone()
    {
        return new AExcelActiveWindow(
            cloneNode(this._activeWindow_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAExcelActiveWindow(this);
    }

    public TActiveWindow getActiveWindow()
    {
        return this._activeWindow_;
    }

    public void setActiveWindow(TActiveWindow node)
    {
        if(this._activeWindow_ != null)
        {
            this._activeWindow_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._activeWindow_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._activeWindow_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._activeWindow_ == child)
        {
            this._activeWindow_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._activeWindow_ == oldChild)
        {
            setActiveWindow((TActiveWindow) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
