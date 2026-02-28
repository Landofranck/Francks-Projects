package project.adapter.in.web.Reducer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.adapter.in.web.Reducer.ReducerDto.*;
import project.adapter.in.web.Utils.IdDto;
import project.adapter.in.web.Utils.Mapper.ReducerDtoMapper;
import project.application.port.in.Reducer.*;
import project.application.port.out.Reducer.DeleteReducerByIdPort;
import project.domain.model.Enums.BrokerType;
import project.domain.model.Money;

import java.util.ArrayList;

@ApplicationScoped
public class ReducerServiceAdapter {

    @Inject
    CreateNewReducerUseCase createNewReducer;
    @Inject
    LoadReducerByIdUseCase loadReducerById;
    @Inject
    AddMatchToReducerUseCase addMatchToReducer;
    @Inject
    ComputeCombinationUseCase computeCombinations;
    @Inject
    DeleteMatchFromReducerUseCase deleteMatchFromReducer;
    @Inject
    PlaceBetFromReducerUseCase placeBetFromReducer;
    @Inject
    RefreshReducerUseCase refreshReducerUseCase;
    @Inject
    LoadAllReducdersUseCase loadAllReducers;
    @Inject
    UpdateReducerStakeUseCase updateReducerStakeUseCase;
    @Inject
    DeleteReducerByIdPort deleteReducerById;
    @Inject
    LoadSummaryReducerUseCase loadSummaryReducerUseCase;
    @Inject
    CreateReducerSummaryUseCase createReducerSummaryUseCase;
    @Inject
    ComuteReducerSummaryUseCase comuteReducerSummaryUseCase;
    @Inject
    ClearAllMatchesFromReducerUseCase clearAllMatchesFromReducer;
    @Inject
    AddAllMatchesToReducerUseCase addAllMatchesToReducerUseCase;
    @Inject
    ShuffelAllReducerOutcomesInSummaryUseCase shuffelAllReducerOutcomesInSummaryUseCase;
    @Inject
    ShuffleReducerMatchesUseCase shuffleReducerMatches;
    @Inject
    DeleteReducerSummaryUseCase deleteReducerSummaryUseCase;
    @Inject
    LoadallReducerSummaryUseCase getallReducerSummary;
    @Inject
    AddReducerToSummaryUseCase addReducerToSummary;
    @Inject
    ReducerDtoMapper mapper;

    public Long createReducer(CreateReducerDto dto) {
        var dom = mapper.toReducerDomain(dto);
        return createNewReducer.createNewReducer(dom);

    }

    public ReadReducerDto loadReducer(Long id) {
        var out = loadReducerById.loadReducer(id);
        return mapper.toReducerDto(out);
    }

    public ReadReducerDto addMatchToReducer(Long reducerId, IdDto match) {
        return mapper.toReducerDto(addMatchToReducer.addMatchToReducer(reducerId, match.Id()));
    }

    public ReadReducerDto getComputeCombinations(Long id, ComputeDto specifications) {
        var rule = mapper.toListOfBlocks(specifications);
        var comp = computeCombinations.computeCombination(id, rule);
        var out = mapper.toReducerDto(comp);
        return out;
    }

    public void deletMatchFromReducer(Long id, IdDto matchId) {
        deleteMatchFromReducer.deletMatchFromReducer(id, matchId.Id());
    }

    public ReadReducerDto placeReducerBet(Long reducerId, Integer index, ReducerPlaceBetDto dto) {
        return mapper.toReducerDto(placeBetFromReducer.placeBetFromReducer(reducerId, dto.bettingId(), index, new Money(dto.stake()), dto.bonusSlip()));
    }

    public ReadReducerDto refreshReducer(Long id) {
        return mapper.toReducerDto(refreshReducerUseCase.refreshReducerById(id));

    }

    public void deleteReducer(Long id) {
        deleteReducerById.deleteReducerById(id);
    }

    public GetAllReducerDto getAllReducers(BrokerType broker) {
        return new GetAllReducerDto(mapper.toReducerDomains(loadAllReducers.loadReducers(broker)), new ArrayList<>());
    }

    public void updateStake(Long id, UpdateReducerStakeDto update) {
        updateReducerStakeUseCase.updateReducerStake(id, update.stake(), update.bonusStake());
    }

    public ReadReducerSummaryDto getReducerSummary(Long summaryId) {
        return mapper.toReducerSummaryDto(loadSummaryReducerUseCase.loadReducer(summaryId));
    }

    public Long createReducerSummary(CreateReducerSummaryDto dto) {
        return createReducerSummaryUseCase.createReducerSummary(mapper.toReducerSummaryDomain(dto));
    }

    public ReadReducerSummaryDto computeReducerSummarySlips(Long summaryId, ComputeDto specifications) {
        comuteReducerSummaryUseCase.comuteReducerSummary(summaryId, mapper.toListOfBlocks(specifications));
        return getReducerSummary(summaryId);
    }

    public void deleteReducerSummary(Long reducerId) {
        deleteReducerSummaryUseCase.deleteReducerSummary(reducerId);
    }

    public ReadReducerDto clearAllReducerMatches(Long reducerId) {
        clearAllMatchesFromReducer.clearAllMatchesFromReducer(reducerId);
        return loadReducer(reducerId);
    }

    public ReadReducerDto AddAllMatchesToReducer(Long reducerId, AddAllMatchesDto dto) {
        addAllMatchesToReducerUseCase.addMatchesToReducer(reducerId, dto.matchIds());
        return loadReducer(reducerId);
    }

    public ReadReducerSummaryDto shuffleAllMatches(Long reducerId, Integer index) {
        shuffelAllReducerOutcomesInSummaryUseCase.shuffleAllReducerOutcomes(reducerId, index);
        return getReducerSummary(reducerId);
    }

    public ReadReducerDto shuffleMatches(Long reducerId, Integer index) {
        shuffleReducerMatches.shuffle(reducerId, index);
        return loadReducer(reducerId);
    }

    public AllReducerSummaryDto getAllReducerSummaries() {
        var out = getallReducerSummary.loadAllReducerSummary();
        return new AllReducerSummaryDto(mapper.toListReducerSummaryDtos(out), new ArrayList<>());
    }

    public ReadReducerSummaryDto addReducerToSummary(Long summaryId, Long reducerId) {
           addReducerToSummary.addReducerToSummary(summaryId, reducerId);
       return getReducerSummary(summaryId);
    }
}
