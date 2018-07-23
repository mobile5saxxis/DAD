package apsara.saxxis.util;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import apsara.saxxis.models.Coupon;
import apsara.saxxis.models.Product;
import apsara.saxxis.models.Stock;

/**
 * Created by Rajesh Dabhi on 26/6/2017.
 */

public class DatabaseHandler {

    //    private static String DB_NAME = "grocery";
//    private static int DB_VERSION = 1;
//    private SQLiteDatabase db;
//
//    public static final String CART_TABLE = "cart";
//
//    public static final String COLUMN_ID = "product_id";
//    public static final String COLUMN_QTY = "qty";
//    public static final String COLUMN_IMAGE = "product_image";
//    public static final String COLUMN_CAT_ID = "category_id";
//    public static final String COLUMN_NAME = "product_name";
//    public static final String COLUMN_PRICE = "price";
//    public static final String COLUMN_UNIT_VALUE = "unit_value";
//    public static final String COLUMN_UNIT = "unit";
//
//    public static final String COLUMN_INCREMENT = "increament";
//    public static final String COLUMN_STOCK = "stock";
//    public static final String COLUMN_TITLE = "title";
//
//    public DatabaseHandler(Context context) {
//        super(context, DB_NAME, null, DB_VERSION);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        this.db = db;
//
//        String exe = "CREATE TABLE IF NOT EXISTS " + CART_TABLE
//                + "(" + COLUMN_ID + " integer primary key, "
//                + COLUMN_QTY + " DOUBLE NOT NULL,"
//                + COLUMN_IMAGE + " TEXT NOT NULL, "
//                + COLUMN_CAT_ID + " TEXT NOT NULL, "
//                + COLUMN_NAME + " TEXT NOT NULL, "
//                + COLUMN_PRICE + " DOUBLE NOT NULL, "
//                + COLUMN_UNIT_VALUE + " DOUBLE NOT NULL, "
//                + COLUMN_UNIT + " TEXT NOT NULL, "
//                + COLUMN_INCREMENT + " DOUBLE NOT NULL, "
//                + COLUMN_STOCK + " DOUBLE NOT NULL, "
//                + COLUMN_TITLE + " TEXT NOT NULL "
//                + ")";
//
//        db.execSQL(exe);
//
//    }
//
    public boolean setCart(Product product) {
        boolean isUpdated = false;

        try {
            if (isInCart(product.getProduct_id(), product.getStockId())) {
                Product p = getProduct(product.getProduct_id(), product.getStockId());

                if (p != null) {
                    p.delete();
                    product.setId(System.currentTimeMillis());
                    Product.save(product);
                }
            } else {
                product.setId(System.currentTimeMillis());
                Product.save(product);
            }

            isUpdated = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isUpdated;
    }

    public boolean isInCart(int id, int stockId) {
        boolean itemExist = false;

        try {
            if (stockId != -1) {
                List<Product> products = Product.find(Product.class, "productId=? AND stock_id=?", String.valueOf(id), String.valueOf(stockId));

                if (products != null && products.size() > 0) {
                    itemExist = true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return itemExist;
    }

    public boolean isInCart(int id) {
        boolean itemExist = false;

        try {
            List<Product> products = Product.find(Product.class, "productId=?", String.valueOf(id));

            if (products != null && products.size() > 0) {
                itemExist = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return itemExist;
    }

    public Product getProduct(int id, int stockId) {
        Product product = null;

        try {
            if (stockId != -1) {
                List<Product> products = Product.find(Product.class, "productId=? AND stock_id=?", String.valueOf(id), String.valueOf(stockId));

                if (products != null && products.size() > 0) {
                    product = products.get(0);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return product;
    }

    public Product getProduct(int id) {
        Product product = null;

        try {
            List<Product> products = Product.find(Product.class, "productId=?", String.valueOf(id));

            if (products != null && products.size() > 0) {
                product = products.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return product;
    }

    public String getCartItemQty(int id, int stockId) {

        String qty = "0";

        try {
            if (stockId != -1) {
                List<Product> products = Product.find(Product.class, "productId=? AND stock_id=?", String.valueOf(id), String.valueOf(stockId));

                if (products != null && products.size() > 0) {
                    qty = String.valueOf(products.get(0).getQuantity());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return qty;

    }

    public String getCartItemQty(int id) {

        String qty = "0";

        try {
            List<Product> products = Product.find(Product.class, "productId=?", String.valueOf(id));

            if (products != null && products.size() > 0) {
                qty = String.valueOf(products.get(0).getQuantity());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return qty;

    }

    //
//    public String getInCartItemQty(String id) {
//        if (isInCart(id)) {
//            db = getReadableDatabase();
//            String qry = "Select *  from " + CART_TABLE + " where " + COLUMN_ID + " = " + id;
//            Cursor cursor = db.rawQuery(qry, null);
//            cursor.moveToFirst();
//            return cursor.getString(cursor.getColumnIndex(COLUMN_QTY));
//        } else {
//            return "0.0";
//        }
//    }

    public String getCartCount() {
        int qty = 0;

        try {
            qty = (int) Product.count(Product.class);

            if (qty < 0) {
                qty = 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return String.valueOf(qty);
    }

    public String getSavedAmount() {
        int totalAmount = 0;

        try {
            List<Product> products = Product.listAll(Product.class);

            for (Product product : products) {

                List<Stock> stocks = new Gson().fromJson(product.getStocks(), new TypeToken<List<Stock>>() {
                }.getType());

                for (Stock stock : stocks) {
                    if (stock.getStockId() == product.getStockId()) {

                        int price = 0;

                        if (!TextUtils.isEmpty(stock.getStrikeprice())) {
                            price = Integer.parseInt(stock.getPrice_val()) - Integer.parseInt(stock.getStrikeprice());
                        }

                        totalAmount += price * product.getQuantity();
                        break;
                    }
                }
            }

            List<Coupon> coupons = Coupon.listAll(Coupon.class);

            if (coupons != null && coupons.size() > 0) {
                Coupon coupon = coupons.get(0);

                if (totalAmount >= Integer.parseInt(coupon.getCoupon_value())) {
                    totalAmount = totalAmount + Integer.parseInt(coupon.getCoupon_value());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return String.valueOf(totalAmount);
    }

    public String getDiscountTotalAmount() {
        int totalAmount = 0;

        try {
            List<Product> products = Product.listAll(Product.class);

            for (Product product : products) {

                List<Stock> stocks = new Gson().fromJson(product.getStocks(), new TypeToken<List<Stock>>() {
                }.getType());

                for (Stock stock : stocks) {
                    if (stock.getStockId() == product.getStockId()) {

                        String price = stock.getStrikeprice();

                        if (TextUtils.isEmpty(price)) {
                            price = stock.getPrice_val();
                        }

                        totalAmount += Integer.parseInt(price) * product.getQuantity();
                        break;
                    }
                }
            }

            List<Coupon> coupons = Coupon.listAll(Coupon.class);

            if (coupons != null && coupons.size() > 0) {
                Coupon coupon = coupons.get(0);

                if (totalAmount >= Integer.parseInt(coupon.getCoupon_value())) {
                    totalAmount = totalAmount - Integer.parseInt(coupon.getCoupon_value());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return String.valueOf(totalAmount);
    }

    public String getTotalAmount() {
        int totalAmount = 0;

        try {
            List<Product> products = Product.listAll(Product.class);

            for (Product product : products) {

                List<Stock> stocks = new Gson().fromJson(product.getStocks(), new TypeToken<List<Stock>>() {
                }.getType());

                for (Stock stock : stocks) {
                    if (stock.getStockId() == product.getStockId()) {

                        String price = stock.getStrikeprice();

                        if (TextUtils.isEmpty(price)) {
                            price = stock.getPrice_val();
                        }

                        totalAmount += Integer.parseInt(price) * product.getQuantity();
                        break;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return String.valueOf(totalAmount);
    }

    public String getCouponAmount() {
        String amount = "0";
        List<Coupon> coupons = Coupon.listAll(Coupon.class);

        if (coupons != null && coupons.size() > 0) {
            Coupon coupon = coupons.get(0);

            amount = coupon.getCoupon_value();
        }

        return amount;
    }

    public String getCouponTitle() {
        String title = null;
        List<Coupon> coupons = Coupon.listAll(Coupon.class);

        if (coupons != null && coupons.size() > 0) {
            Coupon coupon = coupons.get(0);

            title = coupon.getCoupon_title();
        }

        return title;
    }

    public List<Product> getCartAll() {
        List<Product> products = new ArrayList<>();

        try {
            products = Product.listAll(Product.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
    }

    //
//    public String getFavConcatString() {
//        db = getReadableDatabase();
//        String qry = "Select *  from " + CART_TABLE;
//        Cursor cursor = db.rawQuery(qry, null);
//        cursor.moveToFirst();
//        String concate = "";
//        for (int i = 0; i < cursor.getCount(); i++) {
//            if (concate.equalsIgnoreCase("")) {
//                concate = cursor.getString(cursor.getColumnIndex(COLUMN_ID));
//            } else {
//                concate = concate + "_" + cursor.getString(cursor.getColumnIndex(COLUMN_ID));
//            }
//            cursor.moveToNext();
//        }
//        return concate;
//    }
//
    public void clearCart() {
        Coupon.deleteAll(Coupon.class);
        Product.deleteAll(Product.class);
    }

    //
    public void removeItemFromCart(int id, int stockId) {
        try {
            Product product = getProduct(id, stockId);

            if (product != null) {
                product.delete();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addCoupon(Coupon coupon) {
        Coupon.deleteAll(Coupon.class);
        Coupon.save(coupon);
    }

    public void deleteCoupon() {
        Coupon.deleteAll(Coupon.class);
    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//
//    }

}
