package com.salaheddin.store.network;

import android.util.Log;

import com.salaheddin.store.R;
import com.salaheddin.store.helpers.AppConstants;
import com.salaheddin.store.helpers.ListsTypes;
import com.salaheddin.store.models.Brand;
import com.salaheddin.store.models.Cart;
import com.salaheddin.store.models.Category;
import com.salaheddin.store.models.Color;
import com.salaheddin.store.models.DictionaryItem;
import com.salaheddin.store.models.FilterTypes;
import com.salaheddin.store.models.Image;
import com.salaheddin.store.models.Item;
import com.salaheddin.store.models.KeyValue;
import com.salaheddin.store.models.ListWithType;
import com.salaheddin.store.models.MainType;
import com.salaheddin.store.models.Offer;
import com.salaheddin.store.models.Order;
import com.salaheddin.store.models.Product;
import com.salaheddin.store.models.Profile;
import com.salaheddin.store.models.Season;
import com.salaheddin.store.models.SliderItem;
import com.salaheddin.store.models.Store;
import com.salaheddin.store.models.SubCategory;
import com.salaheddin.store.models.TelephoneNumber;
import com.salaheddin.store.models.Type;
import com.salaheddin.store.models.User;
import com.salaheddin.store.models.UserAddress;
import com.salaheddin.store.models.WebServiceResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonParser {
    public static WebServiceResponse json2WebServiceResponse(JSONObject root) {
        int code = 0;
        try {
            code = root.getInt("code");
        } catch (JSONException e) {
        }

        String errorMessage = "";
        try {
            errorMessage = root.getString("message");
        } catch (JSONException e) {
        }

        JSONObject data = null;
        try {
            data = root.getJSONObject("data");
        } catch (JSONException ex) {
        }

        return new WebServiceResponse(code, errorMessage, data);
    }

    public static User json2User(JSONObject root) {
        String id = "";
        try {
            id = root.getString("userId");
        } catch (JSONException e) {
        }

        String session = "";
        try {
            session = root.getString("session");
        } catch (JSONException e) {
        }

        String firstName = "";
        try {
            firstName = root.getString("firstName");
        } catch (JSONException ex) {
        }

        String lastName = "";
        try {
            lastName = root.getString("lastName");
        } catch (JSONException ex) {
        }

        String email = "";
        try {
            email = root.getString("email");
        } catch (JSONException ex) {
        }

        String relatedStoreId = "";
        try {
            relatedStoreId = root.getString("relatedStoreId");
        } catch (JSONException ex) {
        }

        String hashKey = "";
        try {
            hashKey = root.getString("hashKey");
        } catch (JSONException ex) {
        }

        boolean isAdmin = false;
        try {
            isAdmin = root.getBoolean("storeAdmin");
        } catch (JSONException ex) {
        }

        int cartItems = 0;
        try {
            cartItems = root.getInt("cartItems");
        } catch (JSONException ex) {
        }
        return new User(id, session, firstName, lastName, email, hashKey, relatedStoreId, isAdmin, cartItems);
    }

    public static ArrayList<Object> json2Home(JSONObject root, String size) {
        ArrayList offers = new ArrayList<>();
        ArrayList products = new ArrayList<>();
        ArrayList slider = new ArrayList<>();
        try {
            offers = json2OfferList(root.getJSONObject("offers").getJSONArray("dataArray"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            products = json2ProductList(root.getJSONObject("products").getJSONArray("dataArray"), size);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            slider = json2SliderItemList(root.getJSONObject("slider").getJSONArray("dataArray"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayList<Object> objects = new ArrayList<>();

        MainType offerType = new MainType("", "Matjary Offers", R.mipmap.ic_fire);
        MainType fashionType = new MainType("", "Fashion", R.mipmap.ic_diamond);
        MainType lifeStyleType = new MainType("", "Life Style", R.mipmap.ic_lotus);
        MainType saleType = new MainType("", "Sale", R.mipmap.ic_sale);

        if (slider.size() > 0) {
            objects.add(new ListWithType(slider, ListsTypes.SLIDER));
        }
        if (offers.size() > 0) {
            objects.add(offerType);
            objects.add(new ListWithType(offers, ListsTypes.OFFERS));
        }
        objects.add("help");

        ArrayList firstTypes = new ArrayList();
        ArrayList secondTypes = new ArrayList();
        try {
            JSONArray jsonArray = root.getJSONArray("types");
            firstTypes = json2TypesList(jsonArray.getJSONObject(0).getJSONArray("subTypes"));
            secondTypes = json2TypesList(jsonArray.getJSONObject(1).getJSONArray("subTypes"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        objects.add(fashionType);
        objects.add(new ListWithType(firstTypes, ListsTypes.CATEGORIES));
        objects.add(lifeStyleType);
        objects.add(new ListWithType(secondTypes, ListsTypes.CATEGORIES));
        objects.add(saleType);

        objects.add(new ListWithType(products, ListsTypes.PRODUCTS));
        return objects;
    }

    public static Offer json2Offer(JSONObject root) {
        String id = "";
        try {
            id = root.getString("offerId");
        } catch (JSONException e) {
        }

        String name = "";
        try {
            name = root.getString("offerName");
        } catch (JSONException e) {
        }

        String desc = "";
        try {
            desc = root.getString("offerDesc");
        } catch (JSONException ex) {
        }

        String price = "";
        try {
            price = root.getString("offerPrice");
        } catch (JSONException ex) {
        }

        String image = "";
        try {
            String icon = root.getString("offerImage");
            image = WebUrls.getImagesUrl(AppConstants.offers, AppConstants.medium) + icon;
        } catch (JSONException ex) {
        }

        int status = 0;
        try {
            status = root.getInt("offerStatus");
        } catch (JSONException ex) {
        }

        int itemCounts = 0;
        try {
            itemCounts = root.getInt("offerItemsCount");
        } catch (JSONException ex) {
        }

        String oldPrice = "";
        try {
            oldPrice = root.getString("offerItemsPrice");
        } catch (JSONException ex) {
        }

        double remainingTime = 0;
        try {
            remainingTime = root.getDouble("offerRemainingTime");
        } catch (JSONException ex) {
        }

        ArrayList<Product> products = new ArrayList<>();
        try {
            products = json2ProductList(root.getJSONArray("offerProducts"), AppConstants.medium);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new Offer(id, name, desc, oldPrice, price, image, status, itemCounts, remainingTime, products);
    }

    public static ArrayList<Offer> json2OfferList(JSONArray root) {
        ArrayList<Offer> offers = new ArrayList<>();

        for (int i = 0; i < root.length(); i++) {
            Offer offer = new Offer();
            try {
                offer = json2Offer(root.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            offers.add(offer);
        }

        return offers;
    }

    private static Item json2Item(JSONObject root, String size) {
        String id = "";
        try {
            id = root.getString("id");
        } catch (JSONException e) {
        }

        Color color = new Color();
        try {
            color = json2Color(root.getJSONObject("color"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayList<Image> images = new ArrayList<>();
        try {
            images = json2ImageList(root.getJSONArray("images"), size);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        int quantity = 0;
        try {
            quantity = root.getInt("quantity");
        } catch (JSONException ex) {
        }

        String price = "";
        try {
            price = root.getString("price");
        } catch (JSONException ex) {
        }

        String newPrice = "";
        try {
            newPrice = root.getString("newPrice");
        } catch (JSONException ex) {
        }

        Double discount = 0.0;
        try {
            discount = root.getDouble("discount");
        } catch (JSONException ex) {
        }

        boolean isWished = false;
        try {
            isWished = root.getBoolean("isWished");
        } catch (JSONException ex) {
        }

        ArrayList<KeyValue> sizes = new ArrayList<>();
        try {
            sizes = json2SizesList(root.getJSONObject("size").getJSONArray("values"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String currency = "";
        try {
            currency = root.getString("currency");
        } catch (JSONException ex) {
        }

        return new Item(id, color, images, quantity, price, newPrice, discount, isWished, sizes, currency);
    }

    private static ArrayList<Item> json2ItemList(JSONArray root, String size) {
        ArrayList<Item> items = new ArrayList<>();
        for (int i = 0; i < root.length(); i++) {
            Item item = new Item();
            try {
                item = json2Item(root.getJSONObject(i), size);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            items.add(item);
        }

        return items;
    }

    private static Image json2Image(JSONObject root, String size) {
        String id = "";
        try {
            id = root.getString("id");
        } catch (JSONException e) {
        }

        String imageURL = "";
        try {
            String icon = root.getString("URL");
            imageURL = WebUrls.getImagesUrl(AppConstants.products, size) + icon;
        } catch (JSONException ex) {
        }

        return new Image(id, imageURL);
    }

    private static ArrayList<Image> json2ImageList(JSONArray root, String size) {
        ArrayList<Image> images = new ArrayList<>();

        for (int i = 0; i < root.length(); i++) {
            Image image = new Image();
            try {
                image = json2Image(root.getJSONObject(i), size);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            images.add(image);
        }

        return images;
    }

    private static Color json2Color(JSONObject root) {
        String id = "";
        try {
            id = root.getString("id");
        } catch (JSONException e) {
        }

        String imageURL = "";
        try {
            String icon = root.getString("url");
            imageURL = WebUrls.getImagesUrl(AppConstants.colors, AppConstants.medium).concat(icon);
        } catch (JSONException ex) {
        }

        return new Color(id, imageURL);
    }

    public static ArrayList<Color> json2ColorList(JSONArray root) {
        ArrayList<Color> colors = new ArrayList<>();

        for (int i = 0; i < root.length(); i++) {
            Color color = new Color();
            try {
                color = json2Color(root.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            colors.add(color);
        }

        return colors;
    }

    public static Product json2Product(JSONObject root, String size) {
        String id = "";
        try {
            id = root.getString("id");
        } catch (JSONException e) {
        }

        String name = "";
        try {
            name = root.getString("name");
        } catch (JSONException e) {
        }

        String desc = "";
        try {
            desc = root.getString("desc");
        } catch (JSONException ex) {
        }

        int canBeShipped = 0;
        try {
            canBeShipped = root.getInt("canBeShipped");
        } catch (JSONException ex) {
        }

        String shippingFee = "";
        try {
            shippingFee = root.getString("shippingFee");
        } catch (JSONException ex) {
        }

        ArrayList<Item> items = new ArrayList<>();
        try {
            items = json2ItemList(root.getJSONArray("items"), size);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Type type = new Type();
        try {
            type = json2Type(root.getJSONObject("type"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Brand brand = new Brand();
        try {
            brand = json2Brand(root.getJSONObject("brand"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Store store = new Store();
        try {
            store = json2Store(root.getJSONObject("store"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Season season = new Season();
        try {
            season = json2Season(root.getJSONObject("season"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String itemsCount = "";
        try {
            itemsCount = root.getString("itemsCount");
        } catch (JSONException ex) {
        }

        ArrayList<SubCategory> categories = new ArrayList<>();
        try {
            categories = json2SubCategoryList(root.getJSONArray("categories"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayList<Product> relatedProducts = new ArrayList<>();
        try {
            relatedProducts = json2ProductList(root.getJSONArray("relatedProducts"), AppConstants.medium);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new Product(id, name, desc, canBeShipped, shippingFee, items, type, brand, store, categories, season, itemsCount, relatedProducts);
    }

    public static ArrayList<Product> json2ProductList(JSONArray root, String size) {
        ArrayList<Product> products = new ArrayList<>();

        for (int i = 0; i < root.length(); i++) {
            Product product = new Product();
            try {
                product = json2Product(root.getJSONObject(i), size);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            products.add(product);
        }

        return products;
    }

    private static ArrayList<SliderItem> json2SliderItemList(JSONArray root) {
        ArrayList<SliderItem> sliderItems = new ArrayList<>();

        for (int i = 0; i < root.length(); i++) {
            SliderItem sliderItem = new SliderItem();
            try {
                sliderItem = json2SliderItem(root.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            sliderItems.add(sliderItem);
        }

        return sliderItems;
    }

    private static SliderItem json2SliderItem(JSONObject root) {
        String id = "";
        try {
            id = root.getString("id");
        } catch (JSONException e) {
            Log.d("ttt", e.toString());
        }

        String categoryId = "";
        try {
            categoryId = root.getString("categoryId");
        } catch (JSONException e) {
            Log.d("ttt", e.toString());
        }

        String image = "";
        try {
            String icon = root.getString("image");
            image = WebUrls.getImagesUrl(AppConstants.sliders, AppConstants.medium) + icon;
            //image = WebUrls.getImagesUrl(AppConstants.item_types, AppConstants.medium).concat(icon);
        } catch (JSONException ex) {
            Log.d("ttt", ex.toString());
        }

        return new SliderItem(id, image, categoryId);
    }

    private static Type json2Type(JSONObject root) {
        String id = "";
        try {
            id = root.getString("id");
        } catch (JSONException e) {
        }

        String name = "";
        try {
            name = root.getString("name");
        } catch (JSONException e) {
        }

        String image = "";
        try {
            String icon = root.getString("icon");
            image = WebUrls.getImagesUrl(AppConstants.item_types, AppConstants.medium).concat(icon);
        } catch (JSONException ex) {
        }

        return new Type(id, name, image);
    }

    private static Brand json2Brand(JSONObject root) {
        String id = "";
        try {
            id = root.getString("id");
        } catch (JSONException e) {
        }

        String name = "";
        try {
            name = root.getString("name");
        } catch (JSONException e) {
        }

        String image = "";
        try {
            String icon = root.getString("icon ");
            image = WebUrls.getImagesUrl(AppConstants.brands, AppConstants.medium).concat(icon);
        } catch (JSONException ex) {
            Log.d("gfgf", ex.toString());
        }

        return new Brand(id, image, name);
    }

    private static Season json2Season(JSONObject root) {
        String id = "";
        try {
            id = root.getString("id");
        } catch (JSONException e) {
        }

        String name = "";
        try {
            name = root.getString("name");
        } catch (JSONException e) {
        }

        return new Season(id, name);
    }

    private static Store json2Store(JSONObject root) {
        String id = "";
        try {
            id = root.getString("id");
        } catch (JSONException e) {
        }

        String name = "";
        try {
            name = root.getString("name");
        } catch (JSONException e) {
        }

        String image = "";
        try {
            String icon = root.getString("logo");
            image = WebUrls.getImagesUrl(AppConstants.branches, AppConstants.medium) + icon;
        } catch (JSONException ex) {
        }

        String countryId = "";
        try {
            countryId = root.getString("countryId");
        } catch (JSONException e) {
        }

        String mainStoreId = "";
        try {
            mainStoreId = root.getString("mainStoreId");
        } catch (JSONException e) {
        }

        String longitude = "";
        try {
            longitude = root.getString("longitude");
        } catch (JSONException e) {
        }

        String phone = "";
        try {
            phone = root.getString("phone");
        } catch (JSONException e) {
        }

        String latitude = "";
        try {
            latitude = root.getString("latitude");
        } catch (JSONException e) {
        }

        String locationDescription = "";
        try {
            locationDescription = root.getString("locationDescription");
        } catch (JSONException e) {
        }

        return new Store(id, name, image, phone, countryId, mainStoreId, longitude, latitude, locationDescription);
    }

    private static ArrayList<Type> json2TypesList(JSONArray root) {
        ArrayList<Type> types = new ArrayList<>();

        for (int i = 0; i < root.length(); i++) {
            Type type = new Type();
            try {
                type = json2Type(root.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            types.add(type);
        }

        return types;
    }

    private static SubCategory json2SubCategory(JSONObject root) {
        String id = "";
        try {
            id = root.getString("id");
        } catch (JSONException e) {
        }

        String name = "";
        try {
            name = root.getString("name");
        } catch (JSONException e) {
        }

        String image = "";
        try {
            String icon = root.getString("icon");
            image = WebUrls.getImagesUrl(AppConstants.sub_categories, AppConstants.medium) + icon;
        } catch (JSONException ex) {
        }

        String description = "";
        try {
            description = root.getString("desc");
        } catch (JSONException ex) {
        }

        return new SubCategory(id, name, image, description);
    }

    private static ArrayList<SubCategory> json2SubCategoryList(JSONArray root) {
        ArrayList<SubCategory> categories = new ArrayList<>();

        for (int i = 0; i < root.length(); i++) {
            SubCategory category = new SubCategory();
            try {
                category = json2SubCategory(root.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            categories.add(category);
        }
        return categories;
    }

    private static Category json2Category(JSONObject root) {
        String id = "";
        try {
            id = root.getString("id");
        } catch (JSONException e) {
        }

        String name = "";
        try {
            name = root.getString("name");
        } catch (JSONException e) {
        }

        String image = "";
        try {
            String icon = root.getString("icon");
            image = WebUrls.getImagesUrl(AppConstants.categories, AppConstants.medium) + icon;
        } catch (JSONException ex) {
        }

        String description = "";
        try {
            description = root.getString("desc");
        } catch (JSONException ex) {
        }

        ArrayList<SubCategory> subCategories = new ArrayList<>();
        try {
            subCategories = json2SubCategoryList(root.getJSONArray("subCategories"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new Category(id, name, image, description, subCategories);
    }

    public static Cart json2Cart(JSONObject root) {
        String id = "";
        try {
            id = root.getString("id");
        } catch (JSONException e) {
        }

        String price = "";
        try {
            price = root.getString("price");
        } catch (JSONException e) {
        }

        String orderStatus = "";
        try {
            orderStatus = root.getString("orderStatus");
        } catch (JSONException ex) {
        }

        String orderDate = "";
        try {
            orderDate = root.getString("orderDate");
        } catch (JSONException ex) {
        }

        String isOrdered = "";
        try {
            isOrdered = root.getString("isOrdered");
        } catch (JSONException ex) {
        }

        String cancellationNote = "";
        try {
            cancellationNote = root.getString("cancellationNote");
        } catch (JSONException ex) {
        }

        String itemsCount = "";
        try {
            itemsCount = root.getString("itemsCount");
        } catch (JSONException ex) {
        }

        ArrayList<Product> products = new ArrayList<>();
        try {
            products = json2ProductList(root.getJSONArray("products"), AppConstants.medium);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String currency = "";
        try {
            currency = root.getString("currency");
        } catch (JSONException ex) {
        }

        return new Cart(id, price, orderStatus, orderDate, isOrdered, cancellationNote, itemsCount, products, currency);
    }

    public static ArrayList<Object> json2MainCategoryList(JSONArray root) {
        ArrayList<Object> objects = new ArrayList<>();
        ArrayList<Category> categories = new ArrayList<>();
        for (int i = 0; i < root.length(); i++) {
            Category category = new Category();
            try {
                category = json2Category(root.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            categories.add(category);
        }
        for (int i = 0; i < categories.size(); i++) {
            objects.add(categories.get(i));
            objects.add(categories.get(i).getSubCategories());
        }
        return objects;
    }

    public static ArrayList<Order> json2OrderList(JSONArray root) {
        ArrayList<Order> orders = new ArrayList<>();

        for (int i = 0; i < root.length(); i++) {
            Order order = new Order();
            try {
                order = json2Order(root.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            orders.add(order);
        }

        return orders;
    }

    private static Order json2Order(JSONObject root) {
        String id = "";
        try {
            id = root.getString("id");
        } catch (JSONException e) {
        }

        String price = "";
        try {
            price = root.getString("price");
        } catch (JSONException e) {
        }

        String orderStatus = "";
        try {
            orderStatus = root.getString("orderStatus");
        } catch (JSONException ex) {
        }

        String orderDate = "";
        try {
            orderDate = root.getString("orderDate");
        } catch (JSONException ex) {
        }

        String isOrdered = "";
        try {
            isOrdered = root.getString("isOrdered");
        } catch (JSONException ex) {
        }

        String cancellationNote = "";
        try {
            cancellationNote = root.getString("cancellationNote");
        } catch (JSONException ex) {
        }

        String currency = "";
        try {
            currency = root.getString("currency");
        } catch (JSONException ex) {
        }

        return new Order(id, price, orderStatus, orderDate, isOrdered, cancellationNote, currency);
    }

    private static ArrayList<TelephoneNumber> json2TelephoneNumbersList(JSONArray root) {
        ArrayList<TelephoneNumber> telephoneNumbers = new ArrayList<>();

        for (int i = 0; i < root.length(); i++) {
            TelephoneNumber telephoneNumber = new TelephoneNumber();
            try {
                telephoneNumber = json2TelephoneNumber(root.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            telephoneNumbers.add(telephoneNumber);
        }

        return telephoneNumbers;
    }

    private static TelephoneNumber json2TelephoneNumber(JSONObject root) {
        String id = "";
        try {
            id = root.getString("id");
        } catch (JSONException e) {
        }

        String number = "";
        try {
            number = root.getString("number");
        } catch (JSONException e) {
        }

        return new TelephoneNumber(id, number);
    }

    private static UserAddress json2userUserAddress(JSONObject root) {
        String id = "";
        try {
            id = root.getString("id");
        } catch (JSONException e) {
        }

        String name = "";
        try {
            name = root.getString("name");
        } catch (JSONException e) {
        }

        String note = "";
        try {
            note = root.getString("note");
        } catch (JSONException e) {
        }

        String longitude = "";
        try {
            longitude = root.getString("longitude");
        } catch (JSONException e) {
        }

        String latitude = "";
        try {
            latitude = root.getString("latitude");
        } catch (JSONException e) {
        }

        ArrayList<TelephoneNumber> telephoneNumbers = new ArrayList<>();
        try {
            telephoneNumbers = json2TelephoneNumbersList(root.getJSONArray("telephoneNumbers"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new UserAddress(id, name, note, longitude, latitude, telephoneNumbers);
    }

    public static Profile json2Profile(JSONObject root) {
        String id = "";
        try {
            id = root.getString("id");
        } catch (JSONException e) {
        }

        String firetName = "";
        try {
            firetName = root.getString("firetName");
        } catch (JSONException e) {
        }

        String lastName = "";
        try {
            lastName = root.getString("lastName");
        } catch (JSONException e) {
        }

        String email = "";
        try {
            email = root.getString("email");
        } catch (JSONException e) {
        }

        boolean isStoreAdmin = false;
        try {
            isStoreAdmin = root.getBoolean("isStoreAdmin");
        } catch (JSONException e) {
        }

        String storeId = "";
        try {
            storeId = root.getString("storeId");
        } catch (JSONException e) {
        }

        String gender = "";
        try {
            gender = root.getString("gender");
        } catch (JSONException e) {
        }

        UserAddress userAddress = null;
        try {
            userAddress = json2userUserAddress(root.getJSONArray("userAddresses").getJSONObject(0));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new Profile(id, firetName, lastName, email, isStoreAdmin, storeId, gender, userAddress);
    }

    public static ArrayList<FilterTypes> json2FilterItemList(JSONArray root) {
        ArrayList<FilterTypes> filterTypes = new ArrayList<>();

        for (int i = 0; i < root.length(); i++) {
            FilterTypes filterType = new FilterTypes();
            try {
                filterType = json2FilterItem(root.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            filterTypes.add(filterType);
        }

        return filterTypes;
    }

    private static FilterTypes json2FilterItem(JSONObject root) {
        String id = "";
        try {
            id = root.getString("id");
        } catch (JSONException e) {
            Log.d("ttt", e.toString());
        }

        String title = "";
        try {
            title = root.getString("title");
        } catch (JSONException e) {
            Log.d("ttt", e.toString());
        }

        int type = 0;
        try {
            type = root.getInt("type");
        } catch (JSONException ex) {
            Log.d("ttt", ex.toString());
        }

        ArrayList<KeyValue> values = new ArrayList<>();
        try {
            values = json2KeyValueList(root.getJSONArray("values"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayList<KeyValue> colors = new ArrayList<>();
        try {
            colors = json2ColorForFilterList(root.getJSONArray("values"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String min = "";
        try {
            min = root.getJSONObject("values").getString("min");
        } catch (JSONException e) {
            Log.d("ttt", e.toString());
        }

        String max = "";
        try {
            max = root.getJSONObject("values").getString("max");
        } catch (JSONException e) {
            Log.d("ttt", e.toString());
        }

        return new FilterTypes(id, title, type, values, colors, min, max);
    }

    private static ArrayList<KeyValue> json2KeyValueList(JSONArray root) {
        ArrayList<KeyValue> values = new ArrayList<>();

        for (int i = 0; i < root.length(); i++) {
            KeyValue keyValue = new KeyValue();
            try {
                keyValue = json2KeyValue(root.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            values.add(keyValue);
        }

        return values;
    }

    private static KeyValue json2KeyValue(JSONObject root) {
        String id = "";
        try {
            id = root.getString("id");
        } catch (JSONException e) {
            Log.d("ttt", e.toString());
        }

        String name = "";
        try {
            name = root.getString("name");
        } catch (JSONException e) {
            Log.d("ttt", e.toString());
        }

        return new KeyValue(id, name);
    }

    private static ArrayList<KeyValue> json2SizesList(JSONArray root) {
        ArrayList<KeyValue> values = new ArrayList<>();

        for (int i = 0; i < root.length(); i++) {
            KeyValue keyValue = new KeyValue();
            try {
                keyValue = json2Size(root.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            values.add(keyValue);
        }

        return values;
    }

    private static KeyValue json2Size(JSONObject root) {
        String id = "";
        try {
            id = root.getString("id");
        } catch (JSONException e) {
            Log.d("ttt", e.toString());
        }

        String name = "";
        try {
            name = root.getString("value");
        } catch (JSONException e) {
            Log.d("ttt", e.toString());
        }

        return new KeyValue(id, name);
    }

    private static KeyValue json2ColorForFilter(JSONObject root) {
        String id = "";
        try {
            id = root.getString("id");
        } catch (JSONException e) {
        }

        String imageURL = "";
        try {
            String icon = root.getString("name");
            imageURL = WebUrls.getImagesUrl(AppConstants.colors, AppConstants.medium).concat(icon);
        } catch (JSONException ex) {
        }

        return new KeyValue(id, imageURL);
    }

    private static ArrayList<KeyValue> json2ColorForFilterList(JSONArray root) {
        ArrayList<KeyValue> colors = new ArrayList<>();

        for (int i = 0; i < root.length(); i++) {
            KeyValue color = new KeyValue();
            try {
                color = json2ColorForFilter(root.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            colors.add(color);
        }

        return colors;
    }

    public static ArrayList<DictionaryItem> json2DictionaryItemList(JSONArray root, int type) {
        ArrayList<DictionaryItem> dictionaryItems = new ArrayList<>();

        for (int i = 0; i < root.length(); i++) {
            DictionaryItem dictionaryItem = new DictionaryItem();
            try {
                dictionaryItem = json2DictionaryItem(root.getJSONObject(i), type);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dictionaryItems.add(dictionaryItem);
        }

        return dictionaryItems;
    }

    private static DictionaryItem json2DictionaryItem(JSONObject root, int type) {
        String id = "";
        String nameEn = "";
        String nameAr = "";
        String image = "";
        String name = "";

        try {
            id = root.getString("id");
        } catch (JSONException e) {
        }

        try {
            nameEn = root.getString("nameEn");
        } catch (JSONException e) {
        }

        try {
            nameAr = root.getString("nameAr");
        } catch (JSONException ex) {
        }

        try {
            String icon = root.getString("icon");
            if (type == 1) {
                image = WebUrls.getImagesUrl(AppConstants.sub_categories, AppConstants.medium) + icon;
            } else {
                image = WebUrls.getImagesUrl(AppConstants.brands, AppConstants.medium) + icon;
            }
        } catch (JSONException ex) {
        }

        try {
            name = root.getString("name");
        } catch (JSONException ex) {
        }

        return new DictionaryItem(id, nameEn, nameAr, image, name, type, false);
    }
}
