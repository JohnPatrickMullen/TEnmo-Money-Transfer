package com.techelevator.tenmo.api.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.accounts.model.accounts;
import com.techelevator.tenmo.users.dao.usersDao;
import com.techelevator.tenmo.users.model.users;

@RestController
public class usersController {

	
private usersDao usersDao;

public usersController(usersDao usersDao) {
	this.usersDao = usersDao;
}

//Get all users
@RequestMapping(path= "/users", method= RequestMethod.GET)
public List<users> getAllUsers(){
	logAPICall("GET-/users");
	return usersDao.getAllUsers();
}

//Get users by Id
@RequestMapping(path= "/users/{id}", method= RequestMethod.GET)
public users getUsersById(@PathVariable int id) {
	logAPICall("GET-/users/id");
	return usersDao.getUsersById(id);	
}


public void logAPICall(String message) {
	LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss.A");
    String timeNow = now.format(formatter);
    System.out.println(timeNow + ": " + message);
}

}
