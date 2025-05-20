package ph.com.guanzongroup.gtabulate;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.guanzon.appdriver.base.GRiderCAS;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;

public class ComputeScore {
    public static void main(String [] args){
        GRiderCAS oApp = MiscUtil.Connect("M001250001");
        
        String lsContstID = "00001";
        
        String lsSQL = "SELECT a.*" +
                        " FROM Event_Tabulation a" +
                            ", Contest_Participants b" +
                            ", Contest_Master c" +
                        " WHERE a.sGroupIDx = b.sGroupIDx" +
                            " AND b.sContstID = c.sContstID" +
                            " AND c.sContstID = " + SQLUtil.toSQL(lsContstID) +
                        " ORDER BY sComptrID, sGroupIDx";
        
        try {
            ResultSet loRS = oApp.executeQuery(lsSQL);
        
            while(loRS.next()){
                lsSQL = "SELECT COUNT(*) FROM Event_Criteria WHERE sContstID = " + SQLUtil.toSQL(lsContstID);
                
                ResultSet loRSDetail = oApp.executeQuery(lsSQL);
                
                int lnCriteria = 0;
                if (loRSDetail.next()) lnCriteria = loRSDetail.getInt(1);
                
                lsSQL = "SELECT * FROM Event_Tabulation_Detail" +
                        " WHERE sGroupIDx = " + SQLUtil.toSQL(loRS.getString("sGroupIDx")) +
                            " AND sComptrID = " + SQLUtil.toSQL(loRS.getString("sComptrID")) +
                        " ORDER BY nEntryNox";
                
                loRSDetail = oApp.executeQuery(lsSQL);
                double lnScore = 0;
                
                while(loRSDetail.next()){
                    lnScore += loRSDetail.getDouble("nPercentx");
                }
                
                lnScore = lnScore / lnCriteria;
                
                lsSQL = "UPDATE Event_Tabulation SET nRatingsx = " + lnScore +
                        " WHERE sGroupIDx = " + SQLUtil.toSQL(loRS.getString("sGroupIDx")) +
                            " AND sComptrID = " + SQLUtil.toSQL(loRS.getString("sComptrID"));
                
                oApp.beginTrans("Tabulation", "Computation", "Tbln", loRS.getString("sGroupIDx"));
                oApp.executeQuery(lsSQL, "Event_Tabulation", oApp.getBranchCode(), "", "");
                oApp.commitTrans();
            }
            
            lsSQL = "SELECT a.sComptrID" +
                    " FROM Event_Tabulation a" +
                        ", Contest_Participants b" +
                        ", Contest_Master c" +
                    " WHERE a.sGroupIDx = b.sGroupIDx" +
                        " AND b.sContstID = c.sContstID" +
                        " AND c.sContstID = " + SQLUtil.toSQL(lsContstID) +
                    " GROUP BY a.sComptrID";
            
            loRS = oApp.executeQuery(lsSQL);
            
            while(loRS.next()){
                lsSQL = "SELECT a.*" +
                        " FROM Event_Tabulation a" +
                            ", Contest_Participants b" +
                            ", Contest_Master c" +
                        " WHERE a.sGroupIDx = b.sGroupIDx" +
                            " AND b.sContstID = c.sContstID" +
                            " AND c.sContstID = " + SQLUtil.toSQL(lsContstID) +
                            " AND a.sComptrID = " + SQLUtil.toSQL(loRS.getString("sComptrID")) +
                        " ORDER BY a.sComptrID, a.nRatingsx DESC";

                ResultSet loRSDetail = oApp.executeQuery(lsSQL);

                int lnCtr = 1;
                oApp.beginTrans("Tabulation", "Computation", "Tbln", "");
                while(loRSDetail.next()){
                    lsSQL = "UPDATE Event_Tabulation SET nRankxxxx = " + lnCtr +
                            " WHERE sGroupIDx = " + SQLUtil.toSQL(loRSDetail.getString("sGroupIDx")) +
                                " AND sComptrID = " + SQLUtil.toSQL(loRSDetail.getString("sComptrID"));
                    oApp.executeQuery(lsSQL, "Event_Tabulation", oApp.getBranchCode(), "", "");

                    lnCtr++;
                }
                oApp.commitTrans();
            }
        } catch (SQLException |GuanzonException e) {
            e.printStackTrace();
        }
    }
}
