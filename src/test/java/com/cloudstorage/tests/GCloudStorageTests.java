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

        // Ensure authentication (user should have logged in interactively)
        System.out.println("Checking authentication...");
        printCommandOutput("gcloud auth list");

        // Check if the project exists
        printCommandOutput("gcloud projects list --filter=\"projectId=" + ProjectConfig.PROJECT_ID + "\" --format=\"value(projectId)\"");

        printCommandOutput("gcloud projects create " + ProjectConfig.PROJECT_ID + " --set-as-default");

        // Set the active project
        printCommandOutput("gcloud config set project " + ProjectConfig.PROJECT_ID);

        // Verify the project is set correctly
        printCommandOutput("gcloud config list");

        // Add IAM permissions to allow storage operations
        printCommandOutput("gcloud projects add-iam-policy-binding " + ProjectConfig.PROJECT_ID +
                " --member=user:$(gcloud config get-value account) --role=roles/owner || true");

        // Create a bucket in a specific project
        printCommandOutput("gcloud storage buckets create gs://" + ProjectConfig.BUCKET_NAME + " --project=" + ProjectConfig.PROJECT_ID + " --location=us-central1");

        //Input some kishkush into a txt file
        printCommandOutput("echo \"Hello Cloud Storage!\" > " + ProjectConfig.TEST_FILE_NAME);

        //Insert the txt file
        printCommandOutput("gcloud storage cp " + ProjectConfig.TEST_FILE_NAME + " gs://" + ProjectConfig.BUCKET_NAME + "/");
    }


    /**
     * Runs a shell command and prints its output to the terminal for debugging.
     */
    private void printCommandOutput(String command) {
        System.out.println("Executing: " + command);
        String output = CommandRunner.runCommand(command);
        System.out.println("Output:\n" + output);
    }


    @Test()
    @Description("Test to create a GCloud storage bucket")
    @Severity(SeverityLevel.CRITICAL)
    @Step("Executing testCreateBucket")
    public void testCreateBucket() {
        System.out.println("Checking if bucket exists...");

        // Command to check if the bucket exists
        String checkCommand = "gcloud storage buckets list --filter=name:" + ProjectConfig.BUCKET_NAME;

        String checkOutput = CommandRunner.runCommand(checkCommand);

        if (checkOutput.contains(ProjectConfig.BUCKET_NAME)) {
            System.out.println("Bucket already exists, skipping creation.");
            return; // Skip creation, do not fail test
        }

        // Create bucket if it does not exist
        System.out.println("Bucket does not exist, creating...");
        String command = "gcloud storage buckets create gs://" + ProjectConfig.BUCKET_NAME + " --location=me-west1";

        String output = CommandRunner.runCommand(command);
        System.out.println("\n****\noutput: " + output + "\n****\n");

        // Validate creation success
        Assert.assertTrue(output.contains("Creating gs://" + ProjectConfig.BUCKET_NAME) || checkOutput.contains(ProjectConfig.BUCKET_NAME),
                "Bucket creation failed!");
    }

    @Test()
    @Description("Test to upload a file to GCloud storage bucket")
    @Severity(SeverityLevel.CRITICAL)
    @Step("Executing testUploadFile")
    public void testUploadFile() {
        System.out.println("Checking if file exists in bucket named: " + ProjectConfig.BUCKET_NAME);

        // Command to check if the file exists
        String checkCommand = "gcloud storage ls gs://" + ProjectConfig.BUCKET_NAME + "/";
        String checkOutput = CommandRunner.runCommand(checkCommand);

        if (checkOutput.contains(ProjectConfig.TEST_FILE_NAME)) {
            System.out.println("File already exists, skipping upload.");
            return;
        }

        // Upload file if it does not exist
        System.out.println("File does not exist, uploading...");
        String uploadCommand = "gcloud storage cp " + ProjectConfig.TEST_FILE_NAME + " gs://" + ProjectConfig.BUCKET_NAME + "/";
        String output = CommandRunner.runCommand(uploadCommand);

        Assert.assertTrue(output.toLowerCase().contains("copying") || output.toLowerCase().contains("uploaded"),
                "File upload failed!");
    }

    @Test()
    @Description("Test to list the files in GCloud storage bucket")
    @Severity(SeverityLevel.MINOR)
    @Step("Executing testListFiles")
    public void testListFiles() {
        System.out.println("Checking if the test to list the files in GCloud storage bucket is working");

        String command = "gcloud storage ls gs://" + ProjectConfig.BUCKET_NAME + "/";
        String output = CommandRunner.runCommand(command);

        Assert.assertTrue(output.contains("gs://" + ProjectConfig.BUCKET_NAME + "/" + ProjectConfig.TEST_FILE_NAME),
                "File not found in bucket!");
    }

    @Test()
    @Description("Test to generate url to store the file from the GCloud storage bucket")
    @Severity(SeverityLevel.CRITICAL)
    @Step("Executing testGenerateSignedUrl")
    public void testGenerateSignedUrl() {
        System.out.println("Generating signed URL...");

        String command = "gcloud storage sign-url --duration=10m gs://"
                + ProjectConfig.BUCKET_NAME + "/" + ProjectConfig.TEST_FILE_NAME;

        String output = CommandRunner.runCommand(command);

        Assert.assertTrue(output.contains("https://"), "Signed URL generation failed!");
    }


}
