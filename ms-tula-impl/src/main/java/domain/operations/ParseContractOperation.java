package domain.operations;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import team.microchad.api.dto.LawsuitDto;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Service
@NoArgsConstructor
public class ParseContractOperation {

    public LawsuitDto execute(InputStream pdfContract) throws IOException {
        PDDocument document = PDDocument.load(pdfContract);
        StringBuilder result = new StringBuilder();
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        ITesseract tesseract = new Tesseract();
        tesseract.setLanguage("rus");
        for (int page = 0; page < document.getNumberOfPages(); page++) {
            log.info("rendering image of page {}", page);
            BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(page, 150, ImageType.GRAY);
            log.info("end randering pf page {}", page);
            File tempFile = File.createTempFile("tempfile_" + page, ".png");
            ImageIO.write(bufferedImage, "png", tempFile);
            log.info("start parsing image to text on page {}", page);
            try {
                result.append(tesseract.doOCR(tempFile));
            } catch (TesseractException e) {
                throw new RuntimeException(e);
            }
            log.info("end parsing image on page {}", page);
            tempFile.delete();
        }
        System.out.println(result);
        return null;
    }
}
