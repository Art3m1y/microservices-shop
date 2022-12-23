package ru.art3m1y.petprojectshop.adminpanel.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.scheduler.Schedulers;
import ru.art3m1y.petprojectshop.adminpanel.models.Product;
import ru.art3m1y.petprojectshop.adminpanel.utils.exception.ErrorResponse;
import ru.art3m1y.petprojectshop.adminpanel.utils.exception.ProductGetByIdException;
import ru.art3m1y.petprojectshop.login.utils.exceptions.PermissionException;

import java.util.List;

@RestController
@RequestMapping("/admin/products")
public class ProductManagementController {

}
