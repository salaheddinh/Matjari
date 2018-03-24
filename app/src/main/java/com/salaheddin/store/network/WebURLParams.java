package com.salaheddin.store.network;

import android.os.Build;

import com.salaheddin.store.MatjariApplication;
import com.salaheddin.store.helpers.FilterTypesIds;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class WebURLParams {

    public static HashMap<String, String> getForgetPasswordParams(String email) {
        String data = "{\"email\":\"" + email + "\"}";
        HashMap<String, String> result = new HashMap<>();
        result.put("data", data);
        return result;
    }

    public static HashMap<String, String> getSignInParams(String email, String password, String deviceToken) {
        String data = "{\"email\":\"" + email + "\",\"password\":\"" + password + "\",\"deviceToken\":\""
                + deviceToken + "\"}";
        HashMap<String, String> result = new HashMap<>();
        result.put("data", data);
        return result;
    }

    public static HashMap<String, String> getResetPasswordParams(String email, String password, String code) {
        String data = "{\"email\":\"" + email + "\",\"password\":\"" + password +
                "\",\"code\":\"" + code + "\"}";
        HashMap<String, String> result = new HashMap<>();
        result.put("data", data);
        return result;
    }

    public static HashMap<String, String> getRegistrationParams(String email, String password, String firstName, String lastName, String gender) {
        String data = "{\"email\":\"" + email + "\",\"password\":\"" + password +
                "\",\"firstName\":\"" + firstName +
                "\",\"lastName\":\"" + lastName +
                "\",\"gender\":\"" + gender + "\"}";
        HashMap<String, String> result = new HashMap<>();
        result.put("data", data);
        return result;
    }

    public static HashMap<String, String> getHomeParams(String session, int slidesCount, int productsCount) {
        String data = "{\"session\":\"" + session + "\",\"slidesCount\":\"" + slidesCount + "\",\"productsCount\":\""
                + productsCount + "\"}";
        HashMap<String, String> result = new HashMap<>();
        result.put("data", data);
        return result;
    }

    public static HashMap<String, String> getHomePageProductsParams(String session, int pageNumber, int pageSize) {
        String data = "{\"session\":\"" + session + "\",\"pageNumber\":\"" + pageNumber + "\",\"pageSize\":\""
                + pageSize + "\"}";
        HashMap<String, String> result = new HashMap<>();
        result.put("data", data);
        return result;
    }

    public static HashMap<String, String> getCategoryProductsParams(String session, String categoryId, String typeId,
                                                                    int pageNumber, int pageSize,
                                                                    HashMap<Integer, String> filters,
                                                                    int sortBy,String key,String min,String max) {
        String data = "{\"session\":\"" + session + "\",\"pageNumber\":\"" + pageNumber
                + "\",\"categoryId\":\"" + categoryId
                + "\",\"typeId\":\"" + typeId
                + "\",\"filters\":" + convertListToString(filters,min,max)
                + ",\"sortBy\":\"" + sortBy
                + "\",\"key\":\"" + key
                + "\",\"pageSize\":\"" + pageSize + "\"}";
        HashMap<String, String> result = new HashMap<>();
        result.put("data", data);
        return result;
    }

    public static HashMap<String, String> getFilterParams(String session, String categoryId, String typeId,String key) {
        String data = "{\"session\":\"" + session
                + "\",\"categoryId\":\"" + categoryId
                + "\",\"key\":\"" + key
                + "\",\"typeId\":\"" + typeId + "\"}";
        HashMap<String, String> result = new HashMap<>();
        result.put("data", data);
        return result;
    }

    public static HashMap<String, String> getProductDetailsParams(String session, String productId) {
        String data = "{\"session\":\"" + session + "\",\"productId\":\""
                + productId + "\"}";
        HashMap<String, String> result = new HashMap<>();
        result.put("data", data);
        return result;
    }

    public static HashMap<String, String> getOfferDetailsParams(String session, String offerId) {
        String data = "{\"session\":\"" + session + "\",\"offerId\":\""
                + offerId + "\"}";
        HashMap<String, String> result = new HashMap<>();
        result.put("data", data);
        return result;
    }

    public static HashMap<String, String> getAllOffersParams(String session, int pageNumber, int pageSize) {
        String data = "{\"session\":\"" + session + "\",\"pageNumber\":\"" + pageNumber + "\",\"pageSize\":\""
                + pageSize + "\"}";
        HashMap<String, String> result = new HashMap<>();
        result.put("data", data);
        return result;
    }

    public static HashMap<String, String> getWishListParams(String session, int pageNumber, int pageSize) {
        String data = "{\"session\":\"" + session + "\",\"pageNumber\":\"" + pageNumber + "\",\"pageSize\":\""
                + pageSize + "\"}";
        HashMap<String, String> result = new HashMap<>();
        result.put("data", data);
        return result;
    }

    public static HashMap<String, String> getAllCategoriesParams(String session, int pageNumber, int pageSize) {
        String data = "{\"session\":\"" + session + "\",\"pageNumber\":\"" + pageNumber + "\",\"pageSize\":\""
                + pageSize + "\"}";
        HashMap<String, String> result = new HashMap<>();
        result.put("data", data);
        return result;
    }

    public static HashMap<String, String> getAddToCartParams(String session, String itemId,String sizeId) {
        String data = "{\"session\":\"" + session
                + "\",\"sizeId\":\"" + sizeId
                + "\",\"itemId\":\"" + itemId + "\"}";
        HashMap<String, String> result = new HashMap<>();
        result.put("data", data);
        return result;
    }

    public static HashMap<String, String> getAddToWishListParams(String session, String itemId,String size) {
        String data = "{\"session\":\"" + session
                + "\",\"itemId\":\"" + itemId
                + "\",\"size\":\"" + size
                + "\"}";
        HashMap<String, String> result = new HashMap<>();
        result.put("data", data);
        return result;
    }

    public static HashMap<String, String> getOrderOfferParams(String session, String offerId, int quantity) {
        String data = "{\"session\":\"" + session + "\",\"offerId\":\"" + offerId + "\",\"quantity\":\"" + quantity + "\"}";
        HashMap<String, String> result = new HashMap<>();
        result.put("data", data);
        return result;
    }

    public static HashMap<String, String> getCartDetailsParams(String session) {
        String data = "{\"session\":\"" + session + "\"}";
        HashMap<String, String> result = new HashMap<>();
        result.put("data", data);
        return result;
    }

    public static HashMap<String, String> getSubmitOrderParams(String session) {
        String data = "{\"session\":\"" + session + "\"}";
        HashMap<String, String> result = new HashMap<>();
        result.put("data", data);
        return result;
    }

    public static HashMap<String, String> getAddEditPhoneParams(String session, String id, String number) {
        String data = "{\"session\":\"" + session + "\",\"id\":\"" + id + "\",\"number\":\"" + number + "\"}";
        HashMap<String, String> result = new HashMap<>();
        result.put("data", data);
        return result;
    }

    public static HashMap<String, String> getDeletePhoneParams(String session, String id) {
        String data = "{\"session\":\"" + session + "\",\"id\":\"" + id + "\"}";
        HashMap<String, String> result = new HashMap<>();
        result.put("data", data);
        return result;
    }

    public static HashMap<String, String> getAddEditAddressParams(String session, String name, String note, String longitude, String latitude) {
        String data = "{\"session\":\"" + session + "\",\"name\":\"" + name + "\",\"note\":\"" + note +
                "\",\"longitude\":\"" + longitude +
                "\",\"latitude\":\"" + latitude + "\"}";
        HashMap<String, String> result = new HashMap<>();
        result.put("data", data);
        return result;
    }

    public static HashMap<String, String> getEditProfileParams(String session, String firstName, String lastName, String gender) {
        String data = "{\"session\":\"" + session + "\",\"firstName\":\"" + firstName + "\",\"lastName\":\"" + lastName +
                "\",\"gender\":\"" + gender + "\"}";
        HashMap<String, String> result = new HashMap<>();
        result.put("data", data);
        return result;
    }

    public static HashMap<String, String> getCancelOrderParams(String session, String orderId,String note) {
        String data = "{\"session\":\"" + session
                + "\",\"orderId\":\"" + orderId
                + "\",\"note\":\"" + note + "\"}";
        HashMap<String, String> result = new HashMap<>();
        result.put("data", data);
        return result;
    }

    public static HashMap<String, String> getSetProfileParams(String session, String firstName, String lastName, String gender
            , String address, String note, String longitude, String latitude, ArrayList<String> telephones) {
        String data = "{\"session\":\"" + session
                + "\",\"firstName\":\"" + firstName
                + "\",\"lastName\":\"" + lastName
                + "\",\"address\":\"" + address
                + "\",\"note\":\"" + note
                + "\",\"longitude\":\"" + longitude
                + "\",\"latitude\":\"" + latitude
                + "\",\"telephones\":" + convertListToString(telephones)
                + ",\"gender\":\"" + gender + "\"}";
        HashMap<String, String> result = new HashMap<>();
        result.put("data", data);
        return result;
    }

    public static Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        try {
            headers.put("Mobilemanufacture", URLEncoder.encode(Build.MANUFACTURER, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        headers.put("Appversion", MatjariApplication.APP_VERSION);
        try {
            headers.put("Mobilemodel", URLEncoder.encode(Build.MODEL, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            headers.put("Osversion", URLEncoder.encode(Build.VERSION.RELEASE, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        headers.put("Deviceid", MatjariApplication.DEVICE_ID);
        headers.put("Language", MatjariApplication.getInstance().getLangString() == "en" ? "0" : "1");
        headers.put("Platform", "2");
        headers.put("Devicetoken", "2");

        return headers;
    }

    private static String convertListToString(ArrayList<String> strings) {
        String list = "[";
        for (int i = 0; i < strings.size(); i++) {
            String string = strings.get(i);
            if (i == 0) {
                list += "\"" + string + "\"";
            } else {
                list += ",\"" + string + "\"";
            }
        }
        list += "]";
        return list;
    }

    private static String convertListToString(HashMap<Integer, String> map, String min, String max) {
        String list = "[";
        Object[] keys = map.keySet().toArray();
        for (int i = 0; i < keys.length; i++) {
            String item = "{";
            if (keys[i].equals(FilterTypesIds.PRICE)){
                item += "\"" + "id" + "\":\"" + keys[i].toString() + "\"";
                item += ",";
                item += "\"" + "min" + "\":\"" + min + "\"";
                item += ",";
                item += "\"" + "max" + "\":\"" + max + "\"";
                item += "}";
                if (i <keys.length - 1){
                    item += ",";
                }
            }else {
                item += "\"" + "id" + "\":\"" + keys[i].toString() + "\"";
                item += ",";
                item += "\"" + "value" + "\":\"" + map.get(keys[i]) + "\"";
                item += "}";
                if (i <keys.length - 1){
                    item += ",";
                }
            }

            list += item;
        }
        list += "]";
        return list;
    }
}
