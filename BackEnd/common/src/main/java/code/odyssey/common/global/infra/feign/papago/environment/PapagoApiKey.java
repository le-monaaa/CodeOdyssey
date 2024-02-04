package code.odyssey.common.global.infra.feign.papago.environment;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class PapagoApiKey {

    @Value("${papago.apiKey}")
    private String apiKey;
}
