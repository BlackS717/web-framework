package com.black.framework.exceptions;

public class DataSourceNotConfiguredException extends RuntimeException {
    public DataSourceNotConfiguredException() {
        super("Cannot use DatabaseManager: no datasource configuration was found. "
            + "Configure datasource properties in application.properties.");
    }
}