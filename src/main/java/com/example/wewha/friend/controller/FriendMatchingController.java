package com.example.wewha.friend.controller;

import com.example.wewha.common.dto.ApiResponse;
import com.example.wewha.common.entity.User;
import com.example.wewha.common.entity.UserProfile;
import com.example.wewha.common.exception.CustomException;
import com.example.wewha.common.exception.ErrorCode;
import com.example.wewha.common.repository.UserRepository;
import com.example.wewha.friend.dto.MatchProfileRequestDto;
import com.example.wewha.friend.dto.MatchProfileResponseDto;
import com.example.wewha.friend.dto.UserProfileDto;
import com.example.wewha.friend.service.FriendMatchingService;
import com.example.wewha.users.controller.UserController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/friend-matching")
public class FriendMatchingController {

    private final FriendMatchingService friendMatchingService;
    private final UserRepository userRepository;

    // 친구 매칭 설정 저장
    @PostMapping("/settings")
    public ResponseEntity<ApiResponse<UserProfileDto>> saveProfile(@AuthenticationPrincipal UserDetails userDetails, @RequestBody MatchProfileRequestDto dto) {
        String email = userDetails.getUsername();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(()->new CustomException(ErrorCode.ERR_NOT_FOUND));

        UserProfile created = friendMatchingService.saveProfile(currentUser, dto);
        UserProfileDto responseDto = new UserProfileDto(created);

        ApiResponse<UserProfileDto> response = ApiResponse.success(
                201,
                "친구 매칭 설정이 성공적으로 저장되었습니다.",
                responseDto
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/profiles")
    public ResponseEntity<UserController.MsgData<List<MatchProfileResponseDto>>> showProfiles(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(value = "language", required = false) String languageName
    ) {
        if (languageName == null || languageName.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(new UserController.MsgData<>("language 쿼리 파라미터는 필수입니다.", null));
        }

        String email = userDetails.getUsername();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.ERR_NOT_FOUND));

        List<MatchProfileResponseDto> profiles = friendMatchingService.showProfileByStudyLanguage(currentUser, languageName);

        return ResponseEntity.ok(new UserController.MsgData<>(
                "친구 매칭 대상 프로필 목록이 성공적으로 조회되었습니다.",
                profiles
        ));
    }
}
