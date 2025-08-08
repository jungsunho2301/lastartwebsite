package walid.jahin;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles; // ✅ 이 import도 필요!

@ActiveProfiles("test") // ✅ 핵심: 테스트에서 H2 DB 쓰도록 지정
@SpringBootTest
class JahinApplicationTests {

	@Test
	void contextLoads() {
	}

}
