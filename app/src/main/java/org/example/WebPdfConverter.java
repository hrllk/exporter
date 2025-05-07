

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
        jsoupDoc
            .outputSettings()
            .syntax(Document.OutputSettings.Syntax.xml);


        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);


        OutputStream outputStream = new FileOutputStream(output);
        PdfRendererBuilder pdfBuilder = new PdfRendererBuilder();
        pdfBuilder.withW3cDocument(w3cDoc, url);
        pdfBuilder.toStream(outputStream);
        pdfBuilder.run();
        



        

            // try (OutputStream os = new FileOutputStream(output)) {
            //     PdfRendererBuilder builder = new PdfRendererBuilder();
            //     builder.withW3cDocument(w3cDoc, url);
            //     builder.toStream(os);
            //     builder.run();
            // }
            //
            // System.out.println("Successfully created: " + output.getAbsolutePath());
            // return 0;

            
		return null;
	}
    

    

}

