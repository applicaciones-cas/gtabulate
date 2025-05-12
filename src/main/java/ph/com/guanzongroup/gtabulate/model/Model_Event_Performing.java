package ph.com.guanzongroup.gtabulate.model;

import java.sql.SQLException;
import org.guanzon.appdriver.agent.services.Model;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.json.simple.JSONObject;
import ph.com.guanzongroup.gtabulate.model.services.TabulationModels;

public class Model_Event_Performing extends Model {
    Model_Contest_Master poContest;
    Model_Contest_Participants poParticipant;
    
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

            ID = "sPerfrmID";
                        
            poContest = new TabulationModels(poGRider).ContestMaster();
            poParticipant = new TabulationModels(poGRider).Participants();
            
            pnEditMode = EditMode.UNKNOWN;
        } catch (SQLException e) {
            logwrapr.severe(e.getMessage());
            System.exit(1);
        }
    }

    public JSONObject setPerformingId(String id) {
        return setValue("sPerfrmID", id);
    }

    public String getPerformingId() {
        return (String) getValue("sPerfrmID");
    }
    
    public JSONObject setContestId(String id) {
        return setValue("sContstID", id);
    }

    public String getContestId() {
        return (String) getValue("sContstID");
    }
    
    public JSONObject setGroupId(String id) {
        return setValue("sGroupIDx", id);
    }

    public String getGroupId() {
        return (String) getValue("sGroupIDx");
    }
    
        
    public JSONObject setModifiedBy(String modifiedBy){
        return setValue("sModified", modifiedBy);
    }

    public String getModifiedBy() {
        return (String) getValue("sModified");
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
    
    public Model_Contest_Participants Participants() throws SQLException, GuanzonException{
        if (!"".equals((String) getValue("sGroupIDx"))){
            if (poParticipant.getEditMode() == EditMode.READY && 
                poParticipant.getGroupId().equals((String) getValue("sGroupIDx")))
                return poParticipant;
            else{
                poJSON = poParticipant.openRecord((String) getValue("sGroupIDx"));

                if ("success".equals((String) poJSON.get("result")))
                    return poParticipant;
                else {
                    poParticipant.initialize();
                    return poParticipant;
                }
            }
        } else {
            poParticipant.initialize();
            return poParticipant;
        }
    }
}
