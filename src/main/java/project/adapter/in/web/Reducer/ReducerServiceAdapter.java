package project.adapter.in.web.Reducer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.adapter.in.web.CreateReducerDto;
import project.adapter.in.web.DTOMapper;
import project.adapter.in.web.IdDto;
import project.application.port.in.DeleteMatchByIdUsecase;
import project.application.port.in.Reducer.*;
import project.domain.model.Money;

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
    DTOMapper mapper;
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
        var rule=mapper.toListOfBlocks(specifications);
        var comp= computeCombinations.computeCombination(id, rule);
        var out=mapper.toReducerDto(comp);
        return out;
    }

    public void deletMatchFromReducer(Long id, IdDto matchId) {
        deleteMatchFromReducer.deletMatchFromReducer(id,matchId.Id());
    }
    public ReadReducerDto placeReducerBet(Long reducerId, ReducerPlaceBetDto dto){
        var out =mapper.toReducerDto(placeBetFromReducer.placeBetFromReducer(reducerId,dto.bettingId(),dto.slipIndex(),new Money(dto.stake())));
        return out;
    }

}
