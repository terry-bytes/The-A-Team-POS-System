/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ateam.Service;

import ateam.Models.Email;
import ateam.Models.Inventory;
import ateam.Models.Product;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Train 09
 */
public interface EmailService {

    void sendMail(Email email);

    void sendReorderNotification(String managerEmail, List<Inventory> reorderList);

    void sendPasswordResetMail(String email, String otp);

    void VoucherEmail(String to, BigDecimal change, String voucher);

    public void sendSaleReceipt(String toEmail, String salespersonName, String saleTime, List<Product> items, BigDecimal totalAmountWithVAT, BigDecimal vatAmount, BigDecimal change, String paymentMethod, BigDecimal cashPaid, BigDecimal cardPaid, int saleID);
}
