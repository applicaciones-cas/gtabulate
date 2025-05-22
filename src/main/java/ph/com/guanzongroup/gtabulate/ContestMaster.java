package ph.com.guanzongroup.gtabulate;

import java.sql.SQLException;
import org.guanzon.appdriver.agent.ShowDialogFX;
import org.guanzon.appdriver.agent.services.Parameter;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.constant.Logical;
import org.guanzon.appdriver.constant.UserRight;
import org.json.simple.JSONObject;
import ph.com.guanzongroup.gtabulate.model.Model_Contest_Master;
import ph.com.guanzongroup.gtabulate.model.services.TabulationModels;

public class ContestMaster extends Parameter{
    Model_Contest_Master poModel;
    
    @Override
    public void initialize() throws SQLException, GuanzonException {
        psRecdStat = Logical.YES;
        
        poModel = new TabulationModels(poGRider).ContestMaster();
        super.initialize();
        
    }
    
    @Override
    public JSONObject isEntryOkay() throws SQLException{
        poJSON = new JSONObject();
        
        if (poGRider.getUserLevel() < UserRight.SYSADMIN){
            poJSON.put("result", "error");
            poJSON.put("message", "User is not allowed to save record.");
            return poJSON;
        } else {
            poJSON = new JSONObject();
            
            if (poModel.getDescription() == null ||poModel.getDescription().isEmpty()){
                poJSON.put("result", "error");
                poJSON.put("message", "Description must not be empty.");
                return poJSON;
            }
            
        }
        
        
        poJSON.put("result", "success");
        return poJSON;
    }
    
    @Override
    public Model_Contest_Master getModel() {
        return poModel;
    }
    
    @Override
    public JSONObject searchRecord(String value, boolean byCode) throws SQLException, GuanzonException{
        String lsSQL = getSQ_Browse();
        
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "ID»Description",
                "sContstID»sDescript",
                "sContstID»sDescript",
                byCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sContstID"));
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }
    }
}