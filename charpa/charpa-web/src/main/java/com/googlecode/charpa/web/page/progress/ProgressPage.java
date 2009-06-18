package com.googlecode.charpa.web.page.progress;

import com.googlecode.charpa.web.page.BasePage;
import com.googlecode.charpa.web.progress.ProgressPanel;
import org.apache.wicket.PageParameters;

/**
 * Progress page
 */
public class ProgressPage extends BasePage {

    public ProgressPage(PageParameters aParameters) {
        add(new ProgressPanel("progress-panel", aParameters));
    }
}
