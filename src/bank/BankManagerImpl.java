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
        String CREATE_TABLE_ACCOUNT = "create table IF NOT EXISTS ACCOUNT (" + 
	    "AID int, " +
            "BALANCE double," +
	    "primary key (AID)" + 
	    ")";
        
        stmt.executeUpdate(CREATE_TABLE_ACCOUNT);
        
        String CREATE_TABLE_OPERATION = "create table IF NOT EXISTS OPERATION (" + 
	    "OID int, " + 
            "AID int, " +
            "AMOUNT double, " +
            "DATE date, " +
	    "primary key (OID)," + 
            "foreign key(AID) REFERENCES ACCOUNT(AID) ON DELETE CASCADE"+
	    ")";
        
        stmt.executeUpdate(CREATE_TABLE_OPERATION);
        
        String DROP_ACCOUNT_CREATION_TRIGGER ="DROP TRIGGER IF EXISTS ACCOUNT_CREATION_TRIG;";
        stmt.executeUpdate(DROP_ACCOUNT_CREATION_TRIGGER);
        String ACCOUNT_CREATION_TRIGGER ="CREATE TRIGGER ACCOUNT_CREATION_TRIG BEFORE INSERT ON account FOR EACH ROW BEGIN IF (NEW.BALANCE<0) THEN SET NEW.BALANCE=0; END IF;END;";   
        stmt.executeUpdate(ACCOUNT_CREATION_TRIGGER);
        
        String DROP_ACCOUNT_UPDATE_TRIGGER ="DROP TRIGGER IF EXISTS ACCOUNT_UPDATE_TRIG;";
        stmt.executeUpdate(DROP_ACCOUNT_UPDATE_TRIGGER);
        String ACCOUNT_UPDATE_TRIGGER = "CREATE TRIGGER ACCOUNT_UPDATE_TRIG BEFORE UPDATE ON account FOR EACH ROW BEGIN IF(NEW.BALANCE<0) THEN SET NEW.BALANCE=OLD.BALANCE; END IF; END;";
        stmt.executeUpdate(ACCOUNT_UPDATE_TRIGGER);
        /*
        String DROP_BEFORE_INSERT_ACCOUNT_TRIGGER ="DROP TRIGGER IF EXISTS BEFORE_INSERT_ACCOUNT_TRIG;";
        stmt.executeUpdate(DROP_BEFORE_INSERT_ACCOUNT_TRIGGER);
        String BEFORE_INSERT_ACCOUNT_TRIGGER = "CREATE TRIGGER BEFORE_INSERT_ACCOUNT_TRIG BEFORE INSERT ON account FOR EACH ROW BEGIN IF(EXISTS(SELECT * FROM account WHERE AID=NEW.AID)) THEN INSERT INTO account(AID, BALANCE) VALUES(NEW.AID, NEW.BALANCE); END IF; END;";
        stmt.executeUpdate(BEFORE_INSERT_ACCOUNT_TRIGGER);
           */ 
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
