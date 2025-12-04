> 테스트와 테스트 주도 개발
> - 테스트란 무엇일까?
>   - 단위 테스트와 통합 테스트
>   - 테스트 주도 개발이란?

## 테스트란 무엇일까
> 소프트웨어 테스트는 프로그램이 의도한 대로 정확하게 동작하는지 확인하는 과정이다

테스트의 목적은 다음과 같다.
- 버그 발견 : 코드의 오류를 조기에 발견
  - 버그 수정 비용은 시간이 지날 수록 증가한다. (개발-QA-배포-운영 등 뒤쪽 단계에서 발견될 수록 수정 난이도와 비용이 급증)
- 품질 보증 : 소프트웨어의 신뢰성 향상
  - 여기서 신뢰성은 예상대로 동작하고, 예상하지 못한 상황에서도 안전하게 처리할 수 있는 능력을 말한다
- 문서화 : 코드의 사용 방법과 의도를 명시
  - 테스트 코드는 그 자체로 살아있는 문서의 역할을 한다. 주석이나 문서와 달리 항상 실제 코드와 일치함을 보여줄 수 있다.
  - API 사용법 등을 테스트를 통해 보여줄 수 있다.
- 리팩토링 안정성 : 코드 변경 시 기존 기능이 깨지지 않음을 보장
  - 리팩토링을 할 때, 테스트를 통과한다면 안전하게 리팩토링 되었음을 보장한다.

결국 테스트 코드는 **지속 가능한 개발**을 할 수 있도록 하는 핵심 인프라로 볼 수 있다.

### 단위 테스트
> 가장 작은 단위의 코드(함수, 메서드, 클래스 등)에 대한 독립적인 테스트

작은 단위를 테스트하기 때문에 빠르고, 범위가 명확하며 문제 위치를 파악하기 용이하다.

의존성을 분리하기 위해 Mock과 Stub을 사용한다.

예를들어 UserService에서 유저 생성 시 이메일을 발송하는 로직이 있다면 테스트 할 때마다 실제 이메일이 발송될 수 있다.
Mock은 실제 객체의 동작을 흉내내는 가짜 객체로 Mock을 생성해서 Service에 주입하면 실제로 이메일은 발송하지 않으면서 UserService의 로직만 테스트 한다.

Stub은 미리 정의해둔 응답을 반환하는 객체이다. 

예를 들어 DB 쿼리를 테스트 해야하는 경우 실제로 쿼리해야 하는 경우 
실제 응답값을 받아오는 것이 아니라 미리 정의된 값을 반환하도록 Stub을 사용하여 DB없이 빠르게 반환하도록 할 수 있다.
실제 DB와 연결이 되어있지 않아도 테스트가 가능해진다. (DB 의존성 분리)


### 통합 테스트
> 여러 모듈이 함께 동작하는지 테스트 (DB, API, 외부 서비스 등)

특징은 다음과 같다.
- 실제 동작 환경과 유사한 조건에서 테스트 수행
    - ex: 실제 DB, 외부 API, 메시지 큐 등과 연동
- 모듈 간 상호작용 검증
    - 데이터 흐름, 트랜잭션 처리, 서비스 간 의존 관계 등을 확인
- 단위 테스트보다 느리고 복잡
    - I/O가 발생하며, 설정 과정이 필요하기 때문
- 인수 테스트(E2E 테스트)와 비교하면 범위는 좁지만, 구성 요소의 올바른 연동 여부에 집중한다.

예를 들어 UserService와 DB를 실제로 연동하여 유저 생성 -> 저장 -> 조회가 전체 흐름대로 동작하는지 검증할 수 있다.

둘을 한줄로 비교하면 단위 테스트는 개별 모듈의 정확한 동작에 집중하고, 통합 테스트는 모듈 간 연결과 동작 흐름을 보장한다.

## 테스트 주도 개발이란
> 기능 구현을 먼저 하고 기능을 테스트 하는 코드를 작성하는 것이 아니라 반대로 테스트 코드를 먼저 작성하고 테스트를 통과시키기 위한 최소한의 코드를 구현하는 개발 방식


"테스트 코드를 통해 요구사항을 정의하고, 구현은 그 요구를 충족시키기 위해 존재한다"는 철학을 기반으로 한다.

