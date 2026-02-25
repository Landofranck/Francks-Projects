package project.application.port.out.bettingAccount;

import project.domain.model.Enums.League;
import project.domain.model.Enums.MatchKey;
import project.domain.model.MatchOutComePick;

import java.util.List;

public interface FindSlipEventOutComeByParamPort {
    List<MatchOutComePick> findSlipEventEntity(String ownerMatchName,MatchKey matchKey, String outcomeName, League league);
}
