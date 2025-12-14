# 리프레시 토큰 테스트 가이드

## 1. Swagger UI를 통한 수동 테스트

애플리케이션을 실행한 후 `http://localhost:8080/swagger-ui.html`에 접속하여 테스트할 수 있습니다.

### 테스트 시나리오

#### 1단계: 회원가입
1. **POST /auth/register** 엔드포인트 선택
2. 요청 바디 입력:
```json
{
  "name": "테스트유저",
  "email": "test@example.com",
  "password": "password123"
}
```
3. Execute 클릭
4. 응답에서 `accessToken`과 `refreshToken` 복사 및 저장

#### 2단계: 로그인
1. **POST /auth/login** 엔드포인트 선택
2. 요청 바디 입력:
```json
{
  "email": "test@example.com",
  "password": "password123"
}
```
3. Execute 클릭
4. 응답 확인 (accessToken, refreshToken 반환)

#### 3단계: 인증 필요 API 테스트
1. **GET /users/{id}** 등의 인증 필요 엔드포인트 선택
2. 우측 상단 **Authorize** 버튼 클릭
3. `Bearer {accessToken}` 형식으로 입력 (또는 accessToken만 입력)
4. Authorize 클릭
5. API 실행 - 정상 동작 확인

#### 4단계: 토큰 갱신
1. **POST /auth/refresh** 엔드포인트 선택
2. 요청 바디에 리프레시 토큰 입력:
```json
{
  "refreshToken": "eyJhbGc..."
}
```
3. Execute 클릭
4. 새로운 `accessToken`과 `refreshToken` 확인

#### 5단계: 로그아웃
1. **POST /auth/logout** 엔드포인트 선택
2. 요청 바디에 리프레시 토큰 입력:
```json
{
  "refreshToken": "eyJhbGc..."
}
```
3. Execute 클릭
4. 204 No Content 응답 확인

#### 6단계: 로그아웃 후 토큰 재사용 시도
1. **POST /auth/refresh** 엔드포인트 선택
2. 로그아웃한 리프레시 토큰으로 갱신 시도
3. 401 Unauthorized 에러 확인

## 2. PowerShell을 통한 테스트

### 회원가입
```powershell
$registerBody = @{
    name = "테스트유저"
    email = "test@example.com"
    password = "password123"
} | ConvertTo-Json

$registerResponse = Invoke-RestMethod -Uri "http://localhost:8080/auth/register" -Method Post -Body $registerBody -ContentType "application/json"
$accessToken = $registerResponse.accessToken
$refreshToken = $registerResponse.refreshToken

Write-Host "Access Token: $accessToken"
Write-Host "Refresh Token: $refreshToken"
```

### 로그인
```powershell
$loginBody = @{
    email = "test@example.com"
    password = "password123"
} | ConvertTo-Json

$loginResponse = Invoke-RestMethod -Uri "http://localhost:8080/auth/login" -Method Post -Body $loginBody -ContentType "application/json"
$accessToken = $loginResponse.accessToken
$refreshToken = $loginResponse.refreshToken

Write-Host "로그인 성공"
Write-Host "Access Token: $accessToken"
```

### 인증 필요 API 호출
```powershell
$headers = @{
    Authorization = "Bearer $accessToken"
}

$userResponse = Invoke-RestMethod -Uri "http://localhost:8080/users/1" -Method Get -Headers $headers
Write-Host "유저 정보: $($userResponse | ConvertTo-Json)"
```

### 토큰 갱신
```powershell
$refreshBody = @{
    refreshToken = $refreshToken
} | ConvertTo-Json

$refreshResponse = Invoke-RestMethod -Uri "http://localhost:8080/auth/refresh" -Method Post -Body $refreshBody -ContentType "application/json"
$accessToken = $refreshResponse.accessToken
$refreshToken = $refreshResponse.refreshToken

Write-Host "토큰 갱신 완료"
Write-Host "New Access Token: $accessToken"
```

### 로그아웃
```powershell
$logoutBody = @{
    refreshToken = $refreshToken
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/auth/logout" -Method Post -Body $logoutBody -ContentType "application/json"
Write-Host "로그아웃 완료"
```

### 로그아웃 후 토큰 재사용 시도 (에러 확인)
```powershell
try {
    $refreshBody = @{
        refreshToken = $refreshToken
    } | ConvertTo-Json
    
    Invoke-RestMethod -Uri "http://localhost:8080/auth/refresh" -Method Post -Body $refreshBody -ContentType "application/json"
} catch {
    Write-Host "예상된 에러 발생: $($_.Exception.Message)"
    Write-Host "로그아웃된 토큰은 재사용할 수 없습니다."
}
```

## 3. 전체 흐름 테스트 스크립트

아래는 전체 테스트를 자동으로 실행하는 PowerShell 스크립트입니다:

```powershell
$baseUrl = "http://localhost:8080"

Write-Host "=== 리프레시 토큰 테스트 시작 ===" -ForegroundColor Green

Write-Host "`n1. 회원가입 테스트" -ForegroundColor Yellow
$registerBody = @{
    name = "테스트유저$(Get-Random -Maximum 9999)"
    email = "test$(Get-Random -Maximum 9999)@example.com"
    password = "password123"
} | ConvertTo-Json

