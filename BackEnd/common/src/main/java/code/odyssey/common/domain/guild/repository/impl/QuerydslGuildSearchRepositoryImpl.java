package code.odyssey.common.domain.guild.repository.impl;

import code.odyssey.common.domain.guild.dto.GuildSearchRequest;
import code.odyssey.common.domain.guild.dto.GuildSimpleInfo;
import code.odyssey.common.domain.guild.repository.GuildSearchRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static code.odyssey.common.domain.guild.entity.QGuild.guild;
import static code.odyssey.common.domain.guild.entity.QGuildMember.guildMember;

@RequiredArgsConstructor
@Repository
public class QuerydslGuildSearchRepositoryImpl implements GuildSearchRepository {

    private final JPAQueryFactory queryFactory;


    @Override
    public Page<GuildSimpleInfo> search(GuildSearchRequest request, Pageable pageable) {

        JPAQuery<Long> countQuery = queryFactory
                .select(guild.id.count())
                .from(guild)
                .where(
                        guild.disbandedAt.isNull(),
                        keywordContains(request.getKeyword()),
                        languageEq(request.getLanguage()),
                        difficultyEq(request.getDifficulty()),
                        guild.id.notIn(JPAExpressions.select(guildMember.guild.id)
                                .from(guildMember)
                                .where(guildMember.member.id.eq(request.getMemberId())))
                );

        List<Long> ids = queryFactory.select(guild.id)
                .from(guild)
                .where(
                        guild.disbandedAt.isNull(),
                        keywordContains(request.getKeyword()),
                        languageEq(request.getLanguage()),
                        difficultyEq(request.getDifficulty()),
                        guild.id.notIn(JPAExpressions.select(guildMember.guild.id)
                                .from(guildMember)
                                .where(guildMember.member.id.eq(request.getMemberId())))
                )
                .orderBy(guild.id.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        List<GuildSimpleInfo> result = queryFactory
                .select(Projections.fields(GuildSimpleInfo.class,
                        guild.id.as("id"),
                        guild.name,
                        guild.capacity.as("maxCapacity"),
                        guild.id.count().as("curCapacity"),
                        guild.language,
                        guild.difficulty,
                        guild.problemCapacity))
                .from(guild)
                .leftJoin(guildMember)
                .on(guild.id.eq(guildMember.guild.id))
                .where(guild.id.in(ids))
                .groupBy(guild.id)
                .orderBy(guild.id.desc())
                .fetch();


        return PageableExecutionUtils.getPage(result, pageable, countQuery::fetchOne);
    }

    BooleanExpression keywordContains(String keyword) {
        return StringUtils.hasText(keyword) ? guild.name.contains(keyword) : null;
    }

    BooleanExpression languageEq(String language) {
        return StringUtils.hasText(language) ? guild.language.eq(language) : null;
    }


    BooleanExpression difficultyEq(String difficulty) {
        return StringUtils.hasText(difficulty) ? guild.difficulty.eq(difficulty) : null;
    }


}
