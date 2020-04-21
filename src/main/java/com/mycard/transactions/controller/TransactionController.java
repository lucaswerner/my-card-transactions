package com.mycard.transactions.controller;

import com.mycard.transactions.dto.PostTransactionDTO;
import com.mycard.transactions.dto.PrincipalDTO;
import com.mycard.transactions.dto.TransactionDTO;
import com.mycard.transactions.entity.Transaction;
import com.mycard.transactions.service.TransactionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("transactions")
@Api(tags = "transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ModelMapper modelMapper;

    public TransactionController(TransactionService transactionService, ModelMapper modelMapper) {
        this.transactionService = transactionService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    @ApiOperation(value = "getUserTransactionList")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong")
    })
    public ResponseEntity<List<TransactionDTO>> getUserTransactionList(
            @RequestParam("pageSize") @NotNull Integer pageSize,
            @RequestParam("pageNumber") @NotNull Integer pageNumber,
            Authentication authentication
    ) {
        final List<Transaction> transactionList = transactionService.getTransactionListByUserId(
                ((PrincipalDTO) authentication.getPrincipal()).getId(),
                PageRequest.of(pageNumber, pageSize)
        );

        return ResponseEntity.ok().body(this.modelMapper.map(transactionList, new TypeToken<List<TransactionDTO>>() {
        }.getType()));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "GetTransaction")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 204, message = "Could not find transaction")
    })
    public ResponseEntity<TransactionDTO> getTransaction(
            @PathVariable("id") Long id,
                Authentication authentication
    ) {
        final Optional<Transaction> optionalTransaction = transactionService.getTransactionByIdAndUserId(
                id,
                ((PrincipalDTO) authentication.getPrincipal()).getId()
        );

        return optionalTransaction
                .map(transaction -> ResponseEntity.ok().body(this.modelMapper.map(transaction, TransactionDTO.class)))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping
    @ApiOperation(value = "PostTransaction")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong")
    })
    public ResponseEntity<TransactionDTO> saveTransaction(
            @Valid @RequestBody PostTransactionDTO postTransactionDTO,
            HttpServletRequest request
    )
            throws ExecutionException, InterruptedException {

        final Transaction savedTransaction = transactionService
                .saveTransaction(this.modelMapper.map(postTransactionDTO, Transaction.class));

        final URI location = URI.create(String.format(
                request.getRequestURI() + "/%s",
                savedTransaction.getId()));

        return ResponseEntity.created(location).body(this.modelMapper.map(savedTransaction, TransactionDTO.class));
    }
}
