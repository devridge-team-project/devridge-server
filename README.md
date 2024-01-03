## Docker 컨테이너 환경에서 실행하기 🐳

> 실행 전 아래 명령어를 먼저 실행해주세요.
>

```java
git submodule update --remote --init   # 첫 프로젝트 clone 혹은 업데이트 시 실행
./gradlew clean build   # 로컬에서 실행할 경우 생략
```

### MacOS

```
cd scripts
sh localbuild.sh
```

### WindowsOS

```
cd scripts
sh .localbuild.sh
```