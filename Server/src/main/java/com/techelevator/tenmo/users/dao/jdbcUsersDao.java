package com.techelevator.tenmo.users.dao;


import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.users.model.users;

@Component
public class jdbcUsersDao implements usersDao{
	
private JdbcTemplate jdbcTemplate;

public jdbcUsersDao(DataSource dataSource) {
	this.jdbcTemplate = new JdbcTemplate(dataSource);
}


//Get all users
@Override
public List<users> getAllUsers() {
	List<users> allUsers = new ArrayList<>();
	String sqlGetAllusers = "SELECT  user_id, username FROM users";

	SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllusers);
	
	while (results.next()) {
		users userResult = mapRowToUsers(results);
		allUsers.add(userResult);
	}
	return allUsers;
}


//Get users by Id
@Override
public users getUsersById(int userId) {
	String sqlGetUser = "SELECT user_id, username FROM users WHERE user_id = ?";
	SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetUser, userId);
	if (results.next()) {
		return mapRowToUsers(results);
	} else {
		return null;
	}
}


private users mapRowToUsers(SqlRowSet results) {
	users aUser = new users();
	aUser.setUserId(results.getInt("user_id"));
	aUser.setUsername(results.getString("username"));
	return aUser;
}

}
