package com.banking.demo.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.demo.customRequest.CustomRequestForMoneyTransfer;
import com.banking.demo.customerResponse.CustomResponseForPassbook;
import com.banking.demo.customerResponse.CustomerResponseForNoUser;
import com.banking.demo.entities.AccountDetails;
import com.banking.demo.entities.CustomerEntity;
import com.banking.demo.entities.MoneyTransfer;
import com.banking.demo.service.AccountService;
import com.banking.demo.service.CustomerService;
import com.banking.demo.util.Validations;

@RestController
//@CrossOrigin
@RequestMapping("/customer/transaction")
public class TransactionController {
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	Validations validations;
	
	@Autowired
	CustomerService customerService;
	
	@PostMapping("/addBalance")
	public ResponseEntity<Object> addBalance(@RequestBody AccountDetails accountDetails) {
		
		validations.balanceValidation(accountDetails);
		
		AccountDetails fetchAccount = accountService.fetchAccountDetails(accountDetails.getAccountNumber());
		
		if(fetchAccount != null) {
			
			long accountBalance = fetchAccount.getAccountBalance()+ accountDetails.getAccountBalance();
			String updatedDate = ""+new Date();
			
			accountService.updateAccountBalance(accountBalance, updatedDate, accountDetails.getAccountNumber());
			
			CustomerResponseForNoUser loginResponse = new CustomerResponseForNoUser(new Date(), "Balance Updated Successfully", "200");
			return new ResponseEntity<Object>(loginResponse, HttpStatus.OK);
			
		}else {
			CustomerResponseForNoUser loginResponse = new CustomerResponseForNoUser(new Date(), "Invalid Account Number", "409");
			return new ResponseEntity<Object>(loginResponse, HttpStatus.BAD_REQUEST);
		}
	}
		
