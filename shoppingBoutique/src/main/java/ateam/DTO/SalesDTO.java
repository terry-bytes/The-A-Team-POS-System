/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ateam.DTO;

import java.math.BigDecimal;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 *
 * @author T440
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SalesDTO {
    private int SaleId;
    private String teller;
    private String paymentMethod;
    private Timestamp salesDate;
    private BigDecimal totalAmount;
}
