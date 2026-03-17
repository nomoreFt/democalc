# 계산 히스토리 조회 API — TDD Task List

## 현재 상태 분석
- `CalculatorHistoryService`: `save()`, `getAll()`, `clearAll()` 구현 완료
- `HistoryItem`: 도메인 모델 완성 (id, a, b, operator, result, createdAt)
- `CalculatorService`: 계산 후 히스토리 자동 저장 연동 완료
- `CalculatorController`: `GET /api/history` 전체 목록 조회 완료
- `CalculatorControllerTest`: 히스토리 조회 테스트 3종 완료

## 구현할 기능
`GET /api/history?operator=+` — 연산자별 히스토리 필터링 API

---

## TDD Task List (히스토리 필터링)

### Task 4: GET /api/history?operator=+ 요청 시 해당 연산자의 항목만 반환한다
- operator 파라미터가 있으면 해당 연산자의 히스토리만 반환해야 한다
- 여러 연산자로 계산 후 `?operator=+`를 전달하면 덧셈 항목만 응답에 포함되어야 한다

### Task 5: GET /api/history?operator= 파라미터 없이 요청 시 전체 목록을 반환한다 (기존 동작 유지)
- operator 파라미터를 생략하면 기존처럼 전체 히스토리가 반환되어야 한다
- 기존 테스트가 모두 통과해야 한다

### Task 6: GET /api/history?operator=x 지원하지 않는 연산자 요청 시 400을 반환한다
- +, -, *, / 외의 연산자를 operator 파라미터로 전달하면 400 Bad Request를 반환해야 한다

---

## 진행 상황

| Task | Red | Green | Refactor | 완료 |
|------|-----|-------|----------|------|
| Task 1 | v | v | v | v |
| Task 2 | v | v | v | v |
| Task 3 | v | v | v | v |
| Task 4 | v | v | v | v |
| Task 5 | v | v | v | v |
| Task 6 | v | v | v | v |
