package org.example.expert.domain.user.service;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse getUser(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new InvalidRequestException("User not found"));
        return new UserResponse(user.getId(), user.getEmail());
    }

    //level 1-3 : 리팩토링 퀴즈 - 메서드 분리
    @Transactional
    public void changePassword(long userId, UserChangePasswordRequest userChangePasswordRequest) {

        validateNewPassword(userChangePasswordRequest.getNewPassword());

        User user = userRepository.findById(userId).orElseThrow(() -> new InvalidRequestException("User not found."));

        if (passwordEncoder.matches(userChangePasswordRequest.getNewPassword(), user.getPassword())){
            throw new InvalidRequestException("새 비밀번호는 기존 비밀번호와 같을 수 없습니다.");
        }
        if (passwordEncoder.matches(userChangePasswordRequest.getOldPassword(),user.getPassword())){
            throw new InvalidRequestException("잘못된 비밀번호 입니다.");
        }
//        if (userChangePasswordRequest.getNewPassword().length() < 8 ||
//                !userChangePasswordRequest.getNewPassword().matches(".*\\d.*") ||
//                !userChangePasswordRequest.getNewPassword().matches(".*[A-Z].*")) {
//            throw new InvalidRequestException("새 비밀번호는 8자 이상이어야 하고, 숫자와 대문자를 포함해야 합니다.");
//        }

        user.changePassword(passwordEncoder.encode(userChangePasswordRequest.getNewPassword()));
    }

//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new InvalidRequestException("User not found"));
//
//        if (passwordEncoder.matches(userChangePasswordRequest.getNewPassword(), user.getPassword())) {
//            throw new InvalidRequestException("새 비밀번호는 기존 비밀번호와 같을 수 없습니다.");
//        }
//
//        if (!passwordEncoder.matches(userChangePasswordRequest.getOldPassword(), user.getPassword())) {
//            throw new InvalidRequestException("잘못된 비밀번호입니다.");
//        }
//
//        user.changePassword(passwordEncoder.encode(userChangePasswordRequest.getNewPassword()));
//    }

    private void validateNewPassword(@NotBlank String newPassword) {
        if (!isPasswordLengthValid(newPassword)) {
            throw new InvalidRequestException("새 비밀번호는 8자 이상이어야 합니다.");
        }
        if (!containsDigit(newPassword)) {
            throw new InvalidRequestException("새 비밀번호에는 적어도 하나의 숫자가 포함되어야 합니다.");
        }
        if (!containsUppercase(newPassword)) {
            throw new InvalidRequestException("새 비밀번호에는 적어도 하나의 대문자가 포함되어야 합니다.");
        }
    }

    private boolean containsUppercase(@NotBlank String password) {
        return password.length() >= 8;
    }

    private boolean containsDigit(@NotBlank String password) {
        return password.matches(".*\\d.*");
    }

    private boolean isPasswordLengthValid(@NotBlank String password) {
        return password.matches(".*[A-Z].*");
    }
}
