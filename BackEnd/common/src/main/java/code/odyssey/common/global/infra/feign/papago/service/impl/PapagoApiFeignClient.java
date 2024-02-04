package code.odyssey.common.global.infra.feign.papago.service.impl;

import code.odyssey.common.global.config.HeaderConfig;
import code.odyssey.common.global.infra.feign.papago.dto.PapagoApiDto;
import code.odyssey.common.global.infra.feign.papago.dto.PapagoResponseHeader;
import code.odyssey.common.global.infra.feign.papago.dto.PapagoTranslationApi;
import code.odyssey.common.global.infra.feign.papago.environment.PapagoApiKey;

import feign.Request;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="papago", url = "https://openapi.naver.com",
        configuration = {HeaderConfig.class}
)
public interface PapagoApiFeignClient {

    ///v1/papago/n2mt")

    @PostMapping("/v1/papago/n2mt")
    PapagoApiDto<PapagoResponseHeader, PapagoTranslationApi.Response> getEnglishTranslationResult(
            @RequestBody PapagoApiDto<PapagoApiKey, PapagoTranslationApi.Request> request
    );
}