package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil {
	private static final String PROPERTIES_PATH = "src/db_home.properties"; // 환경에 따라 수정
	private Properties prop;
	
	public DBUtil() {
		prop = new Properties();
		try (FileInputStream fis = new FileInputStream(PROPERTIES_PATH)){
			prop.load(fis);
			System.out.println(prop.getProperty("driver"));
			System.out.println(prop.getProperty("url"));
			System.out.println(prop.getProperty("username"));
			System.out.println(prop.getProperty("password"));
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public Connection getConnection() throws SQLException, ClassNotFoundException {
		Class.forName(prop.getProperty("driver"));
		return DriverManager.getConnection(
				prop.getProperty("url"),
				prop.getProperty("username"), 
				prop.getProperty("password"));
	}
}
