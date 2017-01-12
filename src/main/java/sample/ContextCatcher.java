package sample;

import lombok.Data;

@Data
public class ContextCatcher {
    static ACCOUNT_TYPE account_type = null;

    static Integer companyId = null;

    public static void setAccountType(ACCOUNT_TYPE type) {
        account_type = type;
    }

    public static ACCOUNT_TYPE getAccountType() {
        return account_type;
    }

    public static Integer getCompanyId() {
        return companyId;
    }

    public static void setCompanyId(Integer companyId) {
        ContextCatcher.companyId = companyId;
    }
}
