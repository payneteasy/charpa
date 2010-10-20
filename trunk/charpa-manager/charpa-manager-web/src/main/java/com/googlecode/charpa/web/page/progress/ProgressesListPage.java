package com.googlecode.charpa.web.page.progress;

import com.googlecode.charpa.progress.service.IProgressInfoService;
import com.googlecode.charpa.web.page.BasePage;
import com.googlecode.charpa.web.progress.ListProgressesPanel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * List of tasks
 */
public class ProgressesListPage extends BasePage {

    public ProgressesListPage() {
        add(new ListProgressesPanel("progresses-panel", ProgressPage.class, theProgressInfoService));
    }

    @SpringBean
    private IProgressInfoService theProgressInfoService;
}
