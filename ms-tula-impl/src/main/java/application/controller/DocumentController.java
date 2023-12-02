package application.controller;

import domain.operations.ParseContractOperation;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import team.microchad.api.dto.LawsuitDto;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class DocumentController implements team.microchad.api.DocumentsApi {
    final ParseContractOperation parseContract;

    @Override
    public ResponseEntity<Resource> generateDocument(LawsuitDto lawsuitDto) {
        return null;
    }

    @Override
    @RequestMapping(value = "/api/v1/claim",
        method = RequestMethod.POST, consumes = { "multipart/form-data" })
    public ResponseEntity<Void> postClaim(Resource body) {
        return null;
    }

    @Override
    @SneakyThrows(IOException.class)
    public ResponseEntity<Void> postContract(Resource body) {
        parseContract.execute(body.getInputStream());
        return null;
    }
}
