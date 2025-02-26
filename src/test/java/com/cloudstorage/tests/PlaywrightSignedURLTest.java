package com.cloudstorage.tests;

import com.cloudstorage.utils.CommandRunner;
import com.cloudstorage.utils.PlaywrightManager;
import com.cloudstorage.config.ProjectConfig;
import com.microsoft.playwright.Page;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import org.testng.Assert;
import org.testng.annotations.*;
import java.nio.file.Paths;

public class PlaywrightSignedURLTest {
    private String signedUrl;
    private Page page;

    @BeforeClass
    public void setup() {
        page = PlaywrightManager.getPage();
        signedUrl = generateSignedUrl();
    }

    private String generateSignedUrl() {
        String command = ProjectConfig.G_CLOUD_PATH + " storage sign-url --duration=10m gs://"
                + ProjectConfig.BUCKET_NAME + "/" + ProjectConfig.TEST_FILE_NAME;

        String output = CommandRunner.runCommand(command);

        System.out.println("***************************");
        System.out.println("Signed URL Command Output: " + output);
        System.out.println("***************************");

        /// Extract the signed URL
        for (String line : output.split("\n")) {
            if (line.startsWith("signed_url:")) {
                String url = line.replace("signed_url:", "").trim(); // Remove "signed_url:" and extra spaces
                System.out.println("Extracted URL: " + url);
                return url;
            }
        }

        return null;  // If no URL found, return null
    }

    @Test(priority = 5)
    @Description("Test to check the generated url and check if triggered as phishing warning")
    @Severity(SeverityLevel.NORMAL)
    @Step("Executing testSignedUrlDoesNotTriggerPhishingWarning")
    public void testSignedUrlDoesNotTriggerPhishingWarning() {
        if (signedUrl == null) {
            Assert.fail("Failed to generate signed URL");
        }

        System.out.println("Navigating to: " + signedUrl);
        page.navigate(signedUrl);

        // Capture screenshot before checking for phishing warning
        String screenshotPath = "phishing_check.png";
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(screenshotPath)));
        System.out.println("Screenshot saved to: " + screenshotPath);

        // Check for phishing warning text
        boolean isPhishingWarning = page.locator("text='Deceptive site ahead'").count() > 0 ||
                page.locator("text='Phishing attack ahead'").count() > 0 ||
                page.locator("text='Suspicious site'").count() > 0;

        Assert.assertFalse(isPhishingWarning, "Signed URL is flagged as phishing!");
    }

    @AfterClass
    public void tearDown() {
        PlaywrightManager.close();
    }
}
