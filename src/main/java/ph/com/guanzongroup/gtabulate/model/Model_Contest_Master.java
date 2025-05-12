package ph.com.guanzongroup.gtabulate.model;

import java.sql.SQLException;
import java.util.Date;
import org.guanzon.appdriver.agent.services.Model;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.RecordStatus;
import org.json.simple.JSONObject;
import ph.com.guanzongroup.gtabulate.model.services.TabulationModels;

public class Model_Contest_Master extends Model {
    Model_Events poEvents;
    
    @Override
    public void initialize() {
        try {
            poEntity = MiscUtil.xml2ResultSet(System.getProperty("sys.default.path.metadata") + XML, getTable());

            poEntity.last();
            poEntity.moveToInsertRow();

            MiscUtil.initRowSet(poEntity);

            //assign default values
            poEntity.updateString("cRecdStat", RecordStatus.ACTIVE);
            //end - assign default values

            poEntity.insertRow();
            poEntity.moveToCurrentRow();

            poEntity.absolute(1);

            ID = poEntity.getMetaData().getColumnLabel(1);
            
            poEvents = new TabulationModels(poGRider).Events();
            
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

    public JSONObject setDescription(String description) {
        return setValue("sDescript", description);
    }

    public String getDescription() {
        return (String) getValue("sDescript");
    }
    
    public JSONObject setEventId(String id) {
        return setValue("sEventIDx", id);
    }

    public String getEventId() {
        return (String) getValue("sEventIDx");
    }
    
    public JSONObject setEntryNo(int entryNo) {
        return setValue("nEntryNox", entryNo);
    }

    public int getEntryNo() {
        return (int) getValue("nEntryNox");
    }
    
    public JSONObject isWithOnlineVote(boolean onlineVote) {
        return setValue("cOnlineVt", onlineVote);
    }

    public boolean isWithOnlineVote() {
        return (boolean) getValue("cOnlineVt");
    }
    
    public JSONObject setVoteStart(Date datetime) {
        return setValue("dVoteStrt", datetime);
    }

    public Date getVoteStart() {
        return (Date) getValue("dVoteStrt");
    }
    
    public JSONObject setVoteEnd(Date datetime) {
        return setValue("dVoteEndx", datetime);
    }

    public Date getVoteEnd() {
        return (Date) getValue("dVoteEndx");
    }
    
    public JSONObject setRecordStatus(String recordStatus){
        return setValue("cRecdStat", recordStatus);
    }

    public String getRecordStatus() {
        return (String) getValue("cRecdStat");
    }
    
    public Model_Events Events() throws SQLException, GuanzonException{
        if (!"".equals((String) getValue("sEventIDx"))){
            if (poEvents.getEditMode() == EditMode.READY && 
                poEvents.getEventId().equals((String) getValue("sEventIDx")))
                return poEvents;
            else{
                poJSON = poEvents.openRecord((String) getValue("sEventIDx"));

                if ("success".equals((String) poJSON.get("result")))
                    return poEvents;
                else {
                    poEvents.initialize();
                    return poEvents;
                }
            }
        } else {
            poEvents.initialize();
            return poEvents;
        }
    }
}
