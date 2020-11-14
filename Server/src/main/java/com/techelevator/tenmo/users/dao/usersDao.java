package com.techelevator.tenmo.users.dao;

import java.util.List;

import com.techelevator.tenmo.users.model.users;

public interface usersDao {

	List<users> getAllUsers();
	
	users getUsersById(int userId);

}
