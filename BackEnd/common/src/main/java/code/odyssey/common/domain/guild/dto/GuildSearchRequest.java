package code.odyssey.common.domain.guild.dto;

import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class GuildSearchRequest {
    private String keyword;
    private String language;
    private String difficulty;
    private Long memberId;
}
