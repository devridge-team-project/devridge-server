## 개발자들을 위한 지식 공유 플랫폼, DEVRIDGE 🌁

### 배포 주소
[Devridge](https://devridge-client.vercel.app/)

### 프로젝트 소개
> Bridge for Developer, DEVRIDGE
>
* 개발자들을 위한 정보 공유 플랫폼 웹 사이트입니다.
* 커피챗, Q&A 지식 공유와 같은 개발자들을 위한 다양한 기능들을 제공합니다.
### System Architecture
![image](https://github.com/devridge-team-project/devridge-server/assets/96467030/0a8c6af6-10c0-4978-ab24-11596736f4af)



#### UI/UX
- Figma
#### Client
- TypeScript
- react 18
- tailwind
- @tanstack/react-query
- zustand
#### Server
- Spring Boot 2.7.5
- JDK 11
#### DevOps
- Nginx
- Docker
- GitHub Actions
- AWS Service (EC2, S3, RDS, CloudFront)

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
