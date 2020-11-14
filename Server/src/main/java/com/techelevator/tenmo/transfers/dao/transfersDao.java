package com.techelevator.tenmo.transfers.dao;

import java.util.List;

import com.techelevator.tenmo.transfers.model.transfers;

public interface transfersDao {


	List<transfers> getAllTransfers(int userId);

	transfers addTransfer(transfers aTransfer);

	transfers getTransferByTransferId(int transferId);

	void updateAccount(transfers aTransfer);
}
