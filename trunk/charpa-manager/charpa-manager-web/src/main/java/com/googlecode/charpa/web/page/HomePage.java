package com.googlecode.charpa.web.page;

import org.springframework.security.access.annotation.Secured;


/**
 * Home page
 */
@Secured({"ROLE_ADMIN"})
public class HomePage extends BasePage {

    public HomePage() {
        super();
    }
}
