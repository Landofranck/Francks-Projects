package project.adapter.in.web.Reducer;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.jboss.logging.annotations.Message;
import project.adapter.in.web.MatchEventPickDto;

import java.util.List;
public record UpdateMatchDto(@NotNull @Message("you need a match Id DtoMapper 9") Long id, @NotNull @NotEmpty @Message("to update you a non-empty list dtoMapper 9 ") List<MatchEventPickDto> matchOutComes) {
}
