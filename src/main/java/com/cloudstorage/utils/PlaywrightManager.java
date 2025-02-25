package com.cloudstorage.utils;

import com.microsoft.playwright.*;

public class PlaywrightManager {
    private static Playwright playwright;
    private static Browser browser;
    private static Page page;

    public static Page getPage() {
        if (playwright == null) {
            playwright = Playwright.create();
            browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            page = browser.newPage();
        }
        return page;
    }

    public static void close() {
        if (playwright != null) {
            browser.close();
            playwright.close();
        }
    }
}
