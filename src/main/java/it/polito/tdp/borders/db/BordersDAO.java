package it.polito.tdp.borders.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.borders.model.Border;
import it.polito.tdp.borders.model.Country;

public class BordersDAO {

	public void /*List<Country>*/ loadAllCountries(Map<Integer, Country> map) {

		String sql = "SELECT ccode, StateAbb, StateNme FROM country ORDER BY StateAbb";
		//List<Country> result = new ArrayList<Country>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				if(!map.containsKey(rs.getInt("ccode"))) {
				//System.out.format("%d %s %s\n", rs.getInt("ccode"), rs.getString("StateAbb"), rs.getString("StateNme"));
			Country country = new Country(rs.getInt("ccode"), rs.getString("StateAbb"), rs.getString("StateNme"));
			map.put(country.getCcode(), country);
				
				}
			}
			
			conn.close();
			//return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	public List<Country> getVertici(int x, Map<Integer,Country> map){
		String sql = "SELECT c.CCode, c.StateNme, c.StateAbb "
		+"FROM country c, contiguity con "
		+"WHERE con.state1no = c.CCode AND con.year <= ? AND con.conttype = 1 "
		+"GROUP BY c.StateNme";
		
List<Country> result = new ArrayList<Country>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1,x);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				//vado a chiedere all'idMap di darmi l'oggetto con questo id
				result.add(map.get(rs.getInt("ccode")));
				//sono sicura che ci sia perchè l'idMap contiene tutti i paesi
				
				}
			
			rs.close();
			st.close();
			conn.close();
			return result;

	} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
		
	}
	
	public List<Border> getBorders(int anno, Map<Integer,Country> map){
		String sql = "SELECT c.state1no AS id1, c.state1ab AS ab1, c.state2no AS id2, c.state2ab AS ab2 "
				+"FROM contiguity c "
				+"WHERE YEAR <= ? AND c.conttype = 1" ;
		List<Border> result = new LinkedList<Border>();
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1,anno);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				
				Country country1 = map.get(rs.getInt("id1"));
				Country country2 = map.get(rs.getInt("id2"));
				if(country1 != null && country2 != null) {
				result.add(new Border(country1, country2));
				}
				
				}
			
			rs.close();
			st.close();
			conn.close();
			return result;

	} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
		
	}

	public List<Border> getCountryPairs(int anno) {

		System.out.println("TODO -- BordersDAO -- getCountryPairs(int anno)");
		return new ArrayList<Border>();
	}
}
