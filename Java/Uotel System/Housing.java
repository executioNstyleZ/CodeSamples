package phase2;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Housing {

	public Housing(){}
	
	//adds a new PH to the system
	public String addNewPH(String thName, String address, String category, String url, int yearBuilt, String phonenumber, String username, String city, Statement stmt){
		int rowsChanged = 0;
		String result = "";
		
		String sql="INSERT into Housing (name, address, category, URL, yearbuilt, phonenumber, login, city)"
				+ "VALUES ('"+thName+"','"+address+"','"+category+"','"+url+"',"+yearBuilt+", '"+phonenumber+"', '"+username+"', '"+city+"')";
		
		 	try{
   		 	rowsChanged = stmt.executeUpdate(sql);
   		 	
   		 	if(rowsChanged > 0){
   		 		result = "Permanent housing successfully added";
   		 	}else{
   		 		result = "There was an error adding the permanent housing";
   		 	}
   		 	
		 	}
		 	catch(SQLException e)
		 	{
				System.err.println("cannot execute the query");
				System.err.println("error: " + e.getMessage());
		 	}
		 	
		 	return result;
	}
	
	//updates the existing PH with the new information
	public String updateCurrentPH(int hid, String thName, String address, String category, String url, int yearBuilt, String phonenumber, String username, String city, Statement stmt){
		int rowsChanged = 0;
		String result = "";
		
		//TODO: make sure username is the owner of this housing
		
		String sql="UPDATE Housing "
				+ "SET name = '"+thName+"', address = '"+address+"', category = '"+category+"', "
						+ "URL = '"+url+"', yearbuilt = "+yearBuilt+", phonenumber = '"+phonenumber+"', city = '"+city+"' "
								+ "WHERE hid = "+hid+"";
		try{
			rowsChanged = stmt.executeUpdate(sql);
			
			if(rowsChanged > 0){
				result = "Permanent housing successfully updated";
			}else{
				result = "There was an error updatingthe permanent housing";
			}
		}catch(SQLException e){
			System.err.println("cannot execute the query");
			System.err.println("error: " + e.getMessage());
		}
		
		return result;
	}
	
	public String removeHousingOwnedByUser(int hid, Statement stmt){
		String result = "";
		String sql = "DELETE from Housing where hid = "+hid+"";
		
		int rowsAffected = 0;
		
		try{
			rowsAffected = stmt.executeUpdate(sql);
			if(rowsAffected > 0){
				result += "House was removed:";
			}else{
				result += "There was an error processing your request:";
			}
		}
		catch(SQLException e){
			System.err.println("cannot execute the query");
			System.err.println("error: " + e.getMessage());
		}
		
		return result;
	}
	
	public String showHousingOwnedByUser(String username, Statement stmt){
		String result = "";
		String sql = "SELECT * FROM Housing WHERE login = '"+username+"'";
		
		ResultSet rs = null;
		try{
			rs = stmt.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int colCount = rsmd.getColumnCount();
			while(rs.next()){
				for(int i = 1; i <= colCount; i ++){
					result += rs.getString(i) + " ";
				}
				result += "\n";
			}
			
		}catch(SQLException e){
			System.err.println("cannot execute the query");
			System.err.println("error: " + e.getMessage());
		}
		
		return result;
	}
	
	public boolean checkHouseIDbelongsToUser(int hid, String username, Statement stmt){
		boolean result = false;
		
		String sql = "SELECT login FROM Housing WHERE hid = "+hid+"";
		
		ResultSet rs = null;
		try{
			rs = stmt.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();

			rs.next();
			if(rs.getString("login").equals(username)){
				result = true;
			}
			
		}catch(SQLException e){
			System.err.println("cannot execute the query");
			System.err.println("error: " + e.getMessage());
		}
		
		return result;
	}
	
	//returns a string containing a list of PHs with the given name
	//return empty string if none are found
	public String showHouseInformationWithName(String phName, Statement stmt){
		String result = "";
		String sql = "SELECT * FROM Housing WHERE name = '"+phName+"'";
		
		ResultSet rs = null;
		try{
			rs = stmt.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int colCount = rsmd.getColumnCount();
			while(rs.next()){
				for(int i = 1; i < colCount; i ++){
					result += rs.getString(i) + " ";
				}
			}
			
		}catch(SQLException e){
			System.err.println("cannot execute the query");
			System.err.println("error: " + e.getMessage());
		}
		
		return result;
	}
	
	//returns a string that displays all columns of the given ph id
	public String displayPHinformation(int id, Statement stmt){
		String result = "";
		String sql = "SELECT * FROM Housing WHERE hid = "+id+"";
		
		ResultSet rs = null;
		try{
			rs = stmt.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int colCount = rsmd.getColumnCount();
			
			while(rs.next()){
				for(int i = 1; i < colCount; i ++){
					result += rs.getString(i) + " ";
				}
			}
			
		}catch(SQLException e){
			System.err.println("cannot execute the query");
			System.err.println("error: " + e.getMessage());
		}
		
		return result;
	}
	
	//checks whether a TH exists
	public boolean checkHousingExists(int housingID, Statement stmt){
		boolean result= false;
		
		String sql= "SELECT * FROM Housing WHERE hid = "+housingID+"";
		
		ResultSet rs = null;
		try{
			rs = stmt.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			
			if(rsmd.getColumnCount() == 0){
				result = false;
			}else if(rsmd.getColumnCount() > 0){
				result = true;
			}
			
		}catch(SQLException e){
			System.err.println("cannot execute the query");
			System.err.println("error: " + e.getMessage());
		}
		
		return result;
	}
	
	public String displayNameFromID(int hid, Statement stmt){
		String result = "";
		String sql = "SELECT name FROM Housing WHERE hid = "+hid+"";
		
		ResultSet rs = null;
		try{
			rs = stmt.executeQuery(sql);

			rs.next();
			result = rs.getString("name");
			
		}catch(SQLException e){
			System.err.println("cannot execute the query");
			System.err.println("error: " + e.getMessage());
		}
		
		return result;
	}

	public String displayAllHousing(Statement stmt){
		String result = "";
		String sql = "SELECT * FROM Housing";
		
		ResultSet rs = null;
		try{
			rs = stmt.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int colCount = rsmd.getColumnCount();
			
			while(rs.next()){
				for(int i = 1; i < colCount; i ++){
					result += rs.getString(i) + " ";
				}
				result += "\n";
			}
			
		}catch(SQLException e){
			System.err.println("cannot execute the query");
			System.err.println("error: " + e.getMessage());
		}
		
		return result;
	}
	
	//browses through the housing, return a string of information of TH(s) that is found
	// lowerPriceRange = 0 means no input
	// upperPriceRange = 0 means no input
	// city = null means no input
	// keyword = null means no input
	// category = null means no input
	// sorting = 1 means sort by price
	// sorting = 2 means sort by the average numerical score of the feedbacks
	// sorting = 3 means sort by the average numerical score of the trusted user feedbacks
	public String browseTemporaryHousing(int lowerPriceRange, int upperPriceRange, String city, String keyword, String category, int sorting, Statement stmt){
		String result = "";
		
		String sql;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		
		List<Integer> hids = new ArrayList<Integer>();
		
		if(upperPriceRange > 0){
			sql = "select distinct h.hid from Housing h, Available a where price > "+lowerPriceRange+" AND price < "+upperPriceRange+"";
		
			try{
				rs = stmt.executeQuery(sql);
				rsmd = rs.getMetaData();
				
				while(rs.next()){
					int hid = rs.getInt("hid");
					hids.add(hid);
				}
			}
			catch(SQLException e){
				System.err.println("cannot execute the query");
				System.err.println("error: " + e.getMessage());
			}
		}
		
		if(city != null){
			sql = "select distinct h.hid from Housing h, Available a where city = '"+city+"'";
			
			try{
				rs = stmt.executeQuery(sql);
				rsmd = rs.getMetaData();
				
				hids.clear();

				
				while(rs.next()){
					int hid = rs.getInt("hid");
					hids.add(hid);
				}
			}
			catch(SQLException e){
				System.err.println("cannot execute the query");
				System.err.println("error: " + e.getMessage());
			}
		}
		
		if(keyword != null){
			sql = "select distinct h.hid from Keywords k, HasKeywords hk, Housing h "
					+ "where k.word = '"+keyword+"' AND k.wid = hk.wid AND h.hid = hk.hid";
			
			
			
			try{
				rs = stmt.executeQuery(sql);
				rsmd = rs.getMetaData();
				
				hids.clear();

				
				while(rs.next()){
					int hid = rs.getInt("hid");
					hids.add(hid);
				}
			}
			catch(SQLException e){
				System.err.println("cannot execute the query");
				System.err.println("error: " + e.getMessage());
			}
		}
		
		if(category != null){
			sql = "select distinct hid from Housing where category = '"+category+"'";
			
			try{
				rs = stmt.executeQuery(sql);
				rsmd = rs.getMetaData();
				
				hids.clear();

				
				while(rs.next()){
					int hid = rs.getInt("hid");
					hids.add(hid);
				}
			}
			catch(SQLException e){
				System.err.println("cannot execute the query");
				System.err.println("error: " + e.getMessage());
			}
		}
		
		if(sorting == 1){
			for(int i = 0; i < hids.size(); i++){
				int hid = hids.get(i);
				result += byPrice(hid, stmt);
			}
		}
		else if(sorting == 2){
			for(int i = 0; i < hids.size(); i++){
				int hid = hids.get(i);
				result += byScoreFeedbacks(hid, stmt);
			}
		}
		else if(sorting == 3){
			for(int i = 0; i < hids.size(); i++){
				int hid = hids.get(i);
				result += byScoreTrustedFeedbacks(hid, stmt);
			}
		}
		
		return result;
	}
	
	private String byPrice(int hid, Statement stmt){
		String result = "";
		
		String sql = "select h.hid, h.name, h.city, h.category, h.URL, avg(price) as avgPrice "
				+ "from Housing h, Available a WHERE"
				+ " h.hid = a.hid AND h.hid = "+hid+" "
				+ "group by h.hid "
				+ "order by avgPrice DESC";
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		
		try{
			rs = stmt.executeQuery(sql);
			rsmd = rs.getMetaData();
			int colCount = rsmd.getColumnCount();
			
			while(rs.next()){
				for(int i = 1; i <= colCount; i++){
					result += "$" + rs.getString(i) + " ";
				}
				result += "\n";
			}
		}
		catch(SQLException e){
			System.err.println("cannot execute the query");
			System.err.println("error: " + e.getMessage());
		}
		
		return result;
	}
	
	private String byScoreFeedbacks(int hid, Statement stmt){
		String result = "";
		
		String sql = "select h1.hid, h1.name, h1.city, h1.category, h1.URL, avg(f.score) as AvgScore "
				+ " from Feedback f left join Housing h1 on f.hid = h1.hid "
				+ "where exists (select * from Housing h "
				+ "where exists (select distinct login2 from Trust t "
				+ "where t.login2 = f.login AND f.hid = h.hid AND h.hid = "+hid+" )) "
						+ "group by h1.hid "
						+ "order by AvgScore DESC";
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		
		try{
			rs = stmt.executeQuery(sql);
			rsmd = rs.getMetaData();
			int colCount = rsmd.getColumnCount();
			
			while(rs.next()){
				for(int i = 1; i <= colCount; i++){
					result += rs.getString(i) + " ";
				}
				result += "\n";
			}
		}
		catch(SQLException e){
			System.err.println("cannot execute the query");
			System.err.println("error: " + e.getMessage());
		}
		
		return result;
	}
	
	private String byScoreTrustedFeedbacks(int hid, Statement stmt){
		String result = "";
		
		String sql = "select h1.hid, h1.name, h1.city, h1.category, h1.URL, avg(f.score) as AvgScore "
				+ " from Feedback f left join Housing h1 on f.hid = h1.hid "
				+ "where exists (select * from Housing h "
				+ "where exists (select distinct login2 from Trust t "
				+ "where t.isTrusted = 1 AND t.login2 = f.login AND f.hid = h.hid AND h.hid = "+hid+" )) "
						+ "group by h1.hid "
						+ "order by AvgScore DESC";
		
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		
		try{
			rs = stmt.executeQuery(sql);
			rsmd = rs.getMetaData();
			int colCount = rsmd.getColumnCount();
			
			while(rs.next()){
				for(int i = 1; i <= colCount; i++){
					result += rs.getString(i) + " ";
				}
				result += "\n";
			}
		}
		catch(SQLException e){
			System.err.println("cannot execute the query");
			System.err.println("error: " + e.getMessage());
		}
		
		return result;
	}
}

























