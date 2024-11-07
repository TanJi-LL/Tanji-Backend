package com.juseungl.statusapi.application;

import com.tanji.domainrds.domains.tanji_status.service.TanjiStatusCommandService;
import com.tanji.domainrds.domains.tanji_status.service.TanjiStatusQueryService;
import com.tanji.domainredis.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TanjiStatusService {
    private final RedisUtil redisUtil;
    private final TanjiStatusCommandService tanjiStatusCommandService;
    private final TanjiStatusQueryService tanjiStatusQueryService;

}
