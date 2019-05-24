package completeProject;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class Client {
	private String firstName;
	private String lastName;
	private String accountNum;
	private int balance;
	private int amount;
	
	
	public Client() {
		
	}
	
	public Client(String accountNum, int amount){
		this.accountNum = accountNum;
		this.amount = amount;
	}
	
	public Client(String accountNum, String firstName, String lastName,  int balance ) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.accountNum = accountNum;
		this.balance = balance;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAccountNum() {
		return accountNum;
	}

	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
}
