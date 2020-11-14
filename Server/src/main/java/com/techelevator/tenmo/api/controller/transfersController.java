package com.techelevator.tenmo.api.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.transfers.dao.transfersDao;
import com.techelevator.tenmo.transfers.model.transfers;

@RestController
public class transfersController {

	private transfersDao transfersDao;

	public transfersController(transfersDao transfersDao) {
		this.transfersDao = transfersDao;
	}

	
	@RequestMapping(path = "/transfers/{userId}", method = RequestMethod.GET)
	public List<transfers> getAllTransfers(@PathVariable int userId) {
		logAPICall("GET-/transfers/" + userId);
		return transfersDao.getAllTransfers(userId);
	}
	
	
	@RequestMapping(path = "/transfers", method = RequestMethod.POST)
	public transfers addTransfer(@RequestBody transfers aTransfer) {
		logAPICall("POST- /transfers " + aTransfer);
		return transfersDao.addTransfer(aTransfer);
	}

	  
	@RequestMapping(path = "/transfers/search", method = RequestMethod.GET)
	public transfers getTransferByTransferId(@RequestParam(value="transfer_id", defaultValue = "0") int transferId) {
	logAPICall("Get-/transfers/search?transfer_id= " + transferId); 
	return transfersDao.getTransferByTransferId(transferId);
	}
	
	//Will update account by userId after a transfer has been made

	@RequestMapping(path="/transfers", method= RequestMethod.PUT)
	public void updateAccount(@RequestBody transfers aTransfer) {
		logAPICall("PUT-/transfers for " + aTransfer.getTransferId() + " new transfer status id: " + aTransfer.getTransferStatusId());
		transfersDao.updateAccount(aTransfer);
	}
	 

	public void logAPICall(String message) {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss.A");
		String timeNow = now.format(formatter);
		System.out.println(timeNow + ": " + message);
	}

}
