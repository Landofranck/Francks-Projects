package project.adapter.in.web.Reducer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.adapter.in.web.CreateReducerDto;
import project.adapter.in.web.DTOMapper;
import project.adapter.in.web.IdDto;
import project.adapter.in.web.ReadReducerDto;
import project.application.port.in.Reducer.AddMatchToReducerUseCase;
import project.application.port.in.Reducer.ComputeCombinationUseCase;
import project.application.port.in.Reducer.CreateNewReducerUseCase;
import project.application.port.in.Reducer.LoadReducerByIdUseCase;

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
    DTOMapper mapper;
    public void createReducer(CreateReducerDto dto) {
        var dom = mapper.toReducerDomain(dto);
        createNewReducer.createNewReducer(dom);
    }

    public ReadReducerDto loadReducer(Long id) {
        var out = loadReducerById.loadReducer(id);
        return mapper.toReducerDto(out);
    }

    public ReadReducerDto addMatchToReducer(Long reducerId, IdDto match) {
        return mapper.toReducerDto(addMatchToReducer.addMatchToReducer(reducerId, match.Id()));
    }

    public ReadReducerDto getComputeCombinations(Long id) {
        var comp= computeCombinations.computeCombination(id);
        var out=mapper.toReducerDto(comp);
        return out;
    }
}
