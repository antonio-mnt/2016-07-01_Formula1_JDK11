package it.polito.tdp.formulaone.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.formulaone.model.Arco;
import it.polito.tdp.formulaone.model.Circuit;
import it.polito.tdp.formulaone.model.Constructor;
import it.polito.tdp.formulaone.model.Driver;
import it.polito.tdp.formulaone.model.Season;


public class FormulaOneDAO {

	public List<Integer> getAllYearsOfRace() {
		
		String sql = "SELECT year FROM races ORDER BY year" ;
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet rs = st.executeQuery() ;
			
			List<Integer> list = new ArrayList<>() ;
			while(rs.next()) {
				list.add(rs.getInt("year"));
			}
			
			conn.close();
			return list ;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}
	
	public List<Season> getAllSeasons() {
		
		String sql = "SELECT year, url FROM seasons ORDER BY year" ;
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet rs = st.executeQuery() ;
			
			List<Season> list = new ArrayList<>() ;
			while(rs.next()) {
				list.add(new Season(Year.of(rs.getInt("year")), rs.getString("url"))) ;
			}
			
			conn.close();
			return list ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Circuit> getAllCircuits() {

		String sql = "SELECT circuitId, name FROM circuits ORDER BY name";

		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			List<Circuit> list = new ArrayList<>();
			while (rs.next()) {
				list.add(new Circuit(rs.getInt("circuitId"), rs.getString("name")));
			}

			conn.close();
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}
	
	public List<Constructor> getAllConstructors() {

		String sql = "SELECT constructorId, name FROM constructors ORDER BY name";

		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			List<Constructor> constructors = new ArrayList<>();
			while (rs.next()) {
				constructors.add(new Constructor(rs.getInt("constructorId"), rs.getString("name")));
			}

			conn.close();
			return constructors;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}
	
	
	
	public List<Integer> getSeasons() {
		
		String sql = "SELECT distinct year " + 
				"FROM seasons " ;
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet rs = st.executeQuery() ;
			
			List<Integer> list = new ArrayList<>() ;
			while(rs.next()) {
				list.add(rs.getInt("year")) ;
				
			}
			
			conn.close();
			return list ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	
	public List<Driver> getAllDriver(int anno) {

		String sql = "SELECT distinct(d.driverId), d.driverRef, d.number, d.code, d.forename, d.surname, d.dob, d.nationality, d.url " + 
				"FROM races AS ra, results AS re, drivers AS d " + 
				"WHERE ra.year = ? AND ra.raceId = re.raceId AND d.driverId = re.driverId AND re.position <> \"null\" ";

		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);

			ResultSet rs = st.executeQuery();

			List<Driver> result = new ArrayList<>();
			while (rs.next()) {
				result.add(new Driver(rs.getInt("d.driverId"), rs.getString("d.driverRef"), rs.getInt("d.number"),rs.getString("d.code"),
						rs.getString("d.forename"), rs.getString("d.surname"), rs.getDate("d.dob").toLocalDate(), rs.getString("d.nationality"), rs.getString("d.url")));
			}

			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}
	
	
	
	public List<Arco> getAllArchi(int anno, Map<Integer,Driver> idMap) {

		String sql = "SELECT re.driverId, re2.driverId, COUNT(*) AS peso " + 
				"FROM races AS ra, results AS re, races AS ra2, results AS re2 " + 
				"WHERE ra.year = ? AND ra.raceId = re.raceId AND re.position <> \"null\" " + 
				"AND ra2.year = ? AND ra2.raceId = re2.raceId AND re2.position <> \"null\" " + 
				"AND re.position = 1 AND re2.position <> 1 AND re.raceId = re2.raceId " + 
				"GROUP BY re.driverId, re2.driverId ";

		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			st.setInt(2, anno);

			ResultSet rs = st.executeQuery();

			List<Arco> result = new ArrayList<>();
			while (rs.next()) {
				Driver d1 = idMap.get(rs.getInt("re.driverId"));
				Driver d2 = idMap.get(rs.getInt("re2.driverId"));
				Double peso = (double) rs.getInt("peso");
				
				if(d1!=null && d2!=null) {
					result.add(new Arco(d1,d2,peso));
				}
				
			}

			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}


	
}

