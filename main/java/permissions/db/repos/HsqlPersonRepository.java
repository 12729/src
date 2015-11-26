package permissions.db.repos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import permissions.domain.Person;

public class HsqlPersonRepository implements PersonRepository{

	private Connection connection;
	
	private String insertSql ="INSERT INTO person(name,surname) VALUES(?,?)"; 
	private String selectSql ="SELECT * FROM person LIMIT(?,?)";
	private String selectByIdSql ="SELECT * FROM person WHERE id=?";
	private String selectBySurnameSql ="SELECT * FROM person WHERE Surname=? LIMIT(?,?)";
	private String selectByNameSql ="SELECT * FROM person WHERE Name=? LImit(?,?)";
	private String deleteSql = "DELETE FROM person WHERE id=?";
	private String updateSql = "UPDATE person SET (name,surname)=(?,?) WHERE id=?";
	
	private PreparedStatement insert;
	private PreparedStatement select;
	private PreparedStatement selectById;
	private PreparedStatement selectBySurname;
	private PreparedStatement selectByName;
	private PreparedStatement delete;
	private PreparedStatement update;
	

	private String createPersonTable =""
			+ "CREATE TABLE Person("
			+ "id bigint GENERATED BY DEFAULT AS IDENTITY,"
			+ "name VARCHAR(20),"
			+ "surname VARCHAR(50)"
			+ ")";
	
	
	public HsqlPersonRepository(Connection connection){
		this.connection=connection;
		
		try{
			

			insert = connection.prepareStatement(insertSql);
			select = connection.prepareStatement(selectSql);
			selectById = connection.prepareStatement(selectByIdSql);
			selectByName = connection.prepareStatement(selectByNameSql);
			selectBySurname = connection.prepareStatement(selectBySurnameSql);
			delete = connection.prepareStatement(deleteSql);
			update = connection.prepareStatement(updateSql);
			
			
			ResultSet rs = connection.getMetaData().getTables(null, null, null, null);
			
			boolean tableExists =false;
			while(rs.next())
			{
				if(rs.getString("TABLE_NAME").equalsIgnoreCase("Person")){
					tableExists=true;
					break;
				}
			}
			if(!tableExists){
				Statement createTable = connection.createStatement();
				createTable.executeUpdate(createPersonTable);
			}
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		
	}
	
	
	public Person withId(int id) {
		Person result = null;
		try {
			selectById.setInt(1, id);
			ResultSet rs = selectById.executeQuery();
			while(rs.next()){
				Person person = new Person();
				person.setName(rs.getString("name"));
				person.setSurname(rs.getString("surname"));
				person.setId(rs.getInt("id"));
				result = person;
				break;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public List<Person> allOnPage(PagingInfo page) {
List<Person> result = new ArrayList<Person>();
		
		try {
			select.setInt(1, page.getCurrentPage()*page.getSize());
			select.setInt(2, page.getSize());
			ResultSet rs = select.executeQuery();
			while(rs.next()){
				Person person = new Person();
				person.setName(rs.getString("name"));
				person.setSurname(rs.getString("surname"));
				person.setId(rs.getInt("id"));
				result.add(person);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public void add(Person person) {
		try {
			insert.setString(1, person.getName());
			insert.setString(2, person.getSurname());
			
			insert.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public void modify(Person p) {
		try {
			update.setString(1, p.getName());
			update.setString(2, p.getSurname());
			update.setInt(3, p.getId());
			update.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public void remove(Person p) {
		try {
			delete.setInt(1, p.getId());
			delete.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public List<Person> withSurname(String surname, PagingInfo page) {
		
		List<Person> result = new ArrayList<Person>();
		try {
			selectBySurname.setString(1, surname);
			selectBySurname.setInt(2, page.getCurrentPage()*page.getSize());
			selectBySurname.setInt(3, page.getSize());
			ResultSet rs = selectBySurname.executeQuery();
			while(rs.next()){
				Person person = new Person();
				person.setName(rs.getString("name"));
				person.setSurname(rs.getString("surname"));
				person.setId(rs.getInt("id"));
				result.add(person);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public List<Person> withName(String name, PagingInfo page) {

		List<Person> result = new ArrayList<Person>();
		try {
			selectByName.setString(1, name);
			selectByName.setInt(2, page.getCurrentPage()*page.getSize());
			selectByName.setInt(3, page.getSize());
			ResultSet rs = selectByName.executeQuery();
			while(rs.next()){
				Person person = new Person();
				person.setName(rs.getString("name"));
				person.setSurname(rs.getString("surname"));
				person.setId(rs.getInt("id"));
				result.add(person);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

}
