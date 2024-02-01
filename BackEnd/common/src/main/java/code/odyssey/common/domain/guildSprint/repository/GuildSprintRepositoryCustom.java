package code.odyssey.common.domain.guildSprint.repository;

import code.odyssey.common.domain.guildSprint.dto.InProgressGuildSprintInfo;
import code.odyssey.common.domain.guildSprint.dto.WaitingGuildSprintInfo;
import java.util.List;
import java.util.Optional;

public interface GuildSprintRepositoryCustom {

	List<WaitingGuildSprintInfo> findWaitingGuildSprints(Long guildId);
	Optional<InProgressGuildSprintInfo> findInProgressGuildSprint(Long guildId);

}
