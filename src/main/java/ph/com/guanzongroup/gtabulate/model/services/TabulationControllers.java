package ph.com.guanzongroup.gtabulate.model.services;

import java.sql.SQLException;
import org.guanzon.appdriver.base.GRiderCAS;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.LogWrapper;
import ph.com.guanzongroup.gtabulate.Bingo;
import ph.com.guanzongroup.gtabulate.BingoPattern;
import ph.com.guanzongroup.gtabulate.ContestMaster;
import ph.com.guanzongroup.gtabulate.Scoring;

public class TabulationControllers {

    public TabulationControllers(GRiderCAS applicationDriver, LogWrapper logWrapper) {
        poGRider = applicationDriver;
        poLogWrapper = logWrapper;
    }

    public Scoring Scoring() {
        if (poGRider == null) {
            poLogWrapper.severe("TabulationControllers.Scoring: Application driver is not set.");
            return null;
        }

        if (poScoring != null) {
            return poScoring;
        }

        poScoring = new Scoring();
        poScoring.setApplicationDriver(poGRider);
        poScoring.setBranchCode(poGRider.getBranchCode());
        poScoring.setVerifyEntryNo(false);
        poScoring.setWithParent(false);
        poScoring.setLogWrapper(poLogWrapper);
        return poScoring;
    }

    public BingoPattern BingoPattern() throws SQLException, GuanzonException {
        if (poGRider == null) {
            poLogWrapper.severe("TabulationControllers.BingoPattern: Application driver is not set.");
            return null;
        }

        if (poPattern != null) {
            return poPattern;
        }

        poPattern = new BingoPattern();
        poPattern.setApplicationDriver(poGRider);
        poPattern.setWithParentClass(false);
        poPattern.setLogWrapper(poLogWrapper);
        poPattern.initialize();
        poPattern.newRecord();
        return poPattern;
    }

    public ContestMaster ContestMaster() throws  GuanzonException, SQLException {
        if (poGRider == null) {
            poLogWrapper.severe("TabulationControllers.BingoPattern: Application driver is not set.");
            return null;
        }

        if (poContestMaster != null) {
            return poContestMaster;
        }

        poContestMaster = new ContestMaster();
        poContestMaster.setApplicationDriver(poGRider);
        poContestMaster.setWithParentClass(false);
        poContestMaster.setLogWrapper(poLogWrapper);
        poContestMaster.initialize();
        poContestMaster.newRecord();
        return poContestMaster;
    }

    public Bingo Bingo() {
        if (poGRider == null) {
            poLogWrapper.severe("TabulationControllers.Bingo: Application driver is not set.");
            return null;
        }

        if (poBingo != null) {
            return poBingo;
        }

        poBingo = new Bingo();
        poBingo.setApplicationDriver(poGRider);
        poBingo.setBranchCode(poGRider.getBranchCode());
        poBingo.setVerifyEntryNo(true);
        poBingo.setWithParent(false);
        poBingo.setLogWrapper(poLogWrapper);
        return poBingo;
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
    private ContestMaster poContestMaster;
    private Bingo poBingo;
}
