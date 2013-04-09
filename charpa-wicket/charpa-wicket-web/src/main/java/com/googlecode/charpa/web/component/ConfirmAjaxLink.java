package com.googlecode.charpa.web.component;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
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
    protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
        super.updateAjaxAttributes(attributes);
        attributes.getAjaxCallListeners().add(new AjaxCallListener() {
            @Override
            public CharSequence getPrecondition(Component component) {
                return "confirm('"+theQuestionModel.getObject()+"')";
            }
        });
    }

    private final IModel<String> theQuestionModel;
}
