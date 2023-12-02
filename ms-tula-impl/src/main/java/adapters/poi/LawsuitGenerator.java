package adapters.poi;

import domain.ports.LawsuitPort;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field.Str;
import org.apache.poi.POIDocument;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Component;
import team.microchad.api.dto.LawsuitDto;

@Slf4j
public class LawsuitGenerator implements LawsuitPort {

  private static final String TEMPLATE = "template/";

  private static final String HEADER_LEGAL = "header_legal_template.docx";

  private static final String HEADER_NATURAL = "header_natural_template.docx";

  private static final String MAIN = "main_template.docx";

  @Override
  @SneakyThrows
  public void generateLawsuit(LawsuitDto dto) {
    var mainTemplateDoc = getTemplateDoc(TEMPLATE + MAIN);
    XWPFDocument headerTemplateDoc = getTemplateDoc(TEMPLATE + HEADER_NATURAL);
    var rules = dto.getRules();

    if (rules.getIsLegal()) {
      headerTemplateDoc = getTemplateDoc(TEMPLATE + HEADER_LEGAL);
    }


    headerTemplateDoc.getParagraphs()
        .forEach(
            xwpfParagraph ->
            {
              log.info(xwpfParagraph.getText());
            }
        );
  }

  private XWPFDocument getTemplateDoc(String path) throws IOException {
    var mainTemplateURL = getClass().getClassLoader().getResource(path);
    var file = new File(Objects.requireNonNull(mainTemplateURL).getFile());
    var fis = new FileInputStream(file.getAbsolutePath());
    return new XWPFDocument(fis);
  }
}
