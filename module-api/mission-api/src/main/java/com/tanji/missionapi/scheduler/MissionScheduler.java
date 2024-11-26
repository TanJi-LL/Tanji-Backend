//package com.tanji.missionapi.scheduler;
//
//import com.tanji.domainredis.util.RedisUtil;
//import com.tanji.missionapi.application.MissionService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class MissionScheduler {
//
//    private final MissionService missionService;
//
//    /**
//     * 매일 자정 12시 랜덤 데일리 미션을 Redis에 저장
//     */
////    @Scheduled(cron = "0 0 0 * * ?")
//    @Scheduled(cron = "0/1 * * * * ?")
//    public void refreshMissions() {
//
//        missionService.refreshMissions();
//    }
//}
