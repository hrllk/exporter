

package org.example;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.Media;
import com.microsoft.playwright.options.WaitUntilState;
import picocli.CommandLine;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

/**
 * WebPdfConverter
 */
public class WebPdfConverter implements Callable<Integer> {


    @Parameters(index = "0", description = "Source URL")
    private String url;

    @Parameters(index = "1", description = "Output PDF file")
    private File output;

	@Override
	public Integer call() throws Exception {


        String url = "http://alzar.duckdns.org:8090";
        hi();
//        convertToPdf(url);

		return 0;
	}

    public static void main(String[] args) {
        String[] overrideArgs = {"http://alzar.duckdns.org:8090", System.getProperty("user.home") + "/" + "test.pdf"};
        
        int exitCode = new CommandLine(new WebPdfConverter()).execute(overrideArgs);
        System.out.println("exitCode: " + exitCode);
        System.exit(exitCode);
        
        // int exitCode = new CommandLine(new WebToPdfConverter()).execute(args);
        // System.exit(exitCode);

    }
    

    public void hi (){
//        try (Playwright playwright = Playwright.create()) {
//            // Launch Chromium in headless mode
//            Browser browser = playwright.chromium().launch(
//                    new BrowserType.LaunchOptions().setHeadless(true)
//            );
//
//            // Open new browser page
//            Page page = browser.newPage();
//
//            // Navigate to your target URL
//            String url = "http://alzar.duckdns.org:8090";
//            page.navigate(url);
//
//            // Export page to PDF
//            page.pdf(new Page.PdfOptions()
//                    .setPath(Paths.get(System.getProperty("user.home"), "webpage.pdf"))
//                    .setFormat("A4")
//            );
//
//            System.out.println("PDF saved successfully.");
//
//            // Close browser
//            browser.close();
//        }

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions().setHeadless(true)
            );

            BrowserContext context = browser.newContext(
                    new Browser.NewContextOptions().setViewportSize(1280, 800)
            );

            Page page = context.newPage();
            page.emulateMedia(new Page.EmulateMediaOptions().setMedia(Media.SCREEN));
            page.addStyleTag(new Page.AddStyleTagOptions().setContent(
                    "@media print { * { all: unset !important; } }"
            ));

            String url = "http://alzar.duckdns.org:8090";
            page.navigate(url, new Page.NavigateOptions()
                    .setWaitUntil(WaitUntilState.NETWORKIDLE)
            );

            // Optional wait for JS-driven rendering
            page.waitForTimeout(5000);

            // Export to PDF
            page.pdf(new Page.PdfOptions()
                    .setPath(Paths.get(System.getProperty("user.home"), "styled-output.pdf"))
                    .setFormat("A4")
            );

            System.out.println("PDF saved with styles.");

            browser.close();
        }
    }
    

//    public byte[] convertToPdf(String url) throws Exception {
//
//        ChromeLauncher launcher = new ChromeLauncher();
//        ChromeService service = launcher.launch(true); // Headless mode
////        launcher.launch(ChromeArguments.builder()
////                .build())
//
//
//
//        ChromeTab tab = null;
//        ChromeDevToolsService devToolsService = null;
//        // try catch resources
//        try {
//
//            tab = service.createTab();
//            devToolsService = service.createDevToolsService(tab);
//
//            // enable page domain
//            devToolsService.getPage().enable();
//
//
//            // setup page load waiter
//            CountDownLatch loadLatch = new CountDownLatch(1);
//            devToolsService.getPage().onLoadEventFired(event -> loadLatch.countDown());
//
//
//            // navigate to page
//            devToolsService.getPage().navigate(url);
//            // wait for page load
//            if (!loadLatch.await(30, java.util.concurrent.TimeUnit.SECONDS)) {
//                throw new RuntimeException("timeout to load the page");
//            }
//
//
//            return null;
//
////            return Base64.getDecoder().decode(pdfData);
//        } finally {
////            service.close();
//            launcher.close();
//        }
//    }


}

