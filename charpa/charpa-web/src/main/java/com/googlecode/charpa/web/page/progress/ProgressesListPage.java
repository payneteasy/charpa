package com.googlecode.charpa.web.page.progress;

import com.googlecode.charpa.progress.service.IProgressInfoService;
import com.googlecode.charpa.web.page.BasePage;
import com.googlecode.charpa.web.progress.ListProgressesPanel;

/**
 * List of tasks
 */
public class ProgressesListPage extends BasePage {

    public ProgressesListPage() {
        add(new ListProgressesPanel("progresses-panel", ProgressPage.class, theProgressInfoService));
    }

    private IProgressInfoService theProgressInfoService =null;
}
