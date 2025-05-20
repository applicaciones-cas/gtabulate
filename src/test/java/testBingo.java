
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
import ph.com.guanzongroup.gtabulate.Bingo;
import ph.com.guanzongroup.gtabulate.model.Model_Bingo_Detail;
import ph.com.guanzongroup.gtabulate.model.services.TabulationControllers;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class testBingo {

    static GRiderCAS instance;
    static Bingo trans;
    static String poTransaction;

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

        trans = new TabulationControllers(instance, null).Bingo();

    }

    @Test
    public void test01SetBingoNo() {
        System.out.println("===== Running test01SetBingoNo =====");
        JSONObject loJSON;

        try {
            loJSON = trans.openTransaction("");

            if (!"success".equals((String) loJSON.get("result"))) {
                System.err.println((String) loJSON.get("message"));
                Assert.fail();
            }
            poTransaction = trans.getMaster().getTransactionNo();
            //set detail draw
            trans.getDetail(0).setBingoNo(15);
            System.out.println("Record Transaction No to: " + poTransaction);

            System.out.println("Set Bingo No to: " + String.valueOf(trans.getDetail(0).getBingoNo()));

            loJSON = trans.saveTransaction();
            if (!"success".equals((String) loJSON.get("result"))) {
                System.err.println((String) loJSON.get("message"));
                Assert.fail();
            }

        } catch (CloneNotSupportedException | SQLException | ExceptionInInitializerError | GuanzonException e) {
            System.err.println(MiscUtil.getException(e));
            Assert.fail();
        }
    }

    @Test
    public void test02SetBingoNo() {
        System.out.println("===== Running test02SetBingoNo =====");
        JSONObject loJSON;

        try {
            loJSON = trans.openTransaction(poTransaction);

            if (!"success".equals((String) loJSON.get("result"))) {
                System.err.println((String) loJSON.get("message"));
                Assert.fail();
            }

            //set detail draw
            trans.getDetail(1).setBingoNo(30);
            System.out.println("Set Bingo No to: " + String.valueOf(trans.getDetail(trans.getDetailCount() - 1).getBingoNo()));

            loJSON = trans.saveTransaction();
            if (!"success".equals((String) loJSON.get("result"))) {
                System.err.println((String) loJSON.get("message"));
                Assert.fail();
            }

        } catch (CloneNotSupportedException | SQLException | ExceptionInInitializerError | GuanzonException e) {
            System.err.println(MiscUtil.getException(e));
            Assert.fail();
        }
    }

    @Test
    public void test03SetBingoNo() {
        System.out.println("===== Running test03SetBingoNo =====");
        JSONObject loJSON;

        try {
            loJSON = trans.openTransaction(poTransaction);

            if (!"success".equals((String) loJSON.get("result"))) {
                System.err.println((String) loJSON.get("message"));
                Assert.fail();
            }

            //set detail draw
            trans.getDetail(2).setBingoNo(45);
            System.out.println("Set Bingo No to: " + String.valueOf(trans.getDetail(2).getBingoNo()));

            loJSON = trans.saveTransaction();
            if (!"success".equals((String) loJSON.get("result"))) {
                System.err.println((String) loJSON.get("message"));
                Assert.fail();
            }

        } catch (CloneNotSupportedException | SQLException | ExceptionInInitializerError | GuanzonException e) {
            System.err.println(MiscUtil.getException(e));
            Assert.fail();
        }
    }

    @Test
    public void test04SetBingoNo() {
        System.out.println("===== Running test04SetBingoNo =====");
        JSONObject loJSON;

        try {
            loJSON = trans.openTransaction(poTransaction);

            if (!"success".equals((String) loJSON.get("result"))) {
                System.err.println((String) loJSON.get("message"));
                Assert.fail();
            }

            //set detail draw
            trans.getDetail(3).setBingoNo(60);
            System.out.println("Set Bingo No to: " + String.valueOf(trans.getDetail(3).getBingoNo()));

            loJSON = trans.saveTransaction();
            if (!"success".equals((String) loJSON.get("result"))) {
                System.err.println((String) loJSON.get("message"));
                Assert.fail();
            }

        } catch (CloneNotSupportedException | SQLException | ExceptionInInitializerError | GuanzonException e) {
            System.err.println(MiscUtil.getException(e));
            Assert.fail();
        }
    }

    @Test
    public void test05SetBingoNoReplace() {
        System.out.println("===== Running test05SetBingoNoReplace =====");
        JSONObject loJSON;

        try {
            loJSON = trans.openTransaction(poTransaction);

            if (!"success".equals((String) loJSON.get("result"))) {
                System.err.println((String) loJSON.get("message"));
                Assert.fail();
            }

            //set detail draw
            trans.getDetail(1).setBingoNo(75);
            System.out.println("Set Bingo No to: " + String.valueOf(trans.getDetail(1).getBingoNo()));

            loJSON = trans.saveTransaction();
            if (!"success".equals((String) loJSON.get("result"))) {
                System.err.println((String) loJSON.get("message"));
                Assert.fail();
            }

        } catch (CloneNotSupportedException | SQLException | ExceptionInInitializerError | GuanzonException e) {
            System.err.println(MiscUtil.getException(e));
            Assert.fail();
        }
    }

    @Test
    public void test06LoadTransaction() {
        System.out.println("===== Running test06LoadTransaction =====");
        JSONObject loJSON;

        try {
            //pass an empty transaction 
            loJSON = trans.openTransaction(poTransaction);

            if (!"success".equals((String) loJSON.get("result"))) {
                System.err.println((String) loJSON.get("message"));
                Assert.fail();
            }

            int lnBingoDetail = trans.getDetailCount();

            //display total number of criteria for judging
            System.out.println("Bingo Detail count: " + lnBingoDetail);

            //display participants info
            for (int lnCtr = 0; lnCtr <= lnBingoDetail - 1; lnCtr++) {
                Model_Bingo_Detail loDetail = trans.getDetail(lnCtr);
                System.out.println("Transaction No: " + loDetail.getTransactionNo());
                System.out.println("Entry No: " + loDetail.getEntryNo());
                System.out.println("Draw No: " + loDetail.getBingoNo());
            }

            //dispay participants score
            System.out.println("-----");

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
