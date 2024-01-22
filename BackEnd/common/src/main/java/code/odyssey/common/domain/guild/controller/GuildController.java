package code.odyssey.common.domain.guild.controller;

import code.odyssey.common.domain.guild.dto.GuildCreateRequest;
import code.odyssey.common.domain.guild.dto.GuildSearchRequest;
import code.odyssey.common.domain.guild.dto.GuildSimpleInfo;
import code.odyssey.common.domain.guild.service.GuildService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/guilds")
@RestController
public class GuildController {

    private final GuildService guildService;

    @PostMapping
    public ResponseEntity<Long> createGuild(
            @RequestBody GuildCreateRequest request,
            @RequestHeader("X-Authorization-Id") Long memberId
    ) {
        return ResponseEntity.ok(guildService.createGuild(memberId, request));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<GuildSimpleInfo>> searchGuilds(
            GuildSearchRequest request,
            @RequestHeader("X-Authorization-Id") Long memberId,
            Pageable pageable
    ) {
        request.setMemberId(memberId);
        return ResponseEntity.ok(guildService.searchGuilds(request, pageable));
    }
}
