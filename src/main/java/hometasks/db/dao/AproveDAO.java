package hometasks.db.dao;

import hometasks.db.ConnectionFactory;
import hometasks.pojo.Aprove;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class AproveDAO {

    public int createAprove(Aprove aprove){
        String sql = "insert into Aprova (idUsuario,idRegra,estado) values (?,?,?)";
        int id = 0;
        try (Connection conexao = ConnectionFactory.getDBConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql,RETURN_GENERATED_KEYS)) {

            stmt.setString(1, aprove.getIdUser());
            stmt.setInt(2, aprove.getIdRule());
            stmt.setBoolean(3, aprove.isState());
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

    public int updateAprove(Aprove aprove){
        String sql = "update Aprova set estado = ? where idUsuario = ? and idRegra = ?";
        int rows = 0;
        try (Connection conexao = ConnectionFactory.getDBConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setBoolean(1, aprove.isState());
            stmt.setString(2, aprove.getIdUser());
            stmt.setInt(3, aprove.getIdRule());
            rows = stmt.executeUpdate();



        } catch (SQLException ex) {
            System.err.println(ex.toString());
        }
        return rows;
    }

    public Aprove getAprove(String idUsuario, int idRegra){
        String sql = "select * from Aprova where idUsuario = ? and idRegra = ?";
        Aprove aprove = null;
        try (Connection conexao = ConnectionFactory.getDBConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1,idUsuario);
            stmt.setInt(2,idRegra);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                aprove = new Aprove(
                        rs.getString("idUsuario"),
                        rs.getInt("idRegra"),
                        rs.getBoolean("estado"));
            }


        } catch (SQLException ex) {
            System.err.println(ex.toString());
        }
        return aprove;
    }

    public List<Aprove> getAproveRule(int idRegra){
        String sql = "select * from Aprova where idRegra = ?";
        List<Aprove> aproves = new ArrayList<>();
        try (Connection conexao = ConnectionFactory.getDBConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1,idRegra);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()){
                aproves.add(new Aprove(
                        rs.getString("idUsuario"),
                        rs.getInt("idRegra"),
                        rs.getBoolean("estado")));
            }


        } catch (SQLException ex) {
            System.err.println(ex.toString());
        }
        return aproves;
    }
}
