package com.remedictes.views;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
public class Contact implements Serializable {

    private String firstName = "";
    private String lastName = "";
    private String phone = "";
    private String email = "";
    private LocalDate birthDate;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(firstName).append(" ").append(lastName);
        if (birthDate != null) {
            builder.append(", born on ").append(birthDate);
        }
        if (phone != null && !phone.isEmpty()) {
            builder.append(", phone ").append(phone);
        }
        if (email != null && !email.isEmpty()) {
            builder.append(", e-mail ").append(email);
        }
        return builder.toString();
    }
}
