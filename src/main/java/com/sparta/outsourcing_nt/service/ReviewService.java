package com.sparta.outsourcing_nt.service;

import com.sparta.outsourcing_nt.dto.review.req.ReviewRequestDto;
import com.sparta.outsourcing_nt.dto.review.res.ReviewResponseDto;
import com.sparta.outsourcing_nt.entity.*;
import com.sparta.outsourcing_nt.exception.ApplicationException;
import com.sparta.outsourcing_nt.exception.ErrorCode;
import com.sparta.outsourcing_nt.repository.OrderRepository;
import com.sparta.outsourcing_nt.repository.ReviewRepository;
import com.sparta.outsourcing_nt.repository.StoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public ReviewResponseDto createReview(Long storeId, Long orderId, ReviewRequestDto requestDto, User user) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_STORE));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_ORDER));

        // 주문 상태 확인: 완료 상태가 아니면 예외 발생
        if (!order.getStatus().equals(OrderStatus.COMPLETED)) {
            throw new ApplicationException(ErrorCode.ORDER_NOT_COMPLETED);
        }

        // 주문한 사용자와 리뷰 작성자가 일치하는지 확인
        if (!order.getUser().getId().equals(user.getId())) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_REVIEWER);
        }

        // 일치하면 리뷰 생성
        Review review = new Review(requestDto, store, user, order);
        Review savedReview = reviewRepository.save(review);
        return new ReviewResponseDto(savedReview);
    }

    // 별점 범위에 따른 조회
    public List<ReviewResponseDto> getReviewsByRatingRange(Long storeId, int minRating, int maxRating, int page, int size) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_STORE));

        Pageable pageable = PageRequest.of(page, size);
        Page<Review> reviews = reviewRepository.findByStoreAndRatingBetweenOrderByCreatedAtDesc(store, minRating, maxRating, pageable);
        return reviews.stream().map(ReviewResponseDto::new).collect(Collectors.toList());
    }

    @Transactional
    public ReviewResponseDto updateReview(Long storeId, Long reviewId, ReviewRequestDto requestDto, User user) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_REVIEW));

        if (!review.getStore().getId().equals(storeId) || !review.getUser().getId().equals(user.getId())) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_REVIEW_EDIT);
        }

        review.setRating(requestDto.getRating());
        review.setContent(requestDto.getContent());
        return new ReviewResponseDto(review);
    }

    @Transactional
    public ReviewResponseDto deleteReview(Long storeId, Long reviewId, User user) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_REVIEW));

        if (!review.getStore().getId().equals(storeId) || !review.getUser().getId().equals(user.getId())) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_REVIEW_EDIT);
        }
        ReviewResponseDto deletedReviewDto = new ReviewResponseDto(review);
        reviewRepository.delete(review);
        return deletedReviewDto;
    }
}
