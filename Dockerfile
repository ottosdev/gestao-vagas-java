# Etapa 1: Build
FROM maven:3.9.9-eclipse-temurin-17 AS build

# define diretório de trabalho dentro da imagem
WORKDIR /app

# copia pom.xml primeiro (cache do Maven)
COPY pom.xml .

# copia pasta src (código fonte)
COPY src ./src

# roda o build gerando o jar sem testes (opcional tirar -DskipTests)
RUN mvn clean package -DskipTests

# Etapa 2: Runtime
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app
EXPOSE 8080

# copia o jar gerado da etapa anterior
# ajusta esse nome se o seu .jar tiver outro nome
COPY --from=build /app/target/gestao_vagas-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
