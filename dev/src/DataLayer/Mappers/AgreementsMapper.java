package DataLayer.Mappers;

import BuisnnesLayer.Agreement;

import java.sql.*;

public class AgreementsMapper extends Mapper{

    public AgreementsMapper() {
        super();
        create_table();
    }
    @Override
    void create_table() {
        String AgreementsTable = "CREATE TABLE IF NOT EXISTS Agreements(\n" +
                                "\tsupID INTEGER PRIMARY KEY,\n" +
                                "\textraDisc REAL,\n" +
                                "\tdeliveryMods TEXT,\n" +
                                "\tdaysFromOrder INTEGER,\n" +

                                "\tFOREIGN KEY (supID) REFERENCES Suppliers(supID)\n" +
                                ");";
//        String sql = "BEGIN TRANSACTION;" + GeneralProductTable + "COMMIT;";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            // create a new tables
            stmt.execute(AgreementsTable);
            //TODO: in DataController - need to activate loadData
//            if (!identityMap.initialized){
//                LoadPreData();
//                identityMap.initialized = true;
//            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Agreement getAgreement(int supplier_id) {
        Agreement obj = null;
        try (Connection conn = connect()) {
            String statement = "SELECT * FROM Agreements WHERE supID=? ";

            try (PreparedStatement pstmt = conn.prepareStatement(statement)) {
                pstmt.setInt(1, supplier_id);

                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    int supID = rs.getInt(1);
                    double xDisc = rs.getDouble(2);
                    String mod = rs.getString(3);
                    int days = rs.getInt(4);
                    obj = new Agreement(supID,xDisc,mod,days);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return obj;
    }

    public boolean update(Agreement agreement) {
        boolean updated = false;
        try (Connection conn = connect()) {
            String statement = "UPDATE Agreements SET supID=?, extraDisc=?, deliveryMods=?, daysFromOrder=? WHERE supID=? ";

            try (PreparedStatement pstmt = conn.prepareStatement(statement)) {
                pstmt.setInt(1, agreement.getSupplierID());
                pstmt.setDouble(2, agreement.getExtraDiscount());
                pstmt.setString(3, agreement.getDeliveryMode().toString()); //TODO - better change it to String and not enum
                pstmt.setInt(4, agreement.getNumOfDaysFromOrder());
                pstmt.setInt(5, agreement.getSupplierID());
                updated = pstmt.executeUpdate() != 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return updated;
    }

    //TODO: not sure if it will be used
    public boolean delete(Agreement agreement) {
        boolean deleted = false;
        try (Connection conn = connect()) {
            String statement = "DELETE FROM Agreements WHERE supID=?";

            try (PreparedStatement pstmt = conn.prepareStatement(statement)) {
                pstmt.setInt(1, agreement.getSupplierID());
                deleted = pstmt.executeUpdate() != 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return deleted;
    }

    //TODO: make sure the dates are added properly!
    public boolean insertAgreement(Agreement agreement) {
        boolean output = false;
        try (Connection conn = connect()) {
            boolean inserted = false;
            String statement = "INSERT OR IGNORE INTO Agreements(supID,extraDisc,deliveryMods,daysFromOrder) " +
                    "VALUES (?,?,?,?)";

            try (PreparedStatement pstmt = conn.prepareStatement(statement)) {
                pstmt.setInt(1, agreement.getSupplierID());
                pstmt.setDouble(2, agreement.getExtraDiscount());
                pstmt.setString(3, agreement.getDeliveryMode().toString()); //TODO - better change it to String and not enum
                pstmt.setInt(4, agreement.getNumOfDaysFromOrder());
                output = pstmt.executeUpdate() != 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return output;
    }
}
