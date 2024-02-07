package code.odyssey.common.domain.memberSprint.service;

import code.odyssey.common.domain.member.entity.Member;
import code.odyssey.common.domain.member.exception.MemberException;
import code.odyssey.common.domain.member.repository.MemberRepository;
import code.odyssey.common.domain.memberSprint.dto.MemberSprintCreateRequest;
import code.odyssey.common.domain.memberSprint.dto.MemberSprintInfo;
import code.odyssey.common.domain.memberSprint.entity.MemberSprint;
import code.odyssey.common.domain.memberSprint.entity.enums.DayType;
import code.odyssey.common.domain.memberSprint.repository.MemberSprintRepository;
import code.odyssey.common.domain.problem.entity.Problem;
import code.odyssey.common.domain.problem.entity.enums.ProblemType;
import code.odyssey.common.domain.score.dto.ScoreInfo;
import code.odyssey.common.domain.score.entity.Score;
import code.odyssey.common.domain.score.exception.ScoreErrorCode;
import code.odyssey.common.domain.score.exception.ScoreException;
import code.odyssey.common.domain.score.repository.ScoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static code.odyssey.common.domain.member.exception.MemberErrorCode.NOT_EXISTS_MEMBER;

@Transactional
@RequiredArgsConstructor
@Service
@Slf4j
public class MemberSprintService {

    private final MemberSprintRepository memberSprintRepository;
    private final MemberRepository memberRepository;
    private final ScoreRepository scoreRepository;

    public void createSprint(Long memberId, MemberSprintCreateRequest request) {
        Member member = memberRepository.findById(memberId)
                .filter(m -> m.getResignedAt() == null)
                .orElseThrow(() -> new MemberException(NOT_EXISTS_MEMBER));

        // 멤버 이미 등록되어 있는지 체크.
        // 만약 이미 등록이 되어 있으면 삭제 후 저장
        List<MemberSprint> existingSprints = memberSprintRepository.findByMemberId(memberId);
        if (!existingSprints.isEmpty()) {
            memberSprintRepository.deleteAll(existingSprints);
        }


        List<MemberSprint> sprintSchedules = request.getScheduleInfoList().stream()
                .map(scheduleInfo -> {
                    return MemberSprint.builder()
                            .member(member)
                            .day(scheduleInfo.getDay())
                            .recommendType(scheduleInfo.getRecommendType())
                            .recommendDifficulty(scheduleInfo.getRecommendDifficulty())
                            .build();
                })
                .toList();

        memberSprintRepository.saveAll(sprintSchedules);
    }


    public List<Problem> getRecommendedProblems(Long memberId) {

        // 만약에 회원이 스프린트 확정을 누르지 않았다면, 회원 티어에 따라 문제를 랜덤으로 보여준다.
        List<MemberSprint> sprints = memberSprintRepository.findByMemberId(memberId);
        if (sprints.isEmpty()) {
            Score score = scoreRepository.findStatsByMemberId(memberId)
                    .orElseThrow(() -> new ScoreException(ScoreErrorCode.NO_AVAILABLE_SCORES));

            Integer tier = score.getTier();

            return memberSprintRepository.getRandomProblems(
                    memberId,
                    tier,
                    PageRequest.of(0, 4)
            );

        } else {
            // 스프린트 확정을 눌러서 DB에 저장이 되어 있다면, 요일별로 문제를 보여준다.
            DayType day = DayType.valueOf(LocalDate.now().getDayOfWeek().name()); // date를 요일 enum으로 변환

            MemberSprint memberSprint = memberSprintRepository.findByMemberIdAndDay(memberId, day);
            ProblemType ptype = memberSprint.getRecommendType();
            Integer difficulty = memberSprint.getRecommendDifficulty();

            return memberSprintRepository.getRecommendedProblems(
                    memberId,
                    ptype,
                    difficulty,
                    PageRequest.of(0, 4));
        }

    }

    public List<MemberSprintInfo> getSprint(Long memberId) {
        List<MemberSprint> memberSprints = memberSprintRepository.findByMemberId(memberId);

        return memberSprints.stream()
                .map(memberSprint -> MemberSprintInfo.builder()
                        .day(memberSprint.getDay())
                        .recommendedDifficulty(memberSprint.getRecommendDifficulty())
                        .recommendedType(memberSprint.getRecommendType())
                        .build())
                .collect(Collectors.toList());
    }
}
