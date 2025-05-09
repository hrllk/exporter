

package org.example;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.concurrent.Callable;

import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import picocli.CommandLine.Parameters;
import picocli.CommandLine;

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


        Document jsoupDoc = Jsoup.connect(url).get();
        String css = "@font-face { font-family: 'NotoSansKR'; src: url('file:./NotoSansKR-Regular.ttf'); } body { font-family: NotoSansKR; }";
        jsoupDoc.head().appendElement("style").text(css);
        jsoupDoc
            .outputSettings()
            .syntax(Document.OutputSettings.Syntax.xml);


        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);


        OutputStream outputStream = new FileOutputStream(output);
        PdfRendererBuilder pdfBuilder = new PdfRendererBuilder();
        pdfBuilder.useFont(new File("~/Library/Fonts/D2Coding.ttf"), "D2Coding");
        pdfBuilder.withW3cDocument(w3cDoc, url);
        pdfBuilder.toStream(outputStream);
        pdfBuilder.run();


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
    

    

}

