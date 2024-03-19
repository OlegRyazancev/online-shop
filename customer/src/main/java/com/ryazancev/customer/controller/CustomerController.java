package com.ryazancev.customer.controller;

import com.ryazancev.common.dto.customer.CustomerDto;
import com.ryazancev.common.dto.customer.CustomerPurchasesResponse;
import com.ryazancev.common.dto.notification.NotificationDto;
import com.ryazancev.common.dto.notification.NotificationsSimpleResponse;
import com.ryazancev.common.dto.purchase.PurchaseDto;
import com.ryazancev.common.dto.purchase.PurchaseEditDto;
import com.ryazancev.common.dto.review.ReviewsResponse;
import com.ryazancev.common.validation.OnCreate;
import com.ryazancev.common.validation.OnUpdate;
import com.ryazancev.customer.model.Customer;
import com.ryazancev.customer.service.ClientsService;
import com.ryazancev.customer.service.CustomExpressionService;
import com.ryazancev.customer.service.CustomerService;
import com.ryazancev.customer.util.mapper.CustomerMapper;
import com.ryazancev.customer.util.processor.KafkaMessageProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Oleg Ryazancev
 */

@Slf4j
@RestController
@RequestMapping("api/v1/customers")
@RequiredArgsConstructor
@Validated
public class CustomerController {

    private final CustomerService customerService;
    private final CustomExpressionService customExpressionService;
    private final CustomerMapper customerMapper;

    private final KafkaMessageProcessor kafkaMessageProcessor;

    private final ClientsService clientsService;

    @GetMapping("/{id}")
    public CustomerDto getById(
            @PathVariable("id") final Long id) {

        customExpressionService.checkIfAccountLocked();
        customExpressionService.checkAccessUser(id);

        Customer customer = customerService.getById(id);

        return customerMapper.toDetailedDto(customer);
    }

    @PutMapping
    public CustomerDto updateCustomer(
            @RequestBody
            @Validated(OnUpdate.class) final CustomerDto customerDto) {

        customExpressionService.checkAccountConditions();
        customExpressionService.checkAccessUser(customerDto.getId());

        Customer customer = customerMapper.toEntity(customerDto);
        Customer updated = customerService.update(customer);

        return customerMapper.toDetailedDto(updated);
    }

    @GetMapping("/{id}/reviews")
    public ReviewsResponse getReviewsByCustomerId(
            @PathVariable("id") final Long id) {

        customExpressionService.checkIfAccountLocked();
        customExpressionService.checkAccessUser(id);

        Customer customer = customerService.getById(id);

        return (ReviewsResponse) clientsService
                .getReviewsByCustomerId(customer.getId());
    }

    @GetMapping("/{id}/purchases")
    public CustomerPurchasesResponse getPurchasesByCustomerId(
            @PathVariable("id") final Long id) {

        customExpressionService.checkIfAccountLocked();
        customExpressionService.checkAccessUser(id);

        Customer customer = customerService.getById(id);

        return (CustomerPurchasesResponse) clientsService
                .getPurchasesByCustomerId(customer.getId());
    }

    @PostMapping("/purchases")
    public PurchaseDto processPurchase(
            @RequestBody
            @Validated(OnCreate.class) final PurchaseEditDto purchaseEditDto) {

        customExpressionService.checkAccountConditions();
        customExpressionService
                .checkAccessUser(purchaseEditDto.getCustomerId());

        PurchaseDto created = (PurchaseDto) clientsService
                .processPurchase(purchaseEditDto);

        kafkaMessageProcessor
                .sendPurchaseProcessedNotification(purchaseEditDto);

        return created;
    }

    @DeleteMapping("{id}")
    public String deleteCustomerById(@PathVariable("id") final Long id) {

        customExpressionService.checkAccountConditions();
        customExpressionService.checkAccessUser(id);

        return customerService.markCustomerAsDeleted(id);
    }

    @GetMapping("{id}/notifications")
    public NotificationsSimpleResponse getNotificationsByCustomerId(
            @PathVariable("id") final Long id,
            @RequestParam("scope") final String scope) {

        customExpressionService.checkIfAccountLocked();
        customExpressionService.checkAccessUser(id);

        if (scope.equals("admin")) {
            customExpressionService.checkIfCustomerIsAdmin(id);
        }

        return (NotificationsSimpleResponse)
                clientsService.getNotificationsByCustomerId(id, scope);
    }

    @GetMapping("{customerId}/notifications/{notificationId}")
    public NotificationDto getNotificationById(
            @PathVariable("customerId") final Long customerId,
            @PathVariable("notificationId") final String notificationId,
            @RequestParam("scope") final String scope) {

        customExpressionService.checkIfAccountLocked();
        customExpressionService.checkAccessUser(customerId);

        switch (scope) {
            case "admin" -> {
                customExpressionService.checkIfCustomerIsAdmin(customerId);
            }
            case "private" -> {
                customExpressionService
                        .checkAccessPrivateNotification(
                                customerId,
                                notificationId);
            }
            default -> {

            }
        }

        return (NotificationDto)
                clientsService.getNotificationById(notificationId, scope);
    }

//    Endpoints only  for feign clients

    @GetMapping("/{id}/simple")
    public CustomerDto getSimpleById(
            @PathVariable("id") final Long id) {

        Customer customer = customerService.getById(id);

        return customerMapper.toSimpleDto(customer);
    }

    @GetMapping("/{id}/balance")
    public Double getBalanceById(
            @PathVariable("id") final Long id) {

        return customerService.getBalanceByCustomerId(id);
    }

    @PostMapping
    public CustomerDto createCustomer(
            @RequestBody
            @Validated(OnCreate.class) final CustomerDto customerDto) {

        Customer customer = customerMapper.toEntity(customerDto);
        Customer created = customerService.create(customer);

        return customerMapper.toSimpleDto(created);
    }
}
