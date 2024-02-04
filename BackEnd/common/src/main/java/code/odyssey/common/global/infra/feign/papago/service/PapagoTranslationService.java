package code.odyssey.common.global.infra.feign.papago.service;

import code.odyssey.common.global.infra.feign.papago.dto.TranslationResult;

public interface PapagoTranslationService {

    TranslationResult translate(String text);

}
