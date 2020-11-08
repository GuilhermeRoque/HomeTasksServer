package hometasks.db.dao;
import hometasks.db.ConnectionFactory;
import hometasks.pojo.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class UserDAO {

	public boolean createUser(User user) {
		String sql = "insert into User (idUser,date,gender,points,profile,name,telephone,password,email)" +
				" values (?,?,?,?,?,?,?,?,?)";

		try (Connection conn = ConnectionFactory.getDBConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql,RETURN_GENERATED_KEYS)) {

			stmt.setString(1, user.getIdUser());
			stmt.setString(2, user.getDate());
			stmt.setString(3, user.getGender());
			stmt.setInt(4, user.getPoints());
			stmt.setString(5, user.getProfile());
			stmt.setString(6, user.getName());
			stmt.setString(7, user.getTelephone());
			stmt.setString(8, user.getPassword());
			stmt.setString(9, user.getEmail());

			stmt.execute();
		} catch (SQLException ex) {
			System.err.println(ex.toString());
			return false;
		}
		return true;
	}


	public int updateUser(User user) {
		String sql;
		if (user.getIdHome() >0){
			sql = "update User set date = ?, gender = ?, points = ?, profile = ?, name = ?," +
					"telephone = ?, password = ?, email = ?, picture = ?, idHome = ? where idUser = ?";
		}else {
			sql = "update User set date = ?, gender = ?, points = ?, profile = ?, name = ?," +
					"telephone = ?, password = ?, email = ?, picture = ? where idUser = ?";
		}
		int rows = 0;
		try (Connection conn = ConnectionFactory.getDBConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, user.getDate());
			stmt.setString(2, user.getGender());
			stmt.setInt(3, user.getPoints());
			stmt.setString(4, user.getProfile());
			stmt.setString(5, user.getName());
			stmt.setString(6, user.getTelephone());
			stmt.setString(7, user.getPassword());
			stmt.setString(8, user.getEmail());
			stmt.setBlob(9, user.getPicture());

			if(user.getIdHome() >0){
				stmt.setInt(10, user.getIdHome());
				stmt.setString(11, user.getIdUser());

			}else {
				stmt.setString(10, user.getIdUser());
			}

			rows = stmt.executeUpdate();

		} catch (SQLException ex) {
			System.err.println(ex.toString());
			return -1;
		}
		return rows;
	}

	public User getUser(String idUser) {
		String sql = "SELECT * from User where idUser = ?";

		User user = null;
		try (Connection conexao = ConnectionFactory.getDBConnection()) {

			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setString(1,idUser);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				user = new User(
						rs.getString("idUser"),
						rs.getString("name"),
						rs.getString("date"),
						rs.getString("gender"),
						rs.getInt("points"),
						rs.getString("telephone"),
						rs.getString("password"),
						rs.getString("email"),
						rs.getString("profile"),
						rs.getInt("idHome"),
						rs.getBlob("picture"));
			}

			stmt.close();
			rs.close();
		} catch (SQLException ex) {
			System.err.println(ex.toString());
		}

		return user;
	}

	public int deleteUser(String idUser) {
		String sql = "delete from User where idUser = ?";
		int rows = 0;
		try (Connection conn = ConnectionFactory.getDBConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, idUser);
			rows = stmt.executeUpdate();

		} catch (SQLException ex) {
			System.err.println(ex.toString());
		}
		return rows;
	}

	/*Retorna uma lista com Users com o name parecido com o fornecido*/
	public List<User> getUserNameLike(String name) {
		String sql = "select * from User where name like '%"+name+"%' limit 100";
		ArrayList<User> users = new ArrayList<>();
		try (Connection conexao = ConnectionFactory.getDBConnection();
			 PreparedStatement stmt = conexao.prepareStatement(sql);
			 ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				users.add(new User(
						rs.getString("idUser"),
						rs.getString("name"),
						rs.getString("date"),
						rs.getString("gender"),
						rs.getInt("points"),
						rs.getString("telephone"),
						rs.getString("password"),
						rs.getString("email"),
						rs.getString("profile"),
						rs.getInt("idHome"),
						rs.getBlob("picture")));

			}
		} catch (SQLException ex) {
			System.err.println(ex.toString());
		}
		return users;
	}

	public List<User> getUsersLikeID(String idUser) {
		String sql = "select * from User where idUser like '%"+idUser+"%' limit 100";
		ArrayList<User> users = new ArrayList<>();
		try (Connection conexao = ConnectionFactory.getDBConnection();
			 PreparedStatement stmt = conexao.prepareStatement(sql);
			 ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				users.add(new User(
						rs.getString("idUser"),
						rs.getString("name"),
						rs.getString("date"),
						rs.getString("gender"),
						rs.getInt("points"),
						rs.getString("telephone"),
						rs.getString("password"),
						rs.getString("email"),
						rs.getString("profile"),
						rs.getInt("idHome"),
						rs.getBlob("picture")
				));
			}

		} catch (SQLException ex) {
			System.err.println(ex.toString());
		}
		return users;
	}

	public User getUserToken(String token) {
		String sql = "SELECT * from User where token = ?";

		User user = null;
		try (Connection conn = ConnectionFactory.getDBConnection()) {

			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1,token);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				user = new User(
						rs.getString("idUser"),
						rs.getString("name"),
						rs.getString("date"),
						rs.getString("gender"),
						rs.getInt("points"),
						rs.getString("telephone"),
						rs.getString("password"),
						rs.getString("email"),
						rs.getString("profile"),
						rs.getInt("idHome"),
						rs.getBlob("picture"));
			}

			stmt.close();
			rs.close();
		} catch (SQLException ex) {
			System.err.println(ex.toString());
		}

		return user;
	}

	public List<User> getUsersHome(int idHome) {
		String sql = "select * from User where idHome = "+idHome;
		ArrayList<User> users = new ArrayList<>();
		try (Connection conexao = ConnectionFactory.getDBConnection();
			 PreparedStatement stmt = conexao.prepareStatement(sql);
			 ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				users.add(new User(
						rs.getString("idUser"),
						rs.getString("name"),
						rs.getString("date"),
						rs.getString("gender"),
						rs.getInt("points"),
						rs.getString("telephone"),
						rs.getString("password"),
						rs.getString("email"),
						rs.getString("profile"),
						rs.getInt("idHome"),
						rs.getBlob("picture")));
			}

		} catch (SQLException ex) {
			System.err.println(ex.toString());
		}
		return users;
	}

}
