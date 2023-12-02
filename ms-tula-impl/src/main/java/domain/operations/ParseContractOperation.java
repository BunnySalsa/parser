package domain.operations;

import ch.qos.logback.core.util.ExecutorServiceUtil;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;
import team.microchad.api.dto.LawsuitDto;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
@Service
@NoArgsConstructor
public class ParseContractOperation {

    public LawsuitDto execute(InputStream pdfContract) throws IOException {
        PDDocument document = PDDocument.load(pdfContract);
        StringBuilder result = new StringBuilder();
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        ExecutorService service = Executors.newCachedThreadPool();
        List<Future<String>> futureList = new ArrayList<>();
        for (int page = 0; page < document.getNumberOfPages(); page++) {
            Callable<String> parser = new PdfToStringParser(pdfRenderer
                    .renderImageWithDPI(page, 150, ImageType.GRAY), page);
            futureList.add(service.submit(parser));
        }
        for (Future<String> future : futureList) {
            while (!future.isDone());
            try {
                result.append(future.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(result);
        return null;
    }

    static class PdfToStringParser implements Callable<String> {

        final BufferedImage bufferedImage;
        final int page;

        PdfToStringParser(BufferedImage bufferedImage, int page) {
            this.bufferedImage = bufferedImage;
            this.page = page;
        }

        @Override
        public String call() {
            File tempFile = null;
            try {
                ITesseract tesseract = new Tesseract();
                tesseract.setLanguage("rus");
                tempFile = File.createTempFile("tempfile_" + page, ".png");
                ImageIO.write(bufferedImage, "png", tempFile);
                return tesseract.doOCR(tempFile);
            } catch (Exception e) {
                return "ОШИБКА: Не удалось найти текст";
            } finally {
                tempFile.delete();
            }
        }
    }
}
