# 📌 Project Overview

This project automates and tests **Google Cloud Storage CLI** commands using **TestNG**, **Allure** for reporting, and **Playwright** for signed URL validation.

---

# Running the Project with Docker

This section provides step-by-step instructions on how to build and run the project using Docker.

## Steps to Run the Docker Container

1. **Build the Docker Image:**
   ```sh
   docker build -t gcloud-tests .
   ```

2. **Run the Docker Container:**
   ```sh
   docker run -it --rm gcloud-tests
   ```

   This command will start the container and prompt you for authentication.

3. **Authenticate with Google Cloud:**
   - When you run the container, you will see a terminal output similar to this:
     
     ![image](https://github.com/user-attachments/assets/33dbf978-a007-4a9e-8350-e7aa7cfc504c)

   
   - Copy and paste the provided URL into your browser to authenticate your account.
   
   - Enter the verification code back into the terminal.

4. **Verify that the Project and Bucket are Created:**
   - After authentication, the setup script will create the necessary Google Cloud project and storage bucket.
   - You can verify this by running:
     ```sh
     gcloud projects list
     gcloud storage buckets list
     ```

5. **Expected Output:**
   - After a successful setup, you should see output confirming the creation of the project and bucket
     

Now your Google Cloud project and bucket are ready for testing!


---


## 📂 Features

✅ **Automated tests for Google Cloud Storage CLI commands:**
- Create a bucket
- Upload a file
- List files in a bucket
- Generate a signed URL

🔄 **Modular test framework** for easy expansion

📜 **Allure Reporting** for clear test execution insights

🏗 **Maven-based project** for easy build & dependency management

---

## 🚀 Getting Started

### 1️⃣ Prerequisites
Make sure you have the following installed:
- **Java 21+** ✅
- **Maven** ✅
- **Google Cloud SDK** ✅ *(Ensure gcloud CLI is authenticated and configured)*
- **Allure CLI** ✅ *(For test reporting)*

---

### 2️⃣ Clone the Repository
```sh
git clone https://github.com/yourusername/CloudStorageCLI-Testing.git
cd CloudStorageCLI-Testing
```

---

### 3️⃣ Configure Project Settings
Edit `ProjectConfig.java` to match your GCP setup:
```java
public class ProjectConfig {
    public static final String BUCKET_NAME = "your-bucket-name";
    public static final String TEST_FILE_NAME = "test-file.txt";
    public static final String G_CLOUD_PATH = "\"C:\\Program Files (x86)\\Google\\Cloud SDK\\google-cloud-sdk\\bin\\gcloud.cmd\"";
}
```

---

### 4️⃣ Run Tests
Execute tests using Maven:
```sh
mvn clean test
```

---

### 5️⃣ Generate Allure Report
After running the tests, generate the Allure report:
```sh
allure serve target/allure-results
```

---

## 📝 Test Cases

| Test Name              | Description                                  |
|------------------------|----------------------------------------------|
| **testCreateBucket**   | Checks if the bucket exists, creates if not |
| **testUploadFile**     | Uploads a test file to the bucket           |
| **testListFiles**      | Verifies the uploaded file exists in bucket |
| **testGenerateSignedUrl** | Generates a signed URL for the uploaded file |

### 🔹 Associated gcloud Commands

| Operation | gcloud Command |
|-----------|----------------|
| **Create Bucket** | `gcloud storage buckets create gs://<BUCKET_NAME> --location=me-west1` |
| **Upload File** | `gcloud storage cp test-file.txt gs://<BUCKET_NAME>/` |
| **List Files** | `gcloud storage ls gs://<BUCKET_NAME>/` |
| **Generate Signed URL** | `gcloud storage sign-url --duration=10m gs://<BUCKET_NAME>/test-file.txt` |

---

## 📊 Reporting System
The project integrates **Allure Reports** for test execution insights. Reports include:
- **Test results** (pass/fail)
- **Execution time breakdown**
- **Detailed logs per test**

---

## 📂 Project Structure

```
CloudStorageCLI-Testing/
│── .allure/                 # Allure reporting files
│── .idea/                   # IntelliJ project settings
│── allure-results/          # Allure test results
│── src/
│   ├── main/java/com/cloudstorage/
│   │   ├── config/          # Configuration class
│   │   │   ├── ProjectConfig.java
│   │   ├── utils/           # Utility classes
│   │   │   ├── CommandRunner.java
│   │   │   ├── PlaywrightManager.java
│   ├── test/java/com/cloudstorage/
│   │    │   ├── tests/
│   │    │   │   ├── GCloudStorageTests.java    # Tests for gcloud CLI
│   │    │   │   ├── PlaywrightSignedURLTest.java    # Playwright tests for signed URL security
│   │    │   ├── resources/
│   │    │   │   ├──testng.xml    # TestNG configuration
│── target/                  # Compiled test results
│── .gitignore               # Git ignore file
│── phishing_check.png        # Screenshot for Playwright test
│── pom.xml                  # Maven dependencies
│── readme.md                # Project documentation
│── test-file.txt            # Test file for uploads
│── 
```

---

## 🖨 ScreenShots of Allure Report

![Screenshot_1](https://github.com/user-attachments/assets/efc1ceda-9593-441c-aa3c-2e0cf1a7b8d5)
![Screenshot_2](https://github.com/user-attachments/assets/adaf96ca-2e97-48f9-b5ae-b2aecc34b757)

---

## 📜 License
This project is for educational and testing purposes. Modify and use it as needed!

1. docker build -t gcloud-tests .
2. docker run -it --rm gcloud-tests
3. gcloud auth login
4. ENTER THE VERIFICATION CODE
5. 
