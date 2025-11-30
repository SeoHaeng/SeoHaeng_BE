# 서행 <img src="https://github.com/user-attachments/assets/5bf84f86-e400-46cb-b0c1-96ab54c4706f" align=left width=100>
> 강원도 도서 여행 큐레이션 플랫폼‘서행(書行)' 📖 - 백엔드 레포지토리

<br>

<aside>
<img width="3072" height="1734" alt="Frame 1707482464 (2)" src="https://github.com/user-attachments/assets/ee204242-7a90-4afc-bdb6-ef63e31d3371" />

<br>
  
# 소개
> - 프로젝트 기간: 25.05.19 - 25.09.18
> - **서행**은 MZ세대를 위한 감성 도서여행 큐레이션 플랫폼으로, 책과 여행을 결합한 **새로운 여행 경험**을 제공합니다.  
> - 강원도의 독립서점, 북스테이, 북카페, 감성 독서 스팟 등 도서 관련 장소를 중심으로 **지도 기반 도서여행 서비스**를 제공합니다.
> - 서비스 다운로드 링크 : [<img width="113" height="28" alt="image" src="https://github.com/user-attachments/assets/bf3df373-d991-41c5-9538-b1654a721c64" />](https://play.google.com/store/apps/details?id=com.gyurijake.seohaengfe&hl=ko)

<br>

# Overview
>📍 느린 여행 설계 및 기록
>손쉽게 나만의 독서 여행 루트를 설계하고 기록할 수 있어요.
>자연을 품은 강원도에서 나만의 독서 여행을 즐겨보세요.
>
>📍 강원도의 책방을 한눈에
>독립서점, 북카페, 북스테이까지, 강원도의 책방을 지도 위에서 쉽게 확인할 수 있어요.
>
>📍 나만의 독서 스팟 기록 및 공유
>강원도의 책 읽기 좋은 나만의 독서 스팟을 찾아 기록할 수 있어요.
>도서와 감상을 함께 남겨, 나만의 독서 스팟을 다른 서행가들에게 추천할 수 있어요.
>지도에 나만의 책갈피를 끼워보세요.
>
>📍 책으로 이어지는 인연
>북 챌린지를 통해 익명의 서행가와 책을 나누고, 또 다른 서행가를 위해 서점에 책을 남기며 마음이 이어지는 경험을 만들어보세요.
>
>
>강원도의 그림 같은 장소들과 작은 책방 사이, 추억을 새겨보세요.
>
<img width="3072" height="1950" alt="Group 18" src="https://github.com/user-attachments/assets/49ec0d71-5696-4196-9af4-c5e4e43a3627" />

<br>

# 주요 기능
>### 도서 여행 지도 시각화 기능 (**이정표**)
>- **카카오 Open API 기반 지도**에 강원도의 독립서점, 북스테이, 북카페, 음식점, 관광지, 축제, 감성 독서 스팟 등을 **커스텀 마커**로 시각화  
>- **TourAPI 국문 관광정보** 연동으로 상세 정보 제공  
>- 이용자가 **사진·후기 등록**, **저장·공감·공유** 가능한 커뮤니티 기능 포함  
>
>### 독서 스팟 및 도서 정보 기록·공유 기능 (**공간책갈피**)
>- **카카오 Open API 기반 지도**에 책 읽기 좋은 장소 등록  
>- **네이버 책검색 API** 연동으로 관련 도서 정보 함께 기록 및 공유  
>
>
>### 북챌린지 커뮤니티 기능
>- 앱 내 인증 게시판을 통해 **참여 서점에서 진행되는 익명 책 선물 릴레이**(북챌린지) 기록 및 공유  
>- 게시물에 **공감과 댓글** 기능을 통해 이용자 간 소통 가능  
>
>
>### 여행 일정 관리 기능 (**취향길목**)
>- 달력에서 날짜 선택 후 **TourAPI 국문 관광정보** 연동 검색 결과에서 여행지 추가  
>- 추가한 여행지를 지도에 표시해 **동선 효율적 설계**  
>- 각 장소의 상세 정보를 확인하며 **맞춤형 도서 여행 일정** 손쉽게 구성 가능  

<br>

# Backend Developers
| <center>이소정</center>| <center>성유진</center>|
| -------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------- |
| <center><img width="150px" src="https://avatars.githubusercontent.com/Sojeong0430" /></center> | <center><img width="150px" src="https://avatars.githubusercontent.com/uuujini" /></center> |
| <center>[@Sojeong0430](https://github.com/Sojeong0430)</center> | <center>[@uuujini](https://github.com/uuujini)</center>| 
<br/>

# Backend Tech Stack

>### Server
>- Java 17
>- Spring Boot 3.5
>- Gradle 
>- Spring Data JPA / Hibernate
>- Spring Security + OAuth2 Client
>- JWT
>
>### Database
>- MySQL
>- Redis
>
>### DevOps
>- Github
>- Docker / Docker Compose
>- Github Action
>
>### AWS
>- Amazon EC2
>- Amazon RDS
>- Amazon S3

</br>

# Architecture
<img width="3104" height="2404" alt="Frame 1 (1)" src="https://github.com/user-attachments/assets/38a2e9b6-f466-41a5-9d1a-c8e2b8c45862" />


</br>

```bash
# 서행 Backend Directory 구조

seohaeng
├─ main
│  ├─ java
│  │  └─ com
│  │      └─ seohaeng
│  │          └─ backend
│  │              ├─ domain
│  │              │  ├─ common
│  │              │  ├─ bookChallenge
│  │              │  ├─ place
│  │              │  ├─ readingSpot
│  │              │  ├─ review
│  │              │  ├─ travelCourse
│  │              │  └─ user
│  │              └─ global
│  │                  ├─ apiPayload
│  │                  ├─ aws
│  │                  ├─ configuration
│  │                  ├─ redis
│  │                  └─ security
│  └─ resources
│      ├─ static
│      └─ templates
└─ test
└─ java
└─ com
└─ seohaeng
└─ backend                                                       
```

<br>

# API 문서
![Adobe Express - Video Project 3 (1) (1)](https://github.com/user-attachments/assets/09f005d4-b509-4056-b0ea-8029e646b16f)



# 관련 링크
https://www.data.go.kr/data/15101578/openapi.do
