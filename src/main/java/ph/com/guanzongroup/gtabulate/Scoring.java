package ph.com.guanzongroup.gtabulate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.guanzon.appdriver.agent.services.Model;
import org.guanzon.appdriver.agent.services.Transaction;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.json.simple.JSONObject;
import ph.com.guanzongroup.gtabulate.model.Model_Contest_Participants;
import ph.com.guanzongroup.gtabulate.model.services.TabulationModels;

public class Scoring extends Transaction{
    private String psContestId;
    private List<Model> paParticipants;
    
    public void setContestId(String contestId){
        psContestId = contestId;
    }
    
    public int getParticipantsCount(){
        return paParticipants.size();
    }
    
    @Override
    protected JSONObject initialize() {
        poMaster = new TabulationModels(poGRider).Tabulation();
        poDetail = new TabulationModels(poGRider).TabulationDetail();
        
        return super.initialize();
    }
    
    @Override
    protected JSONObject newTransaction() throws CloneNotSupportedException {
        return super.newTransaction();
    }
    
    @Override
    protected JSONObject saveTransaction() throws CloneNotSupportedException, SQLException, GuanzonException {
        return super.saveTransaction();
    }
    
    @Override
    protected JSONObject openTransaction(String transactionNo) throws CloneNotSupportedException, SQLException, GuanzonException {
        return super.openTransaction(transactionNo);
    }
    
    @Override
    protected JSONObject updateTransaction() {
        return super.updateTransaction();
    }
    
    public JSONObject LoadParticipants() throws SQLException, GuanzonException, CloneNotSupportedException{
        poJSON = new JSONObject();
        
        if (psContestId.isEmpty()) {
            poJSON.put("result", "error");
            poJSON.put("message", "Contest is not set.");
            return poJSON;
        }
        
        String lsSQL = "SELECT" +
                            "  sGroupIDx" +
                        " FROM Contest_Participants" +
                        " WHERE sContstID = " + SQLUtil.toSQL(psContestId) +
                        " ORDER BY nEntryNox";
        
        ResultSet loRS = poGRider.executeQuery(lsSQL);
        
        if (MiscUtil.RecordCount(loRS) <= 0) {
            poJSON.put("result", "error");
            poJSON.put("message", "No participants registered on this contest.");
            return poJSON;
        }
        
        paParticipants.clear();
        
        Model_Contest_Participants loParticipant = new Model_Contest_Participants();
        
        while (loRS.next()){
            poJSON = loParticipant.openRecord(loRS.getString("sGroupIDx"));
            
            if ("success".equals((String) poJSON.get("result")))
                paParticipants.add((Model) loParticipant.clone());
            else
                return poJSON;
        }
        
        poJSON = new JSONObject();
        poJSON.put("result", "success");
        return poJSON;
    }
}
