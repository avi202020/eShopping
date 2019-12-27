package com.pj.eshopping.web.api.cart;

import com.pj.eshopping.dto.CartProductDTO;
import com.pj.eshopping.dto.CartProductSlim;
import com.pj.eshopping.dto.UserProfileDTO;
import com.pj.eshopping.exceptions.exceptions.GenericException;
import com.pj.eshopping.model.cart.Cart;
import com.pj.eshopping.model.cart.CartProduct;
import com.pj.eshopping.model.user.UserProfile;
import com.pj.eshopping.repo.CartProductRepository;
import com.pj.eshopping.repo.CartRepository;
import com.pj.eshopping.repo.CartStatusRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController
{
	private final CartRepository cartRepository;
	private final CartProductRepository cartProductRepository;
	private final CartStatusRepository cartStatusRepository;

	private final ModelMapper modelMapper;

	public CartController(CartRepository cartRepository, ModelMapper modelMapper, CartProductRepository cartProductRepository, CartStatusRepository cartStatusRepository)
	{
		this.cartRepository = cartRepository;
		this.modelMapper = modelMapper;
		this.cartProductRepository = cartProductRepository;
		this.cartStatusRepository = cartStatusRepository;
	}

	@GetMapping(path = "/find/user/{id}")
	public Optional<Cart> getCartBasedOnUserId(@PathVariable Long id)
	{
		return cartRepository.findAllByUserProfileUserId(id);
	}

	@PostMapping(path = "/initialize")
	public Cart initializeCart(@RequestBody UserProfileDTO userProfileDTO)
	{
		UserProfile userProfile = modelMapper.map(userProfileDTO, UserProfile.class);

		Cart cart = new Cart();
		cart.setUserProfile(userProfile);
		cart.setCartStatus(cartStatusRepository.findByStatus("Draft").orElse(null));
		return cartRepository.saveAndFlush(cart);
	}

	@PostMapping(path = "/product/add")
	public Cart addProductToCart(@RequestBody CartProductDTO cartProductDTO)
	{
		CartProduct cartProduct = modelMapper.map(cartProductDTO, CartProduct.class);
		return cartProductRepository.saveAndFlush(cartProduct).getCart();
	}

	@PostMapping(path = "/product/update")
	public Cart updateCartProduct(@RequestBody CartProductSlim cartProductSlim)
	{
		Optional<CartProduct> cartProductOptional=cartProductRepository.findById(cartProductSlim.getCartProductId());
		if(cartProductOptional.isPresent())
		{
			CartProduct cartProduct=cartProductOptional.get();
			if (cartProductSlim.getQuantity() == 0)
			{
				deleteCartProduct(cartProduct.getId());
			}
			else
			{
				cartProduct.setQuantity(cartProductSlim.getQuantity());
				cartProductRepository.saveAndFlush(cartProduct);
			}
			return cartRepository.findById(cartProduct.getCart().getId()).orElse(null);
		}
		else
			throw new GenericException("Failed to update Cart. Provided product ID is invalid ", null, HttpStatus.NOT_FOUND, LocalDateTime.now(), null, null);
	}

	@DeleteMapping(path = "/product/delete/{id}")
	public void deleteCartProduct(@PathVariable Long id)
	{
		Optional<CartProduct> optionalCartProduct = cartProductRepository.findById(id);
		optionalCartProduct.ifPresent(cartProductRepository::delete);
	}


	@DeleteMapping(path = "/delete/{id}")
	public void deleteCart(@PathVariable Long id)
	{
		Optional<Cart> cartOptional = cartRepository.findById(id);
		if (cartOptional.isPresent())
		{
			Cart cart = cartOptional.get();
			for (CartProduct cartProduct : cart.getCartProducts())
			{
				cartProductRepository.delete(cartProduct);
			}
			cartRepository.delete(cart);
		}
	}
}
