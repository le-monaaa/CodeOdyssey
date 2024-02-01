package code.odyssey.common.domain.guildSprint.dto;

import code.odyssey.common.domain.problem.entity.enums.ProblemPlatform;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
@JsonNaming(SnakeCaseStrategy.class)
public class InProgressGuildSprintInfo {

	private Long sprintId;
	private String sprintName;
	private LocalDate startedAt;
	private LocalDate endedAt;
	private Integer time;
	private List<InProgressGuildProblemInfo> problemList = new ArrayList<>();

	@Getter
	@JsonNaming(SnakeCaseStrategy.class)
	public static class InProgressGuildProblemInfo {
		private String type;
		private Long guildProblemId;
		private Integer difficulty;
		private String title;
		private Integer percent;
	}

}
