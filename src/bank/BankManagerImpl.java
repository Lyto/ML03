package bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
    private static final String CREATE_TABLE_DUMMY = "create table DUMMY ("
            + "ATT int, "
            + "primary key (ATT)"
            + ")";
    private Connection conn;
    private Statement stmt;
    private ResultSet res;
    private PreparedStatement PreparedStmt;


    /**
     * Creates a new ReservationManager object. This creates a new connection to
     * the specified database.
     *
     * @param url the url of the database to connect to
     * @param user the login name of the user
     * @param password his password
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
        String CREATE_TABLE_ACCOUNT = "create table IF NOT EXISTS ACCOUNT ("
                + "AID int, "
                + "BALANCE double,"
                + "primary key (AID)"
                + ")";

        stmt.executeUpdate(CREATE_TABLE_ACCOUNT);

        String DELETE_CONTENT = "DELETE FROM `account` WHERE AID!=0";
        stmt.executeUpdate(DELETE_CONTENT);

        String CREATE_TABLE_OPERATION = "create table IF NOT EXISTS OPERATION ("
                + "AID int, "
                + "AMOUNT double, "
                + "DATE date, "
                + "foreign key(AID) REFERENCES ACCOUNT(AID) ON DELETE CASCADE"
                + ")";

        stmt.executeUpdate(CREATE_TABLE_OPERATION);

        String DROP_ACCOUNT_CREATION_TRIGGER = "DROP TRIGGER IF EXISTS ACCOUNT_CREATION_TRIG;";
        stmt.executeUpdate(DROP_ACCOUNT_CREATION_TRIGGER);
        String ACCOUNT_CREATION_TRIGGER = "CREATE TRIGGER ACCOUNT_CREATION_TRIG BEFORE INSERT ON account FOR EACH ROW BEGIN IF (NEW.BALANCE<0) THEN SET NEW.BALANCE=0; END IF;END;";
        stmt.executeUpdate(ACCOUNT_CREATION_TRIGGER);

        String DROP_ACCOUNT_UPDATE_TRIGGER = "DROP TRIGGER IF EXISTS ACCOUNT_UPDATE_TRIG;";
        stmt.executeUpdate(DROP_ACCOUNT_UPDATE_TRIGGER);
        String ACCOUNT_UPDATE_TRIGGER = "CREATE TRIGGER ACCOUNT_UPDATE_TRIG BEFORE UPDATE ON account FOR EACH ROW BEGIN IF(NEW.BALANCE<0) THEN SET NEW.BALANCE=OLD.BALANCE; SIGNAL sqlstate '45001' set message_text = \"No way ! You cannot do this !\"; END IF; END;";
        stmt.executeUpdate(ACCOUNT_UPDATE_TRIGGER);

  
        String DROP_ACCOUNT_AFTER_UPDATE_TRIGGER ="DROP TRIGGER IF EXISTS ACCOUNT_AFTER_UPDATE_TRIGGER;";
        stmt.executeUpdate(DROP_ACCOUNT_AFTER_UPDATE_TRIGGER);
        String ACCOUNT_AFTER_UPDATE_TRIGGER = "CREATE TRIGGER ACCOUNT_AFTER_UPDATE_TRIGGER AFTER UPDATE ON account FOR EACH ROW BEGIN INSERT INTO OPERATION(AID, AMOUNT, DATE) VALUES(NEW.AID,NEW.BALANCE-OLD.BALANCE,now()); END;";
        stmt.executeUpdate(ACCOUNT_AFTER_UPDATE_TRIGGER);

    }

    @Override
    public boolean createAccount(int number) throws SQLException {

        String insertString = "INSERT INTO ACCOUNT values (?,?);";

        try {
            conn.setAutoCommit(false);
            PreparedStmt = conn.prepareStatement(insertString);

            PreparedStmt.setInt(1, number);
            PreparedStmt.setDouble(2, 0);

            PreparedStmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            return false;
        }

        return true;
    }

    @Override
    public double getBalance(int number) throws SQLException {

        String selectString = "SELECT BALANCE FROM ACCOUNT where AID=" + number + ";";
        Double balance = null;

        try {
            conn.setAutoCommit(false);
            // récupération du résultat de la requête
            res = stmt.executeQuery(selectString);

            while (res.next()) {
                balance = res.getDouble("BALANCE");
            }
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
        }

        return balance;
    }

    @Override
    public double addBalance(int number, double amount) throws SQLException {

        String updateString = "UPDATE ACCOUNT SET BALANCE = BALANCE +" + amount + " WHERE AID=" + number + ";";

        // Effectuer la mise à jour du compte
        //stmt.executeUpdate(updateString);

        //Date date = new Date(System.currentTimeMillis());
        try {
            conn.setAutoCommit(false);

            // Effectuer la mise à jour du compte
            stmt.executeUpdate(updateString);

            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
        }


        return amount;
    }

    @Override
    public boolean transfer(int from, int to, double amount) throws SQLException {

        boolean success = true; // évalue le succès du transfert 

        try {
            conn.setAutoCommit(false);
            // Effectuer la mise à jour compte débiteur
            String updateAccount1 = "UPDATE ACCOUNT SET BALANCE = BALANCE - " + amount + " WHERE AID=" + from + ";";
            stmt.executeUpdate(updateAccount1);

            // Effectuer la mise à jour compte bénéficiaire
            String updateAccount2 = "UPDATE ACCOUNT SET BALANCE = BALANCE + " + amount + " WHERE AID=" + to + ";";
            stmt.executeUpdate(updateAccount2);

            conn.commit();
        } catch (SQLException e) {
            //conn.rollback();
        }

        return success;
    }

    @Override
    public List<Operation> getOperations(int number, Date from, Date to) throws SQLException {
        // récupération de l'ordre de la requete
        res = stmt.executeQuery("SELECT * FROM OPERATION WHERE AID=" + number +"");

        // creation d'une List d'opérations
        List<Operation> operations = new ArrayList<Operation>();

        // tant qu'il reste une ligne 
        while (res.next()) {
            Operation operation = new Operation(res.getInt("AID"),
                    res.getDouble("amount"), res.getDate("date"));
            operations.add(operation);
        }
        // Retourner l'ArrayList
        return operations;
    }
}
