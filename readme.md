# OnePlusOne API Application

---
## 프로젝트 통합 개요
### 프로젝트 설명
OnePlusOne은 사용자들에게 편의점 행사 상품 및 PB 상품 정보를 제공하는 애플리케이션입니다.  
사용자는 앱을 통해 원하는 상품을 검색하거나 찜하기 기능을 이용할 수 있으며, 지도를 통해 주변 편의점의 위치와 정보를 쉽게 찾아볼 수 있습니다.

### 프로젝트 목적
OnePlusOne API 서버는 애플리케이션에 필요한 데이터를 제공하는 핵심 역할을 합니다.  
크롤링 기술을 활용하여 최신 편의점 상품 정보를 수집하고, 사용자의 검색 요청에 대한 응답, 주변 편의점 정보 제공 등의 기능을 수행합니다.

### 개발 기간
2024년 4월부터 현재까지 진행 중

---
## 레포지토리 세부사항
### 기술 스택
- **Android Studio Iguana**: 2023.2.1
- **Java**: 8
- **compileSdk**: 34

## 주요 기술
---
- **Live Data**
- **DataBinding**
- **ViewBinding**
- **ViewModel**
- **Retrofit**
- **Dagger Hilt**
- **Coroutines**
- **Room**
- **OkHttp**
- **GoogleMap**
- **Glide**


### 주요 기능
- 상품 가져오기 기능
    - 서버로 부터 GS25, 세븐일레븐, CU, Emart24의 상품 정보를 가져와 화면에 출력
    - 어플을 실행시킬 때 마다 서버로 부터 업데이트 여부를 확인 후 업데이트가 있다면 서버로 부터 상품 정보를 가져와 Room DB에 저장 시키는 기능


- Paging 기능
    - DB에 저장되어 있는 상품 정보를 Paging 3라이브러리를 통해 일정 개수만큼 가져와 화면에 출력하는 기능

    - 푸쉬 알림 기능
        - 사용자가 작성한 게시물에 새로운 댓글이 추가됐다면 푸쉬 알림을 보내는 기능
        - 알림 터치 시 해당 게시물로 이동하는 기능

- 필터 & 검색 기능
    - 각종 필터 기능을 통해 원하는 종류의 상품을 필터링 할 수 있음

    - 검색 기능
        - 일반 검색
        - 실시간 인기 검색어
        - 최근 검색어

    - 필터 종류
        - 편의점 종류 별
        - 상품의 카테고리 별(미완성)
        - 행사 종류 별
        - PB상품 여부


- 지도 기능
    - Google Map 를 통해 현재 사용자의 위치를 확인해 주위에 있는 편의점들의 정보를 화면에 표시하는 기능

    - 100M 마다 갱신

- 상품 즐겨 찾기 기능
    - 사용자는 자신이 관심 있는 행사 상품을 즐겨 찾기 할 수 있음


### 🏙 결과
| ![기본화면](https://github.com/user-attachments/assets/e88d7b8b-783d-4200-8d22-97abb3335f9f) | ![메인필터 on](https://github.com/user-attachments/assets/7c5bffc0-8cf6-4666-96ba-889bb0b54f43) |
|---|---|

|![지도 처음](https://github.com/user-attachments/assets/76f779c9-425c-4913-ae29-736560e21d88) | ![지도](https://github.com/user-attachments/assets/1bb4a971-99b1-4541-a80b-d10b40267a55) |
|---|---|


|![검색 연관검색어](https://github.com/user-attachments/assets/6d6b6b16-805a-44db-a77e-7685517674c7) | ![검색 완료](https://github.com/user-attachments/assets/8db10b60-723a-4cf6-8e14-6b5c4171d449) |
|---|---|

|![찜한상품](https://github.com/user-attachments/assets/b894f4f8-3470-4429-adce-8332aa7417a1) | ![상품 없음](https://github.com/user-attachments/assets/4342ad4f-9b94-456b-b82f-3c8c40003995) |
|---|---|
