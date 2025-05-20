import java.sql.SQLException;
import org.guanzon.appdriver.base.GRiderCAS;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.MiscUtil;
import org.json.simple.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import ph.com.guanzongroup.gtabulate.BingoPattern;
import ph.com.guanzongroup.gtabulate.model.services.TabulationControllers;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class testBingoPattern {
    static GRiderCAS instance;
    static BingoPattern record;

    @BeforeClass
    public static void setUpClass() {
        String path;
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            path = "D:/GGC_Maven_Systems";
        } else {
            path = "/srv/GGC_Maven_Systems";
        }
        System.setProperty("sys.default.path.config", path);
        System.setProperty("sys.default.path.images", path + "/images/");
        System.setProperty("sys.default.path.metadata", path + "/config/metadata/tabulation/");

        instance = MiscUtil.Connect();
        
        try {
            record = new TabulationControllers(instance, null).BingoPattern();
            record.initialize();
        } catch (SQLException | GuanzonException e) {
            System.err.println(MiscUtil.getException(e));
            Assert.fail();
        }
        
    }

    @Test
    public void testNewRecord() {
        try {
            JSONObject loJSON;

            loJSON = record.newRecord();
            if ("error".equals((String) loJSON.get("result"))) {
                Assert.fail((String) loJSON.get("message"));
            }           

            loJSON = record.getModel().setDescription("Y Pattern");
            if ("error".equals((String) loJSON.get("result"))) {
                Assert.fail((String) loJSON.get("message"));
            }     
            
            loJSON = record.getModel().setImagePath("YPattern.png");
            if ("error".equals((String) loJSON.get("result"))) {
                Assert.fail((String) loJSON.get("message"));
            }     

//            loJSON = record.getModel().setModifyingId(instance.getUserID());
//            if ("error".equals((String) loJSON.get("result"))) {
//                Assert.fail((String) loJSON.get("message"));
//            }     
//
//            loJSON = record.getModel().setModifiedDate(instance.getServerDate());
//            if ("error".equals((String) loJSON.get("result"))) {
//                Assert.fail((String) loJSON.get("message"));
//            }     

            loJSON = record.saveRecord();
            if ("error".equals((String) loJSON.get("result"))) {
                Assert.fail((String) loJSON.get("message"));
            }  
        } catch (SQLException | GuanzonException | CloneNotSupportedException e) {
            Assert.fail(e.getMessage());
        }
    }
        
    @AfterClass
    public static void tearDownClass() {
        record = null;
        instance = null;
    }
}
