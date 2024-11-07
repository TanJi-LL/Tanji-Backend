package com.tanji.domainrds.domains.tanji_status.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TanjiStatusQueryService {
}
