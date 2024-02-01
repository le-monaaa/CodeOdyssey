package code.odyssey.common.domain.guildSprint.repository.impl;

import static code.odyssey.common.domain.guild.entity.QGuildMember.guildMember;
import static code.odyssey.common.domain.guildSprint.entity.QGuildProblem.guildProblem;
import static code.odyssey.common.domain.guildSprint.entity.QGuildSprint.guildSprint;
import static code.odyssey.common.domain.guildSprint.entity.enums.GuildSprintStatus.IN_PROGRESS;
import static code.odyssey.common.domain.guildSprint.entity.enums.GuildSprintStatus.WAITING;
import static code.odyssey.common.domain.problem.entity.QProblem.problem;
import static code.odyssey.common.domain.problem.entity.QSubmission.submission;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

import code.odyssey.common.domain.guild.entity.QGuildMember;
import code.odyssey.common.domain.guildSprint.dto.InProgressGuildSprintInfo;
import code.odyssey.common.domain.guildSprint.dto.WaitingGuildSprintInfo;
import code.odyssey.common.domain.guildSprint.dto.WaitingGuildSprintInfo.WaitingGuildProblemInfo;
import code.odyssey.common.domain.guildSprint.entity.GuildSprint;
import code.odyssey.common.domain.guildSprint.repository.GuildSprintRepositoryCustom;
import code.odyssey.common.domain.problem.entity.QSubmission;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class GuildSprintRepositoryImpl implements GuildSprintRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<WaitingGuildSprintInfo> findWaitingGuildSprints(Long guildId) {

		return queryFactory.selectFrom(guildSprint)
			.innerJoin(guildSprint.problems, guildProblem)
			.innerJoin(guildProblem.problem, problem)
			.where(guildSprint.guild.id.eq(guildId), guildSprint.status.eq(WAITING))
			.transform(groupBy(guildSprint.id).list(
				Projections.fields(
					WaitingGuildSprintInfo.class,
					guildSprint.id.as("sprintId"),
					guildSprint.title.as("sprintName"),
					guildSprint.period.as("sprintDay"),
					list(Projections.fields(
							WaitingGuildProblemInfo.class,
							guildProblem.id.as("guildProblemId"),
							problem.id.as("problemId"),
							problem.difficulty.as("difficulty"),
							problem.title.as("title"),
							problem.platform.as("platform")
						)
					).as("problemList")
				)
			));

	}

	@Override
	public Optional<InProgressGuildSprintInfo> findInProgressGuildSprint(Long guildId) {


		GuildSprint inProgressGuildSprint = queryFactory.selectFrom(guildSprint)
			.innerJoin(guildSprint.problems, guildProblem)
			.innerJoin(guildProblem.problem, problem)
			.where(guildSprint.guild.id.eq(guildId), guildSprint.status.eq(IN_PROGRESS))
			.fetchOne();

		if (inProgressGuildSprint == null) {
			return Optional.empty();
		}

		// 문제 ID를 기간 내에 제출한 사람이 있는지 체크
		queryFactory.select(guildMember.id.count())
			.from(guildMember)
			.leftJoin(submission)
			.on(submission.member.id.eq(guildMember.member.id))
			.groupBy(guildMember.id)
		;





		return Optional.empty();
	}
}
