# MAMACOCO
 블로그 상태 동기화 서버

## 요약
 MAMACOCO는 블로그와 상태가 동기화된 데이터를 제공하는 서버입니다. 블로그 API로 데이터를 가져와 가공해서 데이터베이스에 저장합니다. 이 때 블로그의 추가/삭제/변경된 카테고리/글의 상태를 동기화합니다. 그리고 REST API를 통해 이 데이터를 제공합니다.
 
## 기능
- 블로그 동기화
- 글 내용 제공

## 시스템 구성
<img src="/image/시스템 구성.png">

## 모듈
<img src="/image/모듈 관계도.png">

### 블로그 API
- Tistory API  
Tistory API로 블로그 데이터를 수신
- TistoryAPIMapper  
Tistory에서 정의한 데이터를 내부 데이터로 변경
### Parser
- XMLParser  
XML을 쉽게 파싱하기위해 사용
- TistoryXMLParser  
Tistory 데이터를 XML로 가져왔을 때 파싱하기위해 사용
### Sync
- TistorySyncRetriever  
비교를 위해 데이터베이스/API로 블로그 데이터를 모음
- TistorySyncComparator  
데이터베이스와 블로그이 상태를 비교하여 변경사항을 확인
- TistorySyncExecuter  
TistorySyncComparator에서 비교한 내용으로 동기화

## 동기화 과정

## TistorySyncRetriever
<img src="/image/Retriever.png">

## TistorySyncComparator
<img src="/image/Comparator.png">
