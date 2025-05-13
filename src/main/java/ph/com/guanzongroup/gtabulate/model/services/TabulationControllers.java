package ph.com.guanzongroup.gtabulate.model.services;

import org.guanzon.appdriver.base.GRiderCAS;
import org.guanzon.appdriver.base.LogWrapper;
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
    
    @Override
    protected void finalize() throws Throwable {
        try {
            poScoring = null;
                    
            poLogWrapper = null;
            poGRider = null;
        } finally {
            super.finalize();
        }
    }
    
    private GRiderCAS poGRider;
    private LogWrapper poLogWrapper;
    
    private Scoring poScoring;
}
