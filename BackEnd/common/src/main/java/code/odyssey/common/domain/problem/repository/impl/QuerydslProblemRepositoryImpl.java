package code.odyssey.common.domain.problem.repository.impl;

import static code.odyssey.common.domain.problem.entity.QProblem.problem;

import code.odyssey.common.domain.problem.dto.problem.ProblemInfo;
import code.odyssey.common.domain.problem.dto.problem.ProblemRequestDto;
import code.odyssey.common.domain.problem.repository.QuerydslProblemRepository;
import code.odyssey.common.domain.problem.service.ProblemService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class QuerydslProblemRepositoryImpl  implements QuerydslProblemRepository {

    private final JPAQueryFactory jpaQueryFactory;

    private BooleanBuilder getSearchOption(ProblemRequestDto request){
        BooleanBuilder searchOptions = new BooleanBuilder();

        if(request.type()!=null){
            searchOptions.and(problem.type.eq(request.type()));
        }

        if(request.platform()!=null){
            searchOptions.and(problem.platform.eq(request.platform()));
        }

        if(request.keyword() != null){
            searchOptions.and(problem.title.contains(request.keyword()));
        }

        if(request.difficulty() != null ){
            searchOptions.and(problem.difficulty.eq(request.difficulty()));
        }

        return searchOptions;
    }

    @Override
    public List<ProblemInfo> getProblems(ProblemRequestDto request, Pageable pageable) {
        BooleanBuilder searchOptions = getSearchOption(request);

        //ProblemInfo(String title, ProblemPlatform platform, int difficulty, ProblemType type) {
        return jpaQueryFactory
                .select(Projections.fields(ProblemInfo.class,
                        problem.title,
                        problem.platform,
                        problem.difficulty,
                        problem.type))
                .from(problem)
                .where(searchOptions)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}