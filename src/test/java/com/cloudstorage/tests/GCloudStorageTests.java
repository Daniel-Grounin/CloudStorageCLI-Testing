package com.cloudstorage.tests;

import com.cloudstorage.utils.CommandRunner;
import com.cloudstorage.config.ProjectConfig;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import io.qameta.allure.*;


public class GCloudStorageTests {

    @BeforeClass
    public void setup() {
        System.out.println("Setting up tests...");
        CommandRunner.runCommand("echo \"Hello Cloud Storage!\" > " + ProjectConfig.TEST_FILE_NAME);
    }

    @Test(priority = 1)
    @Description("Test to create a GCloud storage bucket")
    @Severity(SeverityLevel.CRITICAL)
    @Step("Executing testCreateBucket")
    public void testCreateBucket() {
        System.out.println("Checking if bucket exists...");

        // Command to check if the bucket exists
        String checkCommand = ProjectConfig.G_CLOUD_PATH + " storage buckets list --filter=name:" + ProjectConfig.BUCKET_NAME;

        String checkOutput = CommandRunner.runCommand(checkCommand);

        if (checkOutput.contains(ProjectConfig.BUCKET_NAME)) {
            System.out.println("Bucket already exists, skipping creation.");
            return; // Skip creation, do not fail test
        }

        // Create bucket if it does not exist
        System.out.println("Bucket does not exist, creating...");
        String command = ProjectConfig.G_CLOUD_PATH + " storage buckets create gs://" + ProjectConfig.BUCKET_NAME + " --location=me-west1";

        System.out.println("******************\nThe command is: " + command + "\n******************\n");

        String output = CommandRunner.runCommand(command);
        System.out.println("output: " + output);

        // Validate creation success
        Assert.assertTrue(output.contains("Creating gs://" + ProjectConfig.BUCKET_NAME) || checkOutput.contains(ProjectConfig.BUCKET_NAME),
                "Bucket creation failed!");
    }

    @Test(priority = 2, dependsOnMethods = "testCreateBucket")
    @Description("Test to upload a file to GCloud storage bucket")
    @Severity(SeverityLevel.CRITICAL)
    @Step("Executing testUploadFile")
    public void testUploadFile() {
        System.out.println("Checking if file exists in bucket...");

        // Command to check if the file exists
        String checkCommand = ProjectConfig.G_CLOUD_PATH + " storage ls gs://" + ProjectConfig.BUCKET_NAME + "/";
        String checkOutput = CommandRunner.runCommand(checkCommand);

        if (checkOutput.contains(ProjectConfig.TEST_FILE_NAME)) {
            System.out.println("File already exists, skipping upload.");
            return;
        }

        // Upload file if it does not exist
        System.out.println("File does not exist, uploading...");
        String uploadCommand = ProjectConfig.G_CLOUD_PATH + " storage cp " + ProjectConfig.TEST_FILE_NAME + " gs://" + ProjectConfig.BUCKET_NAME + "/";
        String output = CommandRunner.runCommand(uploadCommand);

        System.out.println("******************\noutput in testUploadFile(test2): " + output + "\n******************\n");

        Assert.assertTrue(output.toLowerCase().contains("copying") || output.toLowerCase().contains("uploaded"),
                "File upload failed!");
    }

    @Test(priority = 3, dependsOnMethods = "testUploadFile")
    @Description("Test to list the files in GCloud storage bucket")
    @Severity(SeverityLevel.MINOR)
    @Step("Executing testListFiles")
    public void testListFiles() throws InterruptedException {
        System.out.println("Checking if file exists in bucket...");

        String command = ProjectConfig.G_CLOUD_PATH + " storage ls gs://" + ProjectConfig.BUCKET_NAME + "/";
        String output = CommandRunner.runCommand(command);

        Assert.assertTrue(output.contains("gs://" + ProjectConfig.BUCKET_NAME + "/" + ProjectConfig.TEST_FILE_NAME),
                "File not found in bucket!");
    }

    @Test(priority = 4, dependsOnMethods = "testListFiles")
    @Description("Test to generate url to store the file from the GCloud storage bucket")
    @Severity(SeverityLevel.CRITICAL)
    @Step("Executing testGenerateSignedUrl")
    public void testGenerateSignedUrl() {
        System.out.println("Generating signed URL...");

        String command = ProjectConfig.G_CLOUD_PATH + " storage sign-url --duration=10m gs://"
                + ProjectConfig.BUCKET_NAME + "/" + ProjectConfig.TEST_FILE_NAME;

        String output = CommandRunner.runCommand(command);

        System.out.println("******************\nSigned URL Command Output: " + output + "\n******************\n");

        Assert.assertTrue(output.contains("https://"), "Signed URL generation failed!");
    }


}
