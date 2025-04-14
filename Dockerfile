# 베이스 이미지 설정 (Java 17 + Maven)
FROM eclipse-temurin:17-jdk

# 작업 디렉토리 설정
WORKDIR /app

# Maven Wrapper 복사
COPY .mvn/ .mvn
COPY mvnw .
COPY pom.xml .

# 의존성 먼저 설치 (캐시 최적화)
RUN ./mvnw dependency:go-offline

# 전체 프로젝트 복사
COPY . .

# 프로젝트 빌드
RUN ./mvnw package -DskipTests

# 실행할 JAR 파일 이름 (target/*.jar에서 추출)
CMD ["java", "-jar", "target/proj0929_proto-0.0.1-SNAPSHOT.jar"]
