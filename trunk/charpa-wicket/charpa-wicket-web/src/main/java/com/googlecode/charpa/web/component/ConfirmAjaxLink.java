package com.googlecode.charpa.web.component;

import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxCallDecorator;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.model.IModel;

/**
 * Display confirm before action
 */
public abstract class ConfirmAjaxLink extends IndicatingAjaxLink {
    public ConfirmAjaxLink(String id, IModel<String> aQuestionModel) {
        super(id);
        theQuestionModel = aQuestionModel;
    }

    @Override
    protected IAjaxCallDecorator getAjaxCallDecorator() {
        return new AjaxCallDecorator() {
            @Override
            public CharSequence decorateScript(CharSequence script) {
                return "if(!confirm('"+theQuestionModel.getObject()+"')) return false;" + script;
            }
        };

    }
    private final IModel<String> theQuestionModel;
}
