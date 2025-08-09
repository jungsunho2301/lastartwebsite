package walid.jahin;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles; // ✅ 이 import도 필요!

@ActiveProfiles("test") // 
@SpringBootTest
class JahinApplicationTests {

	@Test
	void contextLoads() {
	}

}
