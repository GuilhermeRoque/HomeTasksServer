package hometasks.db.dao;

import hometasks.db.ConnectionFactory;
import hometasks.pojo.Invitation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class InvitationDAO {

	public int createInvitation(Invitation invitation) {
		String sql = "insert into Convite (idConvidado,idConvidante,sentido,estado,idCasa) values (?,?,?,?,?)";
		int id = 0;
		try (Connection conexao = ConnectionFactory.getDBConnection();
			 PreparedStatement stmt = conexao.prepareStatement(sql,RETURN_GENERATED_KEYS)) {

			stmt.setString(1, invitation.getIdGuest());
			stmt.setString(2, invitation.getIdInviting());
			stmt.setBoolean(3, invitation.isInvitingIsInitiator());
			stmt.setBoolean(4, invitation.isState());
			stmt.setInt(5, invitation.getIdHome());
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

	public int updateInvitation(Invitation invitation) {
		String sql = "update Convite set sentido = ?, estado = ? where idConvidante = ? " +
				"and idConvidado = ? and idCasa = ?";
		int rows = 0;
		try (Connection conexao = ConnectionFactory.getDBConnection();
			 PreparedStatement stmt = conexao.prepareStatement(sql,RETURN_GENERATED_KEYS)) {

			stmt.setBoolean(1, invitation.isInvitingIsInitiator());
			stmt.setBoolean(2, invitation.isState());
			stmt.setString(3, invitation.getIdInviting());
			stmt.setString(4, invitation.getIdGuest());
			stmt.setInt(5, invitation.getIdHome());
			stmt.executeUpdate();


			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()){
				rows = rs.getInt(1);
			}
			rs.close();

		} catch (SQLException ex) {
			System.err.println(ex.toString());
		}
		return rows;
	}

	public Invitation getInvitation(String idConvidado, String idConvidante, int idCasa) {
		String sql = "select * from Convite where idConvidante = ? " +
				"and idConvidado = ? and idCasa = ?";

		Invitation invitation = null;
		try (Connection conexao = ConnectionFactory.getDBConnection();
			 PreparedStatement stmt = conexao.prepareStatement(sql)) {

			stmt.setString(1, idConvidante);
			stmt.setString(2, idConvidado);
			stmt.setInt(3, idCasa);
			ResultSet rs = stmt.executeQuery();

			if(rs.next()){
				invitation = new Invitation(
						rs.getString("idConvidado"),
						rs.getString("idConvidante"),
						rs.getInt("idCasa"),
						rs.getBoolean("sentido"),
						rs.getBoolean("estado"));
			}

		} catch (SQLException ex) {
			System.err.println(ex.toString());
		}
		return invitation;
	}

	public int deleteInvitation(String idConvidado, String idConvidante, int idCasa) {
		String sql = "delete from Convite where idConvidante = ? " +
				"and idConvidado = ? and idCasa = ?";

		int rows = 0;
		try (Connection conexao = ConnectionFactory.getDBConnection();
			 PreparedStatement stmt = conexao.prepareStatement(sql)) {

			stmt.setString(1, idConvidante);
			stmt.setString(2, idConvidado);
			stmt.setInt(3, idCasa);
			rows = stmt.executeUpdate();

		} catch (SQLException ex) {
			System.err.println(ex.toString());
		}
		return rows;
	}

	public List<Invitation> getInvitationUser(String idUsuario, boolean estado){
		List<Invitation> invitations = new ArrayList<>();
		String sql = "select * from Convite where (idConvidado = ? or idConvidante = ?) and estado = ?";

		try (Connection conexao = ConnectionFactory.getDBConnection();
			 PreparedStatement stmt = conexao.prepareStatement(sql)) {

			stmt.setString(1, idUsuario);
			stmt.setString(2, idUsuario);
			stmt.setBoolean(3, estado);
			ResultSet rs = stmt.executeQuery();

			while(rs.next()){
				invitations.add(new Invitation(
						rs.getString("idConvidado"),
						rs.getString("idConvidante"),
						rs.getInt("idCasa"),
						rs.getBoolean("sentido"),
						rs.getBoolean("estado")));
			}

		} catch (SQLException ex) {
			System.err.println(ex.toString());
		}

		return invitations;
	}
}
