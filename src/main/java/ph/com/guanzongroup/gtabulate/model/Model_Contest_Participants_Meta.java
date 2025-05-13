package ph.com.guanzongroup.gtabulate.model;

import java.sql.SQLException;
import org.guanzon.appdriver.agent.services.Model;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.json.simple.JSONObject;
import ph.com.guanzongroup.gtabulate.model.services.TabulationModels;

public class Model_Contest_Participants_Meta extends Model {
    Model_Contest_Meta poMeta;
    
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

            ID = "sGroupIDx";
            ID2 = "sMetaIDxx";
            
            poMeta = new TabulationModels(poGRider).Meta();
            
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
    
    public JSONObject setMetaId(String id) {
        return setValue("sMetaIDxx", id);
    }

    public String getMetaId() {
        return (String) getValue("sMetaIDxx");
    }
       
    public JSONObject setValue(String name) {
        return setValue("sValuexxx", name);
    }

    public String getValue() {
        return (String) getValue("sValuexxx");
    }
            
    public Model_Contest_Meta Meta() throws SQLException, GuanzonException{
        if (!"".equals((String) getValue("sMetaIDxx"))){
            if (poMeta.getEditMode() == EditMode.READY && 
                poMeta.getMetaId().equals((String) getValue("sMetaIDxx")))
                return poMeta;
            else{
                poJSON = poMeta.openRecord((String) getValue("sMetaIDxx"));

                if ("success".equals((String) poJSON.get("result")))
                    return poMeta;
                else {
                    poMeta.initialize();
                    return poMeta;
                }
            }
        } else {
            poMeta.initialize();
            return poMeta;
        }
    }
}
