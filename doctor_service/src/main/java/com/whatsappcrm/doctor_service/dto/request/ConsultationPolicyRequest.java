package com.whatsappcrm.doctor_service.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ConsultationPolicyRequest {

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal consultationFee;

    @Min(0)
    private Integer freeFollowUpDays;

    private boolean reportReviewFree;

    @DecimalMin("0.0")
    private BigDecimal followUpFee;
}