TDD는 아래와 같은 반복 사이클을 따른다.
>Red -> Green ->Refactor
- Red: 실패하는 테스트를 작성
- Green: 테스트를 통과시키기 위한 최소한의 구현
- Refactor: 중복 제거, 구조 개선(테스트로 안정성 확보)

### TDD의 장점
장점은 다음과 같다.
- 요구사항 명세 명확화
    - 테스트가 곧 요구사항이 되기 때문에 기능 범위가 명확해진다.
- 설계 품질 향상
    - 테스트가 설계를 이끌어 책임 분리가 자연스럽게 발생한다.
- 불필요한 코드 방지
    - 테스트를 통과하기 위한 최소한의 코드만 작성하게 된다.
- 리팩토링 안정성 보장
    - 매 사이클마다 리팩토링이 강제되어 구조적으로 품질이 지속적으로 유지된다.

이러한 장점들을 바탕으로 다음과 같은 상황에 적절하다.
- 비즈니스 규칙이 복잡하고 도메인 설계가 중요한 환경
- 기능 변경 및 확장이 자주 일어나는 서비스
- 기술 구현보다 정확한 프로덕트 요구사항이 핵심인 경우

반대로 UI 중심의 애니메이션이나 렌더링 위주 기능 혹은 프로토타이핑 같은 빠른 작업이 필요한 경우에서는 적절하지 않다.



---

> 자바에서의 테스트 코드 작성
> - 자바에서 테스트를 위해 사용되는 메소드
> - 실제 자바에서의 테스트 코드 예시
>   - Repository Layer의 테스트
>   - Service Layer의 테스트
>   - Controller Layer의 테스트


## 자바에서 테스트 코드 작성
### 자바에서 테스트 작성에 주로 사용하는 도구

#### JUnit
- 자바 표준 테스트 프레임 워크
- 주로 사용되는 메서드
    - `@Test`: 테스트 메서드 표시
    - `@BeforeEach`: 테스트 실행 전 매번 공통 준비
    - `@AfterEach`: 테스트 실행 후 정리 작업
    - `@DisplayName`: 테스트 설명(가독성 향상)
    - `@PrameterizedTest`: 다양한 입력값으로 테스트 반복
#### Mockito
- Mock 객체 생성 및 행동 검증
#### Spring Test
- 스프링 빈 주입, 컨텍스트 로딩 테스트
- `@SpringBootTest`, `@WebMvcTest`, `@DataJpaTest` 등 계층별 테스트를 지원

### Given-When-Then 패턴
> 테스트 코드의 구조를 명확하게 만드는 가독성 높은 작성 패턴

Given-When-Then은 BDD(Behavior Driven Development)에서 유래한 테스트 작성 방식으로, 테스트의 의도를 명확히 전달하는 구조를 제공한다.

**구조:**
- **Given (준비)**: 테스트를 위한 초기 상태 및 전제 조건 설정
  - 테스트에 필요한 데이터 생성
  - Mock 객체의 동작 정의
  - 데이터베이스 초기 상태 설정
  
- **When (실행)**: 테스트하려는 실제 동작 수행
  - 테스트 대상 메서드 호출
  - API 요청 실행
  - 검증하고자 하는 행위 수행
  
- **Then (검증)**: 예상 결과와 실제 결과 비교
  - 반환값 검증
  - 상태 변화 확인
  - 예외 발생 여부 확인
  - Mock 객체 호출 여부 검증

**장점:**
- 테스트의 의도가 명확하게 드러남
- 준비-실행-검증 단계가 명확히 분리되어 가독성 향상
- 팀 내 일관된 테스트 코드 스타일 유지 가능

**예시:**
```java
@Test
@DisplayName("유저 생성 테스트")
void createUser() {
    // given - 테스트에 필요한 데이터 준비
    String userName = "홍길동";
    String email = "hong@example.com";
    
    // when - 실제 테스트할 동작 수행
    User createdUser = userService.createUser(userName, email);
    
    // then - 결과 검증
    assertThat(createdUser).isNotNull();
    assertThat(createdUser.getName()).isEqualTo(userName);
    assertThat(createdUser.getEmail()).isEqualTo(email);
}
```

이러한 구조는 아래의 계층별 테스트 예시에서도 일관되게 사용된다.

## 계층별 테스트 예시

### Repository Layer Test 
리포지토리에서는 주로 DB쿼리와 영속성 테스트가 주를 이룬다.

