package com.github.heidiks;

import java.sql.SQLException;

public class Test {

    public static void main(String[] args) throws SQLException {

//        final Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "admin");
//        if(!connection.isClosed()){
//            System.out.println("yes");
//        }
        System.out.println(Runtime.getRuntime().availableProcessors());

    }
}
