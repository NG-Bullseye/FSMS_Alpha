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
package Wawi.Controller;

import java.util.LinkedList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import Wawi.articles.Article;
import Wawi.Micellenious.WebshopCatalog;

@Controller
public class WelcomeController {
	private final WebshopCatalog catalog;

	public WelcomeController(WebshopCatalog catalog) {
		this.catalog = catalog;
	}

	@RequestMapping("/start")
	public String index(Model model) {
		LinkedList<Article> mostBought = new LinkedList<Article>();
		//catalog.mostBought().forEach(mostBought::add);
		for (int i = mostBought.size() - 1; i > 2; i--) {
			mostBought.remove(i);
		}
		model.addAttribute("catalog", mostBought);
		return "index";
	}

}