try {
    $registerResponse = Invoke-RestMethod -Uri "$baseUrl/auth/register" -Method Post -Body $registerBody -ContentType "application/json"
    $accessToken = $registerResponse.accessToken
    $refreshToken = $registerResponse.refreshToken
    Write-Host "✓ 회원가입 성공" -ForegroundColor Green
    Write-Host "  - User ID: $($registerResponse.userId)"
    Write-Host "  - Email: $($registerResponse.email)"
} catch {
    Write-Host "✗ 회원가입 실패: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

Write-Host "`n2. 인증 API 호출 테스트" -ForegroundColor Yellow
$headers = @{
    Authorization = "Bearer $accessToken"
}

try {
    $userResponse = Invoke-RestMethod -Uri "$baseUrl/users/$($registerResponse.userId)" -Method Get -Headers $headers
    Write-Host "✓ 인증 API 호출 성공" -ForegroundColor Green
    Write-Host "  - User Name: $($userResponse.name)"
} catch {
    Write-Host "✗ 인증 API 호출 실패: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n3. 토큰 갱신 테스트" -ForegroundColor Yellow
$refreshBody = @{
    refreshToken = $refreshToken
} | ConvertTo-Json

try {
    $refreshResponse = Invoke-RestMethod -Uri "$baseUrl/auth/refresh" -Method Post -Body $refreshBody -ContentType "application/json"
    $newAccessToken = $refreshResponse.accessToken
    $newRefreshToken = $refreshResponse.refreshToken
    Write-Host "✓ 토큰 갱신 성공" -ForegroundColor Green
    Write-Host "  - 새 Access Token 발급됨"
    Write-Host "  - 새 Refresh Token 발급됨"
    
    $accessToken = $newAccessToken
    $refreshToken = $newRefreshToken
} catch {
    Write-Host "✗ 토큰 갱신 실패: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n4. 새 토큰으로 API 호출 테스트" -ForegroundColor Yellow
$headers = @{
    Authorization = "Bearer $accessToken"
}

try {
    $userResponse = Invoke-RestMethod -Uri "$baseUrl/users/$($registerResponse.userId)" -Method Get -Headers $headers
    Write-Host "✓ 새 토큰으로 API 호출 성공" -ForegroundColor Green
} catch {
    Write-Host "✗ 새 토큰으로 API 호출 실패: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n5. 로그아웃 테스트" -ForegroundColor Yellow
$logoutBody = @{
    refreshToken = $refreshToken
} | ConvertTo-Json

try {
    Invoke-RestMethod -Uri "$baseUrl/auth/logout" -Method Post -Body $logoutBody -ContentType "application/json"
    Write-Host "✓ 로그아웃 성공" -ForegroundColor Green
} catch {
    Write-Host "✗ 로그아웃 실패: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n6. 로그아웃 후 토큰 재사용 테스트 (에러 예상)" -ForegroundColor Yellow
try {
    $refreshBody = @{
        refreshToken = $refreshToken
    } | ConvertTo-Json
    
    Invoke-RestMethod -Uri "$baseUrl/auth/refresh" -Method Post -Body $refreshBody -ContentType "application/json"
    Write-Host "✗ 로그아웃된 토큰이 재사용됨 (버그!)" -ForegroundColor Red
} catch {
    Write-Host "✓ 로그아웃된 토큰 재사용 차단됨 (정상)" -ForegroundColor Green
    Write-Host "  - 에러 메시지: $($_.Exception.Message)"
}

Write-Host "`n=== 테스트 완료 ===" -ForegroundColor Green
```

위 스크립트를 `test-refresh-token.ps1` 파일로 저장하고 실행:
```powershell
.\test-refresh-token.ps1
```

## 4. JUnit 통합 테스트

`AuthControllerTest.java` 파일이 생성되어 있습니다. 실행 방법:

```bash
cd app
./gradlew test --tests AuthControllerTest
```

## 5. 데이터베이스 확인

MySQL 클라이언트를 통해 리프레시 토큰이 올바르게 저장되고 삭제되는지 확인:

```sql
USE lavine;

SELECT * FROM users;

SELECT rt.id, rt.token, rt.expiry_date, u.email
FROM refresh_tokens rt
JOIN users u ON rt.user_id = u.id;

SELECT COUNT(*) as token_count
FROM refresh_tokens
WHERE user_id = 1;
```

로그인 시 토큰이 생성되고, 로그아웃 시 삭제되며, 사용자당 1개의 토큰만 유지되는지 확인합니다.

## 예상 결과

### 정상 동작
- ✓ 회원가입/로그인 시 accessToken과 refreshToken 발급
- ✓ accessToken으로 API 호출 성공
- ✓ refreshToken으로 새 토큰 발급 성공
- ✓ 로그아웃 후 refreshToken 무효화
- ✓ 사용자당 1개의 refreshToken만 DB에 존재

### 에러 케이스
- ✓ 잘못된 refreshToken → 401 Unauthorized
- ✓ 만료된 refreshToken → 401 Unauthorized
- ✓ 로그아웃된 refreshToken → 401 Unauthorized
- ✓ 존재하지 않는 refreshToken → 401 Unauthorized
