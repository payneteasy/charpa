package com.googlecode.charpa.web.component;

import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxCallDecorator;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;

/**
 * Display confirm before action
 */
public abstract class ConfirmAjaxLink extends IndicatingAjaxLink {
    public ConfirmAjaxLink(String id, String aQuestion) {
        super(id);
        theQuestion = aQuestion;
    }

    @Override
    protected IAjaxCallDecorator getAjaxCallDecorator() {
        return new AjaxCallDecorator() {
            @Override
            public CharSequence decorateScript(CharSequence script) {
                return "if(!confirm('"+theQuestion+"')) return false;" + script;
            }
        };

    }
    private final String theQuestion;
}
