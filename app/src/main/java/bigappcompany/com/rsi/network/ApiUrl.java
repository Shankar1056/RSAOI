package bigappcompany.com.rsi.network;

public final class ApiUrl {
        private static final String BASE_URL = "http://www.rsaoibangalore.in/api/rsi/";
        public static final String API_KEY = "rsi8197028387";
        public static final String SLIDERS = BASE_URL + "sliders/" + API_KEY+"/";
        public static final String IMG_TYPES = BASE_URL + "image_types/" + API_KEY;
        public static final String IMAGES = BASE_URL + "images/" + API_KEY;
        public static final String PDF = BASE_URL + "getAllPdfs/" + API_KEY;
        public static final String EVENTS = BASE_URL + "newsNnotifications/" + API_KEY+"NOTIFICATIONS";
        public static final String BOTH = BASE_URL + "newsNnotifications/" + API_KEY+"/BOTH";
        public static final String VERIFY = BASE_URL + "userVerify";
        public static final String facilities = BASE_URL + "facilityType/"+API_KEY;
        public static final String FACILITY_LIST = BASE_URL + "facilitylist/"+API_KEY;
        public static final String FACILITY_DESC = BASE_URL + "facilityDescription/"+API_KEY;
        public static final String BOOK = BASE_URL + "facilityBooking";
        public static final String BK_HIST = BASE_URL + "getCustomerBookingList/"+API_KEY;
        public static final String BK_DETAILS = BASE_URL + "getCustomerBookingList/"+API_KEY;
        public static final String BK_CANCEL = BASE_URL + "cancelBooking/"+API_KEY;
        public static final String ABOUT_US = BASE_URL + "aboutus/"+API_KEY;
        public static final String CONTACT_US = BASE_URL + "contactus/"+API_KEY;
        public static final String PAYMENT = "http://dev.spotsoon.com/rsi/payment/InitiatePayment";
        public static final String NOTIFICATIONS = BASE_URL + "view_push_notification/" + API_KEY;
        public static final String PAY_BILL = BASE_URL + "getUserBill/" + API_KEY;
        public static final String PAY_DESCRIPTION = BASE_URL + "getUserBillDescription/" + API_KEY;
        public static final String PAYMENT_WEBVIEW = "http://rsaoibangalore.in/payment/XmlBillPaymentInitiate/";
        public static final String RECHARGE = "http://rsi:rsi8197028387@rsaoibangalore.in/api/rsi/SetUserOtherBills";
        public static final String RECHARGE_WEBVIEW = "http://rsaoibangalore.in/payment/otherBillPayment/";
        public static final String GETNOTIFICATIONCOUNT = BASE_URL+"notificationReadCount/";
        public static final String NOTIFICATIONREAD = BASE_URL+"changeNotificationStatus";
        public static final String GETUSERDETAILS = BASE_URL+"getUserDetails/?apikey=rsi8197028387&userId=";
        public static final String FEEDBACK = BASE_URL+"saveFeedbacks";
        
        
        private String unp="rsi:rsi8197028387";

        public String getUnp() {
                return unp;
        }
        
}
