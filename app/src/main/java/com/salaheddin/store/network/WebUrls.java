package com.salaheddin.store.network;

public class WebUrls {
    //private static String SERVER_URL = "http://matjaristore.com/";
    private static String SERVER_URL = "http://adeldev.com/matjari/";
    public static String IMAGES_SERVER_URL = " http://www.adeldev.com/matjari/files/";
    private static String AUTH_APIS = "store-webservices/api/api.php";
    private static String GENERIC_APIS = "store-webservices/api/api2.php";
    private static String CART_APIS = "store-webservices/api/api3.php";

    //authentication
    private static String SIGN_IN = "SignIn";
    private static String REGISTRATION = "Registration";
    private static String FORGET_PASSWORD = "ForgotPassword";
    private static String RESEND_VERIFICATION_CODE = "ResendVerificationCode";
    private static String RESET_PASSWORD = "ResetPassword";

    private static String ALL_CATEGORIES = "AllCategories";
    private static String HOME = "HomePage";
    private static String HOME_PAGE_PRODUCTS = "HomePageProducts";
    private static String CATEGORY_PRODUCTS = "CategoryProducts";
    private static String FILTERS = "Filters";
    private static String ALL_OFFERS = "Offers";
    private static String PRODUCT_DETAILS = "ProductDetails";
    private static String OFFER_DETAILS = "OfferDetails";
    private static String ADD_TO_CART = "AddToCart";
    private static String ORDER_OFFER = "OrderOffer";
    private static String CART_DETAILS = "CartDetails";
    private static String INCREASE_QUANTITY = "IncreaseQuantity";
    private static String DECREASE_QUANTITY = "DecreaseQuantity";
    private static String REMOVE_FROM_CART = "RemoveFromCart";
    private static String SUBMIT_ORDER = "SubmitOrder";
    private static String ADD_TO_WISH_LIST = "AddToWishList";
    private static String REMOVE_FROM_WISH_LIST = "RemoveFromWishList";
    private static String WISH_LIST = "WishList";
    private static String MY_ORDERS = "MyOrders";
    private static String CHECK_OUT = "CheckOut";
    private static String ADD_EDIT_PHONE = "AddEditTelephone";
    private static String DELETE_PHONE = "DeleteTelephone";
    private static String ADD_EDIT_ADDRESS = "AddEditAddress";
    private static String VIEW_PROFILE = "ViewProfile";
    private static String EDIT_PROFILE = "EditProfile";
    private static String SET_PROFILE = "SetProfile";
    private static String DICTIONARY = "Dictionary";
    private static String CANCEL_ORDER = "CancelOrder";

    public static String getImagesUrl(String type, String size) {
        return SERVER_URL + "files/" + type + "/" + size + "/";
    }

    public static String getHomeUrl() {
        return SERVER_URL + GENERIC_APIS + "?name=" + HOME;
    }

    public static String getHomePageProductsUrl() {
        return SERVER_URL + GENERIC_APIS + "?name=" + HOME_PAGE_PRODUCTS;
    }

    public static String getCategoryProductsUrl() {
        return SERVER_URL + GENERIC_APIS + "?name=" + CATEGORY_PRODUCTS;
    }

    public static String getFiltersUrl() {
        return SERVER_URL + GENERIC_APIS + "?name=" + FILTERS;
    }

    public static String getAllOffersUrl() {
        return SERVER_URL + GENERIC_APIS + "?name=" + ALL_OFFERS;
    }

    public static String getAllCategoriesUrl() {
        return SERVER_URL + GENERIC_APIS + "?name=" + ALL_CATEGORIES;
    }

    public static String getProductDetailsUrl() {
        return SERVER_URL + GENERIC_APIS + "?name=" + PRODUCT_DETAILS;
    }

    public static String getOfferDetailsUrl() {
        return SERVER_URL + GENERIC_APIS + "?name=" + OFFER_DETAILS;
    }

    public static String getAddToCartUrl() {
        return SERVER_URL + CART_APIS + "?name=" + ADD_TO_CART;
    }

    public static String getOrderOfferUrl() {
        return SERVER_URL + CART_APIS + "?name=" + ORDER_OFFER;
    }

    public static String getCartDetailsUrl() {
        return SERVER_URL + CART_APIS + "?name=" + CART_DETAILS;
    }

    public static String getDecreaseQuantityUrl() {
        return SERVER_URL + CART_APIS + "?name=" + DECREASE_QUANTITY;
    }

    public static String getIncreaseQuantityUrl() {
        return SERVER_URL + CART_APIS + "?name=" + INCREASE_QUANTITY;
    }

    public static String getRemoveFromCartUrl() {
        return SERVER_URL + CART_APIS + "?name=" + REMOVE_FROM_CART;
    }

    public static String getSubmitOrderUrl() {
        return SERVER_URL + CART_APIS + "?name=" + SUBMIT_ORDER;
    }

    public static String getAddToWishListUrl() {
        return SERVER_URL + CART_APIS + "?name=" + ADD_TO_WISH_LIST;
    }

    public static String getRemoveFromWishListUrl() {
        return SERVER_URL + CART_APIS + "?name=" + REMOVE_FROM_WISH_LIST;
    }

    public static String getWishListUrl() {
        return SERVER_URL + CART_APIS + "?name=" + WISH_LIST;
    }

    public static String getMyOrdersUrl() {
        return SERVER_URL + CART_APIS + "?name=" + MY_ORDERS;
    }

    public static String getCheckOutUrl() {
        return SERVER_URL + CART_APIS + "?name=" + CHECK_OUT;
    }

    public static String getViewProfileUrl() {
        return SERVER_URL + CART_APIS + "?name=" + VIEW_PROFILE;
    }

    public static String getEditProfileUrl() {
        return SERVER_URL + CART_APIS + "?name=" + EDIT_PROFILE;
    }

    public static String getSetProfileUrl() {
        return SERVER_URL + CART_APIS + "?name=" + SET_PROFILE;
    }

    public static String getAddEditPhoneUrl() {
        return SERVER_URL + CART_APIS + "?name=" + ADD_EDIT_PHONE;
    }

    public static String getDeletePhoneUrl() {
        return SERVER_URL + CART_APIS + "?name=" + DELETE_PHONE;
    }

    public static String getAddEditAddressUrl() {
        return SERVER_URL + CART_APIS + "?name=" + ADD_EDIT_ADDRESS;
    }

    public static String getSignInUrl() {
        return SERVER_URL + AUTH_APIS + "?name=" + SIGN_IN;
    }

    public static String getRegistrationUrl() {
        return SERVER_URL + AUTH_APIS + "?name=" + REGISTRATION;
    }

    public static String getForgetPasswordUrl() {
        return SERVER_URL + AUTH_APIS + "?name=" + FORGET_PASSWORD;
    }

    public static String getResendVerificationCodeUrl() {
        return SERVER_URL + AUTH_APIS + "?name=" + RESEND_VERIFICATION_CODE;
    }

    public static String getResetPasswordUrl() {
        return SERVER_URL + AUTH_APIS + "?name=" + RESET_PASSWORD;
    }

    public static String getDictionaryUrl() {
        return SERVER_URL + GENERIC_APIS + "?name=" + DICTIONARY;
    }

    public static String getCancelOrderUrl() {
        String s = SERVER_URL + AUTH_APIS + "?name=" + CANCEL_ORDER;
        return SERVER_URL + AUTH_APIS + "?name=" + CANCEL_ORDER;
    }
}
