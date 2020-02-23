package com.remedictes.views;

import com.remedictes.configuration.SecuredByRole;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.router.Route;

@Route("form")
@SecuredByRole("ROLE_Admin")
public class AdditionalView extends VerticalLayout {

    private ContactForm form = new ContactForm(this);

    public AdditionalView() {
        form.setContact(new Contact());
        HorizontalLayout formLayout = new HorizontalLayout(form);
        add(formLayout);

        Element logoutLink = ElementFactory.createAnchor("logout", "Logout"); //
        getElement().appendChild(logoutLink); //
    }
}
