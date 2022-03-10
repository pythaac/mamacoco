# MAMACOCO
 블로그 상태 동기화 서버  
&nbsp;  

## 요약
 MAMACOCO는 블로그와 상태가 동기화된 데이터를 제공하는 서버입니다. 블로그 API로 데이터를 가져와 가공해서 데이터베이스에 저장합니다. 이 때 블로그의 추가/삭제/변경된 카테고리/글의 상태를 동기화합니다. 그리고 REST API를 통해 이 데이터를 제공합니다.  
&nbsp;  
 
## 기능
- 블로그 동기화
- 글 내용 제공  
&nbsp;  

## 시스템 구성
<img src="/image/시스템 구성.png">  
&nbsp;  

## 데이터베이스 스키마
<img src="/image/scheme.png">  
&nbsp;  

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
&nbsp;  

## 동기화 과정

### TistorySyncRetriever

TistorySyncRetriever는 동기화를 위해 Tistory 블로그 데이터와 DB 데이터를 모아줍니다. 데이터를 모으기 위해 다음 4가지 과정으로 이루어져 있습니다.
- getPostBlog  
Tistory API를 통해 블로그의 글 상태를 가져옵니다.
- getPostDB  
DB에 저장된 블로그의 글 상태를 가져옵니다.
- getCategoryBlog  
Tistory API를 통해 블로그의 카테고리 상태를 가져옵니다.
- getCategoryDB  
DB에 저장된 블로그의 카테고리 상태를 가져옵니다.
<img src="/image/Retriever.png">  
&nbsp;  

### TistorySyncComparator

TistorySyncComparator는 TistorySyncRetriever를 통해 가져온 데이터를 비교합니다. 아래 그림은 블로그 데이터와 DB 데이터를 비교하는 과정을 묘사합니다.  
- 비교 시작 (Compare Strart)  
DB와 Blog의 상태는 각각 리스트 형태로 저장됩니다. 비교를 위해 각 리스트를 가리키는 인덱스(i_db, i_blog)를 가지며, 두 인덱스는 0에서 시작합니다.
- 비교 (Comparing)  
각 인덱스의 데이터를 비교합니다. 비교 과정에서 i_db만 1 증가, i_blog만 1 증가, 그리고 i_db와 i_blog 모두 1 증가할 수 있습니다.
- 비교 종료 (Compare End)  
i_db와 i_blog의 값이 모두 각각 DB 데이터 리스트와 블로그 데이터 리스트의 마지막에 도착하면 비교가 종료됩니다.
<img src="/image/Comparator.png">  
&nbsp;  

다음 그림은 카테고리 데이터를 비교하는 순서도를 나타냅니다.
- isCatCreate  
블로그에서 추가된 데이터로, DB에 생성해야할 카테고리로 판단하는 조건입니다.
- isCatDelete  
블로그에서 지워진 데이터로, DB에서 삭제해야할 카테고리로 판단하는 조건입니다.
- isCatUpdate  
블로그에서 수정된 데이터로, DB에서 수정해야할 카테고리로 판단하는 조건입니다.
<img src="/image/Comparator_Category.png">  
&nbsp;  

다음 그림은 글 데이터를 비교하는 순서도를 나타냅니다.
- isPostCreate  
블로그에서 추가된 데이터로, DB에 생성해야할 글로 판단하는 조건입니다.
- isPostDelete  
블로그에서 지워진 데이터로, DB에서 삭제해야할 글로 판단하는 조건입니다.
- isPostUpdate  
블로그에서 수정된 데이터로, DB에서 수정해야할 글로 판단하는 조건입니다.
<img src="/image/Comparator_Post.png">  
&nbsp;  

### TistorySyncExecuter

TistorySyncExecuter는 TistorySyncComparator를 통해 가져온 데이터를 바탕으로 동기화를 진행합니다. 동기화는 아래 순서대로 진행합니다.
1) createCat  
카테고리를 DB에 생성합니다.
2) createPost  
글을 DB에 생성합니다.
3) updatePost  
글을 DB에 갱신합니다.
4) deletePost  
글을 DB에서 삭제합니다.
5) updateCat  
카테고리를 DB에 갱신합니다.
6) deleteCat  
카테고리를 DB에서 삭제합니다.  
&nbsp;  

다음 그림은 TistorySyncExcuter의 동기화 과정을 나타냅니다.
<img src="/image/Executer1.png">  
<img src="/image/Executer2.png">  
<img src="/image/Executer3.png">  
<img src="/image/Executer4.png">  
<img src="/image/Executer5.png">  
<img src="/image/Executer6.png">  
&nbsp;  
