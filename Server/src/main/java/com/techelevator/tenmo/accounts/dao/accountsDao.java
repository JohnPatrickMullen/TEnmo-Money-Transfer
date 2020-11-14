package com.techelevator.tenmo.accounts.dao;

import java.util.List;

import com.techelevator.tenmo.accounts.model.accounts;

public interface accountsDao {
		
	public List<accounts> getAllAccounts();

	public accounts viewAccountByUserId(int userId);
	
	void updateAccount(accounts anAccount);

}
