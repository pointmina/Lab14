# Lab14

14-1 브로드캐스트 리시버 이해하기

브로드 캐스트 리시버 : 이벤트 모델로 실행되는 컴포넌트
많은 내용을 담지는 못하지만 잠깐 화면 전환이나 이벤트 발생시키기 위해 사용된다(?)
이벤트 실행시키고 자기는 바로 종료

리시버 인텐트는 엑티비티 수가 없어도 되고, 한개이상 있으면 모두 실행해버린다.

14-2 시스템 상태 파악하기

- 화면을 키거나 끄는 상황을 감지하는 브캐리는 메니페스트에 등록하면 실행안됨, 액티비티나 서비스 컴포넌트 코드에서 registerReceiver() 함수를 이용해 동적으로 등록해야만 한다.
- ex)
- ![image](https://github.com/pointmina/Lab14/assets/68779817/a5a8cf74-96cf-4e5b-8bf5-bb077f8cab6f)
- ![image](https://github.com/pointmina/Lab14/assets/68779817/37139017-d2a4-4005-9174-cec71cd109f3)
- 브캐리는 필요없는 순간이 되면 unregisterReceiver() 함수로 등록을 해제해줘야한다.



리시버 실행권한
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED"/>

- 전원 온오프
<action android:name="android.intent.action.BOOT_COMPLETED"/>

- 화면 온오프
Intent.ACTION_SCREEN_ON
Intent.ACTION_SCREEN_OFF

- 배터리 상태
Intent.ACTION_BATTERY_OKAY
Intent.ACTION_BATTERY_LOW
...
