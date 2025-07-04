FROM eclipse-temurin:17-jdk

# Instala Maven e curl
RUN apt-get update && apt-get install -y maven curl && rm -rf /var/lib/apt/lists/*

# Cria um usuário não-root para executar a aplicação
RUN groupadd -r spring && useradd -r -g spring spring

# Define o diretório de trabalho
WORKDIR /app

# Copia os arquivos do projeto
COPY pom.xml .
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn
COPY src ./src




# Baixa dependências e compila
RUN mvn clean package -DskipTests

# Muda a propriedade do arquivo para o usuário spring
RUN chown -R spring:spring /app

# Muda para o usuário spring
USER spring

# Expõe a porta da aplicação
EXPOSE 8082

# Comando para executar a aplicação
ENTRYPOINT ["sh", "-c", "java -jar target/*.jar"] 