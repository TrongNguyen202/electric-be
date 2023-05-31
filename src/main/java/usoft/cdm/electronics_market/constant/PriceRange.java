package usoft.cdm.electronics_market.constant;

import usoft.cdm.electronics_market.model.PriceRangeModel;

import java.util.ArrayList;
import java.util.List;

public class PriceRange {
    public static final List<PriceRangeModel> list = new ArrayList<>();

    static {
        list.add(new PriceRangeModel("100k - 500k", 100000d, 500000d));
        list.add(new PriceRangeModel("500k - 1 triệu", 500000d, 1000000d));
        list.add(new PriceRangeModel("1 triệu - 1,5 triệu", 1000000d, 1500000d));
        list.add(new PriceRangeModel("1,5 triệu - 2 triệu", 1500000d, 2000000d));
        list.add(new PriceRangeModel("2 triệu - 3 triệu", 2000000d, 3000000d));
        list.add(new PriceRangeModel("3 triệu - 5 triệu", 3000000d, 5000000d));
        list.add(new PriceRangeModel("5 triệu - 8 triệu", 5000000d, 8000000d));
        list.add(new PriceRangeModel("8 triệu - 10 triệu", 8000000d, 10000000d));
        list.add(new PriceRangeModel("10 triệu - 15 triệu", 10000000d, 15000000d));
        list.add(new PriceRangeModel("15 triệu - 20 triệu", 15000000d, 20000000d));
        list.add(new PriceRangeModel("20 triệu - 25 triệu", 20000000d, 25000000d));
        list.add(new PriceRangeModel("25 triệu - 30 triệu", 25000000d, 30000000d));
        list.add(new PriceRangeModel("30 triệu - 40 triệu", 30000000d, 40000000d));
        list.add(new PriceRangeModel("40 triệu - 50 triệu", 40000000d, 50000000d));
        list.add(new PriceRangeModel("50 triệu - 100 triệu", 50000000d, 100000000d));
        list.add(new PriceRangeModel("100 triệu - 200 triệu", 100000000d, 200000000d));
        list.add(new PriceRangeModel("Trên 200 triệu", 200000000d, null));
    }
}
