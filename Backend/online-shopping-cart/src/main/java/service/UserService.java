package service;

import dao.UserDAO;
import model.User;
import util.PasswordUtil;

public class UserService {

	private final UserDAO userdao = new UserDAO();

	// for user registration
	public boolean register(User user) {

		// for checking user is alredy exust or not
		if (userdao.existsByEmail(user.getEmail())) {
			return false;
		}

		// for password encryption
		String hashedPassword = PasswordUtil.hashPassword(user.getPassword());
		user.setPassword(hashedPassword);

		// Public registration must never trust a client-supplied role
		// (prevents self-registering as ADMIN, and avoids a crash if role is omitted)
		user.setRole(enums.UserRole.CUSTOMER);

		// return the user for register

		return userdao.register(user);

	}

	public User login(String email, String password) {

	    User user = userdao.login(email);

	    System.out.println("Email entered : " + email);

	    if (user == null) {
	        System.out.println("User not found");
	        return null;
	    }

	    System.out.println("Hash from DB : " + user.getPassword());

	    boolean isValid = PasswordUtil.checkPassword(password, user.getPassword());

	    System.out.println("Password entered : " + password);
	    System.out.println("Password Valid : " + isValid);

	    if (!isValid)
	        return null;

	    user.setPassword(null);

	    return user;
	}

	public java.util.List<User> listAllUsers() {
		return userdao.findAll();
	}

	public boolean updateUserStatus(Long id, String status) {
		return userdao.updateStatus(id, status);
	}

	public User getUserById(Long id) {
	    return userdao.findById(id);
	}
}
