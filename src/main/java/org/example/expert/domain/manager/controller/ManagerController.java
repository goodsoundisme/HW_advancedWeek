package org.example.expert.domain.manager.controller;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.expert.config.JwtUtil;
import org.example.expert.domain.common.annotation.Auth;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.manager.dto.request.ManagerSaveRequest;
import org.example.expert.domain.manager.dto.response.ManagerResponse;
import org.example.expert.domain.manager.dto.response.ManagerSaveResponse;
import org.example.expert.domain.manager.service.ManagerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;
    private final JwtUtil jwtUtil;

    @PostMapping("/todos/{todoId}/managers")
    public ResponseEntity<ManagerSaveResponse> saveManager(
            @Auth AuthUser authUser,
            @PathVariable long todoId,
            @Valid @RequestBody ManagerSaveRequest managerSaveRequest
    ) {
        return ResponseEntity.ok(managerService.saveManager(authUser, todoId, managerSaveRequest));
    }

    @GetMapping("/todos/{todoId}/managers")
    public ResponseEntity<List<ManagerResponse>> getMembers(@PathVariable long todoId) {
        return ResponseEntity.ok(managerService.getManagers(todoId));
    }

    //level 1-4 :  관심사 분리 - JWT 유효성 검사 로직 수정
    @DeleteMapping("/todos/{todoId}/managers/{managerId}")
    public void deleteManager(
//            @RequestHeader("Authorization") String bearerToken,
//            @PathVariable long todoId,
//            @PathVariable long managerId
            HttpServletRequest request,
            @PathVariable long todoId,
            @PathVariable long managerId
    ) {
        // 필터에서 설정한 사용자 정보를 HttpServletRequest에서 가져옴
        Long userId = (Long) request.getAttribute("userId");
        String email = (String) request.getAttribute("email");
        String userRole = (String) request.getAttribute("userRole");

        // 인증된 사용자의 ID를 사용하여 매니저 삭제 비즈니스 로직 처리
        managerService.deleteManager(userId, todoId, managerId);
//        Claims claims = jwtUtil.extractClaims(bearerToken.substring(7));
//        long userId = Long.parseLong(claims.getSubject());
//        managerService.deleteManager(userId, todoId, managerId);
    }
}
