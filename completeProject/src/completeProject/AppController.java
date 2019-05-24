package completeProject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;



@ManagedBean
@SessionScoped
public class AppController {
	private List<Client> clients;
	private AppDbHandler appDbHandler;
	private Logger logger = Logger.getLogger(getClass().getName());
	
	public AppController() throws Exception {
		clients = new ArrayList<>();
		
		appDbHandler = AppDbHandler.getInstance();
	}
	
	public List<Client> getClients() {
		return clients;
	}
	
	
	//Add New Client
	public String addClient(Client theClient) {

		logger.info("Adding client: " + theClient);

		try {
			
			//Add client to the database
			appDbHandler.addClient(theClient);
			logger.info("Your data has been added successfully.. " +"\nYour Name is: " +
			             theClient.getFirstName()+" "+ theClient.getLastName()+
			             "\nYour Account Number is: " + theClient.getAccountNum()+
			             "\nYour Initial balance ia: " + theClient.getBalance());
			
		} catch (Exception exc) {
			//Send this to server logs
			logger.log(Level.SEVERE, "Error adding clients", exc);
			
			//ASdd error message for JSF page
			addErrorMessage(exc);

			return null;
		}
		
		return "main_form?faces-redirect=true";
	}
	
	//Check Balance
	public String checkBalance(String accountNum) {
		logger.info("Checking Balance of account: " + accountNum);

		try {
			
			int theBalance = appDbHandler.checkBalance(accountNum);
			logger.info("Your current balance is: " + theBalance + " EL.");
			
		} catch (Exception exc) {
			logger.log(Level.SEVERE, "Error finding account number", exc);
			
			addErrorMessage(exc);

			return null;
		}
		
		return "main_form?faces-redirect=true";
	}
	
	//Deposit Money
	public String depositMoney(Client theClient) {
		logger.info("Adding Money: " + theClient);

		try {
			
			appDbHandler.depositMoney(theClient);
			logger.info("You have deposited: " + theClient.getAmount() +" EL"+ " Successfully..");
			
		} catch (Exception exc) {
			logger.log(Level.SEVERE, "Error adding money", exc);
			
			addErrorMessage(exc);

			return null;
		}
		
		return "main_form?faces-redirect=true";
	}
	
	//Withdraw Money
	public String withdrawMoney(Client theClient) {
		logger.info("Withrawing Money: " + theClient);
				
		try {
			
			//Add client to the database
			appDbHandler.withdrawMoney(theClient);
			logger.info("You have withdrawn: " + theClient.getAmount() +" EL"+ " Successfully..");
			
		} catch (Exception exc) {
			//Send this to server logs
			logger.log(Level.SEVERE, "Error withdrawing money", exc);
			
			//Add error message for JSF page
			addErrorMessage(exc);

			return null;
		}
		
		return "main_form?faces-redirect=true";
	}
	
	//Calculate interest amount
		public String calcInterest(String accountNum) {
			logger.info("Checking Balance of account: " + accountNum);

			try {
				
				double interestAmount = appDbHandler.calcInterest(accountNum);
				logger.info("The total interest amount is: " + interestAmount + " EL.");
				
			} catch (Exception exc) {
				logger.log(Level.SEVERE, "Error finding account number", exc);
				
				addErrorMessage(exc);

				return null;
			}
			
			return "main_form?faces-redirect=true";
		}
	
	private void addErrorMessage(Exception exc) {
		FacesMessage message = new FacesMessage("Error: " + exc.getMessage());
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
}
