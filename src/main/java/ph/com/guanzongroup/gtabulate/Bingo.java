package ph.com.guanzongroup.gtabulate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.guanzon.appdriver.agent.ShowDialogFX;
import org.guanzon.appdriver.agent.services.Model;
import org.guanzon.appdriver.agent.services.Transaction;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.RecordStatus;
import org.json.simple.JSONObject;
import ph.com.guanzongroup.gtabulate.model.Model_Bingo_Master;
import ph.com.guanzongroup.gtabulate.model.Model_Bingo_Detail;
import ph.com.guanzongroup.gtabulate.model.services.TabulationControllers;
import ph.com.guanzongroup.gtabulate.model.services.TabulationModels;

public class Bingo extends Transaction {

    private BingoPattern poBingoPattern;

    public BingoPattern BingoPattern() {
        if (poBingoPattern == null) {
            return null;
        }

        return poBingoPattern;
    }

    public Model_Bingo_Master getMaster() {
        return (Model_Bingo_Master) poMaster;
    }

    public Model_Bingo_Detail getDetail(int bingoNo) {
        if (getMaster().getTransactionNo().isEmpty()) {
            return null;
        }

        if (bingoNo < 0) {
            return null;
        }
        if (bingoNo > paDetail.size() - 1) {
            AddModelDetail();
        }

        Model_Bingo_Detail loDetail;

        loDetail = (Model_Bingo_Detail) paDetail.get(bingoNo);
        return loDetail;
    }

    public JSONObject initTransaction() throws SQLException, GuanzonException {
        SOURCE_CODE = "Bngo";

        poMaster = new TabulationModels(poGRider).BingoMaster();
        poDetail = new TabulationModels(poGRider).BingoDetail();
        poBingoPattern = new TabulationControllers(poGRider, logwrapr).BingoPattern();
        poBingoPattern.setRecordStatus(RecordStatus.ACTIVE);
        poBingoPattern.initialize();
        poBingoPattern.setWithParentClass(true);
        return super.initialize();
    }

