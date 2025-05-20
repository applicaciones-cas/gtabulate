package ph.com.guanzongroup.gtabulate.model;

import java.sql.SQLException;
import java.util.Date;
import org.guanzon.appdriver.agent.services.Model;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.json.simple.JSONObject;
import ph.com.guanzongroup.gtabulate.model.services.TabulationModels;

public class Model_Event_Tabulation extends Model {
    Model_Contest_Participants poParticipants;
    
    @Override
    public void initialize() {
        try {
            poEntity = MiscUtil.xml2ResultSet(System.getProperty("sys.default.path.metadata") + XML, getTable());

            poEntity.last();
            poEntity.moveToInsertRow();

            MiscUtil.initRowSet(poEntity);
            poEntity.updateObject("nRankxxxx", 0.00);
           
            poEntity.insertRow();
            poEntity.moveToCurrentRow();

            poEntity.absolute(1);

            ID = "sGroupIDx";
            ID2 = "sComptrID";
            
            poParticipants = new TabulationModels(poGRider).Participants();
            
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
    
    public JSONObject setComputerId(String id){
        return setValue("sComptrID", id);
    }

    public String getComputerId() {
        return (String) getValue("sComptrID");
    }

    public JSONObject setJudgeName(String name){
        return setValue("sJudgeNme", name);
    }

    public String getJudgeName() {
        return (String) getValue("sJudgeNme");
    }
    
    public JSONObject setRatings(double rate){
        return setValue("nRatingsx", rate);
    }

    public double getRatings() {
        return (double) getValue("nRatingsx");
    }
    
    public JSONObject setRank(double rank){
        return setValue("nRankxxxx", rank);
    }

    public double getRank() {
        return (double) getValue("nRankxxxx");
    }
    
    public JSONObject setModifiedDate(Date modifiedDate){
        return setValue("dModified", modifiedDate);
    }
    
    public Date getModifiedDate(){
        return (Date) getValue("dModified");
    }
    
    public Model_Contest_Participants Participants() throws SQLException, GuanzonException{
        if (!"".equals((String) getValue("sGroupIDx"))){
            if (poParticipants.getEditMode() == EditMode.READY && 
                poParticipants.getGroupId().equals((String) getValue("sGroupIDx")))
                return poParticipants;
            else{
                poJSON = poParticipants.openRecord((String) getValue("sGroupIDx"));

                if ("success".equals((String) poJSON.get("result")))
                    return poParticipants;
                else {
                    poParticipants.initialize();
                    return poParticipants;
                }
            }
        } else {
            poParticipants.initialize();
            return poParticipants;
        }
    }
    
    @Override
    public String getNextCode(){
        return "";
    }
}
