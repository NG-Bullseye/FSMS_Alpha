/*
 * Copyright 2014-2015 the original author or authors.
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
package Webshop.Accountancy;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/*
	Die Geschäftsführung von Möbel-Hier möchte eine monatliche Abrechnung haben,
 	in der die Möbelverkäufe im Vergleich zum Vormonat aufgeglieder sind.
 */
@Controller
public class AccountancyController {

	@RequestMapping("/")
	public String index() {
		return "accountancy";
	}
}
