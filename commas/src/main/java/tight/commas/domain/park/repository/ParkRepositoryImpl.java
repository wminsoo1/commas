package tight.commas.domain.park.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import tight.commas.domain.park.ParkSearchCondition;
import tight.commas.domain.park.dto.ParkCardDto;
import tight.commas.domain.park.dto.ParkCardDtoV2;
import tight.commas.domain.park.dto.QParkCardDto;
import tight.commas.domain.park.dto.QParkCardDtoV2;
import tight.commas.domain.park.entity.Park;
import tight.commas.domain.park.entity.QPark;
import tight.commas.domain.review.Tag;
import tight.commas.domain.review.entity.QReview;
import tight.commas.domain.review.entity.QReviewTag;
import tight.commas.domain.review.entity.Review;
import tight.commas.domain.review.entity.ReviewTag;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static tight.commas.domain.park.entity.QPark.*;
import static tight.commas.domain.review.entity.QReview.*;
import static tight.commas.domain.review.entity.QReviewTag.*;

public class ParkRepositoryImpl implements ParkRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public ParkRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public Page<ParkCardDto> parkCardSearch(ParkSearchCondition condition, Pageable pageable) {

        //

        List<Review> fetch = queryFactory
                        .selectFrom(review)
                        .leftJoin(review.park, park)
                        .where(parkNameEq(condition.getParkName()))
                        .fetchJoin()
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        List<ReviewTag> reviewTags = queryFactory
                .select(reviewTag)
                .from(reviewTag)
                .leftJoin(reviewTag.review, review)
                .where(review.in(fetch), tagEq(condition.getTags()))
                .fetchJoin()
                .fetch();

        List<ParkCardDto> parkCardDtos = fetch.stream()
                .map(review -> {
                    // 현재 리뷰에 대한 ReviewTag 리스트를 가져옵니다.
                    List<ReviewTag> tagsForReview = reviewTags.stream()
                            .filter(tag -> tag.getReview().getId().equals(review.getId()))
                            .collect(Collectors.toList());

                    // ReviewTag가 없는 경우는 해당 리뷰를 제외합니다.
                    if (tagsForReview.isEmpty()) {
                        return null;
                    }

                    // ReviewTag가 있는 경우 ParkCardDto를 생성합니다.
                    return new ParkCardDto(
                            review.getPark().getParkName(),
                            review.getPark().getAddress(),
                            review.getPark().getImageUrl(),
                            tagsForReview
                    );
                })
                .filter(parkCardDto -> parkCardDto != null)  // null인 경우는 제외합니다.
                .toList();

//        List<ParkCardDto> parkCardDtos = reviewTags.stream()
//                .map(reviewTag ->  new ParkCardDto(
//                        reviewTag.getReview().getPark().getParkName(),
//                        reviewTag.getReview().getPark().getAddress(),
//                        reviewTag.getReview().getPark().getImageUrl(),
//                        reviewTag.getReview().getReviewTags() //n + 1 문제 터짐
//                )).distinct()
//                .collect(Collectors.toList());



//        List<ParkCardDto> parkCardDtos = queryFactory
//                .select(new QParkCardDto(
//                        park.parkName,
//                        park.address,
//                        park.imageUrl))
//                .from(park)
//                .leftJoin(park, review.park)
//                .where(park.parkName.contains(condition.getParkName()))
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//
//        for (ParkCardDto parkCardDto : parkCardDtos) {
//            List<ReviewTag> reviewTags = queryFactory
//                    .selectFrom(reviewTag)
//                    .leftJoin(reviewTag.review, review)
//                    .where(review.park.eq(parkCardDto.getParkName())) // 또는 review.park.eq(parkCardDto.getParkName())
//                    .fetch();
//            parkCardDto.setReviewTags(reviewTags);
//        }

//        List<ParkCardDto> parkCardDto1s = queryFactory
//                .select(new QParkCardDto(
//                        park.parkName,
//                        park.address,
//                        park.imageUrl,
//                        JPAExpressions
//                                .select(reviewTag.tag)
//                                .from(reviewTag)
//                                .where(reviewTag.review.eq(review))
//                                ))
//                .from(review)
//                .leftJoin(review.park, park)
//                .where(park.parkName.contains(condition.getParkName()))
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();


//        List<ParkCardDto> parkCardDtos = queryFactory
//                .select(Projections.constructor(ParkCardDto.class,
//                        review.park.parkName,
//                        review.park.address,
//                        review.park.imageUrl,
//                        Projections.list(Projections.constructor(ReviewTagDto.class,
//                                reviewTag.tag))))
//                .from(review)
//                .leftJoin(review.park)
//                .leftJoin(review.reviewTags, reviewTag)
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();

