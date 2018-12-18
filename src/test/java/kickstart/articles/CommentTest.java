package kickstart.articles;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class CommentTest {

	@Test
	public void testToString() {
		Comment c = new Comment("Text", 1, LocalDateTime.of(1, 1, 1, 1, 1));
		
		assertThat(c.toString()).as("ToString on a comment should return the text").isEqualTo(c.getText());
	}
}
