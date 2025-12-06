# 1. 빌드 단계
FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app
COPY . .
RUN chmod +x gradlew
RUN ./gradlew clean build -x test --no-daemon

# [핵심 수정 부분]
# build/libs 폴더에 jar가 2개 생기므로, 'plain'이 안 붙은 파일만 찾아서 app.jar로 복사해둡니다.
RUN find build/libs -name "*.jar" ! -name "*-plain.jar" -exec cp {} app.jar \;

# 2. 실행 단계
FROM eclipse-temurin:17-jre
WORKDIR /app

# 위에서 우리가 깔끔하게 골라낸 app.jar 파일 하나만 가져옵니다.
COPY --from=builder /app/app.jar app.jar

ENV TZ=Asia/Seoul
ENTRYPOINT ["java", "-jar", "app.jar"]
