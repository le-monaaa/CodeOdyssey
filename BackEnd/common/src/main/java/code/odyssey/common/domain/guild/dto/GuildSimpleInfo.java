package code.odyssey.common.domain.guild.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GuildSimpleInfo {

    private Long id;
    private String name;
    private Integer maxCapacity;
    private Long curCapacity;
    private String language;
    private String difficulty;
    private Integer problemCapacity;

}
