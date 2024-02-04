package code.odyssey.common.global.infra.feign.papago.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PapagoTranslationApi {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request{
        private LangType source;
        private LangType target;
        private String text;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response{
        private String srcLangType;
        private String tarLangType;
        private String translatedText;


        public TranslationResult toDto(){
            return TranslationResult.builder().result(this.translatedText).build();
        }
    }
}
