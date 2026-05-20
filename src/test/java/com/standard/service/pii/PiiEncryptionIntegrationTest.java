package com.standard.service.pii;

import com.standard.service.entity.UserEntity;
import com.standard.service.repository.UserRepository;
import com.standard.service.service.UserService;
import com.standard.service.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PiiEncryptionIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void testEncryptionAndDecryption() {
        // Given
        UserEntity user = new UserEntity();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setNationalId("SSN-123-456");

        // When saving via JPA
        user = userRepository.saveAndFlush(user);

        // Then verify normal object contains raw text transparently
        UserEntity retrievedUser = userRepository.findById(user.getId()).orElseThrow();
        assertEquals("john.doe@example.com", retrievedUser.getEmail());
        assertEquals("SSN-123-456", retrievedUser.getNationalId());

        // Verify underlying database physically contains Ciphertext via raw JDBC
        // NOTE: This native query approach is strictly for NEGATIVE TESTING / VALIDATION purposes only.
        // It is used to prove the data is encrypted on disk. In actual application use cases, 
        // you should ONLY use JPA repositories (which will handle decryption transparently).
        List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT * FROM users WHERE id = ?", user.getId());
        assertEquals(1, rows.size());

        // H2 returns capital column names automatically
        String dbEmail = (String) rows.get(0).get("EMAIL");
        String dbNationalId = (String) rows.get(0).get("NATIONAL_ID");

        assertNotNull(dbEmail);
        assertNotEquals("john.doe@example.com", dbEmail, "Email must be encrypted in database");
        assertTrue(dbEmail.length() > "john.doe@example.com".length());

        assertNotNull(dbNationalId);
        assertNotEquals("SSN-123-456", dbNationalId, "National ID must be encrypted in database");
    }

    @Test
    void testSearchByEncryptedFields() {
        // Given
        UserDto userDto = UserDto.builder()
                .name("Jane Smith")
                .email("jane.smith@example.com")
                .nationalId("SSN-987-654")
                .build();

        // When saved
        UserDto saved = userService.createUser(userDto);
        assertNotNull(saved.getId());

        // Then search by email
        UserDto foundByEmail = userService.getUserByEmail("jane.smith@example.com").orElse(null);
        assertNotNull(foundByEmail);
        assertEquals("Jane Smith", foundByEmail.getName());
        assertEquals("jane.smith@example.com", foundByEmail.getEmail());
        assertEquals("SSN-987-654", foundByEmail.getNationalId());

        // Then search by national ID
        UserDto foundByNationalId = userService.getUserByNationalId("SSN-987-654").orElse(null);
        assertNotNull(foundByNationalId);
        assertEquals("Jane Smith", foundByNationalId.getName());
        assertEquals("jane.smith@example.com", foundByNationalId.getEmail());
        assertEquals("SSN-987-654", foundByNationalId.getNationalId());

        // Then search by non-existent values
        assertTrue(userService.getUserByEmail("notfound@example.com").isEmpty());
        assertTrue(userService.getUserByNationalId("SSN-000-000").isEmpty());
    }
}
