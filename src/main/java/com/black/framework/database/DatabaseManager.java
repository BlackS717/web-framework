package com.black.framework.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import com.black.framework.context.DataSourceConfig;

public class DatabaseManager {
    private final DataSourceConfig dataSourceConfig;

    public DatabaseManager(DataSourceConfig dataSourceConfig){
        this.dataSourceConfig = dataSourceConfig;
    }

    public void executeQuery(QueryBuilder queryBuilder){
        try (Connection connection = DriverManager.getConnection(
                dataSourceConfig.getUrl(),
                dataSourceConfig.getUsername(),
                dataSourceConfig.getPassword()
            )
            ) 
        {

            try(PreparedStatement preparedStatement = connection.prepareStatement(queryBuilder.getQuery())){
                List<Object> parameters = queryBuilder.getParameters();
                int paramSize = parameters.size();

                for (int i = 0; i < paramSize; i++) {
                    preparedStatement.setObject(i+1, parameters.get(i));
                }

                try(ResultSet rs = preparedStatement.executeQuery()){
                    int index = 1;
                    System.out.println(">>> DATABASE QUERY TEST");
                    while(rs.next()){
                        System.out.println(rs.getObject(index++));
                    }
                } catch (Exception e) {
                    
                }

            } catch (Exception e) {
                
            }
            
        } catch (Exception e) {
            // connection failed
        }
    }
}
