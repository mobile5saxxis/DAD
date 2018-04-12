package codecanyon.grocery.util;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import codecanyon.grocery.models.Coupon;
import codecanyon.grocery.models.Product;
import codecanyon.grocery.models.Stock;

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
            if (isInCart(product.getProduct_id())) {
                Product.update(product);
            } else {
                Product.save(product);
            }

            isUpdated = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isUpdated;
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

    public String getTotalAmount() {
        int totalAmount = 0;

        try {
            List<Product> products = Product.listAll(Product.class);

            for (Product product : products) {

                List<Stock> stocks = new Gson().fromJson(product.getStocks(), new TypeToken<List<Stock>>() {
                }.getType());

                for (Stock stock : stocks) {
                    if (stock.getStockId() == product.getStockId()) {
                        totalAmount = Integer.parseInt(stock.getStrikeprice()) * product.getQuantity();
                        break;
                    }
                }
            }

            List<Coupon> coupons = Coupon.listAll(Coupon.class);

            if (coupons != null && coupons.size() > 0) {
                totalAmount = totalAmount - Integer.parseInt(coupons.get(0).getCoupon_value());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return String.valueOf(totalAmount);
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
    public void removeItemFromCart(long id) {
        try {
            Product p = Product.findById(Product.class, id);

            if (p != null) {
                p.delete();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addCoupon(Coupon coupon) {
        Coupon.deleteAll(Coupon.class);
        Coupon.save(coupon);
    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//
//    }

}
