package org.shlimtech.typesevendcore.task;

import io.micrometer.core.instrument.Counter;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.shlimtech.typesevendatabasecommon.service.MetadataService;
import org.shlimtech.typesixdatabasecommon.dto.UserDTO;
import org.shlimtech.typesixdatabasecommon.service.UserService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
@EnableScheduling
@RequiredArgsConstructor
@Log
public class TaskCreator {

    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;
    private final UserService userService;
    private final MetadataService metadataService;
    private final Counter tasksCounter;
    private final Random random = new Random();

    @Scheduled(fixedRate = 10000)
    public void runTaskCreator() {
        List<UserDTO> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return;
        }
        UserDTO user = users.get(random.nextInt(users.size()));
        threadPoolTaskExecutor.execute(new FindMatchForUserTask(metadataService, userService, tasksCounter, user.getId()));
    }

}
