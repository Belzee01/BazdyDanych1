package sample;

import lombok.Data;

@Data
public class ContextCatcher {
    static ACCOUNT_TYPE account_type = null;

    static Integer companyId = null;

    static Integer reportId = null;

    static Boolean error = false;

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

    public static Integer getReportId() {
        return reportId;
    }

    public static void setReportId(Integer reportId) {
        ContextCatcher.reportId = reportId;
    }

    public static Boolean getError() {
        return error;
    }

    public static void setError(Boolean error) {
        ContextCatcher.error = error;
    }
}
