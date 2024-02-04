package code.odyssey.common.global.infra.feign.papago.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class PapagoApiDto<H, B> {
    private H dataHeader;
    private B dataBody;
}
