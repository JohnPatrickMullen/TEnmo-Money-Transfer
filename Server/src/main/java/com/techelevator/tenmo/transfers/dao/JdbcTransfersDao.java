package com.techelevator.tenmo.transfers.dao;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.transfers.model.transfers;


@Component
public class JdbcTransfersDao implements transfersDao {

	private JdbcTemplate jdbcTemplate;
	
	public JdbcTransfersDao(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	
	//Create new transfer and add to database
	@Override
	public transfers addTransfer(transfers aTransfer) {
		String sqlAddTransfer = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES (?, ?, ?, ?, ?)";
		
		jdbcTemplate.update(sqlAddTransfer, aTransfer.getTransferTypeId(), aTransfer.getTransferStatusId(), aTransfer.getAccountFrom(), aTransfer.getAccountTo(), aTransfer.getAmount());

		return aTransfer;
	}
	
	//List all transfers
	@Override
	public List<transfers> getAllTransfers(int userId){
		List<transfers> allTransfers = new ArrayList<>();
		
		String sqlGetAllTransfers = "SELECT u.username, t.* FROM transfers t \r\n" + 
				"JOIN accounts a ON t.account_from = a.account_id OR t.account_to = a.account_id\r\n" + 
				"JOIN users u ON a.user_id = u.user_id\r\n" + 
				"WHERE u.user_id = ?;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllTransfers, userId);
		
		while(results.next()) {
			transfers transfersResult = mapRowToTransfers(results);
			allTransfers.add(transfersResult);
		}
		return allTransfers;
	}
	
	//Get transfer by transfer_id
	@Override 
	public transfers getTransferByTransferId(int transferId) {
		transfers aTransfer = new transfers();
	  
		String sqlGetTransferByTransferId = "SELECT * FROM transfers WHERE transfer_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetTransferByTransferId, transferId);
	  
		while(results.next()) {
			aTransfer = mapRowToTransfers(results);
		}
		return aTransfer; 	
	}
	
	//Updating account balance based on user_id
	@Override
	public void updateAccount(transfers aTransfer) {
		  String sqlUpdateAccountBalance = "UPDATE transfers SET transfer_status_id = ? WHERE transfer_id = ?";
		  jdbcTemplate.update(sqlUpdateAccountBalance, aTransfer.getTransferStatusId(), aTransfer.getTransferId());
		 }
	
	//Create a transfer and add all information from a SqlRowSet
	private transfers mapRowToTransfers(SqlRowSet results) {
		transfers aTransfer = new transfers();
		aTransfer.setTransferId(results.getInt("transfer_id"));
		aTransfer.setTransferTypeId(results.getInt("transfer_type_id"));
		aTransfer.setTransferStatusId(results.getInt("transfer_status_id"));
		aTransfer.setAccountFrom(results.getInt("account_from"));
		aTransfer.setAccountTo(results.getInt("account_to"));
		aTransfer.setAmount(results.getDouble("amount"));
		return aTransfer;
	}
	
}
