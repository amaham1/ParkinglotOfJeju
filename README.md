# ParkinglotOfJeju

[![Video Label](http://img.youtube.com/vi/y4Eh5hBWL2A/0.jpg)](https://youtu.be/y4Eh5hBWL2A)

제주 실시간 주차장API를 활용한 앱입니다.

ViewModel 과 Livedata, Navigation, DataBinding을 활용하였습니다.

XMLPullPaser를 사용하였습니다.

개발언어는 Kotlin 입니다.


<h2>앱의 주요기능</h2> 

두 개의 탭으로 정보를 제공합니다

1. 목록 탭으로 보여줍니다
    - 가져온 주차장 정보를 목록으로 알려줍니다.
    - 주차장 정보가 마지막으로 언제 업데이트 되었는지 알려줍니다.
    - 각각의 주차장 정보를 클릭하면 레이아웃이 펼쳐지면서 더욱 많은 정보를 보여줍니다.
    - 다시 클릭하면 레이아웃이 접혀지면서 깔끔한 목록유지가 가능합니다.
    
2. 지도 탭으로 보여줍니다
    - 지도에 8개의 핀을 표시합니다
    - 핀을 클릭하면 주차장명, 일반주차, 장애인 주차 여부갯수가 말풍선으로 등장합니다.
    - 현재위치를 가져올 수 있습니다.

<h2>사용한 라이브러리</h2> 
    
    - Splitties 
        ㄴactivities: Intent 형식이 아닌 간단한 형식으로 사용하기 위해 사용하였습니다.
        ㄴtoast: 간단한  Toast 메시지를 사용하기 위해 사용하였습니다.
        
    - Shimmer: 주차장 정보를 목록으로 불러오는 과정동안 로딩화면을 동적으로 표시하기 위해 사용하였습니다.
    
    - Foldingcell: 주차장 정보 목록을 클릭하면 펼쳐지고 접혀지는 애니메이션을 위해 사용하였습니다.
    
    - Lottie: 지도에서 현재 위치를 불러올 때 반응형 버튼을 위해 사용하였습니다.
    
    - DaumMap: 지도 맵을 구현하고 각종 기능을 사용하기 위해 사용하였습니다.
    
    - Ted Permission: Permission의 효율적 관리를 위해 사용하였습니다.
    
    - KaKaoMap: 지도를 사용하기 위해 KaKaoMap을 이용하였습니다.
