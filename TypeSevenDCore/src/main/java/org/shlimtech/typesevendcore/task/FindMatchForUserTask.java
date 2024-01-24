package org.shlimtech.typesevendcore.task;

import lombok.extern.java.Log;
import org.shlimtech.typesevendatabasecommon.metadata.Metadata;
import org.shlimtech.typesevendatabasecommon.service.MetadataService;
import org.shlimtech.typesixdatabasecommon.dto.UserDTO;
import org.shlimtech.typesixdatabasecommon.service.UserService;

import java.util.List;

@Log
public class FindMatchForUserTask implements Runnable {

    private final MetadataService metadataService;
    private final UserService userService;
    private final int userId;

    public FindMatchForUserTask(MetadataService metadataService, UserService userService, int userId) {
        this.metadataService = metadataService;
        this.userService = userService;
        this.userId = userId;

        log.info("Creating new task for user: [" + userId + "]");
    }

    private UserDTO findMatch() {
        List<UserDTO> users = userService.getAllUsers().stream().filter(user -> user.getId() != userId).toList();

        Metadata ownMetadata = metadataService.loadUserMetadata(userId);

        int bestMetrics = Integer.MIN_VALUE;
        UserDTO bestUser = null;

        for (UserDTO other : users) {
            Metadata otherMetadata = metadataService.loadUserMetadata(other.getId());

            if (!metadataService.canMatch(ownMetadata, otherMetadata)) {
                continue;
            }

            int metrics = metadataService.metaMetric(ownMetadata, otherMetadata);

            if (metrics > bestMetrics) {
                bestMetrics = metrics;
                bestUser = other;
            }
        }

        return bestUser;
    }

    private void saveMatch(UserDTO match) {
        log.info("For user: [" + userId + "] match is found: [" + match.getId() + "]");
        Metadata metadata = metadataService.loadUserMetadata(userId);
        metadata.getSelectedUsers().clear();
        metadata.getSelectedUsers().add(match.getId());
        metadataService.saveUserMetadata(userId, metadata);
    }

    @Override
    public void run() {
        UserDTO match = findMatch();
        if (match != null) {
            saveMatch(match);
        }
    }

}
