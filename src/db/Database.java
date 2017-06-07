package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Database is a class that specifies the interface to the 
 * Krusty database. Uses JDBC and the MySQL Connector/J driver.
 */
public class Database {
	/** 
	 * The database connection.
	 */
	private Connection conn;
	private static Database theInstance = new Database();


	public static Database getInstance() {
		return theInstance;
	}

	/**
	 * Create the database interface object. Connection to the database
	 * is performed later.
	 */
	private Database() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection 
					("jdbc:mysql://puccini.cs.lth.se/hbg13",
							"hbg13", "osmanhamza13");
		}
		catch (SQLException e) {
			System.err.println(e);
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {	
			System.err.println(e);
			e.printStackTrace();
		}
	}

	/**
	 * Close the connection to the database.
	 * @return 
	 */
	public Connection closeConnection() {
		try {
			if (conn != null)
				conn.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		conn = null;

		System.err.println("Database connection closed.");
		return conn;
	}

	/**
	 * Check if the connection to the database has been established
	 *
	 * @return true if the connection has been established
	 */
	public boolean isConnected() {
		return conn != null;
	}

	public List<String> ingredientStorage() throws SQLException{
		ArrayList<String> ingredients = new ArrayList<String>();
		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT IngredientName, AmountStored "
					+ "FROM Ingredients ");
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				ingredients.add(rs.getString(1) + ", " + rs.getString(2));
			}
			return ingredients;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
	}

	public List<String> getAllCookies() throws SQLException{
		ArrayList<String> ListOfCookies = new ArrayList<String>();
		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT ProductName "
					+ "FROM products "
					+ "order by ProductName");
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				ListOfCookies.add(rs.getString(1));
			}
			return ListOfCookies;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
	}

	public List<String> getRecipe(String productName) throws SQLException{
		ArrayList<String> recipe = new ArrayList<String>();
		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT IngredientName, AmountNeeded "
					+ "FROM hasIngredients "
					+ "WHERE ProductName = ?");
			stmt.setString(1, productName);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				recipe.add(rs.getString(1) + ", " + rs.getString(2));
			}
			return recipe;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}

	}

	public List<String> getPallet(int palletId) throws SQLException{
		List<String> s = new ArrayList<String>();
		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT * "
					+ "FROM Pallets "
					+ "WHERE palletId = ? ");
			stmt.setInt(1, palletId);
			PreparedStatement stmt2 = conn.prepareStatement("select productName from holdsProducts "
					+ "where palletId = ?");
			stmt2.setInt(1, palletId);
			ResultSet rs = stmt.executeQuery();
			ResultSet rs2 = stmt2.executeQuery();
			rs.next();
			rs2.next();
			s.add(rs2.getString(1));
			s.add(booleanToYesNo(rs.getString(2)));
			s.add(rs.getString(4)+ " " + rs.getString(3));
			s.add(rs.getString(6));
			s.add(booleanToYesNo(rs.getString(5)));
			//Reference: s =  "Product Name: " + rs2.getString(1) + "\n " + "Delivered: " + booleanToYesNo(rs.getString(2)) + "\n" + "Production Date: " + rs.getString(4) + " " + rs.getString(3) + "\n" + "Location: " + rs.getString(5) + "\n" + "Blocked: " + booleanToYesNo(rs.getString(6));
			return s;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}


	}

	public List<String> getPallets(String date1, String date2) throws SQLException{
		String s = null;
		List<String> list = new ArrayList<String>();
		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT PalletID "
					+ "FROM Pallets "
					+ "WHERE ProductionDate >= ? "
					+ "and ProductionDate <= ? ");
			stmt.setString(1, date1);
			stmt.setString(2, date2);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				s = rs.getString(1);
				list.add(s);
			}
			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}

	}

	public List<String> getPallets(String productName) throws SQLException{
		String s = null;
		List<String> list = new ArrayList<String>();
		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT PalletID "
					+ "FROM Pallets natural join holdsProducts "
					+ "WHERE productName = ? ");
			stmt.setString(1, productName);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				s =  rs.getString(1);
				list.add(s);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}


	}

	private String booleanToYesNo(String in) {
		if (in.equals("1")) {
			return "Yes";
		} else {
			return "No";
		}
	}

	public void blockPallet(String date, String productName) throws SQLException{
		try {
			PreparedStatement stmt2 = conn.prepareStatement("UPDATE Pallets natural join holdsProducts "
					+ "Set blockedStatus = true "
					+ "where ProductionDate = ? "
					+ "and productName = ? ");
			stmt2.setString(1, date);
			stmt2.setString(2, productName);
			stmt2.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}

	}

	private boolean productionCheck(String productName) throws SQLException{
		boolean check = false;
		try {
			PreparedStatement stmt1 = conn.prepareStatement("Select productName, IngredientName, AmountNeeded, AmountStored "
					+ "From Products natural join hasIngredients natural join Ingredients "
					+ "Where productName = ?");
			stmt1.setString(1, productName);
			ResultSet rs = stmt1.executeQuery();
			while(rs.next()){
				if(rs.getInt(3) > rs.getInt(4)){
					return check;
				}
			}
			check = true;

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		return true;
	}

	public void produceCookies(String productName) throws SQLException{
		if(!productionCheck(productName)){
			throw new SQLException();
		}
		try {
			addPallet();
			addHoldsProduct(productName);
			PreparedStatement stmt = conn.prepareStatement("Update Ingredients natural join hasIngredients "
					+ "set AmountStored = AmountStored - AmountNeeded "
					+ "where productName = ? ");
			stmt.setString(1, productName);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}		

	}

	private void addPallet() throws SQLException{
		try{
			PreparedStatement stmt = conn.prepareStatement("insert into Pallets(DeliveredStatus, ProductionTime, ProductionDate, PalletLocation, BlockedStatus) values (false, curtime(), curdate(), 'freezer' , false) ");
			stmt.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
			throw e;
		}
	}

	private void addHoldsProduct(String productName) throws SQLException{
		try{
			PreparedStatement stmt = conn.prepareStatement("insert into holdsProducts (productName, palletId) "
					+ "select ?, max(palletId) from pallets ");
			stmt.setString(1, productName);
			stmt.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
			throw e;
		}
	}

}
