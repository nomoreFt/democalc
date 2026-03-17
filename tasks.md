# Tasks: [MANUAL] 계산기 나머지 연산 추가

## 구현 태스크

| # | 태스크 | 테스트 대상 | 상태 |
|---|--------|------------|------|
| T1 | 나머지 연산 기본 동작 테스트 | `CalculatorService: 10 % 3 = 1.0` | ✅ |
| T2 | 나머지 0으로 나누기 예외 테스트 | `CalculatorService: 10 % 0 → ArithmeticException` | ✅ |
| T3 | 나머지 연산 히스토리 저장 테스트 | `CalculatorService: % 후 historyService에 저장됨` | ✅ |
| T4 | API 통합 테스트 | `POST /api/calculate { operator: "%" }` | ✅ |

---

## T1: 나머지 연산 기본 동작 테스트

### RED — 작성할 테스트
- [ ] `10.0 % 3.0 = 1.0`
- [ ] `10.5 % 3.0 = 1.5` (부동소수점)
- [ ] `-7.0 % 3.0 = -1.0` (음수 나머지)

### GREEN — 구현할 내용
- 이미 `CalculatorService.computeResult()`에 `%` case 구현됨 → 테스트 통과 확인

### REFACTOR — 개선 포인트
- 없음

---

## T2: 나머지 0으로 나누기 예외 테스트

### RED — 작성할 테스트
- [ ] `10.0 % 0.0` → `ArithmeticException("0으로 나눌 수 없습니다")` 던짐

### GREEN — 구현할 내용
- `requireNonZeroDivisor`가 `/`와 `%` 모두 적용됨 → 테스트 통과 확인

### REFACTOR — 개선 포인트
- 없음

---

## T3: 나머지 연산 히스토리 저장 테스트

### RED — 작성할 테스트
- [ ] `%` 연산 성공 시 historyService에 1건 저장됨
- [ ] `%` 연산 실패(0 나누기) 시 historyService에 저장 안 됨

### GREEN — 구현할 내용
- `CalculatorService.calculate()`가 result 계산 후 `historyService.save()` 호출 → 통과 확인

### REFACTOR — 개선 포인트
- 없음

---

## T4: API 통합 테스트

### RED — 작성할 테스트
- [ ] `POST /api/calculate {"a":10,"b":3,"operator":"%"}` → `200 {"result":1.0}`
- [ ] `POST /api/calculate {"a":10,"b":0,"operator":"%"}` → `400`

### GREEN — 구현할 내용
- 컨트롤러 → 서비스 경로 이미 구현됨 → 통과 확인

### REFACTOR — 개선 포인트
- 없음
