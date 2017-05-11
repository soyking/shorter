package storage

import java.sql.DriverManager

fun init() {
    val connection = DriverManager
            .getConnection("jdbc:mysql://127.0.0.1:3306/shorter", "shorter", "shorter");
}