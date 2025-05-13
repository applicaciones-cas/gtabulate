package ph.com.guanzongroup.gtabulate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.guanzon.appdriver.agent.services.Model;
import org.guanzon.appdriver.agent.services.Transaction;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.json.simple.JSONObject;
import ph.com.guanzongroup.gtabulate.model.Model_Contest_Participants;
import ph.com.guanzongroup.gtabulate.model.Model_Contest_Participants_Meta;
import ph.com.guanzongroup.gtabulate.model.Model_Event_Criteria;
import ph.com.guanzongroup.gtabulate.model.Model_Event_Tabulation;
import ph.com.guanzongroup.gtabulate.model.services.TabulationModels;

public class Scoring extends Transaction{
    private String psContestId;
    
    private List<Model> paParticipants;
    private List<Model> paCriteria;
    
    public void setContestId(String contestId){
        psContestId = contestId;
    }
    
    public int getParticipantsCount(){
        return paParticipants.size();
    }
    
    public int getCriteriaCount(){
        return paCriteria.size();
    }
    
    public Model_Contest_Participants Participant(int row){
        if (row > paParticipants.size()) return null;
        
        return (Model_Contest_Participants) paParticipants.get(row);
    }
    
    public Model_Event_Criteria Criteria(int row){
        if (row > paCriteria.size()) return null;
        
        return (Model_Event_Criteria) paCriteria.get(row);
    }
    
    public Model_Event_Tabulation getMaster(){
        return (Model_Event_Tabulation) poMaster;
    }
    
    public JSONObject initTransaction() {
        poMaster = new TabulationModels(poGRider).Tabulation();
        poDetail = new TabulationModels(poGRider).TabulationDetail();
        
        paCriteria = new ArrayList<>();
        paParticipants = new ArrayList<>();
        
        return super.initialize();
    }
    
    @Override
    public JSONObject newTransaction() throws CloneNotSupportedException {
        return super.newTransaction();
    }
    
    @Override
    public JSONObject saveTransaction() throws CloneNotSupportedException, SQLException, GuanzonException {        
        return super.saveTransaction();
    }
    
    @Override
    public JSONObject openTransaction(String transactionNo) throws CloneNotSupportedException, SQLException, GuanzonException {
        return super.openTransaction(transactionNo);
    }
    
    @Override
    public JSONObject updateTransaction() {
        return super.updateTransaction();
    }
    
    public JSONObject loadCriteriaForJudging() throws SQLException, GuanzonException, CloneNotSupportedException{
        poJSON = new JSONObject();
        
        if (psContestId == null || psContestId.isEmpty()) {
            poJSON.put("result", "error");
            poJSON.put("message", "Contest is not set.");
            return poJSON;
        }
        
        String lsSQL = "SELECT * FROM Event_Criteria" +
                        " WHERE sContstID = " + SQLUtil.toSQL(psContestId) +
                        " ORDER BY nEntryNox";
        
        ResultSet loRS = poGRider.executeQuery(lsSQL);
        
        if (MiscUtil.RecordCount(loRS) <= 0) {
            poJSON.put("result", "error");
            poJSON.put("message", "No criteria for judging registered on this contest.");
            return poJSON;
        }
        
        paCriteria.clear();
        
        while (loRS.next()){
            Model_Event_Criteria loCriteria = new TabulationModels(poGRider).Criteria();
            
            poJSON = loCriteria.openRecord(loRS.getString("sContstID"), 
                                                loRS.getString("nEntryNox"), 
                                                loRS.getString("sCriteria"));
            
            if ("success".equals((String) poJSON.get("result")))
                paCriteria.add((Model) loCriteria);
            else
                return poJSON;
        }
        
        poJSON = new JSONObject();
        poJSON.put("result", "success");
        return poJSON;
    }
    
    public JSONObject loadParticipants() throws SQLException, GuanzonException, CloneNotSupportedException{
        poJSON = new JSONObject();
        
        if (psContestId == null || psContestId.isEmpty()) {
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
        
        while (loRS.next()){
            Model_Contest_Participants loParticipant = new TabulationModels(poGRider).Participants();
            
            poJSON = loParticipant.openRecord(loRS.getString("sGroupIDx"));
            
            if ("success".equals((String) poJSON.get("result")))
                paParticipants.add((Model) loParticipant);
            else
                return poJSON;
        }
        
        poJSON = new JSONObject();
        poJSON.put("result", "success");
        return poJSON;
    }
    
    public Model_Contest_Participants_Meta ParticipantMeta(String groupId, String metaId) throws SQLException, GuanzonException{
        Model_Contest_Participants_Meta meta = new TabulationModels(poGRider).ParticipantsMeta();
        
        poJSON = meta.openRecord(groupId, metaId);
        
        if ("success".equals((String) poJSON.get("result"))) return meta;
        
        return null;
    }
}