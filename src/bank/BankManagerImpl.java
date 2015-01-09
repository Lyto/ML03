package bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

/**
 * A simple implementation of the ReservationManager interface. Each object of
 * this class must create a dedicated connection to the database.
 * <p>
 * <b>Note: DO NOT alter this class's interface.</b>
 * 
 * @author Busca
 * 
 */
public class BankManagerImpl implements BankManager {

    // CLASS FIELDS
    //
    // example of a create table statement executed by createDB()
    private static final String CREATE_TABLE_DUMMY = "create table DUMMY (" + 
	    "ATT int, " + 
	    "primary key (ATT)" + 
	    ")";
    private Connection conn;
    private Statement stmt;
    private ResultSet res;

    /**
     * Creates a new ReservationManager object. This creates a new connection to
     * the specified database.
     * 
     * @param url
     *            the url of the database to connect to
     * @param user
     *            the login name of the user
     * @param password
     *            his password
     */
    public BankManagerImpl(String url, String user, String password) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection(url, user, password);
        stmt = conn.createStatement();
        System.out.println("Connexion à la base de données: OK");
    }

    @Override
    public void createDB() throws SQLException {
	// TODO Auto-generated method stub
        String CREATE_TABLE_ACCOUNT = "create table ACCOUNT (" + 
	    "AID int, " +
            "BALANCE double," +
	    "primary key (AID)" + 
	    ")";
        
        String CREATE_TABLE_OPERATION = "create table OPERATION (" + 
	    "OID int, " + 
            "AID int, " +
            "AMOUNT double, " +
            "DATE date, " +
	    "primary key (OID)," + 
            "foreign key(AID) REFERENCES ACCOUNT(AID) ON DELETE CASCADE,"+
	    ")";
        
        String ACCOUNT_CREATION_TRIGGER = " create TRIGGER ACCOUNT_CREATION_TRIG" +
            " BEFORE INSERT OF BALANCE ON ACCOUNT" +
            " REFERENCING NEW ROW AS BALANCE" + 
            " FOR EACH ROW " +
            " WHEN (BALANCE.ACCOUNT<0)"+
            " SET BALANCE.ACCOUNT=0"; 
        
        String ACCOUNT_UPDATE_TRIGGER = "create TRIGGER ACCOUNT_UPDATE_TRIG" +
            "BEFORE UPDATE OF BALANCE ON ACCOUNT" +
            " REFERENCING " +
            " OLD ROW AS OLDBALANCE" +
            " NEW ROW AS NEWBALANCE" +
            " FOR EACH ROW" +
            " WHEN (NEWBALANCE<0)"+
            " SET NEWBALANCE=OLDBALANCE";
    }

    @Override
    public boolean createAccount(int number) throws SQLException {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public double getBalance(int number) throws SQLException {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public double addBalance(int number, double amount) throws SQLException {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public boolean transfer(int from, int to, double amount) throws SQLException {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public List<Operation> getOperations(int number, Date from, Date to) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

}
