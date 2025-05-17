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

public class Model_Bingo_Master extends Model {
    Model_Bingo_Pattern poPattern;
    
    @Override
    public void initialize() {
        try {
            poEntity = MiscUtil.xml2ResultSet(System.getProperty("sys.default.path.metadata") + XML, getTable());

            poEntity.last();
            poEntity.moveToInsertRow();

            MiscUtil.initRowSet(poEntity);

            //assign default values
            poEntity.updateString("cTranStat", RecordStatus.ACTIVE);
            //end - assign default values

            poEntity.insertRow();
            poEntity.moveToCurrentRow();

            poEntity.absolute(1);

            ID = poEntity.getMetaData().getColumnLabel(1);
            
            poPattern = new TabulationModels(poGRider).BingoPattern();

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
    
    public JSONObject setPatternId(String id) {
        return setValue("sPatternx", id);
    }

    public String getPatternId() {
        return (String) getValue("sPatternx");
    }
    
    public JSONObject setRemarks(String remarks){
        return setValue("sRemarksx", remarks);
    }

    public String getRemarks() {
        return (String) getValue("sRemarksx");
    }
    
    public JSONObject setRecordStatus(String recordStatus){
        return setValue("cRecdStat", recordStatus);
    }

    public String getRecordStatus() {
        return (String) getValue("cRecdStat");
    }
    
    public JSONObject setModifiedBy(String modifiedBy){
        return setValue("sModified", modifiedBy);
    }

    public String getModifiedBy() {
        return (String) getValue("sModified");
    }
    
    public JSONObject setModifiedDate(Date modifiedDate){
        return setValue("dModified", modifiedDate);
    }
    
    public Date getModifiedDate(){
        return (Date) getValue("dModified");
    }
    
    public Model_Bingo_Pattern Pattern() throws SQLException, GuanzonException{
        if (!"".equals((String) getValue("sPatternx"))){
            if (poPattern.getEditMode() == EditMode.READY && 
                poPattern.getPatternId().equals((String) getValue("sPatternx")))
                return poPattern;
            else{
                poJSON = poPattern.openRecord((String) getValue("sPatternx"));

                if ("success".equals((String) poJSON.get("result")))
                    return poPattern;
                else {
                    poPattern.initialize();
                    return poPattern;
                }
            }
        } else {
            poPattern.initialize();
            return poPattern;
        }
    }
    
    @Override
    public String getNextCode(){
        return MiscUtil.getNextCode(getTable(), "sTransNox", false, poGRider.getGConnection().getConnection(), "");
    }
}
