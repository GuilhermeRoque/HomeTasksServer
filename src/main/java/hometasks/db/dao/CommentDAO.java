package hometasks.db.dao;

import hometasks.db.ConnectionFactory;
import hometasks.pojo.Comment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class CommentDAO {

	public int createComment(Comment comment) {
		String sql = "insert into Comentario (texto,midia,data,idTarefa,idUsuario) values (?,?,?,?,?)";
		int id = 0;
		try (Connection conn = ConnectionFactory.getDBConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql,RETURN_GENERATED_KEYS)) {

			stmt.setString(1, comment.getText());
			stmt.setBlob(2, comment.getMidia());
			stmt.setString(3, comment.getDate());
			stmt.setInt(4, comment.getIdTask());
			stmt.setString(5, comment.getIdUser());
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

	public int updateComment(Comment comment) {
		String sql = "update Comentario set texto = ?, midia = ?, data = ? where " +
				"idComentario = ?";
		int rows = 0;
		try (Connection conn = ConnectionFactory.getDBConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql,RETURN_GENERATED_KEYS)) {

			stmt.setString(1, comment.getText());
			stmt.setBlob(2, comment.getMidia());
			stmt.setString(3, comment.getDate());
			stmt.setInt(4, comment.getIdComment());
			rows = stmt.executeUpdate();
		} catch (SQLException ex) {
			System.err.println(ex.toString());
		}
		return rows;
	}

	public Comment getCommentUserTask(int idTarefa, String idUsuario) {
		String sql = "select * from Comentario where idUsuario = ? and idTarefa = ?";
		Comment comment = null;
		try (Connection conn = ConnectionFactory.getDBConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1,idUsuario);
			stmt.setInt(2,idTarefa);
			ResultSet rs = stmt.executeQuery();

			if(rs.next()){
				comment = new Comment(
						rs.getInt("idComentario"),
						rs.getString("texto"),
						rs.getBlob("midia"),
						rs.getString("data"),
						rs.getInt("idTarefa"),
						rs.getString("idUsuario"));
			}


		} catch (SQLException ex) {
			System.err.println(ex.toString());
		}
		return comment;
	}

	public Comment getComment(int idComentario) {
		String sql = "select * from Comentario where idComentario = ?";
		Comment comment = null;
		try (Connection conn = ConnectionFactory.getDBConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1,idComentario);
			ResultSet rs = stmt.executeQuery();

			if(rs.next()){
				comment = new Comment(
						rs.getInt("idComentario"),
						rs.getString("texto"),
						rs.getBlob("midia"),
						rs.getString("data"),
						rs.getInt("idTarefa"),
						rs.getString("idUsuario"));
			}


		} catch (SQLException ex) {
			System.err.println(ex.toString());
		}
		return comment;
	}

	public List<Comment> getCommentTask(int idTarefa){
		String sql = "select * from Comentario where idTarefa = ?";
		List<Comment> comments = new ArrayList<>();
		try (Connection conexao = ConnectionFactory.getDBConnection();
			 PreparedStatement stmt = conexao.prepareStatement(sql)) {

			stmt.setInt(1,idTarefa);
			ResultSet rs = stmt.executeQuery();

			while(rs.next()){
				comments.add(new Comment(
						rs.getInt("idComentario"),
						rs.getString("texto"),
						rs.getBlob("midia"),
						rs.getString("data"),
						rs.getInt("idTarefa"),
						rs.getString("idUsuario")));
			}


		} catch (SQLException ex) {
			System.err.println(ex.toString());
		}
		return comments;

	}
}
