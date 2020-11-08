package hometasks.db.dao;
import hometasks.db.ConnectionFactory;
import hometasks.pojo.Task;
import hometasks.pojo.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class TaskDAO {

	public int createTask(Task task) {
		String sql = "insert into Task (name,description,date,value,idReporter,idOwner,state,alternate,renew) " +
				"values (?,?,?,?,?,?,?);";
		int id = 0;
		try (Connection conn = ConnectionFactory.getDBConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql,RETURN_GENERATED_KEYS)) {

			stmt.setString(1, task.getName());
			stmt.setString(2, task.getDescription());
			stmt.setString(3, task.getDate());
			stmt.setFloat(4, task.getValue());
			stmt.setString(5, task.getIdReporter());
			stmt.setString(6, task.getIdOwner());
			stmt.setString(7, task.getState());
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

	public int updateTask(Task task) {
		String sql = "update Task set name = ?, description = ?, date = ?, valor = ?,idRelator = ?," +
				"idOwner = ?, state = ?, alternate = ?, renew = ? where idTask = ?";
		int rows = 0;
		try (Connection conn = ConnectionFactory.getDBConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, task.getName());
			stmt.setString(2, task.getDescription());
			stmt.setString(3, task.getDate());
			stmt.setFloat(4, task.getValue());
			stmt.setString(5, task.getIdReporter());
			stmt.setString(6, task.getIdOwner());
			stmt.setString(7, task.getState());
            stmt.setBoolean(8, task.isAlternate());
			stmt.setInt(9, task.getIdTask());
			rows = stmt.executeUpdate();

		} catch (SQLException ex) {
			System.err.println(ex.toString());
		}
		return rows;
	}

	public Task getTask(int idTask) {
		String sql = "select * from  Task where idTask = ?";
		Task task = null;
		try (Connection conn = ConnectionFactory.getDBConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {

				stmt.setInt(1, idTask);
				ResultSet rs = stmt.executeQuery();

				if(rs.next()){
					task = new Task(
							rs.getInt("idTask"),
							rs.getString("name"),
							rs.getString("description"),
							rs.getString("idOwner"),
							rs.getString("idReporter"),
							rs.getString("state"),
							rs.getString("date"),
							rs.getFloat("value"),
                            rs.getBoolean("alternate"),
							rs.getBoolean("renew"));
				}

				rs.close();

		} catch (SQLException ex) {
			System.err.println(ex.toString());
		}
		return task;
	}

	public int deleteTask(int idTask) {
		String sql = "delete from  Task where idTask = ?";
		int rows = 0;
		try (Connection conn = ConnectionFactory.getDBConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, idTask);
			rows = stmt.executeUpdate();

		} catch (SQLException ex) {
			System.err.println(ex.toString());
		}
		return rows;
	}

	public List<Task> getTaskUserState(String idUser, String state) {
        ArrayList<Task> task = new ArrayList<>();
		String sql = "select * from  Task where idOwner = ? and state = ?";

		try (Connection conn = ConnectionFactory.getDBConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, idUser);
			stmt.setString(2, state);
			ResultSet rs = stmt.executeQuery();

			while(rs.next()){
				task.add(new Task(
						rs.getInt("idTask"),
						rs.getString("name"),
						rs.getString("description"),
						rs.getString("idOwner"),
						rs.getString("idReporter"),
						rs.getString("state"),
						rs.getString("date"),
						rs.getFloat("value"),
                        rs.getBoolean("alternate"),
						rs.getBoolean("renew")
				));
			}

			rs.close();

		} catch (SQLException ex) {
			System.err.println(ex.toString());
		}
		return task;
	}

    public List<Task> getTasksUserState(User user, String state) {
        String sql = null;
        List<Task> tasks = new ArrayList<>();
        int op = 0;
        if (state.compareToIgnoreCase("done") == 0){
            sql = "select * from Task where idOwner in " +
                    "(Select idUser from User where idCasa = ?) and state = ?";
            op = 1;
        }
        else if (state.compareToIgnoreCase("open") == 0) {
            sql = "select * from  Task where idOwner = ? and state = ?";
            op = 2;
        }
        else{
            return tasks;
        }
        try (Connection conn = ConnectionFactory.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            if(op == 1) {
                stmt.setInt(1, user.getIdHome());
                stmt.setString(2, state);
            }
            else if(op == 2){
                stmt.setString(1, user.getIdUser());
                stmt.setString(2, state);
            }

            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                tasks.add(new Task(
                        rs.getInt("idTask"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("idOwner"),
                        rs.getString("idReporter"),
                        rs.getString("state"),
                        rs.getString("date"),
                        rs.getFloat("value"),
                        rs.getBoolean("alternate"),
						rs.getBoolean("alternate")));
            }

            rs.close();

        } catch (SQLException ex) {
            System.err.println(ex.toString());
        }
        return tasks;
    }
}
