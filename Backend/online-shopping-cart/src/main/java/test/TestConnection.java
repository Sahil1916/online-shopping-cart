package test;

import util.PasswordUtil;

public class TestConnection {

    public static void main(String[] args) {

        String password = "admin123"; // हवा तो password

        String hash = PasswordUtil.hashPassword(password);

        System.out.println(hash);
    }
}