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
import org.guanzon.appdriver.constant.EditMode;
import org.json.simple.JSONObject;
import ph.com.guanzongroup.gtabulate.model.Model_Contest_Participants;
import ph.com.guanzongroup.gtabulate.model.Model_Contest_Participants_Meta;
import ph.com.guanzongroup.gtabulate.model.Model_Event_Criteria;
import ph.com.guanzongroup.gtabulate.model.Model_Event_Tabulation;
import ph.com.guanzongroup.gtabulate.model.Model_Event_Tabulation_Detail;
import ph.com.guanzongroup.gtabulate.model.services.TabulationModels;

public class Scoring extends Transaction{   
    private String psContestId;
    private String psJudgeName;
    
    private List<Model> paParticipants;
    private List<Model> paCriteria;
    
    public void setContestId(String contestId){
        psContestId = contestId;
    }
    
    public void setJudgeName(String name){
        psJudgeName = name;
    }
    
    private String getJudgeName(){
        return psJudgeName;
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
    
    public Model_Event_Tabulation_Detail getDetail(int criteriaNo){
        if(getMaster().getGroupId().isEmpty() ||
            getMaster().getComputerId().isEmpty()) return null;
        
        if (criteriaNo <= 0 || criteriaNo > paCriteria.size()) return null;
        
        Model_Event_Tabulation_Detail loDetail;
        
        //find the criteria record
        for (int lnCtr = 0; lnCtr <= paDetail.size() - 1; lnCtr++){
            loDetail = (Model_Event_Tabulation_Detail) paDetail.get(lnCtr);
            
            if (loDetail.getEntryNo() == criteriaNo) return loDetail;
        }
        
        //no record found, so create a new record for the criteria
        loDetail = new TabulationModels(poGRider).TabulationDetail();
        loDetail.newRecord();
        loDetail.setGroupId(getMaster().getGroupId());
        loDetail.setComputerId(getMaster().getComputerId());
        loDetail.setEntryNo(criteriaNo);
        loDetail.setRate(0.00);
        paDetail.add(loDetail);
        
        return loDetail;
    }
    
    public JSONObject initTransaction() {
        SOURCE_CODE = "Tbln";
        
        poMaster = new TabulationModels(poGRider).Tabulation();
        poDetail = new TabulationModels(poGRider).TabulationDetail();
        
        paCriteria = new ArrayList<>();
        paParticipants = new ArrayList<>();
        
        return super.initialize();
    }
    
    public JSONObject openTransaction(String groupId, String computerId) throws CloneNotSupportedException, SQLException, GuanzonException{
        if (psJudgeName.isEmpty()){
            
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "Judge name must number empty.");
            return poJSON;
        }
        
        
        poJSON = poMaster.openRecord(groupId, computerId);
        
        if (!"success".equals((String) poJSON.get("result"))){
            poJSON = newTransaction();
            
            if (!"success".equals((String) poJSON.get("result"))) return poJSON;
            
            getMaster().setGroupId(groupId);
            getMaster().setComputerId(computerId);
            getMaster().setJudgeName(psJudgeName);
            getMaster().setRatings(0.00);
            return poJSON;
        }
        
        poMaster.updateRecord();
        
        paDetail.clear();
        
        String lsSQL = "SELECT * FROM " + poDetail.getTable() +
                        " WHERE sGroupIDx = " + SQLUtil.toSQL(groupId) +
                            " AND sComptrID = " + SQLUtil.toSQL(computerId);
        
        ResultSet loRS = poGRider.executeQuery(lsSQL);
        
        if (MiscUtil.RecordCount(loRS) > 0){
            while (loRS.next()){
                Model loDetail = (Model) poDetail.clone();
                loDetail.newRecord();

                poJSON = loDetail.openRecord(groupId, computerId, loRS.getInt("nEntryNox"));

                if (!"success".equals((String) poJSON.get("result"))) {
                    poJSON.put("message", "Unable to open transaction detail record.");
                    clear();
                    return poJSON;
                }    
                loDetail.updateRecord();

                paDetail.add(loDetail);
            }
        }
        poEvent = new JSONObject();
        poEvent.put("event", "UPDATE");
        
        pnEditMode = EditMode.UPDATE;
        pbRecordExist = true;
        
        poJSON = new JSONObject();
        poJSON.put("result", "success");
        return poJSON;
    }
    
    @Override
    protected JSONObject newTransaction() throws CloneNotSupportedException {                
        if (!pbInitTran){
            poJSON.put("result", "error");
            poJSON.put("message", "Object is not initialized.");
            return poJSON;
        }
        
        poMaster.initialize();
        poMaster.newRecord();
        
        poDetail.initialize();
        poDetail.newRecord();
        
        paDetail.clear();
        
        poJSON = initFields();
        if ("error".equals((String) poJSON.get("result"))) return poJSON;
        
        pnEditMode = EditMode.ADDNEW;
        
        poEvent = new JSONObject();
        poEvent.put("event", "ADD NEW");            
                
        poJSON = new JSONObject();
        poJSON.put("result", "success");
        return poJSON;
    }
    
    @Override
    public JSONObject saveTransaction() throws CloneNotSupportedException, SQLException, GuanzonException{
        poJSON = new JSONObject();
        
        if (!pbInitTran){
            poJSON.put("result", "error");
            poJSON.put("message", "Object is not initialized.");
            return poJSON;
        }
        
        if (pnEditMode == EditMode.READY){
            poJSON.put("result", "error");
            poJSON.put("message", "Saving of unmodified transaction is not allowed.");
            return poJSON;
        }
                
        poJSON = willSave();
        if ("error".equals((String) poJSON.get("result"))) return poJSON;
        
        if (getEditMode() == EditMode.ADDNEW){
            pdModified = poGRider.getServerDate();
            poMaster.setValue("sModified", poGRider.Encrypt(poGRider.getUserID()));
        }
        
        
        poJSON = save();
        
        if (!pbWthParent) {
            poGRider.beginTrans((String) poEvent.get("event"), 
                        poMaster.getTable(), 
                        SOURCE_CODE, 
                        String.valueOf(poMaster.getValue(1)));
        }
        
        if ("success".equals((String) poJSON.get("result"))){
            //save master and detail
            if (pbVerifyEntryNo){
                poMaster.setValue("nEntryNox", paDetail.size());
            }
            
            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE){
                poMaster.setValue("dModified", pdModified);
                poJSON = poMaster.saveRecord();
                
                if ("error".equals((String) poJSON.get("result"))){
                    if (!pbWthParent) poGRider.rollbackTrans();
                    return poJSON;
                }
                
                for (int lnCtr = 0; lnCtr <= paDetail.size() -1; lnCtr++){
                    paDetail.get(lnCtr).setValue("dModified", pdModified);
                    poJSON = paDetail.get(lnCtr).saveRecord();
                    
                    if ("error".equals((String) poJSON.get("result"))){
                        if (!pbWthParent) poGRider.rollbackTrans();
                        return poJSON;
                    }
                }
            } else {
                poJSON.put("result", "error");
                poJSON.put("message", "Edit mode is not allowed to save transaction.");
                return poJSON;
            }
        } else {
            if (!pbWthParent) poGRider.rollbackTrans();
            
            return poJSON;
        }

        if (!pbWthParent) poGRider.commitTrans();
        
        pnEditMode = EditMode.UNKNOWN;
        pbRecordExist = true;
        
        poJSON = new JSONObject();
        poJSON.put("result", "success");
        poJSON.put("message", "Transaction saved successfully.");
        return poJSON;
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