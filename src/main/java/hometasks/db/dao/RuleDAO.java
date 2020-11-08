package hometasks.db.dao;

import hometasks.db.ConnectionFactory;
import hometasks.pojo.Rule;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class RuleDAO {

	public int createRoutine(Rule rule) {
		String sql = "insert into Regra (descricao,estado,valor,idUsuario,idCasa,data,nome) " +
				"values (?,?,?,?,?,?,?);";
		int id = 0;
		try (Connection conexao = ConnectionFactory.getDBConnection();
			 PreparedStatement stmt = conexao.prepareStatement(sql,RETURN_GENERATED_KEYS)) {

			stmt.setString(1, rule.getDescription());
			stmt.setBoolean(2, rule.isState());
			stmt.setInt(3, rule.getValue());
			stmt.setString(4, rule.getIdUser());
			stmt.setInt(5, rule.getIdHome());
			stmt.setString(6, rule.getDate());
			stmt.setString(7, rule.getName());
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

	public int updateRule(Rule rule) {
		String sql = "update Regra set descricao = ?, estado = ?," +
				"valor = ?, idUsuario = ?, data = ?, nome = ? where idRegra = ?";

		int rows = 0;
		try (Connection conexao = ConnectionFactory.getDBConnection();
			 PreparedStatement stmt = conexao.prepareStatement(sql,RETURN_GENERATED_KEYS)) {

			stmt.setString(1, rule.getDescription());
			stmt.setBoolean(2, rule.isState());
			stmt.setInt(3, rule.getValue());
			stmt.setString(4, rule.getIdUser());
			stmt.setString(5, rule.getDate());
			stmt.setString(6, rule.getName());
			stmt.setInt(7, rule.getIdRule());
			System.out.println("linhas " + rows);
			rows = stmt.executeUpdate();

		} catch (SQLException ex) {
			System.err.println(ex.toString());
		}
		return rows;
	}

	public Rule getRule(int idRegra) {
		String sql = "select * from  Regra where idRegra = ?";
		Rule rule = null;
		try (Connection conexao = ConnectionFactory.getDBConnection();
			 PreparedStatement stmt = conexao.prepareStatement(sql)) {

			stmt.setInt(1, idRegra);
			ResultSet rs = stmt.executeQuery();

			if(rs.next()){
				rule = new Rule(
						rs.getInt("idRegra"),
						rs.getString("nome"),
						rs.getString("descricao"),
						rs.getBoolean("estado"),
						rs.getString("idUsuario"),
						rs.getInt("idCasa"),
						rs.getString("data"),
						rs.getInt("valor"));
			}

			rs.close();

		} catch (SQLException ex) {
			System.err.println(ex.toString());
		}
		return rule;
	}

	public int deleteRule(int idRegra, int idCasa) {
		String sql = "delete from  Regra where idRegra = ? and idCasa = ?";
		int rows = 0;
		try (Connection conexao = ConnectionFactory.getDBConnection();
			 PreparedStatement stmt = conexao.prepareStatement(sql)) {

			stmt.setInt(1, idRegra);
			stmt.setInt(2, idCasa);
			rows = stmt.executeUpdate();

		} catch (SQLException ex) {
			System.err.println(ex.toString());
		}
		return rows;
	}

	public List<Rule> getRulesHome(int idCasa) {
		String sql = "select * from  Regra where idCasa = ?";
		List<Rule> rules = new ArrayList<>();
		try (Connection conexao = ConnectionFactory.getDBConnection();
			 PreparedStatement stmt = conexao.prepareStatement(sql)) {

			stmt.setInt(1, idCasa);
			ResultSet rs = stmt.executeQuery();

			while(rs.next()){
				rules.add(new Rule(
						rs.getInt("idRegra"),
						rs.getString("nome"),
						rs.getString("descricao"),
						rs.getBoolean("estado"),
						rs.getString("idUsuario"),
						rs.getInt("idCasa"),
						rs.getString("data"),
						rs.getInt("valor")));
			}

			rs.close();

		} catch (SQLException ex) {
			System.err.println(ex.toString());
		}
		return rules;
	}

}
