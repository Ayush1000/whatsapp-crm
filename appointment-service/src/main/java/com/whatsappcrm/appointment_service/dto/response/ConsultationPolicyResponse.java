package com.whatsappcrm.appointment_service.dto.response;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class ConsultationPolicyResponse {

    private Long doctorId;

    private BigDecimal consultationFee;

    private Integer freeFollowUpDays;

    private boolean reportReviewFree;

    private BigDecimal followUpFee;
}
