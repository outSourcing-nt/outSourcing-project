package com.sparta.outsourcing_nt.controller;

import com.sparta.outsourcing_nt.config.userdetails.AuthUserDetails;
import com.sparta.outsourcing_nt.dto.review.req.ReviewRequestDto;
import com.sparta.outsourcing_nt.dto.review.res.ReviewResponseDto;
import com.sparta.outsourcing_nt.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/store/{storeId}/review")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/order/{orderId}")
    public ResponseEntity<ReviewResponseDto> createReview(
            @PathVariable Long storeId,
            @PathVariable Long orderId,
            @RequestBody ReviewRequestDto requestDto,
            @AuthenticationPrincipal AuthUserDetails authUser) {
        ReviewResponseDto review = reviewService.createReview(storeId, orderId, requestDto, authUser.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(review);
    }

    // 별점 범위에 따른 리뷰 조회
    @GetMapping("/range")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByRatingRange(
            @PathVariable Long storeId,
            @RequestParam(defaultValue = "1") int minRating,
            @RequestParam(defaultValue = "5") int maxRating,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<ReviewResponseDto> reviews = reviewService.getReviewsByRatingRange(storeId, minRating, maxRating, page, size);
        return ResponseEntity.ok(reviews);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDto> updateReview(
            @PathVariable Long storeId,
            @PathVariable Long reviewId,
            @RequestBody ReviewRequestDto requestDto,
            @AuthenticationPrincipal AuthUserDetails authUser) {
        ReviewResponseDto updatedReview = reviewService.updateReview(storeId, reviewId, requestDto, authUser.getUser());
        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long storeId,
            @PathVariable Long reviewId,
            @AuthenticationPrincipal AuthUserDetails authUser) {
        reviewService.deleteReview(storeId, reviewId, authUser.getUser());
        return ResponseEntity.noContent().build();
    }
}

