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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Month;


/**
 * Diese Klasse bietet eine monatliche Abrechnung,
 * in der die Möbelverkäufe im Vergleich zum Vormonat aufgeglieder sind.
 */

@SuppressWarnings("SameReturnValue")
@Controller
public class AccountancyController {

	private final AccountancyManager accountancyManager;

	/**
	 * @param accountancyManager Managerklasse welche die Logik für die Finanzüberischt beinhaltet
	 */
	@Autowired
	public AccountancyController(AccountancyManager accountancyManager) {
		this.accountancyManager = accountancyManager;
	}

	/**
	 * @param yearFilterForm information about the year to filter
	 * @param model information for the html
	 */
	@PreAuthorize("hasRole('ROLE_BOSS')")
	@RequestMapping("/accountancy")
	public String show(@ModelAttribute("yearFilterForm") YearFilterForm yearFilterForm, Model model) {
		model.addAttribute("time", accountancyManager.getTime());
		model.addAttribute("yearFilterForm", yearFilterForm);
		model.addAttribute("filteredYear", yearFilterForm.getYear());
		model.addAttribute("filteredYearList", accountancyManager.getFilteredYearList(yearFilterForm));
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
		model.addAttribute("monthlyAccountancy", accountancyManager.fetchThisMonthAccountancy());
		//model.addAttribute("monthlyOrders", accountancyManager.fetchMonthlyOrders());
		accountancyManager.checkForPayDay();
		return "accountancy";
	}

	/**
	 * skipps a day forward in time
	 */
	@PreAuthorize("hasRole('ROLE_BOSS')")
	@RequestMapping("/skippDay")
	public String skippDay() {
		accountancyManager.skippDay();
		return "redirect:/accountancy";
	}

	/**
	 * skipps a Month forward in time
	 */
	@PreAuthorize("hasRole('ROLE_BOSS')")
	@RequestMapping("/skippMonth")
	public String skippMonth() {
		accountancyManager.skippMonth();
		return "redirect:/accountancy";
	}

	/***/
	public AccountancyManager getManager() {
		return accountancyManager;
	}
}