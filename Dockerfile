# Use Maven image for building the project
FROM maven:3.9.5-eclipse-temurin-21 AS build

WORKDIR /app

# Install dependencies
RUN apt-get update && apt-get install -y curl unzip \
    && curl -O https://dl.google.com/dl/cloudsdk/channels/rapid/downloads/google-cloud-cli-511.0.0-linux-x86_64.tar.gz \
    && tar -xvzf google-cloud-cli-511.0.0-linux-x86_64.tar.gz \
    && rm google-cloud-cli-511.0.0-linux-x86_64.tar.gz

# Install extra dependencies for Playwright (if needed)
RUN apt-get install -y libnss3 libatk-bridge2.0-0 libcups2 libxkbcommon-x11-0 libgbm1

# Add Google Cloud CLI to PATH
ENV PATH="/app/google-cloud-sdk/bin:$PATH"

# Copy project files
COPY . .

# Build project without running tests
RUN mvn clean install -DskipTests

# ---------------------------
# Runtime stage
# ---------------------------
FROM maven:3.9.5-eclipse-temurin-21 AS runtime

WORKDIR /app

# Copy built project from build stage
COPY --from=build /app /app

# Add Google Cloud CLI to PATH
ENV PATH="/app/google-cloud-sdk/bin:$PATH"

# Start interactive authentication before tests run
CMD gcloud auth login && mvn test
