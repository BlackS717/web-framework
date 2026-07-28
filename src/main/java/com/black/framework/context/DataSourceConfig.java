package com.black.framework.context;

public class DataSourceConfig {
    private final String url;
    private final String driverClassName;
    private final String username;
    private final String password;
    private final boolean useDatabase;
    
    public DataSourceConfig(String url, String driverClassName, String username, String password, boolean useDatabase){
        this.url = url;
        this.driverClassName = driverClassName;
        this.username = username;
        this.password = password;
        this.useDatabase = useDatabase;
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

    public boolean isUseDatabase(){
        return useDatabase;
    }

    @Override
    public String toString(){
        return  "url: " + url + 
                " username: " + username +
                " password:  " + password; 
    }

}
