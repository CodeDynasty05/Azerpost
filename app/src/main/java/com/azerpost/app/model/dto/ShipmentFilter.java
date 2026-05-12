package com.azerpost.app.model.dto;


import com.azerpost.app.model.enums.ShipmentType;
import com.azerpost.app.model.enums.Status;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ShipmentFilter {
    private Status status;

    @Size(max = 100)
    private String receiverName;

    private ShipmentType shipmentType;

    @PositiveOrZero
    private Integer minPrice;

    @PositiveOrZero
    private Integer maxPrice;

    @AssertTrue(message = "minPrice must be less than or equal to maxPrice")
    public boolean isPriceRangeValid() {
        return minPrice == null || maxPrice == null || minPrice <= maxPrice;
    }
}
