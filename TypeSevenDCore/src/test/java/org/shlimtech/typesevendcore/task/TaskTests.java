package org.shlimtech.typesevendcore.task;

import io.micrometer.core.instrument.Counter;
import org.junit.jupiter.api.Test;
import org.shlimtech.typesevendatabasecommon.metadata.Metadata;
import org.shlimtech.typesevendatabasecommon.service.MetadataService;
import org.shlimtech.typesevendcore.BaseTest;
import org.shlimtech.typesixdatabasecommon.dto.UserDTO;
import org.shlimtech.typesixdatabasecommon.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

public class TaskTests extends BaseTest {

    @Autowired
    private UserService userService;
    @Autowired
    private MetadataService metadataService;
    @Autowired
    private Counter tasksCounter;

    @Test
    public void simpleTaskTest() {
        UserDTO user1 = userService.createOrComplementUser(UserDTO.builder().email("a@mail.ru").build());
        UserDTO user2 = userService.createOrComplementUser(UserDTO.builder().email("b@mail.ru").build());

        Metadata met1 = metadataService.generateMetadata();
        Metadata met2 = metadataService.generateMetadata();

        met1.getMetadataEntrySets().get(0).getEntries().get(0).setFlag(true); // man
        met2.getMetadataEntrySets().get(0).getEntries().get(1).setFlag(true); // woman
        met2.getMetadataEntrySets().get(0).getEntries().get(0).setFlag(false); // not man

        met1.getMetadataEntrySets().get(1).getEntries().get(1).setFlag(true); // target is woman
        met2.getMetadataEntrySets().get(1).getEntries().get(0).setFlag(true); // target is man
        met2.getMetadataEntrySets().get(1).getEntries().get(1).setFlag(false); // target is not woman

        metadataService.saveUserMetadata(user1.getId(), met1);
        metadataService.saveUserMetadata(user2.getId(), met2);

        Assert.isTrue(metadataService.canMatch(met1, met2), "can not match");

        new FindMatchForUserTask(metadataService, userService, tasksCounter, user1.getId()).run();

        Metadata result = metadataService.loadUserMetadata(user1.getId());

        Assert.isTrue(result.getSelectedUsers().size() == 1 && result.getSelectedUsers().get(0) == user2.getId(), "not found");
    }

}
