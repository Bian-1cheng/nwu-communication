# 使用Java 17 JRE作为基础镜像
FROM eclipse-temurin:17-jre

# 工作目录
WORKDIR /app

# 复制应用jar到容器内
COPY nwu-communication-0.0.1-SNAPSHOT.jar app.jar

# 暴露端口
EXPOSE 8000

# 设置环境变量，激活生产环境配置
ENV SPRING_PROFILES_ACTIVE=prod

# 入口点和命令结合，指定JVM参数激活生产配置并启动jar
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]