package code.odyssey.common.global.infra.feign.papago.service.impl;



import code.odyssey.common.global.infra.feign.papago.dto.*;
import code.odyssey.common.global.infra.feign.papago.environment.PapagoApiKey;
import code.odyssey.common.global.infra.feign.papago.service.PapagoTranslationService;
import feign.Request;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class PapagoTranslationServiceImpl implements PapagoTranslationService {

    private final PapagoApiKey apiKey;
    private final PapagoApiFeignClient apiClient;


    @Override
    public TranslationResult translate(String text) {
        PapagoApiDto<PapagoApiKey, PapagoTranslationApi.Request> request = PapagoApiDto.<PapagoApiKey, PapagoTranslationApi.Request>builder()
                .dataHeader(apiKey)
                .dataBody(PapagoTranslationApi.Request.builder().text(text).target(LangType.en).source(LangType.ko).build()).build();

        PapagoApiDto<PapagoResponseHeader, PapagoTranslationApi.Response> translations = apiClient.getEnglishTranslationResult(request);
        return translations.getDataBody().toDto();

    }
}