        return new PageImpl<>(parkCardDtos, pageable, parkCardDtos.size());

    }

    private static BooleanExpression tagEq(List<Tag> tags) {
        return tags.isEmpty() ? null : reviewTag.review.id.in(JPAExpressions
                .select(reviewTag.review.id)
                .from(reviewTag)
                .where(reviewTag.tag.in(tags))
                .groupBy(reviewTag.review.id)
                .having(
                        reviewTag.tag.count().eq((long) tags.size())
                ));
    }

    private static BooleanExpression parkNameEq(String parkName) {
        return StringUtils.isEmpty(parkName) ? null : park.parkName.contains(parkName);
    }

    @Override
    public Page<ParkCardDtoV2> parkCardSearchV2(ParkSearchCondition condition, Pageable pageable) {
        List<Review> reviews = findByParkNameContaining(condition, pageable);

        // condition의 태그가 null이면 빈 리스트로 초기화합니다.
        List<Tag> tags = condition.getTags() != null ? condition.getTags() : Collections.emptyList();

        // condition의 태그가 null이 아닌 경우에만 필터링을 수행합니다.
        if (!tags.isEmpty()) {
            reviews = reviews.stream()
                    .filter(review -> review.getTags().containsAll(tags)) //batch 적용
                    .toList();
        }

        List<ParkCardDtoV2> parkCardDtoList = reviews.stream()
                .map(ParkCardDtoV2::new)
                .toList();

        return new PageImpl<>(parkCardDtoList, pageable, parkCardDtoList.size());
    }

    public List<Review> findByParkNameContaining(ParkSearchCondition condition, Pageable pageable) {
        return queryFactory
                .selectFrom(review)
                .leftJoin(review.park, park)
                .where(parkNameEq(condition.getParkName()))
                .fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

    }

    @Override
    public Page<ParkCardDtoV2> parkCardSearchV3(ParkSearchCondition condition, Pageable pageable) {

        List<Review> reviews = queryFactory
                .select(reviewTag.review)
                .from(reviewTag)
                .leftJoin(reviewTag.review.park, park)
                .where(park.parkName.contains(condition.getParkName()))
                .fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<ParkCardDtoV2> parkCardDtoList = reviews.stream()
                .map(ParkCardDtoV2::new)
                .collect(Collectors.toList());

        return new PageImpl<>(parkCardDtoList, pageable, parkCardDtoList.size());
    }

    @Override
    public List<Long> example(ParkSearchCondition condition) {
        List<Long> reviewIds = queryFactory
                .select(reviewTag.review.id)
                .from(reviewTag)
                .where(reviewTag.tag.in(condition.getTags()))
                .groupBy(reviewTag.review.id)
                .having(reviewTag.tag.count().eq((long) condition.getTags().size()))
                .fetch();

        return reviewIds;
    }


    @Override //파크 리스트 조회
    public Page<ParkCardDtoV2> getParkCardDtoV2(Pageable pageable) {

        List<Review> reviewList = queryFactory
                .selectFrom(review)
                .leftJoin(review.park, park)
                .fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Map<Park, List<Review>> parkReviewMap  = reviewList.stream()
                .collect(Collectors.groupingBy(Review::getPark));

        List<ParkCardDtoV2> parkCardDtoList  = parkReviewMap.entrySet().stream()
                .map(entry -> {
                    Park keyPark = entry.getKey();
                    List<Review> reviewsInPark = entry.getValue();

                    Map<Tag, Long> tagCounts = reviewsInPark.stream()
                            .flatMap(review -> review.getReviewTags().stream())
                            .collect(Collectors.groupingBy(ReviewTag::getTag, Collectors.counting()));

                    List<Tag> topTags = tagCounts.entrySet().stream()
                            .sorted(Map.Entry.<Tag, Long>comparingByValue().reversed())
                            .limit(3)
                            .map(Map.Entry::getKey)
                            .toList();

                    return new ParkCardDtoV2(keyPark, topTags);
                }).toList();

        List<ParkCardDtoV2> parkCardDtoV2List = queryFactory
                .select(new QParkCardDtoV2(
                        park.id,
                        park.parkName,
                        park.address,
                        park.imageUrl))
                .from(park)
                .fetch();

        for (ParkCardDtoV2 parkCardDtoV2 : parkCardDtoV2List) {
            for (ParkCardDtoV2 parkCardDto : parkCardDtoList) {
                if (parkCardDtoV2.getParkId().equals(parkCardDto.getParkId())) {
                    parkCardDtoV2.setParkName(parkCardDto.getParkName());
                    parkCardDtoV2.setAddress(parkCardDto.getAddress());
                    parkCardDtoV2.setImageUrl(parkCardDto.getImageUrl());
                    parkCardDtoV2.setReviewTags(parkCardDto.getReviewTags());
                    break;
                }
            }
        }


        return new PageImpl<>(parkCardDtoV2List, pageable, parkCardDtoList.size());
    }

    @Override //파크 검색 및 태그 필터
    public Page<ParkCardDtoV2> parkCardSearchV4(ParkSearchCondition condition, Pageable pageable) {
        List<Review> reviewList = findByParkNameContaining(condition, pageable);

        Map<Park, List<Review>> parkReviewMap  = reviewList.stream()
                .collect(Collectors.groupingBy(Review::getPark));

        List<ParkCardDtoV2> parkCardDtoList  = parkReviewMap.entrySet().stream()
                .map(entry -> {
                    Park keyPark = entry.getKey();
                    List<Review> reviewsInPark = entry.getValue();

                    Map<Tag, Long> tagCounts = reviewsInPark.stream()
                            .flatMap(review -> review.getReviewTags().stream())
                            .collect(Collectors.groupingBy(ReviewTag::getTag, Collectors.counting()));

                    List<Tag> topTags = tagCounts.entrySet().stream()
                            .sorted(Map.Entry.<Tag, Long>comparingByValue().reversed())
                            .limit(3)
                            .map(Map.Entry::getKey)
                            .toList();

                    return new ParkCardDtoV2(keyPark, topTags);
                }).toList();

        // condition의 태그가 null이면 빈 리스트로 초기화합니다.
        List<Tag> tags = condition.getTags() != null ? condition.getTags() : Collections.emptyList();

        // condition의 태그가 null이 아닌 경우에만 필터링을 수행합니다.
        if (!tags.isEmpty()) {
            parkCardDtoList = parkCardDtoList.stream()
                    .filter(parkCardDtoV2 -> parkCardDtoV2.getReviewTags().containsAll(tags))
                    .toList();
        }

        return new PageImpl<>(parkCardDtoList, pageable, parkCardDtoList.size());
    }


//    @Override
//    public Page<ParkDto> search(ParkSearchCondition condition, Pageable pageable) {
//        List<ParkDto> fetch = queryFactory
//                .select(new QParkDto(
//                        park.parkName,
//                        park.outLine,
//                        park.address,
//                        park.mainEquip,
//                        park.plant,
//                        park.imageUrl))
//                .from(park)
//                .where(park.parkName.contains(condition.getParkName()))
//                .limit(pageable.getPageSize())
//                .offset(pageable.getOffset())
//                .fetch();
//
//        JPAQuery<Park> countQuery = queryFactory
//                .selectFrom(park)
//                .where(park.parkName.contains(condition.getParkName()));
//
//        return PageableExecutionUtils.getPage(fetch, pageable, countQuery::fetchCount);
//    }
}
