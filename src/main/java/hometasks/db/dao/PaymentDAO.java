package hometasks.db.dao;

import hometasks.db.ConnectionFactory;
import hometasks.pojo.Payment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class PaymentDAO {

	public int createPayment(Payment payment) {
		String sql = "insert into Pagamento (idCredor,idDevedor,valor,data,juros,descricao,pago) values (?,?,?,?,?,?,?);";
		int id = 0;
		try (Connection conexao = ConnectionFactory.getDBConnection();
			 PreparedStatement stmt = conexao.prepareStatement(sql,RETURN_GENERATED_KEYS)) {

			stmt.setString(1, payment.getIdCreditor());
			stmt.setString(2, payment.getIdDebtor());
			stmt.setFloat(3, payment.getValue());
			stmt.setString(4, payment.getDate());
			stmt.setInt(5, payment.getInterest());
			stmt.setString(6, payment.getDescription());
			stmt.setBoolean(7,false);

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

	public int updatePayment(Payment payment) {
		String sql = "update Pagamento set valor = ?," +
				"data = ?,juros = ?, descricao = ?, pago = ? where idPagamento = ? and idCredor = ? and idDevedor = ?;";
		int rows = 0;
		try (Connection conexao = ConnectionFactory.getDBConnection();
			 PreparedStatement stmt = conexao.prepareStatement(sql)) {

			stmt.setFloat(1, payment.getValue());
			stmt.setString(2, payment.getDate());
			stmt.setInt(3, payment.getInterest());
			stmt.setString(4, payment.getDescription());
			stmt.setBoolean(5, payment.isDone());
			stmt.setInt(6, payment.getIdPayment());
			stmt.setString(7, payment.getIdCreditor());
			stmt.setString(8, payment.getIdDebtor());
			rows = stmt.executeUpdate();

		} catch (SQLException ex) {
			System.err.println(ex.toString());
		}
		return rows;
	}

	public List<Payment> getPaymentUser(String idUsuario) {

		List<Payment> payments = new ArrayList<>();
		String sql = "SELECT * from Pagamento where (idCredor = ? or idDevedor = ?) and (pago is false or pago is NULL) ";

		Payment payment = null;
		try (Connection conexao = ConnectionFactory.getDBConnection();
			 PreparedStatement stmt = conexao.prepareStatement(sql)) {

			stmt.setString(1,idUsuario);
			stmt.setString(2,idUsuario);
			ResultSet rs = stmt.executeQuery();

			while(rs.next()){
				payments.add(new Payment(
						rs.getInt("idPagamento"),
						rs.getString("idDevedor"),
						rs.getString("idCredor"),
						Integer.parseInt(rs.getString("juros")),
						rs.getFloat("valor"),
						rs.getString("data"),
						rs.getString("descricao"),
						rs.getBoolean("pago")));
			}

			rs.close();
		} catch (SQLException ex) {
			System.err.println(ex.toString());
		}
		return payments;
	}

	public int deletePayment(Payment payment) {
		String sql = "DELETE from Pagamento where idPagamento = ? and idCredor = ? and idDevedor = ?";
		int rows = 0;
		try (Connection conexao = ConnectionFactory.getDBConnection();
			 PreparedStatement stmt = conexao.prepareStatement(sql)) {

			stmt.setInt(1, payment.getIdPayment());
			stmt.setString(2, payment.getIdCreditor());
			stmt.setString(3, payment.getIdDebtor());
			rows = stmt.executeUpdate();

		} catch (SQLException ex) {
			System.err.println(ex.toString());
		}
		return rows;
	}

	public Payment getPayment(int idPagamento) {


		String sql = "SELECT * from Pagamento where idPagamento = ?";
		Payment payment = null;

		try (Connection conexao = ConnectionFactory.getDBConnection();
			 PreparedStatement stmt = conexao.prepareStatement(sql)) {

			stmt.setInt(1,idPagamento);
			ResultSet rs = stmt.executeQuery();

			if(rs.next()){
				payment = new Payment(
						rs.getInt("idPagamento"),
						rs.getString("idDevedor"),
						rs.getString("idCredor"),
						Integer.parseInt(rs.getString("juros")),
						rs.getFloat("valor"),
						rs.getString("data"),
						rs.getString("descricao"),
						rs.getBoolean("pago"));
			}

			rs.close();
		} catch (SQLException ex) {
			System.err.println(ex.toString());
		}
		return payment;
	}

}
