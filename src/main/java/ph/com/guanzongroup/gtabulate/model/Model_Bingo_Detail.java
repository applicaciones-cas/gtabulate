package ph.com.guanzongroup.gtabulate.model;

import java.sql.SQLException;
import org.guanzon.appdriver.agent.services.Model;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.json.simple.JSONObject;

public class Model_Bingo_Detail extends Model {
    @Override
    public void initialize() {
        try {
            poEntity = MiscUtil.xml2ResultSet(System.getProperty("sys.default.path.metadata") + XML, getTable());

            poEntity.last();
            poEntity.moveToInsertRow();

            MiscUtil.initRowSet(poEntity);

            poEntity.insertRow();
            poEntity.moveToCurrentRow();

            poEntity.absolute(1);

            ID = "sTransNox";
            ID2 = "nEntryNox";
            
            pnEditMode = EditMode.UNKNOWN;
        } catch (SQLException e) {
            logwrapr.severe(e.getMessage());
            System.exit(1);
        }
    }

    public JSONObject setTransactionNo(String transactionNo) {
        return setValue("sTransNox", transactionNo);
    }

    public String getTransactionNo() {
        return (String) getValue("sTransNox");
    }

    public JSONObject setEntryNo(String entryNo) {
        return setValue("nEntryNox", entryNo);
    }

    public String getEntryNo() {
        return (String) getValue("nEntryNox");
    }
    
    public JSONObject setBingoNo(int number) {
        return setValue("nBingoNox", number);
    }

    public String getBingoNo() {
        return (String) getValue("nBingoNox");
    }
    
    @Override
    public String getNextCode(){
        return "";
    }
}
