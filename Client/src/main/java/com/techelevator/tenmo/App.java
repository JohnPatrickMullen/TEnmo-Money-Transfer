package com.techelevator.tenmo;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.techelevator.accounts.model.accounts;
import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.UserCredentials;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.tenmo.users.model.users;
import com.techelevator.transfers.model.transfers;
import com.techelevator.view.ConsoleService;

public class App {

private static final String API_BASE_URL = "http://localhost:8080/";
    
    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	
    private AuthenticatedUser currentUser;
    private ConsoleService console;
    private AuthenticationService authenticationService;
    
    private RestTemplate apiCall = new RestTemplate();		//Created our RestTemplate
    private Scanner aScanner = new Scanner(System.in);		//Created a new scanner 

    public static void main(String[] args) {
    	App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL));
    	app.run();
    }

    public App(ConsoleService console, AuthenticationService authenticationService) {
		this.console = console;
		this.authenticationService = authenticationService;
	}

	public void run() {
		System.out.println("*********************");
		System.out.println("* Welcome to TEnmo! *");
		System.out.println("*********************");
		
		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() {
		while(true) {
			String choice = (String)console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if(MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if(MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
			} else if(MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				viewPendingRequests();
			} else if(MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				System.out.println("you picked send bucks option");
				sendBucks();
			} else if(MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
				requestBucks();
			} else if(MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else {
				// the only other option on the main menu is to exit
				exitProgram();
			}
		}
	}

	private void viewCurrentBalance() {
		//Create a new account using the currentUser's ID
		accounts newAccount = apiCall.getForObject(API_BASE_URL + "/accounts/" + currentUser.getUser().getId(), accounts.class);		
		//Printing out the balance of the newAccount we made aboev
		System.out.println("Your current account balance is:" + newAccount.getBalance());
		
		
	}

	private void viewTransferHistory() {
	//Created a response entity to make a List of transfers, using the currentUser's ID to get all associated transfers
	ResponseEntity<transfers[]> responseEntity = apiCall.getForEntity(API_BASE_URL + "transfers/" + currentUser.getUser().getId(),transfers[].class);
	
	//Assigned the body of the response entity to the List allTransfers
	List<transfers> allTransfers = Arrays.asList(responseEntity.getBody());
	
	
	System.out.println("TransferID ------- To/From ---------- Amount");
	
	//Looped through each transfer in allTransfers
	for(transfers transfer : allTransfers) {
		//Created a new transfer using the transfer idea for each transfer
		transfers aTransfer = apiCall.getForObject(API_BASE_URL + "/transfers/search?transfer_id=" + transfer.getTransferId() , transfers.class);
		
		//if the transfer accountTo id equals the userId
		if(transfer.getAccountTo() == currentUser.getUser().getId()) {
			// create a newUser using the accountFromId
			users accountFromUser = apiCall.getForObject(API_BASE_URL + "/users/" + aTransfer.getAccountFrom(), users.class);
			// print the transfer id												the accountFrom username								the transfer amount
			System.out.println(transfer.getTransferId() + "                   From: " + accountFromUser.getUsername() + "               $" + transfer.getAmount());
			
		} else {	// else (if we're the sender/accountFrom)
		//Create a new user object using the accountTo id
		users accountToUser = apiCall.getForObject(API_BASE_URL + "/users/" + aTransfer.getAccountTo(), users.class);	
		// print the transfer id												the accountTo username								the transfer amount
		System.out.println(transfer.getTransferId() + "                   To: " + accountToUser.getUsername() + "               $" + transfer.getAmount());
		
		}
	}
	// ask the user to choose a transferId to view details
	System.out.println("Please enter transfer ID to view details (0 to cancel):");
	// scan for the userInput
	String userInput = aScanner.nextLine();
	// parse the userInput String into an int so that it can be used to search for the specific transfer
	int userChoice = Integer.parseInt(userInput);
	
	System.out.println("Transfer Details");
	//invoke the transfer details method we wrote below
	getTransferDetails(userChoice);
	
	
		
	}
	// Created a new method to get Transfer details with a parameter of an int representing the transfer id
	private void getTransferDetails(int id) {
		// created a new transfer object using the id
		transfers aTransfer = apiCall.getForObject(API_BASE_URL + "/transfers/search?transfer_id=" + id , transfers.class);
		//created a new user object for the accountFromId so that we can access the specific usernames
		users accountFromUser = apiCall.getForObject(API_BASE_URL + "/users/" + aTransfer.getAccountFrom(), users.class);
		//created a new user object for the accountToId so that we can access the specific usernames
		users accountToUser = apiCall.getForObject(API_BASE_URL + "/users/" + aTransfer.getAccountTo(), users.class);
		
		//Print out the details
		System.out.println("Id: " + aTransfer.getTransferId());
		System.out.println("From: " + accountFromUser.getUsername());
		System.out.println("To: " + accountToUser.getUsername());
		
		// If statement that determines what transferType will be printed based on the transferTypeId
		// We concede that this is not loosely coupled
		
		if(aTransfer.getTransferTypeId() == 1) {
			System.out.println("Type: Request");
		} else {
			System.out.println("Type: Send");
		}
		
		// If statement that determines what transferStatus will be printed based on the transferStatusId
		
		if(aTransfer.getTransferStatusId() == 1) {
			System.out.println("Status: Pending");
		} else if (aTransfer.getTransferStatusId() == 2) {
			System.out.println("Status: Approved");
		} else {
			System.out.println("Status: Rejected");
		}
		
		System.out.println("Amount: $" + aTransfer.getAmount());
	}

	private void viewPendingRequests() {
		//Created a response entity to make a List of transfers, using the currentUser's ID to get all associated transfers
		ResponseEntity<transfers[]> responseEntity = apiCall.getForEntity(API_BASE_URL + "transfers/" + currentUser.getUser().getId(),transfers[].class);
		
		//Assigned the body of the response entity to the List allTransfers
		List<transfers> allTransfers = Arrays.asList(responseEntity.getBody());
		
		transfers aTransfer = new transfers();
		
		System.out.println("TransferID ------- To ---------- Amount");
		
		//Looped through each transfer in allTransfers
		for(transfers transfer : allTransfers) {
			//Created a new transfer using the transfer id for each transfer
			aTransfer = apiCall.getForObject(API_BASE_URL + "/transfers/search?transfer_id=" + transfer.getTransferId() , transfers.class);
			
			//if the transfer status id = 1 and we're not the accountTo, print out the pending transfer
			if(transfer.getTransferStatusId() == 1 && transfer.getAccountTo() != currentUser.getUser().getId()) {
				// create a newUser using the accountFromId
				users accountToUser = apiCall.getForObject(API_BASE_URL + "/users/" + aTransfer.getAccountTo(), users.class);
				// print the transfer id												the accountFrom username								the transfer amount
				System.out.println(transfer.getTransferId() + "                   " + accountToUser.getUsername() + "               $" + transfer.getAmount());
				
			} else {
			
				//What should I put here in the else statement if I don't need anything to happen???
				
			}
			
		}
		// ask the user to choose a transferId to view details
		System.out.println("Please enter transfer ID to approve/reject (0 to cancel):");
		// scan for the userInput
		String userInput = aScanner.nextLine();
		// parse the userInput String into an int so that it can be used to search for the specific transfer
		int userTransferChoice = Integer.parseInt(userInput);
		
		aTransfer = apiCall.getForObject(API_BASE_URL + "/transfers/search?transfer_id=" + userTransferChoice, transfers.class);
		
		if(aTransfer.getTransferStatusId() == 1) {
		System.out.println("You chose the following transfer ID:" + userTransferChoice);
		
		pendingTransferApprovalProcess(userTransferChoice);
		
		} else {
			System.out.println("Invalid choice");
		}
	}
	
	//Method to handle approving/rejecting a transfer
	private void pendingTransferApprovalProcess(int userTransferChoice) {
		
		// create a new transfer object using the userChoice
		transfers aTransfer = apiCall.getForObject(API_BASE_URL + "/transfers/search?transfer_id=" + userTransferChoice , transfers.class);
		
		// Show user options
		System.out.println("1: Approve");
		System.out.println("2: Reject");
		System.out.println("0: Don't approve or reject");
		System.out.println("-----------------------");
		System.out.println("Please chose an option:");
		
		// Use the scanner to get user input on what they want to do with the transfer
		String approveOrRejectInput = aScanner.nextLine();
		int approveOrRejectChoice = Integer.parseInt(approveOrRejectInput);
		
		// if the user chooses option 1, update the transfer to be approved, and make the appropriate changes to the balances
		if(approveOrRejectChoice == 1) {
			//set the transfer status id of aTransfer to 2 - approved
			aTransfer.setTransferStatusId(2);
			
			//creating a new account object for the fromAccount using the transfer's fromAccount
			accounts fromAccount = apiCall.getForObject(API_BASE_URL + "/accounts/" + aTransfer.getAccountFrom(), accounts.class);
		
			//creating a new account object for the toAccount using the transfer's toAccount
			accounts toAccount = apiCall.getForObject(API_BASE_URL + "/accounts/" + aTransfer.getAccountTo(), accounts.class);
			
			//update the to and from accounts balances to reflect the transfer being approved
			fromAccount.setBalance(fromAccount.getBalance() - aTransfer.getAmount());
			toAccount.setBalance(toAccount.getBalance() + aTransfer.getAmount());
			
			//send the updates for the two accounts and the transfer back to the server
			apiCall.put(API_BASE_URL + "/accounts", fromAccount);
			apiCall.put(API_BASE_URL + "/accounts", toAccount);
			apiCall.put(API_BASE_URL +"/transfers", aTransfer);
			
			System.out.println("Transfer approved!");
			
		} 
		// if the user chose option 2, update the transfer status id to be rejected
		else if(approveOrRejectChoice == 2) {
			aTransfer.setTransferStatusId(3);
			apiCall.put(API_BASE_URL +"/transfers", aTransfer);
			System.out.println("Transfer rejected");
		}
		else {
			System.out.println("The chosen transaction will remain pending");
		}
	}

	// Method to view a list of users that we can send money to (using id's)
	private void getListOfUsers() {
		// Call the APi to array of generic response - getForEntity instead of getForObject since we're getting an array of users
		ResponseEntity<users[]> responseEntity = apiCall.getForEntity(API_BASE_URL + "users",users[].class);
		
		// Take the response data and convert it to a List
		List<users> allUsers =  Arrays.asList(responseEntity.getBody());
		
		System.out.println("Number of users " + allUsers.size());
		
		// Loop through each object in the list and print out the userId and username
		for(users aUser: allUsers) {
			System.out.println(aUser.getUserId() + " " + aUser.getUsername());
		}
	}
	
	private void sendBucks() {
		//Create a new transfer object
		transfers aTransfer = new transfers();
		aTransfer.setTransferTypeId(2);
		aTransfer.setTransferStatusId(2);
		//accounts anAccount = new accounts();
		
		// invoke the getListOfUsers method
		getListOfUsers();
	 
		// Get input of the choice from the user
		System.out.println("Choose A User");
		String userInput = aScanner.nextLine();
		int userChoice = Integer.parseInt(userInput);
		
		// if statement to make sure that you aren't sending money to yourself
		if(userChoice != currentUser.getUser().getId()) {
		
			aTransfer.setAccountTo(userChoice);	//Set accountTo to the ID of user's choice
			aTransfer.setAccountFrom(currentUser.getUser().getId());	// set accountFrom to the currentUser's id
		
		// ask how much we want to send (checking that it's not more than we have in our account)
			System.out.println("Choose an amount");
			String userAmountInput = aScanner.nextLine();
			double userAmountToSend = Double.parseDouble(userAmountInput);
					
			aTransfer.setAmount(userAmountToSend);	// set the amount to the user's amount input
		
			//creating a new account object for the fromAccount using the currentUser's id
			accounts fromAccount = apiCall.getForObject(API_BASE_URL + "/accounts/" + currentUser.getUser().getId(), accounts.class);
		
			//creating a new account object for the toAccount using the userChoice
			accounts toAccount = apiCall.getForObject(API_BASE_URL + "/accounts/" + userChoice, accounts.class);
		
			// before processing the transfers check to to see if the fromAccount has enough money to complete the transfer
			if (userAmountToSend > fromAccount.getBalance()) {
					//If it doesn't sent the error message
					System.out.println("Not enough funds to complete transfer");
					
				} else {

		// decrease amount from sender account
				fromAccount.setBalance(fromAccount.getBalance() - userAmountToSend);
		
		// increase amount for user account
				toAccount.setBalance(toAccount.getBalance() + userAmountToSend);
		
		// Update accounts in server with the correct amounts
				apiCall.put(API_BASE_URL + "/accounts", fromAccount);
				apiCall.put(API_BASE_URL + "/accounts", toAccount);
		
		// Post the new transfer to the server
				apiCall.postForEntity(API_BASE_URL + "transfers", makeTransfersEntity(aTransfer), transfers.class);
		
				System.out.println("Transfer sent");
		
			}
		
		} else {
		// if the user has chosen themselves as who they want to send money to send this message and don't process the transfer
			System.out.println("You cannot transfer money to yourself");
				
		}
	
	}

	private void requestBucks() {
	//Create a new transfer object
		transfers aTransfer = new transfers();
		aTransfer.setTransferTypeId(1);
		aTransfer.setTransferStatusId(1);
				
	// invoke the getListOfUsers method
		getListOfUsers();
		
	// Get input of the choice from the user
		System.out.println("Choose A User");
		String userInput = aScanner.nextLine();
		int userChoice = Integer.parseInt(userInput);
			
	if(userChoice != currentUser.getUser().getId()) {	
		
		aTransfer.setAccountFrom(userChoice);	//Set accountFrom to the ID of user's choice
		aTransfer.setAccountTo(currentUser.getUser().getId());	// set accountTo to the currentUser's id
		
	// ask how much we want to send (checking that it's not more than we have in our account)
		System.out.println("Choose an amount");
		String userAmountInput = aScanner.nextLine();
		double userAmountToRequest = Double.parseDouble(userAmountInput);
							
		aTransfer.setAmount(userAmountToRequest);	// set the amount to the user's amount input
		
		// Post the new transfer to the server
		apiCall.postForEntity(API_BASE_URL + "transfers", makeTransfersEntity(aTransfer), transfers.class);

		System.out.println("Transfer request sent");
		
	} else {
		// if the user has chosen themselves as who they want to request money from send this message
			System.out.println("You cannot request money from yourself");
				
		}
	}
		

	
	private void exitProgram() {
		System.exit(0);
	}

	private void registerAndLogin() {
		while(!isAuthenticated()) {
			String choice = (String)console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}
	

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
            	authenticationService.register(credentials);
            	isRegistered = true;
            	System.out.println("Registration successful. You can now login.");
            } catch(AuthenticationServiceException e) {
            	System.out.println("REGISTRATION ERROR: "+e.getMessage());
				System.out.println("Please attempt to register again.");
            }
        }
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) //will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
		    try {
				currentUser = authenticationService.login(credentials);
			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: "+e.getMessage());
				System.out.println("Please attempt to login again.");
			}
		}
	}
	
	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}
	
	// Method to create a new HttpEntity so that we can send our new transfers back to the server
	private HttpEntity<transfers> makeTransfersEntity(transfers aTransfer) {
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    //headers.setBearerAuth(currentUser.getToken());
	    HttpEntity<transfers> entity = new HttpEntity<>(aTransfer, headers);
	    return entity;
	  }
}
