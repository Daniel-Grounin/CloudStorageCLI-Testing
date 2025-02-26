# Cloud Storage CLI Testing

This project contains automated tests for **Google Cloud Storage CLI** using **TestNG** and **Allure** for reporting. It verifies core functionalities such as bucket creation, file upload, listing files, and generating signed URLs.

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
│   ├── test/java/com/cloudstorage/tests/
│   │   ├── GCloudStorageTests.java    # Tests for gcloud CLI
│   │   ├── PlaywrightSignedURLTest.java    # Playwright tests for signed URL security
│── target/                  # Compiled test results
│── .gitignore               # Git ignore file
│── phishing_check.png        # Screenshot for Playwright test
│── pom.xml                  # Maven dependencies
│── readme.md                # Project documentation
│── test-file.txt            # Test file for uploads
│── testng.xml               # TestNG configuration
```

---

## ✅ Automated Tests & gcloud Commands

Each test case automates a **gcloud storage** command to verify its expected behavior.

### 🏗️ 1. Bucket Creation
**Test:** Ensures a Google Cloud Storage bucket is created if it doesn't exist.
**gcloud Command:**
```sh
gcloud storage buckets create gs://<BUCKET_NAME> --location=me-west1
```

---

### 📤 2. Upload File
**Test:** Uploads a test file to the bucket if it does not already exist.
**gcloud Command:**
```sh
gcloud storage cp test-file.txt gs://<BUCKET_NAME>/
```

---

### 📋 3. List Files
**Test:** Lists files inside the specified bucket to check if the file exists.
**gcloud Command:**
```sh
gcloud storage ls gs://<BUCKET_NAME>/
```

---

### 🔏 4. Generate Signed URL
**Test:** Generates a signed URL for a stored file and ensures it is accessible.
**gcloud Command:**
```sh
gcloud storage sign-url --duration=10m gs://<BUCKET_NAME>/test-file.txt
```

---

### 🕵️‍♂️ 5. Playwright Test: Phishing Check
**Test:** Uses **Playwright** to verify that the signed URL does not trigger phishing warnings in a browser.

---

## 🚀 Running the Tests

### 1️⃣ Install Dependencies
Ensure you have **Maven**, **gcloud CLI**, and **Playwright** installed.
```sh
mvn clean install
```

### 2️⃣ Run Tests with TestNG
```sh
mvn test
```

### 3️⃣ View Allure Test Reports
```sh
allure serve allure-results
```

This will launch a detailed test report in your browser, showing test results, execution time, and logs.

---

## 🛠️ Configurations
All configuration settings (bucket name, paths) are stored in `ProjectConfig.java`. Modify as needed.

---

## 🖨 ScreenShots of Allure Report

![Screenshot_1](https://github.com/user-attachments/assets/efc1ceda-9593-441c-aa3c-2e0cf1a7b8d5)
![Screenshot_2](https://github.com/user-attachments/assets/adaf96ca-2e97-48f9-b5ae-b2aecc34b757)

---

## 📜 License
This project is for educational and testing purposes. Modify and use it as needed!
