package ph.com.guanzongroup.gtabulate.model.services;

import java.sql.SQLException;
import org.guanzon.appdriver.base.GRiderCAS;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.LogWrapper;
import ph.com.guanzongroup.gtabulate.BingoPattern;
import ph.com.guanzongroup.gtabulate.Scoring;

public class TabulationControllers {
    public TabulationControllers(GRiderCAS applicationDriver, LogWrapper logWrapper){
        poGRider = applicationDriver;
        poLogWrapper = logWrapper;
    }
    
    public Scoring Scoring(){
        if (poGRider == null){
            poLogWrapper.severe("TabulationControllers.Scoring: Application driver is not set.");
            return null;
        }
        
        if (poScoring != null) return poScoring;
        
        poScoring = new Scoring();
        poScoring.setApplicationDriver(poGRider);
        poScoring.setBranchCode(poGRider.getBranchCode());
        poScoring.setVerifyEntryNo(true);
        poScoring.setWithParent(false);
        poScoring.setLogWrapper(poLogWrapper);
        return poScoring;        
    }
    
    public BingoPattern BingoPattern() throws SQLException, GuanzonException{
        if (poGRider == null){
            poLogWrapper.severe("TabulationControllers.BingoPattern: Application driver is not set.");
            return null;
        }
        
        if (poPattern != null) return poPattern;
        
        poPattern = new BingoPattern();
        poPattern.setApplicationDriver(poGRider);
        poPattern.setWithParentClass(false);
        poPattern.setLogWrapper(poLogWrapper);
        poPattern.initialize();
        poPattern.newRecord();
        return poPattern;        
    }
    
    @Override
    protected void finalize() throws Throwable {
        try {
            poScoring = null;
            poPattern = null;
                    
            poLogWrapper = null;
            poGRider = null;
        } finally {
            super.finalize();
        }
    }
    
    private GRiderCAS poGRider;
    private LogWrapper poLogWrapper;
    
    private Scoring poScoring;
    private BingoPattern poPattern;
}
