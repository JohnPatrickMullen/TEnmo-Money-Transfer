# TEnmo-Money-Transfer
Second Capstone Project for Tech Elevator - created a command line money transfer system using the Spring Boot Java framework incorporating SQL, user authentication and Web API server

Required Use Cases
You should attempt to complete all of the following required use cases.
1. [COMPLETED FOR YOU] As a user of the system, I need to be able to register myself with a username
and password.
1. A new registered user starts with an initial balance of 1,000 TE Bucks.
2. The ability to register has been provided in your starter code.

2. [COMPLETED FOR YOU] As a user of the system, I need to be able to log in using my registered
username and password.
1. Logging in returns an Authentication Token. I need to include this token with all my subsequent
interactions with the system outside of registering and logging in.
2. The ability to log in has been provided in your starter code.

3. As an authenticated user of the system, I need to be able to see my Account Balance.

4. As an authenticated user of the system, I need to be able to send a transfer of a specific amount of TE
Bucks to a registered user.
  1. I should be able to choose from a list of users to send TE Bucks to.
  2. A transfer includes the User IDs of the from and to users and the amount of TE Bucks.
  3. The receiver's account balance is increased by the amount of the transfer.
  4. The sender's account balance is decreased by the amount of the transfer.
  5. I can't send more TE Bucks than I have in my account.
  6. A Sending Transfer has an initial status of "approve."
  
5. As an authenticated user of the system, I need to be able to see transfers I have sent or received.

6. As an authenticated user of the system, I need to be able to retrieve the details of any transfer based
upon the transfer ID.

Optional Use Cases (Completed)
If you complete all of the required use cases and are looking for additional challenge, complete as many of
the following optional use cases as you can.

7. As an authenticated user of the system, I need to be able to request a transfer of a specific amount of
TE Bucks from another registered user.
  1. I should be able to choose from a list of users to request TE Bucks from.
  2. A transfer includes the User IDs of the from and to users and the amount of TE Bucks.
  3. A Request Transfer has an initial status of "pending."
  4. No account balance changes until the request is approved.
  5. The transfer request should appear in both users' list of transfers (use case #5).
  
8. As an authenticated user of the system, I need to be able to see my "pending" transfers.

9. As an authenticated user of the system, I need to be able to either approve or reject a Request Transfer.
  1. I can't "approve" a given Request Transfer for more TE Bucks than I have in my account.
  2. The Request Transfer status is "approved" if I approve, or "rejected" if I reject the request.
  3. If the transfer is approved, the requester's account balance is increased by the amount of the request.
  4. If the transfer is approved, the requestee's account balance is decreased by the amount of the request.
  5. If the transfer is rejected, no account balance changes.