`@SpringBootTest`와 `@Transactional`을 함께 사용하여 데이터베이스 통합 테스트를 수행한다.

```Java
@SpringBootTest
@Transactional
@DisplayName("UserRepository 테스트")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("유저 ID로 조회 - 성공")
    void findById_Success() {
        // given
        User user = new User("홍길동");
        User savedUser = userRepository.save(user);

        // when
        Optional<User> found = userRepository.findById(savedUser.getId());

        // then
        assertThat(found).isPresent(); // AssertJ를 통해 사용하는 검증 메서드
        assertThat(found.get().getId()).isEqualTo(savedUser.getId());
        assertThat(found.get().getName()).isEqualTo("홍길동");
    }

    @Test
    @DisplayName("유저 ID로 조회 - 존재하지 않는 경우")
    void findById_NotFound() {
        // given
        Long nonExistentId = 999L;

        // when
        Optional<User> found = userRepository.findById(nonExistentId);

        // then
        assertThat(found).isEmpty();
    }
}
```

### Service Layer Test
비즈니스 로직은 주로 Mocking을 이용하여 DB 등과 외부 의존성을 제거하고 작업한다. 
```Java
@ExtendWith(MockitoExtension.class)
@DisplayName("UserService 테스트")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("유저 조회 - 성공")
    void getUser_Success() {
        // given
        Long userId = 1L;
        User user = new User("홍길동");

        given(userRepository.findById(userId))
                .willReturn(Optional.of(user));

        // when
        UserResponse response = userService.getUser(userId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("홍길동");
    }

    @Test
    @DisplayName("유저 조회 - 존재하지 않는 경우")
    void getUser_NotFound() {
        // given
        Long userId = 999L;
        given(userRepository.findById(userId))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.getUser(userId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("유저를 찾을 수 없습니다");
    }
}
```
### Controller Layer Test
HTTP 요청과 응답을 검증 하는 것이 중요하다. 

Service를 Mocking하여 Controller 레이어만 단위 테스트하는 방식을 사용한다.

**조회 기능 테스트 예시:**
```Java
@ExtendWith(MockitoExtension.class)
@DisplayName("UserController 테스트")
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    @DisplayName("유저 조회 API - 성공")
    void getUser_Success() {
        // given
        Long userId = 1L;
        UserResponse userResponse = new UserResponse(userId, "홍길동");
        given(userService.getUser(userId)).willReturn(userResponse);

        // when
        ResponseEntity<UserResponse> response = userController.getUser(userId);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isEqualTo(userId);
        assertThat(response.getBody().name()).isEqualTo("홍길동");
    }

    @Test
    @DisplayName("유저의 그룹 목록 조회 API - 성공")
    void getUserGroups_Success() {
        // given
        Long userId = 1L;
        
        TaskResponse taskResponse = new TaskResponse(1L, "할일1", false, 1L, userId);
        GroupResponse group1 = new GroupResponse(1L, "프로젝트", userId, List.of(taskResponse));
        GroupResponse group2 = new GroupResponse(2L, "개인", userId, List.of());

        given(userService.getUserGroups(userId)).willReturn(List.of(group1, group2));

        // when
        ResponseEntity<List<GroupResponse>> response = userController.getUserGroups(userId);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(2);
    }
}
```

#### 예시에서 사용된 어노테이션들 부가 설명
| 어노테이션                                 | 사용 위치  | 목적 / 설명                                                                |
| ------------------------------------- | ------ | ---------------------------------------------------------------------- |
| `@SpringBootTest`                     | 클래스    | **전체 애플리케이션 컨텍스트 로딩** Repository 테스트 등 통합 테스트 수행                       |
| `@Transactional`                      | 클래스    | 테스트 메서드 실행 후 자동으로 롤백하여 데이터베이스를 초기 상태로 유지                              |
| `@ExtendWith(MockitoExtension.class)` | 클래스    | JUnit5에서 Mockito 사용을 활성화 → `@Mock`, `@InjectMocks` 동작                  |
| `@Mock`                               | 필드     | 순수 Mockito Mock 객체 생성 (Spring 컨텍스트와 무관)                                |
| `@InjectMocks`                        | 필드     | `@Mock` 객체를 주입받아 테스트 대상 객체 생성                                          |
| `@Autowired`                          | 필드/생성자 | Spring 컨텍스트에 등록된 Bean을 의존성 주입                                          |
