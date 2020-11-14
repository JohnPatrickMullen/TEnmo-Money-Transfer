package com.techelevator.tenmo.api.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.accounts.dao.accountsDao;
import com.techelevator.tenmo.accounts.model.accounts;



@RestController
public class accountsController {
	
private accountsDao accountsDao;

public accountsController(accountsDao accountsDao) {
	this.accountsDao = accountsDao;
}


//Will show all accounts 

@RequestMapping(path = "/accounts", method = RequestMethod.GET)
public List<accounts> getAllAccounts(){
	logAPICall("GET-/accounts");
	return accountsDao.getAllAccounts();
}


//Well get accounts by account id

@RequestMapping(path = "/accounts/{id}", method = RequestMethod.GET)
public accounts getAccountById(@PathVariable int id) {
	logAPICall("GET-/accounts/" + id);
	return accountsDao.viewAccountByUserId(id);
}


//Will update account by userId after a transfer has been made

@RequestMapping(path="/accounts", method= RequestMethod.PUT)
public void updateAccount(@RequestBody accounts anAccount) {
	logAPICall("PUT-/accounts for " + anAccount.getUserId() + " new balance " + anAccount.getBalance());
	accountsDao.updateAccount(anAccount);
}


public void logAPICall(String message) {
	LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss.A");
    String timeNow = now.format(formatter);
    System.out.println(timeNow + ": " + message);
}

}
