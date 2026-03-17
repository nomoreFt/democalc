# 계산 히스토리 조회 API — TDD Task List

## 현재 상태 분석
- `CalculatorHistoryService`: `save()`, `getAll()`, `clearAll()` 구현 완료
- `HistoryItem`: 도메인 모델 완성 (id, a, b, operator, result, createdAt)
- `CalculatorService`: 계산 후 히스토리 자동 저장 연동 완료
- `CalculatorController`: `POST /api/calculate`만 존재, 히스토리 조회 엔드포인트 없음

## 구현할 기능
`GET /api/history` — 계산 히스토리 목록을 최신순으로 반환하는 REST API

---

## TDD Task List

### Task 1: GET /api/history 요청 시 200 OK와 빈 배열을 반환한다
- 히스토리가 없을 때 `GET /api/history`를 호출하면 200 OK와 `[]`를 반환해야 한다
- 컨트롤러에 `@GetMapping("/history")` 엔드포인트를 추가하는 것이 목표

### Task 2: GET /api/history 요청 시 저장된 히스토리 목록을 반환한다
- 계산 후 `GET /api/history`를 호출하면 저장된 항목이 JSON 배열로 반환되어야 한다
- 응답 JSON 각 항목에 id, a, b, operator, result, createdAt 필드가 포함되어야 한다

### Task 3: GET /api/history 요청 시 최신순으로 정렬된 목록을 반환한다
- 여러 계산 후 `GET /api/history`를 호출하면 가장 최근 항목이 첫 번째로 반환되어야 한다

---

## 진행 상황

| Task | Red | Green | Refactor | 완료 |
|------|-----|-------|----------|------|
| Task 1 | v | v | v | v |
| Task 2 | v | v | v | v |
| Task 3 | v | v | v | v |
