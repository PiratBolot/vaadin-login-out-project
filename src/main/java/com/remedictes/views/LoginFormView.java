package com.remedictes.views;

import com.remedictes.configuration.CustomRequestCache;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;


@Tag("sa-login-view")
@Route(value = LoginFormView.ROUTE)
@PageTitle("Login")
public class LoginFormView extends VerticalLayout {
    public static final String ROUTE = "login";

    private LoginOverlay login = new LoginOverlay();

    @Autowired
    public LoginFormView(AuthenticationManager authenticationManager,
                         CustomRequestCache requestCache) {
        // configures login dialog and adds it to the main view
        login.setOpened(true);
        login.setForgotPasswordButtonVisible(false);

        login.setI18n(createRussianI18n());

        add(login);

        login.addLoginListener(e -> { //
            try {
                // try to authenticate with given credentials, should always return !null or throw an {@link AuthenticationException}
                final Authentication authentication = authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(e.getUsername(), e.getPassword()));

                // if authentication was successful we will update the security context and redirect to the page requested first
                if(authentication != null ) {
                    login.close();
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    //Access to view by role
                    if (authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(role -> role.equals("ROLE_Admin"))) {
                        UI.getCurrent().navigate(AdditionalView.class);
                    } else {
                        UI.getCurrent().navigate(requestCache.resolveRedirectUrl());
                    }
                }
            } catch (AuthenticationException ex) { //
                login.setError(true);
            }
        });
    }

    private LoginI18n createRussianI18n() {
        final LoginI18n i18n = LoginI18n.createDefault();

        i18n.setHeader(new LoginI18n.Header());
        i18n.getHeader().setTitle("Vaadin Login/out");
        i18n.getHeader().setDescription("Главная форма");
        i18n.getForm().setUsername("Логин");
        i18n.getForm().setTitle("Вход");
        i18n.getForm().setSubmit("Войти");
        i18n.getForm().setPassword("Пароль");
        i18n.getErrorMessage().setTitle("Неверый логин/пароль");
        i18n.getErrorMessage()
                .setMessage("");
        return i18n;
    }
}