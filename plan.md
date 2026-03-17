# Plan: [MANUAL] 계산기 나머지 연산 추가

## 현재 구조 분석

`CalculatorService.computeResult()`에 `%` case가 이미 있고 0 체크도 포함됨:
```kotlin
"%" -> {
    requireNonZeroDivisor(request.b)
    request.a % request.b
}
```

기존 테스트 파일:
- `CalculatorServiceHistoryTest`: +, /, @ 연산자 위주 (% 테스트 없음)
- `CalculatorControllerTest`: 컨트롤러 통합 테스트 (% 없음)

## 구현 전략

서비스 로직은 건드리지 않고 **테스트만 추가**한다.

1. 서비스 단위 테스트 → `CalculatorServiceHistoryTest`에 추가 또는 전용 파일 생성
2. 컨트롤러 통합 테스트 → `CalculatorControllerTest`에 추가

## 변경될 파일

| 파일 | 변경 유형 | 내용 |
|------|----------|------|
| `CalculatorServiceHistoryTest.kt` | 수정 | % 연산 단위 테스트 추가 |
| `CalculatorControllerTest.kt` | 수정 | % 연산 통합 테스트 추가 |

## 엣지 케이스
- `b = 0.0`일 때 `ArithmeticException` 던지는지 확인
- 음수 나머지: `-7 % 3` → Kotlin Double은 `-1.0` (Java 동일)
- 부동소수점: `10.5 % 3.0` → `1.5`

## 테스트 전략
- **단위 테스트**: `CalculatorService` 직접 호출, `CalculatorHistoryService` 실제 객체 사용
- **통합 테스트**: `@SpringBootTest` + `MockMvc`로 HTTP 레벨 검증
