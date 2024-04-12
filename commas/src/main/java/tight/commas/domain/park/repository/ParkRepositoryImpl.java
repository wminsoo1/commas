package tight.commas.domain.park.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
import tight.commas.domain.userParkLike.entity.UserParkLike;
import tight.commas.domain.userParkLike.repository.UserParkLikeRepository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static tight.commas.domain.park.entity.QPark.*;
import static tight.commas.domain.review.entity.QReview.*;
import static tight.commas.domain.review.entity.QReviewTag.*;

public class ParkRepositoryImpl implements ParkRepositoryCustom{

    private JPAQueryFactory queryFactory;
    private UserParkLikeRepository userParkLikeRepository;

    @Autowired
    public ParkRepositoryImpl(EntityManager em, UserParkLikeRepository userParkLikeRepository) {
        this.queryFactory = new JPAQueryFactory(em);
        this.userParkLikeRepository = userParkLikeRepository;
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
                    List<ReviewTag> tagsForReview = reviewTags.stream()
                            .filter(tag -> tag.getReview().getId().equals(review.getId()))
                            .collect(Collectors.toList());

                    if (tagsForReview.isEmpty()) {
                        return null;
                    }

                    return new ParkCardDto(
                            review.getPark().getParkName(),
                            review.getPark().getAddress(),
                            review.getPark().getImageUrl(),
                            tagsForReview
                    );
                })
                .filter(parkCardDto -> parkCardDto != null)
                .toList();

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

        List<Tag> tags = condition.getTags() != null ? condition.getTags() : Collections.emptyList();

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
    public Page<ParkCardDtoV2> getParkCardDtoV2(Pageable pageable, Long userId) {

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

        List<UserParkLike> userParkLikes = userParkLikeRepository.findByUserId(userId);

        List<Park> parks = userParkLikes.stream()
                .map(UserParkLike::getPark)
                .toList();

        for (ParkCardDtoV2 parkCardDtoV2 : parkCardDtoV2List) {
            for (Park park : parks) {
                if (parkCardDtoV2.getParkId().equals(park.getId())) {
                    parkCardDtoV2.setLikeStatus(true);
                }
            }
        }


        return new PageImpl<>(parkCardDtoV2List, pageable, parkCardDtoList.size());
    }

    @Override //파크 검색 및 태그 필터
    public Page<ParkCardDtoV2> parkCardSearchV4(ParkSearchCondition condition, Pageable pageable, Long userId) {
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

        List<Tag> tags = condition.getTags() != null ? condition.getTags() : Collections.emptyList();

        if (!tags.isEmpty()) {
            parkCardDtoList = parkCardDtoList.stream()
                    .filter(parkCardDtoV2 -> parkCardDtoV2.getReviewTags().containsAll(tags))
                    .toList();
        }

        List<UserParkLike> userParkLikes = userParkLikeRepository.findByUserId(userId);

        List<Park> parks = userParkLikes.stream()
                .map(UserParkLike::getPark)
                .toList();

        for (ParkCardDtoV2 parkCardDtoV2 : parkCardDtoList) {
            for (Park park : parks) {
                if (parkCardDtoV2.getParkId().equals(park.getId())) {
                    parkCardDtoV2.setLikeStatus(true);
                }
            }
        }

        return new PageImpl<>(parkCardDtoList, pageable, parkCardDtoList.size());
    }
}
