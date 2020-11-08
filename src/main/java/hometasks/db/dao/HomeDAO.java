package hometasks.db.dao;

import hometasks.db.ConnectionFactory;
import hometasks.pojo.Home;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class HomeDAO {

	public int createHome(Home home) {
		String sql = "insert into Casa (nome,descricao,aluguel,endereco,foto) values (?,?,?,?,?);";
		int id = 0;
		// Try-with-resources irá fechar automaticamente a conexão
		try (Connection conexao = ConnectionFactory.getDBConnection();
			 PreparedStatement stmt = conexao.prepareStatement(sql,RETURN_GENERATED_KEYS)) {

			stmt.setString(1, home.getName());
			stmt.setString(2, home.getDescription());
			stmt.setInt(3, home.getRent());
			stmt.setString(4, home.getAddress());
			stmt.setBlob(5, home.getPicture());
			stmt.execute();

			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()){
				id = rs.getInt(1);
			}
			rs.close();

		} catch (SQLException ex) {
			System.err.println(ex.toString());
		}
		return id;
	}

	public int updateHome(Home home) {
		String sql = "update Casa set nome = ?, descricao = ?, aluguel = ?, endereco = ?, foto = ?" +
				" where idCasa = ?";
		int rows = 0;
		try (Connection conexao = ConnectionFactory.getDBConnection();
			 PreparedStatement stmt = conexao.prepareStatement(sql,RETURN_GENERATED_KEYS)) {

			stmt.setString(1, home.getName());
			stmt.setString(2, home.getDescription());
			stmt.setInt(3, home.getRent());
			stmt.setString(4, home.getAddress());
			stmt.setBlob(5, home.getPicture());
			stmt.setInt(6, home.getIdHome());

			rows = stmt.executeUpdate();
		} catch (SQLException ex) {
			System.err.println(ex.toString());
		}
		return rows;
	}

	public Home getHome(int id) {
		String sql = "SELECT * from Casa where idCasa = "+id;

		Home home = null;
		try (Connection conexao = ConnectionFactory.getDBConnection();
			 PreparedStatement stmt = conexao.prepareStatement(sql);
			 ResultSet rs = stmt.executeQuery()) {
			if (rs.next()) {
				home = new Home(
						rs.getInt("idCasa"),
						rs.getString("nome"),
						rs.getString("endereco"),
						rs.getInt("aluguel"),
						rs.getString("descricao"),
						rs.getBlob("foto"));
			}
		} catch (SQLException ex) {
			System.err.println(ex.toString());
		}

		return home;
	}

	public int deleteHome(int id) {
		String sql = "DELETE from Casa where idCasa = ?";
		int rows = 0;
		try (Connection conexao = ConnectionFactory.getDBConnection();
			 PreparedStatement stmt = conexao.prepareStatement(sql)) {

			stmt.setInt(1, id);
			rows = stmt.executeUpdate();

		} catch (SQLException ex) {
			System.err.println(ex.toString());
		}

		return rows;
	}

	/*Retorna Lista de Casas com o nome parecido com o parâmetro 'nome' de entrada*/
	public List<Home> buscaCasasNome(String nome) {
		String sql = "SELECT * from Casa where nome like '%"+nome+"%' limit 100";
		List<Home> homes = new ArrayList<>();
		try (Connection conexao = ConnectionFactory.getDBConnection();
			 PreparedStatement stmt = conexao.prepareStatement(sql);
			 ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				homes.add(new Home(
						rs.getInt("idCasa"),
						rs.getString("nome"),
						rs.getString("endereco"),
						rs.getInt("aluguel"),
						rs.getString("descricao"),
						rs.getBlob("foto")));
			}
		} catch (SQLException ex) {
			System.err.println(ex.toString());
		}
		return homes;
	}

	/*Retorna Lista de Casas com o endereço parecido com o parâmetro 'endereco' de entrada*/
	public List<Home> buscaCasasEndereco(String endereco) {
		String sql = "SELECT * from Casa where endereco like '%"+endereco+"%' limit 100";
		List<Home> homes = new ArrayList<>();
		try (Connection conexao = ConnectionFactory.getDBConnection();
			 PreparedStatement stmt = conexao.prepareStatement(sql);
			 ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				homes.add(new Home(
						rs.getInt("idCasa"),
						rs.getString("nome"),
						rs.getString("endereco"),
						rs.getInt("aluguel"),
						rs.getString("descricao"),
						rs.getBlob("foto")));
			}
		} catch (SQLException ex) {
			System.err.println(ex.toString());
		}
		return homes;
	}

}
