package com.tanji.domainrds.domains.tanji_status.repository;

import com.tanji.domainrds.domains.tanji_status.domain.TanjiStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TanjiStatusRepository extends JpaRepository<TanjiStatus, Long> {
}
