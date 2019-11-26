package com.pj.springsecurity.web.api.order;

import com.pj.springsecurity.model.order.Order;
import com.pj.springsecurity.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController
{

	private final OrderService orderService;

	public OrderController(OrderService orderService)
	{
		this.orderService = orderService;
	}

	@GetMapping(value = "/list")
	public List<Order> getOrder()
	{
		return orderService.getOrder();
	}

	@GetMapping(value = "/find/{id}")
	public Optional<Order> getOrderById(@PathVariable Long id)
	{
		return orderService.getOrderById(id);
	}

	@PostMapping(path = "/create")
	public Order createOrder(@RequestBody Long id)
	{
		return orderService.createOrder();
	}


	@PutMapping(path = "/update")
	public Order updateOrder(@RequestBody Order order)
	{
		return orderService.updateOrder(order);
	}
}
