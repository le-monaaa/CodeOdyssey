package code.odyssey.common.domain.guild.repository;

import code.odyssey.common.domain.guild.dto.GuildSearchRequest;
import code.odyssey.common.domain.guild.dto.GuildSimpleInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GuildSearchRepository {


    Page<GuildSimpleInfo> search(GuildSearchRequest request, Pageable pageable);
}
