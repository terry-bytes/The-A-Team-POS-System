/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ateam.DTO;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author T440
 */
@Setter @Getter @AllArgsConstructor @NoArgsConstructor
public class TopSellingEmployeeDTO {
    private int productId;
    private int employeeId;
    private int totalSales;
    private BigDecimal totalAmount;
}

