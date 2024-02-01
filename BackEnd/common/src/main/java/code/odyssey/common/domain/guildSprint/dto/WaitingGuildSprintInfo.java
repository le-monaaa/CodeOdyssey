package code.odyssey.common.domain.guildSprint.dto;

import code.odyssey.common.domain.problem.entity.enums.ProblemPlatform;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
@JsonNaming(SnakeCaseStrategy.class)
public class WaitingGuildSprintInfo {

	private Long sprintId;
	private String sprintName;
	private Integer sprintDay;
	private List<WaitingGuildProblemInfo> problemList = new ArrayList<>();

	@Getter
	@JsonNaming(SnakeCaseStrategy.class)
	public static class WaitingGuildProblemInfo {
		private Long guildProblemId;
		private Long problemId;
		private Integer difficulty;
		private String title;
		private ProblemPlatform platform;

	}

}
