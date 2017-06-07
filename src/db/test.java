package db;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;

public class test {

	public static void main(String[] args) throws SQLException {
		Database db = Database.getInstance();
		System.out.println(db.getPallets("Amneris"));
	}
}
