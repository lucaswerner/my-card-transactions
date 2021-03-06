package com.mycard.transactions.controller;

import com.mycard.transactions.dto.PostTransactionDTO;
import com.mycard.transactions.dto.PrincipalDTO;
import com.mycard.transactions.dto.TransactionDTO;
import com.mycard.transactions.service.TransactionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("transactions")
@Api(tags = "transaction")
public class TransactionController {

    private TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    @ApiOperation(value = "UserTransactionPage")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong")
    })
    public ResponseEntity<Page<TransactionDTO>> getUserTransactionPage(
            @RequestParam("pageNumber") @NotNull Integer pageNumber,
            Authentication authentication
    ) {
        return ResponseEntity.ok().body(
                transactionService.getTransactionDTOPageByUserId(
                        ((PrincipalDTO) authentication.getPrincipal()).getId(),
                        pageNumber
                )
        );
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "UserTransaction")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 204, message = "Could not find transaction")
    })
    public ResponseEntity<TransactionDTO> getTUserTransaction(
            @PathVariable("id") Long id,
            Authentication authentication
    ) {
        return transactionService.getTransactionDTOByIdAndUserId(
                id,
                ((PrincipalDTO) authentication.getPrincipal()).getId()
        )
                .map(transaction -> ResponseEntity.ok().body(transaction))
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
    ) {
        final TransactionDTO transactionDTO = transactionService.saveTransaction(postTransactionDTO);
        final URI location = URI.create(String.format(
                request.getRequestURI() + "/%s",
                transactionDTO.getId()));

        return ResponseEntity.created(location).body(transactionDTO);
    }

    @GetMapping("/card-and-date")
    @ApiOperation(value = "TransactionListByCardAndDate")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 204, message = "Could not find transaction")
    })
    public ResponseEntity<List<TransactionDTO>> getTransactionListByCardAndDate(
            @RequestParam("cardBin") Long cardBin,
            @RequestParam("cardNumber") Long cardNumber,
            @RequestParam("timeStampStart") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate timeStampStart,
            @RequestParam("timeStampEnd") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate timestampEnd
    ) {
        return ResponseEntity
                .ok()
                .body(transactionService.getTransactionDTOListByCardAndDate(cardBin, cardNumber, timeStampStart, timestampEnd));
    }
}
