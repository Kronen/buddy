package com.kronen.buddy.test.integration;

import com.kronen.buddy.backend.persistence.domain.backend.Plan;
import com.kronen.buddy.backend.persistence.domain.backend.Role;
import com.kronen.buddy.backend.persistence.domain.backend.User;
import com.kronen.buddy.backend.persistence.domain.backend.UserRole;
import com.kronen.buddy.common.enums.PlansEnum;
import com.kronen.buddy.common.enums.RolesEnum;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryIntegrationTest extends AbstractIntegrationTest {

    @Rule
    public TestName testName = new TestName();

    @Before
    public void init() {
        assertNotNull(planRepository);
        assertNotNull(roleRepository);
        assertNotNull(userRepository);
    }

    @Test
    public void testCreateNewPlan() {
        Plan basicPlan = createPlan();

        planRepository.save(basicPlan);
        Optional<Plan> retrievedPlan = planRepository.findById(PlansEnum.BASIC.getId());

        assertThat(retrievedPlan.isPresent()).isTrue();
    }

    @Test
    public void testCreateNewRole() {
        Role basicRole = createRole(RolesEnum.BASIC);

        roleRepository.save(basicRole);
        Optional<Role> retrievedRole = roleRepository.findById(RolesEnum.BASIC.getId());

        assertThat(retrievedRole.isPresent()).isTrue();
    }

    @Test
    public void testCreateNewUser() {
        User user = createUser(testName);
        Optional<User> newlyCreatedUser = userRepository.findById(user.getId());

        assertThat(newlyCreatedUser.isPresent()).isTrue();
        assertThat(newlyCreatedUser.get().getId() != 0).isTrue();
        assertThat(newlyCreatedUser.get().getPlan()).isNotNull();
        Set<UserRole> newlyCreatedUserUserRoles = newlyCreatedUser.get().getUserRoles();
        assertThat(newlyCreatedUserUserRoles).isNotNull();
        for(UserRole ur : newlyCreatedUserUserRoles) {
            assertThat(ur.getRole()).isNotNull();
            assertThat(ur.getRole().getId()).isNotNull();
        }
    }

    @Test
    public void testDeleteUser() {
        User user = createUser(testName);

        userRepository.delete(user);
        Optional<User> deletedUser = userRepository.findById(user.getId());

        assertThat(deletedUser.isPresent()).isFalse();
    }

    @Test
    public void testGetUserByEmail() {
        User user = createUser(testName);

        Optional<User> foundUser = userRepository.findByEmail(user.getEmail());

        assertThat(foundUser.isPresent()).isTrue();
        assertThat(foundUser.get().getId()).isNotNull();
    }

    @Test
    public void testUpdateUserPassword() {
        User user = createUser(testName);
        String newPassword = UUID.randomUUID().toString();

        userRepository.updateUserPassword(user.getId(), newPassword);
        Optional<User> userRetrieved = userRepository.findById(user.getId());

        assertThat(userRetrieved.get().getPassword()).isEqualTo(newPassword);
    }

}
