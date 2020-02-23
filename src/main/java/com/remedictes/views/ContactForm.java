package com.remedictes.views;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.SerializablePredicate;

import java.util.Optional;
import java.util.stream.Collectors;

public class ContactForm extends FormLayout {

    private TextField firstName = new TextField("First name");
    private TextField lastName = new TextField("Last name");
    private TextField phone = new TextField("Phone");
    private DatePicker birthDate = new DatePicker("Birthdate");
    private TextField email = new TextField("E-mail");
    private Button save = new Button("Save");
    private Button reset = new Button("Reset");
    private Label infoLabel = new Label();

    private Binder<Contact> binder = new Binder<>(Contact.class);
    private AdditionalView additionalView;

    public ContactForm(AdditionalView additionalView) {
        this.additionalView = additionalView;

        firstName.setValueChangeMode(ValueChangeMode.EAGER);
        lastName.setValueChangeMode(ValueChangeMode.EAGER);
        phone.setValueChangeMode(ValueChangeMode.EAGER);
        email.setValueChangeMode(ValueChangeMode.EAGER);

        save.getStyle().set("marginRight", "10px");
        infoLabel.getStyle().set("color", "white");
        infoLabel.getStyle().set("padding", "10px");
        infoLabel.getStyle().set("font-family", "Arial");

        HorizontalLayout buttons = new HorizontalLayout(save, reset, infoLabel);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        add(firstName, lastName, phone, birthDate, email, buttons);
        binder.bindInstanceFields(this);

        save.addClickListener(this::save);
        reset.addClickListener(this::reset);

        SerializablePredicate<String> phoneOrEmailPredicate = value -> !phone
                .getValue().trim().isEmpty()
                || !email.getValue().trim().isEmpty();

        Binder.Binding<Contact, String> emailBinding = binder.forField(email)
                .withValidator(phoneOrEmailPredicate,
                        "Both phone and email cannot be empty")
                .withValidator(new EmailValidator("Incorrect email address"))
                .bind(Contact::getEmail, Contact::setEmail);

        Binder.Binding<Contact, String> phoneBinding = binder.forField(phone)
                .withValidator(phoneOrEmailPredicate,
                        "Both phone and email cannot be empty")
                .bind(Contact::getPhone, Contact::setPhone);

        email.addValueChangeListener(event -> phoneBinding.validate());
        phone.addValueChangeListener(event -> emailBinding.validate());

        firstName.setRequiredIndicatorVisible(true);
        lastName.setRequiredIndicatorVisible(true);

        binder.forField(firstName)
                .withValidator(new StringLengthValidator(
                        "Please add the first name", 1, null))
                .bind(Contact::getFirstName, Contact::setFirstName);
        binder.forField(lastName)
                .withValidator(new StringLengthValidator(
                        "Please add the last name", 1, null))
                .bind(Contact::getLastName, Contact::setLastName);

        binder.bind(birthDate, Contact::getBirthDate, Contact::setBirthDate);
    }

    public void setContact(Contact contact) {
        binder.setBean(contact);
    }

    private void save(ClickEvent<Button> event) {
        infoLabel.setVisible(true);
        if (binder.writeBeanIfValid(binder.getBean())) {
            infoLabel.getStyle().set("background-color", "#4CAF50");
            infoLabel.setText("Saved bean values: " + binder.getBean());
        } else {
            BinderValidationStatus<Contact> validate = binder.validate();
            String errorText = validate.getFieldValidationStatuses()
                    .stream().filter(BindingValidationStatus::isError)
                    .map(BindingValidationStatus::getMessage)
                    .map(Optional::get).distinct()
                    .collect(Collectors.joining(", "));
            infoLabel.getStyle().set("background-color", "#ff9800");
            infoLabel.setText("There are errors: " + errorText);
        }
    }

    private void reset(ClickEvent<Button> event) {
        binder.readBean(null);
        infoLabel.setText("");
        infoLabel.setVisible(false);

    }


}
