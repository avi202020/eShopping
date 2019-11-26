package com.pj.springsecurity.web.api.product;

import com.pj.springsecurity.model.order.Currency;
import com.pj.springsecurity.repo.CurrencyRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/currency")
public class CurrencyController
{
	private final CurrencyRepository currencyRepository;

	public CurrencyController(CurrencyRepository currencyRepository)
	{
		this.currencyRepository = currencyRepository;
	}


	@GetMapping(value = "/list")
	public List<Currency> getCurrencies()
	{
		return currencyRepository.findAll();
	}

	@GetMapping(value = "/find/{id}")
	public Optional<Currency> getProductById(@PathVariable Long id)
	{
		return currencyRepository.findById(id);
	}
}
