package tight.commas.domain.park.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import tight.commas.domain.park.ParkSearchCondition;
import tight.commas.domain.park.dto.ParkDto;
import tight.commas.domain.park.dto.QParkDto;
import tight.commas.domain.park.entity.Park;
import tight.commas.domain.park.entity.QPark;

import java.util.List;
import java.util.Map;

import static tight.commas.domain.park.entity.QPark.*;

public class ParkRepositoryImpl implements ParkRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public ParkRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<ParkDto> search(ParkSearchCondition condition, Pageable pageable) {
        List<ParkDto> fetch = queryFactory
                .select(new QParkDto(
                        park.parkName,
                        park.outLine,
                        park.address,
                        park.mainEquip,
                        park.plant,
                        park.imageUrl))
                .from(park)
                .where(park.parkName.contains(condition.getParkName()))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        JPAQuery<Park> countQuery = queryFactory
                .selectFrom(park)
                .where(park.parkName.contains(condition.getParkName()));

        return PageableExecutionUtils.getPage(fetch, pageable, countQuery::fetchCount);
    }
}
