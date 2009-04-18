package com.googlecode.charpa.web.page;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.AbstractReadOnlyModel;

/**
 * Base page with main layout
 */
public class BasePage extends WebPage {
    /**
     * {@inheritDoc}
     */
    public BasePage() {
        init();
    }

    /**
     * {@inheritDoc}
     */
    public BasePage(PageParameters aParameters) {
        super(aParameters);
        init();
    }

    private void init() {
//        addLink("products-link", ListProductsPage.class, productsPanel);

        add(new Label("user-name", "todo"));
    }

    private void addLink(String aId, Class aPageClass) {
        addLink(aId, aPageClass, this);
    }

    private void addLink(String aId, Class aPageClass, MarkupContainer aComponent) {

        BookmarkablePageLink link = new BookmarkablePageLink(aId, aPageClass);
        if (getClass().equals(aPageClass)) { // || aPageClass.getPackage().equals(getClass().getPackage())) {
            link.add(new AttributeModifier("class", true, new AbstractReadOnlyModel() {
                public Object getObject() {
                    return "p-nav-selected";
                }
            }));
        }
        aComponent.add(link);
    }
}
