package completeProject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public class AppDbHandler {
	private static AppDbHandler instance;
	private DataSource dataSource;
	private String jndiName = "java:comp/env/jdbc/clients_db";
	
	public static AppDbHandler getInstance() throws Exception {
		if (instance == null) {
			instance =new AppDbHandler();
		}
			return instance;
	}
	
	private AppDbHandler() throws Exception {		
		dataSource = getDataSource();
	}

	private DataSource getDataSource() throws NamingException {
		Context context = new InitialContext();
		
		DataSource theDataSource = (DataSource) context.lookup(jndiName);
		
		return theDataSource;
	}
	
	public List<Client> getClients() throws Exception {

		List<Client> clients = new ArrayList<>();

		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = getConnection();

			String sql = "select BALANCE from clients where ACCOUNT_NUM=?";

			myStmt = myConn.createStatement();

			myRs = myStmt.executeQuery(sql);

			while (myRs.next()) {
				
				//Retrieve data from result set row
				String accountNum = myRs.getString("ACCOUNT_NUM");
				String firstName = myRs.getString("FIRST_NAME");
				String lastName = myRs.getString("LAST_NAME");
				int balance = myRs.getInt("BALANCE");

				// create new client object
				Client tempClient = new Client(accountNum, firstName, lastName, balance);

				//Add each to the list of clients
				clients.add(tempClient);
			}
			
			return clients;		
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}
	
	public Client getClient(String accountNumm) throws Exception {
		
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = getConnection();

			String sql = "select BALANCE from student where ACCOUNT_NUM=?";

			myStmt = myConn.prepareStatement(sql);
			
			//Set parameters
			myStmt.setString(1, accountNumm);
			
			myRs = myStmt.executeQuery();

			Client theClient = null;
			
			//Retrieving data from result set row
			if (myRs.next()) {
				String accountNum = myRs.getString("ACCOUNT_NUM");
				String firstName = myRs.getString("FIRST_NAME");
				String lastName = myRs.getString("LAST_NAME");
				int balance = myRs.getInt("BALANCE");
			}
			else {
				throw new Exception("Could not find the account number: "
			                      + accountNumm +", Please Check the Correct One!");
			}

			return theClient;
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}
	
	//Add Client QUERY
	public void addClient(Client theClient) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "insert into clients (ACCOUNT_NUM, FIRST_NAME, LAST_NAME, BALANCE) "
					+ "values (?, ?, ?, ?)";

			myStmt = myConn.prepareStatement(sql);

			//Setting parameters
			myStmt.setString(1, theClient.getAccountNum());
			myStmt.setString(2, theClient.getFirstName());
			myStmt.setString(3, theClient.getLastName());
			myStmt.setInt(4, theClient.getBalance());
			
			myStmt.execute();			
		}
		finally {
			close (myConn, myStmt);
		}
		
	}
	
	//Check Balance QUERY
	public int checkBalance(String accountNumm) throws Exception {
			
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		
		
		try {
			myConn = getConnection();

			String sql = "select BALANCE from clients where ACCOUNT_NUM=?";
			myStmt = myConn.prepareStatement(sql);
			//Setting parameters
			myStmt.setString(1, accountNumm);
			myRs = myStmt.executeQuery();
			
			int newBalance = 0;
			//Retrieving data from result set row
			if (myRs.next()) {
					newBalance = myRs.getInt("BALANCE");
				}
			else {
					throw new Exception("Could not find the account number: "
				                      + accountNumm +", Please Check the Correct One!");
				}
			return newBalance;
			}
		
			finally {
				close (myConn, myStmt, myRs);
			}
		}
		
	
	//Deposit QUERY
	public void depositMoney(Client theClient) throws Exception {
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;

		try {
			myConn = getConnection();
			int amount = theClient.getAmount();

			String sql = "select BALANCE from clients where ACCOUNT_NUM=?";

			myStmt = myConn.prepareStatement(sql);

			myStmt.setString(1, theClient.getAccountNum());
			myRs = myStmt.executeQuery();
			if (myRs.next()) {
				
				int balance = myRs.getInt("BALANCE");
				int newBalance = balance + amount;

				String sql2 = "update clients "
							+ " set BALANCE=?"
							+ " where ACCOUNT_NUM=?";

				myStmt = myConn.prepareStatement(sql2);

				myStmt.setInt(1, newBalance);
				myStmt.setString(2, theClient.getAccountNum());
				
				myStmt.execute();

			}
			else {
				throw new Exception("Could not find the account number: "
			                      + theClient.getAccountNum() +", Please Check the Correct One!");
			}
				
		}
		finally {
			close (myConn, myStmt);
		}
	}
	
	//Withdraw QUERY
	public void withdrawMoney(Client theClient) throws Exception {
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		int newBalance;
		try {
			myConn = getConnection();
			int amount = theClient.getAmount();

			String sql = "select BALANCE from clients where ACCOUNT_NUM=?";

			myStmt = myConn.prepareStatement(sql);

			myStmt.setString(1, theClient.getAccountNum());
			myRs = myStmt.executeQuery();
			if (myRs.next()) {
				
				int balance = myRs.getInt("BALANCE");
				newBalance = balance - amount;
				if(newBalance > 0) {
					String sql2 = "update clients "
							+ " set BALANCE=?"
							+ " where ACCOUNT_NUM=?";

				myStmt = myConn.prepareStatement(sql2);

				myStmt.setInt(1, newBalance);
				myStmt.setString(2, theClient.getAccountNum());
				
				myStmt.execute();
				}
				else {
					throw new Exception("Insufficient Balance to complete the process: "
		                      + theClient.getAmount() +", Please Check your balance!");
				}
			}
			else {
				throw new Exception("Could not find the account number: "
			                      + theClient.getAccountNum() +", Please Check the Correct One!");
			}
			
		}
		finally {
			close (myConn, myStmt);
		}
		
	}
	
	//Calculate interest amount QUERY
		public double calcInterest(String accountNumm) throws Exception {
				
			Connection myConn = null;
			PreparedStatement myStmt = null;
			ResultSet myRs = null;
					
			try {
				myConn = getConnection();

				String sql = "select BALANCE from clients where ACCOUNT_NUM=?";
				myStmt = myConn.prepareStatement(sql);
				//Setting parameters
				myStmt.setString(1, accountNumm);
				myRs = myStmt.executeQuery();
				
				double interestRate = 0.5;
				double interestAmount = 0;
				//Retrieving data from result set row
				if (myRs.next()) {
						int balance = myRs.getInt("BALANCE");
						interestAmount = balance*(interestRate/100);
					}
				else {
						throw new Exception("Could not find the account number: "
					                      + accountNumm +", Please Check the Correct One!");
					}
				return interestAmount;
				}
			
				finally {
					close (myConn, myStmt, myRs);
				}
			}
			
	
	private Connection getConnection() throws Exception {

		Connection theConn = dataSource.getConnection();
		
		return theConn;
	}
	
	private void close(Connection theConn, Statement theStmt) {
		close(theConn, theStmt, null);
	}
	
	private void close(Connection theConn, Statement theStmt, ResultSet theRs) {

		try {
			if (theRs != null) {
				theRs.close();
			}

			if (theStmt != null) {
				theStmt.close();
			}

			if (theConn != null) {
				theConn.close();
			}
			
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}	

}
