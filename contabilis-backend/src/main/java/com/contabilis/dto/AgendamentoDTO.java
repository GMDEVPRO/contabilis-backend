package com.contabilis.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AgendamentoDTO {
    private Long servicoId;
    private LocalDate data;
}
