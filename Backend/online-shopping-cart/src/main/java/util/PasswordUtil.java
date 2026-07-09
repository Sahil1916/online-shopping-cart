package util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {
	//private constructure 
	private PasswordUtil(){
		
	}
	
	// for password encription  for calling without object  means we dont need to create a object of that methods
	
	
	public static String hashPassword(String password) {
		
		return BCrypt.hashpw(password, BCrypt.gensalt());
		
		//hi navin  random salt tayar karate using .gensalt().
		//passwrod + hash asa use karun hashcode tayar karate password cha 
	}
	

	//for verify the password
	
	public static boolean checkPassword(String password , String hashpassword) {
		
		return BCrypt.checkpw(password, hashpassword);
		// verify the password and return true or falses
		//password takes user gives  password
		//hashpassword takes database hashcode 
		// this method comapre both and return boolean value 
	}

	
}
