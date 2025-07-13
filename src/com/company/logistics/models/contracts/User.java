package com.company.logistics.models.contracts;

import com.company.logistics.enums.UserRole;

public interface User extends Printable {
    String getUsername();

    String getName();

    UserRole getRole();

    boolean checkPassword(String password);
}