		@PostMapping("/transferMoney")
		public ResponseEntity<Object> transferMoney(@RequestBody CustomRequestForMoneyTransfer moneyTransfer) {
			
			validations.transferMoney(moneyTransfer);
			
			AccountDetails fetchAccount = accountService.fetchAccountDetails(moneyTransfer.getAccountNumber());
			AccountDetails fetchUserAccount = accountService.fetchAccountDetails(moneyTransfer.getUserAccountNumber());
			CustomerEntity fetchedAcc = customerService.findUserByAccount(moneyTransfer.getAccountNumber());
			
			if (fetchAccount != null && fetchUserAccount != null) {
				if (moneyTransfer.getAmount() >= 0 && moneyTransfer.getAmount() <= fetchAccount.getAccountBalance()) {
					if (fetchUserAccount.getBranchName().equals(moneyTransfer.getBranchName())) {
						if(fetchUserAccount.getIfsc().equals(moneyTransfer.getIfsc())) {
							if(fetchedAcc.getTransPin().equals(moneyTransfer.getTransactionPin())) {
								if (moneyTransfer.getAccountNumber().equals(moneyTransfer.getUserAccountNumber())) {
									CustomerResponseForNoUser loginResponse = new CustomerResponseForNoUser(new Date(), "Sender and Receiver account can't be same", "409");
									return new ResponseEntity<Object>(loginResponse, HttpStatus.BAD_REQUEST);
								} else {
									long senderBalance = fetchAccount.getAccountBalance() - moneyTransfer.getAmount();
									String updatedDate = ""+new Date();
									accountService.updateAccountBalance(senderBalance, updatedDate, fetchAccount.getAccountNumber());
									
									long ReceiverBalance = fetchUserAccount.getAccountBalance() + moneyTransfer.getAmount();
									accountService.updateAccountBalance(ReceiverBalance, updatedDate, fetchUserAccount.getAccountNumber());
									
									MoneyTransfer updateCrDbSender = new MoneyTransfer();
									updateCrDbSender.setAccountNumber(moneyTransfer.getAccountNumber());
									updateCrDbSender.setUserAccountNumber(moneyTransfer.getUserAccountNumber());
									updateCrDbSender.setAmount(moneyTransfer.getAmount());
									updateCrDbSender.setCreatedAt(""+new Date());
									updateCrDbSender.setUpdatedAt(""+new Date());
									updateCrDbSender.setTransactionStatus("Debit");
									
									accountService.updateCreditDebit(updateCrDbSender);
									
									MoneyTransfer updateCrDbReceiver = new MoneyTransfer();
									updateCrDbReceiver.setAccountNumber(moneyTransfer.getUserAccountNumber());
									updateCrDbReceiver.setUserAccountNumber(moneyTransfer.getAccountNumber());
									updateCrDbReceiver.setAmount(moneyTransfer.getAmount());
									updateCrDbReceiver.setCreatedAt(""+new Date());
									updateCrDbReceiver.setUpdatedAt(""+new Date());
									updateCrDbReceiver.setTransactionStatus("Credit");
									
									accountService.updateCreditDebit(updateCrDbReceiver);
									
									CustomerResponseForNoUser loginResponse = new CustomerResponseForNoUser(new Date(), "Money Send Successfully", "200");
									return new ResponseEntity<Object>(loginResponse, HttpStatus.OK);
								}
								
							}else {
								CustomerResponseForNoUser loginResponse = new CustomerResponseForNoUser(new Date(), "Invalid Transaction Pin", "409");
								return new ResponseEntity<Object>(loginResponse, HttpStatus.BAD_REQUEST);
							}
						}else {
							CustomerResponseForNoUser loginResponse = new CustomerResponseForNoUser(new Date(), "Invalid IFSC Code", "409");
							return new ResponseEntity<Object>(loginResponse, HttpStatus.BAD_REQUEST);
						}
					}else {
						CustomerResponseForNoUser loginResponse = new CustomerResponseForNoUser(new Date(), "Invalid Branch Name", "409");
						return new ResponseEntity<Object>(loginResponse, HttpStatus.BAD_REQUEST);
					}
				}else {
					CustomerResponseForNoUser loginResponse = new CustomerResponseForNoUser(new Date(), "Enter a valid amount or You don't have enough amount", "409");
					return new ResponseEntity<Object>(loginResponse, HttpStatus.BAD_REQUEST);
				}	
			}else {
				CustomerResponseForNoUser loginResponse = new CustomerResponseForNoUser(new Date(), "User Does Not Exist", "409");
				return new ResponseEntity<Object>(loginResponse, HttpStatus.BAD_REQUEST);
			}
			
		}
		
		@PostMapping("/viewPassbook")
		public ResponseEntity<Object> viewPassbook(@RequestBody AccountDetails accountDetails) {
			List<List<String>> data = new ArrayList<List<String>>();
			AccountDetails fetchAccount =  accountService.fetchAccountDetails(accountDetails.getAccountNumber());
			if (fetchAccount != null) {
				List<MoneyTransfer> fetchTransAcc = accountService.fetchTransAccount(accountDetails.getAccountNumber());
//				System.out.println(fetchTransAcc);
				
				for (int i=0;i<fetchTransAcc.size();i++)
				{
					List<String> tempdata = new ArrayList<String>();
					tempdata.add(fetchTransAcc.get(i).getAccountNumber());
					tempdata.add(fetchTransAcc.get(i).getUserAccountNumber());
					tempdata.add(fetchTransAcc.get(i).getAmount()+"");
					tempdata.add(fetchTransAcc.get(i).getCreatedAt());
					tempdata.add(fetchTransAcc.get(i).getTransactionStatus());
					data.add(tempdata);
				}
				CustomResponseForPassbook loginResponse = new CustomResponseForPassbook(data);
				
				return new ResponseEntity<Object>(loginResponse, HttpStatus.OK);
			} else {
				CustomerResponseForNoUser loginResponse = new CustomerResponseForNoUser(new Date(), "User Does Not Exist", "409");
				return new ResponseEntity<Object>(loginResponse, HttpStatus.BAD_REQUEST);
			}
			
		}
}
