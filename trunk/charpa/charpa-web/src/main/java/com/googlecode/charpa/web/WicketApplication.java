package com.googlecode.charpa.web;

import com.googlecode.charpa.web.page.HomePage;
import com.googlecode.charpa.web.page.progress.ProgressesListPage;
import com.googlecode.charpa.web.page.progress.ProgressPage;
import com.googlecode.charpa.web.page.command.CommandsListPage;
import org.apache.wicket.extensions.ajax.markup.html.form.upload.UploadWebRequest;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * Application object for web application.
 * If you want to run this application without deploying, run the Start class.
 */
public class WicketApplication extends WebApplication {

    /**
     * Mounts pages
     */
    public WicketApplication() {
//        mountBookmarkablePage("/test", TestPage.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void init() {
        String sourcePath = "src/main/java";
        if(new File(sourcePath).exists()) {
            getResourceSettings().addResourceFolder("src/main/java");
        }
        addComponentInstantiationListener(new SpringComponentInjector(this));
        getDebugSettings().setOutputMarkupContainerClassName(false);

        mountBookmarkablePage("/commands"   , CommandsListPage.class);
        mountBookmarkablePage("/tasks"      , ProgressesListPage.class);
        mountBookmarkablePage("/task"       , ProgressPage.class);

        
//        getSecuritySettings().setAuthorizationStrategy(new SpringSecurityAuthorizationStrategy());

    }

    /**
     * Return spring's apprication context
     *
     * @return spring's apprication context
     */
    public ApplicationContext getApplicationContext() {
        return WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
    }

    /**
     * {@inheritDoc}
     */
    public Class<CommandsListPage> getHomePage() {
        return CommandsListPage.class;
    }


    /**
     * @see org.apache.wicket.protocol.http.WebApplication#newWebRequest(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected WebRequest newWebRequest(HttpServletRequest servletRequest) {
        return new UploadWebRequest(servletRequest);
    }
}

