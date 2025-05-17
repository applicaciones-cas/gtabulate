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
import ph.com.guanzongroup.gtabulate.Scoring;
import ph.com.guanzongroup.gtabulate.model.Model_Contest_Participants;
import ph.com.guanzongroup.gtabulate.model.Model_Contest_Participants_Meta;
import ph.com.guanzongroup.gtabulate.model.Model_Event_Criteria;
import ph.com.guanzongroup.gtabulate.model.services.TabulationControllers;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class testScoring {
    static GRiderCAS instance;
    static Scoring trans;

    @BeforeClass
    public static void setUpClass() {
        String path;
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            path = "D:/GGC_Maven_Systems";
        } else {
            path = "/srv/GGC_Maven_Systems";
        }
        System.setProperty("sys.default.path.config", path);
        System.setProperty("sys.default.path.images", path + "/images");
        System.setProperty("sys.default.path.metadata", path + "/config/metadata/tabulation/");

        instance = MiscUtil.Connect();
        
        trans = new TabulationControllers(instance, null).Scoring();
        trans.setVerifyEntryNo(false);
    }

    @Test
    public void test01LoadParticipants() {        
        JSONObject loJSON;
        
        try {
            //get terminal id
            System.out.println("Terminal No.: " + instance.getTerminalNo());
            
            //pass the contest id
            trans.setContestId("00001");
            //pass the judge name
            trans.setJudgeName("Michael Cuison");
            
            loJSON = trans.initTransaction();
            
            if (!"success".equals((String) loJSON.get("result"))){
                System.err.println((String) loJSON.get("message"));
                Assert.fail();
            }
            
            loJSON = trans.loadCriteriaForJudging();
            
            if (!"success".equals((String) loJSON.get("result"))){
                System.err.println((String) loJSON.get("message"));
                Assert.fail();
            }
            
            int lnCriteria = trans.getCriteriaCount();
            
            //display total number of criteria for judging
            System.out.println("Criteria for judging count: " + lnCriteria);
            
            //display participants info
            for (int lnCtr = 0; lnCtr <= lnCriteria - 1; lnCtr++){
                Model_Event_Criteria loCriteria = trans.Criteria(lnCtr);
                System.out.println("Criteria No: " + loCriteria.getEntryNo());
                System.out.println("Criteria Name: " + loCriteria.getDescription());
                System.out.println("Criteria Percentage: " + String.valueOf(loCriteria.getPercentage()));
            }
            
            loJSON = trans.loadParticipants();
            
            if (!"success".equals((String) loJSON.get("result"))){
                System.err.println((String) loJSON.get("message"));
                Assert.fail();
            }
            
            int lnParticipants = trans.getParticipantsCount();
            
            //display total number of participants
            System.out.println("Participants count: " + lnParticipants);
            
            //display participants info
            for (int lnCtr = 0; lnCtr <= lnParticipants - 1; lnCtr++){
                //display participant info
                Model_Contest_Participants loPaticipants = trans.Participant(lnCtr);
                System.out.println("Participant ID: " + loPaticipants.getGroupId());
                System.out.println("Participant No.: " + loPaticipants.getEntryNo());
                System.out.println("Participant Name: " + loPaticipants.getEntryName());
                System.out.println("No. of Members: " + loPaticipants.getMembers());
                
                //get participant school/town name
                Model_Contest_Participants_Meta loMeta = trans.ParticipantMeta(loPaticipants.getGroupId(), "00002");
                if (loMeta != null){
                    System.out.println("School/Town Name: " + loMeta.getValue());
                } else {
                    System.err.println("Unable to load participant meta. --> " + loPaticipants.getGroupId());
                    Assert.fail();
                }
                
                //get participant picture name
                loMeta = trans.ParticipantMeta(loPaticipants.getGroupId(), "00003");
                if (loMeta != null){
                    System.out.println("Picture location: " + System.getProperty("sys.default.path.images") + loMeta.getValue().substring(45));
                } else {
                    System.err.println("Unable to load participant meta. --> " + loPaticipants.getGroupId());
                    Assert.fail();
                }
                
                //dispay participants score
                System.out.println("-----");
            }
            
        } catch (CloneNotSupportedException | SQLException | ExceptionInInitializerError | GuanzonException e) {
            System.err.println(MiscUtil.getException(e));
            Assert.fail();
        }
    }
    
    @Test
    public void test02SetRating() {
        JSONObject loJSON;
        
        try {            
            loJSON = trans.openTransaction("00012", instance.getTerminalNo());
            
            if (!"success".equals((String) loJSON.get("result"))){
                System.err.println((String) loJSON.get("message"));
                Assert.fail();
            }
                        
            //set detail scores
            trans.getDetail(1).setRate(95.00);        
            System.out.println("Set score to: " + String.valueOf(trans.getDetail(1).getRate()));
            
            loJSON = trans.saveTransaction();
            if (!"success".equals((String) loJSON.get("result"))){
                System.err.println((String) loJSON.get("message"));
                Assert.fail();
            }
            
        } catch (CloneNotSupportedException | SQLException | ExceptionInInitializerError | GuanzonException e) {
            System.err.println(MiscUtil.getException(e));
            Assert.fail();
        }
    }
    
    @Test
    public void test03SetRating() {
        JSONObject loJSON;
        
        try {            
            loJSON = trans.openTransaction("00012", instance.getTerminalNo());
            
            if (!"success".equals((String) loJSON.get("result"))){
                System.err.println((String) loJSON.get("message"));
                Assert.fail();
            }
                        
            //set detail scores
            trans.getDetail(2).setRate(90.00);        
            System.out.println("Set score to: " + String.valueOf(trans.getDetail(2).getRate()));
            
            loJSON = trans.saveTransaction();
            if (!"success".equals((String) loJSON.get("result"))){
                System.err.println((String) loJSON.get("message"));
                Assert.fail();
            }
            
        } catch (CloneNotSupportedException | SQLException | ExceptionInInitializerError | GuanzonException e) {
            System.err.println(MiscUtil.getException(e));
            Assert.fail();
        }
    }
    
    @Test
    public void test04SetRating() {
        JSONObject loJSON;
        
        try {            
            loJSON = trans.openTransaction("00012", instance.getTerminalNo());
            
            if (!"success".equals((String) loJSON.get("result"))){
                System.err.println((String) loJSON.get("message"));
                Assert.fail();
            }
                        
            //set detail scores
            trans.getDetail(3).setRate(85.00);        
            System.out.println("Set score to: " + String.valueOf(trans.getDetail(3).getRate()));
            
            loJSON = trans.saveTransaction();
            if (!"success".equals((String) loJSON.get("result"))){
                System.err.println((String) loJSON.get("message"));
                Assert.fail();
            }
            
        } catch (CloneNotSupportedException | SQLException | ExceptionInInitializerError | GuanzonException e) {
            System.err.println(MiscUtil.getException(e));
            Assert.fail();
        }
    }
    
    @Test
    public void test05SetRating() {
        JSONObject loJSON;
        
        try {            
            loJSON = trans.openTransaction("00012", instance.getTerminalNo());
            
            if (!"success".equals((String) loJSON.get("result"))){
                System.err.println((String) loJSON.get("message"));
                Assert.fail();
            }
                        
            //set detail scores
            trans.getDetail(4).setRate(70.00);        
            System.out.println("Set score to: " + String.valueOf(trans.getDetail(4).getRate()));
            
            loJSON = trans.saveTransaction();
            if (!"success".equals((String) loJSON.get("result"))){
                System.err.println((String) loJSON.get("message"));
                Assert.fail();
            }
            
        } catch (CloneNotSupportedException | SQLException | ExceptionInInitializerError | GuanzonException e) {
            System.err.println(MiscUtil.getException(e));
            Assert.fail();
        }
    }
    
    @Test
    public void test06SetRating() {
        JSONObject loJSON;
        
        try {            
            loJSON = trans.openTransaction("00012", instance.getTerminalNo());
            
            if (!"success".equals((String) loJSON.get("result"))){
                System.err.println((String) loJSON.get("message"));
                Assert.fail();
            }
                        
            //set detail scores
            trans.getDetail(5).setRate(75.00);        
            System.out.println("Set score to: " + String.valueOf(trans.getDetail(5).getRate()));
            
            loJSON = trans.saveTransaction();
            if (!"success".equals((String) loJSON.get("result"))){
                System.err.println((String) loJSON.get("message"));
                Assert.fail();
            }
            
        } catch (CloneNotSupportedException | SQLException | ExceptionInInitializerError | GuanzonException e) {
            System.err.println(MiscUtil.getException(e));
            Assert.fail();
        }
    }
    
    @Test
    public void test07SetRating() {
        JSONObject loJSON;
        
        try {            
            loJSON = trans.openTransaction("00012", instance.getTerminalNo());
            
            if (!"success".equals((String) loJSON.get("result"))){
                System.err.println((String) loJSON.get("message"));
                Assert.fail();
            }
                        
            //set detail scores
            trans.getDetail(6).setRate(100.00);        
            System.out.println("Set score to: " + String.valueOf(trans.getDetail(5).getRate()));
            
            loJSON = trans.saveTransaction();
            if (!"success".equals((String) loJSON.get("result"))){
                System.err.println((String) loJSON.get("message"));
                Assert.fail();
            }
            
        } catch (CloneNotSupportedException | SQLException | ExceptionInInitializerError | GuanzonException e) {
            System.err.println(MiscUtil.getException(e));
            Assert.fail();
        }
    }
    
    @AfterClass
    public static void tearDownClass() {
        trans = null;
        instance = null;
    }
}