    public JSONObject openTransaction(String transactiNo) throws CloneNotSupportedException, SQLException, GuanzonException {

        poJSON = poMaster.openRecord(transactiNo);
        if (!"success".equals((String) poJSON.get("result"))) {
            poJSON = newTransaction();
            if ("success".equals((String) poJSON.get("result"))) {
                return poJSON;
            }
        }
        poMaster.updateRecord();
        //loadpattern data
        String lspatternx = poMaster.getValue("sPatternx").toString();
        if (lspatternx != null && !lspatternx.isEmpty()) {
            BingoPattern loBingoPattern = new TabulationControllers(poGRider, logwrapr).BingoPattern();
            loBingoPattern.setWithParentClass(true);
            loBingoPattern.searchRecord(lspatternx, true);
            if (!"success".equals((String) poJSON.get("result"))) {
                poJSON.put("message", "Unable to open transaction detail record.");
                clear();
                return poJSON;
            }
            poBingoPattern = loBingoPattern;
        }else{
            BingoPattern loBingoPattern = new TabulationControllers(poGRider, logwrapr).BingoPattern();
            loBingoPattern.setWithParentClass(true);
            poBingoPattern =loBingoPattern;
        
        }
        paDetail.clear();

        String lsSQL = "SELECT * FROM " + poDetail.getTable()
                + " WHERE sTransNox = " + SQLUtil.toSQL(transactiNo);

        ResultSet loRS = poGRider.executeQuery(lsSQL);

        if (MiscUtil.RecordCount(loRS) > 0) {
            while (loRS.next()) {
                Model loDetail = (Model) poDetail.clone();
                loDetail.newRecord();

                poJSON = loDetail.openRecord(transactiNo, loRS.getInt("nEntryNox"));

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
        if (!pbInitTran) {
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
        if ("error".equals((String) poJSON.get("result"))) {
            return poJSON;
        }

        pnEditMode = EditMode.ADDNEW;

        poEvent = new JSONObject();
        poEvent.put("event", "ADD NEW");

        poJSON = new JSONObject();
        poJSON.put("result", "success");
        return poJSON;
    }

    @Override
    public JSONObject saveTransaction() throws CloneNotSupportedException, SQLException, GuanzonException {
        poJSON = new JSONObject();

        if (!pbInitTran) {
            poJSON.put("result", "error");
            poJSON.put("message", "Object is not initialized.");
            return poJSON;
        }

        if (pnEditMode == EditMode.READY) {
            poJSON.put("result", "error");
            poJSON.put("message", "Saving of unmodified transaction is not allowed.");
            return poJSON;
        }

        poJSON = willSave();
        if ("error".equals((String) poJSON.get("result"))) {
            return poJSON;
        }

        pdModified = poGRider.getServerDate();
        if (getEditMode() == EditMode.ADDNEW) {
            poMaster.setValue("sModified", poGRider.Encrypt(poGRider.getUserID()));
        }

        poJSON = save();

        if (!pbWthParent) {
            poGRider.beginTrans((String) poEvent.get("event"),
                    poMaster.getTable(),
                    SOURCE_CODE,
                    String.valueOf(poMaster.getValue(1)));
        }

        if ("success".equals((String) poJSON.get("result"))) {
            //save master and detail
            if (pbVerifyEntryNo) {
                poMaster.setValue("nEntryNox", paDetail.size());
            }

            if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
                poMaster.setValue("dModified", pdModified);
                poJSON = poMaster.saveRecord();

                if ("error".equals((String) poJSON.get("result"))) {
                    if (!pbWthParent) {
                        poGRider.rollbackTrans();
                    }
                    return poJSON;
                }

                for (int lnCtr = 0; lnCtr <= paDetail.size() - 1; lnCtr++) {
                    paDetail.get(lnCtr).setValue("sTransNox", String.valueOf(poMaster.getValue(1)));
                    paDetail.get(lnCtr).setValue("nEntryNox", lnCtr + 1);
                    poJSON = paDetail.get(lnCtr).saveRecord();

                    if ("error".equals((String) poJSON.get("result"))) {
                        if (!pbWthParent) {
                            poGRider.rollbackTrans();
                        }
                        return poJSON;
                    }
                }
            } else {
                poJSON.put("result", "error");
                poJSON.put("message", "Edit mode is not allowed to save transaction.");
                return poJSON;
            }
        } else {
            if (!pbWthParent) {
                poGRider.rollbackTrans();
            }

            return poJSON;
        }

        if (!pbWthParent) {
            poGRider.commitTrans();
        }

        pnEditMode = EditMode.UNKNOWN;
        pbRecordExist = true;

        poJSON = new JSONObject();
        poJSON.put("result", "success");
        poJSON.put("message", "Transaction saved successfully.");
        return poJSON;
    }

    private JSONObject AddModelDetail() {

        poDetail = new Model_Bingo_Detail();
        poDetail.setApplicationDriver(poGRider);
        poDetail.setXML("Model_Bingo_Detail");
        poDetail.setTableName("Bingo_Detail");
        poDetail.initialize();
        poDetail.newRecord();

        paDetail.add(poDetail);
        poJSON = new JSONObject();
        poJSON.put("result", "success");
        poJSON.put("message", "Transaction saved successfully.");
        return poJSON;
    }

    @Override
    public JSONObject searchTransaction(String value, boolean byCode) {
        try {
            String lsSQL = SQL_BROWSE;

            poJSON = ShowDialogFX.Search(poGRider,
                    lsSQL,
                    value,
                    "Transaction No»nEntryNox»sPatternx",
                    "sTransNox»nEntryNox»sPatternx",
                    "sTransNox»nEntryNox»sPatternx",
                    byCode ? 0 : 1);

            if (poJSON != null) {
                return openTransaction((String) poJSON.get("sTransNox"));

            } else {
                poJSON = new JSONObject();
                poJSON.put("result", "error");
                poJSON.put("message", "No record loaded.");
                return poJSON;
            }
        } catch (CloneNotSupportedException | SQLException | GuanzonException ex) {
            Logger.getLogger(Bingo.class.getName()).log(Level.SEVERE, null, ex);
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }
    }

}
