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
package kickstart.catalog;

import forms.CompositeForm;
import kickstart.articles.Article;
import forms.Filterform;
import forms.Form;
import org.salespointframework.catalog.ProductIdentifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CatalogController {

	private final CatalogManager manager;

	CatalogController(CatalogManager manager){
	this.manager = manager;
	}

	@ModelAttribute("colours")
	public String[] colours() {
		return new String[] {
				"schwarz","blau","weiß","rot","braun","grün"
		};
	}
	@ModelAttribute("categories")
	public String[] categories() {
		return new String[] {
				"Tisch","Schrank","Stuhl","Regal","Bett"
		};
	}

	@RequestMapping("/catalog")
	String catalog(Model model){


		model.addAttribute("catalog", manager.getWholeCatalog());

		model.addAttribute("urlAll","catalog");
		model.addAttribute("urlPart","part");
		model.addAttribute("urlComposite","composite");
		model.addAttribute("filterform", new Filterform());

		return "catalog";
	}
	@PostMapping("/catalog")
	String catalogFiltered (@ModelAttribute("filterform") Filterform filterform, Model model){
		model.addAttribute("catalog", manager.filteredCatalog(filterform));
		return "catalog";
	}
	@GetMapping("artikel/{identifier}")
	public String detail(@PathVariable ProductIdentifier identifier, Model model){

		model.addAttribute("article", manager.getArticle(identifier));
		System.out.println(manager.getArticle(identifier).getDescription());

		return "article";
	}
	@GetMapping("edit/{identifier}")
	public String detailEdit(@PathVariable ProductIdentifier identifier, Model model){

		model.addAttribute("article", manager.getArticle(identifier));
		model.addAttribute("form",new Form()); //Damit man im folgenden form bearbeiten kann
		return "edit";
	}

	@PostMapping("edit/{identifier}")
	public String editArticle(@PathVariable ProductIdentifier identifier, @ModelAttribute("form") Form form, Model model){
		manager.editArticle(form, identifier);
		model.addAttribute("article",manager.getArticle(identifier));
		return "article";
	}
	@GetMapping("catalog/part/new")
	public String showNew(Model model){
		model.addAttribute("form",new Form());
		return"newPart";
	}
	@PostMapping("catalog/part/new")
	public String editNew(@ModelAttribute("form") Form form, Model model){
		manager.newPart(form);
		model.addAttribute("catalog", manager.getWholeCatalog());
		return"redirect:/catalog";
	}
	@GetMapping("catalog/composite/new")
	public String newSelection(Model model){
		model.addAttribute("compositeForm",new CompositeForm());
		return"newPart";
	}

}
