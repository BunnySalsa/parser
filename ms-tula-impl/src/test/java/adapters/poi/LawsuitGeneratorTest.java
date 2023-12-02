package adapters.poi;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import team.microchad.api.dto.LawsuitDto;
import team.microchad.api.dto.RulesDto;

class LawsuitGeneratorTest {
  @Test
  void shouldLoadFile() {
    LawsuitGenerator generator = new LawsuitGenerator();
    generator.generateLawsuit(new LawsuitDto().rules(new RulesDto().isLegal(false)));
  }
}