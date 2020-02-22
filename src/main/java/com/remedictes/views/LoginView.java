package com.remedictes.views;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;

@Tag("sa-login-view")
@Route(value = LoginView.ROUTE)
@PageTitle("Login")
public class LoginView extends Component {
    public static final String ROUTE = "login";

    public interface Model extends TemplateModel {
        void setError(boolean error);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        getElement().setText("Do you wonder why you do not see any login UI? Well, this is the master branch only containing the security setup. For different UI implementations check the other branches :)");
    }
}