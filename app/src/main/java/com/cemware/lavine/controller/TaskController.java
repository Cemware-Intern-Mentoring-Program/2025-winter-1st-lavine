package com.cemware.lavine.controller;

import com.cemware.lavine.dto.TaskCreateRequest;
import com.cemware.lavine.dto.TaskResponse;
import com.cemware.lavine.dto.TaskUpdateRequest;
import com.cemware.lavine.dto.TaskUpdateStatusRequest;
import com.cemware.lavine.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Tag(name = "Task", description = "할 일 관리 API")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @Operation(summary = "할 일 생성", description = "새로운 할 일을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "할 일 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskCreateRequest request) {
        TaskResponse response = taskService.createTask(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Location", "/tasks/" + response.id())
                .body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "할 일 수정", description = "할 일 제목을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "할 일 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "할 일을 찾을 수 없음")
    })
    public ResponseEntity<TaskResponse> updateTask(
            @Parameter(description = "할 일 ID", required = true) @PathVariable Long id,
            @Valid @RequestBody TaskUpdateRequest request) {
        TaskResponse response = taskService.updateTask(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "할 일 상태 변경", description = "할 일의 완료 상태를 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상태 변경 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "할 일을 찾을 수 없음")
    })
    public ResponseEntity<TaskResponse> updateTaskStatus(
            @Parameter(description = "할 일 ID", required = true) @PathVariable Long id,
            @Valid @RequestBody TaskUpdateStatusRequest request) {
        TaskResponse response = taskService.updateTaskStatus(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "할 일 삭제", description = "할 일을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "할 일 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "할 일을 찾을 수 없음")
    })
    public ResponseEntity<Void> deleteTask(
            @Parameter(description = "할 일 ID", required = true) @PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "할 일 조회", description = "할 일 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "할 일 조회 성공"),
            @ApiResponse(responseCode = "404", description = "할 일을 찾을 수 없음")
    })
    public ResponseEntity<TaskResponse> getTask(
            @Parameter(description = "할 일 ID", required = true) @PathVariable Long id) {
        TaskResponse response = taskService.getTask(id);
        return ResponseEntity.ok(response);
    }
}

