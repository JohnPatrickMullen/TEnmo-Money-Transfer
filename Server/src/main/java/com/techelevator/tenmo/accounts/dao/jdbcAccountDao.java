package com.techelevator.tenmo.accounts.dao;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.accounts.model.accounts;
import com.techelevator.tenmo.accounts.dao.accountsDao;

@Component
public class jdbcAccountDao implements accountsDao {

	private JdbcTemplate jdbcTemplate;
	
	public jdbcAccountDao(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	
	//Made a List to get all accounts
	@Override
	public List<accounts> getAllAccounts(){
		List<accounts> allAccounts = new ArrayList<>();
		String sqlGetAllAccounts = "SELECT * FROM accounts";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllAccounts);
		
		while(results.next()) {
			accounts accountsResult = mapRowToAccount(results);
			allAccounts.add(accountsResult);
		}
		return allAccounts;
	}
	
	
	
	@Override
	public accounts viewAccountByUserId(int userId) {
		accounts userAccount = null;
		
		String query = "SELECT * FROM accounts WHERE user_id = ?";			//SQL query to get balance of a user by id
		SqlRowSet results = jdbcTemplate.queryForRowSet(query, userId);		//SqlRowSet 
		
		if(results.next()) {		// if there was a row returned from the query, position at the row
		
			userAccount = mapRowToAccount(results);		//Map the values of results onto userAccount
		
		}
		return userAccount;			//return the balance of userAccount
	}
	
	
	 //Updating account balance based on user_id
	public void updateAccount(accounts anAccount) {
	  String sqlUpdateAccountBalance = "UPDATE accounts SET balance=? WHERE user_id=?";
	  jdbcTemplate.update(sqlUpdateAccountBalance, anAccount.getBalance(),anAccount.getUserId());
	 }
	 
	
	//Create a new account
	private accounts mapRowToAccount(SqlRowSet results) {
		accounts account = new accounts();
		account.setAccountId(results.getInt("account_id"));
		account.setUserId(results.getInt("user_id"));
		account.setBalance(results.getDouble("balance"));
		return account;
	}
	
}
