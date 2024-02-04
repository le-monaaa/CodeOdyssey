package code.odyssey.common.global.infra.feign.papago.dto;

import lombok.Data;

@Data
public class PapagoResponseHeader {

    private String successCode;
    private String resultCode;
    private String resultMessage;
}
