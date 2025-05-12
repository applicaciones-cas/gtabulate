package ph.com.guanzongroup.gtabulate.model;

import java.sql.SQLException;
import org.guanzon.appdriver.agent.services.Model;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.json.simple.JSONObject;
import ph.com.guanzongroup.gtabulate.model.services.TabulationModels;

public class Model_Contest_Participants extends Model {
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

            ID = poEntity.getMetaData().getColumnLabel(1);
            
            poContest = new TabulationModels(poGRider).ContestMaster();
            
            pnEditMode = EditMode.UNKNOWN;
        } catch (SQLException e) {
            logwrapr.severe(e.getMessage());
            System.exit(1);
        }
    }

    public JSONObject setGroupId(String id) {
        return setValue("sGroupIDx", id);
    }

    public String getGroupId() {
        return (String) getValue("sGroupIDx");
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
    
    public JSONObject setEntryName(String name) {
        return setValue("sEntryNme", name);
    }

    public String getEntryName() {
        return (String) getValue("sEntryNme");
    }
    
    public JSONObject setMembers(int members) {
        return setValue("nMemberxx", members);
    }

    public int getMembers() {
        return (int) getValue("nMemberxx");
    }
        
    public Model_Contest_Master Contest() throws SQLException, GuanzonException{
        if (!"".equals((String) getValue("sContstID"))){
            if (poContest.getEditMode() == EditMode.READY && 
                poContest.getEventId().equals((String) getValue("sContstID")))
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
