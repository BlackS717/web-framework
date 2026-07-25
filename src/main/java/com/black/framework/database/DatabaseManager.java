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

        try{
            Class.forName(dataSourceConfig.getDriverClassName());
        } catch (Exception e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public String executeQuery(QueryBuilder queryBuilder){
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
                    String data = "";
                    while(rs.next()){
                        data += rs.getObject(index++).toString();
                    }

                    return data;
                } catch (Exception e) {
                    return "Failed to Query the data";
                }

            } catch (Exception e) {
                return "Failed to prepare statement to database";    
            }
            
        } catch (Exception e) {
            return "Failed to connect to database " + dataSourceConfig.toString() + "\n" + e.getMessage() ;
        }
    }
}
