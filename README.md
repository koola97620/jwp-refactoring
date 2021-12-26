# 키친포스

## 요구 사항

### 도메인 정리

#### 메뉴 (Menu) 

- 메뉴 생성 기능
  - 상품 가격은 0 이상 이어야 한다.
  - 메뉴 그룹이 등록되어 있어야 한다.
  - 메뉴 상품이 상품에 저장되어 있어야 한다.
  - 상품 가격 * 상품 개수의 합을 구한다.
  - 메뉴 저장시 메뉴 상품도 함께 저장한다.
- 메뉴 조회 기능
  - 모든 메뉴와 메뉴 상품을 조회한다.

#### 메뉴 그룹 (MenuGroup)

- 메뉴 그룹 생성 기능
  - 메뉴 그룹을 저장한다.
- 메뉴 그룹 조회 기능
  - 모든 메뉴 그룹 조회한다.

#### 주문 (Order)

- 주문 생성 기능
  - 주문 항목이 없으면 주문을 생성할 수 없다.
  - 주문 항목은 메뉴에 등록 되어 있어야 한다.
  - 주문 상태는 COOKING 이다.
  - ~~주문 테이블이 등록되어 있어야 한다.~~
  - 주문 테이블이 비어있으면 안된다. (empty=false)
  - 주문을 저장할 때 주문항목도 함께 저장된다.
- 주문 조회 기능
  - 모든 주문과 주문 항목을 조회한다.
- 주문 상태 변경 기능
  - 주문상태가 COMPLETION 이면 상태를 변경할 수 없다.
  - 주문에 속한 주문 항목을 조회한다.


#### 상품 (Product)

- 상품 생성 기능
  - 상품 가격은 0 이상 이어야 한다.
  - 상품을 저장한다.
- 상품 조회 기능
  - 모든 상품을 조회한다.

#### 주문 테이블 (OrderTable)

- 주문 테이블 생성 기능
  - 단체 지정 초기화 후 저장한다.
- 주문 테이블 공석 변경 기능
  - 저장된 주문 테이블에 단체 지정 되어 있지 않으면 변경할 수 없다
  - 주문 상태가 COOKING, MEAL 이면 변경할 수 없다.
  - 주문 테이블을 저장한다.
- 주문 테이블 방문한 손님 수 변경 기능
  - 변경하려는 손님 수가 0보다 작을 수 없다.
  - 저장된 주문 테이블이 없으면 변경할 수 없다.
  - 저장된 주문 테이블이 비어있으면 변경할 수 없다.
  - 방문한 손님 수 변경 후 저장한다.

#### 단체 지정 (Table Group)

- 단체 지정 생성 기능
  - 주문 테이블이 없거나 주문 테이블 수가 2보다 작으면 단체 생성할 수 없다.
  - 주문 테이블이 등록되어 있어야 한다.
  - 주문 테이블이 비어있어야 한다. (empty=true)
  - 이미 단체가 지정된 주문 테이블이 있으면 안된다.
  - 단체 지정과 주문 테이블을 함께 저장한다.
- 주문테이블 단체 해지 기능 
  - 주문테이블이 저장 되어 있어야 한다.
  - 주문의 상태가 COOLING, MEAL 이면 해지가 불가능하다.
  - 주문테이블에 지정된 단체를 해지한다.

#### 메모

- kitchenpos 라는 도메인 아래에 3가지 하위 도메인이 존재하는 것 같다.
- 주문, 메뉴, 테이블

- Order, OrderLintItem
  - Order 가 List<OrderLIneItem> 포함
  - OrderLineItem 이 OrderId 를 가지고 있다.
- Menu, MenuProduct, Product
  - Menu 가 List<MenuProduct> 포함
  - MenuProduct 이 MenuId 를 가지고 있다.
  - Menu 와 Product 는 다대다 관계.
  - MenuProduct 가 MenuId, ProductId 를 가지고 있다.
- OrderTable, TableGroup
  - TableGroup 이 List<OrderTable> 포함 (조회용?)
  - OrderTable 가 TableGroupId 를 가지고 있다.


### 구현 기능 목록

- [ ] 주문 테스트 생성
  - [x] 주문 인수테스트 생성
  - [ ] 주문 단위 테스트 생성
  - [ ] 주문 통합 테스트 생성
- [ ] 메뉴 테스트 생성
  - [x] 메뉴 인수테스트 생성
  - [ ] 메뉴 단위 테스트 생성
  - [ ] 메뉴 통합 테스트 생성
- [ ] 테이블 테스트 생성
  - [x] 테이블 인수테스트 생성
  - [ ] 테이블 단위 테스트 생성
  - [ ] 테이블 통합 테스트 생성

## 용어 사전

| 한글명 | 영문명 | 설명 |
| --- | --- | --- |
| 상품 | product | 메뉴를 관리하는 기준이 되는 데이터 |
| 메뉴 그룹 | menu group | 메뉴 묶음, 분류 |
| 메뉴 | menu | 메뉴 그룹에 속하는 실제 주문 가능 단위 |
| 메뉴 상품 | menu product | 메뉴에 속하는 수량이 있는 상품 |
| 금액 | amount | 가격 * 수량 |
| 주문 테이블 | order table | 매장에서 주문이 발생하는 영역 |
| 빈 테이블 | empty table | 주문을 등록할 수 없는 주문 테이블 |
| 주문 | order | 매장에서 발생하는 주문 |
| 주문 상태 | order status | 주문은 조리 ➜ 식사 ➜ 계산 완료 순서로 진행된다. |
| 방문한 손님 수 | number of guests | 필수 사항은 아니며 주문은 0명으로 등록할 수 있다. |
| 단체 지정 | table group | 통합 계산을 위해 개별 주문 테이블을 그룹화하는 기능 |
| 주문 항목 | order line item | 주문에 속하는 수량이 있는 메뉴 |
| 매장 식사 | eat in | 포장하지 않고 매장에서 식사하는 것 |
