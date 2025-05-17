package ph.com.guanzongroup.gtabulate.model.services;

import org.guanzon.appdriver.base.GRiderCAS;
import ph.com.guanzongroup.gtabulate.model.Model_Bingo_Detail;
import ph.com.guanzongroup.gtabulate.model.Model_Bingo_Master;
import ph.com.guanzongroup.gtabulate.model.Model_Bingo_Pattern;
import ph.com.guanzongroup.gtabulate.model.Model_Contest_Master;
import ph.com.guanzongroup.gtabulate.model.Model_Contest_Meta;
import ph.com.guanzongroup.gtabulate.model.Model_Contest_Participants;
import ph.com.guanzongroup.gtabulate.model.Model_Contest_Participants_Meta;
import ph.com.guanzongroup.gtabulate.model.Model_Event_Criteria;
import ph.com.guanzongroup.gtabulate.model.Model_Event_Performing;
import ph.com.guanzongroup.gtabulate.model.Model_Event_Results;
import ph.com.guanzongroup.gtabulate.model.Model_Event_Tabulation;
import ph.com.guanzongroup.gtabulate.model.Model_Event_Tabulation_Detail;
import ph.com.guanzongroup.gtabulate.model.Model_Events;

public class TabulationModels {
    public TabulationModels(GRiderCAS applicationDriver){
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
    
    public Model_Contest_Participants Participants(){
        if (poGRider == null){
            System.err.println("ParamTabulate.Participants: Application driver is not set.");
            return null;
        }
        
        if (poParticipants == null){
            poParticipants = new Model_Contest_Participants();
            poParticipants.setApplicationDriver(poGRider);
            poParticipants.setXML("Model_Contest_Participants");
            poParticipants.setTableName("Contest_Participants");
            poParticipants.initialize();
        }

        return poParticipants;
    }
    
    public Model_Contest_Participants_Meta ParticipantsMeta(){
        if (poGRider == null){
            System.err.println("ParamTabulate.ParticipantsMeta: Application driver is not set.");
            return null;
        }
        
        if (poParticipantsMeta == null){
            poParticipantsMeta = new Model_Contest_Participants_Meta();
            poParticipantsMeta.setApplicationDriver(poGRider);
            poParticipantsMeta.setXML("Model_Contest_Participants_Meta");
            poParticipantsMeta.setTableName("Contest_Participants_Meta");
            poParticipantsMeta.initialize();
        }

        return poParticipantsMeta;
    }
    
    public Model_Contest_Meta Meta(){
        if (poGRider == null){
            System.err.println("ParamTabulate.ParticipantsMeta: Application driver is not set.");
            return null;
        }
        
        if (poMeta == null){
            poMeta = new Model_Contest_Meta();
            poMeta.setApplicationDriver(poGRider);
            poMeta.setXML("Model_Contest_Meta");
            poMeta.setTableName("Contest_Meta");
            poMeta.initialize();
        }

        return poMeta;
    }
    
    public Model_Event_Criteria Criteria(){
        if (poGRider == null){
            System.err.println("ParamTabulate.Criteria: Application driver is not set.");
            return null;
        }
        
        if (poCriteria == null){
            poCriteria = new Model_Event_Criteria();
            poCriteria.setApplicationDriver(poGRider);
            poCriteria.setXML("Model_Event_Criteria");
            poCriteria.setTableName("Event_Criteria");
            poCriteria.initialize();
        }

        return poCriteria;
    }
    
    public Model_Event_Performing Performing(){
        if (poGRider == null){
            System.err.println("ParamTabulate.Performing: Application driver is not set.");
            return null;
        }
        
        if (poPerforming == null){
            poPerforming = new Model_Event_Performing();
            poPerforming.setApplicationDriver(poGRider);
            poPerforming.setXML("Model_Event_Performing");
            poPerforming.setTableName("Event_Performing");
            poPerforming.initialize();
        }

        return poPerforming;
    }
    
    public Model_Event_Results Results(){
        if (poGRider == null){
            System.err.println("ParamTabulate.Results: Application driver is not set.");
            return null;
        }
        
        if (poResults == null){
            poResults = new Model_Event_Results();
            poResults.setApplicationDriver(poGRider);
            poResults.setXML("Model_Event_Results");
            poResults.setTableName("Event_Results");
            poResults.initialize();
        }

        return poResults;
    }
    
    public Model_Event_Tabulation Tabulation(){
        if (poGRider == null){
            System.err.println("ParamTabulate.Tabulation: Application driver is not set.");
            return null;
        }
        
        if (poTabulation == null){
            poTabulation = new Model_Event_Tabulation();
            poTabulation.setApplicationDriver(poGRider);
            poTabulation.setXML("Model_Event_Tabulation");
            poTabulation.setTableName("Event_Tabulation");
            poTabulation.initialize();
        }

        return poTabulation;
    }
    
    public Model_Event_Tabulation_Detail TabulationDetail(){
        if (poGRider == null){
            System.err.println("ParamTabulate.TabulationDetail: Application driver is not set.");
            return null;
        }
        
        if (poTabulationDetil == null){
            poTabulationDetil = new Model_Event_Tabulation_Detail();
            poTabulationDetil.setApplicationDriver(poGRider);
            poTabulationDetil.setXML("Model_Event_Tabulation_Detail");
            poTabulationDetil.setTableName("Event_Tabulation_Detail");
            poTabulationDetil.initialize();
        }

        return poTabulationDetil;
    }
    
    public Model_Bingo_Pattern BingoPattern(){
        if (poGRider == null){
            System.err.println("ParamTabulate.BingoPattern: Application driver is not set.");
            return null;
        }
        
        if (poBingoPattern == null){
            poBingoPattern = new Model_Bingo_Pattern();
            poBingoPattern.setApplicationDriver(poGRider);
            poBingoPattern.setXML("Model_Bingo_Patterns");
            poBingoPattern.setTableName("Bingo_Patterns");
            poBingoPattern.initialize();
        }

        return poBingoPattern;
    }
    
    public Model_Bingo_Master BingoMaster(){
        if (poGRider == null){
            System.err.println("ParamTabulate.BingoMaster: Application driver is not set.");
            return null;
        }
        
        if (poBingoMaster == null){
            poBingoMaster = new Model_Bingo_Master();
            poBingoMaster.setApplicationDriver(poGRider);
            poBingoMaster.setXML("Model_Bingo_Master");
            poBingoMaster.setTableName("Bingo_Master");
            poBingoMaster.initialize();
        }

        return poBingoMaster;
    }
    
    public Model_Bingo_Detail BingoDetail(){
        if (poGRider == null){
            System.err.println("ParamTabulate.BingoDetail: Application driver is not set.");
            return null;
        }
        
        if (poBingoDetail == null){
            poBingoDetail = new Model_Bingo_Detail();
            poBingoDetail.setApplicationDriver(poGRider);
            poBingoDetail.setXML("Model_Bingo_Detail");
            poBingoDetail.setTableName("Bingo_Detail");
            poBingoDetail.initialize();
        }

        return poBingoDetail;
    }
    
    private final GRiderCAS poGRider;
    
    private Model_Events poEvents;
    private Model_Event_Criteria poCriteria;
    private Model_Event_Performing poPerforming;
    private Model_Event_Results poResults;
    private Model_Event_Tabulation poTabulation;
    private Model_Event_Tabulation_Detail poTabulationDetil;
    private Model_Contest_Meta poMeta;
    private Model_Contest_Master poContestMaster;
    private Model_Contest_Participants poParticipants;
    private Model_Contest_Participants_Meta poParticipantsMeta;
    private Model_Bingo_Pattern poBingoPattern;
    private Model_Bingo_Master poBingoMaster;
    private Model_Bingo_Detail poBingoDetail;
}