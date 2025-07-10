package com.company.logistics.models.contracts;

import com.company.logistics.enums.UserRole;

import java.util.List;

public interface User extends Printable {
    String getUsername();

    String getName();

    UserRole getRole();

    boolean checkPassword(String password);
}
