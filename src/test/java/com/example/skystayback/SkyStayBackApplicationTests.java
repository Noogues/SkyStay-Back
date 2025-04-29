package com.example.skystayback;

import com.example.skystayback.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SkyStayBackApplicationTests {

    @Mock
    private UserRepository userRepository;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
}
