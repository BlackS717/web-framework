package com.black.framework.context;

public class DataSourceConfig {
    private final String url;
    private final String driverClassName;
    private final String username;
    private final String password;
    
    public DataSourceConfig(String url, String driverClassName, String username, String password){
        this.url = url;
        this.driverClassName = driverClassName;
        this.username = username;
        this.password = password;
    }
    
    public String getUrl(){
        return this.url;
    }

    public String getDriverClassName() {
        return driverClassName;
    }
    
    public String getUsername(){
        return this.username;
    }

    public String getPassword(){
        return this.password;
    }

    @Override
    public String toString(){
        return  "url: " + url + 
                " username: " + username +
                " password:  " + password; 
    }

}
