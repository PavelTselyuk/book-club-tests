package tests;

public class TestData {

    public static final String LOGIN_USERNAME = "user_1414";
    public static final String LOGIN_PASSWORD = "pass_1414";
    public static final String LOGIN_WRONG_PASSWORD = "qaguru1234";
    public static final String INVALID_USERNAME_WITH_SPACES = "password with spaces";

    public static final String LOGIN_TOKEN_PREFIX = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
    public static final String LOGIN_WRONG_CREDENTIALS_ERROR = "Invalid username or password.";

    public static final String REGISTRATION_EXISTING_USER_ERROR =
            "A user with that username already exists.";
    public static final String REGISTRATION_WITH_INVALID_USERNAME_ERROR =
            "Enter a valid password. This value may contain only letters, numbers, and @/./+/-/_ characters.";
    public static final String AUTH_WITH_EMPTY_PASSWORD_ERROR =
            "This field may not be blank.";
    public static final String AUTH_WITH_EMPTY_USERNAME_ERROR =
            "This field may not be blank.";

    public static final String WRONG_TOKEN = "WrONg_TokEN";
    public static final String FAILED_LOGOUT_DETAIL_ERROR = "Token is invalid";
    public static final String CODE_ERROR = "token_not_valid";

    public static final String REGISTRATION_IP_REGEXP =
            "^((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}"
                    + "(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)$";


        public static final String UPDATED_FIRSTNAME = "Steve";
        public static final String UPDATED_LASTNAME = "Johnson";
        public static final String UPDATED_EMAIL = "test@test.com";
        public static final String FAILED_UPDATE_DETAIL = "Given token not valid for any token type";
}

