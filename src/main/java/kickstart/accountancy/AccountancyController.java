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
package kickstart.accountancy;

import org.javamoney.moneta.Money;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import java.time.Month;


/*
	Die Geschäftsführung von Möbel-Hier möchte eine monatliche Abrechnung haben,
 	in der die Möbelverkäufe im Vergleich zum Vormonat aufgeglieder sind.
 */
@Controller
public class AccountancyController {


	private AccountancyManager accountancyManager;
	//YearFilterForm yearFilterForm=new YearFilterForm() ;

	private UserAccount userAccount;
	@Autowired
	public AccountancyController(AccountancyManager accountancyManager, UserAccountManager userAccountManager)
	{
		this.accountancyManager = accountancyManager;
		//iniziiert das Prudukt "stuhl", den Useracount und fügt produkt dem catalog hinzu
	//	accountancyManager.initOrder();
	}

	@RequestMapping("/accountancy")
	public String show(@ModelAttribute("yearFilterForm") YearFilterForm yearFilterForm,Model model) {
		model.addAttribute("time", accountancyManager.getTime());

		model.addAttribute("yearFilterForm",yearFilterForm);
		model.addAttribute("filteredYear",yearFilterForm.getYear());
		model.addAttribute("filteredYearList",accountancyManager.getFilteredYearList(yearFilterForm));

		model.addAttribute("dezValue", accountancyManager.fetchMonthlyAccountancyValue(Month.DECEMBER));
		model.addAttribute("novValue", accountancyManager.fetchMonthlyAccountancyValue(Month.NOVEMBER));
		model.addAttribute("oktValue", accountancyManager.fetchMonthlyAccountancyValue(Month.OCTOBER));
		model.addAttribute("sepValue", accountancyManager.fetchMonthlyAccountancyValue(Month.SEPTEMBER));
		model.addAttribute("augValue", accountancyManager.fetchMonthlyAccountancyValue(Month.AUGUST));
		model.addAttribute("julValue", accountancyManager.fetchMonthlyAccountancyValue(Month.JULY));
		model.addAttribute("junValue", accountancyManager.fetchMonthlyAccountancyValue(Month.JUNE));
		model.addAttribute("maiValue", accountancyManager.fetchMonthlyAccountancyValue(Month.MAY));
		model.addAttribute("aprValue", accountancyManager.fetchMonthlyAccountancyValue(Month.APRIL));
		model.addAttribute("marValue", accountancyManager.fetchMonthlyAccountancyValue(Month.MARCH));
		model.addAttribute("febValue", accountancyManager.fetchMonthlyAccountancyValue(Month.FEBRUARY));
		model.addAttribute("janValue", accountancyManager.fetchMonthlyAccountancyValue(Month.JANUARY));

		model.addAttribute("monthlyAccountancy",accountancyManager.fetchThisMonthAccountancy());
		//model.addAttribute("monthlyOrders", accountancyManager.fetchMonthlyOrders());
		accountancyManager.checkForPayDay();
		return "accountancy";
	}

	@RequestMapping("/skippDay")
	public String skippDay() {
		accountancyManager.skippDay();

		return "redirect:/accountancy";
	}

	@RequestMapping("/skippMonth")
	public String skippMonth() {
		accountancyManager.skippMonth();
		return "redirect:/accountancy";
	}

	/*  */
	@RequestMapping("/plus")
	public String order() {
		accountancyManager.addEntry(Money.of(20,"EUR"));
		return "redirect:/accountancy";
	}

	@RequestMapping("/minus")
	public String payDay() {
		accountancyManager.addEntry(Money.of(-20,"EUR"));
		return "redirect:/accountancy";
	}

	public AccountancyManager getManager(){
		return accountancyManager;
	}
}