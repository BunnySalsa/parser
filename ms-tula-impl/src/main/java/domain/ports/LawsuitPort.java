package domain.ports;

import team.microchad.api.dto.LawsuitDto;

public interface LawsuitPort {
  void generateLawsuit(LawsuitDto dto);
}
