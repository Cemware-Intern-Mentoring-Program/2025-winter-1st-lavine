package com.cemware.lavine.controller;

import com.cemware.lavine.dto.GroupCreateRequest;
import com.cemware.lavine.dto.GroupResponse;
import com.cemware.lavine.dto.GroupUpdateRequest;
import com.cemware.lavine.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
@Tag(name = "Group", description = "그룹 관리 API")
@SecurityRequirement(name = "bearerAuth")
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    @Operation(summary = "그룹 생성", description = "새로운 그룹을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "그룹 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    public ResponseEntity<GroupResponse> createGroup(@Valid @RequestBody GroupCreateRequest request) {
        GroupResponse response = groupService.createGroup(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Location", "/groups/" + response.id())
                .body(response);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "그룹 이름 수정", description = "그룹 이름을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "그룹 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "그룹을 찾을 수 없음")
    })
    public ResponseEntity<GroupResponse> updateGroup(
            @Parameter(description = "그룹 ID", required = true) @PathVariable Long id,
            @Valid @RequestBody GroupUpdateRequest request) {
        GroupResponse response = groupService.updateGroup(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "그룹 삭제", description = "그룹을 삭제합니다. 하위 할 일도 함께 삭제됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "그룹 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "그룹을 찾을 수 없음")
    })
    public ResponseEntity<Void> deleteGroup(
            @Parameter(description = "그룹 ID", required = true) @PathVariable Long id) {
        groupService.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "그룹 조회", description = "그룹 정보를 조회합니다. 하위 할 일도 함께 조회됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "그룹 조회 성공"),
            @ApiResponse(responseCode = "404", description = "그룹을 찾을 수 없음")
    })
    public ResponseEntity<GroupResponse> getGroup(
            @Parameter(description = "그룹 ID", required = true) @PathVariable Long id) {
        GroupResponse response = groupService.getGroup(id);
        return ResponseEntity.ok(response);
    }
}

