package ph.com.guanzongroup.gtabulate.model.services;

import org.guanzon.appdriver.base.GRiderCAS;
import ph.com.guanzongroup.gtabulate.model.Model_Contest_Master;
import ph.com.guanzongroup.gtabulate.model.Model_Events;

public class ParamTabulate {
    public ParamTabulate(GRiderCAS applicationDriver){
        poGRider = applicationDriver;
    }
    
    public Model_Events Events(){
        if (poGRider == null){
            System.err.println("ParamTabulate.Events: Application driver is not set.");
            return null;
        }
        
        if (poEvents == null){
            poEvents = new Model_Events();
            poEvents.setApplicationDriver(poGRider);
            poEvents.setXML("Model_Events");
            poEvents.setTableName("Events");
            poEvents.initialize();
        }

        return poEvents;
    }
    
    public Model_Contest_Master ContestMaster(){
        if (poGRider == null){
            System.err.println("ParamTabulate.Events: Application driver is not set.");
            return null;
        }
        
        if (poContestMaster == null){
            poContestMaster = new Model_Contest_Master();
            poContestMaster.setApplicationDriver(poGRider);
            poContestMaster.setXML("Model_Contest_Master");
            poContestMaster.setTableName("Contest_Master");
            poContestMaster.initialize();
        }

        return poContestMaster;
    }
    
    private final GRiderCAS poGRider;
    
    private Model_Events poEvents;
    private Model_Contest_Master poContestMaster;
}
