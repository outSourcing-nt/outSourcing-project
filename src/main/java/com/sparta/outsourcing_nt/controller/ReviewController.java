package com.sparta.outsourcing_nt.controller;

import com.sparta.outsourcing_nt.config.userdetails.AuthUserDetails;
import com.sparta.outsourcing_nt.dto.review.req.ReviewRequestDto;
import com.sparta.outsourcing_nt.dto.review.res.ReviewResponseDto;
import com.sparta.outsourcing_nt.service.ReviewService;
import com.sparta.outsourcing_nt.util.result.ApiResult;
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
    public ResponseEntity<ApiResult<ReviewResponseDto>> createReview(
            @PathVariable Long storeId,
            @PathVariable Long orderId,
            @RequestBody ReviewRequestDto requestDto,
            @AuthenticationPrincipal AuthUserDetails authUser) {
        ReviewResponseDto review = reviewService.createReview(storeId, orderId, requestDto, authUser.getUser());
        return new ResponseEntity<>(
                ApiResult.success("리뷰 생성 완료", review),
                HttpStatus.OK
        );
    }

    // 별점 범위에 따른 리뷰 조회
    @GetMapping("/range")
    public ResponseEntity<ApiResult<List<ReviewResponseDto>>> getReviewsByRatingRange(
            @PathVariable Long storeId,
            @RequestParam(defaultValue = "1") int minRating,
            @RequestParam(defaultValue = "5") int maxRating,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<ReviewResponseDto> reviews = reviewService.getReviewsByRatingRange(storeId, minRating, maxRating, page, size);
        return new ResponseEntity<>(
                ApiResult.success("별점 범위에 따른 리뷰 조회 성공", reviews),
                HttpStatus.OK
        );
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ApiResult<ReviewResponseDto>> updateReview(
            @PathVariable Long storeId,
            @PathVariable Long reviewId,
            @RequestBody ReviewRequestDto requestDto,
            @AuthenticationPrincipal AuthUserDetails authUser) {
        ReviewResponseDto updatedReview = reviewService.updateReview(storeId, reviewId, requestDto, authUser.getUser());
        return new ResponseEntity<>(
                ApiResult.success("리뷰 수정 성공", updatedReview),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResult<ReviewResponseDto>> deleteReview(
            @PathVariable Long storeId,
            @PathVariable Long reviewId,
            @AuthenticationPrincipal AuthUserDetails authUser) {
        ReviewResponseDto deleteReview = reviewService.deleteReview(storeId, reviewId, authUser.getUser());
        return new ResponseEntity<>(
                ApiResult.success("리뷰 삭제 성공", deleteReview),
                HttpStatus.NO_CONTENT
        );
    }
}

