package com.whatsappcrm.doctor_service.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ConsultationPolicyResponse {

    private Long id;
    private Long tenantId;
    private Long doctorId;

    private BigDecimal consultationFee;
    private Integer freeFollowUpDays;
    private boolean reportReviewFree;
    private BigDecimal followUpFee;
}
