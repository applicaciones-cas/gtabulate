package ph.com.guanzongroup.gtabulate.model;

import java.math.BigDecimal;
import java.sql.SQLException;
import org.guanzon.appdriver.agent.services.Model;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.json.simple.JSONObject;
import ph.com.guanzongroup.gtabulate.model.services.TabulationModels;

public class Model_Event_Criteria extends Model {
    Model_Contest_Master poContest;
    
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

            ID = "sContstID";
            ID2 = "nEntryNox";
            ID3 = "sCriteria";
            
            poContest = new TabulationModels(poGRider).ContestMaster();
            
            pnEditMode = EditMode.UNKNOWN;
        } catch (SQLException e) {
            logwrapr.severe(e.getMessage());
            System.exit(1);
        }
    }

    public JSONObject setContestId(String id) {
        return setValue("sContstID", id);
    }

    public String getContestId() {
        return (String) getValue("sContstID");
    }
    
    public JSONObject setEntryNo(int entryNo) {
        return setValue("nEntryNox", entryNo);
    }

    public int getEntryNo() {
        return (int) getValue("nEntryNox");
    }
        
    public JSONObject setDescription(String description){
        return setValue("sCriteria", description);
    }

    public String getDescription() {
        return (String) getValue("sCriteria");
    }
    
    public JSONObject setPercentage(BigDecimal percentage){
        return setValue("nPercentx", percentage);
    }

    public BigDecimal getPercentage() {
        return (BigDecimal) getValue("nPercentx");
    }
    
    public Model_Contest_Master Contest() throws SQLException, GuanzonException{
        if (!"".equals((String) getValue("sContstID"))){
            if (poContest.getEditMode() == EditMode.READY && 
                poContest.getContestId().equals((String) getValue("sContstID")))
                return poContest;
            else{
                poJSON = poContest.openRecord((String) getValue("sContstID"));

                if ("success".equals((String) poJSON.get("result")))
                    return poContest;
                else {
                    poContest.initialize();
                    return poContest;
                }
            }
        } else {
            poContest.initialize();
            return poContest;
        }
    }
}
