/*
 * Copyright 2013-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package Wawi.articles;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;

/**
 * This class are the comments that a user can write about an article. In
 * addition to this text, the comment also has a rating and a creation time
 */
@Entity
@Table(name = "COMMENTS")
public class Comment implements Serializable {

	@Getter
	private @Id @GeneratedValue long id;

	@Getter
	private String text;

	@Getter
	private int rating;

	@Getter
	private LocalDateTime date;

	@SuppressWarnings("unused")
	private Comment() {
	}

	public Comment(String text, int rating, LocalDateTime dateTime) {

		this.text = text;
		this.rating = rating;
		this.date = dateTime;
	}

	/**
	 * @return Returns the text of the comment
	 */
	@Override
	public String toString() {
		return text;
	}
}
