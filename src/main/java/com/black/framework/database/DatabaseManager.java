package com.black.framework.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.black.framework.context.DataSourceConfig;

public class DatabaseManager {
    private final DataSourceConfig dataSourceConfig;

    public boolean isUseDatabase(){
        return dataSourceConfig.isUseDatabase();
    }

    public DatabaseManager(DataSourceConfig dataSourceConfig) throws SQLException{
        this.dataSourceConfig = dataSourceConfig;

        if(dataSourceConfig.isUseDatabase()){

            try (Connection connection = DriverManager.getConnection(
                    dataSourceConfig.getUrl(),
                    dataSourceConfig.getUsername(),
                    dataSourceConfig.getPassword()
                )
                ) 
            {

            } catch (SQLException e) {
                throw new SQLException();
            }
        }

    }

    public QueryResults executeQuery(QueryBuilder queryBuilder) throws SQLException{
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
                    System.out.println(">>> DATABASE QUERY TEST");
                    
                    ResultSetMetaData metaData = rs.getMetaData();    
                    int columnCount =  metaData.getColumnCount();
                    
                    List<String> columnsName = new ArrayList<>();
                    
                    for (int i = 1; i <= columnCount; i++) {
                        columnsName.add(metaData.getColumnLabel(i));
                    }

                    QueryResults results = new QueryResults(columnsName);

                    while(rs.next()){
                        List<Object> rowValues = new ArrayList<>();
                        
                        for (int i = 1; i <= columnCount; i++) {
                            Object value = rs.getObject(i);
                            rowValues.add(value);
                        }

                        results.addRow(rowValues);
                    }

                    return results;
                } catch (Exception e) {
                    throw new SQLException("Failed to process database query result", e);
                }

            } catch (Exception e) {
                throw new SQLException();
            }
            
        } catch (Exception e) {
            throw new SQLException();
        }
    }
}
