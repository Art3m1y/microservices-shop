package ru.art3m1y.petprojectshop.login.utils.exceptions;

public class PermissionException extends RuntimeException {
    public PermissionException() {
        super("У вас нет прав для доступа к этой странице!");
    }
}
