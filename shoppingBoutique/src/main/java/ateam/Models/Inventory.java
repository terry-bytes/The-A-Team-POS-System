package ateam.Models;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {

    private int inventory_ID;
    private int product_ID;
    private int store_ID;
    private int inventory_quantity;
    private int reorder_point;
    private Timestamp last_updated;
    private int added_by_employee_ID;
}
