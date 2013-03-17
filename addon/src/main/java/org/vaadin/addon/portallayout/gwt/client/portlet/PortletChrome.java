package org.vaadin.addon.portallayout.gwt.client.portlet;

import org.vaadin.addon.portallayout.gwt.client.portlet.header.PortletHeader;

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.Util;

public class PortletChrome extends FlowPanel {
    
    private final PortletSlot associatedSlot = new PortletSlot(this);
    
    private final PortletHeader header = new PortletHeader();

    private final Element contentWrapper = DOM.createDiv();
    
    private final Element contentEl = DOM.createDiv();
    
    private Widget contentWidget;
    
    public PortletChrome() {
        super();
        setStyleName("v-portlet");
        header.setStyleName("v-portlet-header");
        getElement().getStyle().setPosition(Position.RELATIVE);
        super.add(header);

        contentWrapper.setClassName("v-portlet-content-wrapper");
        contentEl.setClassName("v-portlet-content");
        contentWrapper.appendChild(contentEl);
        
        getElement().appendChild(contentWrapper);
        
        contentEl.getStyle().setDisplay(Display.NONE);
        contentWrapper.getStyle().setPosition(Position.STATIC);
    }
    
    public PortletSlot getAssociatedSlot() {
        return associatedSlot;
    }
    
    public Widget getContentWidget() {
        return contentWidget;
    }
    
    public void setContentWidget(Widget content) {
        this.contentWidget = content;
        add(content);
    }

    public PortletHeader getHeader() {
        return header;
    }
    
    @Override
    public void add(Widget child) {
        super.add(child, hasRelativeHeight() ? contentEl : contentWrapper);
    }

    public void blur() {
        if (contentWidget != null) {
            contentWidget.getElement().blur();
        }
    }

    public void close() {
        getAssociatedSlot().removeFromParent();
    }

    public boolean isCollapsed() {
        return getStyleName().contains("collapsed");
    }

    public Element getContentElement() {
        return contentEl;
    }

    public void setHeaderToolbar(Widget toolbar) {
        header.setToolbar(toolbar);
    }

    public void updateContentStructure(boolean isHeightRelative) {
        if (isHeightRelative != hasRelativeHeight()) {
            if (isHeightRelative && !isCollapsed()) {
                contentEl.getStyle().setDisplay(Display.BLOCK);
                contentWrapper.getStyle().setPosition(Position.ABSOLUTE);
                remove(contentWidget);
                add(contentWidget, contentEl);
            } else {
                contentEl.getStyle().setDisplay(Display.NONE);
                contentWrapper.getStyle().setPosition(Position.STATIC);
                remove(contentWidget);
                add(contentWidget, contentWrapper);            
            }   
        }
    }

    public boolean hasRelativeHeight() {
        return Display.BLOCK.getCssName().equalsIgnoreCase(contentEl.getStyle().getDisplay());
    }

    public Element getElementWrapper() {
        return contentWrapper;
    }

    /**
     * For the case of relative-height content: the method will set the content element height to 'h' so that contentHeight(%) * h == exactly 
     * the height of available area (content wrapper).
     */
    public void resizeContent(int wrapperHeight) {
        float relativeHeightValue = Util.parseRelativeSize(getContentWidget().getElement().getStyle().getHeight());
        if (relativeHeightValue >= 0) {
            int contentHeight = (int) (wrapperHeight * 1f / relativeHeightValue * 100f);
            contentEl.getStyle().setHeight(contentHeight, Unit.PX);   
        }
    }

    public double getHeaderHeight() {
        return header.getOffsetHeight();
    }
}
