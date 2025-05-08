package ph.com.guanzongroup.gtabulate.model;

import java.sql.SQLException;
import java.util.Date;
import org.guanzon.appdriver.agent.services.Model;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.RecordStatus;
import org.json.simple.JSONObject;

public class Model_Events extends Model {
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

            pnEditMode = EditMode.UNKNOWN;
        } catch (SQLException e) {
            logwrapr.severe(e.getMessage());
            System.exit(1);
        }
    }

    public JSONObject setEventId(String id) {
        return setValue("sEventIDx", id);
    }

    public String getEventId() {
        return (String) getValue("sEventIDx");
    }

    public JSONObject setDescription(String description) {
        return setValue("sEventNme", description);
    }

    public String getDescription() {
        return (String) getValue("sEventNme");
    }
    
    public JSONObject setEventYear(int year) {
        return setValue("nEventYrx", year);
    }

    public int getEventYear() {
        return (int) getValue("nEventYrx");
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
    
    public JSONObject setEntryNo(int entryNo) {
        return setValue("nNoContst", entryNo);
    }

    public int getEntryNo() {
        return (int) getValue("nNoContst");
    }
    
    public JSONObject setRecordStatus(String recordStatus){
        return setValue("cRecdStat", recordStatus);
    }

    public String getRecordStatus() {
        return (String) getValue("cRecdStat");
    }
}
