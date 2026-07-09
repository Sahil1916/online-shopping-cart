package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import config.DBconnection;
import enums.UserRole;
import model.User;

public class UserDAO {

	public boolean register(User user) {

		String sql = "INSERT INTO USERS (name,email,password,role)VALUES(?,?,?,?)";

		try (Connection con = DBconnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql);) {

			ps.setString(1, user.getName());
			ps.setString(2, user.getEmail());
			ps.setString(3, user.getPassword());
			ps.setString(4, user.getRole().name());
			

			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			throw new RuntimeException(e);
		}

	}

	public User login(String email) {

		return findByEmail(email);

	}

	public User findByEmail(String email) {
		 System.out.println("Searching Email = " + email);

		String sql = "SELECT * FROM users WHERE email = ?";

		try (Connection con = DBconnection.getConnection();

				PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, email);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {

				User user = new User();

				user.setId(rs.getLong("id"));
				user.setName(rs.getString("name"));
				user.setEmail(rs.getString("email"));
				user.setPassword(rs.getString("password"));
				user.setRole(UserRole.valueOf(rs.getString("role")));
				user.setCreatedAt(rs.getTimestamp("created_at"));
				
				 System.out.println("USER FOUND");

				return user;
			}

		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			throw new RuntimeException(e);
		}

		 System.out.println("USER NOT FOUND");
		return null;

	}

	
	public boolean existsByEmail(String email) {

		return findByEmail(email) != null;

	}

	public java.util.List<User> findAll() {

		java.util.List<User> users = new java.util.ArrayList<>();
		String sql = "SELECT * FROM users";

		try (Connection con = DBconnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				User user = new User();
				user.setId(rs.getLong("id"));
				user.setName(rs.getString("name"));
				user.setEmail(rs.getString("email"));
				user.setRole(UserRole.valueOf(rs.getString("role")));
				user.setCreatedAt(rs.getTimestamp("created_at"));
				users.add(user);
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return users;
	}

	public boolean updateStatus(Long id, String status) {

		String sql = "UPDATE users SET status = ? WHERE id = ?";

		try (Connection con = DBconnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, status);
			ps.setLong(2, id);

			return ps.executeUpdate() > 0;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
