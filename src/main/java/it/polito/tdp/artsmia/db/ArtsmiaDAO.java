package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.artsmia.model.Arco;
import it.polito.tdp.artsmia.model.ArtObject;

public class ArtsmiaDAO {

	public List<ArtObject> listObjects() {
		
		String sql = "SELECT * from objects";
		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
						res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
						res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
						res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
				
				result.add(artObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	
	public List<Arco> listArchi(Map<Integer, ArtObject> idMap) {
		
		String sql = "SELECT e1.object_id, e2.object_id, COUNT(DISTINCT e1.exhibition_id) AS peso " + 
				"FROM exhibition_objects AS e1, exhibition_objects AS e2 " + 
				"WHERE e1.exhibition_id = e2.exhibition_id AND e1.object_id > e2.object_id " + 
				"GROUP BY e1.object_id, e2.object_id ";
		
		List<Arco> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				ArtObject a1 = idMap.get(res.getInt("e1.object_id"));
				ArtObject a2 = idMap.get(res.getInt("e2.object_id"));
				int peso = res.getInt("peso");
				
				
				if(a1!=null && a2!= null) {
					Arco arco = new Arco(a1,a2,peso);
					result.add(arco);
				}
				

			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
