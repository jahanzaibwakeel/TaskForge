package com.taskforge.taskforge.service.organizationmember;

public class OrganizationMemberNotFoundException extends RuntimeException {

    public OrganizationMemberNotFoundException(String message) {
        super(message);
    }
}